define(['filer', 'async', 'log', 'content', 'domparser'],
function(Filer, Async, Log, Content) {

  var Path = Filer.Path;
  var watchList = {};

  /**
   * Open, Write to the document stream, and Close.
   */
  function _writeMarkup(markup) {
    // Stash our require env on the document object so it survies the window
    // being recreated. The nohost.js shim uses it to acquire shared resources
    // (e.g., filesystem) that are already loaded.
    document.require = require;
    document.documentElement.innerHTML = markup;
  }

  /**
   * Read a file and maybe setup a watch for changes to trigger a reload.
   */
  function _readFile(fs, path, encoding, callback) {
    fs.readFile(path, encoding, function(err, data) {
      if(err) {
        return callback(err);
      }

      watchList[path] = true;
      callback(null, data);
    });
  }

  /**
   * Given an HTML string, rewrite it with inline resources
   */
  function _processHTML(html, path, fs, callback) {
    var dir = Path.dirname(path);
    var parser = new DOMParser();
    var doc;

    function rewriteElements(type, urlType, mime, callback) {
      var elems = doc.querySelectorAll(type);

      Async.eachSeries(elems, function(elem, cb) {
        // Skip any links for protocols (we only want relative paths)
        var url = elem.getAttribute(urlType);
        if(!url || /\:?\/\//.test(url) || /\s*data\:/.test(url)) {
          return cb();
        }

        var path = Path.resolve(dir, url);
        fs.exists(path, function(found) {
          if(!found) {
            return cb();
          }

          _readFile(fs, path, null, function(err, data) {
            if(err) {
              return cb(err);
            }
            mime = mime || Content.mimeFromExt(Path.extname(path));
            elem[urlType] = Content.toDataURL(data, mime);
            cb();
          });
        });
      }, function(err) {
        if(err) {
          Log.error(err);
        }
        callback();
      });
    }

    // Turn this html into a DOM, process it, then go back to html and write
    doc = parser.parseFromString(html, 'text/html');

    // Replace links with file contents from fs
    Async.series([
      function anchors(callback) {
        var elems = doc.querySelectorAll('a');
        var elem;

        for(var i = 0; i < elems.length; i++) {
          elem = elems[i];
          var url = elem.getAttribute('href');
          if(!url || /\:?\/\//.test(url) || /\s*data\:/.test(url)) {
            continue;
          }
          elem.href = '?' + Path.join(dir, url);
        }

        callback();
      },
      function links(callback) {
        var elems = doc.querySelectorAll('link');

        Async.eachSeries(elems, function(elem, cb) {
          // Skip any links for protocols (we only want relative paths)
          var url = elem.getAttribute('href');
          if(!url || /\:?\/\//.test(url) || /\s*data\:/.test(url)) {
            return cb();
          }

          var path = Path.resolve(dir, url);
          fs.exists(path, function(found) {
            if(!found) {
              return cb();
            }

            _readFile(fs, path, 'utf8', function(err, data) {
              if(err) {
                return cb(err);
              }

              _processCSS(data, path, fs, function(err, css) {
                elem.href = Content.toDataURL(data, 'text/css');
                cb();
              });
            });
          });
        }, function(err) {
          if(err) {
            Log.error(err);
          }
          callback();
        });
      },
      function iframes(callback) {
        var elems = doc.querySelectorAll('iframe');

        Async.eachSeries(elems, function(elem, cb) {
          // Skip any links for protocols (we only want relative paths)
          var url = elem.getAttribute('src');
          if(!url || /\:?\/\//.test(url) || /\s*data\:/.test(url)) {
            return cb();
          }

          var path = Path.resolve(dir, url);
          fs.exists(path, function(found) {
            if(!found) {
              return cb();
            }

            _readFile(fs, path, 'utf8', function(err, data) {
              if(err) {
                return cb(err);
              }

              _processHTML(data, path, fs, function(err, html) {
                elem.src = Content.toDataURL(data, 'text/html');
                cb();
              });
            });
          });
        }, function(err) {
          if(err) {
            Log.error(err);
          }
          callback();
        });
      },
      function inlineStyles(callback) {
        var elems = doc.querySelectorAll('style');

        Async.eachSeries(elems, function(elem, cb) {
          var content = elem.innerHTML;
          if(!content) {
            cb();
            return;
          }

          _processCSS(content, path, fs, function(err, css) {
            if(err) {
              Log.error(err);
            }
            elem.innerHTML = css;
            cb();
          });
        }, function(err) {
          if(err) {
            Log.error(err);
          }
          callback();
        });
      },
      function styleAttributes(callback) {
        var elems = doc.querySelectorAll('[style]');

        Async.eachSeries(elems, function(elem, cb) {
          var content = elem.getAttribute('style');
          if(!content) {
            cb();
            return;
          }

          _processCSS(content, path, fs, function(err, css) {
            if(err) {
              Log.error(err);
            }
            elem.setAttribute('style', css);
            cb();
          });
        }, function(err) {
          if(err) {
            Log.error(err);
          }
          callback();
        });

      },
      function imgs(callback) {
        rewriteElements('img', 'src', null, callback);
      },
      function scripts(callback) {
        rewriteElements('script', 'src', 'text/javascript', callback);
      },
      function sources(callback) {
        rewriteElements('source', 'src', null, callback);
      },
      function videos(callback) {
        rewriteElements('video', 'src', null, callback);
      },
      function audios(callback) {
        rewriteElements('audio', 'src', null, callback);
      },
      function injectNohost(callback) {
        // Inject nohost scripts and shims to override native behaviour
        var head = doc.getElementsByTagName('head')[0];
        var nohostScript = doc.createElement('script');
        var nohostWatchlist = Object.keys(watchList).toString();

        nohostScript.src = 'shims/nohost.js';
        nohostScript.setAttribute('data-nohost-watchlist', nohostWatchlist);
        head.insertBefore(nohostScript, head.firstChild);
        callback();
      }
    ], function(err, result) {
      // Return the processed HTML
      callback(null, doc.documentElement.innerHTML);
    });
  }

  /**
   * Given a CSS string, rewrite it to include external resources (imports, url())
   */
  function _processCSS(css, path, fs, callback) {
    var dir = Path.dirname(path);

    // Do a two stage pass of the css content, replacing all interesting url(...)
    // uses with the contents of files in the server root.
    // Thanks to Pomax for helping with this
    function aggregate(content, callback) {
      var urls = [];

      function fetch(input, replacements, next) {
        if(input.length === 0) {
          return next(false, replacements);
        }

        var filename = input.splice(0,1)[0];
        _readFile(fs, Path.resolve(dir, filename), null, function(err, data) {
          if(err) {
            return next("failed on " + path, replacements);
          }

          // Queue a function to do the replacement in the second pass
          replacements.push(function(content) {
            // Swap the filename with the contents of the file
            var filenameCleaned = filename.replace(/\./g, '\\.').replace(/\//g, '\\/');
            var regex = new RegExp(filenameCleaned, 'gm');
            var mime = Content.mimeFromExt(Path.extname(filename));
            return content.replace(regex, Content.toDataURL(data, mime));
          });
          fetch(input, replacements, next);
        });
      }

      function fetchFiles(list, next) {
        fetch(list, [], next);
      }

      content.replace(/url\(['"]?([^'"\)]+)['"]?\)/g, function(_, url) {
        if(!url || /\:?\/\//.test(url) || /\s*data\:/.test(url)) {
          return;
        }
        urls.push(url);
      });
      fetchFiles(urls, callback);
    }

    aggregate(css, function(err, replacements) {
      if(err) {
        callback(err);
        return;
      }
      replacements.forEach(function(replacement) {
        css = replacement(css);
      });
      callback(null, css);
    });
  }

  /**
   * Send an Apache-style 404
   */
  function handle404(url) {
    var html = '<!DOCTYPE html>' +
          '<html><head>' +
          '<title>404 Not Found</title>' +
          '</head><body>' +
          '<h1>Not Found</h1>' +
          '<p>The requested URL ' + url + ' was not found on this server.</p>' +
          '<hr>' +
          '<address>nohost/0.0.1 (Web) Server</address>' +
          '</body></html>';
    _writeMarkup(html);
  }

  /**
   * Synthesize a document for images
   */
  function handleImage(path, fs) {
    var syntheticDoc = '<!DOCTYPE html>' +
          '<html><head>' +
          '<title>' + path + '</title>' +
          '<style>' +
          '  /* based on http://dxr.mozilla.org/mozilla-central/source/layout/style/TopLevelImageDocument.css */' +
          '  @media not print {' +
          '    body {' +
          '      margin: 0;' +
          '    }' +
          '    img {' +
          '      text-align: center;' +
          '      position: absolute;' +
          '      margin: auto;' +
          '      top: 0;' +
          '      right: 0;' +
          '      bottom: 0;' +
          '      left: 0;' +
          '    }' +
          '  }' +
          '  img {' +
          '    image-orientation: from-image;' +
          '  }' +
          '</style></head><body>' +
          '<img src="' + path + '"></body></html>';
    _processHTML(syntheticDoc, path, fs, function(err, html) {
      if(err) {
        Log.error('unable to read `' + path + '`');
        handle404(path);
        return;
      }
      _writeMarkup(html);
    });
  }

  /**
   * Synthesize a document for media
   */
  function handleMedia(path, fs) {
    var syntheticDoc = '<!DOCTYPE html>' +
          '<html><head>' +
          '<title>' + path + '</title>' +
          '<style>' +
          '  /* based on http://dxr.mozilla.org/mozilla-central/source/layout/style/TopLevelVideoDocument.css */' +
          '  body {' +
          '    height: 100%;' +
          '    width: 100%;' +
          '    margin: 0;' +
          '    padding: 0;' +
          '  }' +
          '  video {' +
          '    position: absolute;' +
          '    top: 0;' +
          '    right: 0;' +
          '    bottom: 0;' +
          '    left: 0;' +
          '    margin: auto;' +
          '    max-width: 100%;' +
          '    max-height: 100%;' +
          '  }' +
          '  video:focus {' +
          '    outline-width: 0;' +
          '  }' +
          '</style></head><body>' +
          '<video src="' + path + '" controls></video></body></html>';
    _processHTML(syntheticDoc, path, fs, function(err, html) {
      if(err) {
        Log.error('unable to read `' + path + '`');
        handle404(path);
        return;
      }
      _writeMarkup(html);
    });
  }

  /**
   * Send the raw file, making it somewhat more readable
   */
  function handleFile(path, fs) {
    _readFile(fs, path, 'utf8', function(err, data) {
      if(err) {
        Log.error('unable to read `' + path + '`');
        handle404(path);
        return;
      }

      // Escape the file a bit for inclusing in <pre>...</pre>
      data = data.replace(/</gm, '&lt;')
        .replace(/>/gm, '&gt;')
        .replace(/&/gm, '&amp;');

      var syntheticDoc = '<!DOCTYPE html>' +
            '<html><head></head>' +
            '<body><pre>' + data + '</pre></body></html>';
      _writeMarkup(syntheticDoc);
    });
  }

  /**
   * Send an Apache-style directory listing
   */
  function handleDir(path, fs) {
    var sh = fs.Shell();
    var parent = Path.dirname(path);

    var header = '<!DOCTYPE html>' +
          '<html><head><title>Index of ' + path + '</title></head>' +
          '<body><h1>Index of ' + path + '</h1>' +
          '<table><tr><th><img src="icons/blank.png" alt="[ICO]"></th>' +
          '<th><a href="#">Name</a></th><th><a href="#">Last modified</a></th>' +
          '<th><a href="#">Size</a></th><th><a href="#">Description</a></th></tr>' +
          '<tr><th colspan="5"><hr></th></tr>' +
          '<tr><td valign="top"><img src="icons/back.png" alt="[DIR]"></td>' +
          '<td><a href="?' + parent + '">Parent Directory</a>       </td><td>&nbsp;</td>' +
          '<td align="right">  - </td><td>&nbsp;</td></tr>';

    var footer = '<tr><th colspan="5"><hr></th></tr>' +
          '</table><address>nohost/0.0.1 (Web)</address>' +
          '</body></html>';

    function formatDate(d) {
      // 20-Apr-2004 17:14
      return d.getDay() + '-' +
        d.getMonth() + '-' +
        d.getFullYear() + ' ' +
        d.getHours() + ':' +
        d.getMinutes();
    }

    function formatSize(s) {
      var units = ['', 'K', 'M'];
      if(!s) {
        return '-';
      }
      var i = (Math.floor(Math.log(s) / Math.log(1024)))|0;
      return Math.round(s / Math.pow(1024, i), 2) + units[i];
    }

    function row(icon, alt, href, name, modified, size) {
      icon = icon || 'icons/unknown.png';
      alt = alt || '[   ]';
      modified = formatDate(new Date(modified));
      size = formatSize(size);

      return '<tr><td valign="top"><img src="' + icon + '" alt="' + alt + '"></td><td>' +
        '<a href="' + href + '">' + name + '</a>             </td>' +
        '<td align="right">' + modified + '  </td>' +
        '<td align="right">' + size + '</td><td>&nbsp;</td></tr>';
    }

    function processEntries(entries) {
      var rows = '';
      entries.forEach(function(entry) {
        var name = Path.basename(entry.path);
        var ext = Path.extname(entry.path);
        var href = '?' + Path.join(path, entry.path);
        var icon;
        var alt;

        if(entry.type === 'DIRECTORY') {
          icon = 'icons/folder.png';
          alt = '[DIR]';
        } else { // file
          if(Content.isImage(ext)) {
            icon = 'icons/image2.png';
            alt = '[IMG]';
          } else if(Content.isMedia(ext)) {
            icon = 'icons/movie.png';
            alt = '[MOV]';
          } else {
            icon = 'icons/text.png';
            alt = '[TXT]';
          }
        }
        rows += row(icon, alt, href, name, entry.modified, entry.size);
      });

      _writeMarkup(header + rows + footer);
    }

    sh.ls(path, function(err, list) {
      if(err) {
        handle404(path);
        return;
      }
      processEntries(list);
    });
  }

  /**
   * HTML files need to have external resources inlined
   */
  function handleHTML(path, fs) {
    _readFile(fs, path, 'utf8', function(err, html) {
      if(err) {
        Log.error('unable to read `' + path + '`');
        handle404(path);
        return;
      }

      _processHTML(html, path, fs, function(err, html) {
        if(err) {
          Log.error('unable to read `' + path + '`');
          handle404(path);
          return;
        }
        _writeMarkup(html);
      });
    });
  }


  return {
    handle404: handle404,
    handleImage: handleImage,
    handleMedia: handleMedia,
    handleFile: handleFile,
    handleDir: handleDir,
    handleHTML: handleHTML
  };

});

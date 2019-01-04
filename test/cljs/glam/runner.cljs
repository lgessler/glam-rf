(ns glam.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [glam.core-test]))

(doo-tests 'glam.core-test)

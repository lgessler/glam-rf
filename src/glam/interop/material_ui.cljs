(ns glam.interop.material-ui
  (:require
   [cljs.analyzer.api :refer [ns-resolve]]
   [reagent.core :as r]
   ["@material-ui/core" :as mui]
   ["@material-ui/core/styles" :refer [withStyles createMuiTheme]]
   ["@material-ui/core/colors/blue" :default blue]
   ["@material-ui/core/colors/green" :default green]
   ["@material-ui/icons" :as mui-icons]))


;; there are too many icons to list and adapt manually, so assume
;; that everything in the @material-ui/icons is a valid icon component
;; and store the adapted components in icons
(def ^:private icons
  (let [map-v (fn [f coll]
                (reduce-kv (fn [m k v]
                             (assoc m k (f v)))
                           (empty coll)
                           coll))]
    (map-v (fn [v]
             (r/adapt-react-class v))
           (js->clj mui-icons))))

(defn get-icon
  [name]
  (get icons name))

;; use mui withStyles higher order component on a component: this generates
;; CSS classes for the provided styles and provides the class names in the
;; :classes key of the component's props, where the associated value is a
;; map from class name to css class name. Note that because of interop the
;; end result is that the outer-level key is a clojure keyword, but the
;; inner-level key is a string.
(defn get-styled-component
  [styles reagent-component]
  (let [styles-decorator (withStyles styles)]
    (-> reagent-component
        r/reactify-component
        styles-decorator
        r/adapt-react-class)))

(defn- get-class-name
  "Given props from a component wrapped with style-component, retrieve the
  CSS class name corresponding to a name."
  [props name]
  (-> props :classes (aget name)))

(def ^:export default-theme
  (createMuiTheme
   (clj->js
    {:palette
     {:primary {:light (aget green "300")
                :main (aget green "500")
                :dark (aget green "700")}
      :secondary {:light (aget blue "300")
                  :main (aget blue "500")
                  :dark (aget blue "700")}}})))

(def ^:export create-mui-theme createMuiTheme)

;; wrappers for mui classes
(def ^:export app-bar (r/adapt-react-class mui/AppBar))
(def ^:export avatar (r/adapt-react-class mui/Avatar))
(def ^:export backdrop (r/adapt-react-class mui/Backdrop))
(def ^:export badge (r/adapt-react-class mui/Badge))
(def ^:export bottom-navigation (r/adapt-react-class mui/BottomNavigation))
(def ^:export bottom-navigation-action (r/adapt-react-class mui/BottomNavigationAction))
(def ^:export button (r/adapt-react-class mui/Button))
(def ^:export button-base (r/adapt-react-class mui/ButtonBase))
(def ^:export card (r/adapt-react-class mui/Card))
(def ^:export card-action-area (r/adapt-react-class mui/CardActionArea))
(def ^:export card-actions (r/adapt-react-class mui/CardActions))
(def ^:export card-content (r/adapt-react-class mui/CardContent))
(def ^:export card-header (r/adapt-react-class mui/CardHeader))
(def ^:export card-media (r/adapt-react-class mui/CardMedia))
(def ^:export checkbox (r/adapt-react-class mui/Checkbox))
(def ^:export chip (r/adapt-react-class mui/Chip))
(def ^:export circular-progress (r/adapt-react-class mui/CircularProgress))
(def ^:export click-away-listener (r/adapt-react-class mui/ClickAwayListener))
(def ^:export collapse (r/adapt-react-class mui/Collapse))
(def ^:export css-baseline (r/adapt-react-class mui/CssBaseline))
(def ^:export dialog (r/adapt-react-class mui/Dialog))
(def ^:export dialog-actions (r/adapt-react-class mui/DialogActions))
(def ^:export dialog-content (r/adapt-react-class mui/DialogContent))
(def ^:export dialog-content-text (r/adapt-react-class mui/DialogContentText))
(def ^:export dialog-title (r/adapt-react-class mui/DialogTitle))
(def ^:export divider (r/adapt-react-class mui/Divider))
(def ^:export drawer (r/adapt-react-class mui/Drawer))
(def ^:export expansion-panel (r/adapt-react-class mui/ExpansionPanel))
(def ^:export expansion-panel-actions (r/adapt-react-class mui/ExpansionPanelActions))
(def ^:export expansion-panel-details (r/adapt-react-class mui/ExpansionPanelDetails))
(def ^:export expansion-panel-summary (r/adapt-react-class mui/ExpansionPanelSummary))
(def ^:export fab (r/adapt-react-class mui/Fab))
(def ^:export fade (r/adapt-react-class mui/Fade))
(def ^:export filled-input (r/adapt-react-class mui/FilledInput))
(def ^:export form-control (r/adapt-react-class mui/FormControl))
(def ^:export form-control-label (r/adapt-react-class mui/FormControlLabel))
(def ^:export form-group (r/adapt-react-class mui/FormGroup))
(def ^:export form-helper-text (r/adapt-react-class mui/FormHelperText))
(def ^:export form-label (r/adapt-react-class mui/FormLabel))
(def ^:export grid (r/adapt-react-class mui/Grid))
(def ^:export grid-list (r/adapt-react-class mui/GridList))
(def ^:export grid-list-tile (r/adapt-react-class mui/GridListTile))
(def ^:export grid-list-tile-bar (r/adapt-react-class mui/GridListTileBar))
(def ^:export grow (r/adapt-react-class mui/Grow))
(def ^:export hidden (r/adapt-react-class mui/Hidden))
(def ^:export icon (r/adapt-react-class mui/Icon))
(def ^:export icon-button (r/adapt-react-class mui/IconButton))
(def ^:export input (r/adapt-react-class mui/Input))
(def ^:export input-adornment (r/adapt-react-class mui/InputAdornment))
(def ^:export input-base (r/adapt-react-class mui/InputBase))
(def ^:export input-label (r/adapt-react-class mui/InputLabel))
(def ^:export linear-progress (r/adapt-react-class mui/LinearProgress))
(def ^:export link (r/adapt-react-class mui/Link))
;; name conflict with cljs.core/list
(def ^:export list-base (r/adapt-react-class mui/List))
(def ^:export list-item (r/adapt-react-class mui/ListItem))
(def ^:export list-item-avatar (r/adapt-react-class mui/ListItemAvatar))
(def ^:export list-item-icon (r/adapt-react-class mui/ListItemIcon))
(def ^:export list-item-secondary-action (r/adapt-react-class mui/ListItemSecondaryAction))
(def ^:export list-item-text (r/adapt-react-class mui/ListItemText))
(def ^:export list-subheader (r/adapt-react-class mui/ListSubheader))
(def ^:export menu (r/adapt-react-class mui/Menu))
(def ^:export menu-item (r/adapt-react-class mui/MenuItem))
(def ^:export menu-list (r/adapt-react-class mui/MenuList))
(def ^:export mobile-stepper (r/adapt-react-class mui/MobileStepper))
(def ^:export modal (r/adapt-react-class mui/Modal))
(def ^:export modal-manager (r/adapt-react-class mui/ModalManager))
(def ^:export mui-theme-provider (r/adapt-react-class mui/MuiThemeProvider))
(def ^:export native-select (r/adapt-react-class mui/NativeSelect))
(def ^:export no-ssr (r/adapt-react-class mui/NoSsr))
(def ^:export outlined-input (r/adapt-react-class mui/OutlinedInput))
(def ^:export paper (r/adapt-react-class mui/Paper))
(def ^:export popover (r/adapt-react-class mui/Popover))
(def ^:export popper (r/adapt-react-class mui/Popper))
(def ^:export portal (r/adapt-react-class mui/Portal))
(def ^:export radio (r/adapt-react-class mui/Radio))
(def ^:export radio-group (r/adapt-react-class mui/RadioGroup))
(def ^:export root-ref (r/adapt-react-class mui/RootRef))
(def ^:export select (r/adapt-react-class mui/Select))
(def ^:export slide (r/adapt-react-class mui/Slide))
(def ^:export snackbar (r/adapt-react-class mui/Snackbar))
(def ^:export snackbar-content (r/adapt-react-class mui/SnackbarContent))
(def ^:export step (r/adapt-react-class mui/Step))
(def ^:export step-button (r/adapt-react-class mui/StepButton))
(def ^:export step-connector (r/adapt-react-class mui/StepConnector))
(def ^:export step-content (r/adapt-react-class mui/StepContent))
(def ^:export step-icon (r/adapt-react-class mui/StepIcon))
(def ^:export step-label (r/adapt-react-class mui/StepLabel))
(def ^:export stepper (r/adapt-react-class mui/Stepper))
(def ^:export svg-icon (r/adapt-react-class mui/SvgIcon))
(def ^:export swipeable-drawer (r/adapt-react-class mui/SwipeableDrawer))
(def ^:export switch (r/adapt-react-class mui/Switch))
(def ^:export tab (r/adapt-react-class mui/Tab))
(def ^:export table (r/adapt-react-class mui/Table))
(def ^:export table-body (r/adapt-react-class mui/TableBody))
(def ^:export table-cell (r/adapt-react-class mui/TableCell))
(def ^:export table-footer (r/adapt-react-class mui/TableFooter))
(def ^:export table-head (r/adapt-react-class mui/TableHead))
(def ^:export table-pagination (r/adapt-react-class mui/TablePagination))
(def ^:export table-row (r/adapt-react-class mui/TableRow))
(def ^:export table-sort-label (r/adapt-react-class mui/TableSortLabel))
(def ^:export tabs (r/adapt-react-class mui/Tabs))
(def ^:export text-field (r/adapt-react-class mui/TextField))
(def ^:export toolbar (r/adapt-react-class mui/Toolbar))
(def ^:export tooltip (r/adapt-react-class mui/Tooltip))
(def ^:export typography (r/adapt-react-class mui/Typography))
(def ^:export zoom (r/adapt-react-class mui/Zoom))

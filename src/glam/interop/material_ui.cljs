(ns glam.interop.material-ui
  (:require
   [clojure.walk :refer [prewalk]]
   [cljs.analyzer.api :refer [ns-resolve]]
   [reagent.core :as r]
   ["@material-ui/core/AppBar" :default AppBar]
   ["@material-ui/core/Avatar" :default Avatar]
   ["@material-ui/core/Backdrop" :default Backdrop]
   ["@material-ui/core/Badge" :default Badge]
   ["@material-ui/core/BottomNavigation" :default BottomNavigation]
   ["@material-ui/core/BottomNavigationAction" :default BottomNavigationAction]
   ["@material-ui/core/Button" :default Button]
   ["@material-ui/core/ButtonBase" :default ButtonBase]
   ["@material-ui/core/Card" :default Card]
   ["@material-ui/core/CardActionArea" :default CardActionArea]
   ["@material-ui/core/CardActions" :default CardActions]
   ["@material-ui/core/CardContent" :default CardContent]
   ["@material-ui/core/CardHeader" :default CardHeader]
   ["@material-ui/core/CardMedia" :default CardMedia]
   ["@material-ui/core/Checkbox" :default Checkbox]
   ["@material-ui/core/Chip" :default Chip]
   ["@material-ui/core/CircularProgress" :default CircularProgress]
   ["@material-ui/core/ClickAwayListener" :default ClickAwayListener]
   ["@material-ui/core/Collapse" :default Collapse]
   ["@material-ui/core/CssBaseline" :default CssBaseline]
   ["@material-ui/core/Dialog" :default Dialog]
   ["@material-ui/core/DialogActions" :default DialogActions]
   ["@material-ui/core/DialogContent" :default DialogContent]
   ["@material-ui/core/DialogContentText" :default DialogContentText]
   ["@material-ui/core/DialogTitle" :default DialogTitle]
   ["@material-ui/core/Divider" :default Divider]
   ["@material-ui/core/Drawer" :default Drawer]
   ["@material-ui/core/ExpansionPanel" :default ExpansionPanel]
   ["@material-ui/core/ExpansionPanelActions" :default ExpansionPanelActions]
   ["@material-ui/core/ExpansionPanelDetails" :default ExpansionPanelDetails]
   ["@material-ui/core/ExpansionPanelSummary" :default ExpansionPanelSummary]
   ["@material-ui/core/Fab" :default Fab]
   ["@material-ui/core/Fade" :default Fade]
   ["@material-ui/core/FilledInput" :default FilledInput]
   ["@material-ui/core/FormControl" :default FormControl]
   ["@material-ui/core/FormControlLabel" :default FormControlLabel]
   ["@material-ui/core/FormGroup" :default FormGroup]
   ["@material-ui/core/FormHelperText" :default FormHelperText]
   ["@material-ui/core/FormLabel" :default FormLabel]
   ["@material-ui/core/Grid" :default Grid]
   ["@material-ui/core/GridList" :default GridList]
   ["@material-ui/core/GridListTile" :default GridListTile]
   ["@material-ui/core/GridListTileBar" :default GridListTileBar]
   ["@material-ui/core/Grow" :default Grow]
   ["@material-ui/core/Hidden" :default Hidden]
   ["@material-ui/core/Icon" :default Icon]
   ["@material-ui/core/IconButton" :default IconButton]
   ["@material-ui/core/Input" :default Input]
   ["@material-ui/core/InputAdornment" :default InputAdornment]
   ["@material-ui/core/InputBase" :default InputBase]
   ["@material-ui/core/InputLabel" :default InputLabel]
   ["@material-ui/core/LinearProgress" :default LinearProgress]
   ["@material-ui/core/Link" :default Link]
   ["@material-ui/core/List" :default List]
   ["@material-ui/core/ListItem" :default ListItem]
   ["@material-ui/core/ListItemAvatar" :default ListItemAvatar]
   ["@material-ui/core/ListItemIcon" :default ListItemIcon]
   ["@material-ui/core/ListItemSecondaryAction" :default ListItemSecondaryAction]
   ["@material-ui/core/ListItemText" :default ListItemText]
   ["@material-ui/core/ListSubheader" :default ListSubheader]
   ["@material-ui/core/Menu" :default Menu]
   ["@material-ui/core/MenuItem" :default MenuItem]
   ["@material-ui/core/MenuList" :default MenuList]
   ["@material-ui/core/MobileStepper" :default MobileStepper]
   ["@material-ui/core/Modal" :default Modal]
   ["@material-ui/core/NativeSelect" :default NativeSelect]
   ["@material-ui/core/NoSsr" :default NoSsr]
   ["@material-ui/core/OutlinedInput" :default OutlinedInput]
   ["@material-ui/core/Paper" :default Paper]
   ["@material-ui/core/Popover" :default Popover]
   ["@material-ui/core/Popper" :default Popper]
   ["@material-ui/core/Portal" :default Portal]
   ["@material-ui/core/Radio" :default Radio]
   ["@material-ui/core/RadioGroup" :default RadioGroup]
   ["@material-ui/core/RootRef" :default RootRef]
   ["@material-ui/core/Select" :default Select]
   ["@material-ui/core/Slide" :default Slide]
   ["@material-ui/core/Snackbar" :default Snackbar]
   ["@material-ui/core/SnackbarContent" :default SnackbarContent]
   ["@material-ui/core/Step" :default Step]
   ["@material-ui/core/StepButton" :default StepButton]
   ["@material-ui/core/StepConnector" :default StepConnector]
   ["@material-ui/core/StepContent" :default StepContent]
   ["@material-ui/core/StepIcon" :default StepIcon]
   ["@material-ui/core/StepLabel" :default StepLabel]
   ["@material-ui/core/Stepper" :default Stepper]
   ["@material-ui/core/SvgIcon" :default SvgIcon]
   ["@material-ui/core/SwipeableDrawer" :default SwipeableDrawer]
   ["@material-ui/core/Switch" :default Switch]
   ["@material-ui/core/Tab" :default Tab]
   ["@material-ui/core/Table" :default Table]
   ["@material-ui/core/TableBody" :default TableBody]
   ["@material-ui/core/TableCell" :default TableCell]
   ["@material-ui/core/TableFooter" :default TableFooter]
   ["@material-ui/core/TableHead" :default TableHead]
   ["@material-ui/core/TablePagination" :default TablePagination]
   ["@material-ui/core/TableRow" :default TableRow]
   ["@material-ui/core/TableSortLabel" :default TableSortLabel]
   ["@material-ui/core/Tabs" :default Tabs]
   ["@material-ui/core/TextField" :default TextField]
   ["@material-ui/core/Toolbar" :default Toolbar]
   ["@material-ui/core/Tooltip" :default Tooltip]
   ["@material-ui/core/Typography" :default Typography]
   ["@material-ui/core/Zoom" :default Zoom]
   #_["@material-ui/core" :as mui]
   ["@material-ui/core/styles" :refer [withStyles createMuiTheme MuiThemeProvider]]
   ["@material-ui/core/colors/blue" :default blue]
   ["@material-ui/core/colors/green" :default green]
   ["@material-ui/icons" :as mui-icons]))

;; use mui withStyles higher order component on a component: this generates
;; CSS classes for the provided styles and provides the class names in the
;; :classes key of the component's props, where the associated value is a
;; map from class name to css class name. Note that because of interop the
;; end result is that the outer-level key is a clojure keyword, but the
;; inner-level key is a string.
(defn styled-component
  [styles reagent-component]
  (let [styles-decorator (withStyles styles)]
    (-> reagent-component
        r/reactify-component
        styles-decorator
        r/adapt-react-class)))

(def ^:export default-theme
  (createMuiTheme
   #_(clj->js
    {:palette
     {:primary {:light (aget green "300")
                :main (aget green "500")
                :dark (aget green "700")}
      :secondary {:light (aget blue "300")
                  :main (aget blue "500")
                  :dark (aget blue "700")}}})))

(def ^:export create-mui-theme createMuiTheme)

;; wrappers for mui classes
(def ^:export app-bar (r/adapt-react-class AppBar))
(def ^:export avatar (r/adapt-react-class Avatar))
(def ^:export backdrop (r/adapt-react-class Backdrop))
(def ^:export badge (r/adapt-react-class Badge))
(def ^:export bottom-navigation (r/adapt-react-class BottomNavigation))
(def ^:export bottom-navigation-action (r/adapt-react-class BottomNavigationAction))
(def ^:export button (r/adapt-react-class Button))
(def ^:export button-base (r/adapt-react-class ButtonBase))
(def ^:export card (r/adapt-react-class Card))
(def ^:export card-action-area (r/adapt-react-class CardActionArea))
(def ^:export card-actions (r/adapt-react-class CardActions))
(def ^:export card-content (r/adapt-react-class CardContent))
(def ^:export card-header (r/adapt-react-class CardHeader))
(def ^:export card-media (r/adapt-react-class CardMedia))
(def ^:export checkbox (r/adapt-react-class Checkbox))
(def ^:export chip (r/adapt-react-class Chip))
(def ^:export circular-progress (r/adapt-react-class CircularProgress))
(def ^:export click-away-listener (r/adapt-react-class ClickAwayListener))
(def ^:export collapse (r/adapt-react-class Collapse))
(def ^:export css-baseline (r/adapt-react-class CssBaseline))
(def ^:export dialog (r/adapt-react-class Dialog))
(def ^:export dialog-actions (r/adapt-react-class DialogActions))
(def ^:export dialog-content (r/adapt-react-class DialogContent))
(def ^:export dialog-content-text (r/adapt-react-class DialogContentText))
(def ^:export dialog-title (r/adapt-react-class DialogTitle))
(def ^:export divider (r/adapt-react-class Divider))
(def ^:export drawer (r/adapt-react-class Drawer))
(def ^:export expansion-panel (r/adapt-react-class ExpansionPanel))
(def ^:export expansion-panel-actions (r/adapt-react-class ExpansionPanelActions))
(def ^:export expansion-panel-details (r/adapt-react-class ExpansionPanelDetails))
(def ^:export expansion-panel-summary (r/adapt-react-class ExpansionPanelSummary))
(def ^:export fab (r/adapt-react-class Fab))
(def ^:export fade (r/adapt-react-class Fade))
(def ^:export filled-input (r/adapt-react-class FilledInput))
(def ^:export form-control (r/adapt-react-class FormControl))
(def ^:export form-control-label (r/adapt-react-class FormControlLabel))
(def ^:export form-group (r/adapt-react-class FormGroup))
(def ^:export form-helper-text (r/adapt-react-class FormHelperText))
(def ^:export form-label (r/adapt-react-class FormLabel))
(def ^:export grid (r/adapt-react-class Grid))
(def ^:export grid-list (r/adapt-react-class GridList))
(def ^:export grid-list-tile (r/adapt-react-class GridListTile))
(def ^:export grid-list-tile-bar (r/adapt-react-class GridListTileBar))
(def ^:export grow (r/adapt-react-class Grow))
(def ^:export hidden (r/adapt-react-class Hidden))
(def ^:export icon (r/adapt-react-class Icon))
(def ^:export icon-button (r/adapt-react-class IconButton))
(def ^:export input (r/adapt-react-class Input))
(def ^:export input-adornment (r/adapt-react-class InputAdornment))
(def ^:export input-base (r/adapt-react-class InputBase))
(def ^:export input-label (r/adapt-react-class InputLabel))
(def ^:export linear-progress (r/adapt-react-class LinearProgress))
(def ^:export link (r/adapt-react-class Link))
;; name conflict with cljs.core/list
(def ^:export list-base (r/adapt-react-class List))
(def ^:export list-item (r/adapt-react-class ListItem))
(def ^:export list-item-avatar (r/adapt-react-class ListItemAvatar))
(def ^:export list-item-icon (r/adapt-react-class ListItemIcon))
(def ^:export list-item-secondary-action (r/adapt-react-class ListItemSecondaryAction))
(def ^:export list-item-text (r/adapt-react-class ListItemText))
(def ^:export list-subheader (r/adapt-react-class ListSubheader))
(def ^:export menu (r/adapt-react-class Menu))
(def ^:export menu-item (r/adapt-react-class MenuItem))
(def ^:export menu-list (r/adapt-react-class MenuList))
(def ^:export mobile-stepper (r/adapt-react-class MobileStepper))
(def ^:export modal (r/adapt-react-class Modal))
(def ^:export mui-theme-provider (r/adapt-react-class MuiThemeProvider))
(def ^:export native-select (r/adapt-react-class NativeSelect))
(def ^:export no-ssr (r/adapt-react-class NoSsr))
(def ^:export outlined-input (r/adapt-react-class OutlinedInput))
(def ^:export paper (r/adapt-react-class Paper))
(def ^:export popover (r/adapt-react-class Popover))
(def ^:export popper (r/adapt-react-class Popper))
(def ^:export portal (r/adapt-react-class Portal))
(def ^:export radio (r/adapt-react-class Radio))
(def ^:export radio-group (r/adapt-react-class RadioGroup))
(def ^:export root-ref (r/adapt-react-class RootRef))
(def ^:export select (r/adapt-react-class Select))
(def ^:export slide (r/adapt-react-class Slide))
(def ^:export snackbar (r/adapt-react-class Snackbar))
(def ^:export snackbar-content (r/adapt-react-class SnackbarContent))
(def ^:export step (r/adapt-react-class Step))
(def ^:export step-button (r/adapt-react-class StepButton))
(def ^:export step-connector (r/adapt-react-class StepConnector))
(def ^:export step-content (r/adapt-react-class StepContent))
(def ^:export step-icon (r/adapt-react-class StepIcon))
(def ^:export step-label (r/adapt-react-class StepLabel))
(def ^:export stepper (r/adapt-react-class Stepper))
(def ^:export svg-icon (r/adapt-react-class SvgIcon))
(def ^:export swipeable-drawer (r/adapt-react-class SwipeableDrawer))
(def ^:export switch (r/adapt-react-class Switch))
(def ^:export tab (r/adapt-react-class Tab))
(def ^:export table (r/adapt-react-class Table))
(def ^:export table-body (r/adapt-react-class TableBody))
(def ^:export table-cell (r/adapt-react-class TableCell))
(def ^:export table-footer (r/adapt-react-class TableFooter))
(def ^:export table-head (r/adapt-react-class TableHead))
(def ^:export table-pagination (r/adapt-react-class TablePagination))
(def ^:export table-row (r/adapt-react-class TableRow))
(def ^:export table-sort-label (r/adapt-react-class TableSortLabel))
(def ^:export tabs (r/adapt-react-class Tabs))
(def ^:export text-field (r/adapt-react-class TextField))
(def ^:export toolbar (r/adapt-react-class Toolbar))
(def ^:export tooltip (r/adapt-react-class Tooltip))
(def ^:export typography (r/adapt-react-class Typography))
(def ^:export zoom (r/adapt-react-class Zoom))

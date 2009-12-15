/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

// ================================== PUBLIC API METHODS

//  popupMenu.show();

//  popupMenu.showAtXY(x, y);

//  popupMenu.setLeft(x); // todo: consider replacing setLeft/setRight with setPosition(x, y)
//  popupMenu.setTop(y);

//  popupMenu.hide();

//  popupMenu.showForEvent(event);

// ================================== END OF PUBLIC API METHODS

O$.PopupMenu = {
  _init: function(popupMenuId,
                  rolloverClass,
                  forId,
                  forEventName,
                  indentVisible,
                  indentClass,
                  defaultItemClass,
                  defaultSelectedClass,
                  defaultContentClass,
                  defaultDisabledClass,

                  itemIconUrl,
                  disabledItemIconUrl,
                  selectedItemIconUrl,
                  selectedDisabledItemIconUrl,

                  submenuImageUrl,
                  disabledSubmenuImageUrl,
                  selectedSubmenuImageUrl,
                  selectedDisabledSubmenuImageUrl,

                  isRootMenu,
                  itemsProperties,
                  submenuHorisontalOffset,
                  submenuShowDelay,
                  submenuHideDelay,
                  selectDisabledItems,
                  events) {
    var popupMenu = O$.initComponent(popupMenuId, {rollover: rolloverClass});

    O$.assignEvents(popupMenu, events, true);

    O$.setupArtificialFocus(popupMenu, null);

    O$.Popup._init(popupMenuId, false);

    popupMenu._isRoot = function() {
      return !!isRootMenu;
    };
    if (!isRootMenu) {
      document._openFaces_popupsOnpage.pop(popupMenuId);
    }

    popupMenu._popupMenuItems = [];

    var childNodes = popupMenu.childNodes;

    // Create object tree for easy access to the PopupMenu and menuItems
    for (var i = 0; i < childNodes.length; i++) {
      var menuItem = popupMenu._popupMenuItems[i] = childNodes[i];

      menuItem._isSeparator = function() {
        return !!this._separator;
      };

      menuItem._popupMenu = popupMenu;

      var anchor = O$(menuItem.id + "::commandLink");
      if (anchor) {
        menuItem._anchor = anchor;
        menuItem._anchor._menuItem = menuItem;
        menuItem._index = i;
        menuItem._icon = O$(menuItem.id + "::image");
        menuItem._caption = O$(menuItem.id + "::caption");
        menuItem._iconspan = O$(menuItem.id + "::imagespan");

        menuItem._arrow = O$(menuItem.id + "::arrow");
        menuItem._arrowspan = O$(menuItem.id + "::arrowspan");

        menuItem._imagefakespan = O$(menuItem.id + "::imagefakespan");
        menuItem._arrowfakespan = O$(menuItem.id + "::arrowfakespan");

        menuItem._separator = null;
        anchor._defaultItemClass = defaultItemClass;
        anchor._defaultDisabledClass = defaultDisabledClass;

        if (defaultContentClass)
          O$.appendClassNames(menuItem._caption, [defaultContentClass]);
      } else {
        menuItem._separator = O$(menuItem.id + "::separator");
      }

      menuItem._properties = itemsProperties[i];
    }

    O$.PopupMenu._handlePaddings(popupMenu);

    if (popupMenu._isRoot() && forId)
      O$.PopupMenu._addHandlersToOpenMenu(popupMenu, forId, forEventName);

    /*Set styles for each item*/
    for (i = 0; i < popupMenu._popupMenuItems.length; i++) {
      menuItem = popupMenu._popupMenuItems[i];

      O$.PopupMenu._updateMenuItemBackground(menuItem);

      if (!menuItem._isSeparator()) {
        O$.setStyleMappings(menuItem._anchor, {
          defaultClass:  menuItem._anchor._defaultItemClass
        });

        menuItem._disabled = menuItem._properties.disabled;

        O$.PopupMenu._setStyleForSubmenuArea(menuItem, submenuImageUrl, selectedSubmenuImageUrl, disabledSubmenuImageUrl, selectedDisabledSubmenuImageUrl);
      }

      O$.PopupMenu._setStylesForIndentArea(menuItem, indentVisible, indentClass, itemIconUrl, selectedItemIconUrl, disabledItemIconUrl, selectedDisabledItemIconUrl);

      /* Update styles of fake spans*/
      if (menuItem._iconspan)
        menuItem._imagefakespan.style.verticalAlign = O$.PopupMenu._getStyleProperty(menuItem._iconspan, "vertical-align");
      if (menuItem._arrowfakespan)
        menuItem._arrowfakespan.style.verticalAlign = O$.PopupMenu._getStyleProperty(menuItem._arrowspan, "vertical-align");
    }

    if (!indentVisible) {
      /*set default style if indent is not visible*/
      popupMenu.style.backgroundColor = O$.PopupMenu._getStyleProperty(popupMenu, "backgroundColor");
    }

    O$.PopupMenu._updatePopupMenuBackground(popupMenu, indentClass);

    //-------------------------Events----------------------------------
    var eventName = (O$.isSafariOnMac() || O$.isOpera() || O$.isMozillaFF()) ? "onkeypress" : "onkeydown";
    popupMenu._prevKeyHandler = popupMenu[eventName];
    popupMenu[eventName] = function (evt) {
      var e = evt ? evt : window.event;
      var menuItem;
      switch (e.keyCode) {
        case 27: // escape
          O$.PopupMenu._closeAllMenu(popupMenu);
          break;
        case 13: // enter
          if (popupMenu._selectedIndex != null) {
            menuItem = popupMenu._popupMenuItems[popupMenu._selectedIndex];
            O$.sendEvent(menuItem._anchor, "click");
          }
          break;
        case 38: // up
          O$.PopupMenu._setSelectedMenuItem(popupMenu, -1, selectDisabledItems);
          break;
        case 40: // down
          O$.PopupMenu._setSelectedMenuItem(popupMenu, 1, selectDisabledItems);
          break;
        case 37: // left
          if (popupMenu._parentPopupMenu) {
            popupMenu._parentPopupMenu.closeAllChildMenus();
            popupMenu._parentPopupMenu.focus();
          }
          break;
        case 39: // right
          menuItem = popupMenu._popupMenuItems[popupMenu._selectedIndex];
          if (menuItem._openSubmenuTask)  clearTimeout(menuItem._openSubmenuTask);
          O$.PopupMenu._showSubmenu(popupMenu, menuItem._anchor, submenuHorisontalOffset);
          O$.PopupMenu._showFirstMenuItemonChild(menuItem, selectDisabledItems);
          break;
      }
      O$.cancelBubble(e);
    };

    for (i = 0; i < popupMenu._popupMenuItems.length; i++) {
      menuItem = popupMenu._popupMenuItems[i];
      if (!menuItem._separator) {

        menuItem.selectedDisabledClass = O$.combineClassNames([itemsProperties[i].selectedClass, defaultSelectedClass]);
        menuItem.selectedClass = O$.combineClassNames([itemsProperties[i].selectedClass, defaultSelectedClass]);

        O$.PopupMenu.setMenuItemEnabled(menuItem.id, !menuItem._properties.disabled, selectDisabledItems);

        O$.PopupMenu._setAdditionalIEStylesForMenuItem(menuItem);
        O$.PopupMenu._addMenuItemClickHandler(menuItem, submenuHorisontalOffset, selectDisabledItems);

        O$.PopupMenu._addMouseOverOutEvents(menuItem, selectDisabledItems, submenuHorisontalOffset, submenuShowDelay, submenuHideDelay);
      }
    }

    popupMenu.show = function() {
      this.style.visibility = "visible";
      this.style.display = "block";
      O$.PopupMenu._initIEWidthWorkaround(this);
      if (this.onshow)
        this.onshow();
    };

    popupMenu.hide = function() {
      if (O$.PopupMenu._getStyleProperty(this, "visibility") != "hidden" &&
          O$.PopupMenu._getStyleProperty(this, "display") != "none") {
        this.style.visibility = "hidden";
        this.style.display = "none";

        O$.PopupMenu._clearSelection(popupMenu);

        if (this.onhide)
          this.onhide();
      }
    };

    popupMenu.showAtXY = function (x, y) {
      this.setLeft(x);
      this.setTop(y);
      this.show();
    };

    popupMenu.showForEvent = function (event) {
      var pos = O$.getEventPoint(event, this);
      this.showAtXY(pos.x, pos.y);
    };

    popupMenu.closeAllChildMenus = function () {
      if (!!popupMenu._OpenedChildMenu) {
        popupMenu._OpenedChildMenu.closeAllChildMenus();
        popupMenu._OpenedChildMenu.updateCoordinates = null;
        popupMenu._OpenedChildMenu.hide();
        popupMenu._OpenedChildMenu = null;
      }
    };
  },

  setMenuItemEnabled: function(menuItemId, enabled, selectDisabledItems) {
    if (!menuItemId)
      throw "O$.PopupMenu.setMenuItemEnabled: MenuItem's clientId must be passed as a parameter";
    var menuItem = O$(menuItemId);
    if (!menuItem)
      throw "O$.PopupMenu.setMenuItemEnabled: Invalid clientId passed - no such component was found: " + menuItemId;
    if (menuItem._isSeparator())
      throw "O$.PopupMenu.setMenuItemEnabled: MenuItem must not be separator";

    menuItem._disabled = !enabled;

    /* Update icons and styles if user chaged enabling of the MenuItem */
    if (enabled) {
      O$.setStyleMappings(menuItem._anchor, {
        disabled: null
      });
      if (menuItem._icon) {
        if (menuItem._icon.imageSrc) {
          menuItem._icon.src = menuItem._icon.imageSrc;
          menuItem._icon.style.display = "";
        }
        else
          menuItem._icon.style.display = "none";
      }
      if (menuItem._menuId)
        menuItem._arrow.src = menuItem._arrow.arrowImage;
    } else {
      var disabledClass = O$.combineClassNames([menuItem._properties.disabledClass, menuItem._anchor._defaultDisabledClass]);
      O$.setStyleMappings(menuItem._anchor, {
        disabled: disabledClass
      });
      if (menuItem._icon) {
        if (menuItem._icon.disabledImgSrc) {
          menuItem._icon.src = menuItem._icon.disabledImgSrc;
          menuItem._icon.style.display = "";
        }
        else
          menuItem._icon.style.display = "none";
      }
      if (menuItem._menuId)
        menuItem._arrow.src = menuItem._arrow.arrowDisabledImage;
    }

    /*Update selected class*/
    var style = menuItem._anchor.className;
    var rolloverStyle = style;
    if (!enabled && selectDisabledItems) {
      rolloverStyle = menuItem.selectedDisabledClass + " " + menuItem.className;
    }
    if (enabled) {
      rolloverStyle = menuItem.selectedClass + " " + menuItem.className;
    }

    /*When user adds the border on rollover then menuItems jumps on mouseover*/
    /*we need to compensate rollover border with padding*/
    menuItem._diffTop = O$.calculateNumericCSSValue(O$.PopupMenu._getCssProperty(rolloverStyle, "borderTopWidth")) -
                        O$.calculateNumericCSSValue(O$.PopupMenu._getCssProperty(style, "borderTopWidth"));

    menuItem._diffBottom = O$.calculateNumericCSSValue(O$.PopupMenu._getCssProperty(rolloverStyle, "borderBottomWidth")) -
                           O$.calculateNumericCSSValue(O$.PopupMenu._getCssProperty(style, "borderBottomWidth"));

    menuItem._diffLeft = O$.calculateNumericCSSValue(O$.PopupMenu._getCssProperty(rolloverStyle, "borderLeftWidth")) -
                         O$.calculateNumericCSSValue(O$.PopupMenu._getCssProperty(style, "borderLeftWidth"));

    menuItem._diffRight = O$.calculateNumericCSSValue(O$.PopupMenu._getCssProperty(rolloverStyle, "borderRightWidth")) -
                          O$.calculateNumericCSSValue(O$.PopupMenu._getCssProperty(style, "borderRightWidth"));

    O$.PopupMenu._addPaddingToMenuItem(menuItem);
  },

  _getCssProperty: function(styleClass, propertyName) {
    var value = O$.getStyleClassProperty(styleClass, propertyName);
    if (!value) return "";
    return value;
  },

  _getStyleProperty: function(element, propertyName) {
    var value = O$.getElementStyle(element, propertyName);
    if (value == "auto") return "0px";
    return value;
  },

  _setHighlightMenuItem: function(popupMenu, anchor, selectDisabledItems) {
    var menuItem = anchor.parentNode;
    if (menuItem._popupMenu._selectedIndex != menuItem._index) {
      O$.PopupMenu._clearSelection(popupMenu);
      popupMenu._selectedIndex = menuItem._index;
      anchor._focused = true;
      popupMenu.focus();

      /*if (keepParentItemSelected && popupMenu._parentPopupMenuItem) {
       O$.setStyleMappings(popupMenu._parentPopupMenuItem, {
       select:  popupMenu._parentPopupMenuItem.parentNode.selectedClass
       });
       var arrow = popupMenu._parentPopupMenuItem.parentNode._arrow
       if (arrow.arrowSelectedImage)
       arrow.src = arrow.arrowSelectedImage;
       }

       if (popupMenu._parentPopupMenuItem)
       popupMenu._parentPopupMenu._selectedIndex = popupMenu._parentPopupMenuItem.parentNode._index;
       */

      if (!menuItem._disabled) {
        O$.setStyleMappings(anchor, {
          select: menuItem.selectedClass
        });
        if (menuItem._icon && menuItem._icon.imageSelectedSrc)
          menuItem._icon.src = menuItem._icon.imageSelectedSrc;

      } else {
        if (selectDisabledItems) {
          O$.setStyleMappings(anchor, {
            select:  menuItem.selectedDisabledClass
          });
          if (menuItem._icon && menuItem._icon.disabledImgSelectedSrc)
            menuItem._icon.src = menuItem._icon.disabledImgSelectedSrc;
        }
      }
      if (menuItem._arrow) {
        var arrow = menuItem._arrow;
        if (arrow.arrowSelectedImage && !menuItem._disabled)
          arrow.src = arrow.arrowSelectedImage;
        else if (arrow.arrowSelectedDisabledImage && menuItem._disabled)
          arrow.src = arrow.arrowSelectedDisabledImage;
      }
      O$.PopupMenu._substractPaddingFromMenuItem(menuItem);
      if (menuItem._popupMenu._OpenedChildMenu != null &&
          menuItem._popupMenu._OpenedChildMenu == O$(menuItem._menuId)) {
        var childMenu = menuItem._popupMenu._OpenedChildMenu;
        if (childMenu.updateCoordinates) {
          childMenu.updateCoordinates = null;
          childMenu.style.left = (O$.calculateNumericCSSValue(childMenu.style.left) - menuItem._diffLeft) + "px";
          childMenu.style.top = (O$.calculateNumericCSSValue(childMenu.style.top) - menuItem._diffTop) + "px";
        }
      }
    }
  },

  _unselectMenuItem: function(menuItem) {
    if (menuItem._popupMenu._selectedIndex != null &&
        menuItem._popupMenu._selectedIndex == menuItem._index) {
      if (!menuItem._isSeparator()) {
        menuItem._popupMenu._selectedIndex = null;

        O$.setStyleMappings(menuItem._anchor, {
          select:  null
        });

        if (menuItem._arrow) {
          var arrow = menuItem._arrow;
          if (arrow.arrowSelectedImage && !menuItem._disabled)
            arrow.src = arrow.arrowImage;
          else if (arrow.arrowSelectedDisabledImage && menuItem._disabled)
            arrow.src = arrow.arrowDisabledImage;
        }

        if (menuItem._disabled == null) {
          if (menuItem._icon && menuItem._icon.imageSelectedSrc)
            menuItem._icon.src = menuItem._icon.imageSrc;
        } else {
          if (menuItem._icon && menuItem._icon.disabledImgSelectedSrc)
            menuItem._icon.src = menuItem._icon.disabledImgSrc;
        }
        O$.PopupMenu._addPaddingToMenuItem(menuItem);

        /* avoid jumping submenu if border changes*/
        if (menuItem._popupMenu._OpenedChildMenu != null &&
            menuItem._popupMenu._OpenedChildMenu == O$(menuItem._menuId)) {
          var childMenu = menuItem._popupMenu._OpenedChildMenu;
          childMenu.updateCoordinates = true;
          childMenu.style.left = (O$.calculateNumericCSSValue(childMenu.style.left) + menuItem._diffLeft) + "px";
          childMenu.style.top = (O$.calculateNumericCSSValue(childMenu.style.top) + menuItem._diffTop) + "px";
        }
      }
    }
  },

  _clearSelection: function(popupMenu) {
    var children = popupMenu.childNodes;
    for (var i = 0; i < children.length; i++) {
      O$.PopupMenu._unselectMenuItem(children[i]);
    }
  },

  _setSelectedMenuItem: function(popupMenu, inc, selectDisabledItems) {
    popupMenu.focus();
    var index = popupMenu._selectedIndex;

    if (index == null) {
      index = 0;
      inc = 0;
    }

    var length = popupMenu._popupMenuItems.length;
    var menuItems = popupMenu._popupMenuItems;
    var nextIndex = index + inc;

    if (nextIndex >= length) {
      nextIndex = nextIndex - length;
    }
    if (nextIndex < 0) {
      nextIndex = length + nextIndex;
    }
    if (menuItems[nextIndex]._isSeparator() || (!selectDisabledItems && menuItems[nextIndex].disabled)) {
      var inc_add = inc >= 0 ? inc + 1 : inc - 1;
      O$.PopupMenu._setSelectedMenuItem(popupMenu, inc_add, selectDisabledItems);
    }
    else {
      O$.PopupMenu._setHighlightMenuItem(popupMenu, popupMenu._popupMenuItems[nextIndex]._anchor, selectDisabledItems);
      //popupMenu._selectedItem = this._menuItem;
    }
  },

  _showSubmenu: function(popupMenu, this_, submenuHorisontalOffset) {
    if (!this_.parentNode._disabled) {
      if (!!this_._menuId) {
        var childPopupMenu = O$(this_._menuId);
        if (popupMenu._OpenedChildMenu != childPopupMenu) {
          popupMenu.closeAllChildMenus();
          var popupMenuRect = O$.getElementBorderRectangle(popupMenu, true);
          popupMenu._OpenedChildMenu = childPopupMenu;

          var popup_menu_border_top = O$.getNumericElementStyle(popupMenu, "border-top-width");
          var childPopupMenu_border_left = (!O$.isOpera()) ? O$.getNumericElementStyle(popupMenu, "border-left-width") : 0;
          var item_border_left = O$.getNumericElementStyle(this_, "border-left-width");
          childPopupMenu.showAtXY(popupMenuRect.width - this_.offsetLeft - childPopupMenu_border_left - item_border_left + submenuHorisontalOffset, -popup_menu_border_top);

          O$.PopupMenu._clearSelection(childPopupMenu);

          childPopupMenu.focus();
        }
        popupMenu._enter = false;
      }
    }
  },

  _showFirstMenuItemonChild: function(menuItem, selectDisabledItems) {
    if (!!menuItem._menuId && !menuItem._disabled) {
      var childPopupMenu = O$(menuItem._menuId);
      var childNodes = childPopupMenu.childNodes;
      for (var i = 0; i < childNodes.length; i++) {
        if (!childNodes[i]._isSeparator() && !(!!childNodes[i]._disabled && !selectDisabledItems)) {
          O$.PopupMenu._setHighlightMenuItem(childPopupMenu, childNodes[i]._anchor, selectDisabledItems);
          break;
        }
      }
    }
  },

  _closeAllMenu: function(popupMenu) {
    popupMenu.closeAllChildMenus();
    while (popupMenu) {
      popupMenu.hide();
      popupMenu = popupMenu._parentPopupMenu;
    }
  },

  _initIEWidthWorkaround: function(popupMenu) {
    if (O$.isExplorer() && popupMenu._menuItemsAlign == null) {
      popupMenu._menuItemsAlign = true;
      var maxWidthItem = 0;
      var width;
      popupMenu._popupMenuItems.forEach(function(menuItem) {
        if (!menuItem._isSeparator()) {
          width = menuItem._anchor.offsetWidth;
        }
        if (width > maxWidthItem)
          maxWidthItem = width;
      });
      popupMenu._popupMenuItems.forEach(function(menuItem) {
        var paddingLeft, paddingRight;
        if (menuItem._isSeparator()) {
          var separator = menuItem._separator;
          paddingLeft = O$.getNumericElementStyle(separator, "padding-left");
          paddingRight = O$.getNumericElementStyle(separator, "padding-right");
          if (O$.isQuirksMode()) {
            paddingLeft = paddingRight = 0;
          }
          var width = maxWidthItem - O$.getNumericElementStyle(menuItem._separator, "margin-right") -
                      (O$.getNumericElementStyle(menuItem, "margin-left") +
                       O$.getNumericElementStyle(menuItem._separator, "margin-left")) - paddingLeft - paddingRight;

          separator.style.width = width + " px";
        } else {
          var anchor = menuItem._anchor;
          var borderLeftWidth = O$.getNumericElementStyle(anchor, "border-left-width");
          var borderRightWidth = O$.getNumericElementStyle(anchor, "border-right-width");
          paddingLeft = O$.getNumericElementStyle(anchor, "padding-left");
          paddingRight = O$.getNumericElementStyle(anchor, "padding-right");
          if (O$.isQuirksMode()) {
            borderLeftWidth = borderRightWidth = paddingLeft = paddingRight = 0;
          }
          anchor.style.width = maxWidthItem - borderLeftWidth - borderRightWidth - paddingLeft - paddingRight + " px";
        }
      });
    }

  },

  _substractPaddingFromMenuItem: function(menuItem) {
    var anchorStyle = menuItem._anchor.style;
    anchorStyle.paddingTop = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._anchor, "padding-top") - menuItem._diffTop) + "px";
    anchorStyle.paddingBottom = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._anchor, "padding-bottom") - menuItem._diffBottom) + "px";
    anchorStyle.paddingRight = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._anchor, "padding-right") - menuItem._diffRight) + "px";
    anchorStyle.paddingLeft = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._anchor, "padding-left") - menuItem._diffLeft) + "px";

    if (menuItem._iconspan != null) {
      var iconSpanStyle = menuItem._iconspan.style;
      iconSpanStyle.paddingTop = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._iconspan, "padding-top") - menuItem._diffTop) + "px";
      iconSpanStyle.paddingLeft = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._iconspan, "padding-left") - menuItem._diffLeft) + "px";
      iconSpanStyle.paddingBottom = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._iconspan, "padding-bottom") - menuItem._diffBottom) + "px";
      if (O$.isExplorer())
        iconSpanStyle.width = O$.calculateNumericCSSValue(O$.getElementStyle(menuItem._iconspan, "width")) - menuItem._diffLeft + "px";
    }

    if (menuItem._arrowspan != null) {
      var arrowSpanStyle = menuItem._arrowspan.style;
      arrowSpanStyle.paddingTop = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._arrowspan, "padding-top") - menuItem._diffTop) + "px";
      arrowSpanStyle.paddingRight = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._arrowspan, "padding-right") - menuItem._diffRight) + "px";
      arrowSpanStyle.paddingBottom = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._arrowspan, "padding-bottom") - menuItem._diffBottom) + "px";
      if (O$.isExplorer())
        arrowSpanStyle.width = O$.calculateNumericCSSValue(O$.getElementStyle(menuItem._arrowspan, "width")) - menuItem._diffRight + "px";

    }
  },

  _addPaddingToMenuItem: function(menuItem) {
    var anchorStyle = menuItem._anchor.style;
    anchorStyle.paddingTop = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._anchor, "padding-top") + menuItem._diffTop) + "px";
    anchorStyle.paddingBottom = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._anchor, "padding-bottom") + menuItem._diffBottom) + "px";
    anchorStyle.paddingRight = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._anchor, "padding-right") + menuItem._diffRight) + "px";
    anchorStyle.paddingLeft = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._anchor, "padding-left") + menuItem._diffLeft) + "px";

    if (menuItem._iconspan != null) {
      var iconSpanStyle = menuItem._iconspan.style;
      iconSpanStyle.paddingTop = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._iconspan, "padding-top") + menuItem._diffTop) + "px";
      if (O$.isExplorer() && O$.isStrictMode()) {
        iconSpanStyle.paddingLeft = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._iconspan, "padding-left") + menuItem._diffLeft -
                                                                 O$.getNumericElementStyle(menuItem._anchor, "border-left-width") -
                                                                 O$.getNumericElementStyle(menuItem._anchor, "border-right-width")) + "px";
      }
      else {
        iconSpanStyle.paddingLeft = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._iconspan, "padding-left") + menuItem._diffLeft) + "px";
      }
      iconSpanStyle.paddingBottom = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._iconspan, "padding-bottom") + menuItem._diffBottom) + "px";
      if (O$.isExplorer())
        iconSpanStyle.width = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._iconspan, "width") + menuItem._diffLeft) + "px";
    }

    if (menuItem._arrowspan != null) {
      var arrowSpanStyle = menuItem._arrowspan.style;
      arrowSpanStyle.paddingTop = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._arrowspan, "padding-top") + menuItem._diffTop) + "px";
      if (O$.isExplorer() && O$.isStrictMode()) {
        arrowSpanStyle.paddingRight = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._arrowspan, "padding-right") + menuItem._diffRight -
                                                                   O$.getNumericElementStyle(menuItem._anchor, "border-left-width") -
                                                                   O$.getNumericElementStyle(menuItem._anchor, "border-right-width")) + "px";
      }
      else {
        arrowSpanStyle.paddingRight = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._arrowspan, "padding-right") + menuItem._diffRight) + "px";
      }
      arrowSpanStyle.paddingBottom = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._arrowspan, "padding-bottom") + menuItem._diffBottom) + "px";
      if (O$.isExplorer())
        arrowSpanStyle.width = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._arrowspan, "width") + menuItem._diffRight) + "px";
    }
  },

  _getWidth: function(element) {
    return O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(element, "width")) +
           O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(element, "padding-right")) +
           O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(element, "padding-left"));
  },

  _setAdditionalIEStylesForMenuItem: function(menuItem) {
    if (O$.isExplorer() && O$.isQuirksMode()) {
      if (menuItem._caption != null) {
        menuItem._caption.style.height = "100%";
      }
      if (menuItem._iconspan != null) {
        menuItem._iconspan.style.height = "100%";
      }
      if (menuItem._arrowspan != null) {
        menuItem._arrowspan.style.height = "100%";
      }
    }
    if (O$.isExplorer() && O$.isStrictMode()) {
      var height = O$.getNumericElementStyle(menuItem._anchor, "height") ;
      height += O$.getNumericElementStyle(menuItem._anchor, "border-top-width") +
                O$.getNumericElementStyle(menuItem._anchor, "border-bottom-width");
      if (menuItem._iconspan != null)
        menuItem._iconspan.style.height = height + "px";
      if (menuItem._arrowspan != null)
        menuItem._arrowspan.style.height = height + "px";
    }
  },

  _getNonNegative: function(value) {
    if (value < 0) return 0;
    return value;
  },

  /*When user defines padding for the PopupMenu we need to transform padding to margin of the menuItems*/
  _handlePaddings: function(popupMenu) {
    var paddingBottom = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(popupMenu, "padding-bottom"));
    var paddingTop = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(popupMenu, "padding-top"));
    var paddingRight = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(popupMenu, "padding-right"));

    popupMenu._popupMenuItems[0].style.paddingTop = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(popupMenu._popupMenuItems[0], "padding-top")) + paddingTop + "px";
    var lastMenuItem = popupMenu._popupMenuItems[popupMenu._popupMenuItems.length - 1];
    lastMenuItem.style.paddingBottom = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(lastMenuItem, "padding-bottom")) + paddingBottom + "px";
    popupMenu.style.paddingTop = 0;
    popupMenu.style.paddingBottom = 0;
    popupMenu.style.paddingRight = 0;

    for (var i = 0; i < popupMenu._popupMenuItems.length; i++) {
      var menuItem = popupMenu._popupMenuItems[i];
      menuItem.style.paddingRight = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(lastMenuItem, "padding-right")) + paddingRight + "px";
    }
  },

  _updateMenuItemBackground: function(menuItem) {
    var listItemStyle = menuItem.style;
    var popupMenu = menuItem._popupMenu;
    listItemStyle.backgroundColor = O$.getElementStyle(popupMenu, "background-color");
    listItemStyle.backgroundAttachment = O$.getElementStyle(popupMenu, "background-attachment");
    listItemStyle.backgroundImage = O$.getElementStyle(popupMenu, "background-image");
    listItemStyle.backgroundPositionX = O$.getElementStyle(popupMenu, "background-position-x");
    listItemStyle.backgroundPositionY = O$.getElementStyle(popupMenu, "background-position-y");
    listItemStyle.backgroundRepeat = O$.getElementStyle(popupMenu, "background-repeat");
  },

  _updatePopupMenuBackground: function(popupMenu, indentClass) {
    var popupTagStyle = popupMenu.style;
    popupTagStyle.backgroundColor = O$.PopupMenu._getCssProperty(indentClass, "background-color");
    popupTagStyle.backgroundAttachment = O$.PopupMenu._getCssProperty(indentClass, "background-attachment");
    popupTagStyle.backgroundImage = O$.PopupMenu._getCssProperty(indentClass, "background-imagee");
    popupTagStyle.backgroundPositionX = O$.PopupMenu._getCssProperty(indentClass, "background-position-x");
    popupTagStyle.backgroundPositionY = O$.PopupMenu._getCssProperty(indentClass, "background-position-y");
    popupTagStyle.backgroundRepeat = O$.PopupMenu._getCssProperty(indentClass, "background-repeat");
  },

  _setStylesForIndentArea: function(
          menuItem, indentVisible, indentClass, defaultIconItemUrl, defaultSelectedIconItemUrl,
          defaultDisabledIconItemUrl, defaultSelectedDisabledIconItemUrl) {
    if (indentVisible) {
      var width = O$.calculateNumericCSSValue(O$.PopupMenu._getCssProperty(indentClass, "width"));
      var borderRight = O$.PopupMenu._getCssProperty(indentClass, "borderRight");
      var borderRightWidth = O$.calculateNumericCSSValue(O$.PopupMenu._getCssProperty(indentClass, "borderRightWidth"));
      menuItem.style.borderLeft = borderRight;
      menuItem.style.marginLeft = width + "px";

      if (!menuItem._separator) {
        var margin = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(menuItem._anchor, "margin-left"));
        menuItem._anchor.style.marginLeft = (-1 * (width + borderRightWidth) + margin) + "px";

        if (menuItem._icon) {
          menuItem._icon.imageSrc = menuItem._properties.imgSrc;
          menuItem._icon.imageSelectedSrc = menuItem._properties.imgSelectedSrc;

          menuItem._icon.disabledImgSrc = menuItem._properties.disabledImgSrc;
          menuItem._icon.disabledImgSelectedSrc = menuItem._properties.disabledImgSelectedSrc;

          if (!defaultDisabledIconItemUrl) {
            defaultDisabledIconItemUrl = defaultIconItemUrl;
            defaultSelectedDisabledIconItemUrl = defaultSelectedIconItemUrl;
          }

          if (menuItem._icon.imageSrc && !menuItem._icon.disabledImgSrc) {
            menuItem._icon.disabledImgSrc = menuItem._icon.imageSrc;
            menuItem._icon.disabledImgSelectedSrc = menuItem._icon.imageSelectedSrc;
          }

          if (!menuItem._icon.imageSrc) {
            menuItem._icon.imageSrc = defaultIconItemUrl;
            menuItem._icon.imageSelectedSrc = defaultSelectedIconItemUrl;
          }

          if (!menuItem._icon.disabledImgSrc) {
            menuItem._icon.disabledImgSrc = defaultDisabledIconItemUrl;
            menuItem._icon.disabledImgSelectedSrc = defaultSelectedDisabledIconItemUrl;
          }
        }
        var icon_width = O$.PopupMenu._getWidth(menuItem._iconspan);
        var paddingLeft = O$.getNumericElementStyle(menuItem._caption, "padding-left");
        menuItem._caption.style.paddingLeft = (paddingLeft + icon_width + borderRightWidth) + "px";
      } else {
        margin = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(menuItem._separator, "margin-left"));
        var padding = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(menuItem, "padding-left"));
        menuItem._separator.style.marginLeft = (-1 * (width + borderRightWidth) + margin + padding) + "px";
      }
    }
  },

  _setStyleForSubmenuArea: function(
          menuItem, submenuImageUrl, selectedSubmenuImageUrl, disabledSubmenuImageUrl, selectedDisabledSubmenuImageUrl) {
    if (menuItem._properties.menuId) {
      menuItem._menuId = menuItem._properties.menuId;
      menuItem._anchor._menuId = menuItem._properties.menuId;

      var childPopupMenu = O$(menuItem._menuId);
      childPopupMenu._parentPopupMenu = menuItem._popupMenu;
      childPopupMenu._parentPopupMenuItem = menuItem._anchor;

      var arrowDisabledImage = menuItem._properties.disabledSubmenuImageUrl;
      var arrowSelectedDisabledImage = menuItem._properties.selectedDisabledSubmenuImageUrl;
      var arrowImage = menuItem._properties.submenuImageUrl;
      var arrowSelectedImage = menuItem._properties.selectedSubmenuImageUrl;

      if (!disabledSubmenuImageUrl) {
        disabledSubmenuImageUrl = submenuImageUrl;
        selectedDisabledSubmenuImageUrl = selectedSubmenuImageUrl;
      }

      if (arrowImage && !arrowDisabledImage) {
        arrowDisabledImage = arrowImage;
        arrowSelectedDisabledImage = arrowSelectedImage;
      }

      if (!arrowImage) {
        arrowImage = submenuImageUrl;
        arrowSelectedImage = selectedSubmenuImageUrl;
      }

      if (!arrowDisabledImage) {
        arrowDisabledImage = disabledSubmenuImageUrl;
        arrowSelectedDisabledImage = selectedDisabledSubmenuImageUrl;
      }

      menuItem._arrow.arrowImage = arrowImage;
      menuItem._arrow.arrowSelectedImage = arrowSelectedImage;

      menuItem._arrow.arrowDisabledImage = arrowDisabledImage;
      menuItem._arrow.arrowSelectedDisabledImage = arrowSelectedDisabledImage;
    }
    if (menuItem._arrowspan != null) {
      /* set margin for the arrow */
      var arrowAreaWidth = O$.PopupMenu._getWidth(menuItem._arrowspan);
      menuItem._caption.style.paddingRight = O$.calculateNumericCSSValue(menuItem._caption.style.paddingRight) + arrowAreaWidth + "px";
    }
  },

  _addHandlersToOpenMenu: function(popupMenu, forId, forEventName) {
    var forElement = O$(forId);
    if (!forElement)
      forElement = popupMenu.parentNode;

    var eventName = "contextmenu";
    if (forEventName) {
      if (O$.stringStartsWith(forEventName, "on"))
        eventName = forEventName.substring(2, forEventName.length);
      else
        eventName = "contextMenu";
    }

    if (eventName == "contextmenu") {
      if (O$.isOpera()) {
        O$.addEventHandler(forElement, "mouseup", function(evt) {
          if (!O$.isChild(popupMenu, evt.target ? evt.target : evt.srcElement)) {
            if (O$.isCtrlPressed(evt) && evt.button != 2) {
              popupMenu.showForEvent(evt);
              popupMenu.focus();
            }
          }
        });
      } else {
        O$.disabledContextMenuFor(forElement);
        O$.addEventHandler(forElement, eventName, function(evt) {
          if (!O$.isChild(popupMenu, evt.target ? evt.target : evt.srcElement)) {
            if (!O$.isShiftPressed(evt)) {
              if (!forElement._isDisabledContextMenu)
                O$.disabledContextMenuFor(forElement);
              popupMenu.showForEvent(evt);
              popupMenu.focus();
            } else {
              O$.enabledContextMenuFor(forElement);
            }
          }
        });
      }
    } else {
      // if not context menu
      O$.addEventHandler(forElement, eventName, function(evt) {
        if (!O$.isChild(popupMenu, evt.target ? evt.target : evt.srcElement)) {
          popupMenu.showForEvent(evt);
          popupMenu.focus();
        }
      });
    }
  },

  _addMenuItemClickHandler: function(menuItem, submenuHorisontalOffset, selectDisabledItems) {
    var popupMenu = menuItem._popupMenu;
    if (!menuItem._properties.disabled) {
      if (menuItem._properties.action) {
        function itemClick(menuItemElement) {
          O$.addEventHandler(menuItemElement, "click", function() {
            eval(menuItemElement.parentNode._properties.action);
          });
        }

        itemClick(menuItem._anchor);
      }
      else {
        function defaultItemClick(menuItemElement) {
          O$.addEventHandler(menuItemElement, "click", function(evt) {
            var menuItem = menuItemElement.parentNode;
            if (menuItem._menuId) {
              if (menuItem._openSubmenuTask) {
                clearTimeout(menuItem._openSubmenuTask);
              }
              if (popupMenu._parentPopupMenu) {
                clearTimeout(popupMenu._parentPopupMenu._closeAllTask);
              }
              O$.PopupMenu._showSubmenu(popupMenu, menuItem._anchor, submenuHorisontalOffset);
              O$.PopupMenu._showFirstMenuItemonChild(menuItem, selectDisabledItems);
            }
            else {
              O$.PopupMenu._closeAllMenu(popupMenu);
            }
            O$.stopEvent(evt); //to prevent set focus for the popupMenu
          });
        }

        defaultItemClick(menuItem._anchor);
      }
    }
  },

  _addMouseOverOutEvents: function(menuItem, selectDisabledItems, submenuHorisontalOffset, submenuShowDelay, submenuHideDelay) {
    var popupMenu = menuItem._popupMenu;
    var menuItemElement = menuItem._anchor;

    O$.addMouseOverListener(menuItemElement, function(evt) {
      var menuItem = menuItemElement.parentNode;
      if (menuItem._disabled && !selectDisabledItems) return;

      if (popupMenu._parentPopupMenu) {
        clearTimeout(popupMenu._parentPopupMenu._closeAllTask);
      }

      O$.PopupMenu._setHighlightMenuItem(popupMenu, menuItemElement, selectDisabledItems);

      menuItem._openSubmenuTask = setTimeout(function() {
        if (menuItemElement._focused)
          O$.PopupMenu._showSubmenu(popupMenu, menuItemElement, submenuHorisontalOffset);
      }, submenuShowDelay);

    });

    O$.addMouseOutListener(menuItemElement, function(evt) {
      var menuItem = menuItemElement.parentNode;
      if (menuItem._disabled && !selectDisabledItems) return;
      if (menuItemElement._oldMouseOut) menuItemElement._oldMouseOut(evt);
      menuItemElement._focused = null;

      O$.PopupMenu._unselectMenuItem(menuItemElement.parentNode);

      if (popupMenu._closeAllTask) {
        clearTimeout(popupMenu._closeAllTask);
      }

      popupMenu._closeAllTask = setTimeout(function() {
        popupMenu.closeAllChildMenus();
        popupMenu.focus();
      }, submenuHideDelay);

    });

  }
};


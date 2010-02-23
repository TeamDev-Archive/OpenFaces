/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

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
                  submenuHorizontalOffset,
                  submenuShowDelay,
                  submenuHideDelay,
                  selectDisabledItems,
                  events) {
    O$.Popup._init(popupMenuId, false);
    var popupMenu = O$.initComponent(popupMenuId, {rollover: rolloverClass}, {
      show: function() {
        finishInitialization();
        setTimeout(function() {
          O$.addIETransparencyControl(popupMenu);
        }, 1);
        this.style.visibility = "visible";
        this.style.display = "block";
        O$.PopupMenu._initIEWidthWorkaround(this);
        if (this.onshow)
          this.onshow();
      },

      hide: function() {
        finishInitialization();
        O$.removeIETransparencyControl(popupMenu);
        this.closeChildMenus();
        if (O$.PopupMenu._getStyleProperty(this, "visibility") != "hidden" &&
            O$.PopupMenu._getStyleProperty(this, "display") != "none") {
          this.style.visibility = "hidden";
          this.style.display = "none";

          O$.PopupMenu._clearSelection(popupMenu);
          if (popupMenu._closeAllTask)
            clearTimeout(popupMenu._closeAllTask);

          if (this.onhide)
            this.onhide();
        }
      },

      showAtXY: function (x, y /*, relativeToContainer*/ ) {
        finishInitialization();
        var relativeToContainer = arguments[2];
        if (relativeToContainer !== undefined) {
          var containerPos = relativeToContainer !== null ? O$.getElementPos(relativeToContainer) : {x: 0, y: 0};
          var absoluteX = x + containerPos.x;
          var absoluteY = y + containerPos.y;
          var popupContainer = popupMenu.offsetParent;
          var popupContainerPos = O$.getElementPos(popupContainer);
          x = absoluteX - popupContainerPos.x;
          y = absoluteY - popupContainerPos.y;
        }
        this.setLeft(x);
        this.setTop(y);
        this.show();
      },

      showForEvent: function (event) {
        finishInitialization();
        var pos = O$.getEventPoint(event, this);
        this.showAtXY(pos.x, pos.y);
      },

      closeChildMenus: function () {
        if (!!popupMenu._openedChildMenu) {
          popupMenu._openedChildMenu.closeChildMenus();
          popupMenu._openedChildMenu.updateCoordinates = null;
          popupMenu._openedChildMenu.hide();
          popupMenu._openedChildMenu = null;
        }
      },
      _isRoot: function() {
        return !!isRootMenu;
      }
    });

    if ((O$.isQuirksMode() && O$.isExplorer()) || (O$.isExplorer6() && O$.isStrictMode())) {
      O$.setStyleMappings(popupMenu, {ieQuirksFix: "o_popup_menu_iequirks"});
    }

    O$.assignEvents(popupMenu, events, true);

    O$.setupArtificialFocus(popupMenu, null);


    if (!isRootMenu) {
      O$._popupsOnPage.pop(popupMenuId);
    }

    var childNodes = popupMenu.childNodes;
    var items = popupMenu._items = [];

    // Create object tree for easy access to the PopupMenu and menuItems
    for (var i = 0; i < childNodes.length; i++) {
      var menuItem = childNodes[i];
      items.push(menuItem);

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

    var initialized = false;
    function finishInitialization() {
      if (initialized) return;
      initialized = true;
      if (!O$.isElementPresentInDocument(popupMenu)) {
        // can be the case if a PopupMenu was unloaded earlier than the postponed initialization occured
        return;
      }
      O$.PopupMenu._handlePaddings(popupMenu);

      if (popupMenu._isRoot() && forId)
        O$.PopupMenu._addHandlersToOpenMenu(popupMenu, forId, forEventName);

      /*Set styles for each item*/
      for (i = 0; i < popupMenu._items.length; i++) {
        menuItem = popupMenu._items[i];

        O$.PopupMenu._updateMenuItemBackground(menuItem);

        if (!menuItem._isSeparator()) {
          O$.setStyleMappings(menuItem._anchor, {
            defaultClass:  menuItem._anchor._defaultItemClass
          });

          menuItem._disabled = menuItem._properties.disabled;

          O$.PopupMenu._setStyleForSubmenuArea(menuItem, submenuImageUrl, selectedSubmenuImageUrl, disabledSubmenuImageUrl, selectedDisabledSubmenuImageUrl);
        }

        O$.PopupMenu._setStylesForIndentArea(menuItem, indentVisible, indentClass, itemIconUrl, selectedItemIconUrl, disabledItemIconUrl, selectedDisabledItemIconUrl);

        /* Update styles of fake spans */
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
              menuItem = popupMenu._items[popupMenu._selectedIndex];
              O$.sendEvent(menuItem._anchor, "click");
              O$.PopupMenu._closeAllMenu(popupMenu);
              O$.breakEvent(evt);
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
              popupMenu._parentPopupMenu.closeChildMenus();
              popupMenu._parentPopupMenu.focus();
            }
            break;
          case 39: // right
            if (popupMenu._selectedIndex == null) {
              O$.PopupMenu._setSelectedMenuItem(popupMenu, 1, selectDisabledItems);
            } else {
              menuItem = popupMenu._items[popupMenu._selectedIndex];
              O$.PopupMenu._showSubmenu(popupMenu, menuItem._anchor, submenuHorizontalOffset);
              O$.PopupMenu._showFirstMenuItemonChild(menuItem, selectDisabledItems);
            }
            break;
        }
        O$.cancelBubble(e);
      };

      for (i = 0; i < popupMenu._items.length; i++) {
        menuItem = popupMenu._items[i];
        if (!menuItem._separator) {
          menuItem.selectedDisabledClass = O$.combineClassNames([itemsProperties[i].selectedClass, defaultSelectedClass]);
          menuItem.selectedClass = O$.combineClassNames([itemsProperties[i].selectedClass, defaultSelectedClass]);

          O$.PopupMenu.setMenuItemEnabled(menuItem.id, !menuItem._properties.disabled, selectDisabledItems);

          O$.PopupMenu._setAdditionalIEStylesForMenuItem(menuItem);
          O$.PopupMenu._addMenuItemClickHandler(menuItem, submenuHorizontalOffset, selectDisabledItems);

          O$.PopupMenu._addMouseOverOutEvents(menuItem, selectDisabledItems, submenuHorizontalOffset, submenuShowDelay, submenuHideDelay);
        }
      }
    }

    setTimeout(function() {
      // deferring full initialization to avoid extra delays in page loading, or Ajax reloads including the PopupMenu
      O$.addLoadEvent(finishInitialization);
    }, 100);
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

    /* Update icons and styles if enabled flag has been changed */
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

    /* Update selected class */
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
      if (menuItem._popupMenu._openedChildMenu != null &&
          menuItem._popupMenu._openedChildMenu == O$(menuItem._menuId)) {
        var childMenu = menuItem._popupMenu._openedChildMenu;
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
        if (menuItem._popupMenu._openedChildMenu != null &&
            menuItem._popupMenu._openedChildMenu == O$(menuItem._menuId)) {
          var childMenu = menuItem._popupMenu._openedChildMenu;
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

    var length = popupMenu._items.length;
    var menuItems = popupMenu._items;
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
      O$.PopupMenu._setHighlightMenuItem(popupMenu, popupMenu._items[nextIndex]._anchor, selectDisabledItems);
    }
  },

  _showSubmenu: function(popupMenu, itemAnchor, submenuHorizontalOffset) {
    var menuItem = itemAnchor.parentNode;
    if (popupMenu._closeAllTask) {
      clearTimeout(popupMenu._closeAllTask);
      popupMenu._closeAllTask = null;
    }
    if (menuItem._openSubmenuTask) {
      clearTimeout(menuItem._openSubmenuTask);
      menuItem._openSubmenuTask = null;
    }

    if (menuItem._disabled)
      return;
    if (!itemAnchor._menuId)
      return;
    var childPopupMenu = O$(itemAnchor._menuId);
    if (childPopupMenu.isVisible()) return;
    if (popupMenu._openedChildMenu != childPopupMenu) {
      popupMenu.closeChildMenus();
      var popupMenuRect = O$.getElementBorderRectangle(popupMenu, true);
      popupMenu._openedChildMenu = childPopupMenu;

      var popup_menu_border_top = O$.getNumericElementStyle(popupMenu, "border-top-width");
      var childPopupMenu_border_left = (!O$.isOpera()) ? O$.getNumericElementStyle(popupMenu, "border-left-width") : 0;
      var item_border_left = O$.getNumericElementStyle(itemAnchor, "border-left-width");
      var x = popupMenuRect.width - O$.getElementPos(itemAnchor, true).x - childPopupMenu_border_left - item_border_left + submenuHorizontalOffset;
      var y = -popup_menu_border_top - popupMenu.clientTop - O$.getNumericElementStyle(itemAnchor, "border-top-width");
      childPopupMenu.showAtXY(x, y);
      var visibleRect = O$.getVisibleAreaRectangle();
      var subMenuRect = O$.getElementBorderRectangle(childPopupMenu);
      if (subMenuRect.getMaxX() > visibleRect.getMaxX()) {
        childPopupMenu.setLeft(-subMenuRect.width - childPopupMenu_border_left - item_border_left);
      }
      if (subMenuRect.getMaxY() > visibleRect.getMaxY()) {
        childPopupMenu.setTop(y - (subMenuRect.getMaxY() - visibleRect.getMaxY()) - 5);
      }

      O$.PopupMenu._clearSelection(childPopupMenu);

      childPopupMenu.focus();
    }
    popupMenu._enter = false;
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
    popupMenu.closeChildMenus();
    while (popupMenu) {
      popupMenu.hide();
      popupMenu = popupMenu._parentPopupMenu;
    }
  },

  _initIEWidthWorkaround: function(popupMenu) {
    if (O$.isExplorer() && popupMenu._menuItemsAligned == null) {
      popupMenu._menuItemsAligned = true;
      var maxWidthItem = 0;
      var width;
      popupMenu._items.forEach(function(menuItem) {
        if (!menuItem._isSeparator()) {
          width = menuItem._anchor.offsetWidth;
        }
        if (width > maxWidthItem)
          maxWidthItem = width;
      });
      popupMenu._items.forEach(function(menuItem) {
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
    function substractPaddings(element, paddings, diffs) {
      var len = paddings.length;
      if (diffs.length != len) throw "paddings and diffs arrays should be of the same length";

      for (var i = 0; i < len; i++) {
        var paddingName = paddings[i];
        var diff = diffs[i];
        if (!element._cachedSubstractedPaddings)
          element._cachedSubstractedPaddings = {};

        var padding = element._cachedSubstractedPaddings[paddingName];
        if (!padding)
          padding = element._cachedSubstractedPaddings[paddingName] = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(element, paddingName) - diff) + "px";
        element.style[paddingName] = padding;
      }
    }
    substractPaddings(menuItem._anchor,
            ["paddingTop", "paddingBottom", "paddingLeft", "paddingRight"],
            [menuItem._diffTop, menuItem._diffBottom, menuItem._diffLeft, menuItem._diffRight]);

    if (menuItem._iconspan != null) {
      substractPaddings(menuItem._iconspan,
              ["paddingTop", "paddingBottom", "paddingLeft"],
              [menuItem._diffTop, menuItem._diffBottom, menuItem._diffLeft]);
      var iconSpanStyle = menuItem._iconspan.style;
      if (O$.isExplorer())
        iconSpanStyle.width = O$.calculateNumericCSSValue(O$.getElementStyle(menuItem._iconspan, "width")) - menuItem._diffLeft + "px";
    }

    if (menuItem._arrowspan != null) {
      var arrowSpanStyle = menuItem._arrowspan.style;
      substractPaddings(menuItem._arrowspan,
              ["paddingTop", "paddingBottom", "paddingRight"],
              [menuItem._diffTop, menuItem._diffBottom, menuItem._diffRight]);
      if (O$.isExplorer())
        arrowSpanStyle.width = O$.calculateNumericCSSValue(O$.getElementStyle(menuItem._arrowspan, "width")) - menuItem._diffRight + "px";
    }
  },

  _addPaddingToMenuItem: function(menuItem) {
    function addPaddings(element, paddings, diffs) {
      var len = paddings.length;
      if (diffs.length != len) throw "paddings and diffs arrays should be of the same length";

      for (var i = 0; i < len; i++) {
        var paddingName = paddings[i];
        var diff = diffs[i];
        if (!element._cachedAddedPaddings)
          element._cachedAddedPaddings = {};

        var padding = element._cachedAddedPaddings[paddingName];
        if (!padding)
          padding = element._cachedAddedPaddings[paddingName] = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(element, paddingName) + diff) + "px";
        element.style[paddingName] = padding;
      }
    }

    addPaddings(menuItem._anchor,
            ["paddingTop", "paddingBottom", "paddingLeft", "paddingRight"],
            [menuItem._diffTop, menuItem._diffBottom, menuItem._diffLeft, menuItem._diffRight]);

    if (menuItem._iconspan != null) {
      addPaddings(menuItem._iconspan,
              ["paddingTop", "paddingBottom"],
              [menuItem._diffTop, menuItem._diffBottom]);
      var iconSpanStyle = menuItem._iconspan.style;
      if (O$.isExplorer() && O$.isStrictMode()) {
        iconSpanStyle.paddingLeft = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._iconspan, "padding-left") + menuItem._diffLeft -
                                                                 O$.getNumericElementStyle(menuItem._anchor, "border-left-width", true) -
                                                                 O$.getNumericElementStyle(menuItem._anchor, "border-right-width", true)) + "px";
      }
      else {
        iconSpanStyle.paddingLeft = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._iconspan, "padding-left") + menuItem._diffLeft) + "px";
      }
      if (O$.isExplorer())
        iconSpanStyle.width = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._iconspan, "width") + menuItem._diffLeft) + "px";
    }

    if (menuItem._arrowspan != null) {
      addPaddings(menuItem._arrowspan,
              ["paddingTop", "paddingBottom"],
              [menuItem._diffTop, menuItem._diffBottom]);
      var arrowSpanStyle = menuItem._arrowspan.style;
      if (O$.isExplorer() && O$.isStrictMode()) {
        arrowSpanStyle.paddingRight = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._arrowspan, "padding-right") + menuItem._diffRight -
                                                                   O$.getNumericElementStyle(menuItem._anchor, "border-left-width", true) -
                                                                   O$.getNumericElementStyle(menuItem._anchor, "border-right-width", true)) + "px";
      }
      else {
        arrowSpanStyle.paddingRight = O$.PopupMenu._getNonNegative(O$.getNumericElementStyle(menuItem._arrowspan, "padding-right") + menuItem._diffRight) + "px";
      }
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

    popupMenu._items[0].style.paddingTop = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(popupMenu._items[0], "padding-top")) + paddingTop + "px";
    var lastMenuItem = popupMenu._items[popupMenu._items.length - 1];
    lastMenuItem.style.paddingBottom = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(lastMenuItem, "padding-bottom")) + paddingBottom + "px";
    popupMenu.style.paddingTop = 0;
    popupMenu.style.paddingBottom = 0;
    popupMenu.style.paddingRight = 0;

    for (var i = 0; i < popupMenu._items.length; i++) {
      var menuItem = popupMenu._items[i];
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
      var indentWidth = O$.calculateNumericCSSValue(O$.PopupMenu._getCssProperty(indentClass, "width"));
      var indentAlign = O$.PopupMenu._getCssProperty(indentClass, "text-align");
      var indentPaddingLeft = O$.PopupMenu._getCssProperty(indentClass, "padding-left");
      var indentBorderRight = O$.PopupMenu._getCssProperty(indentClass, "borderRight");
      var indentBorderRightWidth = O$.calculateNumericCSSValue(O$.PopupMenu._getCssProperty(indentClass, "borderRightWidth"));
      menuItem.style.borderLeft = indentBorderRight;
      menuItem.style.marginLeft = indentWidth + "px";

      if (!menuItem._separator) {
        var margin = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(menuItem._anchor, "margin-left"));
        menuItem._anchor.style.marginLeft = (-1 * (indentWidth + indentBorderRightWidth) + margin) + "px";

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
        menuItem._iconspan.style.width = indentWidth + "px";
        menuItem._iconspan.style.textAlign = indentAlign;
        menuItem._iconspan.style.paddingLeft = indentPaddingLeft;
        var paddingLeft = O$.getNumericElementStyle(menuItem._caption, "padding-left");
        menuItem._caption.style.paddingLeft = (paddingLeft + indentWidth + indentBorderRightWidth) + "px";
      } else {
        margin = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(menuItem._separator, "margin-left"));
        var padding = O$.calculateNumericCSSValue(O$.PopupMenu._getStyleProperty(menuItem, "padding-left"));
        menuItem._separator.style.marginLeft = (-1 * (indentWidth + indentBorderRightWidth) + margin + padding) + "px";
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

  _addMenuItemClickHandler: function(menuItem, submenuHorizontalOffset, selectDisabledItems) {
    var popupMenu = menuItem._popupMenu;
    if (!menuItem._properties.disabled) {
      menuItem._click = function(evt) {
        if (menuItem._properties.action) {
          var handler;
          eval("handler = function(event){" + menuItem._properties.action + "}");
          handler.call(menuItem, evt);
          O$.PopupMenu._closeAllMenu(popupMenu);
        } else {
          if (menuItem._menuId) {
            if (menuItem._openSubmenuTask) {
              clearTimeout(menuItem._openSubmenuTask);
              menuItem._openSubmenuTask = null;
            }
            if (popupMenu._parentPopupMenu && popupMenu._parentPopupMenu._closeAllTask) {
              clearTimeout(popupMenu._parentPopupMenu._closeAllTask);
              popupMenu._parentPopupMenu._closeAllTask = null;
            }
            O$.PopupMenu._showSubmenu(popupMenu, menuItem._anchor, submenuHorizontalOffset);
            O$.PopupMenu._showFirstMenuItemonChild(menuItem, selectDisabledItems);
          } else {
            O$.PopupMenu._closeAllMenu(popupMenu);
          }
          O$.stopEvent(evt); //to prevent set focus for the popupMenu
        }

      };
      O$.addEventHandler(menuItem._anchor, "click", function(evt) {
        menuItem._click(evt);
      });
      var clickHandler = menuItem._anchor.onclick;
      if (clickHandler) {
        menuItem._anchor.onclick = function(evt) {
          // ensure hiding the menu _before_ invoking possible Ajax menu reload to avoid menu's visible flag to be
          // saved to the server and thus menu reloading in the visible state in this case
          O$.PopupMenu._closeAllMenu(popupMenu);
          clickHandler.call(menuItem._anchor, evt);
        }
      }
    }
  },

  _addMouseOverOutEvents: function(menuItem, selectDisabledItems, submenuHorizontalOffset, submenuShowDelay, submenuHideDelay) {
    var popupMenu = menuItem._popupMenu;
    var menuItemElement = menuItem._anchor;

    O$.addMouseOverListener(menuItemElement, function() {
      var menuItem = menuItemElement.parentNode;
      if (menuItem._disabled && !selectDisabledItems) return;

      if (popupMenu._parentPopupMenu && popupMenu._parentPopupMenu._closeAllTask) {
        clearTimeout(popupMenu._parentPopupMenu._closeAllTask);
        popupMenu._parentPopupMenu._closeAllTask = null;
      }

      O$.PopupMenu._setHighlightMenuItem(popupMenu, menuItemElement, selectDisabledItems);

      if (popupMenu._closeAllTask) {
        clearTimeout(popupMenu._closeAllTask);
        popupMenu._closeAllTask = null;
      }
      if (submenuShowDelay >= 0)
        menuItem._openSubmenuTask = setTimeout(function() {
          if (menuItemElement._focused)
            O$.PopupMenu._showSubmenu(popupMenu, menuItemElement, submenuHorizontalOffset);
        }, submenuShowDelay);

    });

    O$.addMouseOutListener(menuItemElement, function() {
      var menuItem = menuItemElement.parentNode;
      if (menuItem._disabled && !selectDisabledItems) return;
      menuItemElement._focused = null;

      O$.PopupMenu._unselectMenuItem(menuItemElement.parentNode);

      if (popupMenu._closeAllTask) {
        clearTimeout(popupMenu._closeAllTask);
        popupMenu._closeAllTask = null;
      }

      if (submenuHideDelay >= 0)
        popupMenu._closeAllTask = setTimeout(function() {
          if (!popupMenu.isVisible()) return;
          popupMenu.closeChildMenus();
          popupMenu.focus();
        }, submenuHideDelay);

    });

  }
};


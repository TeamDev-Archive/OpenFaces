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

O$.DropDownField = {
  _init: function(dropDownId,
                  popupTimeout,
                  listAlignment,
                  rolloverPopupItemClass,
                  itemValues,
                  customValueAllowed,
                  required,
                  suggestionMode,
                  suggestionDelay,
                  suggestionMinChars,
                  manualListOpeningAllowed,
                  autoCompleteOn,
                  popupTableStructureAndStyleParams) {
    var dropDown = O$(dropDownId);

    dropDown._listAlignment = listAlignment;
    dropDown._customValueAllowed = customValueAllowed;
    dropDown._required = required;

    dropDown._manualListOpeningAllowed = manualListOpeningAllowed;
    if (suggestionMinChars < 0)
      suggestionMinChars = 0;
    if (!suggestionMode)
      suggestionMode = "none";

    var field = dropDown._field;
    var popup = dropDown._popup;

    dropDown.value = field.value;

    dropDown._keyNavigationStarted = false;
    dropDown._popupTimeout = popupTimeout;
    dropDown._highlightedItemIndex = -1;

    var innerTable = O$(popup.id + "::innerTable");
    innerTable._selectionClass = rolloverPopupItemClass;
    popupTableStructureAndStyleParams.body.rolloverRowClassName = null;
    dropDown._initListStyles = function() {
      var oldCursor = document.body.style.cursor;
      document.body.style.cursor = "progress";
      O$.Tables._init(innerTable, popupTableStructureAndStyleParams);
      document.body.style.cursor = oldCursor;
      O$.repaintWindowForSafari(true);
    };

    function scrollPopupProportionally() {
      if (innerTable.offsetHeight > popup.clientHeight) {
        var scrollPos = dropDown._highlightedItemIndex / (dropDown._items.length - 1);
        popup.scrollTop = (innerTable.offsetHeight - popup.clientHeight) * scrollPos;
      }
    }

    dropDown._getFirstVisibleItemIndex = function() {
      return O$.DropDownField._getNeighboringVisibleItem(dropDown, -1, false);
    };

    dropDown._getLastVisibleItemIndex = function() {
      return O$.DropDownField._getNeighboringVisibleItem(dropDown, dropDown._items.length, true);
    };

    dropDown._getItemVerticalBounds = function(itemIndex) {
      var firstItemIndex = dropDown._getFirstVisibleItemIndex();
      if (firstItemIndex == -1)
        return {top : 0, bottom : 0};
      var firstItem = dropDown._items[firstItemIndex];
      var item = dropDown._items[itemIndex];
      if (!item && firstItem) {
        item = firstItem;
      }
      var itemTop = O$.getElementPos(item).y - O$.getElementPos(innerTable).y;
      var itemBottom = itemTop + item.offsetHeight;
      return {top : itemTop, bottom : itemBottom};
    };

    dropDown._getItemIndexAtY = function(y, searchFromIndex, searchUpwards) {
      for (var i = searchFromIndex, count = dropDown._items.length; i < count && i >= 0; searchUpwards ? i-- : i++) {
        if (!dropDown._items[i]._isVisible())
          continue;
        var bounds = dropDown._getItemVerticalBounds(i);
        if (y >= bounds.top && y < bounds.bottom)
          return i;
      }
      return -1;
    };

    dropDown._scrollToHighlightedItem = function() {
      if (dropDown._highlightedItemIndex == -1) {
        popup.scrollTop = 0;
        return;
      }

      var item = dropDown._items[dropDown._highlightedItemIndex];
      if (!item._isVisible())
        return;
      if (O$.isSafari2()) {
        scrollPopupProportionally();
      } else {
        var bounds = dropDown._getItemVerticalBounds(dropDown._highlightedItemIndex);
        if (!bounds) {
          popup.scrollTop = 0;
          return;
        }
        if (bounds.top < popup.scrollTop)
          popup.scrollTop = bounds.top;
        else if (bounds.bottom > popup.scrollTop + popup.clientHeight)
          popup.scrollTop = bounds.bottom - popup.clientHeight;
      }
    };

    dropDown._addCachedSuggestions = function(
            filterCriterionText,
            newRows, newRowsToStylesMap, newRowCellsToStylesMap, itemValues) {
      var cacheMapKey = filterCriterionText ? filterCriterionText.toLowerCase() : "";
      if (!dropDown._cachedSuggestionLists)
        dropDown._cachedSuggestionLists = [];
      dropDown._cachedSuggestionLists[cacheMapKey] = {
        rows: newRows,
        rowsToStylesMap: newRowsToStylesMap,
        cellsToStylesMap: newRowCellsToStylesMap,
        values: itemValues};
    };

    dropDown._filterCriterion = null;
    dropDown._getFilterCriterion = function() {
      return dropDown._filterCriterion;
    };

    function completeShowingSuggestions(text, autoCompletionAllowedForThisKey) {
      if (dropDown.isOpened())
        O$.DropDown._initPopup(dropDown);

      if (!dropDown.isOpened()) {
        if (text) {
          dropDown.dropDown();
          dropDown.focus();

        }
      }

      var continuationLength = 0;
      var textLength;
      if (text != null) {
        var itemValue = undefined;
        if (autoCompleteOn && (autoCompletionAllowedForThisKey || autoCompletionAllowedForThisKey == undefined)) {
          for (var i = 0, count = dropDown._items.length; i < count; i++) {
            var item = dropDown._items[i];
            if (!item._isVisible())
              continue;
            var itemText = item._itemLabel;
            if (O$.stringStartsWith(itemText.toUpperCase(), text.toUpperCase())) {
              continuationLength = itemText.length - text.length;
              text = itemText;
              itemValue = item._itemValue;
              break;
            }
          }
        }
        dropDown._setValue(text, itemValue, true);
        if (continuationLength) {
          textLength = text.length;
          O$._selectTextRange(field, textLength - continuationLength, textLength);
        }
      } else {
        textLength = field.value.length;
        O$._selectTextRange(field, textLength, textLength);
      }

      var highlightedItemIndex = dropDown._getHighlightedItemIndex();
      if (highlightedItemIndex != -1 && (highlightedItemIndex >= dropDown._items.length || !dropDown._items[highlightedItemIndex]._isVisible())) {
        highlightedItemIndex = -1;
        dropDown._setHighlightedItemIndex(highlightedItemIndex);
      }
      if (highlightedItemIndex == -1 && !customValueAllowed) {
        var firstVisibleItem = dropDown._getFirstVisibleItemIndex();
        dropDown._setHighlightedItemIndex(firstVisibleItem);
      }
      dropDown._scrollToHighlightedItem();
    }

    dropDown._setFilterCriterion = function(text, autoCompletionAllowedForThisKey) {
      if (dropDown._filterCriterion == text)
        return;
      dropDown._filterCriterion = text;
      if (suggestionMode == "custom") {
        if (!dropDown._showCachedSuggestions(text, autoCompletionAllowedForThisKey))
          O$.requestComponentPortions(dropDownId,
                  ["filterCriterion:" + ((text != null) ? "[" + text + "]" : "null")],
                  null,
                  O$.DropDownField._filteredItemsLoaded);
      } else {
        dropDown._filterItems();
        completeShowingSuggestions(text, autoCompletionAllowedForThisKey);
      }
    };

    dropDown._showCachedSuggestions = function (text, autoCompletionAllowedForThisKey) {
      var cacheMapKey = text ? text.toLowerCase() : "";
      var cachedSuggestions = dropDown._cachedSuggestionLists ? dropDown._cachedSuggestionLists[cacheMapKey] : null;
      if (!cachedSuggestions)
        return false;

      var innerTable = O$(dropDown._popup.id + "::innerTable");
      dropDown._setHighlightedItemIndex(-1);
      innerTable.body._removeAllRows();
      innerTable._insertRowsAfter(-1, cachedSuggestions.rows, cachedSuggestions.rowsToStylesMap, cachedSuggestions.cellsToStylesMap);

      // Safari 3 painting fix (JSFC-3270)
      O$.repaintWindowForSafari(true);
      dropDown._setHighlightedItemIndex(-1);
      dropDown._items = O$.DropDownField._prepareListElements(dropDown);
      dropDown._initItems(cachedSuggestions.values);

      completeShowingSuggestions(text, autoCompletionAllowedForThisKey);
      return true;
    };


    dropDown._filterItems = function() {
      function shouldItemBeIncluded(item, filterText) {
        if (suggestionMode == "none" || suggestionMode == "all" || !filterText)
          return true;
        var caseSensitive = false;
        var itemLabel = item._itemLabel;
        if (!caseSensitive) {
          filterText = filterText.toLowerCase();
          itemLabel = itemLabel.toLowerCase();
        }
        if (suggestionMode == "stringStart") {
          return O$.stringStartsWith(itemLabel, filterText);
        } else if (suggestionMode == "substring") {
          var idx = itemLabel.indexOf(filterText);
          return idx != -1;
        } else {
          if (suggestionMode != "stringEnd")
            throw "O$._initDropDownField: Unknown suggestionMode: " + suggestionMode;
          return O$.stringEndsWith(itemLabel, filterText);
        }
      }

      var filterText = dropDown._filterCriterion;
      for (var i = 0, count = dropDown._items.length; i < count; i++) {
        var item = dropDown._items[i];
        var showItem = shouldItemBeIncluded(item, filterText);
        item._setVisible(showItem);
      }
      // Safari 3 painting fix (JSFC-3270)
      O$.repaintWindowForSafari(true);

    };

    function setItemSelected(itemIndex, selected) {
      var item = dropDown._items[itemIndex];
      O$.assert(item, "setItemSelected: couldn't find item at index: " + itemIndex);
      item._selected = selected;
      if (!item._updateStyle) {
        // can be the case in case of O$.destroyAllFunctions functioning during Ajax when
        // org.openfaces.ajaxCleanupRequired init parameter is set to true
        return;
      }
      item._updateStyle();
    }

    dropDown._setHighlightedItemIndex = function(index) {
      if (dropDown._highlightedItemIndex == index)
        return;
      if (dropDown._highlightedItemIndex != -1) {
        setItemSelected(dropDown._highlightedItemIndex, false);
      }
      dropDown._highlightedItemIndex = index;
      if (dropDown._highlightedItemIndex != -1) {
        setItemSelected(dropDown._highlightedItemIndex, true);
      }
    };

    dropDown._getHighlightedItemIndex = function() {
      return dropDown._highlightedItemIndex;
    };

    if (!O$.isSafari()) { // safari can't scroll popup with focus reacquiring functionality
      if (O$.isMozillaFF() || O$.isExplorer()) {
        popup.onmousedown = function() {
          dropDown._reacquireFocus = true;
        };
      } else {
        popup.onfocus = function() {
          dropDown.focus();
        };
      }
    }

    dropDown._items = O$.DropDownField._prepareListElements(dropDown);
    dropDown._initItems = function(itemValues) {
      for (var i = 0, count = dropDown._items.length; i < count; i++) {
        var item = dropDown._items[i];
        item.onmouseover = function() {
          dropDown._setHighlightedItemIndex(this._index);
        };
        item.onmousemove = function() {
          dropDown._setHighlightedItemIndex(this._index);
        };
        item.onclick = function() {
          O$.DropDownField._itemClicked(dropDown, this);
        };
        item.onmouseup = function() {
          O$.DropDownField._itemClicked(dropDown, this);
        };

        var itemData = itemValues[i];
        item._itemValue = itemData[0];
        item._itemLabel = itemData.length == 2 ? itemData[1] : item._itemValue;
        item._select = function() {
          var text = this._itemLabel;
          var itemValue = this._itemValue;
          dropDown._setValue(text, itemValue);
          setTimeout(function() {
            dropDown._setFilterCriterion(null);
          }, 1);
        };
      }
    };
    if (itemValues) {
      dropDown._initItems(itemValues);
      dropDown._addCachedSuggestions(null, dropDown._items,
              popupTableStructureAndStyleParams.rowStylesMap, 
              popupTableStructureAndStyleParams.cellStylesMap,
              itemValues);
    }

    //  O$.DropDownField._onLoadHandler(dropDown.id);

    // Add close on timeout behavior
    if (dropDown._popupTimeout > 0) {
      dropDown._timeOutClose = O$.DropDownField._closeOnTimeout;
      var closeOnTimeout = O$.getEventHandlerFunction("_timeOutClose", "'" + dropDownId + "'", dropDown);
      O$.addEventHandler(dropDown, "mouseout", closeOnTimeout);
      O$.addEventHandler(popup, "mouseout", closeOnTimeout);
    }

    // Set handler that closes popup list on click out of popup
    dropDown.onLoadHandler = O$.DropDownField._onLoadHandler;
    var bodyOnLoadHandler = O$.getEventHandlerFunction("onLoadHandler", "'" + dropDownId + "'", dropDown);
    O$.addEventHandler(window, "load", bodyOnLoadHandler);

    function clearSuggestionTimer() {
      if (dropDown._currentSuggestionTimeoutId) {
        clearTimeout(dropDown._currentSuggestionTimeoutId);
        dropDown._currentSuggestionTimeoutId = null;
      }
    }

    dropDown._selectCurrentValue = function(e) {
      dropDown._checkFieldValueAgainstList(true);
      if (field.value != dropDown.value)
        dropDown._setValue(field.value, undefined, false, e); // the case for clipboard-pasted text
      else
        dropDown._setValue(dropDown.value, dropDown._selectedItemValue, false, e);
    };

    function handleFieldChange(e) {
      dropDown._selectCurrentValue(e);
      if (field._onchangeFlag != undefined)
        field._onchangeFlag = true;
    }

    field.onfocus = function() {
      dropDown._ddf_focused = true;
      if (dropDown._blurWaiter) {
        dropDown._blurWaiter = undefined;
        clearTimeout(dropDown._blurWaiter);
      }
    };

    field.onblur = function(e) {
      dropDown._ddf_focused = false;
      if (dropDown._blurWaiter)
        clearTimeout(dropDown._blurWaiter);
      if (!dropDown._reacquireFocus)
        dropDown._blurWaiter = setTimeout(function() {
          if (field.value != dropDown._promptText) {
            handleFieldChange(e);
          }
          // for IE/Mozilla not firing field change when field is changed explicitly with autocompletion
        }, 50);
      else {
        dropDown._reacquireFocus = undefined;
        dropDown.focus();
      }
    };

    dropDown._fireOnChangeIfChanged = function() {
      if (dropDown._lastOnchangeItemLabel != dropDown.value/* ||
       dropDown._lastOnchangeItemValue != dropDown._selectedItemValue*/) {
        clearSuggestionTimer();
        if (dropDown._ddf_focused)
          O$._selectTextRange(field, field.value.length, field.value.length);

        dropDown._lastOnchangeItemLabel = dropDown.value;
        dropDown._lastOnchangeItemValue = dropDown._selectedItemValue;

        if (dropDown._promptText) {
          if ((dropDown._field.value == dropDown._promptText) && (dropDown._statePrompt.value == "true")) {
            if (dropDown._promptTextClass)
              O$.excludeClassNames(dropDown._field, [dropDown._promptTextClass]);
            dropDown._statePrompt.value = false;
          }
        }

        if (dropDown.client_onchange) {
          var event = O$.createEvent("change");
          var returnedValue = dropDown.client_onchange(event);
          if (returnedValue == undefined)
            returnedValue = event.returnValue;
          return returnedValue;
        }
      }
    };

    dropDown._checkFieldValueAgainstList = function(allowAsynchronousFilterSetting) {
      if (dropDown._customValueAllowed)
        return;
      var preferredText = dropDown.value;
      if (field.value != preferredText) {
        field.value = preferredText;
        if (allowAsynchronousFilterSetting)
          setTimeout(function() {
            dropDown._setFilterCriterion(null);
          }, 1);
        else
          dropDown._setFilterCriterion(null);
        clearSuggestionTimer();
      }
    };

    // Keyboard navigation
    dropDown._handleKeyPress = O$.DropDownField._keyPressHandler;
    field.onkeypress = function (e) {
      var result = dropDown._handleKeyPress(e);
      var evt = O$.getEvent(e);
      var keyCode = evt.keyCode;
      var autoCompletionAllowedForThisKey = keyCode != 8 && keyCode != 46;
      // Backspace, Del

      var valueBefore = field.value;
      field._onchangeFlag = false;
      setTimeout(function() {
        var valueAfter = field.value;
        if (valueBefore == valueAfter)
          return;

        dropDown._setValue(valueAfter, undefined, true);
        if (suggestionMode == "none") {
          if (autoCompleteOn && autoCompletionAllowedForThisKey) {
            clearSuggestionTimer();
            var completeText = function() {
              dropDown._currentSuggestionTimeoutId = null;

              var continuationLength = 0;
              var completedText = valueAfter;
              var completedItemValue = undefined;
              for (var i = 0, count = dropDown._items.length; i < count; i++) {
                var item = dropDown._items[i];
                if (!item._isVisible())
                  continue;
                var itemText = item._itemLabel;
                if (O$.stringStartsWith(itemText.toUpperCase(), valueAfter.toUpperCase())) {
                  continuationLength = itemText.length - valueAfter.length;
                  completedText = itemText;
                  completedItemValue = item._itemValue;
                  break;
                }
              }

              if (continuationLength) {
                dropDown._setValue(completedText, completedItemValue, true);
                var textLength = completedText.length;
                O$._selectTextRange(field, textLength - continuationLength, textLength);
              }
            };
            if (suggestionDelay)
              dropDown._currentSuggestionTimeoutId = setTimeout(completeText, suggestionDelay);
            else
              completeText();

          }
          return;
        }

        var selfModifiedInOnChange = field._onchangeFlag;
        field._onchangeFlag = undefined;
        if (selfModifiedInOnChange)
          return;
        if (field.value.length < suggestionMinChars) {
          if (dropDown.isOpened()) {
            dropDown.closeUp();
            dropDown.focus();
          }
          dropDown._setFilterCriterion(null);
          return;
        }

        clearSuggestionTimer();
        dropDown._currentSuggestionTimeoutId = setTimeout(function() {
          dropDown._currentSuggestionTimeoutId = null;
          dropDown._setFilterCriterion(field.value, autoCompletionAllowedForThisKey);
        }, suggestionDelay);
      }, 1);
      evt.cancelBubble = true;
      return result;
    };

    field._handleKeyDown = O$.DropDownField._keyDownHandler;
    field.onkeydown = function (e) {
      if (O$.isSafari()) {
        if (field._skipRepetitiveOnkeydown) {
          field._skipRepetitiveOnkeydown = undefined;
          return;
        }
        field._skipRepetitiveOnkeydown = true;
        setTimeout(function() {
          field._skipRepetitiveOnkeydown = undefined;
        }, 1);
      }
      var result = field._handleKeyDown(e);
      // IE doesn't react on arrows in onKeyPress
      if (!O$.isExplorer())
        return result;
      var evt = O$.getEvent(e);
      var keyCode = evt.keyCode;
      if (keyCode == 8 || keyCode == 46) // Tab or Del
        field.onkeypress(e);
      return result;
    };

    dropDown.getValue = function () {
      return dropDown.value;
    };

    dropDown.setValue = function (text) {
      dropDown._setValue(text);
    };

    dropDown._setValue = function (text, itemValue, onChangeDisabled, initiatingEvent) {
      if (text) { // retain only the first line because user can't enter new-line anyway
        var idx = text.indexOf("\n");
        if (idx != -1)
          text = text.substring(0, idx);
      }
      if (!text)
        text = "";

      var existingListItemRequired = !dropDown._customValueAllowed;
      if (!required) {
        if (!text) {
          itemValue = null;
          existingListItemRequired = false;
        }
      }
      if (existingListItemRequired) {
        if (itemValue == undefined) {
          var item = dropDown._findItemByLabel(text);
          if (!item)
            return; // retain old value when setting a value not from the list
          itemValue = item._itemValue;
          text = item._itemLabel;
          // to account for case-insensitive text search in _findItemByLabel
        }
      }

      if (dropDown.value != text) {
        dropDown.value = text;
      }
      var newItemValue = itemValue != undefined
              ? (itemValue != null ? "[" + itemValue + "]" : "null")
              : "";
      if (dropDown._valueField.value != newItemValue) {
        dropDown._valueField.value = newItemValue;
      }
      dropDown._selectedItemValue = itemValue;
      if (dropDown.isOpened()) {
        dropDown._highlightSelectedItem();
        dropDown._scrollToHighlightedItem();
      }
      if (field.value != text)
        field.value = text;
      if (!onChangeDisabled) {
        if (dropDown._fireOnChangeIfChanged() === false) {
          var evt = O$.getEvent(initiatingEvent);
          if (evt) {
            if (evt.preventDefault)
              evt.preventDefault();
            evt.returnValue = false;
          }
        }
      }
    };

    dropDown.dropDown = function () {
      if (dropDown.isOpened())
        return false;
      popup._prepareForRearrangementBeforeShowing();
      O$.DropDown._initPopup(dropDown);
      O$.correctElementZIndex(popup, dropDown);
      popup.show();
      dropDown._keyNavigationStarted = false;
      return true;
    };

    dropDown.closeUp = function () {
      if (!dropDown.isOpened())
        return false;
      popup.hide();
      dropDown._keyNavigationStarted = false;
      return true;
    };

    dropDown.isOpened = function () {
      return popup.isVisible();
    };

    dropDown._getSelectedItem = function() {
      var selectedItem = null;
      var currentValue = dropDown._selectedItemValue;
      if (currentValue != undefined) {
        for (var i = 0, count = dropDown._items.length; i < count; i++) {
          var item = dropDown._items[i];
          if (item._itemValue == currentValue) {
            selectedItem = item;
            break;
          }
        }
      }
      return selectedItem;
    };

    dropDown._highlightSelectedItem = function() {
      var itemToHighlight = dropDown._getSelectedItem();
      dropDown._setHighlightedItemIndex(itemToHighlight ? itemToHighlight._index : -1);
    };
    dropDown._findItemByLabel = function (text) {
      function itemByLabel(text, caseSensitive) {
        for (var i = 0, count = dropDown._items.length; i < count; i++) {
          var item = dropDown._items[i];
          var itemFound = caseSensitive ? item._itemLabel == text : O$.stringsEqualIgnoreCase(item._itemLabel, text);
          if (itemFound)
            return item;
        }
        return null;
      }

      var item = itemByLabel(text, true);
      if (!item)
        item = itemByLabel(text, false);
      return item;
    };

    dropDown._showHidePopup = function () {
      if (dropDown.isOpened())
        dropDown.closeUp();
      else
        dropDown.dropDown();

      // reacquire focus when pressing the dropper button
      if (!dropDown._ddf_focused) {
        if (dropDown._field.value != dropDown._promptText) {
          dropDown.focus();
        }
        // focus is already lost on Mozilla and Opera
      } else {
        dropDown._reacquireFocus = true;
        // field is still in focus under IE
      }
    };

    dropDown._visibilityChangeListener = function() {
      if (popup.isVisible()) {
        if (dropDown.ondropdown)
          dropDown.ondropdown();
        O$.repaintAreaForOpera(popup, true);
        dropDown._highlightSelectedItem();
        dropDown._scrollToHighlightedItem();
        if (O$.isExplorer()) {
          // the second _scrollToHighlightedItem is added for IE, where the first invokation doesn't always manage to
          // scroll correctly (probably because of appearing of higher multi-line items as a result of popup sizing)
          dropDown._scrollToHighlightedItem();
        }
      } else {
        if (dropDown.oncloseup)
          dropDown.oncloseup();
        O$.repaintAreaForOpera(document.body, true);
      }
    };
    popup._addVisibilityChangeListener(dropDown._visibilityChangeListener);
    dropDown._clientValueFunctionExists = true;
    dropDown._clientValueFunction = function() {
      if (dropDown._skipValidation) {
        return dropDown._initialText;
      }
      return dropDown.value;
    };

    var valueField = O$(dropDown.id + "::value");
    dropDown._valueField = valueField;

    dropDown._initValue = function (initialText) {
      var initialItemValue = valueField.value;
      if (initialItemValue == "")
        initialItemValue = null;
      else {
        initialItemValue = initialItemValue.substring(1, initialItemValue.length - 1);
      }
      dropDown._setValue(initialText, initialItemValue, true);
      dropDown._lastOnchangeItemLabel = dropDown.value;
      dropDown._lastOnchangeItemValue = dropDown._selectedItemValue;

      setTimeout(dropDown._initListStyles, 10);
    };
  },

  _onLoadHandler: function(controlId) {
    var dropDown = O$(controlId);

    var button = dropDown._button;
    if (!button)
      return;

    var field = dropDown._field;

    // Workaround for IE (dropdown field expands when field width is 100% and styles are aplied by js on mouseover/mouseout)
    if (O$.isExplorer() && (dropDown._fieldClass != dropDown._rolloverFieldClass || dropDown._containerClass != dropDown._rolloverContainerClass)) {
      setTimeout(function() {
        O$.DropDownField._fixSize(dropDown);
      }, 100);
    }

    if (button.clientHeight < field.clientHeight) {
      button.style.height = field.clientHeight + "px";
      O$.repaintAreaForOpera(dropDown.parentNode, true);
      // JSFC-2224
    }

  },

  _fixSize: function(dropDown) {
    var field = dropDown._field;
    if (field.clientWidth > 0) {
      var tdCell = field.parentNode;
      field.style.width = tdCell.offsetWidth + "px";
      O$.repaintAreaForOpera(dropDown, true);
    } else {
      setTimeout(function() {
        O$.DropDownField._fixSize(dropDown);
      }, 200);
    }
  },

  _keyPressHandler: function(e) {
    var evt = O$.getEvent(e);
    var needCancelBubble = false;
    var keyCode = evt.keyCode;
    var dropDown = this;
    var popup = dropDown._popup;

    var dropDownOpened = dropDown.isOpened();
    if (dropDownOpened && (keyCode == 13 || keyCode == 27)) { // Enter || Esc
      needCancelBubble = true;
      dropDown._keyNavigationStarted = false;
      popup.hide();
      if (keyCode == 13) {
        var currentItemIndex = dropDown._getHighlightedItemIndex();
        if (currentItemIndex != -1) {
          var dropDownItem = dropDown._items[currentItemIndex];
          dropDownItem._select();
        } else {
          dropDown._selectCurrentValue();
        }
      }
    } else {
      if (!dropDownOpened && keyCode == 13)
        dropDown._selectCurrentValue();
      if (dropDown.client_onkeypress)
        return dropDown.client_onkeypress(evt);
    }

    if (needCancelBubble) {
      evt.cancelBubble = true;
      return false;
    }
    return true;
  },


  _getNeighboringVisibleItem: function(dropDown, itemIndex, searchUpwards) {
    var itemCount = dropDown._items.length;
    var i = itemIndex;
    if (searchUpwards)
      i--;
    else
      i++;
    for (; i >= 0 && i < itemCount; searchUpwards ? i-- : i++) {
      var currentItem = dropDown._items[i];
      if (currentItem._isVisible())
        return i;
    }
    var item = dropDown._items[itemIndex];
    if (!item) {
      itemIndex = -1;
    }
    return itemIndex;
  },

  _keyDownHandler: function(e) {
    var evt = O$.getEvent(e);
    var keyCode = evt.keyCode;
    var dropDown = this._dropDown;
    var popup = dropDown._popup;
    var needCancelBuble = false;
    var highlightedItemIndex = dropDown._getHighlightedItemIndex();
    var newHighlightedItemIndex = null;
    if (keyCode == 40) { // Down
      if (!dropDown.isOpened()) {
        if (!dropDown._manualListOpeningAllowed)
          return true;
        popup._prepareForRearrangementBeforeShowing();
        O$.DropDown._initPopup(dropDown);
        O$.correctElementZIndex(popup, dropDown);
        popup.show();
      } else {
        // Change selected index
        newHighlightedItemIndex = O$.DropDownField._getNeighboringVisibleItem(dropDown, highlightedItemIndex, false);
      }

      needCancelBuble = true;
      if (dropDown._popupCloseTimer) {
        clearTimeout(dropDown._popupCloseTimer);
      }
      dropDown._keyNavigationStarted = true;

    }
    var itemBounds;
    if (dropDown.isOpened()) {
      if (keyCode == 38) { // Up
        needCancelBuble = true;
        dropDown._keyNavigationStarted = true;
        newHighlightedItemIndex = O$.DropDownField._getNeighboringVisibleItem(dropDown, highlightedItemIndex, true);
      } else if (keyCode == 36) { // Home
        needCancelBuble = true;
        dropDown._keyNavigationStarted = true;
        newHighlightedItemIndex = dropDown._getFirstVisibleItemIndex();
      } else if (keyCode == 35) { // End
        needCancelBuble = true;
        dropDown._keyNavigationStarted = true;
        newHighlightedItemIndex = dropDown._getLastVisibleItemIndex();
      } else if (keyCode == 33) { // Page Up
        needCancelBuble = true;
        dropDown._keyNavigationStarted = true;
        itemBounds = dropDown._getItemVerticalBounds(highlightedItemIndex);
        var newScrollTop = itemBounds.top - popup.offsetHeight;

        newHighlightedItemIndex = dropDown._getItemIndexAtY(newScrollTop + 20, highlightedItemIndex, true);
        if (newHighlightedItemIndex >= highlightedItemIndex)
          newHighlightedItemIndex = O$.DropDownField._getNeighboringVisibleItem(dropDown, highlightedItemIndex, false);
        if (newHighlightedItemIndex == -1)
          newHighlightedItemIndex = dropDown._getFirstVisibleItemIndex();
      } else if (keyCode == 34) { // Page Down
        needCancelBuble = true;
        dropDown._keyNavigationStarted = true;
        itemBounds = dropDown._getItemVerticalBounds(highlightedItemIndex);
        var newScrollBottom = itemBounds.bottom + popup.offsetHeight;

        newHighlightedItemIndex = dropDown._getItemIndexAtY(newScrollBottom - 20, highlightedItemIndex, false);
        if (newHighlightedItemIndex != -1 && newHighlightedItemIndex <= highlightedItemIndex)
          newHighlightedItemIndex = O$.DropDownField._getNeighboringVisibleItem(dropDown, highlightedItemIndex, false);
        if (newHighlightedItemIndex == -1)
          newHighlightedItemIndex = dropDown._getLastVisibleItemIndex();
      }
    }
    if (keyCode == 9) { // Tab
      dropDown._popupOpened = false;
      popup.hide();
    }

    if (newHighlightedItemIndex != null && newHighlightedItemIndex != highlightedItemIndex) {
      dropDown._setHighlightedItemIndex(newHighlightedItemIndex);
      if (dropDown.onDropdownNavigation)
        dropDown.onDropdownNavigation();  
      dropDown._scrollToHighlightedItem();
    }

    if (needCancelBuble) {
      evt.cancelBubble = true;
      return true;
    }
    return true;
  },

  _itemClicked: function(dropDown, dropDownItem) {
    dropDown.closeUp();
    dropDown.focus();
    if (dropDown._dropDownMouseOut)
      dropDown._dropDownMouseOut();
    setTimeout(function() {
      dropDownItem._select();
    }, 1);
    // for JSFC-1624 to work under IE (focus-tracker doesn't save focus if submit is going from onchange and change is made with mouse)
  },

  _closeOnTimeout: function(controlId) {
    var dropDown = O$(controlId);
    if (dropDown._popupCloseTimer) {
      clearTimeout(dropDown._popupCloseTimer);
    }
    if (!dropDown._keyNavigationStarted) {
      dropDown._popupCloseTimer = setTimeout(function() {
        var popup = dropDown._popup;
        popup.hide();
      }, dropDown._popupTimeout);
    }
  },

  _prepareListElements: function(dropDown) {
    var itemList = [];
    for (var index = 0, item; item = O$(dropDown.id + "--popup::popupItem" + index); index++) {
      item._index = index;
      itemList.push(item);
    }
    return itemList;
  },

  _setFieldOnChange: function(controlId, func) {
    var dropDown = O$(controlId);
    dropDown.client_onchange = func;
  },

  _setOnKeyPress: function(controlId, func) {
    var dropDown = O$(controlId);
    dropDown.client_onkeypress = func;
  },

  _setOnDropDown: function(controlId, func) {
    var dropDown = O$(controlId);
    dropDown.ondropdown = func;
  },

  _setOnCloseUp: function(controlId, func) {
    var dropDown = O$(controlId);
    dropDown.oncloseup = func;
  },


  _filteredItemsLoaded: function(dropDownField, portionName, portionHTML, portionScripts) {
    var sepIdx = portionName.indexOf(":");
    var filterCriterionStr = portionName.substring(sepIdx + 1);
    var filterCriterion;
    if (filterCriterionStr == "null")
      filterCriterion = null;
    else {
      O$.assert(filterCriterionStr.charAt(0) == "[", "O$.DropDownField._filteredItemsLoaded: Unrecognized filterCriterionStr: " + filterCriterionStr);
      filterCriterion = filterCriterionStr.substring(1, filterCriterionStr.length - 1);
    }

    var cacheOnly = dropDownField._field.value != filterCriterion;

    var tempDiv = document.createElement("div");
    tempDiv.style.display = "none";
    tempDiv.innerHTML = "<table><tbody>" + portionHTML + "</tbody></table>";
    if (cacheOnly)
      document.body.appendChild(tempDiv);
    try {
      var tableBody = tempDiv.getElementsByTagName("tbody")[0];
      var children = tableBody.childNodes;
      var newNodes = [];
      var i, count;
      for (i = 0,count = children.length; i < count; i++) {
        var child = children[i];
        newNodes.push(child);
      }
      var newRows = [];
      for (i = 0,count = newNodes.length; i < count; i++) {
        var newNode = newNodes[i];
        if (!newNode || !newNode.tagName)
          continue;
        var tagName = newNode.tagName.toUpperCase();
        if (tagName == "TR") {
          newRows.push(newNode);
        }
      }

      dropDownField.__acceptLoadedItems = function(newRowsToStylesMap, newRowCellsToStylesMap, itemValues) {
        dropDownField.__acceptLoadedItems = undefined;
        dropDownField._addCachedSuggestions(filterCriterion, newRows, newRowsToStylesMap, newRowCellsToStylesMap, itemValues);
        if (cacheOnly)
          return;

        dropDownField._showCachedSuggestions(filterCriterion);
      };
      O$.executeScripts(portionScripts);
    } finally {
      if (cacheOnly)
        document.body.removeChild(tempDiv);
    }

  },

  _acceptLoadedItems: function(dropDownFieldId, newItemParams) {
    var dropDownField = O$(dropDownFieldId);
    dropDownField.__acceptLoadedItems.apply(dropDownField, newItemParams);
  }



};
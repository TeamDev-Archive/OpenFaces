/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

O$.DropDownField = {
  _init: function(dropDownId, popupTimeout, listAlignment, rolloverPopupItemClass, itemValues, customValueAllowed,
                  required, suggestionMode, suggestionDelay, suggestionMinChars, manualListOpeningAllowed,
                  autoCompleteOn, totalItemCount, pageSize, popupTableStructureAndStyleParams, cachingAllowed,
                  itemPresentationColumn, changeValueOnSelect) {
    var dropDown = O$(dropDownId);
    var super_ = O$.extend(dropDown, {
      _listAlignment: listAlignment,
      _customValueAllowed: customValueAllowed,
      _required: required,
      _manualListOpeningAllowed: manualListOpeningAllowed,
      _keyNavigationStarted: false,
      _popupTimeout: popupTimeout,
      _highlightedItemIndex: -1,
      _cachingAllowed: cachingAllowed,
      _itemPresentation: O$(dropDownId + "::itemPresentation"),
      _itemPresentationContainer: O$(dropDownId + "::itemPresentation::container"),
      _itemPresentationColumn: itemPresentationColumn,
      _fieldContainer: O$(dropDownId + "::field::container"),
      _changeValueOnSelect:changeValueOnSelect,

      _showPresentationPromptText:function (promptText) {
        var td = document.createElement("td");
        td.innerHTML = dropDown._promptText || "&nbsp;";
        while (itemPresentation.hasChildNodes()) {
          itemPresentation.removeChild(itemPresentation.childNodes[0]);
        }
        itemPresentation.appendChild(td);
      },

      _copyClassesToItemPresentation:function () {

        function removeClassFromСlassName(className, classesToRemove) {
          var newClassName = className;
          for (var i = 0; i < classesToRemove.length; i++) {
            if (classesToRemove[i]) {
              newClassName = newClassName.replace(new RegExp("\\b" + classesToRemove[i] + "\\b"), "");
            }
          }
          return newClassName;
        }

        if (dropDown._items == null || dropDown._items.length == 0)
          return;
        var oddRowClassName = popupTableStructureAndStyleParams.body.oddRowClassName;
        var selectedRow = dropDown._getSelectedItem() || dropDown._items[0];
        var rowTable = selectedRow._table;
        if (rowTable) {
          itemPresentation.className = removeClassFromСlassName(selectedRow.className, [rowTable._selectionClass, oddRowClassName]);
          itemPresentation.style.cssText = selectedRow.style.cssText;
          for (var i = 0; i < itemPresentation.childNodes.length; i++) {
            var tdElemnt = itemPresentation.childNodes[i];
            O$.excludeClassNames(tdElemnt, [rowTable._selectionClass, oddRowClassName]);
            tdElemnt.style.borderBottom = "";
          }
          if (itemPresentation.childNodes.length == 1){
            itemPresentation.childNodes[0].style.borderRight = "";
          }
        }
      },

      _initListStyles: function() {
        var oldCursor = document.body.style.cursor;
        document.body.style.cursor = "progress";
        O$.initUnloadableComponent(innerTable);
        O$.Tables._init(innerTable, popupTableStructureAndStyleParams);
        document.body.style.cursor = oldCursor;
        if (itemPresentation) {
          if (dropDown._getSelectedItem())
            dropDown._setItemPresentationValue();
          else
            dropDown._copyClassesToItemPresentation();
        }
        O$.repaintWindowForSafari(true);
      },

      _getFirstVisibleItemIndex: function() {
        return O$.DropDownField._getNeighboringVisibleItem(dropDown, -1, false);
      },

      _getLastVisibleItemIndex: function() {
        return O$.DropDownField._getNeighboringVisibleItem(dropDown, dropDown._items.length, true);
      },

      _getItemVerticalBounds: function(itemIndex) {
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
      },

      _getItemIndexAtY: function(y, searchFromIndex, searchUpwards) {
        for (var i = searchFromIndex, count = dropDown._items.length; i < count && i >= 0; searchUpwards ? i-- : i++) {
          if (!dropDown._items[i]._isVisible())
            continue;
          var bounds = dropDown._getItemVerticalBounds(i);
          if (y >= bounds.top && y < bounds.bottom)
            return i;
        }
        return -1;
      },

      _scrollToHighlightedItem: function() {
        if (dropDown._highlightedItemIndex == -1) {
          popup.scrollTop = 0;
          return;
        }

        var item = dropDown._items[dropDown._highlightedItemIndex];
        if (!item._isVisible())
          return;
        if (O$.isSafari2()) {
          function scrollPopupProportionally() {
            if (innerTable.offsetHeight > popup.clientHeight) {
              var scrollPos = dropDown._highlightedItemIndex / (dropDown._items.length - 1);
              popup.scrollTop = (innerTable.offsetHeight - popup.clientHeight) * scrollPos;
            }
          }

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
      },

      _addCachedSuggestions: function(filterCriterionText, newRows, newRowsToStylesMap, newRowCellsToStylesMap, itemValues, appendItems) {
        var cacheMapKey = filterCriterionText ? filterCriterionText.toLowerCase() : "";
        if (!dropDown._cachedSuggestionLists)
          dropDown._cachedSuggestionLists = [];
        if (!appendItems || !dropDown._cachedSuggestionLists[cacheMapKey]) {
          dropDown._cachedSuggestionLists[cacheMapKey] = {
            rows: newRows,
            rowsToStylesMap: newRowsToStylesMap,
            cellsToStylesMap: newRowCellsToStylesMap,
            values: itemValues};
          return 0;
        } else {
          var suggestionsData = dropDown._cachedSuggestionLists[cacheMapKey];
          var prevSuggestionCount = suggestionsData.rows.length;
          suggestionsData.rows = suggestionsData.rows.concat(newRows);
          O$.extend(suggestionsData.rowsToStylesMap, newRowsToStylesMap);
          O$.extend(suggestionsData.cellsToStylesMap, newRowCellsToStylesMap);
          suggestionsData.values = suggestionsData.values.concat(itemValues);
          return prevSuggestionCount;
        }
      },

      _filterCriterion: null,
      _getFilterCriterion: function() {
        return dropDown._filterCriterion;
      },

      _checkAdditionalPageNeeded: function(forceReload, loadFullList) {
        if (suggestionMode != "custom" || (dropDown._cachingAllowed && dropDown._filterCriterion)) return;
        var itemsLoaded = forceReload ? 0 : dropDown._items.length;
        if (totalItemCount != -1) {
          if (itemsLoaded >= totalItemCount) return;
        } else {
          if (itemsLoaded > 0) return;
        }

        if (!forceReload && innerTable.offsetHeight > popup.clientHeight) {
          var bottomEdge = popup.scrollTop + popup.clientHeight;
          var unrevealedBottomSpace = innerTable.offsetHeight - bottomEdge;
          if (unrevealedBottomSpace > 50) return;
        }

        if (!forceReload && dropDown._additionalPageRequestedAfterItemCount == itemsLoaded) return;
        dropDown._additionalPageRequestedAfterItemCount = itemsLoaded;

        var text = dropDown._filterCriterion;
        var rowCount = totalItemCount - itemsLoaded;
        if (pageSize != -1 && rowCount > pageSize)
          rowCount = pageSize;
        O$.Ajax.requestComponentPortions(dropDownId,
                ["filterCriterion:" + ((text != null && !loadFullList) ? "[" + text + "]" : "null")],
                "{pageStart: " + itemsLoaded + ", pageSize: " + rowCount + ", forceReload:" + (forceReload === true) + "}",
                O$.DropDownField._filteredItemsLoaded);
      },

      _setFilterCriterion: function(text, autoCompletionAllowedForThisKey) {
        if (dropDown._filterCriterion == text)
          return;
        dropDown._filterCriterion = text;
        if (suggestionMode == "custom") {
          if (!dropDown._showCachedSuggestions(text, autoCompletionAllowedForThisKey))
            O$.Ajax.requestComponentPortions(dropDownId,
                    ["filterCriterion:" + ((text != null) ? "[" + text + "]" : "null")],
                    null,
                    O$.DropDownField._filteredItemsLoaded);
          else {
            dropDown._checkAdditionalPageNeeded();
          }
        } else {
          dropDown._filterItems();
          dropDown._completeShowingSuggestions(text, autoCompletionAllowedForThisKey);
        }
      },

      _clearSuggestions: function() {
        dropDown._filterCriterion = dropDown._field.value;
        dropDown._showSuggestions(dropDown._field.value, null, false);
      },

      _showSuggestions: function(text, cachedSuggestions, autoCompletionAllowedForThisKey, itemsAppended) {
        var innerTable = O$(dropDown.id + "--popup::innerTable");
        if (itemsAppended == undefined) {
          dropDown._setHighlightedItemIndex(-1);
          innerTable.body._removeAllRows();
          if (cachedSuggestions)
            innerTable._insertRowsAfter(-1, cachedSuggestions.rows, cachedSuggestions.rowsToStylesMap, cachedSuggestions.cellsToStylesMap);
        } else if (cachedSuggestions) {
          var from = cachedSuggestions.rows.length - itemsAppended;
          innerTable._insertRowsAfter(
                  innerTable.body._getRows().length - 1,
                  cachedSuggestions.rows.concat([]).splice(from, itemsAppended),
                  cachedSuggestions.rowsToStylesMap, cachedSuggestions.cellsToStylesMap);
        }
        // Safari 3 painting fix (JSFC-3270)
        O$.repaintWindowForSafari(true);
        if (itemsAppended == undefined)
          dropDown._setHighlightedItemIndex(-1);
        dropDown._items = O$.DropDownField._prepareListElements(dropDown);
        if (cachedSuggestions)
          dropDown._initItems(cachedSuggestions.values, itemsAppended ? cachedSuggestions.values.length - itemsAppended : 0);
        if (itemsAppended == undefined)
          dropDown._completeShowingSuggestions(text, autoCompletionAllowedForThisKey);
      },


      _showCachedSuggestions: function (text, autoCompletionAllowedForThisKey, itemsLoadedCallback, itemsAppended) {
        var cacheMapKey = text ? text.toLowerCase() : "";
        var cachedSuggestions = dropDown._cachedSuggestionLists ? dropDown._cachedSuggestionLists[cacheMapKey] : null;
        if (!cachedSuggestions)
          return false;

        if (!dropDown._cachingAllowed && !itemsLoadedCallback) {
          // if caching is not allowed, issue a new request each time suggestions are needed
          return false;
        }
        dropDown._showSuggestions(text, cachedSuggestions, autoCompletionAllowedForThisKey, itemsAppended);
        return true;
      },

      _completeShowingSuggestions: function(text, autoCompletionAllowedForThisKey) {
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
            dropDown._setValue(text, itemValue, true);
            if (continuationLength) {
              textLength = text.length;
              O$._selectTextRange(field, textLength - continuationLength, textLength);
            }
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
      },

      _filterItems: function() {
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

      },
      _setHighlightedItemIndex: function(index) {
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

        if (dropDown._highlightedItemIndex == index)
          return;
        if (dropDown._highlightedItemIndex != -1) {
          setItemSelected(dropDown._highlightedItemIndex, false);
        }
        dropDown._highlightedItemIndex = index;
        if (dropDown._highlightedItemIndex != -1) {
          setItemSelected(dropDown._highlightedItemIndex, true);
        }
      },

      _getHighlightedItemIndex: function() {
        return dropDown._highlightedItemIndex;
      },
      _initItems: function(itemValues, fromIndex) {
        for (var i = fromIndex, count = dropDown._items.length; i < count; i++) {
          var item = dropDown._items[i];
          var itemData = itemValues[i];
          O$.extend(item, {
            _itemValue: itemData[0],
            _itemLabel: itemData.length == 2 ? itemData[1] : itemData[0],

            onmouseover: function() {
              dropDown._setHighlightedItemIndex(this._index);
            },
            onmousemove: function() {
              dropDown._setHighlightedItemIndex(this._index);
            },
            onclick: function() {
              O$.DropDownField._itemClicked(dropDown, this);
            },
            onmouseup: function() {
              O$.DropDownField._itemClicked(dropDown, this);
            },

            _select: function() {
              var text = this._itemLabel;
              var itemValue = this._itemValue;
              dropDown._setValue(text, itemValue);
              dropDown._filterCriterion = null;
            }
          });


        }
      },

      _selectCurrentValue: function(e) {
        dropDown._checkFieldValueAgainstList(true);
        if (field.value != dropDown.value) {
          dropDown._setValue(field.value, undefined, false, e); // the case for clipboard-pasted text
        } else {
          dropDown._setValue(dropDown.value, dropDown._selectedItemValue, false, e);
        }
      },

      _fireOnChangeIfChanged: function() {
        if (dropDown._lastOnchangeItemLabel != dropDown.value/* ||
         dropDown._lastOnchangeItemValue != dropDown._selectedItemValue*/) {
          clearSuggestionTimer();
          if (dropDown._ddf_focused)
            O$._selectTextRange(field, field.value.length, field.value.length);

          dropDown._lastOnchangeItemLabel = dropDown.value;
          dropDown._lastOnchangeItemValue = dropDown._selectedItemValue;

          if (dropDown._promptText) {
            if ((dropDown._field.value == dropDown._promptText) && (dropDown._promptVisible.value == "true")) {
              if (dropDown._promptTextClass)
                O$.excludeClassNames(dropDown._field, [dropDown._promptTextClass]);
              dropDown._promptVisible.value = false;
            }
          }

          if (dropDown.onchange_adapted) {
            var event = O$.createEvent("change");
            var returnedValue = dropDown.onchange_adapted(event);
            if (returnedValue == undefined)
              returnedValue = event.returnValue;
            return returnedValue;
          }
        }
      },

      _checkFieldValueAgainstList: function(allowAsynchronousFilterSetting) {
        if (dropDown._customValueAllowed)
          return;
        var preferredText = dropDown._field.oldValue != undefined?dropDown._field.oldValue:"";
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
      },

      getValue: function () {
        return dropDown.value;
      },

      setValue: function (text) {
        dropDown._setValue(text);
      },

      _setItemPresentationValue:function () {

        var selectedItem = dropDown._getSelectedItem();
        if (!selectedItem){
          dropDown._showPresentationPromptText(dropDown._promptText);
          return;
        }
        var newItemPresentationContentNode = selectedItem.cloneNode(true);
        O$.removeIdsFromNode(newItemPresentationContentNode);

        // Remove old values from itemPresentation field
        while (itemPresentation.hasChildNodes()) {
          itemPresentation.removeChild(itemPresentation.childNodes[0]);
        }

        // Copy values from selected item to itemPresentation field
        // Copy all columns for _itemPresentationColumn == -1 and copy only _itemPresentationColumn-th column for other values
        if (dropDown._itemPresentationColumn == -1) {
          while (newItemPresentationContentNode.hasChildNodes()) {
            itemPresentation.appendChild(newItemPresentationContentNode.childNodes[0]);
          }
        } else {
          if (dropDown._itemPresentationColumn < newItemPresentationContentNode.childNodes.length)
            itemPresentation.appendChild(newItemPresentationContentNode.childNodes[dropDown._itemPresentationColumn]);
        }
        O$.setElementSize(dropDown._itemPresentationContainer, {height : dropDown.offsetHeight-2});
        dropDown._copyClassesToItemPresentation();
      },

      _setValue: function (text, itemValue, onChangeDisabled, initiatingEvent) {
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
            if (!item){
              return; // retain old value when setting a value not from the list
            }
            itemValue = item._itemValue;
            text = item._itemLabel;
            // to account for case-insensitive text search in _findItemByLabel
          }
        }

        if (dropDown.value != text) {
          dropDown.value = text;
          dropDown._field.oldValue = text;
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
        if (itemPresentation)
          dropDown._setItemPresentationValue();
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
      },

      dropDown: function () {
        if (dropDown.isOpened())
          return false;
        popup._prepareForRearrangementBeforeShowing();
        O$.DropDown._initPopup(dropDown);
        O$.correctElementZIndex(popup, dropDown);
        popup.show();
        dropDown._keyNavigationStarted = false;
        return true;
      },

      closeUp: function () {
        if (!dropDown.isOpened())
          return false;
        popup.hide();
        dropDown._keyNavigationStarted = false;
        return true;
      },

      isOpened: function () {
        return popup.isVisible();
      },

      _getSelectedItem: function() {
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
      },

      _highlightSelectedItem: function() {
        var itemToHighlight = dropDown._getSelectedItem();
        dropDown._setHighlightedItemIndex(itemToHighlight ? itemToHighlight._index : -1);
      },

      _findItemByLabel: function (text) {
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
      },

      _showHidePopup: function () {
        if (dropDown.isOpened())
          dropDown.closeUp();
        else {
          dropDown.dropDown();
          if (!dropDown._cachingAllowed)
            dropDown._clearSuggestions();
          dropDown._checkAdditionalPageNeeded(!dropDown._cachingAllowed, true);
        }

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
      },

      _visibilityChangeListener: function() {
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
      },

      setDisabled: function(disabled) {
        super_.setDisabled.call(this, disabled);
      },

      _setCustomEvents: function(events) {
        O$.extend(this, events);
      },

      _selectValueByKeyCode: function (keyCode) {
        var currentItemIndex = dropDown._getHighlightedItemIndex();
        if (keyCode == 13 || (dropDown._changeValueOnSelect && (keyCode == 38 || keyCode == 40))) {
          if (currentItemIndex == -1) {
            dropDown._selectCurrentValue();
          } else {
            var dropDownItem = dropDown._items[currentItemIndex];
            dropDownItem._select();
          }
        }
      }
    });

    if (suggestionMinChars < 0)
      suggestionMinChars = 0;
    if (!suggestionMode)
      suggestionMode = "none";

    var field = dropDown._field;
    var popup = dropDown._popup;
    var itemPresentation = dropDown._itemPresentation;
    field._oldValue = "";
    // Switch off browser input field autocomplete
    field.setAttribute("autocomplete","off");

    if (itemPresentation) {
      O$.extend(field, {
        _show:function () {
          if (suggestionMode == "none" || !field._hidden)
            return;
          if (field._hidden) {
            field._oldValue = dropDown._selectedItemValue;
            O$.setElementSize(dropDown._fieldContainer, O$.getElementSize(dropDown._itemPresentationContainer));
          }
          field._hidden = false;
          if (!O$.isExplorer6() && !O$.isExplorer7())
            dropDown._fieldContainer.style.display = "table";
          dropDown._itemPresentationContainer.style.display = "none";
          if (O$.isSafari() || O$.isChrome()) {
            O$.excludeClassNames(this, ["o_hiddenFocusChromeSafari"]);
          } else {
            O$.excludeClassNames(this, ["o_hiddenFocus"]);
          }
        },

        _hide:function (doNotClearList) {
          field._hidden = true;
          dropDown._field._oldValue = dropDown._selectedItemValue;
          dropDown._fieldContainer.style.display = "";
          dropDown._itemPresentationContainer.style.display = "";
          dropDown._fieldContainer.style.height = "0";
          if (!doNotClearList)
            dropDown._setFilterCriterion(null);
          if (O$.isSafari() || O$.isChrome()) {
            if (!O$.checkClassNameUsed(this, ["o_hiddenFocusChromeSafari"]))
              O$.appendClassNames(this, ["o_hiddenFocusChromeSafari"]);
          } else {
            if (!O$.checkClassNameUsed(this, ["o_hiddenFocus"]))
              O$.appendClassNames(this, ["o_hiddenFocus"]);
          }
        },

        _cancelChanges:function () {
          if (!field._hidden) {
            if (dropDown._field._oldValue) {
              dropDown._selectedItemValue = dropDown._field._oldValue;
            } else {
              dropDown._selectedItemValue = null;
              dropDown._field.value = "";
              dropDown.value = "";
            }
            /*dropDown._setFilterCriterion(null);
            dropDown._setItemPresentationValue();        */
            dropDown._field._hide(true);
          }
        }
      });

      itemPresentation.onmousedown = function (e) {
        if (dropDown._disabled)
          return;
        dropDown.dropDown();
        if (O$.isExplorer8AndOlder()) {
          setTimeout(function () {
            field.focus();
          }, 1);
        } else {
          field.focus();
        }

        O$.Popup._hideAllPopupsExceptOne(popup);
        O$.cancelEvent(e);
      };

      itemPresentation.ondblclick = function () {
        if (dropDown._disabled)
          return;
        if (dropDown._getSelectedItem()) {
          field.value = dropDown._getSelectedItem()._itemLabel;
        }
        field._show();
        field.select();
      };

      field._hide();
    }

    popup.onscroll = function() {
      dropDown._checkAdditionalPageNeeded()
    };
    O$.addUnloadHandler(dropDown, function () {
      popup.onscroll = null;
    });

    dropDown.value = field.value;

    var innerTable = O$(dropDown.id + "--popup::innerTable");
    innerTable._selectionClass = rolloverPopupItemClass;
    popupTableStructureAndStyleParams.body.rolloverRowClassName = null;

    // Copy "colgroup" tag from drop down table to Item Presentation table
    if (itemPresentation) {
      var colgroupCalls = O$.Tables._getTableColTags(innerTable);
      if (colgroupCalls.length > 0) {
        var itemPresentationTable = O$.getChildNodesWithNames(dropDown._itemPresentationContainer, ["table"])[0];
        var itemPresentationColgroup = O$.getChildNodesWithNames(itemPresentationTable, ["colgroup"])[0];
        if (dropDown._itemPresentationColumn == -1) {
          for (var j = 0; j < colgroupCalls.length; j++)
            itemPresentationColgroup.appendChild(colgroupCalls[j].cloneNode(true));
        } else {
          if (dropDown._itemPresentationColumn < colgroupCalls.length)
            itemPresentationColgroup.appendChild(colgroupCalls[dropDown._itemPresentationColumn].cloneNode(true));
        }
      }
    }
    if (!O$.isSafari()) { // safari can't scroll popup with focus reacquiring functionality
      if (O$.isMozillaFF() || O$.isExplorer()) {
        popup.onmousedown = function() {
          dropDown._reacquireFocus = true;
        };
        O$.addUnloadHandler(dropDown, function () {
          popup.onmousedown = null;
        });
      } else {
        popup.onfocus = function() {
          dropDown.focus();
        };
        O$.addUnloadHandler(dropDown, function () {
          popup.onfocus = null;
        });
      }
    }

    dropDown._items = O$.DropDownField._prepareListElements(dropDown);

    if (itemValues) {
      dropDown._initItems(itemValues, 0);
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
    O$.addUnloadHandler(dropDown, function () {
      O$.removeEventHandler(window, "load", bodyOnLoadHandler);
    });
    O$.initUnloadableComponent(dropDown);

    function clearSuggestionTimer() {
      if (dropDown._currentSuggestionTimeoutId) {
        clearTimeout(dropDown._currentSuggestionTimeoutId);
        dropDown._currentSuggestionTimeoutId = null;
      }
    }

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
    O$.addUnloadHandler(dropDown, function () {
      field.onfocus = null;
      field.onblur = null;
      field.onkeypress = null;
      field.onkeydown = null;
    });

    field.onblur = function(e) {
      if (itemPresentation)
        field._cancelChanges();
      dropDown._ddf_focused = false;
      if (dropDown._blurWaiter)
        clearTimeout(dropDown._blurWaiter);
      if (!dropDown._reacquireFocus)
        dropDown._blurWaiter = setTimeout(function() {
          if (field.value != dropDown._promptText) {
            var currentTime = new Date().getTime();
            if (dropDown._lastItemClickTime && currentTime - dropDown._lastItemClickTime < 200)
              return;
            handleFieldChange(e);
          }
          // for IE/Mozilla not firing field change when field is changed explicitly with auto-completion
        }, 100);
      else {
        dropDown._reacquireFocus = undefined;
        dropDown.focus();
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

      if (field._hidden)
        field.value = "";
      var valueBefore = field.value;

      // Prevent previous value returning in Firefox on ESC button if value already selected & removes cursor in old IE versions
      if (keyCode == 27 && !dropDown.isOpened()) { // ESC
        field.blur();
        field.focus();
        return;
      }

      field._onchangeFlag = false;
      setTimeout(function() {
        var valueAfter = field.value;
        if (valueBefore == valueAfter)
          return;
        if (dropDown._itemPresentation)
          field._show();
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

    // Workaround for IE (dropdown field expands when field width is 100% and styles are applied by js on mouseover/mouseout)
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

    //if pressed top or bottom arrow or enter
    dropDown._selectValueByKeyCode(keyCode);

    if (dropDownOpened && (keyCode == 13 || keyCode == 27)) { // Enter || Esc
      needCancelBubble = true;
      dropDown._keyNavigationStarted = false;
      popup.hide();
      var currentItemIndex = dropDown._getHighlightedItemIndex();
      if (dropDown._itemPresentation){
        if (currentItemIndex == -1) {
          dropDown._field._cancelChanges();
        } else {
          dropDown._field._hide();
        }
        // Hide text cursor for hidden field in IE
        if (O$.isExplorer8AndOlder()) {
          dropDown._field.blur();
          dropDown._field.focus();
        }
      }
    } else {
      if (!dropDownOpened && keyCode == 13)
        dropDown._selectCurrentValue();
      if (dropDown.onkeypress_adapted)
        return dropDown.onkeypress_adapted(evt);
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
    var needCancelBubble = false;
    var highlightedItemIndex = dropDown._getHighlightedItemIndex();
    var newHighlightedItemIndex = null;
    if (keyCode == 40) { // Down
      if (!dropDown.isOpened()) {
        if (!dropDown._manualListOpeningAllowed || dropDown._disabled)
          return true;
        dropDown.dropDown();
        dropDown._checkAdditionalPageNeeded(!dropDown._cachingAllowed,true);
        O$.cancelEvent(e);
      } else {
        // Change selected index
        newHighlightedItemIndex = O$.DropDownField._getNeighboringVisibleItem(dropDown, highlightedItemIndex, false);
      }

      needCancelBubble = true;
      if (dropDown._popupCloseTimer) {
        clearTimeout(dropDown._popupCloseTimer);
      }
      dropDown._keyNavigationStarted = true;

    }
    var itemBounds;
    if (dropDown.isOpened()) {
      if (keyCode == 38) { // Up
        needCancelBubble = true;
        dropDown._keyNavigationStarted = true;
        newHighlightedItemIndex = O$.DropDownField._getNeighboringVisibleItem(dropDown, highlightedItemIndex, true);
      } else if (keyCode == 36) { // Home
        needCancelBubble = true;
        dropDown._keyNavigationStarted = true;
        newHighlightedItemIndex = dropDown._getFirstVisibleItemIndex();
      } else if (keyCode == 35) { // End
        needCancelBubble = true;
        dropDown._keyNavigationStarted = true;
        newHighlightedItemIndex = dropDown._getLastVisibleItemIndex();
      } else if (keyCode == 33) { // Page Up
        needCancelBubble = true;
        dropDown._keyNavigationStarted = true;
        itemBounds = dropDown._getItemVerticalBounds(highlightedItemIndex);
        var newScrollTop = itemBounds.top - popup.offsetHeight;

        newHighlightedItemIndex = dropDown._getItemIndexAtY(newScrollTop + 20, highlightedItemIndex, true);
        if (newHighlightedItemIndex >= highlightedItemIndex)
          newHighlightedItemIndex = O$.DropDownField._getNeighboringVisibleItem(dropDown, highlightedItemIndex, false);
        if (newHighlightedItemIndex == -1)
          newHighlightedItemIndex = dropDown._getFirstVisibleItemIndex();
      } else if (keyCode == 34) { // Page Down
        needCancelBubble = true;
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
    if (dropDown._itemPresentation && keyCode == 27) { // ESC
      dropDown._field._cancelChanges();
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

    if (needCancelBubble) {
      evt.cancelBubble = true;
      return true;
    }
    return true;
  },

  _itemClicked: function(dropDown, dropDownItem) {
    dropDown._lastItemClickTime = new Date().getTime();
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

    var cacheOnly = !(filterCriterion == null ||  dropDownField._field.value == (filterCriterion || ""));

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

      dropDownField.__acceptLoadedItems = function(newRowsToStylesMap, newRowCellsToStylesMap, itemValues, appendItems) {
        dropDownField.__acceptLoadedItems = undefined;
        var prevSuggestionCount = dropDownField._addCachedSuggestions(filterCriterion, newRows, newRowsToStylesMap, newRowCellsToStylesMap, itemValues, appendItems);
        if (!appendItems && cacheOnly)
          return;
        if (prevSuggestionCount == 0)
          appendItems = false;
        dropDownField._showCachedSuggestions(filterCriterion, undefined, true, appendItems ? newRows.length : undefined);
        dropDownField._highlightSelectedItem();
        dropDownField._scrollToHighlightedItem();
      };
      O$.Ajax.executeScripts(portionScripts);
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
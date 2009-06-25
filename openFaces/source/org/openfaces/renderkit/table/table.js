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
// -------------------------- COMMON TABLE FUNCTIONS

O$._initDataTableAPI = function(table) {
  table.selectAllRows = function() {
    this.__selectAllRows();
  }
  table.clearSelection = function() {
    this.__clearSelection();
  }
  table.isSelectionEmpty = function() {
    return this.__isSelectionEmpty();
  }
  table.getSelectedRowIndex = function() {
    return this.__getSelectedRowIndex();
  }
  table.setSelectedRowIndex = function(rowIndex) {
    this.__setSelectedRowIndex(rowIndex);
  }
  table.getSelectedRowIndexes = function() {
    return this.__getSelectedRowIndexes();
  }
  table.setSelectedRowIndexes = function(rowIndexes) {
    this.__setSelectedRowIndexes(rowIndexes);
  }
  table.getRowCount = function() {
    return this.__getRowCount();
  }
  table._of_dataTableComponentMarker = true;
}

O$._initTable = function(tableId, structureAndStyleParams, useAjax, rolloverClass, apiInitializationFunctionName) {
  var table = O$(tableId);
  if (!table)
    throw "O$._initTable: couldn't find table by id: " + tableId;
  if (table._commonTableFunctionsInitialized)
    return;
  table._commonTableFunctionsInitialized = true;

  table._rolloverClass = rolloverClass;
  table._useAjax = useAjax;

  try {
    O$._initTableStyles.apply(table, structureAndStyleParams);
  } finally {
    table.style.visibility = "visible";
    // can't just exclude the "o_initially_invisible" from table.className because of IE issue (JSFC-2337)
  }
  O$._initApiFunctions(table);
  O$._initTableMouseOvers(table);

  table._originalClassName = table.className;
  table._updateStyle = function() {
    O$.setElementStyleMappings(this, {
      rollover: this._mouseIsOver ? this._rolloverClass : null
    });
  }

  table._unloadHandlers = [];
  var i, count;
  table.onComponentUnload = function() {
    for (i = 0,count = table._unloadHandlers.length; i < count; i++) {
      table._unloadHandlers[i]();
    }

    var filtersToHide = this._filtersToHide;
    if (!O$.isExplorer6() || !filtersToHide)
      return false;

    for (i = 0, count = filtersToHide.length; i < count; i++) {
      var filter = filtersToHide[i];
      filter.style.visibility = "hidden";
    }
    return true;
  }
  if (apiInitializationFunctionName) {
    var initFunction = eval(apiInitializationFunctionName);
    if (initFunction)
      initFunction(table);
  }

  table._cleanUp = function() {
    table._bodyRows = new Array();
    table._headerRows = new Array();
    table._footerRows = new Array();
  }
}


O$._initTableMouseOvers = function(table) {
  table._mouseIsOver = false;
  if (table._rolloverClass) {
    O$.addEventHandlerSimple(table, "mouseover", "_mouseOverHandler");
    O$.addEventHandlerSimple(table, "mouseout", "_mouseOutHandler");
  }

  table._mouseOverHandler = function() {
    this._mouseIsOver = true;
    this._updateStyle();
  }

  table._mouseOutHandler = function() {
    this._mouseIsOver = false;
    this._updateStyle();
  }
}

O$._setCellProperty = function(cell, propertyName, propertyValue) {
  if (!cell)
    return;

  try {
    cell[propertyName] = propertyValue;
  } catch (e) {
    O$.logError("O$._setCellProperty: couldn't set cell property \"" + propertyName + "\" to \"" + propertyValue + "\" ; original error: " + e.message);
    throw e;
  }
}


O$._initApiFunctions = function(table) {
  table.__selectAllRows = function() {
    if (this._selectableItems != "rows")
      throw "selectAllRows: The table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
    if (!this._multipleSelectionAllowed)
      throw "selectAllRows: The table is not set up for multiple selection. Table's clientId is: " + this.id;
    this._selectAllItems();
  }
  table.__clearSelection = function() {
    this._unselectAllItems();
  }
  table.__isSelectionEmpty = function() {
    var selectedItems = this._getSelectedItems();
    if (!selectedItems || selectedItems.length == 0)
      return true;
    return selectedItems[0] == -1;
  }
  table.__getSelectedRowIndex = function() {
    if (this._selectableItems != "rows")
      throw "getSelectedRowIndex: The specified table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
    if (this._multipleSelectionAllowed)
      throw "getSelectedRowIndex can only used on a table with single selection mode; table's clientId is: " + this.id;

    var selectedItems = this._getSelectedItems();
    if (selectedItems.length == 0)
      return -1;
    return selectedItems[0];
  }
  table.__setSelectedRowIndex = function(rowIndex) {
    if (this._selectableItems != "rows")
      throw "setSelectedRowIndex: The specified table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
    if (this._multipleSelectionAllowed)
      throw "setSelectedRowIndex can only used on a table with single selection mode; table's clientId is: " + this.id;
    var bodyRows = table._getBodyRows();
    if ((rowIndex != -1) && (rowIndex < 0 || rowIndex >= bodyRows.length))
      throw "setSelectedRowIndex parameter is out of range (" + rowIndex + "); table's clientId is: " + this.id + "; number of rows is: " + bodyRows.length;
    this._setSelectedItems(rowIndex != -1 ? [rowIndex] : []);
  }
  table.__getSelectedRowIndexes = function() {
    if (this._selectableItems != "rows")
      throw "getSelectedRowIndexes: The specified table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
    if (!this._multipleSelectionAllowed)
      throw "getSelectedRowIndexes can only used on a table with multiple selection mode; table's clientId is: " + this.id;

    var selectedItems = this._getSelectedItems();
    if (!selectedItems || (selectedItems.length == 1 && selectedItems[0] == -1))
      selectedItems = [];
    return selectedItems;
  }
  table.__setSelectedRowIndexes = function(rowIndexes) {
    if (this._selectableItems != "rows")
      throw "setSelectedRowIndexes: The specified table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
    if (!this._multipleSelectionAllowed)
      throw "setSelectedRowIndexes can only used on a table with multiple selection mode; table's clientId is: " + this.id;
    if (!rowIndexes)
      rowIndexes = [];

    var bodyRows = table.__getRowCount();
    for (var i = 0, count = rowIndexes.length; i < count; i++) {
      var rowIndex = rowIndexes[i];
      if (rowIndex < 0 || rowIndex >= bodyRows.length)
        throw "setSelectedRowIndexes parameter is out of range (" + rowIndex + "); table's clientId is: " + this.id + "; number of rows is: " + bodyRows.length;
    }
    this._setSelectedItems(rowIndexes);
  }
  table.__getRowCount = function() {
    if (this._noDataRows)
      return 0;
    var bodyRows = this._getBodyRows();
    return bodyRows.length;
  }
}

// -------------------------- KEYBOARD NAVIGATION SUPPORT

O$._initTableKeyboardNavigation = function(tableId, controlPaging, focusedClassName, canPageBack, canPageForth,
                                           canSelectLastPage, tabindex) {
  var table = O$(tableId);
  table._controlPagingWithKeyboard = controlPaging;
  table._canPageBack = canPageBack;
  table._canPageForth = canPageForth;
  table._canSelectLastPage = canSelectLastPage;

  O$.setupArtificialFocus(table, focusedClassName, tabindex);

  var pagingFld = O$(table.id + "::paging");
  if (pagingFld)
    pagingFld.value = "";
  table._performPagingAction = function(actionStr) {
    O$.addHiddenField(this, this.id + "::paging", actionStr);
    O$._submitTableInternal(this);
  }

  table._nextPage = function() {
    if (this._canPageForth) this._performPagingAction("selectNextPage");
  }
  table._previousPage = function() {
    if (this._canPageBack) this._performPagingAction("selectPrevPage");
  }
  table._firstPage = function() {
    if (this._canPageBack) this._performPagingAction("selectFirstPage");
  }
  table._lastPage = function() {
    if (this._canSelectLastPage) this._performPagingAction("selectLastPage");
  }
  table._selectPageNo = function(pageNo) {
    this._performPagingAction("selectPageNo:" + pageNo);
  }

  var eventName = (O$.isSafariOnMac() || O$.isOpera() || O$.isMozillaFF()) ? "onkeypress" : "onkeydown";
  table._prevKeyHandler = table[eventName];
  table[eventName] = function (evt) {
    var e = O$.getEvent(evt);

    if (this._prevKeyHandler)
      this._prevKeyHandler(evt);

    var ctrlPressed = e.ctrlKey;
    var altPressed = e.altKey;
    var shiftPressed = e.shiftKey;
    var noModifiersPressed = !ctrlPressed && !altPressed && !shiftPressed;

    e.upPressed = false;
    e.downPressed = false;
    e.homePressed = false;
    e.endPressed = false;
    e.pageUpPressed = false;
    e.pageDownPressed = false;
    e.leftPressed = false;
    e.rightPressed = false;
    e.plusPressed = false;
    e.minusPressed = false;
    switch (e.keyCode) {
      case 33: // page up
      case 63276: /* Safari for Mac */
        e.pageUpPressed = true;
        break;
      case 34: // page down
      case 63277: /* Safari for Mac */
        e.pageDownPressed = true;
        break;
      case 35: // end
      case 63275:
        e.endPressed = true;
        break;
      case 36: // home
      case 63273:
        e.homePressed = true;
        break;
      case 37: // left
      case 63234:
        e.leftPressed = true;
        break;
      case 38: // up
      case 63232:
        e.upPressed = true;
        break;
      case 39: // right
      case 63235:
        e.rightPressed = true;
        break;
      case 40: // down
      case 63233:
        e.downPressed = true;
        break;
      case 107: // plus
      case 43:
        e.plusPressed = true;
        break;
      case 109: // minus
      case 45:
        e.minusPressed = true;
        break;
    }

    var passEvent = true;

    if (this._controlPagingWithKeyboard && !altPressed && !shiftPressed) {
      if (e.pageUpPressed) {
        passEvent = false;
        this._previousPage();
      }
      if (e.pageDownPressed) {
        passEvent = false;
        this._nextPage();
      }
      if (ctrlPressed && e.homePressed) {
        passEvent = false;
        this._firstPage();
      }
      if (ctrlPressed && e.endPressed) {
        passEvent = false;
        this._lastPage();
      }
    }

    if (this._selectionKeyboardSupport && this._selectionEnabled) {
      var rowCount = this.__getRowCount();
      if (this._multipleSelectionAllowed && !altPressed && !ctrlPressed) {      // ------ multiple selection
        var selectedRowIndexes = this.__getSelectedRowIndexes();
        var idx;
        if (selectedRowIndexes.length == 0)
          idx = -1;
        else if (selectedRowIndexes.length == 1)
          idx = selectedRowIndexes[0];
        else {
          idx = this._rangeEndRowIndex;
          if (!idx)
            idx = selectedRowIndexes[0];
        }

        if (!shiftPressed) {
          var newIdx = O$._checkTableNavigation(this, idx, rowCount, e);
          if (newIdx != null) {
            passEvent = false;
            this.__setSelectedRowIndexes([newIdx]);
            O$._scrollToRowIndexes(this, [newIdx]);
            this._baseRowIndex = null;
            this._baseSelectedRowIndexes = null;
            this._rangeEndRowIndex = null;
          }
        } else {
          var baseRowIndex = this._baseRowIndex;
          if (baseRowIndex == null) {
            baseRowIndex = idx != -1 ? idx : 0;
            this._baseRowIndex = baseRowIndex;
            this._baseSelectedRowIndexes = selectedRowIndexes;
          }
          var rangeEndRowIndex = this._rangeEndRowIndex;
          if (rangeEndRowIndex == null)
            rangeEndRowIndex = baseRowIndex;
          var newRangeEndRowIndex = O$._checkTableNavigation(this, rangeEndRowIndex, rowCount, e);
          if (newRangeEndRowIndex != null) {
            passEvent = false;
            var newSelectedRowIndexes = O$._combineSelectedRowsWithRange(this, this._baseSelectedRowIndexes, baseRowIndex, newRangeEndRowIndex);
            this._rangeEndRowIndex = newRangeEndRowIndex;
            this.__setSelectedRowIndexes(newSelectedRowIndexes);
            O$._scrollToRowIndexes(this, newSelectedRowIndexes);
          }
        }

      }
      if (!this._multipleSelectionAllowed && noModifiersPressed) {              // ------ single selection
        var idx = this.__getSelectedRowIndex();
        var newIdx = O$._checkTableNavigation(this, idx, rowCount, e);
        if (newIdx != null) {
          passEvent = false;
          this.__setSelectedRowIndex(newIdx);
          O$._scrollToRowIndexes(this, [newIdx]);
        }
      }
    }
    if (passEvent) {
      if (this._onKeyboardNavigation)
        passEvent = this._onKeyboardNavigation(e);
    }
    if (!passEvent) {
      e.cancelBubble = true;
    }

    var tabPressed = e.keyCode == 9;
    if (!tabPressed && O$.isExplorer6()) {
      // cancel the event to avoid rare IE6 crashes (JSFC-3783)
      var target = (e != null)
              ? (e.target ? e.target : e.srcElement)
              : null;

      var isTableFocused = false;
      if (target._table) {
        isTableFocused = target._table._of_dataTableComponentMarker || target._table._of_treeTableComponentMarker;
      } else if (target._of_dataTableComponentMarker || target._of_treeTableComponentMarker) {
        isTableFocused = true;
      }

      if (!isTableFocused && O$.isControlFocusable(target)) {
        return true;
      }

      if (e.preventDefault)
        e.preventDefault();
      e.returnValue = false;
      return false;
    } else {
      // allow focus traversal with Tab key while the table is focused
      return true;
    }
  }

  table._prevOnfocus_kn = table.onfocus;
  table.onfocus = function(e) {
    if (this._submitting)
      return;
    if (this._prevOnfocus_kn)
      this._prevOnfocus_kn(e);
    var focusFld = O$(this.id + "::focused");
    focusFld.value = "true";
  }

  table._prevOnblur_kn = table.onblur;
  table.onblur = function(e) {
    if (this._submitting)
      return;
    if (this._prevOnblur_kn)
      this._prevOnblur_kn(e);
    var focusFld = O$(this.id + "::focused");
    focusFld.value = "false";
  }

  var focusFld = O$(table.id + "::focused");
  if (focusFld.value == "true") {
    setTimeout(function() {
      table.focus()
    }, 1);
  }

  table._unloadHandlers.push(function() {
    O$._deinitializeTableKeyboardNavigation(table);
  });
}

O$._deinitializeTableKeyboardNavigation = function(table) {
  table.onfocus = null;
  table.onblur = null;
  if (table._focusControl) {
    //    if (table._focused)
    //      table.blur();
    table._focusControl.parentNode.removeChild(table._focusControl);
  }
}

O$._scrollToRowIndexes = function(table, rowIndexes) {
  var bodyRows = table._getBodyRows();

  var boundingRect = null;
  for (var i = 0, count = rowIndexes.length; i < count; i++) {
    var rowIndex = rowIndexes[i];
    var row = bodyRows[rowIndex];
    var rect = O$.getElementBorderRectangle(row);
    if (!boundingRect)
      boundingRect = rect;
    else
      boundingRect.addRectangle(rect);
  }

  if (boundingRect)
    O$.scrollRectIntoView(boundingRect);
}


O$._combineSelectedRowsWithRange = function(table, baseSelectedRowIndexes, baseRowIndex, rangeEndRowIndex) {
  O$.assert(baseRowIndex, "O$._combineSelectedRowsWithRange: baseRowIndex should be specified");
  O$.assert(rangeEndRowIndex, "O$._combineSelectedRowsWithRange: rangeEndRowIndex should be specified");

  var result = new Array();
  var alreadyIncludedIndexes = new Array();
  var rangeStart, rangeEnd;
  if (baseRowIndex < rangeEndRowIndex) {
    rangeStart = baseRowIndex;
    rangeEnd = rangeEndRowIndex;
  } else {
    rangeStart = rangeEndRowIndex;
    rangeEnd = baseRowIndex;
  }

  var i;
  if (baseSelectedRowIndexes) {
    var count = baseSelectedRowIndexes.length;
    for (i = 0; i < count; i++) {
      var idx = baseSelectedRowIndexes[i];
      result.push(idx);
      if (idx >= rangeStart && idx <= rangeEnd)
        alreadyIncludedIndexes.push(idx);
    }
  }

  var bodyRows = table._getBodyRows();
  for (i = rangeStart; i <= rangeEnd; i++) {
    if (O$.findValueInArray(i, alreadyIncludedIndexes) == -1) {
      var row = bodyRows[i];
      if (row._visible)
        result.push(i);
    }
  }
  return result;
}

O$._checkTableNavigation = function(table, idx, rowCount, e) {
  var newIndex = null;
  if (e.upPressed) {
    if (idx == -1)
      newIndex = O$._getNeighboringVisibleRowIndex(table, -1, +1);
    else
      newIndex = O$._getNeighboringVisibleRowIndex(table, idx, -1);
  }
  if (e.downPressed) {
    if (idx == -1)
      newIndex = O$._getNeighboringVisibleRowIndex(table, -1, +1);
    else
      newIndex = O$._getNeighboringVisibleRowIndex(table, idx, +1);
  }
  if (e.homePressed) {
    newIndex = O$._getNeighboringVisibleRowIndex(table, idx, -rowCount);
  }
  if (e.endPressed) {
    newIndex = O$._getNeighboringVisibleRowIndex(table, idx, rowCount);
  }
  /*
   if (e.pageUpPressed) {
   if (idx == -1)
   newIndex = O$._getNeighboringVisibleRowIndex(table, -1, +1);
   else
   newIndex = O$._getNeighboringVisibleRowIndex(table, idx, -5);
   }
   if (e.pageDownPressed) {
   if (idx == -1)
   newIndex = O$._getNeighboringVisibleRowIndex(table, -1, +1);
   else
   newIndex = O$._getNeighboringVisibleRowIndex(table, idx, +5);
   }
   */

  return newIndex;
}

O$._getNeighboringVisibleRowIndex = function(table, startRowIndex, stepCount) {
  var bodyRows = table._getBodyRows();
  if (stepCount == 0)
    return bodyRows[startRowIndex];
  var dir = (stepCount > 0) ? +1 : -1;
  var rowIndex = startRowIndex;
  var destRowIndex = startRowIndex;
  var stepsRemaining = (stepCount > 0) ? stepCount : -stepCount;
  while (stepsRemaining > 0) {
    rowIndex += dir;
    if (rowIndex < 0 || rowIndex >= bodyRows.length)
      break;
    var row = bodyRows[rowIndex];
    if (row._visible) {
      destRowIndex = rowIndex;
      stepsRemaining--;
    }
  }
  return destRowIndex;
}


// -------------------------- TABLE SELECTION SUPPORT

O$._initTableSelection = function(tableId, enabled, selectableItems,
                                  multipleSelectionAllowed, selectedItems, selectionClass,
                                  selectionChangeHandler, postEventOnSelectionChange, selectionColumnIndexes,
                                  mouseSupport, keyboardSupport) {
  var table = O$(tableId);
  O$.assert(table, "Couldn't find table by id: " + tableId);

  O$.assert(!table._selectionInitialized, "O$._initTableSelection shouldn't be called twice on the same table");
  table._selectionInitialized = true;

  // initialize fields
  table._selectionEnabled = !table._noDataRows && enabled;
  table._selectableItems = selectableItems;
  table._multipleSelectionAllowed = multipleSelectionAllowed;
  table._selectionClass = selectionClass;
  table._selectionColumnIndexes = selectionColumnIndexes;
  table._selectionMouseSupport = mouseSupport;
  table._selectionKeyboardSupport = keyboardSupport;

  // initialize function references
  table._selectItem = O$._table_selectItem;
  table._unselectItem = O$._table_unselectItem;
  table._getSelectedItems = function() {
    if (!this._selectedItems)
      this._selectedItems = [];
    return this._selectedItems;
  }
  table._setSelectionFieldValue = function (value) {
    var selectionFieldId = this.id + "::selection";
    var selectionField = O$(selectionFieldId);
    O$.assert(selectionField, "Couldn't find selectionField by id: " + selectionFieldId);
    selectionField.value = value;
  }
  table._setSelectedItems = function(items, forceUpdate) {
    var changesArray = new Array();
    var changesArrayIndexes = new Array();
    var oldSelectedItemsStr = "";
    var i;
    if (this._selectedItems)
      for (i = 0; i < this._selectedItems.length; i++) {
        var item = this._selectedItems[i];
        if (i > 0)
          oldSelectedItemsStr += ",";
        oldSelectedItemsStr += item;
        changesArray[item] = "unselect";
        changesArrayIndexes.push(item);
      }
    if (!this._multipleSelectionAllowed && items && items.length > 1)
      items = [items[0]];
    if (items.length == 1 && items[0] == -1)
      items = [];
    this._selectedItems = items;
    var newSelectedItemsStr = "";
    if (this._selectedItems) {
      for (i = 0; i < this._selectedItems.length; i++) {
        if (i > 0)
          newSelectedItemsStr += ",";
        var itemToSelect = this._selectedItems[i];
        O$.assert(itemToSelect, "table._setSelectedItems: itemToSelect is undefined for index " + i);
        newSelectedItemsStr += itemToSelect;
        if (changesArray[itemToSelect] == "unselect" && !forceUpdate)
          changesArray[itemToSelect] = null;
        else
          changesArray[itemToSelect] = "select";
        changesArrayIndexes.push(itemToSelect);
      }
    }

    var count = changesArrayIndexes.length;
    for (i = 0; i < count; i++) {
      var changesArrayIndex = changesArrayIndexes[i];
      var change = changesArray[changesArrayIndex];
      if (change) {
        if (change == "select")
          this._selectItem(changesArrayIndex);
        if (change == "unselect")
          this._unselectItem(changesArrayIndex);
        changesArray[changesArrayIndex] = null;
      }
    }

    var selectionFieldValue = O$._formatSelectedItems(this._selectableItems, this._selectedItems);
    this._setSelectionFieldValue(selectionFieldValue);
    if (!this._blockSelectionChangeNotifications && oldSelectedItemsStr != newSelectedItemsStr) {
      if (this._selectionChangeHandlers) {
        for (var handlerIdx = 0, handlerCount = this._selectionChangeHandlers.length;
             handlerIdx < handlerCount;
             handlerIdx++) {
          var handler = this._selectionChangeHandlers[handlerIdx];
          var obj = handler[0];
          var methodName = handler[1];
          obj[methodName]();
        }
      }
      if (this._postEventOnSelectionChange) {
        var eventFieldId = this.id + "::selectionEvent";
        var eventField = O$(eventFieldId);
        eventField.value = this._postEventOnSelectionChange;
        window._submittingTable = this;
        setTimeout(function() {
          O$.submitEnclosingForm(window._submittingTable);
        }, 1);
      }
    }
  }

  table._selectAllItems = function() {
    O$.assert(this._multipleSelectionAllowed, "table._selectAllItems: multiple selection is not allowed for table: " + this.id);

    if (this._noDataRows)
      return;
    if (this._selectableItems == "rows") {
      var rows = this._getBodyRows();
      var allItems = new Array();
      for (var i = 0, count = rows.length; i < count; i++)
        allItems[i] = i;
      this._setSelectedItems(allItems);
    }
  }

  table._unselectAllItems = function() {
    this._setSelectedItems([]);
  }

  table._isItemSelected = function(item) {
    var result = O$.findValueInArray(item, selectedItems) != -1;
    return result;
  }

  table._toggleItemSelected = function(itemIndex) {
    if (itemIndex == -1) {
      O$.logError("_toggleItemSelected: itemIndex == -1");
      return;
    }
    var selectedIndexes = this._selectedItems;
    var newArray = new Array();
    for (var i = 0, count = selectedIndexes.length; i < count; i++) {
      var idx = selectedIndexes[i];
      if (idx != itemIndex)
        newArray.push(idx);
    }
    if (newArray.length == selectedIndexes.length)
      newArray.push(itemIndex);
    this._setSelectedItems(newArray);
  }

  // run initialization code
  if (selectableItems == "rows") {
    var rows = table._getBodyRows();
    for (var i = 0, count = rows.length; i < count; i++) {
      var row = rows[i];
      O$._initRowForSelection(row);
    }
  }

  table._setSelectedItems(selectedItems);
  table._setSelectionFieldValue("");

  if (selectionChangeHandler) {
    eval("table.onchange = function(event) {if (!event._of_event)return;" + selectionChangeHandler + "}");
    // checking _of_event is needed if this is a bubbled event from some child
    table._fireOnSelectionChange = function() {
      O$.sendEvent(table, "change");
    }
    O$._addTableSelectionChangeHandler(table, [table, "_fireOnSelectionChange"]);
  }
  table._postEventOnSelectionChange = postEventOnSelectionChange;

  table._addRowInsertionCallback(function(table, insertedAfterIndex, insertedRows) {
    var insertedRowCount = insertedRows.length;
    var i;
    for (i = 0; i < insertedRowCount; i++) {
      var insertedRow = insertedRows[i];
      O$._initRowForSelection(insertedRow);
    }
    var selectedItems = table._getSelectedItems();
    if (table._selectableItems != "rows")
      throw "Not supported selectable item type: " + table._selectableItems;

    var selectedItemCount = selectedItems.length;
    if (selectedItemCount > 0) {
      var newSelectedItems = new Array();
      var selectionChanged = false;
      for (i = 0; i < selectedItemCount; i++) {
        var rowIndex = selectedItems[i];
        if (rowIndex > insertedAfterIndex) {
          rowIndex += insertedRowCount;
          selectionChanged = true;
        }
        newSelectedItems.push(rowIndex);
      }
      /*if (selectionChanged) */
    {
      table._blockSelectionChangeNotifications = true;
      table._setSelectedItems(newSelectedItems, true);
      table._blockSelectionChangeNotifications = false;
    }
    }
  });
}

O$._initRowForSelection = function(row) {
  var table = row._table;
  if (table._selectionEnabled) {
    if (row._originalClickHandler)
      O$.logError("O$._initTableSelection: row click handler already initialized");
    row._originalClickHandler = row.onclick;
    row.onclick = O$._tableRow_handleSelectionOnClick;
  }
  var cells = row._cells;
  for (var cellIndex = 0, cellCount = cells.length; cellIndex < cellCount; cellIndex++) {
    var cell = cells[cellIndex];
    var cellSpan = O$._getCellColSpan(cell);
    var colIndex = cell._colIndex;
    if ((colIndex != undefined) && (O$.findValueInArray(colIndex, table._selectionColumnIndexes) != -1))
      O$._initSelectionCell(cell);
    colIndex += cellSpan;
  }
}

O$._addTableSelectionChangeHandler = function(table, handler) {
  O$.assert(handler, "O$._addTableSelectionChangeHandler: handler must be specified. table.id = " + table.id);
  var handlers = table._selectionChangeHandlers;
  if (!handlers) {
    handlers = new Array();
    table._selectionChangeHandlers = handlers;
  }
  handlers.push(handler);
}

O$._initSelectionCell = function(cell) {
  var checkBoxAsArray = O$.getChildNodesWithNames(cell, ["input"]);
  //  O$.assert(checkBoxAsArray.length == 1); // can be more than one because of service fields being rendered into the last table cell
  if (!checkBoxAsArray || checkBoxAsArray.length == 0)
    return;
  var checkBox = checkBoxAsArray[0];
  if (!checkBox)
    return;
  checkBox._cell = cell;
  var row = cell._row;
  var table = row._table;
  checkBox.checked = false;
  // fix for Mozilla's issue: reloading a page retains previous values for inputs regardless of their values received from server
  checkBox.disabled = !table._selectionEnabled;
  cell._selectionCheckBox = checkBox;
  cell.onclick = function(evt) {
    cell._handlingClick = true;
    try {
      var cellRow = this._row;
      var cellTable = cellRow._table;
      if (!cellTable._selectionEnabled)
        return;
      if (cellTable._multipleSelectionAllowed) {
        this._selectionCheckBox.checked = !this._selectionCheckBox.checked;
        this._selectionCheckBox.onclick(evt);
      } else {
        if (!this._selectionCheckBox.checked) {
          this._selectionCheckBox.checked = true;
        }
        this._selectionCheckBox.onclick(evt);
      }
    } finally {
      cell._handlingClick = false;
    }
  }
  cell.ondblclick = O$.repeatClickOnDblclick;

  checkBox.onclick = function(evt) {
    if (evt)
      event = evt;
    var checkBoxCell = this._cell;
    if (!checkBoxCell._handlingClick)
      return;
    var checkBoxRow = checkBoxCell._row;
    var checkBoxTable = checkBoxRow._table;
    if (!checkBoxTable._selectionEnabled)
      return;
    if (checkBoxTable._selectableItems != "rows")
      return;
    if (!checkBoxTable._multipleSelectionAllowed) {
      if (this.checked)
        checkBoxTable._setSelectedItems([checkBoxRow._index]);
      else
        checkBoxTable._setSelectedItems([]);
      event.cancelBubble = true;
    } else {
      checkBoxTable._toggleItemSelected(row._index);
      event.cancelBubble = true;
    }
  }
  checkBox.ondblclick = function(evt) {
    if (O$.isExplorer())
      this.click(evt);
    event.cancelBubble = true;
  }

  var cellRow = cell._row;
  if (!cellRow._selectionCheckBoxes)
    cellRow._selectionCheckBoxes = new Array();
  cellRow._selectionCheckBoxes.push(checkBox);
}

O$._tableRow_handleSelectionOnClick = function(evt) {
  if (this._originalClickHandler)
    this._originalClickHandler(evt);

  if (evt)
    event = evt;

  var table = this._table;
  if (!table._selectionMouseSupport)
    return;
  if (table._selectableItems == "rows") {
    table._baseRowIndex = null;
    table._baseSelectedRowIndexes = null;
    table._rangeEndRowIndex = null;
    if (!table._multipleSelectionAllowed) {
      table._setSelectedItems([this._index]);
    } else {
      if (event.ctrlKey || event.metaKey) {
        table._toggleItemSelected(this._index);
        var newSelectedRowIndexes = table.__getSelectedRowIndexes();
        table._baseRowIndex = (O$.findValueInArray(this._index, newSelectedRowIndexes) != -1) ? this._index : null;
        table._baseSelectedRowIndexes = newSelectedRowIndexes;
        table._rangeEndRowIndex = null;
      } else
        table._setSelectedItems([this._index]);
    }
  }
}

O$._formatSelectedItems = function(selectableItems, selectedItemIndexes) {
  if (selectableItems == "rows" || selectableItems == "columns") {
    var result = "[";
    for (var i = 0; i < selectedItemIndexes.length; i++) {
      var itemIndex = selectedItemIndexes[i];
      if (result.length > 1)
        result += ",";
      result += itemIndex;
    }
    result += "]";
    return result;
  }
  throw "O$._formatSelectedItems: unknown selectableItems: " + selectableItems;
}

O$._table_selectItem = function(itemIndex) {
  O$.assert(itemIndex, "O$._table_selectItem: itemIndex should be specified");
  if (this._selectableItems == "rows") {
    if (itemIndex == -1)
      return;
    var rows = this._getBodyRows();
    if (itemIndex < 0 || itemIndex >= rows.length)
      throw "Row index out of range: " + itemIndex;
    var row = rows[itemIndex];
    row._selected = true;
    row._updateStyle();
    O$._setSelectionCheckboxesSelected(row, true);
  }
}

O$._table_unselectItem = function(itemIndex) {
  O$.assert(itemIndex, "O$._table_unselectItem: itemIndex should be specified");
  if (this._selectableItems == "rows") {
    if (itemIndex == -1)
      return;
    var rows = this._getBodyRows();
    var row = rows[itemIndex];

    row._selected = false;
    row._updateStyle();
    O$._setSelectionCheckboxesSelected(row, false);
  }
}

O$._setSelectionCheckboxesSelected = function(row, selected) {
  if (row._selectionCheckBoxes) {
    for (var i = 0, count = row._selectionCheckBoxes.length; i < count; i++) {
      var checkbox = row._selectionCheckBoxes[i];
      checkbox.checked = selected;
    }
  }
}

O$._initCheckboxColHeader = function(headerId, colId) {
  var header = O$(headerId);
  var table = O$.findParentNode(header, "TABLE");
  if (!table)
    throw "SelectAllCheckbox must be placed in a header of <o:dataTable> component. clientId = " + headerId;
  header._table = table;
  header._columnObjectId = colId;
  header.style.cursor = "default";

  if (!table._checkBoxColumnHeaders)
    table._checkBoxColumnHeaders = new Array();
  if (!table._checkBoxColumnHeaders[colId])
    table._checkBoxColumnHeaders[colId] = new Array();
  var colHeadersArray = table._checkBoxColumnHeaders[colId];
  colHeadersArray.push(header);

  header._updateFromCheckboxes = function(tableColumn) {
    var cells = tableColumn.body ? tableColumn.body._cells : [];
    var allChecked = true;
    var atLeastOneCheckboxFound = false;
    for (var i = 0, count = cells.length; i < count; i++) {
      var cell = cells[i];
      if (!cell)
        continue;
      var checkBox = cell._checkBox;
      if (!checkBox)
        continue;
      atLeastOneCheckboxFound = true;
      if (!checkBox.checked) {
        allChecked = false;
        break;
      }
    }
    this.checked = atLeastOneCheckboxFound ? allChecked : false;
  };

  header.onclick = function(e) {
    var cell = O$.findAnyParentNode(this, ["td", "th"]);
    var col = cell._column;
    O$._setAllCheckboxes(col, this.checked);
    var columnObj = O$(this._columnObjectId);
    columnObj._updateHeaderCheckBoxes();
    col._updateSubmissionField();
    var evt = O$.getEvent(e);
    evt.cancelBubble = true;
  }
  header.ondblclick = function(e) {
    if (O$.isExplorer())
      this.click();
    var evt = O$.getEvent(e);
    evt.cancelBubble = true;
  }
}

O$._setAllCheckboxes = function(col, checked) {
  var cells = col.body ? col.body._cells : [];
  for (var i = 0, count = cells.length; i < count; i++) {
    var cell = cells[i];
    if (cell && cell._checkBox) // to account for the "no data" row
      cell._checkBox.checked = checked;
  }
}

O$._initSelectionHeader = function(headerId) {
  var header = O$(headerId);
  var table = O$.findParentNode(header, "TABLE");
  if (!table)
    throw "SelectAllCheckbox must be placed in a header of <o:dataTable> component. clientId = " + headerId;
  header._table = table;
  header.style.cursor = "default";
  header._updateStateFromTable = function() {
    var headerTable = this._table;
    var selectedItems = headerTable._getSelectedItems();
    var bodyRows = headerTable._getBodyRows();
    if (selectedItems.length == 0) {
      this.checked = false;
    } else {
      this.checked = selectedItems.length == bodyRows.length;
    }
  };
  O$._addTableSelectionChangeHandler(table, [header, "_updateStateFromTable"]);
  header.onclick = function(e) {
    if (this.disabled) {
      this.disabled = false;
      this.checked = true;
      this._table._selectAllItems();
    } else {
      if (this.checked)
        this._table._selectAllItems();
      else
        this._table._unselectAllItems();
    }
    var evt = O$.getEvent(e);
    evt.cancelBubble = true;
  }
  header.ondblclick = function(e) {
    if (O$.isExplorer())
      this.click();
    var evt = O$.getEvent(e);
    evt.cancelBubble = true;
  }
}

// -------------------------- CHECKBOX COLUMN SUPPORT

O$._setCheckboxColIndexes = function(colId, checkedRowIndexes) {
  var columnObj = O$(colId);
  columnObj._setCheckedIndexes(checkedRowIndexes);
}

O$._initCheckboxCol = function(tableId, colIndex, colId, checkedRowIndexes) {
  var table = O$(tableId);
  var tableColumn = table._columns[colIndex];
  var columnObj = O$(colId);
  tableColumn._submissionField = columnObj;
  columnObj._tableColumn = tableColumn;

  columnObj._setCheckedIndexes = function(checkedIndexes) {
    var bodyCells = tableColumn.body ? tableColumn.body._cells : [];
    for (var i = 0, count = bodyCells.length; i < count; i++) {
      var cell = bodyCells[i];
      if (!cell)
        continue;
      cell._column = tableColumn;
      O$._initCheckboxCell(cell, columnObj);
      if (cell._checkBox) {
        cell._checkBox.checked = O$.findValueInArray(i, checkedIndexes) != -1;
      }
    }
    tableColumn._updateSubmissionField();
  }

  tableColumn._updateSubmissionField = function() {
    var bodyCells = tableColumn.body ? tableColumn.body._cells : [];
    var selectedRows = "";
    for (var i = 0, count = bodyCells.length; i < count; i++) {
      var cell = bodyCells[i];
      if (!cell)
        continue;
      if (cell._checkBox && cell._checkBox.checked) {
        if (selectedRows.length > 0)
          selectedRows += ",";
        selectedRows += i;
      }
    }
    this._submissionField.value = selectedRows;
  }

  columnObj._setCheckedIndexes(checkedRowIndexes);

  if (table._checkBoxColumnHeaders) {
    columnObj._headers = table._checkBoxColumnHeaders[colId];
  }
  columnObj._updateHeaderCheckBoxes = function() {
    if (!this._headers)
      return;
    var tableCol = this._tableColumn;
    for (var i = 0, count = this._headers.length; i < count; i++) {
      var header = this._headers[i];
      header._updateFromCheckboxes(tableCol);
    }
  }

  columnObj._updateHeaderCheckBoxes();
}

O$._initCheckboxCell = function(cell, colField) {
  if (cell._checkBoxCellInitialized)
    return;
  cell._checkBoxCellInitialized = true;
  var checkBoxAsArray = O$.getChildNodesWithNames(cell, ["input"]);
  var checkBox = checkBoxAsArray[0];
  if (!checkBox)
    return;
  checkBox._cell = cell;
  cell._checkBox = checkBox;
  cell._columnObj = colField;
  cell.onclick = function(evt) {
    if (evt)
      event = evt;
    if (event._checkBoxClickProcessed) {
      event.cancelBubble = true;
      return;
    }
    this._checkBox.checked = !this._checkBox.checked;
    this._processCheckboxChange();
    event.cancelBubble = true;
    return false;
  }
  cell.ondblclick = O$.repeatClickOnDblclick;

  checkBox.onclick = function(evt) {
    if (evt)
      event = evt;
    var checkBoxCell = this._cell;
    checkBoxCell._processCheckboxChange();
    event._checkBoxClickProcessed = true;
    event.cancelBubble = true;
  }
  checkBox.ondblclick = function(e) {
    if (O$.isExplorer())
      this.click(e);
    event.cancelBubble = true;
  }

  cell._processCheckboxChange = function() {
    var columnObj = this._columnObj;
    columnObj._updateHeaderCheckBoxes();
    cell._column._updateSubmissionField();
  }
}

// -------------------------- TABLE SORTING SUPPORT

O$._initTableSorting = function(tableId, columnSortableFlags, sortedColIndex, sortableHeaderClass, sortableHeaderRolloverClass,
                                sortedColClass, sortedColHeaderClass, sortedColBodyClass, sortedColFooterClass,
                                sortingImagesToPreload) {
  var table = O$(tableId);
  table._sortedColIndex = sortedColIndex;
  table._sortedColClass = sortedColClass;
  table._sortedColBodyClass = sortedColBodyClass;
  O$.assert(table, "Couldn't find table by id: " + tableId);

  O$.preloadImages(sortingImagesToPreload);

  for (var i = 0, count = table._columns.length; i < count; i++) {
    var columnSortable = columnSortableFlags[i];
    if (!columnSortable)
      continue;
    var column = table._columns[i]
    var colHeader = column.header ? column.header._cell : null;
    if (!colHeader)
      continue;

    O$.setElementStyleMappings(colHeader, {_sortableHeaderClass: sortableHeaderClass});
    O$._setCellProperty(colHeader, "_table", table);
    O$._setCellProperty(colHeader, "_index", i);
    var clickHandler = function() {
      if (this._prevOnclick)
        this._prevOnclick();
      var focusField = O$(table.id + "::focused")
      if (focusField)
        focusField.value = true; // set true explicitly before it gets auto-set when the click bubbles up (JSFC-801)
      O$._toggleColumnSorting(this._table, this._index);
    }

    colHeader._prevOnclick = colHeader.onclick;
    colHeader.onclick = clickHandler;

    table._sortableHeaderRolloverClass = sortableHeaderRolloverClass;
    colHeader._headerMouseOver = function() {
      O$.setElementStyleMappings(this, {_sortedHeaderRolloverClass: table._sortableHeaderRolloverClass});
    }
    colHeader._headerMouseOut = function() {
      O$.setElementStyleMappings(this, {_sortedHeaderRolloverClass: null});
    }
    O$.addEventHandlerSimple(colHeader, "mouseover", "_headerMouseOver", colHeader);
    O$.addEventHandlerSimple(colHeader, "mouseout", "_headerMouseOut", colHeader);
  }

  if (sortedColIndex != -1) {
    var column = table._columns[table._sortedColIndex];
    // Applying style to cells is needed for sorted column styles to have priority over
    // even/odd row styles - for backward compatibility with QK versions earlier than 1.2.2 (JSFC-2884)
    column._forceUsingCellStyles = true;

    var headerCell = (column.header) ? column.header._cell : null;
    var startIdx = table._commonHeaderExists ? 1 : 0;
    if (headerCell)
      O$._setCellStyleMappings(headerCell, {
        _sortedColClass: (table._forceUsingCellStyles || column._useCellStyles) ? sortedColClass : null,
        _sortedColHeaderClass: sortedColHeaderClass});

    O$.setElementStyleMappings(column, {_sortedColClass: table._sortedColClass});
    O$.setElementStyleMappings(column.body, {_sortedColBodyClass: table._sortedColBodyClass});
    column._updateStyle();

    var footerCell = column.footer ? column.footer._cell : null;
    if (footerCell)
      O$._setCellStyleMappings(footerCell, {
        _sortedColClass: (table._forceUsingCellStyles || column._useCellStyles) ? sortedColClass : null,
        _sortedColFooterClass: sortedColFooterClass});
  }

  table._sortingEnabled = true;
}

O$._toggleColumnSorting = function(table, columnIndex) {
  var sortingFieldId = table.id + "::sorting";
  var sortingField = O$(sortingFieldId);
  sortingField.value = "" + columnIndex;
  O$._submitTableInternal(table);
}

O$._submitTableWithField = function(tableId, focusedField, additionalParams) {
  var focusedFieldId = focusedField ? focusedField.id : null;
  var table = O$(tableId);
  var focusFilterField = function() {
    if (!focusedFieldId)
      return;
    var field = O$(focusedFieldId);
    if (!field)
      return;
    if (field.focus)
      try {
        field.focus();
      } catch(e) {
        // ignore failed focus attempts
      }
  }
  O$._submitTableInternal(table, function() {
    setTimeout(focusFilterField, 1);
  }, additionalParams);
}

O$._performPagerAction = function(tableId, field, paramName, paramValue) {
  O$._submitTableWithField(tableId, field, [[paramName, paramValue]]);
}

O$._submitTableInternal = function(table, completionCallback, additionalParams) {
  var useAjax = table._useAjax;
  if (!useAjax) {
    if (additionalParams)
      for (var i = 0, count = additionalParams.length; i < count; i++) {
        var paramEntry = additionalParams[i];
        O$.addHiddenField(table, paramEntry[0], paramEntry[1]);
      }
    O$.submitEnclosingForm(table);
  } else {
    O$.reloadComponents([table.id], {onajaxend: completionCallback, additionalParams: additionalParams});
  }
}

O$._filterFieldKeyPressHandler = function(tableId, filterField, event) {
  if (event.keyCode == 13) {
    // filter only in cases when onchange is not fired by pressing Enter to avoid double filter requests
    var onchangeFired = filterField.nodeName.toUpperCase() != "INPUT";
    if (!onchangeFired)
      O$._filterDataTable(tableId, filterField);

    event.cancelBubble = true;
    return false;
  }
  var inFieldNavigation = (event.keyCode >= 35 && event.keyCode <= 40);
  if (inFieldNavigation)
    event.cancelBubble = true;
  return true;
}

O$._filterDataTable = function(tableId, filterField) {
  // setTimeout in the following script is needed to avoid page blinking when using combo-box filter in IE (JSFC-2263)
  setTimeout(function() {
    O$._submitDropDownFilter(tableId, filterField);
  }, 1);
}

O$._submitDropDownFilter = function(tableId, filter) {
  var table = O$(tableId);
  if (table._useAjax)
    O$._submitTableWithField(tableId, filter);
  else
    O$.submitEnclosingForm(table);
}


O$._showTableFilter = function(tableId, filterId) {
  var table = O$(tableId);
  var f = O$(filterId);

  if (!table._filtersToHide)
    table._filtersToHide = new Array();
  table._filtersToHide.push(f);
}


// -------------------------- COLUMN RESIZING SUPPORT

O$._initTableColumnResizing = function(tableId, retainTableWidth, minColWidth, resizeHandleWidth, columnParams) {
  O$.addLoadEvent(function() {
    if (minColWidth == null)
      minColWidth = 10;

    if (resizeHandleWidth == null)
      resizeHandleWidth = 9;
    else
      resizeHandleWidth = O$.calculateNumericCSSValue(resizeHandleWidth);
    if (resizeHandleWidth < 1)
      resizeHandleWidth = 1;

    var table = O$(tableId);
    var tableBordersCollapsed = O$.getElementStyleProperty(table, "border-collapse") == "collapse";
    var tableCellPadding = O$.calculateNumericCSSValue(table.cellPadding);
    var colWidthsFieldId = table.id + "::colWidths";
    var colWidthsField = O$.addHiddenField(table, colWidthsFieldId);

    function recalculateTableWidth(colWidths) {
      var totalWidth = 0;
      for (var i = 0, count = table._columns.length; i < count; i++) {
        var column = table._columns[i];
        var columnWidth = colWidths ? colWidths[i] : column.getWidth();
        totalWidth += columnWidth;
      }

      var borderLeft = O$.getNumericStyleProperty(table, "border-left-width", true);
      var borderRight = O$.getNumericStyleProperty(table, "border-right-width", true);
      if (tableBordersCollapsed) {
        if (O$.isOpera() || O$.isSafari()) {
          borderLeft = Math.floor(borderLeft / 2);
          borderRight = Math.ceil(borderRight / 2);
        }
      }
      var width = totalWidth + borderLeft + borderRight;
      table.style.width = width + "px";
      return width;
    }

    function getColWidths() {
      var colWidths = []
      for (var i = 0, count = table._columns.length; i < count; i++) {
        var column = table._columns[i];
        var columnWidth = column.getWidth();
        colWidths[i] = columnWidth;
      }
      return colWidths;
    }

    var colCount = table._columns.length;

    for (var i = 0; i < colCount; i++) {
      var col = table._columns[i];
      if (col.header && col.header._cell)
        col.header._cell.style.overflow = "hidden";
      if (col.filter && col.filter._cell)
        col.filter._cell.style.overflow = "hidden";
      if (col.footer && col.footer._cell)
        col.footer._cell.style.overflow = "hidden";
      var bodyCells = col.body._cells;
      for (var j = 0, jCount = bodyCells.length; j < jCount; j++) {
        var cell = bodyCells[j];
        if (!cell || cell.colSpan > 1)
          continue;
        cell.style.overflow = "hidden";
      }

      var thisColumnParams = columnParams[colIndex];
      if (thisColumnParams) {
        col._resizable = thisColumnParams.resizable;
        col._minResizingWidth = thisColumnParams.minWidth;
      }
      if (col._resizable == undefined)
        col._resizable = true;
      if (col._minResizingWidth == undefined)
        col._minResizingWidth = minColWidth;
      col._minResizingWidth = O$.calculateNumericCSSValue(col._minResizingWidth);
      if (col._minResizingWidth < 0)
        col._minResizingWidth = 0;
    }

    table._addCellInsertionCallback(function(cell, row, column) {
      cell.style.overflow = "hidden";
    });

    for (var colIndex = 0; colIndex < colCount; colIndex++) {
      var column = table._columns[colIndex];

      column.setWidth = function(width, additionalAttempts) {
        if (!O$.isOpera())
          additionalAttempts = 0;
        if (additionalAttempts) {
          this.setWidth(width, 0);
          var actualWidth;
          // additional attempts are needed because at least Opera doesn't always set the specified width from
          // the first attempt
          for (var attempt = 0; attempt < additionalAttempts; attempt++) {
            actualWidth = this.getWidth();
            if (actualWidth == width)
              break;
            this.setWidth(width, 0);
          }
          if (actualWidth != width)
            this.setWidth(actualWidth, 0);
          return actualWidth;
        }

        function measureColRightBorder(col) {
          if (col.header && col.header._cell)
            return O$.getNumericStyleProperty(col.header._cell, "border-right-width", true);
          if (col.filter && col.filter._cell)
            return O$.getNumericStyleProperty(col.filter._cell, "border-right-width", true);
          if (!table._noDataRows && col.body._cells.length > 0)
            return O$.getNumericStyleProperty(col.body._cells[0], "border-right-width", true);
          if (col.footer && col.footer._cell)
            return O$.getNumericStyleProperty(col.footer._cell, "border-right-width", true);
          return 0;
        }

        var strictMode = O$.isStrictMode();
        var explorer = O$.isExplorer();
        var paddingLeft = O$.isOpera() || (explorer && strictMode) ? tableCellPadding : 0;
        var paddingRight = O$.isOpera() || (explorer && strictMode) ? tableCellPadding : 0;
        var colTagWidth = width - paddingLeft - paddingRight;
        if (explorer && strictMode)
          colTagWidth -= measureColRightBorder(this);
        this._colTag.style.width = colTagWidth + "px";
        if (strictMode && (explorer || O$.isMozillaFF()))
          return width;
        function setCellWidth(cell, width) {
          if (!cell)
            return;
          var w = width;
          if (!O$.isExplorer()) {
            var paddingLeft = O$.getNumericStyleProperty(cell, "padding-left", true);
            var paddingRight = O$.getNumericStyleProperty(cell, "padding-right", true);
            if (!paddingLeft) paddingLeft = tableCellPadding;
            if (!paddingRight) paddingRight = tableCellPadding;
            w -= paddingLeft + paddingRight;
            var borderRight = O$.getNumericStyleProperty(cell, "border-right-width", true);
            w -= borderRight;
            if (!tableBordersCollapsed) {
              var borderLeft = O$.getNumericStyleProperty(cell, "border-left-width", true);
              w -= borderLeft;
            }
          }
          cell.style.width = w + "px";
        }

        if (this.header)
          setCellWidth(this.header._cell, width);
        if (this.filter)
          setCellWidth(this.filter._cell, width);
        for (var i = 0, count = this.body._cells.length; i < count; i++) {
          var cell = this.body._cells[i];
          if (!cell || cell.colSpan > 1)
            continue;
          setCellWidth(cell, width);
        }
        if (this.footer)
          setCellWidth(this.footer._cell, width);
        return width;
      }
      column.getWidth = function() {
        if (this.header && this.header._cell)
          return this.header._cell.offsetWidth;
        if (this.filter && this.filter._cell)
          return this.filter._cell.offsetWidth;
        for (var i = 0, count = this.body._cells.length; i < count; i++) {
          var cell = this.body._cells[i];
          if (!cell || cell.colSpan > 1)
            continue;
          return cell.offsetWidth;
        }
        if (this.footer && this.footer._cell)
          return this.footer._cell.offsetWidth;
        return this._colTag.offsetWidth;
      }

      if (!column.header || !column.header._cell)
        continue;

      var widthCompensationColIndex = -1;
      if (retainTableWidth) {
        for (var i = table._columns.length - 1; i >= 0; i--) {
          var c = table._columns[i];
          if (c._resizable) {
            widthCompensationColIndex = i;
            break;
          }
        }
      }
      if (widthCompensationColIndex != -1 && colIndex >= widthCompensationColIndex)
        continue;

      if (!column._resizable)
        continue;
      var resizeHandle = document.createElement("div");
      resizeHandle.style.cursor = "e-resize"
      resizeHandle.style.position = "absolute";
      resizeHandle.style.border = "0px none transparent";//"1px solid black";

      if (O$.isExplorer()) {
        // IE needs an explicit background because otherwise this absolute div will "leak" some events to the underlying
        // component (when a mouse is directly over any of table's gridline)
        resizeHandle.style.background = "silver";
        resizeHandle.style.filter = "alpha(opacity=0)"
      }

      column.header._cell.appendChild(resizeHandle);
      column._resizeHandle = resizeHandle;
      resizeHandle._column = column;
      resizeHandle.onmouseover = function() {
        if (this._draggingInProgress)
          return;
        if (!table.parentNode)
          this.parentNode.removeChild(this);
        else
          this._updatePos();
      }
      resizeHandle.onmousedown = function (e) {
        O$.startDragAndDrop(e, this);
      }
      resizeHandle.onclick = function(e) {
        O$.breakEvent(e);
      }
      resizeHandle.ondragstart = function() {
        table._columnResizingInProgress = true;
        var resizeDecorator = document.createElement("div");
        resizeDecorator.style.position = "absolute";
        resizeDecorator.style.borderLeft = "0px none transparent";//"1px solid gray";
        table.parentNode.appendChild(resizeDecorator);
        this._column._resizeDecorator = resizeDecorator;
        resizeDecorator._column = this._column;

        resizeDecorator._updatePos = function() {
          var headerCell = this._column.header._cell;
          var cellPos = O$.getElementBorderRectangle(headerCell, true);
          var tablePos = O$.getElementPos(table, true);

          this.style.top = cellPos.getMinY() + "px";
          this.style.left = cellPos.getMaxX() + "px";
          this.style.width = "0px";//"1px";
          this.style.height = tablePos.top + table.offsetHeight - cellPos.getMinY() + "px";
        }
        resizeDecorator._updatePos();
        var headerCell = this._column.header._cell;
        this._dragStartCellPos = O$.getElementBorderRectangle(headerCell, true);
      }
      resizeHandle.setLeft = function(left) {
        this.style.left = left + "px";
        var newColRightEdge = left + Math.floor(resizeHandleWidth / 2) + 1;
        var newColWidth = newColRightEdge - this._dragStartCellPos.getMinX();
        if (newColWidth < this._column._minResizingWidth)
          newColWidth = this._column._minResizingWidth;

        var colWidths = getColWidths();
        table.style.width = "auto";
        var thisAndNextColWidth;
        var nextCol;
        if (widthCompensationColIndex != -1) {
          var nextColWidth = colWidths[widthCompensationColIndex];
          var thisColWidth = colWidths[this._column._colIndex];
          thisAndNextColWidth = thisColWidth + nextColWidth;
          nextCol = table._columns[widthCompensationColIndex];
          var maxWidthForThisCol = thisAndNextColWidth - nextCol._minResizingWidth;
          if (newColWidth > maxWidthForThisCol)
            newColWidth = maxWidthForThisCol;
          var actualNextColWidth = nextCol.setWidth(thisAndNextColWidth - newColWidth, 3);
          colWidths[widthCompensationColIndex] = actualNextColWidth;
          newColWidth = thisAndNextColWidth - actualNextColWidth;
        }

        var actualColWidth = this._column.setWidth(newColWidth, 3);
        colWidths[this._column._colIndex] = actualColWidth;
        if (widthCompensationColIndex != -1) {
          if (actualColWidth != newColWidth) {
            colWidths[widthCompensationColIndex] = nextCol.setWidth(thisAndNextColWidth - actualColWidth, 3);
          }
        }

        if (!retainTableWidth)
          recalculateTableWidth(colWidths);
        else
          table.style.width = table._originalWidth + "px";
        this._column._resizeDecorator._updatePos();
        O$.repaintAreaForOpera(table);
        table._fixFF3ColResizingIssue();
      }
      resizeHandle.setTop = function(top) {
        this.style.top = top + "px";
      }
      resizeHandle.ondragend = function() {
        table._columnResizingInProgress = false;
        this._column._resizeDecorator.parentNode.removeChild(this._column._resizeDecorator);
        table._updateResizeHandlePositions();

        var totalWidth = 0;
        var colWidths = [];
        for (var i = 0; i < colCount; i++) {
          var col = table._columns[i];
          var colWidth = col.getWidth();
          colWidths[i] = colWidth + "px";
          totalWidth += colWidth;
        }

        colWidthsField.value = (O$.isOpera() ? table.style.width : totalWidth + "px") + ":" +
                               "[" + colWidths.join(",") + "]";
        if (table._focusable) {
          if (!table._focused)
            table.focus();
        }
      }
      resizeHandle._updatePos = function() {
        var parentColumn = null;
        for (var col = this._column; col._parentColumn; col = col._parentColumn) {
          var indexAmongSiblings = O$.findValueInArray(col, col._parentColumn.subColumns);
          var lastColInGroup = indexAmongSiblings == col._parentColumn.subColumns.length - 1;
          if (!lastColInGroup)
            break;
          if (col._parentColumn.header && col._parentColumn.header._cell)
            parentColumn = col._parentColumn;
        }
        var bottomCell = this._column.filter ? this._column.filter._cell : this._column.header._cell;
        var bottomCellPos = O$.getElementBorderRectangle(bottomCell, true);
        var topCellPos = parentColumn ? O$.getElementBorderRectangle(parentColumn.header._cell, true) : O$.getElementBorderRectangle(this._column.header._cell, true);

        var minY = topCellPos.getMinY();
        this.style.top = minY + "px";
        this.style.left = bottomCellPos.getMaxX() - Math.floor(resizeHandleWidth / 2) - 1 + "px";
        this.style.width = resizeHandleWidth + "px";
        this.style.height = bottomCellPos.getMaxY() - minY + "px";
      }

      column._resizeHandle._updatePos();
    }

    var colWidths = getColWidths();
    table.style.tableLayout = "fixed";

    table.style.width = "auto";
    for (var i = 0, count = colWidths.length; i < count; i++) {
      var column = table._columns[i];
      column.setWidth(colWidths[i]);
    }
    table._originalWidth = recalculateTableWidth(colWidths);

    table._updateResizeHandlePositions = function() {
      for (var i = 0, count = table._columns.length; i < count; i++) {
        var column = table._columns[i];
        if (column._resizeHandle)
          column._resizeHandle._updatePos();
      }
    }

    O$.addEventHandler(window, "resize", table._updateResizeHandlePositions);
    O$.addEventHandler(table, "mouseover", function() {
      if (!table._columnResizingInProgress)
        table._updateResizeHandlePositions();
    });

    table._unloadHandlers.push(function() {
      O$.removeEventHandler(window, "resize", table._updateResizeHandlePositions);
    })

    table._fixFF3ColResizingIssue = function() { // See JSFC-3720
      if (! (O$.isMozillaFF3() && O$.isQuirksMode()))
        return;
      if (table._deepestColumnHierarchyLevel > 1) {
        var prevWidth = table.style.width;
        table.style.width = "0";
        table.styleWidth = prevWidth;
      }
    }


  });

}
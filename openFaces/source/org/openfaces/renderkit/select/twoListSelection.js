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
O$._initTwoListSelection = function(controlId,
                                    events,
                                    allowAddRemoveAll,
                                    allowItemsOrdering,
                                    disabled,
                                    rolloverClass) {
  var tls = O$(controlId);
  tls._leftSelectionField = O$(controlId + "::left_listBox_selection");
  tls._rightSelectionField = O$(controlId + "::right_listBox_selection");
  tls._leftListBox = O$(controlId + "::left");
  tls._rightListBox = O$(controlId + "::right");

  tls._leftCaption = O$(controlId + "::left_caption");
  tls._rightCaption = O$(controlId + "::right_caption");
  tls._ascImage = O$(controlId + "::sort_asc")
  tls._desImage = O$(controlId + "::sort_desc")

  tls._selectBtn = O$(controlId + "::select");
  tls._removeBtn = O$(controlId + "::remove");
  tls._disabled = disabled;

  O$.initComponent(controlId, {rollover: rolloverClass}, events);

  if (disabled) {
    tls._selectAllBtn = O$(controlId + "::select_all");
    tls._removeAllBtn = O$(controlId + "::remove_all");
    tls._moveUpBtn = O$(controlId + "::up");
    tls._moveDownBtn = O$(controlId + "::down");
  } else {

    tls._leftListBox.onchange = function() {
      O$._tls_updateSelectionField(tls, "left");
      O$._tls_updateButtons(tls);
    }
    tls._leftListBox.ondblclick = function() {
      O$._tls_moveRight(tls);
    }

    tls._rightListBox.onchange = function() {
      O$._tls_updateSelectionField(tls, "right");
      O$._tls_updateButtons(tls);
    }

    tls._rightListBox.ondblclick = function() {
      O$._tls_moveLeft(tls);
    }
    tls._selectBtn.onclick = function() {
      O$._tls_moveRight(tls);
    }
    tls._removeBtn.onclick = function() {
      O$._tls_moveLeft(tls);
    }

    if (O$.isExplorer()) {
      tls._selectBtn.ondblclick = function() {
        this.onclick();
      }
      tls._removeBtn.ondblclick = function() {
        this.onclick();
      }
    }

    if (allowAddRemoveAll) {
      tls._selectAllBtn = O$(controlId + "::select_all");
      tls._removeAllBtn = O$(controlId + "::remove_all");
      tls._selectAllBtn.onclick = function() {
        O$._tls_moveAllRight(tls);
      }
      tls._removeAllBtn.onclick = function() {
        O$._tls_moveAllLeft(tls);
      }
    }
    tls._allowItemsOrdering = allowItemsOrdering;
    if (allowItemsOrdering) {
      tls._moveUpBtn = O$(controlId + "::up");
      tls._moveDownBtn = O$(controlId + "::down");
      tls._moveUpBtn.onclick = function() {
        O$._tls_moveUp(tls);
      }
      tls._moveDownBtn.onclick = function() {
        O$._tls_moveDown(tls);
      }
      if (O$.isExplorer()) {
        tls._moveUpBtn.ondblclick = function() {
          this.onclick();
        }
        tls._moveDownBtn.ondblclick = function() {
          this.onclick();
        }
      }
    }
  }

  if (O$.isExplorer() || O$.isMozillaFF() || O$.isSafari3AndLate() /*todo:check whether O$.isSafari3AndLate check is really needed (it was added by mistake)*/) {
    setTimeout(function() {
      O$._tls_updateButtonsWidth(tls);
    }, 100);
  } else {
    O$._tls_updateButtonsWidth(tls);
  }
  if (!disabled) {
    tls._selectedItemsField = O$(controlId + "::selected_items");
    tls._allowAddRemoveAll = allowAddRemoveAll;
    O$._tls_updateButtons(tls, true);

    var lb = tls._leftListBox;
    lb.onchange = function() {
      O$._tls_updateSelectionField(tls, "left");
      O$._tls_updateButtons(tls);
    };

    lb = tls._rightListBox;
    lb.onchange = function() {
      O$._tls_updateSelectionField(tls, "right");
      O$._tls_updateButtons(tls);
    };

    if (tls._ascImage != undefined) {
      tls._sortingDirection = false;

      // todo move it to default style for sortable list caption in TwoListSelectionRanderer, when "JSFC-497 TwoListSelection: make first list sortable" is done
      tls._rightCaption.style.cursor = "pointer";
      // -------------

      tls._rightCaption.onclick = function () {
        O$._tls_sortSelection(tls);
      }
    }
    // value getter
    tls.getValue = function () {
      return O$._getTLSValue(tls.id);
    };

    // value setter
    tls.setValue = function(value) {
      O$._setTLSValue(tls.id, value);
    };

    // client validation support
    tls._clientValueFunctionExist = true;
    tls._clientValueFunction = function () {
      return tls.getValue();
    }
  }
}

O$._getTLSValue = function(controlId) {
  var tls = O$(controlId);
  var res = O$.getArrayFromString(tls._selectedItemsField.value, ",");
  return res;
}

O$._setTLSValue = function(controlId, value) {
  var tls = O$(controlId);
  var res = "";
  if (value) {
    for (var i = 0; i < value.length; i++) {
      res += value[i];
      if (i < value.length - 1) {
        res += ",";
      }
    }
  }
  tls._selectedItemsField.value = res;
  O$._tls_applyValue(tls, tls._selectedItemsField.value);
  O$._tls_updateButtons(tls);
}

O$._tls_getButtonWidth = function(button, curWidth) {
  if (button.offsetWidth > curWidth) {
    return button.offsetWidth;
  } else {
    return curWidth;
  }
}

O$._tls_updateButtonsWidth = function(tls) {
  var maxBtnWidth = 50 * 1;  // 50 * 1 needed for JSFC-3336
  if (tls._selectAllBtn) {
    maxBtnWidth = O$._tls_getButtonWidth(tls._selectAllBtn, maxBtnWidth);
  }
  if (tls._removeAllBtn) {
    maxBtnWidth = O$._tls_getButtonWidth(tls._removeAllBtn, maxBtnWidth);
  }
  if (tls._moveUpBtn) {
    maxBtnWidth = O$._tls_getButtonWidth(tls._moveUpBtn, maxBtnWidth);
  }
  if (tls._moveDownBtn) {
    maxBtnWidth = O$._tls_getButtonWidth(tls._moveDownBtn, maxBtnWidth);
  }
  maxBtnWidth = O$._tls_getButtonWidth(tls._selectBtn, maxBtnWidth);
  maxBtnWidth = O$._tls_getButtonWidth(tls._removeBtn, maxBtnWidth);

  if (tls._selectAllBtn) {
    tls._selectAllBtn.style.width = maxBtnWidth + "px";
  }
  if (tls._removeAllBtn) {
    tls._removeAllBtn.style.width = maxBtnWidth + "px";
  }
  if (tls._moveUpBtn) {
    tls._moveUpBtn.style.width = maxBtnWidth + "px";
  }
  if (tls._moveDownBtn) {
    tls._moveDownBtn.style.width = maxBtnWidth + "px";
  }
  tls._selectBtn.style.width = maxBtnWidth + "px";
  tls._removeBtn.style.width = maxBtnWidth + "px";
}

O$._tls_applyValue = function(tls, value) {
  var leftListBox = tls._leftListBox;
  var rightListBox = tls._rightListBox;
  O$._tls_moveAllLeft(tls);
  var values = O$.getArrayFromString(value, ",");

  for (var i = 0; i < values.length; i++) {
    var newValue = values[i];
    for (var j = 0; j < leftListBox.options.length; j++) {
      var option = leftListBox.options[j];
      if (option.value == newValue && !option.disabled) {
        O$._tls_moveOption(leftListBox, rightListBox, option, j);
        break;
      }
    }
  }
}

O$._tls_updateButtons = function(tls, isSubmit) {
  var leftListBox = tls._leftListBox;
  var rightListBox = tls._rightListBox;
  var leftSelectionField = tls._leftSelectionField;
  var rightSelectionField = tls._rightSelectionField;

  var leftListOptions = leftListBox.options;
  var rightListOptions = rightListBox.options;
  var leftListSize = leftListOptions.length;
  var rightListSize = rightListOptions.length;
  tls._selectBtn.disabled = !leftSelectionField.value;
  if (isSubmit && leftSelectionField.value) {
    var values = O$.getArrayFromString(leftSelectionField.value, ",");
    for (var valueIndex = 0, valueCount = values.length; valueIndex < valueCount; valueIndex++) {
      var value = values[valueIndex];
      for (var optionIndex = 0; optionIndex < leftListSize; optionIndex++) {
        var option = leftListOptions[optionIndex];
        if (option.value == value) {
          option.selected = true;
          break;
        }
      }
    }
  }

  if (tls._moveUpBtn) {
    tls._moveUpBtn.disabled = true;
    tls._moveDownBtn.disabled = true;
  }
  tls._removeBtn.disabled = !rightSelectionField.value;
  if (rightSelectionField.value) {
    var highlightedValues = O$.getArrayFromString(rightSelectionField.value, ",");
    var firstSelectedIndex = -1;
    var lastSelectedIndex = -1;
    for (var valueIndex = 0; valueIndex < highlightedValues.length; valueIndex++) {
      var value = highlightedValues[valueIndex];
      for (var optionIndex = 0; optionIndex < rightListSize; optionIndex++) {
        var option = rightListOptions[optionIndex];
        if (option.value != value)
          continue;

        if (isSubmit && firstSelectedIndex == -1) {
          option.selected = true;
        }
        if (firstSelectedIndex == -1) {
          firstSelectedIndex = optionIndex;
        }
        lastSelectedIndex = optionIndex;
      }
    }

    if (rightListSize > 1 && tls._allowItemsOrdering) {
      var estimatedSelectedItems = lastSelectedIndex - firstSelectedIndex + 1;
      var contiguousSelection = estimatedSelectedItems == highlightedValues.length;
      if (contiguousSelection) {
        tls._moveUpBtn.disabled = firstSelectedIndex == 0;
        tls._moveDownBtn.disabled = lastSelectedIndex == rightListSize - 1;
      }
    }
  }

  if (tls._allowAddRemoveAll) {
    if (leftListSize == 0) {
      tls._selectAllBtn.disabled = true;
    } else {
      var nonDisabledItemsFound = false;
      for (var leftOptionIndex = 0; leftOptionIndex < leftListSize; leftOptionIndex++) {
        if (!leftListOptions[leftOptionIndex].disabled) {
          nonDisabledItemsFound = true;
          break;
        }
      }
      tls._selectAllBtn.disabled = !nonDisabledItemsFound;
    }

    if (rightListSize == 0) {
      tls._removeAllBtn.disabled = true;
    } else {
      var found = false;
      for (var rightOptionIndex = 0; rightOptionIndex < rightListSize; rightOptionIndex++) {
        if (!rightListOptions[rightOptionIndex].disabled) {
          found = true;
          break;
        }
      }
      tls._removeAllBtn.disabled = !found;
    }
  }

  var selectedItemsField = tls._selectedItemsField;
  var result = "";
  for (var i = 0; i < rightListSize; i++) {
    var obj = rightListOptions[i];
    result += obj.value;
    if (i < rightListSize - 1) {
      result += ",";
    }
  }
  selectedItemsField.value = result;
}


O$._tls_hideOrderingIcon = function(tls) {
  if (tls._ascImage) {
    tls._ascImage.style.display = "none";
    tls._desImage.style.display = "none";
  }
}

O$._tls_updateSelectionField = function(tls, listbox) {
  var listBox;
  var selectionField;
  if (listbox == "left") {
    listBox = tls._leftListBox;
    selectionField = tls._leftSelectionField;
  } else {
    listBox = tls._rightListBox;
    selectionField = tls._rightSelectionField;
  }
  var result = "";
  var options = listBox.options;
  for (var i = 0, count = options.length; i < count; i++) {
    var option = options[i];
    if (!option.selected)
      continue;
    if (option.disabled)
      option.selected = false;
    result += option.value;
    if (i < count - 1) {
      result += ",";
    }
  }
  if (result.lastIndexOf(",") == result.length - 1) {
    result = result.substring(0, result.length - 1)
  }
  selectionField.value = result;
}

O$._tls_moveRight = function(tls) {
  var leftList = tls._leftListBox;
  var rightList = tls._rightListBox;
  var fireOnAdd = false;
  var selectedIndex = -1;
  for (var i = 0; i < leftList.options.length; i++) {
    var lo = leftList.options[i];
    if (lo.selected && !lo.disabled) {
      fireOnAdd = true;

      O$._tls_moveOption(leftList, rightList, lo, i);
      if (selectedIndex = -1)
        selectedIndex = i;
      i--;
      O$._tls_updateSelectionField(tls, "left");
      O$._tls_updateButtons(tls);
    }
  }
  if (selectedIndex == -1)
    return;
  O$._tls_setSelection(leftList, selectedIndex, tls._selectBtn);
  O$._tls_updateSelectionField(tls, "right");
  O$._tls_updateSelectionField(tls, "left");
  O$._tls_updateButtons(tls);
  O$._tls_hideOrderingIcon(tls);
  if (fireOnAdd) {
    O$._tls_fireEvent("add", tls._events.onadd);
    O$._tls_fireEvent("change", tls._events.onchange);
  }
}

O$._tls_moveOption = function(from, to, option, index) {
  to.options[to.options.length] = new Option(option.text, option.value);
  if (O$.isExplorer()) {
    from.options.remove(index);
  } else {
    from.options[index] = null;
  }
}

O$._tls_setSelection = function(listBox, selectedIndex, button) {
  var selected = false;
  // there are items lower
  var options = listBox.options;
  var optionCount = options.length;
  if (optionCount > selectedIndex + 1) {
    for (var i = selectedIndex; i < optionCount; i++) {
      var option = options[i];
      if (!option.disabled) {
        option.selected = true;
        selected = true;
        break;
      }
    }
    if (!selected) {
      // go up
      for (var i = optionCount - 1; i >= 0; i--) {
        var option = options[i];
        if (!option.disabled) {
          option.selected = true;
          selected = true;
          break;
        }
      }
    }
  } else { // there are no items lower. select upper
    for (var i = optionCount - 1; i >= 0; i--) {
      var option = options[i];
      if (!option.disabled) {
        option.selected = true;
        selected = true;
        break;
      }
    }
  }
  button.disabled = !selected;
}

O$._tls_moveAllRight = function(tls) {
  var leftList = tls._leftListBox;
  var rightList = tls._rightListBox;
  var fireOnAdd = false;
  for (var i = 0; i < leftList.options.length; i++) {
    var lo = leftList.options[i];
    if (!lo.disabled) {
      fireOnAdd = true;
      O$._tls_moveOption(leftList, rightList, lo, i);
      i--;
    }
  }
  O$._tls_hideOrderingIcon(tls);
  if (fireOnAdd) {
    O$._tls_updateSelectionField(tls, "left");
    O$._tls_updateButtons(tls);
    O$._tls_fireEvent("add", tls._events.onadd);
    O$._tls_fireEvent("change", tls._events.onchange);
  }
}

O$._tls_moveLeft = function(tls) {
  var leftList = tls._leftListBox;
  var rightList = tls._rightListBox;
  var fireOnRemove = false;
  var selectedIndex = -1;
  for (var i = 0; i < rightList.options.length; i++) {
    var ro = rightList.options[i];
    if (ro.selected && !ro.disabled) {
      fireOnRemove = true;
      O$._tls_moveOption(rightList, leftList, ro, i);
      if (selectedIndex == -1) {
        selectedIndex = i;
      }
      i--;
      O$._tls_updateSelectionField(tls, "right");
      O$._tls_updateButtons(tls);
    }
  }
  if (selectedIndex == -1)
    return;
  O$._tls_setSelection(rightList, selectedIndex, tls._removeBtn);
  O$._tls_updateSelectionField(tls, "left");
  O$._tls_updateSelectionField(tls, "right");
  O$._tls_updateButtons(tls);
  if (fireOnRemove) {
    O$._tls_fireEvent("remove", tls._events.onremove);
    O$._tls_fireEvent("change", tls._events.onchange);
  }
}

O$._tls_moveUp = function(tls) {
  O$._tls_moveSelectedItems(tls, "up");
}

O$._tls_moveDown = function(tls) {
  O$._tls_moveSelectedItems(tls, "down");
}

O$._tls_moveSelectedItems = function(tls, direction) {
  var firstSelectedIndex = tls._rightListBox.selectedIndex;
  if (firstSelectedIndex == -1)
    return;

  var options = tls._rightListBox.options;
  var optionCount = options.length;

  var selectedItemCount = 0;
  for (var i = firstSelectedIndex; i < optionCount; i++) {
    var option = options[i];
    if (option.selected)
      selectedItemCount++;
    else
      break;
  }

  var adjacentIndex = direction == "up" ? firstSelectedIndex - 1 : firstSelectedIndex + selectedItemCount;
  if (adjacentIndex == -1 || adjacentIndex == optionCount)
    return;
  var adjacentOption = options[adjacentIndex];
  var adjacentItemValue = adjacentOption.value;
  var adjacentItemText = adjacentOption.text;
  var adjacentItemDisabled = adjacentOption.disabled;
  var adjacentItemClassName = adjacentOption.className;
  if (adjacentOption.disabled)
    adjacentOption.disabled = false;
  var destOption = adjacentOption;
  for (var i = 0; i < selectedItemCount; i++) {
    var srcOption = options[direction == "up" ? firstSelectedIndex + i : firstSelectedIndex + selectedItemCount - 1 - i];
    destOption.value = srcOption.value;
    destOption.text = srcOption.text;
    if (destOption.className != srcOption.className)
      destOption.className = srcOption.className;

    destOption = srcOption;
  }
  var optionAfter = options[direction == "up" ? adjacentIndex + selectedItemCount : firstSelectedIndex];
  optionAfter.value = adjacentItemValue;
  optionAfter.text = adjacentItemText;
  if (optionAfter.disabled != adjacentItemDisabled)
    optionAfter.disabled = adjacentItemDisabled;
  if (optionAfter.className != adjacentItemClassName)
    optionAfter.className = adjacentItemClassName;

  var newFirstSelectedIndex = direction == "up" ? firstSelectedIndex - 1 : firstSelectedIndex + 1;
  tls._rightListBox.selectedIndex = newFirstSelectedIndex;
  for (var i = 0; i < selectedItemCount; i++) {
    options[newFirstSelectedIndex + i].selected = true;
  }

  O$._tls_updateSelectionField(tls, "right");
  O$._tls_updateButtons(tls);
  O$._tls_hideOrderingIcon(tls);
}

O$._tls_moveAllLeft = function(tls) {
  var leftList = tls._leftListBox;
  var rightList = tls._rightListBox;
  var fireOnRemove = false;
  for (var i = 0; i < rightList.options.length; i++) {
    var ro = rightList.options[i];
    if (!ro.disabled) {
      fireOnRemove = true;
      O$._tls_moveOption(rightList, leftList, ro, i);
      i--;
    }
  }
  if (fireOnRemove) {
    O$._tls_updateSelectionField(tls, "right");
    O$._tls_updateButtons(tls);
    O$._tls_fireEvent("remove", tls._events.onremove);
    O$._tls_fireEvent("change", tls._events.onchange);
  }
}

O$._tls_sortSelection = function(tls) {
  tls._sortingDirection = !tls._sortingDirection;

  var rightList = tls._rightListBox;
  var options = new Array();
  for (var rightIdx = 0, rightCount = rightList.options.length; rightIdx < rightCount; rightIdx++) {
    options.push(rightList.options[rightIdx]);
  }
  if (tls._sortingDirection) {
    options.sort(O$._tls_compareAsc);
  } else {
    options.sort(O$._tls_compareDesc);
  }
  while (rightList.options.length > 0) {
    if (O$.isExplorer()) {
      rightList.options.remove(0);
    } else {
      rightList.options[0] = null;
    }
  }
  for (var i = 0, count = options.length; i < count; i++) {
    rightList.options[rightList.options.length] = options[i];
  }
  O$._tls_updateSelectionField(tls, "right");
  O$._tls_updateButtons(tls);

  if (tls._sortingDirection) {
    tls._ascImage.style.display = "block";
    tls._desImage.style.display = "none";
  } else {
    tls._ascImage.style.display = "none";
    tls._desImage.style.display = "block";
  }
}

O$._tls_compareAsc = function(a, b) {
  if (a.text < b.text) return -1;
  if (a.text > b.text) return 1;
  return 0;
}

O$._tls_compareDesc = function(a, b) {
  if (a.text > b.text) return -1;
  if (a.text < b.text) return 1;
  return 0;
}

O$._tls_fireEvent = function(eventName, handler) {
  if (!handler)
    return;
  var event = O$.createEvent(eventName);
  handler(event);
}

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
O$.TwoListSelection = {
  _init: function(controlId,
                  events,
                  allowAddRemoveAll,
                  allowItemsOrdering,
                  disabled,
                  rolloverClass) {
    var tls = O$.initComponent(controlId, {rollover: rolloverClass}, {
      _allowAddRemoveAll: allowAddRemoveAll,
      _disabled: disabled,
      _allowItemsOrdering: allowItemsOrdering,

      _leftSelectionField: O$(controlId + "::left_listBox_selection"),
      _rightSelectionField: O$(controlId + "::right_listBox_selection"),
      _leftListBox: O$(controlId + "::left"),
      _rightListBox: O$(controlId + "::right"),

      _leftCaption: O$(controlId + "::left_caption"),
      _rightCaption: O$(controlId + "::right_caption"),
      _ascImage: O$(controlId + "::sort_asc"),
      _desImage: O$(controlId + "::sort_desc"),

      _selectBtn: O$(controlId + "::select"),
      _removeBtn: O$(controlId + "::remove"),
      _selectedItemsField: O$(controlId + "::selected_items"),
      _selectAllBtn: O$(controlId + "::select_all"),
      _removeAllBtn: O$(controlId + "::remove_all"),
      _moveUpBtn: O$(controlId + "::up"),
      _moveDownBtn: O$(controlId + "::down"),

      selectAll: function() {
        O$.TwoListSelection._moveAllRight(tls);
      },

      unselectAll: function() {
        O$.TwoListSelection._moveAllLeft(tls);
      },

      getValue: function () {
        return O$.TwoListSelection._getValue(tls.id);
      },

      setValue: function(value) {
        O$.TwoListSelection._setValue(tls.id, value);
      },

      // client validation support
      _clientValueFunctionExists: true,
      _clientValueFunction: function () {
        return tls.getValue();
      }
    }, events);

    if (!disabled) {
      tls._leftListBox.onchange = function() {
        O$.TwoListSelection._updateSelectionField(tls, "left");
        O$.TwoListSelection._updateButtons(tls);
      };
      tls._leftListBox.ondblclick = function() {
        O$.TwoListSelection._moveRight(tls);
      };

      tls._rightListBox.onchange = function() {
        O$.TwoListSelection._updateSelectionField(tls, "right");
        O$.TwoListSelection._updateButtons(tls);
      };

      tls._rightListBox.ondblclick = function() {
        O$.TwoListSelection._moveLeft(tls);
      };
      tls._selectBtn.onclick = function() {
        O$.TwoListSelection._moveRight(tls);
      };
      tls._removeBtn.onclick = function() {
        O$.TwoListSelection._moveLeft(tls);
      };

      if (O$.isExplorer()) {
        tls._selectBtn.ondblclick = function() {
          this.onclick();
        };
        tls._removeBtn.ondblclick = function() {
          this.onclick();
        };
      }

      if (allowAddRemoveAll) {
        tls._selectAllBtn.onclick = function() {
          O$.TwoListSelection._moveAllRight(tls);
        };
        tls._removeAllBtn.onclick = function() {
          O$.TwoListSelection._moveAllLeft(tls);
        };
      }

      if (allowItemsOrdering) {
        tls._moveUpBtn.onclick = function() {
          O$.TwoListSelection._moveUp(tls);
        };
        tls._moveDownBtn.onclick = function() {
          O$.TwoListSelection._moveDown(tls);
        };
        if (O$.isExplorer()) {
          tls._moveUpBtn.ondblclick = function() {
            this.onclick();
          };
          tls._moveDownBtn.ondblclick = function() {
            this.onclick();
          };
        }
      }

      O$.TwoListSelection._updateButtons(tls, true);

      var lb = tls._leftListBox;
      lb.onchange = function() {
        O$.TwoListSelection._updateSelectionField(tls, "left");
        O$.TwoListSelection._updateButtons(tls);
      };

      lb = tls._rightListBox;
      lb.onchange = function() {
        O$.TwoListSelection._updateSelectionField(tls, "right");
        O$.TwoListSelection._updateButtons(tls);
      };

      if (tls._ascImage != undefined) {
        tls._sortingDirection = false;

        // todo move it to default style for sortable list caption in TwoListSelectionRanderer, when "JSFC-497 TwoListSelection: make first list sortable" is done
        tls._rightCaption.style.cursor = "pointer";
        // -------------

        tls._rightCaption.onclick = function () {
          O$.TwoListSelection._sortSelection(tls);
        };
      }

    }

    if (O$.isExplorer() || O$.isMozillaFF()) {
      setTimeout(function() {
        O$.TwoListSelection._updateButtonsWidth(tls);
      }, 100);
    } else {
      O$.TwoListSelection._updateButtonsWidth(tls);
    }

  },

  _getValue: function(controlId) {
    var tls = O$(controlId);
    var res = O$.getArrayFromString(tls._selectedItemsField.value, ",");
    return res;
  },

  _setValue: function(controlId, value) {
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
    O$.TwoListSelection._applyValue(tls, tls._selectedItemsField.value);
    O$.TwoListSelection._updateButtons(tls);
  },

  _getButtonWidth: function(button, curWidth) {
    if (button.offsetWidth > curWidth) {
      return button.offsetWidth;
    } else {
      return curWidth;
    }
  },

  _updateButtonsWidth: function(tls) {
    var maxBtnWidth = 50 * 1;  // 50 * 1 needed for JSFC-3336 (false alert of NOD32 antivirus) 
    if (tls._selectAllBtn) {
      maxBtnWidth = O$.TwoListSelection._getButtonWidth(tls._selectAllBtn, maxBtnWidth);
    }
    if (tls._removeAllBtn) {
      maxBtnWidth = O$.TwoListSelection._getButtonWidth(tls._removeAllBtn, maxBtnWidth);
    }
    if (tls._moveUpBtn) {
      maxBtnWidth = O$.TwoListSelection._getButtonWidth(tls._moveUpBtn, maxBtnWidth);
    }
    if (tls._moveDownBtn) {
      maxBtnWidth = O$.TwoListSelection._getButtonWidth(tls._moveDownBtn, maxBtnWidth);
    }
    maxBtnWidth = O$.TwoListSelection._getButtonWidth(tls._selectBtn, maxBtnWidth);
    maxBtnWidth = O$.TwoListSelection._getButtonWidth(tls._removeBtn, maxBtnWidth);

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
  },

  _applyValue: function(tls, value) {
    var leftListBox = tls._leftListBox;
    var rightListBox = tls._rightListBox;
    O$.TwoListSelection._moveAllLeft(tls);
    var values = O$.getArrayFromString(value, ",");

    for (var i = 0; i < values.length; i++) {
      var newValue = O$.unescapeSymbol(values[i]);
      for (var j = 0; j < leftListBox.options.length; j++) {
        var option = leftListBox.options[j];
        if (option.value == newValue && !option.disabled) {
          O$.TwoListSelection._moveOption(leftListBox, rightListBox, option, j);
          break;
        }
      }
    }
  },

  _updateButtons: function(tls, isSubmit) {
    var leftSelectionField = tls._leftSelectionField;
    var rightSelectionField = tls._rightSelectionField;

    var leftListOptions = tls._leftListBox.options;
    var rightListOptions = tls._rightListBox.options;
    var leftListSize = leftListOptions.length;
    var rightListSize = rightListOptions.length;
    tls._selectBtn.disabled = !leftSelectionField.value;
    var value, valueIndex, valueCount, option, optionIndex;
    if (isSubmit && leftSelectionField.value) {
      var values = O$.getArrayFromString(leftSelectionField.value, ",");
      for (valueIndex = 0, valueCount = values.length; valueIndex < valueCount; valueIndex++) {
        value = O$.unescapeSymbol(values[valueIndex]);
        for (optionIndex = 0; optionIndex < leftListSize; optionIndex++) {
          option = leftListOptions[optionIndex];
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
      for (valueIndex = 0; valueIndex < highlightedValues.length; valueIndex++) {
        value = O$.unescapeSymbol(highlightedValues[valueIndex]);
        for (optionIndex = 0; optionIndex < rightListSize; optionIndex++) {
          option = rightListOptions[optionIndex];
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
      var str = O$.escapeSymbol(obj.value, ",");
      result += str;
      if (i < rightListSize - 1) {
        result += ",";
      }
    }
    selectedItemsField.value = result;
  },


  _hideOrderingIcon: function(tls) {
    if (tls._ascImage) {
      tls._ascImage.style.display = "none";
      tls._desImage.style.display = "none";
    }
  },

  _updateSelectionField: function(tls, listbox) {
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
      result = result.substring(0, result.length - 1);
    }
    selectionField.value = result;
  },

  _moveRight: function(tls) {
    var leftList = tls._leftListBox;
    var rightList = tls._rightListBox;
    var fireOnAdd = false;
    var selectedIndex = -1;
    for (var i = 0; i < leftList.options.length; i++) {
      var lo = leftList.options[i];
      if (lo.selected && !lo.disabled) {
        fireOnAdd = true;

        O$.TwoListSelection._moveOption(leftList, rightList, lo, i);
        if (selectedIndex = -1)
          selectedIndex = i;
        i--;
        O$.TwoListSelection._updateSelectionField(tls, "left");
        O$.TwoListSelection._updateButtons(tls);
      }
    }
    if (selectedIndex == -1)
      return;
    O$.TwoListSelection._setSelection(leftList, selectedIndex, tls._selectBtn);
    O$.TwoListSelection._updateSelectionField(tls, "right");
    O$.TwoListSelection._updateSelectionField(tls, "left");
    O$.TwoListSelection._updateButtons(tls);
    O$.TwoListSelection._hideOrderingIcon(tls);
    if (fireOnAdd) {
      O$.TwoListSelection._fireEvent("add", tls.onadd);
      O$.TwoListSelection._fireEvent("change", tls.onchange);
    }
  },

  _moveOption: function(from, to, option, index) {
    to.options[to.options.length] = new Option(option.text, option.value);
    if (O$.isExplorer()) {
      from.options.remove(index);
    } else {
      from.options[index] = null;
    }
  },

  _setSelection: function(listBox, selectedIndex, button) {
    var selected = false;
    // there are items lower
    var options = listBox.options;
    var optionCount = options.length;
    var i, option;
    if (optionCount > selectedIndex + 1) {
      for (i = selectedIndex; i < optionCount; i++) {
        option = options[i];
        if (!option.disabled) {
          option.selected = true;
          selected = true;
          break;
        }
      }
      if (!selected) {
        // go up
        for ( i = optionCount - 1; i >= 0; i--) {
          option = options[i];
          if (!option.disabled) {
            option.selected = true;
            selected = true;
            break;
          }
        }
      }
    } else { // there are no items lower. select upper
      for (i = optionCount - 1; i >= 0; i--) {
        option = options[i];
        if (!option.disabled) {
          option.selected = true;
          selected = true;
          break;
        }
      }
    }
    button.disabled = !selected;
  },

  _moveAllRight: function(tls) {
    var leftList = tls._leftListBox;
    var rightList = tls._rightListBox;
    var fireOnAdd = false;
    for (var i = 0; i < leftList.options.length; i++) {
      var lo = leftList.options[i];
      if (!lo.disabled) {
        fireOnAdd = true;
        O$.TwoListSelection._moveOption(leftList, rightList, lo, i);
        i--;
      }
    }
    O$.TwoListSelection._hideOrderingIcon(tls);
    if (fireOnAdd) {
      O$.TwoListSelection._updateSelectionField(tls, "left");
      O$.TwoListSelection._updateButtons(tls);
      O$.TwoListSelection._fireEvent("add", tls.onadd);
      O$.TwoListSelection._fireEvent("change", tls.onchange);
    }
  },

  _moveLeft: function(tls) {
    var leftList = tls._leftListBox;
    var rightList = tls._rightListBox;
    var fireOnRemove = false;
    var selectedIndex = -1;
    for (var i = 0; i < rightList.options.length; i++) {
      var ro = rightList.options[i];
      if (ro.selected && !ro.disabled) {
        fireOnRemove = true;
        O$.TwoListSelection._moveOption(rightList, leftList, ro, i);
        if (selectedIndex == -1) {
          selectedIndex = i;
        }
        i--;
        O$.TwoListSelection._updateSelectionField(tls, "right");
        O$.TwoListSelection._updateButtons(tls);
      }
    }
    if (selectedIndex == -1)
      return;
    O$.TwoListSelection._setSelection(rightList, selectedIndex, tls._removeBtn);
    O$.TwoListSelection._updateSelectionField(tls, "left");
    O$.TwoListSelection._updateSelectionField(tls, "right");
    O$.TwoListSelection._updateButtons(tls);
    if (fireOnRemove) {
      O$.TwoListSelection._fireEvent("remove", tls.onremove);
      O$.TwoListSelection._fireEvent("change", tls.onchange);
    }
  },

  _moveUp: function(tls) {
    O$.TwoListSelection._moveSelectedItems(tls, "up");
  },

  _moveDown: function(tls) {
    O$.TwoListSelection._moveSelectedItems(tls, "down");
  },

  _moveSelectedItems: function(tls, direction) {
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
    for (i = 0; i < selectedItemCount; i++) {
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
    for (i = 0; i < selectedItemCount; i++) {
      options[newFirstSelectedIndex + i].selected = true;
    }

    O$.TwoListSelection._updateSelectionField(tls, "right");
    O$.TwoListSelection._updateButtons(tls);
    O$.TwoListSelection._hideOrderingIcon(tls);
  },

  _moveAllLeft: function(tls) {
    var leftList = tls._leftListBox;
    var rightList = tls._rightListBox;
    var fireOnRemove = false;
    for (var i = 0; i < rightList.options.length; i++) {
      var ro = rightList.options[i];
      if (!ro.disabled) {
        fireOnRemove = true;
        O$.TwoListSelection._moveOption(rightList, leftList, ro, i);
        i--;
      }
    }
    if (fireOnRemove) {
      O$.TwoListSelection._updateSelectionField(tls, "right");
      O$.TwoListSelection._updateButtons(tls);
      O$.TwoListSelection._fireEvent("remove", tls.onremove);
      O$.TwoListSelection._fireEvent("change", tls.onchange);
    }
  },

  _sortSelection: function(tls) {
    tls._sortingDirection = !tls._sortingDirection;

    var rightList = tls._rightListBox;
    var options = [];
    for (var rightIdx = 0, rightCount = rightList.options.length; rightIdx < rightCount; rightIdx++) {
      options.push(rightList.options[rightIdx]);
    }
    if (tls._sortingDirection) {
      options.sort(O$.TwoListSelection._compareAsc);
    } else {
      options.sort(O$.TwoListSelection._compareDesc);
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
    O$.TwoListSelection._updateSelectionField(tls, "right");
    O$.TwoListSelection._updateButtons(tls);

    if (tls._sortingDirection) {
      tls._ascImage.style.display = "block";
      tls._desImage.style.display = "none";
    } else {
      tls._ascImage.style.display = "none";
      tls._desImage.style.display = "block";
    }
  },

  _compareAsc: function(a, b) {
    if (a.text < b.text) return -1;
    if (a.text > b.text) return 1;
    return 0;
  },

  _compareDesc: function(a, b) {
    if (a.text > b.text) return -1;
    if (a.text < b.text) return 1;
    return 0;
  },

  _fireEvent: function(eventName, handler) {
    if (!handler)
      return;
    var event = O$.createEvent(eventName);
    handler(event);
  }
};
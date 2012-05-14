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

O$.Filters = {
  _showFilter: function(componentId, filterId) {
    var filteredComponent = O$(componentId);
    var f = O$(filterId);

    if (!filteredComponent._filtersToHide)
      filteredComponent._filtersToHide = [];
    filteredComponent._filtersToHide.push(f);
  },

  _getFilterValue: function(filterField) {
    var value = filterField.getValue ? filterField.getValue() : filterField.value;
    return value;
  },

  _filterComponent: function(componentId, filterId, filterField) {
    var currentValue = O$.Filters._getFilterValue(filterField);
    if (filterField._lastSubmittedValue === currentValue) {
      // avoid sending the second request when onchange comes after the key-press-initiated filter has already been
      // already triggered (e.g. unloading a table in Chrome sends onchange, which results in double Ajax-filtering
      // requests when filter-as-you-type is turned on)
      return;
    }
    filterField._lastSubmittedValue = currentValue;

    if (filterField._checkFieldValueAgainstList) filterField._checkFieldValueAgainstList();
    O$.cancelDelayedAction(null, componentId);
    // setTimeout in the following script is needed to avoid page blinking when using combo-box filter in IE (JSFC-2263)
    setTimeout(function() {
      O$.Filters._submitFilter(componentId, filterId, filterField);
    }, 1);
  },

  _submitFilter: function(componentId, filterId, filterField) {
    var filteredComponent = O$(componentId);
    if (filteredComponent._useAjax)
      O$._submitComponentWithField(componentId, filterField, null, filterId ? [filterId] : []);
    else
      O$.submitEnclosingForm(filteredComponent);
  },

  _filterFieldKeyPressHandler: function(componentId, filterId, filterField, event, autoFilteringDelay) {
    if (event.keyCode == 13) {

      // filter only in cases when onchange is not fired by pressing Enter to avoid double filter requests
      var onchangeFired = filterField.nodeName.toUpperCase() != "INPUT";
      if (!onchangeFired)
        O$.Filters._filterComponent(componentId, filterId, filterField);

      event.cancelBubble = true;
      return false;
    } else if (autoFilteringDelay != -1) {
      var valueBefore = filterField.getValue ? filterField.getValue() : filterField.value;
      setTimeout(function() {
        var valueAfter = filterField.getValue ? filterField.getValue() : filterField.value;
        if (filterField.isOpened && !filterField.isOpened)
          filterField._dropDownNavigationStarted = false;
        if (filterField._dropDownNavigationStarted) return;
        if (valueBefore == valueAfter) {
          if (filterField._autoFilterAlreadyScheduled) {
            // postpone auto-filtering until the user stops pressing the buttons
            // (e.g. cursor navigation in the field)
            O$.invokeFunctionAfterDelay(function() {
              O$.Filters._filterComponent(componentId, filterId, filterField);
            }, autoFilteringDelay, componentId);
          }
          return;
        }
        // prevent autofiltering when a user started drop-down navigation
        filterField._autoFilterAlreadyScheduled = true;
        if (filterField.getValue) {
          filterField.onDropdownNavigation = function() {
            filterField._dropDownNavigationStarted = true;
            O$.cancelDelayedAction(null, componentId);
          };
        }
        O$.invokeFunctionAfterDelay(function() {
          O$.Filters._filterComponent(componentId, filterId, filterField);
        }, autoFilteringDelay, componentId);
      }, 50);

    }
    var inFieldNavigation = (event.keyCode >= 35 && event.keyCode <= 40);
    if (inFieldNavigation)
      event.cancelBubble = true;
    return true;
  }


};
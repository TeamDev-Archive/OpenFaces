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

O$.Calendar = {
  CURRENT_MONTH: 1,
  NON_CURRENT_MONTH: 2,
  SELECTED_DAY: 3,

  DAY_COUNTS: [0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334],

  // ================================== INTERNAL FUNCTIONS

  _init: function(calendarId,
                  selectedDate,
                  todayDate,
                  rolloverClass,
                  focusable,
                  focusedClass,
                  dayClass, rolloverDayClass,
                  inactiveMonthDayClass, rolloverInactiveMonthDayClass,
                  selectedDayClass, rolloverSelectedDayClass,
                  todayClass, rolloverTodayClass,
                  weekendClass, rolloverWeekendClass,
                  disabledDayClass, rolloverDisabledDayClass,
                  firstDayOfWeek,
                  headerClassName,
                  dateRanges,
                  showFooter,
                  localeStr,
                  required,
                  disabled) {
    var cal = O$.initComponent(calendarId, {rollover: rolloverClass}, {
      _dayClass: dayClass,
      _rolloverDayClass: rolloverDayClass,
      _inactiveMonthDayClass: inactiveMonthDayClass,
      _rolloverInactiveMonthDayClass: rolloverInactiveMonthDayClass,
      _selectedDayClass: selectedDayClass,
      _rolloverSelectedDayClass: rolloverSelectedDayClass,
      _todayClass: todayClass,
      _rolloverTodayClass: rolloverTodayClass,
      _weekendClass: weekendClass,
      _rolloverWeekendClass: rolloverWeekendClass,
      _disabledDayClass: disabledDayClass,
      _rolloverDisabledDayClass: rolloverDisabledDayClass,
      _headerClass: headerClassName,

      _firstDayOfWeek: firstDayOfWeek,
      _dateRanges: dateRanges,

      _showFooter: showFooter,

      _localeStr: localeStr,
      _required: required,
      _disabled: disabled,

      _weekNumber: O$(calendarId + "::week_num"),
      _monthSelector: O$(calendarId + "--month"),
      _decMonthSelector: O$(calendarId + "::month_decrease"),
      _incMonthSelector: O$(calendarId + "::month_increase"),
      _yearSelector: O$(calendarId + "--year"),
      _decYearSelector: O$(calendarId + "::year_decrease"),
      _incYearSelector: O$(calendarId + "::year_increase"),
      _todaySelector: O$(calendarId + "::today"),
      _noneSelector: O$(calendarId + "::none"),
      _valueHolder: O$(calendarId + "::long_date_holder"),
      _valueDateHolder: O$(calendarId + "::date_holder"),
      _hasOwnItsOwnMouseBehavior: true,

      ondragstart: O$.breakEvent,
      onselectstart: O$.breakEvent
    });

    if (focusable) {
      O$.setupArtificialFocus(cal, focusedClass);

      var eventName = (O$.isSafariOnMac() || O$.isOpera() || O$.isMozillaFF()) ? "onkeypress" : "onkeydown";
      cal._prevKeyHandler = cal[eventName];
      cal[eventName] = function (evt) {
        var e = evt ? evt : window.event;
        switch (e.keyCode) {
          case 33: // page up
            O$.Calendar._prevMonth(cal);
            break;
          case 34: // page down
            O$.Calendar._nextMonth(cal);
            break;
          case 38: // up
            O$.Calendar._incSelectedDate(cal, -7);
            break;
          case 40: // down
            O$.Calendar._incSelectedDate(cal, 7);
            break;
          case 37: // left
            O$.Calendar._incSelectedDate(cal, -1);
            break;
          case 39: // right
            O$.Calendar._incSelectedDate(cal, 1);
            break;
        }
        if (cal._prevKeyHandler) {
          cal._prevKeyHandler(e);
        }
        if ((e.keyCode == 33 || e.keyCode == 34 || e.keyCode == 38 || e.keyCode == 40 || e.keyCode == 37
                || e.keyCode == 39) && O$.isSafari()) {
          return false;
        }
      };
    }

    var colTag = O$(calendarId + "::col");
    var dateCellWidth = O$.getStyleClassProperty(dayClass, "width");
    if (dateCellWidth) {
      colTag.style.width = dateCellWidth;
    }

    try {
      var valueHolder = cal._valueHolder;
      var dtf, tDate;
      if (selectedDate) {
        cal._valueDateHolder.value = selectedDate;
        dtf = O$.getDateTimeFormatObject(cal._localeStr);
        if (!dtf) return;
        var sDate = dtf.parse(selectedDate, "dd/MM/yyyy");
        cal._valueHolder.value = sDate.getTime();
        tDate = dtf.parse(todayDate, "dd/MM/yyyy");
        cal._todayDate = tDate;
        O$.Calendar._updateCalendar(cal, sDate);
      } else {
        valueHolder.value = "";
        dtf = O$.getDateTimeFormatObject(cal._localeStr);
        if (!dtf) return;
        tDate = dtf.parse(todayDate, "dd/MM/yyyy");
        cal._todayDate = tDate;
        O$.Calendar._updateCalendar(cal);
      }
    } finally {
      O$.excludeClassNames(cal, ["o_initially_invisible"]);
    }

    if (!cal._disabled) {
      cal._dateChange_listeners = null;
      cal._addDateChangeListener = function(listenerObjectFunction) {
        if (!this._dateChange_listeners) {
          this._dateChange_listeners = [];
        }
        this._dateChange_listeners.push(listenerObjectFunction);
      };

      cal._decMonthSelector.onclick = function(e) {
        O$.Calendar._decMonthClick(cal);
        O$.breakEvent(e);
      };
      cal._decMonthSelector.ondblclick = O$.repeatClickOnDblclick;
      cal._incMonthSelector.onclick = function() {
        O$.Calendar._incMonthClick(cal);
      };
      cal._incMonthSelector.ondblclick = O$.repeatClickOnDblclick;

      var monthSelector = cal._monthSelector;
      monthSelector._drop = O$(monthSelector.id + "--drop");
      monthSelector.onmousedown = function(e) {
        O$.Calendar._hideDrops(cal);
        O$.Calendar._showDrop(cal, this);
        O$.breakEvent(e);
      };

      cal._decYearSelector.onclick = function() {
        O$.Calendar._decYearClick(cal);
      };
      cal._decYearSelector.ondblclick = O$.repeatClickOnDblclick;
      cal._incYearSelector.onclick = function() {
        O$.Calendar._incYearClick(cal);
      };
      cal._incYearSelector.ondblclick = O$.repeatClickOnDblclick;

      var yearSelector = cal._yearSelector;
      yearSelector._drop = O$(yearSelector.id + "--drop");
      yearSelector.onmousedown = function(e) {
        O$.Calendar._hideDrops(cal);
        O$.Calendar._showDrop(cal, this);
        O$.breakEvent(e);
      };
      O$.Calendar._initializeDrops(cal);
      O$.Calendar._adjustMonthAndYearSelectorWidth(cal);
    }

    if (cal._showFooter) {
      var nonSel = cal._noneSelector;
      if (required || !selectedDate || cal._disabled) {
        nonSel.style.color = "#808080";
        nonSel.style.cursor = "default";
        nonSel.onmouseup = null;
      } else {
        nonSel.style.color = "";
        nonSel.style.cursor = "pointer";
        nonSel.onmouseup = function() {
          O$.Calendar._noneClick(cal);
        };
      }
    }

    cal._clientValueFunctionExists = true;
    cal._clientValueFunction = function () {
      var valueHolder = cal._valueHolder;
      return valueHolder.value;
    };

    cal.getSelectedDate = function () {
      return O$.Calendar._getSelectedDate(cal.id);
    };

    cal.setSelectedDate = function (date) {
      O$.Calendar._setSelectedDate(cal.id, date);
    };
  },

  _getSelectedDate: function(calendarId) {
    var cal = O$(calendarId);
    if (cal._isDateChooser) {
      cal = cal._calendar;
    }
    var valueHolder = cal._valueHolder;
    if (!valueHolder.value) {
      return null;
    } else {
      var date = new Date();
      date.setTime(valueHolder.value);
      return date;
    }
  },

  _setSelectedDate: function(calendarId, date) {
    var cal = O$(calendarId);
    if (cal._isDateChooser) {
      cal = cal._calendar;
    }
    var valueHolder = cal._valueHolder;
    var valueDateHolder = cal._valueDateHolder;
    if (date) {
      valueHolder.value = date.getTime();
      O$.Calendar._updateDateHolder(cal, date);
      O$.Calendar._updateCalendar(cal, date);
    } else {
      valueHolder.value = "";
      valueDateHolder.value = "";
      if (cal._showFooter) {
        var none = cal._noneSelector;
        none.style.color = "#808080";
        none.style.cursor = "default";
        none.onmouseup = null;
      }
    }
    O$.Calendar._notifyDateChangeListeners(cal);
  },

  _notifyDateChangeListeners: function(cal) {
    var listeners = cal._dateChange_listeners;
    if (listeners && listeners != null && listeners.length > 0) {
      for (var i = 0; i < listeners.length; i ++) {
        var listener = listeners[i];
        listener(O$.Calendar._getSelectedDate(cal.id));
      }
    }
  },

  _closestDayOfMonth: function(year, month, day) {
    if (new Date(year, month, day).getDate() != day)
      return O$.Calendar._closestDayOfMonth(year, month, --day);
    else
      return new Date(year, month, day);
  },

  _prevMonth: function(cal) {
    var date = O$.Calendar._getSelectedDate(cal.id);
    if (!date) {
      O$.Calendar._selectCurrentDate(cal);
      return;
    }
    var month = date.getMonth();
    var year = date.getFullYear();
    if (month == 0) {
      month = 11;
      year = year - 1;
    } else {
      month = month - 1;
    }
    var day = date.getDate();
    var newDate = O$.Calendar._closestDayOfMonth(year, month, day);

    if (cal._dateRanges && !O$.Calendar._insideRanges(newDate, cal)) {
      return;
    }
    O$.Calendar._cellClick(cal, newDate);
  },

  _insideRanges: function(date, cal) {
    var found = false;
    var ranges = cal._dateRanges.getDateRanges();
    for (var i = 0, count = ranges.length; i < count; i ++) {
      if (ranges[i].isDateInRange(date)) {
        found = true;
        break;
      }
    }
    return found;
  },

  _selectCurrentDate: function(cal) {
    O$.Calendar._todayClick(cal);
  },


  _nextMonth: function(cal) {
    var date = O$.Calendar._getSelectedDate(cal.id);
    if (!date) {
      O$.Calendar._selectCurrentDate(cal);
      return;
    }
    var month = date.getMonth();
    var year = date.getFullYear();
    if (month == 11) {
      month = 0;
      year = year + 1;
    } else {
      month = month + 1;
    }
    var day = date.getDate();
    var newDate = O$.Calendar._closestDayOfMonth(year, month, day);
    if (cal._dateRanges && !O$.Calendar._insideRanges(newDate, cal)) {
      return;
    }
    O$.Calendar._cellClick(cal, newDate);
  },

  _incSelectedDate: function(cal, inc) {
    var date = O$.Calendar._getSelectedDate(cal.id);
    if (!date) {
      O$.Calendar._selectCurrentDate(cal);
      return;
    }
    var newDate = O$.incDay(date, inc);
    if (cal._dateRanges && !O$.Calendar._insideRanges(newDate, cal)) {
      return;
    }
    O$.Calendar._cellClick(cal, newDate);
  },

  _updateCalendar: function(calendar, selectedDate) {
    var monthYearChanged = false;
    var today = calendar._todayDate;
    var dates;
    var none = calendar._noneSelector;
    var todaySel = calendar._todaySelector;
    if (selectedDate) {
      calendar._currentMonth = selectedDate.getMonth();
      calendar._currentYear = selectedDate.getFullYear();
      dates = O$.Calendar._buildDatesArray(calendar, selectedDate, true);
    } else {
      if (!O$.Calendar._checkMonth(calendar._currentMonth) || !calendar._currentYear) {
        calendar._currentYear = today.getFullYear();
        calendar._currentMonth = today.getMonth();
      }
      dates = O$.Calendar._buildDatesArray_YMD(calendar, calendar._currentYear, calendar._currentMonth);
      monthYearChanged = true;
    }

    var tempDate = O$.Calendar._getSelectedDate(calendar.id);
    if (calendar._showFooter) {
      if (tempDate && !calendar._disabled) {
        if (calendar._required) {
          none.style.color = "#808080";
          none.style.cursor = "default";
          none.onmouseup = null;
        } else {
          none.style.color = "";
          none.style.cursor = "pointer";
          none.onmouseup = function() {
            O$.Calendar._noneClick(calendar);
          };
        }
      } else {
        none.style.color = "#808080";
        none.style.cursor = "default";
        none.onmouseup = null;
      }
    }

    var dateRanges = calendar._dateRanges;
    var drStyleClassName;
    var drRolloverStyleClassName;
    var disableExcluded;
    var disableIncluded;
    if (dateRanges) {
      drStyleClassName = dateRanges._styleClassName;
      drRolloverStyleClassName = dateRanges._rolloverStyleClassName;
      disableExcluded = dateRanges._disableExcluded;
      disableIncluded = dateRanges._disableIncluded;
    }

    calendar._todayInBound = false;
    calendar._applyOnclick4today = true;

    var iterator = 0;
    var dayClassName = calendar._dayClass;
    if (dayClassName === undefined)
      dayClassName = null; // need to distinguish between assigned and not assigned cell._className
    var rolloverDayClassName = calendar._rolloverDayClass;
    var inactiveMonthDayClassName = calendar._inactiveMonthDayClass;
    var rolloverInactiveMonthDayClassName = calendar._rolloverInactiveMonthDayClass;
    var weekendClassName = calendar._weekendClass;
    var rolloverWeekendClassName = calendar._rolloverWeekendClass;
    var allCells = [];
    var body = O$(calendar.id + "::body");
    var allTdElements = body.getElementsByTagName("div");
    O$.assert(allTdElements.length == 6 * 7);
    var tdIndex = 0;
    for (var row = 0; row < 6; row++) {
      for (var col = 0; col < 7; col++) {
        var cell = allTdElements[tdIndex++];
        var pair = dates[iterator++];
        var type = pair[0];
        var date = pair[1];
        var thisIsToday = pair._today;
        allCells.push(cell);

        cell._type = type;

        if (!cell._eventsInitialized) {
          cell._eventsInitialized = true;
          cell.onmouseover = function() {
            if (this._rolloverClassName)
              O$.appendClassNames(this, [this._rolloverClassName]);
            O$.Calendar._operaStrictWorkaround();
            calendar._lastHoveredCell = this;
          };

          cell.onmouseout = function() {
            if (this._rolloverClassName)
              O$.excludeClassNames(this, [this._rolloverClassName]);
            O$.Calendar._operaStrictWorkaround();
          };
        }

        cell._className = dayClassName; // use this _className property temporarily to avoid repetitive className assignments, which affects performance in IE6
        cell._rolloverClassName = rolloverDayClassName;

        // set WEEKEND style
        var day = date.getDay();
        if (day == 0 || day == 6) {
          cell._className += " " + weekendClassName;
          cell._rolloverClassName += " " + rolloverWeekendClassName;
        }

        switch (type) {
          case O$.Calendar.CURRENT_MONTH:
            cell._isCurrentMonth = true;
            break;
          case O$.Calendar.NON_CURRENT_MONTH:
            cell._className += " " + inactiveMonthDayClassName;
            cell._rolloverClassName += " " + rolloverInactiveMonthDayClassName;
            cell._isCurrentMonth = false;
            break;
        }

        var foundInRange = false;

        if (dateRanges) {
          var ranges = dateRanges.getDateRanges();
          for (var i = 0, count = ranges.length; i < count; i ++) {
            var range = ranges[i];
            if (range.isDateInRange(date)) {
              cell._className += " " + range._styleClassName;
              cell._rolloverClassName += " " + range._rolloverStyleClassName;
              foundInRange = true;
            }
          }
          if (foundInRange) {
            cell._className += " " + dateRanges._styleClassName;
            cell._rolloverClassName += " " + dateRanges._rolloverStyleClassName;
          }

          if ((!foundInRange && disableExcluded) || (foundInRange && disableIncluded)) {
            cell._className += " " + calendar._disabledDayClass;
            cell._rolloverClassName += " " + calendar._rolloverDisabledDayClass;
            if (type == O$.Calendar.NON_CURRENT_MONTH)
              cell._rolloverClassName = cell._rolloverClassName.replace(rolloverInactiveMonthDayClassName, "");
          }
        }

        // set TODAY style
        if (thisIsToday) {
          calendar._todayInBound = true;
          cell._className += " " + calendar._todayClass;
          cell._rolloverClassName += " " + calendar._rolloverTodayClass;
          cell._isToday = true;
          if (calendar._showFooter) {
            todaySel._todayCell = cell;
          }
        } else {
          cell._isToday = false;
        }

        if (thisIsToday && dateRanges && ((foundInRange && disableIncluded) || (!foundInRange && disableExcluded))) {
          calendar._applyOnclick4today = false;
        }

        if (type == O$.Calendar.SELECTED_DAY) {
          cell._className += " " + calendar._selectedDayClass;
          cell._rolloverClassName += " " + calendar._rolloverSelectedDayClass;
          calendar._selectedCell = cell;
          cell._isSelected = true;
        } else {
          cell._isSelected = false;
        }

        cell._date = date;
        cell.innerHTML = date.getDate();

        O$.Calendar._updateDateRangesSelectedStyles(calendar, cell);

        if (!dateRanges ||
            (dateRanges && ((foundInRange && !disableIncluded) || (!foundInRange && !disableExcluded)))) {
          if (type == O$.Calendar.NON_CURRENT_MONTH) {
            cell.onmouseup = function() {
              O$.Calendar._cellClick(calendar, this._date);
            };
          } else if (type == O$.Calendar.SELECTED_DAY) {
            cell.onmouseup = null;
          } else if (type == O$.Calendar.CURRENT_MONTH) {
            cell.onmouseup = function() {
              O$.Calendar._updateWithinMonth(calendar, this._date, this);
            };
          }
        } else {
          cell.onmouseup = null;
        }

        if (calendar._disabled) {
          cell._className += " " + calendar._disabledDayClass;
          cell._rolloverClassName += " " + calendar._rolloverDisabledDayClass;
          if (type == O$.Calendar.NON_CURRENT_MONTH)
            cell._rolloverClassName = cell._rolloverClassName.replace(rolloverInactiveMonthDayClassName, "");
          cell.onmouseover = null;
          cell.onmouseout = null;
          cell.onmouseup = null;
          cell.onclick = null;
        }
        cell.className = cell._className;
        cell._className = undefined;
      }
    }
    if (O$.isMozillaFF() || O$.isSafari3AndLate() /*todo:check whether O$.isSafari3AndLate check is really needed (it was added by mistake)*/) {
      O$.addUnloadEvent(function() {
        // work around Mozilla problems firing old events when new page is loading (JSFC-2276)
        for (var i = 0, count = allCells.length; i < count; i++) {
          var cell = allCells[i];
          cell.onmouseover = null;
          cell.onmouseout = null;
          cell.onmouseup = null;
          cell.onclick = null;
        }
        allCells = null;
      });
    }
    if (calendar._showFooter) {
      O$.Calendar._updateToday(calendar);
    }


    var ms = calendar._monthSelector;
    var ys = calendar._yearSelector;

    var dtf = O$.getDateTimeFormatObject(calendar._localeStr);
    if (!dtf) return;
    var months = dtf.getMonths();
    if (selectedDate) {
      ms.innerHTML = months[selectedDate.getMonth()];
      ys.innerHTML = selectedDate.getFullYear();
    } else {
      if (monthYearChanged) {
        ms.innerHTML = months[calendar._currentMonth];
        ys.innerHTML = calendar._currentYear;
      } else {
        ms.innerHTML = months[today.getMonth()];
        ys.innerHTML = today.getFullYear();
      }
    }
    if (calendar._onPeriodChange) {
      var e = O$.createEvent("periodchange");
      calendar._onPeriodChange(e);
    }
    calendar._dropsInitialized = false;
    O$.repaintAreaForOpera(calendar, true);
  },

  _updateToday: function(calendar) {
    var date = O$.Calendar._getSelectedDate(calendar.id);
    var today = new Date();
    if (date && date.getFullYear() == today.getFullYear()
            && date.getMonth() == today.getMonth()
            && date.getDate() == today.getDate()
            && calendar._applyOnclick4today) {
      calendar._applyOnclick4today = false;
    }

    if (calendar._dateRanges && calendar._applyOnclick4today) {
      var foundInRange = false;
      var ranges = calendar._dateRanges.getDateRanges();
      for (var i = 0, count = ranges.length; i < count; i ++) {
        if (ranges[i].isDateInRange(today)) {
          foundInRange = true;
          break;
        }
      }
      if ((foundInRange && calendar._dateRanges._disableIncluded)
              || (!foundInRange && calendar._dateRanges._disableExcluded)) {
        calendar._applyOnclick4today = false;
      }
    }
    var todaySel = calendar._todaySelector;
    if (calendar._todayInBound) { //apply inbound onclick for today
      if (calendar._applyOnclick4today && !calendar._disabled) {
        todaySel.style.color = "";
        todaySel.style.cursor = "pointer";
        if (calendar._currentMonth == today.getMonth() && calendar._currentYear == today.getFullYear()) {
          todaySel.onmouseup = function() {
            O$.Calendar._updateWithinMonth(calendar, this._todayCell._date, this._todayCell);
          };
        } else {
          todaySel.onmouseup = function() {
            O$.Calendar._todayClick(calendar);
          };
        }
      } else {
        todaySel.style.color = "#808080";
        todaySel.style.cursor = "default";
        todaySel.onmouseup = null;
      }
    } else { //apply whole calendar update onclick for today
      if (calendar._applyOnclick4today && !calendar._disabled) {
        todaySel.style.color = "";
        todaySel.style.cursor = "pointer";
        todaySel.onmouseup = function() {
          O$.Calendar._todayClick(calendar);
        };
      } else {
        todaySel.style.color = "#808080";
        todaySel.style.cursor = "default";
        todaySel.onmouseup = null;
      }
    }
  },

  _cellClick: function(cal, date) {
    O$.Calendar._setSelectedDate(cal.id, O$.cloneDateTime(date));
    if (cal._onDateChange) {
      cal._onDateChange();
    }
  },

  _buildDatesArray_YMD: function(calendar, year, month, day) {
    if (!year) {
      return O$.Calendar._buildDatesArray(calendar, null, false);
    }
    if (!O$.Calendar._checkMonth(month)) {
      alert("Month check: " + month);
      return O$.Calendar._buildDatesArray(calendar, null, false);
    }
    if (!day) {
      var date = new Date(year, month, 1);
      return O$.Calendar._buildDatesArray(calendar, date, false);
    }
    return O$.Calendar._buildDatesArray(calendar, new Date(year, month, day), false);
  },

  _checkMonth: function(month) {
    return month >= 0 && month < 12;
  },

  _buildDatesArray: function(calendar, date, isSelected) {
    var today = calendar._todayDate;
    if (!date)
      date = today;

    var incYear = date.getFullYear();
    var incMonth = date.getMonth();
    var incDay = date.getDate();

    var todayYear = today.getFullYear();
    var todayMonth = today.getMonth();
    var todayDate = today.getDate();

    var monthStartDate = new Date(incYear, incMonth, 1);
    var dayOfWeek = monthStartDate.getDay();
    var preDays;
    var firstDayOfWeek = calendar._firstDayOfWeek;

    preDays = dayOfWeek - firstDayOfWeek;
    if (preDays > 6) preDays -= 7;
    else if (preDays < 0) preDays += 7;

    monthStartDate = O$.incDay(monthStartDate, -preDays);
    var dates = [];

    var storedSelectedDate = O$.Calendar._getSelectedDate(calendar.id);
    if (storedSelectedDate) {
      var storedSelectedDate_date = storedSelectedDate.getDate();
      var storedSelectedDate_month = storedSelectedDate.getMonth();
      var storedSelectedDate_fullYear = storedSelectedDate.getFullYear();
    }
    var currentDay = monthStartDate;
    for (var i = 0; i < 42; i ++) {
      var y = currentDay.getFullYear();
      var m = currentDay.getMonth();
      var d = currentDay.getDate();

      var pair = [];

      if (y < incYear || m < incMonth) {
        pair[0] = O$.Calendar.NON_CURRENT_MONTH;
      } else if (y > incYear || m > incMonth) {
        pair[0] = O$.Calendar.NON_CURRENT_MONTH;
      } else if ((d == incDay && isSelected)
              || (storedSelectedDate
              && storedSelectedDate_date == d
              && storedSelectedDate_month == m
              && storedSelectedDate_fullYear == y)) {
        pair[0] = O$.Calendar.SELECTED_DAY;
      } else {
        pair[0] = O$.Calendar.CURRENT_MONTH;
      }
      pair[1] = currentDay;
      if (d == todayDate && m == todayMonth && y == todayYear)
        pair._today = true;

      dates.push(pair);
      currentDay = O$.incDay(currentDay);
    }
    return dates;
  },

  _compareDates: function(date1, date2) {
    if (date1.getFullYear() < date2.getFullYear()) return -1;
    else if (date1.getFullYear() == date2.getFullYear()
            && date1.getMonth() < date2.getMonth()) return -1;
    else if (date1.getFullYear() == date2.getFullYear()
            && date1.getMonth() == date2.getMonth()
            && date1.getDate() < date2.getDate()) return -1;
    else if (date1.getFullYear() == date2.getFullYear()
            && date1.getMonth() == date2.getMonth()
            && date1.getDate() == date2.getDate()) return 0;
    else if (date1.getFullYear() > date2.getFullYear()) return 1;
    else if (date1.getFullYear() == date2.getFullYear()
            && date1.getMonth() > date2.getMonth()) return 1;
    else return 1;
  },

  _decMonthClick: function(cal) {
    var month = cal._currentMonth;
    if (month == 0) {
      month = 11;
      cal._currentYear = cal._currentYear - 1;
    } else {
      month = month - 1;
    }
    cal._currentMonth = month;
    O$.Calendar._updateCalendar(cal);
  },

  _incMonthClick: function(cal) {
    var month = cal._currentMonth;
    if (month == 11) {
      month = 0;
      cal._currentYear = cal._currentYear + 1;
    } else {
      month = month + 1;
    }
    cal._currentMonth = month;
    O$.Calendar._updateCalendar(cal);
  },

  _decYearClick: function(cal) {
    cal._currentYear -= 1;
    O$.Calendar._updateCalendar(cal);
  },

  _incYearClick: function(cal) {
    cal._currentYear += 1;
    O$.Calendar._updateCalendar(cal);
  },

  _todayClick: function(cal) {
    var date = cal._todayDate;
    if (cal._dateRanges) {
      var found = false;
      var ranges = cal._dateRanges.getDateRanges();
      for (var i = 0, count = ranges.length; i < count; i ++) {
        if (ranges[i].isDateInRange(date)) {
          found = true;
          break;
        }
      }
      if ((found && !cal._dateRanges._disableIncluded)
              || (!found && !cal._dateRanges._disableExcluded)) {
        O$.Calendar._setTodaySettings(cal, date);
      }
    } else {
      O$.Calendar._setTodaySettings(cal, date);
    }
  },

  _setTodaySettings: function(cal, date) {
    O$.Calendar._setSelectedDate(cal.id, date);

    if (cal._onDateChange) {
      cal._onDateChange();
    }
  },

  _noneClick: function(calendar) {
    O$.Calendar._setSelectedDate(calendar.id);
    calendar._applyOnclick4today = true;
    O$.Calendar._updateToday(calendar);
    //todo: clear selection from cell

    var selectedCell = calendar._selectedCell;
    if (selectedCell) {
      var className = selectedCell.className;
      var rolloverClassName = selectedCell._rolloverClassName;
      selectedCell.className = className.replace(calendar._selectedDayClass, "");
      selectedCell._rolloverClassName = rolloverClassName.replace(calendar._rolloverSelectedDayClass, "");
      calendar._selectedCell = null;
      if (calendar._dateRanges) {
        O$.excludeClassNames(selectedCell, [calendar._dateRanges._selectedDayClass]);
        selectedCell._rolloverClassName
                = selectedCell._rolloverClassName.replace(calendar._dateRanges._rolloverSelectedDayClass, "");
        var ranges = calendar._dateRanges.getDateRanges();
        for (var i = 0, count = ranges.length; i < count; i ++) {
          var simpleRange = ranges[i];
          if (simpleRange.isDateInRange(selectedCell._date) && !calendar._dateRanges._disableIncluded) {
            if (simpleRange._selectedDayStyleClassName) {
              selectedCell.className = selectedCell.className.replace(simpleRange._selectedDayStyleClassName, "");
            }
            if (simpleRange._rolloverSelectedDayStyleClassName) {
              selectedCell._rolloverClassName
                      = selectedCell._rolloverClassName.replace(simpleRange._rolloverSelectedDayStyleClassName, "");
            }
          }
        }
      }
      if (selectedCell._isCurrentMonth) {
        selectedCell.onmouseup = function () {
          O$.Calendar._updateWithinMonth(calendar, this._date, this);
        };
      } else {
        selectedCell.onmouseup = function () {
          O$.Calendar._cellClick(calendar, this._date);
        };
      }
    }
    if (calendar._onDateChange) {
      calendar._onDateChange();
    }
  },

  _fillDrop: function(calendar, dropTable, items, onclickHandler, boldIndex) {
    var rolloverClass = calendar._selectedDayClass;
    dropTable.className = calendar._headerClass;

    var nodeChildren = dropTable.childNodes;
    var copiedCildren = [];
    var i, count;
    for (i = 0; i < nodeChildren.length; i++) {
      copiedCildren[i] = nodeChildren[i];
    }
    for (i = 0; i < copiedCildren.length; i++) {
      var child = copiedCildren[i];
      dropTable.removeChild(child);
    }

    var tbody = document.createElement("tbody");
    dropTable.appendChild(tbody);

    var parentFontFamily = O$.getElementStyle(dropTable, "font-family");
    var parentFontSize = O$.getElementStyle(dropTable, "font-size");
    var parentFontWeight = O$.getElementStyle(dropTable, "font-weight");
    var parentFontStyle = O$.getElementStyle(dropTable, "font-style");

    for (i = 0, count = items.length; i < count; i ++) {
      var tr = document.createElement("tr");
      var td = document.createElement("td");
      tr.appendChild(td);
      var div = document.createElement("div");
      td.appendChild(div);
      div.style.fontFamily = parentFontFamily;
      div.style.fontSize = parentFontSize;
      div.style.fontWeight = parentFontWeight;
      div.style.fontStyle = parentFontStyle;
      div.id = dropTable.id + "_" + i;
      div.innerHTML = items[i];
      div._rolloverClass = rolloverClass;
      div.style.width = "100%";

      tr._itemNo = i;
      tr._item = items[i];
      tr._table = dropTable;
      var applyOnclick = true;
      if (i == boldIndex) {
        div.style.fontWeight = "bold";
        tr._isBold = true;
        applyOnclick = false;
      }

      tr.onmouseover = function() {
        O$.Calendar._dropItemMouseOver(this);
        O$.Calendar._operaStrictWorkaround();
      };
      tr.onmouseout = function () {
        O$.Calendar._dropItemMouseOut(this);
        O$.Calendar._operaStrictWorkaround();
      };
      if (applyOnclick) {
        tr.onmouseup = onclickHandler;
        tr.style.cursor = "pointer";
      } else {
        tr.style.cursor = "default";
      }
      tr._drop = dropTable;
      tbody.appendChild(tr);
    }

    dropTable.style.border = "1px solid black";
    dropTable.onmouseout = function () {
      if (this._timeout) {
        clearTimeout(this._timeout);
      }
      this._timeout = setTimeout(function() {
        O$.Calendar._hideDrop(dropTable);
      }, 3000);
    };
    dropTable.onmouseover = function() {
      if (this._timeout) {
        clearTimeout(this._timeout);
      }
    };
  },

  _initializeDrops: function(cal) {
    if (cal._dropsInitialized)
      return;
    cal._dropsInitialized = true;

    var monthDrop = cal._monthSelector._drop;
    var dtf = O$.getDateTimeFormatObject(cal._localeStr);
    if (!dtf) {
      O$.logError("O$.Calendar._initializeDrops: unsupported locale: " + cal._localeStr);
      return;
    }
    var months = dtf.getMonths();
    O$.Calendar._fillDrop(cal, monthDrop, months, function() {
      O$.Calendar._monthDropItemClick(cal, this);
    }, cal._currentMonth);

    monthDrop.style.width = "11ex";
    monthDrop.style.textAlign = "center";

    var yearDrop = cal._yearSelector._drop;

    var yearsBefore = 4;
    var yearsAfter = 4;
    var curYear = cal._currentYear;
    var firstYear = curYear - yearsBefore;
    var lastYear = curYear + yearsAfter;
    var years = [];

    for (var i = firstYear; i <= lastYear; i++)
      years.push(i);

    O$.Calendar._fillDrop(cal, yearDrop, years, function() {
      O$.Calendar._yearDropItemClick(cal, this);
    }, yearsBefore);

    yearDrop.style.width = "6ex";
    yearDrop.style.textAlign = "center";
  },

  _hideDrop: function(drop) {
    if (drop._timeout) {
      clearTimeout(drop._timeout);
    }
    drop.hide();
  },

  _hideDrops: function(cal) {
    var monthDrop = cal._monthSelector._drop;
    var yearDrop = cal._yearSelector._drop;
    O$.Calendar._hideDrop(monthDrop);
    O$.Calendar._hideDrop(yearDrop);
  },

  _showDrop: function(calendar, selector) {
    var drop = selector._drop;
    O$.correctElementZIndex(drop, calendar);
    O$.Calendar._initializeDrops(calendar);
    var selectorRect = O$.getElementBorderRectangle(selector, true);
    drop.showAtXY(selectorRect.x, selectorRect.getMaxY());
    O$.Calendar._operaStrictWorkaround();
  },

  _dropItemMouseOver: function(dropItem) {
    dropItem.style.background = !O$.isSafari() ? "Highlight" : "blue";
    dropItem.style.color = !O$.isSafari() ? "HighlightText" : "white";
    dropItem.style.cursor = "pointer";
    if (dropItem._isBold) {
      dropItem.style.fontWeight = "bold";
    }
  },

  _dropItemMouseOut: function(dropItem) {
    dropItem.style.background = O$.isSafari() ? "none" : "";
    dropItem.style.color = "";
    dropItem.style.cursor = "pointer";
    if (dropItem._isBold) {
      dropItem.style.fontWeight = "bold";
    }
  },

  _monthDropItemClick: function(cal, item) {
    O$.Calendar._hideDrop(item._drop);
    cal._currentMonth = item._itemNo;
    O$.Calendar._updateCalendar(cal);
  },

  _yearDropItemClick: function(cal, item) {
    O$.Calendar._hideDrop(item._drop);
    cal._currentYear = item._item;
    O$.Calendar._updateCalendar(cal);
  },

  _operaStrictWorkaround: function() {
    if (!O$.isOpera())
      return;
    var body = document.getElementsByTagName("body")[0];
    body.style.visibility = "hidden";
    body.style.visibility = "visible";
  },

  _updateWithinMonth: function(calendar, date, cell) {
    var selectedCell = calendar._selectedCell;
    var useTempStyle;
    if (selectedCell) {
      selectedCell._isSelected = false;
      useTempStyle = selectedCell._className !== undefined;
      var className = useTempStyle ? selectedCell._className : selectedCell.className;
      var rolloverClassName = selectedCell._rolloverClassName;

      className = className.replace(calendar._selectedDayClass, "");
      if (useTempStyle)
        selectedCell._className = className;
      else
        selectedCell.className = className;
      selectedCell._rolloverClassName = rolloverClassName.replace(calendar._rolloverSelectedDayClass, "");
      selectedCell.onmouseup = function() {
        O$.Calendar._updateWithinMonth(calendar, this._date, this);
      };
    }
    if (cell._className !== undefined)
      cell._className += " " + calendar._selectedDayClass;
    else
      cell.className += " " + calendar._selectedDayClass;
    cell._rolloverClassName += " " + calendar._rolloverSelectedDayClass;
    cell._isSelected = true;
    cell.onmouseup = null;
    calendar._selectedCell = cell;

    if (calendar._showFooter) {
      var none = calendar._noneSelector;
      none.style.color = "";
      none.style.cursor = "pointer";
      none.onmouseup = function() {
        O$.Calendar._noneClick(calendar);
      };
    }

    calendar._applyOnclick4today = true;

    var valueHolder = calendar._valueHolder;
    var clonedDate = O$.cloneDateTime(date);
    valueHolder.value = clonedDate.getTime();
    O$.Calendar._updateDateHolder(calendar, clonedDate);
    calendar._currentMonth = date.getMonth();
    calendar._currentYear = date.getFullYear();

    if (calendar._dateRanges) {
      if (selectedCell) {
        useTempStyle = selectedCell._className !== undefined;
        var newStyle = (useTempStyle ? selectedCell._className : selectedCell.className).replace(calendar._dateRanges._selectedDayClass, "");
        if (useTempStyle)
          selectedCell._className = newStyle;
        else
          selectedCell.className = newStyle;
        selectedCell._rolloverClassName
                = selectedCell._rolloverClassName.replace(calendar._dateRanges._rolloverSelectedDayClass, "");
        var ranges = calendar._dateRanges.getDateRanges();
        for (var i = 0, count = ranges.length; i < count; i++) {
          var simpleRange = ranges[i];
          if (simpleRange.isDateInRange(selectedCell._date)) {
            if (!calendar._dateRanges._disableIncluded) {
              if (simpleRange._selectedDayStyleClassName) {
                newStyle = (useTempStyle ? selectedCell._className : selectedCell.className).replace(simpleRange._selectedDayStyleClassName, "");
                if (useTempStyle)
                  selectedCell._className = newStyle;
                else
                  selectedCell.className = newStyle;
              }
              if (simpleRange._rolloverSelectedDayStyleClassName) {
                selectedCell._rolloverClassName
                        = selectedCell._rolloverClassName.replace(simpleRange._rolloverSelectedDayStyleClassName, "");
              }
            }
          }
        }
      }
      O$.Calendar._updateDateRangesSelectedStyles(calendar, cell);
    }

    if (calendar._showFooter) {
      O$.Calendar._updateToday(calendar);
    }
    if (calendar._onDateChange) {
      calendar._onDateChange();
    }
    O$.Calendar._notifyDateChangeListeners(calendar);
  },

  _updateDateRangesSelectedStyles: function(calendar, cell) {
    var useTempStyle = cell._className !== undefined;
    var dateRanges = calendar._dateRanges;
    if (!dateRanges)
      return;

    var foundInRange = false;
    var ranges = dateRanges.getDateRanges();
    for (var i = 0, count = ranges.length; i < count; i ++) {
      var range = ranges[i];
      if (range.isDateInRange(cell._date)) {
        foundInRange = true;
        if (!dateRanges._disableIncluded) {
          if (cell._isSelected) {
            if (range._selectedDayStyleClassName) {
              if (useTempStyle) {
                cell._className = cell._className.replace(calendar._selectedDayClass, "");
                cell._className += " " + range._selectedDayStyleClassName;
              } else {
                cell.className = cell.className.replace(calendar._selectedDayClass, "");
                cell.className += " " + range._selectedDayStyleClassName;
              }
            }
            if (range._rolloverSelectedDayStyleClassName) {
              cell._rolloverClassName = cell._rolloverClassName.replace(calendar._rolloverSelectedDayClass, "");
              cell._rolloverClassName += " " + range._rolloverSelectedDayStyleClassName;
            }
          }
        }
      }
    }
    if (foundInRange && !dateRanges._disableIncluded) {
      if (cell._isSelected) {
        if (dateRanges._selectedDayClass) {
          if (useTempStyle) {
            cell._className = cell._className.replace(calendar._selectedDayClass, "");
            cell._className += " " + dateRanges._selectedDayClass;
          } else {
            cell.className = cell.className.replace(calendar._selectedDayClass, "");
            cell.className += " " + dateRanges._selectedDayClass;
          }
        }
        if (dateRanges._rolloverSelectedDayClass) {
          cell._rolloverClassName = cell._rolloverClassName.replace(calendar._rolloverSelectedDayClass, "");
          cell._rolloverClassName += " " + dateRanges._rolloverSelectedDayClass;
        }

        if (cell._isToday && dateRanges._rolloverSelectedDayClass) {
          cell._rolloverClassName = cell._rolloverClassName.replace(calendar._rolloverTodayClass, "");
        }
      } else {
        if (cell._isToday) {
          cell._rolloverClassName = cell._rolloverClassName.replace(calendar._rolloverTodayClass, "");
        }
      }
    }

  },

  _updateDateHolder: function(calendar, date) {
    var valueDate = calendar._valueDateHolder;
    if (date) {
      var dtf = O$.getDateTimeFormatObject(calendar._localeStr);
      if (!dtf) return;
      valueDate.value = dtf.format(date, "dd/MM/yyyy");
    }
  },

  _adjustMonthAndYearSelectorWidth: function(calendar) {
    if (calendar._monthSelector._drop.clientWidth) {
      calendar._monthSelector.style.width = calendar._monthSelector._drop.clientWidth - 3;
      calendar._yearSelector.style.width = calendar._yearSelector._drop.clientWidth - 3;
    } else {
      setTimeout(function() {
        O$.Calendar._adjustMonthAndYearSelectorWidth(calendar);
      }, 40);
    }
  }
};
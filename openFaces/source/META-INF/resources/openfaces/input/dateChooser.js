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
O$.DateChooser = {
  _init: function(dateChooserId,
                  dateFormat,
                  calendarDate,
                  localeStr,
                  valueChangeHandler) {
    var dc = O$(dateChooserId);
    O$.extend(dc, {
      _isDateChooser: true,
      _calendar: O$(dateChooserId + "--popup--calendar"),

      _dateFormat: dateFormat,
      _localeStr: localeStr,

      _valueChangeHandler: valueChangeHandler
    });

    dc._field._oldValueHolder = dc._initialText;
    dc._field.onchange = function(e) {
      if (!e)
        e = event;
      e.cancelBubble = true;
      // works in IE only
      this._oldValueHolder = null;
      dc.validateInputAndUpdateCalendar();
    };

    if (valueChangeHandler) {
      dc._prevOnchange = dc.onchange;
      eval("dc._invokeChangeHandler = function(event) {" + valueChangeHandler + "}");
      dc.onchange = function(e) {
        if (e && e.target && e.target == dc._field) // works everywhere except IE
          return;
        if (this._prevOnchange)
          this._prevOnchange();
        dc._invokeChangeHandler(e);
      };
    }

    // Sometimes date chooser may be placed into container with "display: none;".
    // We have to catch the moment when container becomes to displayable one and set the date chooser width
    var timeoutID = setTimeout(function() {
      O$.DateChooser._checkCalendarWidth(dc);
    }, 100);

    if (!O$._dateChooserTimerID) {
      O$._dateChooserTimerID = timeoutID;
    }

    dc._showHidePopup = function () {
      O$.DateChooser._changeCalendarVisibility(dc);
    };

    dc._dateChangeListener = function(date) {
      if (dc._isSetDateFromInput) return;
      var popup = dc._popup;
      if (O$.validateById != undefined) {
        if (dc._of_messages) {
          dc._of_messages = undefined;
          O$.updateClientMessages(dc.id);
          O$.updateClientMessagesPosition();
        }
      }
      if (dc._keyDownEvent) {
        var e = dc._keyDownEvent;
        if (e.keyCode == 33 || e.keyCode == 34 || e.keyCode == 38 || e.keyCode == 40 || e.keyCode == 37
                || e.keyCode == 39) {
          dc._keyDownEvent = null;
          return;
        }
        dc._keyDownEvent = null;
      }
      popup.hide();

      try {
        dc._field.focus();
      } catch(e) {
        //IE
      }
      O$.DateChooser._updateDCField(dc, date != null ? new Date(date) : null);
    };

    var eventName = (O$.isSafariOnMac() || O$.isOpera() || O$.isMozillaFF()) ? "onkeypress" : "onkeydown";
    dc._prevKeyHandler_DC = dc[eventName];
    dc[eventName] = function(evt) {
      var e = evt ? evt : window.event;
      dc._keyDownEvent = e;
      switch (e.keyCode) {
        case 40: // down
          if (!popup.isVisible()) {
            dc._showHidePopup();
          }
      }
      if (dc._prevKeyHandler_DC)
        dc._prevKeyHandler_DC(evt);
    };

    var cal = dc._calendar;
    cal._prevKeyHandler_DC = cal[eventName];
    cal[eventName] = function(evt) {
      var e = evt ? evt : window.event;
      dc._keyDownEvent = e;
      switch (e.keyCode) {
        case 27: // escape
          dc._popup.hide();
          dc._field.focus();
          dc._keyDownEvent = null;
          return;
        case 13: // enter
          dc._dateChangeListener(O$.Calendar._getSelectedDate(cal.id));
          return false;
      }
      cal._prevKeyHandler_DC(evt);
    };

    cal._addDateChangeListener(dc._dateChangeListener);

    dc.validateInputAndUpdateCalendar = function() {
      var date;
      if (O$.validateById != undefined) {
        var converters = O$.getValidators(dc);
        for (var i = 0; i < converters.length; i ++) {
          var v = converters[i];
          if (v instanceof O$._DateTimeConverterValidator) {
            if (dc._of_messages) {
              var newMessages = [];
              dc._of_messages.forEach(function (msg) {
                if (!msg.validator || !(msg.validator instanceof O$._DateTimeConverterValidator))
                  newMessages.push(msg);
              });
              dc._of_messages = newMessages;
            }

            var result = v.validate(dc);

            if (result && result instanceof Date) {
              date = result;
            }
            if (!result) {
              // show invalid format message, but prevent hiding other messages such as required that might already be visible
              dc.valid = result;
            }
            O$.updateClientMessages(dc.id);
            O$.updateClientMessagesPosition();
            break;
          }
        }
      }
      /*
       var dtf = O$.getDateTimeFormatObject(dc._localeStr);
       if (!dtf) return;
       var date = dtf.parse(field.value, dc._dateFormat, valueHolder.value);
       */

      dc._isSetDateFromInput = true;
      if (O$.trim(dc._field.value).length > 0)
        O$.Calendar._setSelectedDate(dc._calendar.id, date);
      else
        O$.Calendar._noneClick(dc._calendar);
      dc._isSetDateFromInput = false;
      if (dc._field._oldValueHolder != dc._field.value) {
        if (!dc._updatingField && dc.onchange) {
          O$.sendEvent(dc, "change");
        }
        dc._field._oldValueHolder = dc._field.value;
      }
    };

    // Hide popup default border for date chooser
    var popup = dc._popup;
    popup.style.borderWidth = "0px";

    dc._clientValueFunctionExists = true;
    dc._clientValueFunction = function () {
      return dc._field.value;
    };

    popup._addVisibilityChangeListener(function () {
      var lastHoveredCell = dc._calendar._lastHoveredCell;
      if (lastHoveredCell)
        lastHoveredCell.onmouseout();
    });

    // Related to JSFC-2042. Adjust date for calendar inner component.
    if (calendarDate) {
      cal._valueDateHolder.value = calendarDate;
      var dtf = O$.getDateTimeFormatObject(cal._localeStr);
      if (!dtf) return;
      var sDate = dtf.parse(calendarDate, "dd/MM/yyyy");
      cal._valueHolder.value = sDate.getTime();
      O$.Calendar._updateCalendar(cal, sDate);
    }

    dc.getSelectedDate = function () {
      return O$.DateChooser._getDate(dc.id);
    };

    dc.setSelectedDate = function (date) {
      O$.DateChooser._setDate(dc.id, date);
    };
  },

  _changeCalendarVisibility: function(dc) {
    var popup = dc._popup;
    var calendar = dc._calendar;
    if (popup.isVisible()) {
      popup.hide();
      dc._field.focus();
    } else {
      dc.validateInputAndUpdateCalendar();
      popup._prepareForRearrangementBeforeShowing();
      O$.DropDown._initPopup(dc, calendar);
      O$.correctElementZIndex(popup, dc);
      popup.show();
      dc._calendar.focus();
    }
    if (O$.isOpera()) {
      var body = document.getElementsByTagName("body")[0];
      body.style.visibility = "hidden";
      body.style.visibility = "visible";
    }
  },

  _updateDCField: function(dc, date) {
    var field = dc._field;
    if (date) {
      var dtf = O$.getDateTimeFormatObject(dc._localeStr);
      if (!dtf) return;
      var sDate = dtf.format(date, dc._dateFormat);
      dc._updatingField = true;
      if (field.value != sDate) {
        try {
          field.value = sDate;
          field._oldValueHolder = field.value;
        } finally {
          dc._updatingField = false;
        }
        if (dc.onchange)
          O$.sendEvent(dc, "change");
      }
    } else {
      if (field.value != "") {
        dc._updatingField = true;
        try {
          field.value = "";
          field._oldValueHolder = field.value;
        } finally {
          dc._updatingField = false;
        }
        if (dc.onchange)
          O$.sendEvent(dc, "change");
      }

    }
    if (O$.isOpera()) {
      var body = document.getElementsByTagName("body")[0];
      body.style.visibility = "hidden";
      body.style.visibility = "visible";
    }
  },

  _getDate: function(dcId) {
    var dc = O$(dcId);
    return O$.Calendar._getSelectedDate(dc._calendar.id);
  },

  _setDate: function(dcId, date) {
    var dc = O$(dcId);
    O$.Calendar._setSelectedDate(dc._calendar.id, date);
  },

  _checkCalendarWidth: function(dc) {
    var isElementInDocument = O$.isElementPresentInDocument(dc);
    if (!isElementInDocument) {
      clearTimeout(O$._dateChooserTimerID);
      return;
    }
    var cal = dc._calendar;
    if (cal.offsetWidth != 0) {
      dc.style.width = cal.offsetWidth + "px";
      dc.style.visibility = "visible";
      O$.repaintAreaForOpera(dc, true);
    } else {
      setTimeout(function() {
        O$.DateChooser._checkCalendarWidth(dc);
      }, 100);
    }
  }

};
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
O$._initDateChooser = function(dateChooserId,
                               dateFormat,
                               calendarDate,
                               localeStr,
                               valueChangeHandler) {
  var dc = O$(dateChooserId);
  dc._isDateChooser = true;
  dc._calendar = O$(dateChooserId + "--popup--calendar");
  //  dc._valueHolderId = dateChooserId + "_valueHolder";

  dc._dateFormat = dateFormat;
  dc._localeStr = localeStr;

  dc._valueChangeHandler = valueChangeHandler;

  var cal = dc._calendar;
  //  var valueHolder = O$(dc._valueHolderId);

  var field = dc._field;

  field._oldValueHolder = dc._initialText;

  field.onchange = function(e) {
    if (!e)
      e = event;
    e.cancelBubble = true;
    // works in IE only
    field._oldValueHolder = null;
    dc.validateInputAndUpdateCalendar();
  };

  if (valueChangeHandler) {
    dc._prevOnchange = dc.onchange;
    eval("dc._invokeChangeHandler = function(event) {" + valueChangeHandler + "}");
    dc.onchange = function(e) {
      if (e && e.target && e.target == field) // works everywhere except IE
        return;
      if (this._prevOnchange)
        this._prevOnchange();
      dc._invokeChangeHandler(e);
    }
  }

  // Sometimes date chooser may be placed into container with "display: none;".
  // We have to catch the moment when container becomes to displayable one and set the date chooser width
  var timeoutID = setTimeout(function() {
    O$._dateChooser_checkCalendarWidth(dc);
  }, 100);

  if (!O$._dateChooserTimerID) {
    O$._dateChooserTimerID = timeoutID;
  }
  //  O$._dateChooser_checkCalendarWidth (dc.id);

  dc._showHidePopup = function () {
    O$._dc_changeCalendarVisibility(dc);
  }

  dc._dateChangeListener = function(date) {
    if (dc._isSetDateFromInput) return;
    var popup = dc._popup;
    if (self.O$.validateById != undefined) {
      if (dc._of_messages) {
        dc._of_messages = undefined;
        O$.updateClientMessages();
        O$.updateClientMessagesPosition();
      }
    }
    if (dc._keyDownEvent)
    {
      var e = dc._keyDownEvent;
      if (e.keyCode == 33 || e.keyCode == 34 || e.keyCode == 38 || e.keyCode == 40 || e.keyCode == 37
              || e.keyCode == 39) {
        dc._keyDownEvent = null;
        return;
      }
      dc._keyDownEvent = null;
    }
    popup.hide();
    dc._field.focus();
    O$._dc_updateDCField(dc, date != null ? new Date(date) : null);
  }

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
  }

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
        dc._dateChangeListener(O$._getSelectedDate(cal.id));
        return false;
    }
    cal._prevKeyHandler_DC(evt)
  }

  cal._addDateChangeListener(dc._dateChangeListener);

  dc.validateInputAndUpdateCalendar = function() {
    //  var field = dc._field;
    //    var valueHolder = O$(dc._valueHolderId);
    /*
     var formatter = new DateChooserAjaxDateFormatter();
     formatter.validateAndParse(field.value);
     */
    var date;
    if (self.O$.validateById != undefined) {
      var converters = O$.getValidators(dc);
      for (var i = 0; i < converters.length; i ++) {
        var v = converters[i];
        if (v instanceof O$._DateTimeConverterValidator) {
          if (dc._of_messages) {
            dc._of_messages = undefined;
          }
          //          dc._isManualValidation = true;
          var result = v.validate(dc);
          //          dc._isManualValidation = false;
          //          if (result[0] == "true" || result[0] == true) {
          if (result && result instanceof Date) {
            date = result;
          }
          dc.valid = result;
          //            }
          //            O$.updateClientMessages();
          //            O$.updateClientMessagesPosition();
          //          } else {
          O$.updateClientMessages();
          O$.updateClientMessagesPosition();
          //          }
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
      O$._setSelectedDate(dc._calendar.id, date);
    else
      O$._noneClick(dc._calendar);
    dc._isSetDateFromInput = false;
    if (dc._field._oldValueHolder != dc._field.value) {
      if (!dc._updatingField && dc.onchange) {
        O$.sendEvent(dc, "change");
      }
      dc._field._oldValueHolder = dc._field.value;
    }
  }

  // Hide popup default border for date chooser
  var popup = dc._popup;
  popup.style.borderWidth = "0px";

  dc._clientValueFunctionExist = true;
  dc._clientValueFunction = function () {
    return dc._field.value;
    /*
     var valueHolder = O$(dc._valueHolderId);
     return valueHolder.value;
     */
  }

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
    O$._updateCalendar(cal, sDate);
  }

  dc.getSelectedDate = function () {
    return O$._getDateChooserDate(dc.id);
  }

  dc.setSelectedDate = function (date) {
    O$._setDateChooserDate(dc.id, date);
  }
}

O$._dc_changeCalendarVisibility = function(dc) {
  var popup = dc._popup;
  var calendar = dc._calendar;
  if (popup.isVisible()) {
    popup.hide();
    dc._field.focus();
  } else {
    dc.validateInputAndUpdateCalendar();
    popup._prepareForRearrangementBeforeShowing();
    O$._dropDown_initPopup(dc, calendar);
    O$.correctElementZIndex(popup, dc);
    popup.show();
    dc._calendar.focus();
  }
  if (O$.isOpera()) {
    var body = document.getElementsByTagName("body")[0];
    body.style.visibility = "hidden";
    body.style.visibility = "visible";
  }
}

O$._dc_updateDCField = function(dc, date) {
  //  var valueHolder = O$(dc._valueHolderId);
  var field = dc._field;
  //  var oldValue = valueHolder.value;
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
    //    valueHolder.value = date.getTime();
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
  //  if (valueHolder.value != oldValue && dc._onValueChange) {
  //    dc._onValueChange();
  //  }
  if (O$.isOpera()) {
    var body = document.getElementsByTagName("body")[0];
    body.style.visibility = "hidden";
    body.style.visibility = "visible";
  }
}

O$._getDateChooserDate = function(dcId) {
  var dc = O$(dcId);
  var date = O$._getSelectedDate(dc._calendar.id);
  return date;
}

O$._setDateChooserDate = function(dcId, date) {
  var dc = O$(dcId);
  O$._setSelectedDate(dc._calendar.id, date);
}

O$._dateChooser_checkCalendarWidth = function(dc) {
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
      O$._dateChooser_checkCalendarWidth(dc)
    }, 100);
  }
}


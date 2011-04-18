/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

O$.DaySwitcher = {
  _init: function(switcherId,
                  timeTableId,
                  day,
                  pattern,
                  locale,
                  stylingParams,
                  enabled,
                  upperPattern) {
    var dtf = O$.getDateTimeFormatObject(locale);
    var switcher = O$.initComponent(switcherId, {rollover: stylingParams.rolloverClass}, {
      _day: dtf.parse(day, "dd/MM/yyyy"),
      _timeTableId: timeTableId,
      _pattern: pattern,
      _locale: locale,
      _upperPattern: upperPattern
    });

    var textId = switcherId + "::text";
    switcher._text = O$(textId);
    if (switcher._text) {
      O$.initComponent(textId, {rollover: stylingParams.textRolloverClass});
    }

    if (enabled) {
      var previousButtonId = switcherId + "::previous_button";
      O$.initComponent(previousButtonId, {rollover: stylingParams.previousButtonRolloverClass});
      switcher._previousButton = O$(previousButtonId);
      var previousButton = switcher._previousButton;

      var nextButtonId = switcherId + "::next_button";
      O$.initComponent(nextButtonId, {rollover: stylingParams.nextButtonRolloverClass});
      switcher._nextButton = O$(nextButtonId);
      var nextButton = switcher._nextButton;

      previousButton.onmouseup = function () {
        O$.setStyleMappings(previousButton, {
          pressed: null
        });
      };

      nextButton.onmouseup = function () {
        O$.setStyleMappings(nextButton, {
          pressed: null
        });
      };

      previousButton.ondragstart =
      nextButton.ondragstart =
      previousButton.onselectstart =
      nextButton.onselectstart = function(e) {
        O$.breakEvent(e);
      };

      previousButton.ondblclick =
      nextButton.ondblclick = O$.repeatClickOnDblclick;

    }

    switcher._getTimeTable = function() {
      if (!switcher._timeTable) {
        switcher._timeTable = O$(switcher._timeTableId);
      }
      return switcher._timeTable;
    };

    switcher.getDay = function() {
      return switcher._day;
    };

    switcher.setDay = function(day) {
      if (O$._datesEqual(switcher._day, day))
        return;
      switcher._day = day;

      switcher._updateText();

      var timeTable = switcher._getTimeTable();
      if (!timeTable) {
        return;
      }

      switcher._timeTable.setDay(day);
    };

    switcher.previousPeriod = function() {
      var prevDay = O$.incDay(switcher._day, -this._getPeriodSize());
      switcher.setDay(prevDay);
    };

    switcher.nextPeriod = function() {
      var nextDay = O$.incDay(switcher._day, this._getPeriodSize());
      switcher.setDay(nextDay);
    };

    if (enabled) {
      previousButton.onmousedown = function () {
        O$.setStyleMappings(previousButton, {
          pressed: stylingParams.previousButtonPressedClass
        });
        switcher.previousPeriod();
      };

      nextButton.onmousedown = function () {
        O$.setStyleMappings(nextButton, {
          pressed: stylingParams.nextButtonPressedClass
        });
        switcher.nextPeriod();
      };

      O$.addLoadEvent(function () {
        var timeTable = switcher._getTimeTable();
        if (!timeTable) {
          return;
        }

        var _onPeriodChange = timeTable._onPeriodChange;
        timeTable._onPeriodChange = function(day) {
          if (_onPeriodChange) {
            _onPeriodChange(day);
          }
          switcher.setDay(day);
        };
      });

    }

    var upperTextId = switcherId + "::upper_text";
    switcher._upperText = O$(upperTextId);
    if (switcher._upperText) {
      O$.initComponent(upperTextId, {rollover: stylingParams.upperTextRolloverClass});
    }

    switcher.today = function() {
      var today = new Date();
      switcher.setDay(today);
    };

    switcher._updateText = function() {
      if (switcher._pattern) {
        var dtf = O$.getDateTimeFormatObject(switcher._locale);
        switcher._text.innerHTML = dtf.format(this._day, switcher._pattern);
      }

      if (switcher._upperPattern) {
        dtf = O$.getDateTimeFormatObject(switcher._locale);
        switcher._upperText.innerHTML = dtf.format(this._day, switcher._upperPattern);
      }
    };

    switcher._getPeriodSize = function() {
      return 1;
    }

  }
};
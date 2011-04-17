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

O$.WeekSwitcher = {
  _init: function(switcherId,
                  timeTableId,
                  firstDay,
                  pattern,
                  locale,
                  stylingParams,
                  enabled,
                  splitter) {
    var dtf = O$.getDateTimeFormatObject(locale);
    var switcher = O$.initComponent(switcherId, {rollover: stylingParams.rolloverClass}, {
      _firstDay: dtf.parse(firstDay, "dd/MM/yyyy"),
      _timeTableId: timeTableId,
      _pattern: pattern,
      _locale: locale,
      _splitter: splitter
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

    switcher.getFirstDay = function() {
      return switcher._firstDay;
    };

    switcher.setFirstDay = function(firstDay) {
      if (O$._datesEqual(switcher._firstDay, firstDay))
        return;
      switcher._firstDay = firstDay;

      if (switcher._pattern) {
        var dtf = O$.getDateTimeFormatObject(switcher._locale);
        var lastDay = O$.incDay(firstDay, 6);

        var text = dtf.format(firstDay, switcher._pattern)
            .concat(switcher._splitter)
            .concat(dtf.format(lastDay, switcher._pattern));
        if (!isChrome()) {
          switcher._text.innerHTML = text;
        } else {
          var cloned = switcher._text.cloneNode();
          var origin = switcher._text;
          cloned.innerHTML = text;
          switcher._text.parentNode.replaceChild(cloned, origin);
          switcher._text = cloned;
        }
      }

      var timeTable = switcher._getTimeTable();
      if (!timeTable) {
        return;
      }

      switcher._timeTable.setDay(firstDay)
    };

    isChrome = function() {
      return Boolean(window.chrome);
    };

    switcher.previousPeriod = function() {
      var prevDay = O$.incDay(switcher._firstDay, -7);
      switcher.setFirstDay(prevDay);
    };

    switcher.nextPeriod = function() {
      var nextDay = O$.incDay(switcher._firstDay, 7);
      switcher.setFirstDay(nextDay);
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

        var _onWeekChange = timeTable._onWeekChange;
        timeTable._onWeekChange = function(firstDay) {
          if (_onWeekChange) {
            _onWeekChange(firstDay);
          }
          switcher.setFirstDay(firstDay);
        };
      });

    }


  }
};
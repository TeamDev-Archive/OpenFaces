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
  _init: function(weekSwitcherId,
                  weekTableId,
                  firstDay,
                  pattern,
                  locale,
                  stylingParams,
                  enabled,
                  splitter) {
    var dtf = O$.getDateTimeFormatObject(locale);
    var weekSwitcher = O$.initComponent(weekSwitcherId, {rollover: stylingParams.rolloverClass}, {
      _firstDay: dtf.parse(firstDay, "dd/MM/yyyy"),
      _weekTableId: weekTableId,
      _pattern: pattern,
      _locale: locale,
      _splitter: splitter
    });

    var textId = weekSwitcherId + "::text";
    weekSwitcher._text = O$(textId);
    if (weekSwitcher._text) {
      O$.initComponent(textId, {rollover: stylingParams.textRolloverClass});
    }

    if (enabled) {
      var previousButtonId = weekSwitcherId + "::previous_button";
      O$.initComponent(previousButtonId, {rollover: stylingParams.previousButtonRolloverClass});
      weekSwitcher._previousButton = O$(previousButtonId);
      var previousButton = weekSwitcher._previousButton;

      var nextButtonId = weekSwitcherId + "::next_button";
      O$.initComponent(nextButtonId, {rollover: stylingParams.nextButtonRolloverClass});
      weekSwitcher._nextButton = O$(nextButtonId);
      var nextButton = weekSwitcher._nextButton;

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

    weekSwitcher._getWeekTable = function() {
      if (!weekSwitcher._weekTable) {
        weekSwitcher._weekTable = O$(weekSwitcher._weekTableId);
      }
      return weekSwitcher._weekTable;
    };

    weekSwitcher.getFirstDay = function() {
      return weekSwitcher._firstDay;
    };

    weekSwitcher.setFirstDay = function(firstDay) {
      if (O$._datesEqual(weekSwitcher._firstDay, firstDay))
        return;
      weekSwitcher._firstDay = firstDay;

      if (weekSwitcher._pattern) {
        var dtf = O$.getDateTimeFormatObject(weekSwitcher._locale);
        var lastDay = O$.incDay(firstDay, 6);

        var text = dtf.format(firstDay, weekSwitcher._pattern)
            .concat(weekSwitcher._splitter)
            .concat(dtf.format(lastDay, weekSwitcher._pattern));
        if (!isChrome()) {
          weekSwitcher._text.innerHTML = text;
        } else {
          var cloned = weekSwitcher._text.cloneNode();
          var origin = weekSwitcher._text;
          cloned.innerHTML = text;
          weekSwitcher._text.parentNode.replaceChild(cloned, origin);
          weekSwitcher._text = cloned;
        }
      }

      var weekTable = weekSwitcher._getWeekTable();
      if (!weekTable) {
        return;
      }

      weekSwitcher._weekTable.setDay(firstDay)
    };

    isChrome = function() {
      return Boolean(window.chrome);
    };

    weekSwitcher.previousWeek = function() {
      var prevDay = O$.incDay(weekSwitcher._firstDay, -7);
      weekSwitcher.setFirstDay(prevDay);
    };

    weekSwitcher.nextWeek = function() {
      var nextDay = O$.incDay(weekSwitcher._firstDay, 7);
      weekSwitcher.setFirstDay(nextDay);
    };

    if (enabled) {
      previousButton.onmousedown = function () {
        O$.setStyleMappings(previousButton, {
          pressed: stylingParams.previousButtonPressedClass
        });
        weekSwitcher.previousWeek();
      };

      nextButton.onmousedown = function () {
        O$.setStyleMappings(nextButton, {
          pressed: stylingParams.nextButtonPressedClass
        });
        weekSwitcher.nextWeek();
      };

      O$.addLoadEvent(function () {
        var weekTable = weekSwitcher._getWeekTable();
        if (!weekTable) {
          return;
        }

        var _onWeekChange = weekTable._onWeekChange;
        weekTable._onWeekChange = function(firstDay) {
          if (_onWeekChange) {
            _onWeekChange(firstDay);
          }
          weekSwitcher.setFirstDay(firstDay);
        };
      });

    }


  }
};
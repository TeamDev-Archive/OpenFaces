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

O$.TimePeriodSwitcher = {
  _init: function(switcherId,
                  timeTableId,
                  day,
                  pattern,
                  locale,
                  stylingParams,
                  enabled) {
    var dtf = O$.getDateTimeFormatObject(locale);
    var textId = switcherId + "::text";
    var switcher = O$.initComponent(switcherId, {rollover: stylingParams.rolloverClass}, {
      _day: dtf.parse(day, "dd/MM/yyyy"),
      _timeTableId: timeTableId,
      _pattern: pattern,
      _locale: locale,
      _text: O$(textId),

      _getTimeTable: function() {
        if (!this._timeTable) {
          this._timeTable = O$(this._timeTableId);
        }
        return this._timeTable;
      },

      getDay: function() {
        return switcher._day;
      },

      setDay: function(day) {
        if (O$._datesEqual(this._day, day))
          return;
        this._day = day;

        this._updateText();

        var timeTable = this._getTimeTable();
        if (!timeTable) {
          return;
        }

        this._timeTable.setDay(day);
      },

      previousPeriod: function() {
        var prevDay = O$.incDay(this._day, -this._getPeriodSize());
        this.setDay(prevDay);
      },

      nextPeriod: function() {
        var nextDay = O$.incDay(this._day, this._getPeriodSize());
        this.setDay(nextDay);
      }

    });

    if (switcher._text) {
      O$.initComponent(textId, {rollover: stylingParams.textRolloverClass});
    }

    if (enabled) {
      var previousButton = switcher._previousButton = O$.initComponent(switcherId + "::previous_button", {
        rollover: stylingParams.previousButtonRolloverClass,
        pressed: stylingParams.previousButtonPressedClass
      }, {
        onmousedown: function () {
          switcher.previousPeriod();
        }

      });

      var nextButton = switcher._nextButton = O$.initComponent(switcherId + "::next_button", {
        rollover: stylingParams.nextButtonRolloverClass,
        pressed: stylingParams.nextButtonPressedClass
      }, {
        onmousedown: function () {
          switcher.nextPeriod();
        }
      });

      previousButton.ondragstart = nextButton.ondragstart = previousButton.onselectstart = nextButton.onselectstart =
              function(e) {
                O$.breakEvent(e);
              };
      previousButton.ondblclick = nextButton.ondblclick = function(e) {
        O$.repeatClickOnDblclick(e);
        O$.breakEvent(e)
      };

      O$.addLoadEvent(function () {
        var timeTable = switcher._getTimeTable();
        if (!timeTable) {
          return;
        }

        var _onPeriodChange = timeTable._onPeriodChange;
        timeTable._onPeriodChange = function(day) {
          if (_onPeriodChange) {
            _onPeriodChange.apply(this, arguments);
          }
          switcher.setDay(day);
        };
      });

    }
  }
};

O$.WeekSwitcher = {
  _init: function(switcherId,
                  timeTableId,
                  day,
                  pattern,
                  locale,
                  stylingParams,
                  enabled,
                  splitter) {
    O$.TimePeriodSwitcher._init.apply(null, arguments);

    var switcher = O$.initComponent(switcherId, null, {
      _splitter: splitter,

      _updateText: function() {
        if (this._pattern) {
          var dtf = O$.getDateTimeFormatObject(switcher._locale);
          var lastDay = O$.incDay(this._day, 6);

          this._text.innerHTML = dtf.format(this._day, this._pattern)
              .concat(this._splitter)
              .concat(dtf.format(lastDay, this._pattern));
        }
      },

      _getPeriodSize: function() {
        return 7;
      }

    });

  }
};

O$.DaySwitcher = {
  _init: function(switcherId,
                  timeTableId,
                  day,
                  pattern,
                  locale,
                  stylingParams,
                  enabled,
                  upperPattern) {
    O$.TimePeriodSwitcher._init.apply(null, arguments);

    var upperTextId = switcherId + "::upper_text";

    var switcher = O$.initComponent(switcherId, null, {
      _upperPattern: upperPattern,
      _upperText: O$(upperTextId),

      today: function() {
        var today = new Date();
        this.setDay(today);
      },

      _updateText: function() {
        if (this._pattern) {
          var dtf = O$.getDateTimeFormatObject(this._locale);
          this._text.innerHTML = dtf.format(this._day, this._pattern);
        }

        if (this._upperPattern) {
          dtf = O$.getDateTimeFormatObject(this._locale);
          this._upperText.innerHTML = dtf.format(this._day, this._upperPattern);
        }
      },

      _getPeriodSize: function() {
        return 1;
      }

    });

    if (switcher._upperText) {
      O$.initComponent(upperTextId, {rollover: stylingParams.upperTextRolloverClass});
    }

  }
};
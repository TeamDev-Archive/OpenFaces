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
    var switcher = O$.initComponent(switcherId, {rollover: stylingParams.rolloverClass}, {
      _day: dtf.parse(day, "dd/MM/yyyy"),
      _timeTableId: timeTableId,
      _pattern: pattern,
      _locale: locale,
      _text: O$(switcherId + "::text"),

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

      _previousPeriod: function(date) {
        return O$.incDay(date, -this._getPeriodSize())
      },

      _nextPeriod: function(date) {
        return O$.incDay(date, this._getPeriodSize())
      },

      previousPeriod: function() {
        var prevDay = this._previousPeriod(this._day);
        this.setDay(prevDay);
      },

      nextPeriod: function() {
        var nextDay = this._nextPeriod(this._day);
        this.setDay(nextDay);
      }

    });

    if (switcher._text) {
      O$.initComponent(switcher._text.id, {rollover: stylingParams.textRolloverClass});
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

      previousButton.ondragstart = nextButton.ondragstart =
              previousButton.onselectstart = nextButton.onselectstart = O$.breakEvent;
      previousButton.ondblclick = nextButton.ondblclick = function(e) {
        O$.repeatClickOnDblclick(e);
        O$.breakEvent(e)
      };

      O$.addLoadEvent(function () {
        var timeTable = switcher._getTimeTable();
        if (!timeTable) {
          return;
        }

        var prevOnperiodchange = timeTable.onperiodchange;
        timeTable.onperiodchange = function(day) {
          if (prevOnperiodchange) {
            prevOnperiodchange.apply(this, arguments);
          }
          switcher.setDay(day);
        };
      });

    }
  }
};

O$.MonthSwitcher = {
  _init: function(switcherId,
                  timeTableId,
                  day,
                  pattern,
                  locale,
                  stylingParams,
                  enabled) {
    O$.TimePeriodSwitcher._init.apply(null, arguments);

    var switcher = O$.initComponent(switcherId, null, {
      _updateText: function() {
        if (this._pattern) {
          var dtf = O$.getDateTimeFormatObject(switcher._locale);
          this._text.innerHTML = dtf.format(this._day, this._pattern);
        }
      },

      _previousPeriod: function(date) {
        var result = O$.cloneDate(date);
        result.setMonth(result.getMonth() - 1);
        return result;
      },

      _nextPeriod: function(date) {
        var result = O$.cloneDate(date);
        result.setMonth(result.getMonth() + 1);
        return result;
      }

    });

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

    var switcher = O$.initComponent(switcherId, null, {
      _upperPattern: upperPattern,
      _upperText: O$(switcherId + "::upper_text"),

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

    if (switcher._text) {
      var textCell = switcher._text.parentNode;
      var popup = jQuery(textCell).find(".o_daySwitcherPopup")[0];
      if (popup) {
        O$.appendClassNames(textCell, ["o_daySwitcherClickableCell"]);
        var calendar = jQuery(popup).find(".o_calendar")[0];
        textCell.onclick = function(e) {
          calendar.setSelectedDate(switcher.getDay());
          popup.show();
          O$.correctElementZIndex(popup, switcher._getTimeTable());
        };
        calendar.onchange = function() {
          var newDate = this.getSelectedDate();
          if (!newDate) return;
          switcher.setDay(newDate);
          popup.hide();
        }
      }
    }


    if (switcher._upperText) {
      O$.initComponent(switcher._upperText.id, {rollover: stylingParams.upperTextRolloverClass});
    }

  }
};
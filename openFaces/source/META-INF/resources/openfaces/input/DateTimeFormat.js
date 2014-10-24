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

//todo: only date parsing is supported
O$.DateTimeFormat = function(month, shortMonth, weekdays, shortWeekdays) {
  this.PATTERN_CHARS = "GyMdkHmsSEDFwWahKzZ";
  this.CURRENTLY_SUPPORTED_PATTERN_CHARS_FORMAT_ONLY = "yMdEHhmsSa";
  this.CURRENTLY_SUPPORTED_PATTERN_CHARS = "yMdE";
  //todo: ideally, all chars in PATTERN_CHARS should be supported for full featured formatting

  this.MONTHS = month;
  this.SHORT_MONTHS = shortMonth;
  this.WEEKDAYS = weekdays;
  this.SHORT_WEEKDAYS = shortWeekdays;
};

O$.extend(O$.DateTimeFormat.prototype, {
  parse: function(val, format, currentValue) {
    if (val != null && val.length == 0) return null;
    var date;
    var dateComplete = false;
    var month;
    var monthComplete = false;
    var year;
    var yearComplete = false;
    var sDate = val;

    var oldDate = new Date();
    if (currentValue && currentValue != "") {
      oldDate.setTime(currentValue);
    }

    //1. transform '[]' parts to [] parts
    var tempFormat = format;
    var re = /'(.+?)'/;
    while (re.test(tempFormat)) {
      tempFormat = tempFormat.replace(re, RegExp.$1);
    }
    //2. try to find date by pattern

    var foundMatches = [];
    var iterator = 0;
    for (var charIndex = 0; charIndex < this.CURRENTLY_SUPPORTED_PATTERN_CHARS.length; charIndex++) {
      var letter = this.CURRENTLY_SUPPORTED_PATTERN_CHARS.charAt(charIndex);
      var regexp = new RegExp('(' + letter + '+)');
      while (regexp.test(tempFormat)) {
        foundMatches[iterator] = RegExp.$1;
        tempFormat = tempFormat.replace(foundMatches[iterator], "[" + iterator + "]");
        iterator++;
      }
    }

    var regexp = new RegExp("\\[(.+?)\\]");
    var matchCounter = 0;
    while (matchCounter < foundMatches.length) {
      var match = regexp.exec(tempFormat);
      if (match) {
        var prefix = tempFormat.substring(0, match.index);
        if (prefix) {
          var prefLen = prefix.length;
          var prefix_sDate = sDate.substring(0, match.index);
          if (prefix.toLowerCase() != prefix_sDate.toLowerCase()) {
            throw "The entered date (" + val + ") is incorrectly formatted. It must have the following format: " + format;
          }
          tempFormat = tempFormat.substring(prefLen);
          sDate = sDate.substring(prefLen);
        }
        var fmIndex = match[1];
        var pat = foundMatches[fmIndex];
        if (pat.charAt(0) == 'y') {
          if (pat.length < 3) {
            var currentYear = new Date().getFullYear();

            var curYearPref = currentYear.toString().substring(0, 2);
            var cys = parseInt(currentYear.toString().substring(2));
            var yearPart = sDate.substring(0, 2);
            try {
              var iyearPart = parseInt(yearPart);
              var absValue = Math.abs(cys - iyearPart);
              if (absValue > 20) {
                currentYear = currentYear - iyearPart;
                curYearPref = currentYear.toString().substring(0, 2);
              }
            } catch (e) {
              throw "The entered date (" + val + ") is incorrectly formatted. It must have the following format: " + format;
            }
            year = curYearPref + yearPart;
            sDate = sDate.substring(2);
          } else {
            year = sDate.substring(0, 4);
            sDate = sDate.substring(4);
          }
          yearComplete = true;
        } else if (pat.charAt(0) == 'M') {
          if (pat.length < 3) {
            month = sDate.substring(0, 2);
            sDate = sDate.substring(month.length);
            month = month - 1;
            monthComplete = true;
          } else if (pat.length == 3) {
            for (var shortMonthIndex = 0; shortMonthIndex < this.SHORT_MONTHS.length; shortMonthIndex++) {
              var _m = this.unescapeHtml(this.SHORT_MONTHS[shortMonthIndex]);
              var _sDate_month = sDate.substring(0, _m.length);
              if (_sDate_month.toLowerCase() == _m.toLowerCase()) {
                sDate = sDate.substring(_sDate_month.length);
                month = shortMonthIndex;
                monthComplete = true;
                break;
              } else {
                monthComplete = false;
              }
            }
          } else if (pat.length > 3) {
            for (var monthIndex = 0; monthIndex < this.MONTHS.length; monthIndex++) {
              var _m = this.unescapeHtml(this.MONTHS[monthIndex]);
              var _sDate_month = sDate.substring(0, _m.length);
              if (_sDate_month.toLowerCase() == _m.toLowerCase()) {
                sDate = sDate.substring(_sDate_month.length);
                month = monthIndex;
                monthComplete = true;
                break;
              } else {
                monthComplete = false;
              }
            }
          }
        } else if (pat.charAt(0) == "d") {
          var secondChar = sDate.length >= 2 ? sDate.substring(1, 2) : null;
          var secondCharIsADigit = secondChar && secondChar >= "0" && secondChar <= "9";
          var dateCharCount = secondCharIsADigit ? 2 : 1;
          date = sDate.substring(0, dateCharCount);
          sDate = sDate.substring(dateCharCount);
          dateComplete = true;
        } else {
          if (pat.charAt(0) == "E") {
            var found = false;
            if (pat.length < 4) {
              for (var shortWeekdayIndex = 0; shortWeekdayIndex < this.SHORT_WEEKDAYS.length; shortWeekdayIndex++) {
                var shortWeekDay = this.unescapeHtml(this.SHORT_WEEKDAYS[shortWeekdayIndex]);
                if (sDate.toLowerCase().indexOf(shortWeekDay.toLowerCase()) > -1) {
                  sDate = sDate.substring(shortWeekDay.length);
                  found = true;
                  break;
                }
              }
            } else {
              for (var weekdayIndex = 0; weekdayIndex < this.WEEKDAYS.length; weekdayIndex++) {
                var weekDay = this.unescapeHtml(this.WEEKDAYS[weekdayIndex]);
                if (sDate.toLowerCase().indexOf(weekDay.toLowerCase()) > -1) {
                  sDate = sDate.substring(weekDay.length);
                  found = true;
                  break;
                }
              }
            }
            if (!found) {
              throw "The entered date (" + val + ") is incorrectly formatted. It must have the following format: " + format;
            }
          }
        }
        tempFormat = tempFormat.substring(match[0].length);
      } else {
        throw "The entered date (" + val + ") is incorrectly formatted. It must have the following format: " + format;
      }
      matchCounter++;
    }

    var parsedDate = new Date();

    if (yearComplete) {
      parsedDate.setFullYear(year, 0, 1);
    } else {
      parsedDate.setFullYear(oldDate.getFullYear(), 0, 1);
    }
    if (monthComplete) {
      parsedDate.setMonth(month);
    } else {
      parsedDate.setMonth(oldDate.getMonth());
    }
    if (dateComplete) {
      parsedDate.setDate(date);
    } else {
      parsedDate.setDate(oldDate.getDate());
    }
    return parsedDate;
  },

  format: function(dDate, format) {
    var portions = [];
    var currentPortion = "";
    for (var charIndex = 0; charIndex < format.length; charIndex++) {
      var ch = format.charAt(charIndex);
      if (ch == "\'") {
        portions.push(currentPortion);
        currentPortion = "";
      } else {
        currentPortion += ch;
      }
    }
    portions.push(currentPortion);

    var result = "";
    var formattedPortion = true;
    for (var i = 0; i < portions.length; i++, formattedPortion = !formattedPortion) {
      var portion = portions[i];
      if (formattedPortion) {
        var formattedText = this.format2(dDate, portion);
        result += formattedText;
      } else {
        result += portion;
      }
    }
    return result;
  },

  format2: function(dDate, format) {
    if (!dDate)
      return "";
    var day = dDate.getDay();
    var date = dDate.getDate();
    var month = dDate.getMonth();
    var year = dDate.getFullYear();
    var hours = dDate.getHours();
    var minutes = dDate.getMinutes();
    var seconds = dDate.getSeconds();
    var millis = dDate.getMilliseconds();

    var foundMatches = [];
    var iterator = 0;
    for (var charIndex = 0; charIndex < this.CURRENTLY_SUPPORTED_PATTERN_CHARS_FORMAT_ONLY.length; charIndex++) {
      var letter = this.CURRENTLY_SUPPORTED_PATTERN_CHARS_FORMAT_ONLY.charAt(charIndex);
      var regexp = new RegExp("(" + letter + "+)");
      while (regexp.test(format)) {
        foundMatches[iterator] = RegExp.$1;
        format = format.replace(foundMatches[iterator], "[" + iterator + "]");
        iterator++;
      }
    }

    for (var matchIndex = 0; matchIndex < foundMatches.length; matchIndex++) {
      var match = foundMatches[matchIndex];
      var re = "\\[" + matchIndex + "\\]";
      var toReplace = new RegExp(re, "g");
      switch (match.charAt(0)) {
        case "y":
          if (match.length < 3) {
            format = format.replace(toReplace, year.toString().substring(2));
          } else {
            format = format.replace(toReplace, year.toString());
          }
          break;
        case "M":
          if (match.length < 3) {
            var mn = month + 1;
            if (mn.toString().length == 1) {
              format = format.replace(toReplace, "0" + mn);
            } else {
              format = format.replace(toReplace, mn);
            }
          } else if (match.length == 3) {
            format = format.replace(toReplace, this.SHORT_MONTHS[month]);
          } else {
            format = format.replace(toReplace, this.MONTHS[month]);
          }
          break;
        case "d":
          if (match.length > 1 && date.toString().length == 1) {
            format = format.replace(toReplace, "0" + date);
          } else {
            format = format.replace(toReplace, date);
          }
          break;
        case "E":
          if (match.length < 4) {
            format = format.replace(toReplace, this.SHORT_WEEKDAYS[day]);
          } else {
            format = format.replace(toReplace, this.WEEKDAYS[day]);
          }
          break;
        case "H":
          if (match.length > 1 && hours < 10)
            hours = "0" + hours;
          format = format.replace(toReplace, hours);
          break;
        case "h":
          var smallHours = hours % 12;
          if (smallHours == 0)
            smallHours = 12;
          if (match.length > 1 && smallHours < 10)
            smallHours = "0" + smallHours;
          format = format.replace(toReplace, smallHours);
          break;
        case "m":
          if (match.length > 1 && minutes < 10)
            minutes = "0" + minutes;
          format = format.replace(toReplace, minutes);
          break;
        case "a":
          var ampmStr = hours < 12 ? "AM" : "PM";
          format = format.replace(toReplace, ampmStr);
          break;
        case "s":
          if (match.length > 1 && seconds < 10)
            seconds = "0" + seconds;
          format = format.replace(toReplace, seconds);
          break;
        case "S":
          var millisStr = millis;
          if (match.length == 2 && millis < 10)
            millisStr = "0" + millis;
          if (match.length == 3) {
            if (millis < 100) millisStr = "0" + millis;
            if (millis < 10) millisStr = "0" + millis;
          }
          format = format.replace(toReplace, millisStr);

        //todo: other formattings
      }
    }
    //transform html special character codes to ordinal characters
    format = this.unescapeHtml(format);

    re = /'(.+?)'/;
    while (re.test(format)) {
      format = format.replace(re, RegExp.$1);
    }
    return format;
  },

  unescapeHtml: function(val) {
    var re = /\&\#(\d+)\;/;
    while (re.test(val)) {
      val = val.replace(re, String.fromCharCode(RegExp.$1));
    }
    return val;
  },

  getMonths: function() {
    return this.MONTHS;
  },

  getShortMonths: function () {
    return this.SHORT_MONTHS;
  },

  getWeekdays: function() {
    return this.WEEKDAYS;
  },

  getShortWeekdays: function() {
    return this.SHORT_WEEKDAYS;
  }
});

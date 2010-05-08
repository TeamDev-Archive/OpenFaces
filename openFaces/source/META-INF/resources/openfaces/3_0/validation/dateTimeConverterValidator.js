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

O$._DateTimeConverterValidator = function(summary, detail, pattern, locale) {
  this.conversionSummary = summary;
  this.conversionDetail = detail;
  this.type = "converter";
  this.locale = locale;
  this.pattern = pattern;
};

O$._DateTimeConverterValidator.prototype.validate = function(input) {
  var value = O$.trim(O$.getValue(input));
  if (O$.notEmpty(value)) {
    if (this.pattern.length == 0)
      return new Array(true, null);
    if (!O$._isCorrectDate(value, this.pattern, this.locale)) {
      O$.addMessage(input, this.conversionSummary, this.conversionDetail, null, this);
      return false;
    }
    var date;
    var time = O$._getDateFromFormat(value, this.pattern, this.locale);
    if (time != 0) {
      date = new Date(time);
    }
    return date;
  }
  return true;
};

O$._isCorrectDate = function(value, pattern, locale) {
  return O$._isDate(value, pattern, locale);
};


O$.MONTH_NAMES = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
O$.DAY_NAMES = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

// ------------------------------------------------------------------
// O$._isDate ( date_string, format_string )
// Returns true if date string matches format of format string and
// is a valid date. Else returns false.
// It is recommended that you trim whitespace around the value before
// passing it to this function, as whitespace is NOT ignored!
// ------------------------------------------------------------------
O$._isDate = function(val, format, locale) {
  var date = O$._getDateFromFormat(val, format, locale);
  return date != 0;

};

// ------------------------------------------------------------------
// Utility functions for parsing in getDateFromFormat()
// ------------------------------------------------------------------
O$._isInteger = function(val) {
  var digits = "1234567890";
  for (var i = 0; i < val.length; i++) {
    if (digits.indexOf(val.charAt(i)) == -1) {
      return false;
    }
  }
  return true;
};
O$._getInt = function(str, i, minlength, maxlength) {
  for (var x = maxlength; x >= minlength; x--) {
    var token = str.substring(i, i + x);
    if (token.length < minlength) {
      return null;
    }
    if (O$._isInteger(token)) {
      return token;
    }
  }
  return null;
};

// ------------------------------------------------------------------
// O$._getDateFromFormat( date_string , format_string )
//
// This function takes a date string and a format string. It matches
// If the date string matches the format string, it returns the
// getTime() of the date. If it does not match, it returns 0.
// ------------------------------------------------------------------
O$._getDateFromFormat = function(val, format, locale) {
  val = val + "";
  format = format + "";
  var i_val = 0;
  var i_format = 0;
  var c = "";
  var token = "";
  var x,y;
  var now = new Date();
  var year = now.getFullYear();
  var month = now.getMonth() + 1;
  var date = 1;
  var hh = now.getHours();
  var mm = now.getMinutes();
  var ss = now.getSeconds();
  var amPm = "";
  var dayNames = O$.DAY_NAMES;

  var monthNames = O$.MONTH_NAMES;
  if (locale) {
    var dtf = O$.getDateTimeFormatObject(locale);
    if (dtf) {
      dayNames = dtf.WEEKDAYS.concat(dtf.SHORT_WEEKDAYS);
      monthNames = dtf.MONTHS.concat(dtf.SHORT_MONTHS);
    }
  }

  while (i_format < format.length) {
    // Get next token from format string
    c = format.charAt(i_format);
    token = "";
    while ((format.charAt(i_format) == c) && (i_format < format.length)) {
      token += format.charAt(i_format++);
    }
    // Extract contents of value based on format token
    if (token == "yyyy" || token == "yy" || token == "y") {
      if (token == "yyyy") {
        x = 4;
        y = 4;
      }
      if (token == "yy") {
        x = 2;
        y = 2;
      }
      if (token == "y") {
        x = 2;
        y = 4;
      }
      year = O$._getInt(val, i_val, x, y);
      if (year == null) {
        return 0;
      }
      i_val += year.length;
      if (year.length == 2) {
        if (year > 70) {
          year = 1900 + (year - 0);
        }
        else {
          year = 2000 + (year - 0);
        }
      }
    } else if (token == "MMM" || token == "NNN") {
      month = 0;
      for (var monthNameIndex = 0; monthNameIndex < monthNames.length; monthNameIndex++) {
        var month_name = O$.unescapeHtml(monthNames[monthNameIndex]);
        if (val.substring(i_val, i_val + month_name.length).toLowerCase() == month_name.toLowerCase()) {
          if (token == "MMM" || (token == "NNN" && monthNameIndex > 11)) {
            month = monthNameIndex + 1;
            if (month > 12) {
              month -= 12;
            }
            i_val += month_name.length;
            break;
          }
        }
      }
      if ((month < 1) || (month > 12)) {
        return 0;
      }
    }
    else if (token == "EE" || token == "E") {
        for (var dayNameIndex = 0; dayNameIndex < dayNames.length; dayNameIndex++) {
          var day_name = O$.unescapeHtml(dayNames[dayNameIndex]);
          if (val.substring(i_val, i_val + day_name.length).toLowerCase() == day_name.toLowerCase()) {
            i_val += day_name.length;
            break;
          }
        }
      }
      else if (token == "MM" || token == "M") {
          month = O$._getInt(val, i_val, token.length, 2);
          if (month == null || (month < 1) || (month > 12)) {
            return 0;
          }
          i_val += month.length;
        }
        else if (token == "dd" || token == "d") {
            date = O$._getInt(val, i_val, token.length, 2);
            if (date == null || (date < 1) || (date > 31)) {
              return 0;
            }
            i_val += date.length;
          }
          else if (token == "hh" || token == "h") {
              hh = O$._getInt(val, i_val, token.length, 2);
              if (hh == null || (hh < 1) || (hh > 12)) {
                return 0;
              }
              i_val += hh.length;
            }
            else if (token == "HH" || token == "H") {
                hh = O$._getInt(val, i_val, token.length, 2);
                if (hh == null || (hh < 0) || (hh > 23)) {
                  return 0;
                }
                i_val += hh.length;
              }
              else if (token == "KK" || token == "K") {
                  hh = O$._getInt(val, i_val, token.length, 2);
                  if (hh == null || (hh < 0) || (hh > 11)) {
                    return 0;
                  }
                  i_val += hh.length;
                }
                else if (token == "kk" || token == "k") {
                    hh = O$._getInt(val, i_val, token.length, 2);
                    if (hh == null || (hh < 1) || (hh > 24)) {
                      return 0;
                    }
                    i_val += hh.length;
                    hh--;
                  }
                  else if (token == "mm" || token == "m") {
                      mm = O$._getInt(val, i_val, token.length, 2);
                      if (mm == null || (mm < 0) || (mm > 59)) {
                        return 0;
                      }
                      i_val += mm.length;
                    }
                    else if (token == "ss" || token == "s") {
                        ss = O$._getInt(val, i_val, token.length, 2);
                        if (ss == null || (ss < 0) || (ss > 59)) {
                          return 0;
                        }
                        i_val += ss.length;
                      }
                      else if (token == "a") {
                          if (val.substring(i_val, i_val + 2).toLowerCase() == "am") {
                            amPm = "AM";
                          }
                          else if (val.substring(i_val, i_val + 2).toLowerCase() == "pm") {
                            amPm = "PM";
                          }
                          else {
                            return 0;
                          }
                          i_val += 2;
                        }
                        else {
                          if (token != "'")
                            if (val.substring(i_val, i_val + token.length) != token) {
                              return 0;
                            }
                            else {
                              i_val += token.length;
                            }
                        }
  }
  // If there are any trailing characters left in the value, it doesn't match
  if (i_val != val.length) {
    return 0;
  }
  // Is date valid for month?
  if (month == 2) {
    // Check for leap year
    if (( (year % 4 == 0) && (year % 100 != 0) ) || (year % 400 == 0)) { // leap year
      if (date > 29) {
        return 0;
      }
    }
    else {
      if (date > 28) {
        return 0;
      }
    }
  }
  if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
    if (date > 30) {
      return 0;
    }
  }
  // Correct hours value
  if (hh < 12 && amPm == "PM") {
    hh = hh - 0 + 12;
  }
  else if (hh > 11 && amPm == "AM") {
    hh -= 12;
  }
  var newdate = new Date(year, month - 1, date, hh, mm, ss);
  return newdate.getTime();
};


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

O$._RegexValidator = function(summary, detail, regexPattern, trimNeeded, type) {
  this.summary = summary;
  this.detail = detail;
  this.regexPattern = regexPattern;
  this.trimNeeded = trimNeeded;
  this.type = type;
  try {
    this.regExp = new RegExp(regexPattern);
  } catch(e) {
    alert("Error in O$._RegexValidator. Currently defined pattern '" + this.regexPattern + "' is invalid.");
  }

};

O$._RegexValidator.prototype.validate = function(input) {
  var value = O$.getValue(input);

  if (this.trimNeeded) {
    value = O$.trim(value);
  }

  if (O$.notEmpty(value)) {
    if (!O$.matchRegExp(value, this.regExp)) {
      O$.addMessage(input, this.summary, this.detail, null, this);
      return false;
    }
  }
  return true;
};
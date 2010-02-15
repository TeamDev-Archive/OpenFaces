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

O$._CustomValidator = function(summary, detail, validateFunction) {
  this.summary = summary;
  this.detail = detail;
  this.validateFunction = validateFunction;
};

O$._CustomValidator.prototype.validate = function(input) {
  var value = O$.getValue(input);
  if (!value)
  {
    if (!this.validateFunction(input, value)) {
      O$.addMessage(input, this.summary, this.detail, null, this);
      return false;
    }
  } else
    if (O$.notEmpty(value)) {
      if (!this.validateFunction(input, value)) {
        O$.addMessage(input, this.summary, this.detail, null, this);
        return false;
      }
    }
  return true;
};

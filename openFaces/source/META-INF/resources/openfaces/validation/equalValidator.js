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

O$._EqualValidator = function(summary, detail, equalToElementId) {
  this.summary = summary;
  this.detail = detail;
  this.equalToElementId = equalToElementId;
};

O$._EqualValidator.prototype.validate = function(input) {
  var value = O$.getValue(input);
  var equalTo = O$.byIdOrName(this.equalToElementId);
  if (!equalTo)
    return true;
  var equalToValue = O$.getValue(equalTo);
  if (O$.notEmpty(value) || O$.notEmpty(equalToValue)) {
    if (!(value == equalTo.value))
    {
      O$.addMessage(input, this.summary, this.detail, null, this);
      return false;
    }

  }
  return true;

};

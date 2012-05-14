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

O$._LongConverterValidator = function(summary, detail) {
  this.conversionSummary = summary;
  this.conversionDetail = detail;
  this.type = "converter";
};

O$._LongConverterValidator.prototype.validate = function(input) {
  var value = O$.trim(O$.getValue(input));
  if (O$.notEmpty(value)) {
    if (!O$.isLong(value)) {
      O$.addMessage(input, this.conversionSummary, this.conversionDetail, null, this);
      return false;
    }
  }
  return true;
};

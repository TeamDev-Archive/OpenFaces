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

O$._DoubleRangeValidator = function(minimum, maximum, rangeSummary, rangeDetail, typeSummary, typeDetail) {
  this.minimum = minimum;
  this.maximum = maximum;
  this.rangeSummary = rangeSummary;
  this.rangeDetail = rangeDetail;
  this.typeSummary = typeSummary;
  this.typeDetail = typeDetail;
};

O$._DoubleRangeValidator.prototype.validate = function(input) {
  var value = O$.trim(O$.getValue(input));
  if (O$.notEmpty(value)) {
    if (!O$.isDouble(value)) {
      O$.addMessage(input, this.typeSummary, this.typeDetail, null, this);
      return false;
    }
    if (this.maximum) {
      if (value > this.maximum) {
        O$.addMessage(input, this.rangeSummary, this.rangeDetail, null, this);
        return false;
      }
    }
    if (this.minimum) {
      if (value < this.minimum) {
        O$.addMessage(input, this.rangeSummary, this.rangeDetail, null, this);
        return false;
      }
    }
  }
  return true;
};

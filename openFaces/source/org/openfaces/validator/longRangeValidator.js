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

O$._LongRangeValidator = function(minimum, maximum, rangeSummary, rangeDetail, typeSummary, typeDetail) {
  var min = O$.getLong(minimum);

  if (min != null) {
    this.minimum = min;
    this.testLowerBundle = true;
  } else {
    this.testLowerBundle = false;
  }


  var max = O$.getLong(maximum);
  if (max != null) {
    this.maximum = max;
    this.testUpperBundle = true;
  } else {
    this.testUpperBundle = false;
  }

  this.rangeSummary = rangeSummary;
  this.rangeDetail = rangeDetail;
  this.typeSummary = typeSummary;
  this.typeDetail = typeDetail;
};

O$._LongRangeValidator.prototype.validate = function(input) {
  var value = O$.trim(O$.getValue(input));

  if (O$.notEmpty(value)) {

    if (!O$.isLong(value)) {
      O$.addMessage(input, this.typeSummary, this.typeDetail, null, this);
      return false;
    }
    if (this.testUpperBundle) {
      if (value > this.maximum) {
        O$.addMessage(input, this.rangeSummary, this.rangeDetail, null, this);
        return false;
      }
    }

    if (this.testLowerBundle) {
      if (value < this.minimum) {
        O$.addMessage(input, this.rangeSummary, this.rangeDetail, null, this);
        return false;
      }
    }
  }
  return true;
};

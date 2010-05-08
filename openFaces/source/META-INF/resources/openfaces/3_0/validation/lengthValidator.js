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

O$._LengthValidator = function(minimum, maximum, minimumSummary, minimumDetail, maximumSummary, maximumDetail) {
  this.minimum = minimum;
  this.maximum = maximum;
  this.minimumSummary = minimumSummary;
  this.minimumDetail = minimumDetail;
  this.maximumSummary = maximumSummary;
  this.maximumDetail = maximumDetail;
};

O$._LengthValidator.prototype.validate = function(input) {
  var value = O$.getValue(input);
  if (O$.notEmpty(value)) {
    var length = value.length;
    if (this.maximum) {
      if (length > this.maximum) {
        O$.addMessage(input, this.maximumSummary, this.maximumDetail, null, this);
        return false;
      }
    }
    if (this.minimum) {
      if (length < this.minimum) {
        O$.addMessage(input, this.minimumSummary, this.minimumDetail, null, this);
        return false;
      }
    }
  }
  return true;
};

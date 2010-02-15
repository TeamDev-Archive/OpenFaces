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

O$._NumberConverterValidator = function(summary, detail, numberConverter) {
  this.conversionSummary = summary;
  this.conversionDetail = detail;
  this.converter = numberConverter;
  this.type = "converter";
};

O$._NumberConverterValidator.prototype.validate = function(input) {
  var value = O$.trim(O$.getValue(input));
  if (O$.notEmpty(value)) {
    var requestHelper = new O$.RequestHelper();
    try {
      var param = {
        validator: this.converter,
        value: value
      };

      var result = requestHelper.call("?validator=numberConverter", param, [{name:'teamdev_ajax_VALIDATOR',value:'validator'}]);

      if (result && result == "false") {
        O$.addMessage(input, this.conversionSummary, this.conversionDetail, null, this);
        return false;
      } else {
        return true;
      }
    } catch(x) {
    }
    //if (!O$.isLong(value)) {
    //O$.addMessage(input, this.conversionSummary, this.conversionDetail);
    //return false;

    //}
  }
  return true;
};

O$.NumberConverter = function(currencyCode, currencySymbol, locale, maxFractionDigits, maxIntegerDigits,
                              minFractionDigits, minIntegerDigits, pattern, type, groupingUsed, integerOnly, javaClassName) {
  this.currencyCode = currencyCode;
  this.currencySymbol = currencySymbol;
  this.locale = locale;
  this.maxFractionDigits = maxFractionDigits;
  this.maxIntegerDigits = maxIntegerDigits;
  this.minFractionDigits = minFractionDigits;
  this.minIntegerDigits = minIntegerDigits;
  this.pattern = pattern;
  this.type = type;
  this.groupingUsed = groupingUsed;
  this.integerOnly = integerOnly;
  this.javaClassName = javaClassName;
  this.validatorId = "javax.faces.Number";
};

/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

var formName = "validationForm:";
function fillInvalidData() {
  var field = O$(formName + "requiredField");
  field.value = "";
  field = O$(formName + "doubleRangeField");
  field.value = 5;
  field = O$(formName + "longRangeField");
  field.value = 1001;

  field = O$(formName + "integerField");
  field.value = "Non-integer value";
  field = O$(formName + "doubleField");
  field.value = "Non-double value";

  field = O$(formName + "byteField");
  field.value = "2V0";
  field = O$(formName + "dateTimeField2");
  field.value = "12/02/2007";
}

function fillValidData() {
  var field = O$(formName + "requiredField");
  field.value = "Required value";

  field = O$(formName + "doubleRangeField");
  field.value = 84;
  field = O$(formName + "longRangeField");
  field.value = -2;

  field = O$(formName + "integerField");
  field.value = 125;
  field = O$(formName + "doubleField");
  field.value = 42.12;

  field = O$(formName + "byteField");
  field.value = 120;
  field = O$(formName + "dateTimeField2");
  field.value = "13.10.2007";
}

function firstLoad() {
  var helper = O$(formName + "firstLoadHelper");
  if (helper.value == "true") {
    fillInvalidData();
    helper.value = "false";
  }
}

window.onload = firstLoad;

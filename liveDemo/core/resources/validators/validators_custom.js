/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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
  var field = O$(formName + "customValidatorField");
  field.value = "Incorrect value";
  field = O$(formName + "regExpValidatorField");
  field.value = "Incorrect value";

  field = O$(formName + "emailValidatorField");
  field.value = "Not e-mail address";
  field = O$(formName + "urlValidatorField");
  field.value = "Not URL address";
  field = O$(formName + "equalValidatorField1");
  field.value = "password 1";
  field = O$(formName + "equalValidatorField2");
  field.value = "password 2";
}
function fillValidData() {
  var field = O$(formName + "customValidatorField");
  field.value = "TeamDev";

  field = O$(formName + "regExpValidatorField");
  field.value = "+38 (057) 555 55 55";
  field = O$(formName + "emailValidatorField");
  field.value = "someone@gmail.com";
  field = O$(formName + "urlValidatorField");
  field.value = "http://openfaces.org";
  field = O$(formName + "equalValidatorField1");
  field.value = "A8edf2-ewer";
  field = O$(formName + "equalValidatorField2");
  field.value = "A8edf2-ewer";
}

function firstLoad() {
  var helper = O$(formName + "firstLoadHelper");
  if (helper.value == "true") {
    fillInvalidData();
    helper.value = "false";
  }
}

window.onload = firstLoad;

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

function fillInvalidData(formName) {
  var field = document.getElementById(formName + "validDR");
  field.value = 5;
  field = document.getElementById(formName + "required");
  field.value = "";
  O$._setSelectedDate(formName + "c", null);
  O$._setDateChooserDate(formName + "dch", null);
  O$(formName + "ddf").setValue("");
  O$(formName).setValue("tls");
  field = document.getElementById(formName + "equal1");
  field.value = "text";
  field = document.getElementById(formName + "equal2");
  field.value = "another text";
  field = document.getElementById(formName + "url");
  field.value = "not url";
  field = document.getElementById(formName + "email");
  field.value = "not email";
  field = document.getElementById(formName + "regExp");
  field.value = "not number";
  field = document.getElementById(formName + "custom");
  field.value = "not 10";
}

function fillValidData(formName) {
  var field = document.getElementById(formName + "validDR");
  field.value = 0.1;
  field = document.getElementById(formName + "required");
  field.value = "text";
  O$._setSelectedDate(formName + "c", new Date());
  O$._setDateChooserDate(formName + "dch", new Date());
  O$(formName + "ddf").setValue("red");
  O$(formName).setValue("tls", ['value0']);
  field = document.getElementById(formName + "equal1");
  field.value = "password";
  field = document.getElementById(formName + "equal2");
  field.value = "password";
  field = document.getElementById(formName + "url");
  field.value = "http://www.teamdev.com";
  field = document.getElementById(formName + "email");
  field.value = "support@teamdev.com";
  field = document.getElementById(formName + "regExp");
  field.value = "-1.3";
  field = document.getElementById(formName + "custom");
  field.value = "10";
}
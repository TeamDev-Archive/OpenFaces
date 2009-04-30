var formName = "validationForm:";
function fillInvalidData() {
  var field = document.getElementById(formName + "customValidatorField");
  field.value = "Incorrect value";
  field = document.getElementById(formName + "regExpValidatorField");
  field.value = "Incorrect value";

  field = document.getElementById(formName + "emailValidatorField");
  field.value = "Not e-mail address";
  field = document.getElementById(formName + "urlValidatorField");
  field.value = "Not URL address";
  field = document.getElementById(formName + "equalValidatorField1");
  field.value = "password 1";
  field = document.getElementById(formName + "equalValidatorField2");
  field.value = "password 2";
}
function fillValidData() {
  var field = document.getElementById(formName + "customValidatorField");
  field.value = "TeamDev";

  field = document.getElementById(formName + "regExpValidatorField");
  field.value = "+38 (057) 555 55 55";
  field = document.getElementById(formName + "emailValidatorField");
  field.value = "someone@gmail.com";
  field = document.getElementById(formName + "urlValidatorField");
  field.value = "http://openfaces.org";
  field = document.getElementById(formName + "equalValidatorField1");
  field.value = "A8edf2-ewer";
  field = document.getElementById(formName + "equalValidatorField2");
  field.value = "A8edf2-ewer";
}

function firstLoad() {
  var helper = document.getElementById(formName + "firstLoadHelper");
  if (helper.value == "true") {
    fillInvalidData();
    helper.value = "false";
  }
}

window.onload = firstLoad;

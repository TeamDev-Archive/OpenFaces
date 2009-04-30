var formName = "validationForm:";
function fillInvalidData() {
  var field = document.getElementById(formName + "requiredField");
  field.value = "";
  field = document.getElementById(formName + "doubleRangeField");
  field.value = 5;
  field = document.getElementById(formName + "longRangeField");
  field.value = 1001;

  field = document.getElementById(formName + "integerField");
  field.value = "Non-integer value";
  field = document.getElementById(formName + "doubleField");
  field.value = "Non-double value";

  field = document.getElementById(formName + "byteField");
  field.value = "2V0";
  field = document.getElementById(formName + "dateTimeField2");
  field.value = "12/02/2007";
}

function fillValidData() {
  var field = document.getElementById(formName + "requiredField");
  field.value = "Required value";

  field = document.getElementById(formName + "doubleRangeField");
  field.value = 84;
  field = document.getElementById(formName + "longRangeField");
  field.value = -2;

  field = document.getElementById(formName + "integerField");
  field.value = 125;
  field = document.getElementById(formName + "doubleField");
  field.value = 42.12;

  field = document.getElementById(formName + "byteField");
  field.value = 120;
  field = document.getElementById(formName + "dateTimeField2");
  field.value = "13.10.2007";
}

function firstLoad() {
  var helper = document.getElementById(formName + "firstLoadHelper");
  if (helper.value == "true") {
    fillInvalidData();
    helper.value = "false";
  }
}

window.onload = firstLoad;

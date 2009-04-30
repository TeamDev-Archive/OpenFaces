<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <title>Validators in one form (custom)</title>
  <script type="text/javascript">
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
      field.value = "http://teamdev.com";
      field = document.getElementById(formName + "equalValidatorField1");
      field.value = "A8edf2-ewer";
      field = document.getElementById(formName + "equalValidatorField2");
      field.value = "A8edf2-ewer";
    }

    function firstLoad() {
      var helper = document.getElementById(formName + "firstLoadHelper");
      if (helper.value == true || helper.value == 'true') {
        fillInvalidData();
        helper.value = false;
      }
    }

    firstLoad();
  </script>
</head>

<body>
<f:view>
  <h:form id="validationForm">
    <%@ include file="ValidatorsInOneFormCustom_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
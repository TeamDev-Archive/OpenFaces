<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <title>Standart Validators</title>
  <style type="text/css">
    .demo_table td {
      border-bottom: 1px solid #E0E0E0;
      padding: 4px;
    }
  </style>
  <script type="text/javascript">
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
    <%@ include file="Validators_standard_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
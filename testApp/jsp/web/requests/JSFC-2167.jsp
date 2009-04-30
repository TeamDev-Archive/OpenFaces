<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o"%>
<%@ taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a4j" %>

<html>
<head>
  <title>Validation OpenFaces Demo</title>
  <style type="text/css">
    .demo_table td {
      border-bottom: 1px solid #E0E0E0;
      padding: 4px;
    }
  </style>

</head>

<body>
<div id="nonFooter">
  <f:view>
    <h:form id="validationForm">
      <o:clientValidationSupport clientValidation="onDemand"/>
      <h:inputHidden id="firstLoadHelper" value="#{JSFC2167.firstLoad}"/>

      <div class="control_demo_space">
        <!-- start content -->
        <%@ include file="JSFC_2167_core.xhtml" %>
        <!-- end content -->
      </div>

    </h:form>
  </f:view>
</div>

</body>
<script type="text/javascript">
  var formName = "validationForm:";
  function fillInvalidData() {
    var field = document.getElementById(formName + "requiredField");
    field.value = "";
/*    field = document.getElementById(formName + "doubleRangeField");
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
    field.value = "12/02/2007";*/
  }

  function fillValidData() {
    var field = document.getElementById(formName + "requiredField");
    field.value = "Required value";

/*    field = document.getElementById(formName + "doubleRangeField");
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
    field.value = "13.10.2007";*/
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
</html>
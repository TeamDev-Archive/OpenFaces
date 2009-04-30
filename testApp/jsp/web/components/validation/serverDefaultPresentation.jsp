<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <title>Test server defult presentation</title>
  <script type="text/javascript">
    function fillInvalidData() {
      var formName = "testForm:";
      var field = document.getElementById(formName + "validDR");
      field.value = 5;
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

    function fillValidData() {
      var formName = "testForm:";
      var field = document.getElementById(formName + "validDR");
      field.value = 0.1;
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
  </script>
</head>

<body>
<f:view>
  <h:form id="testForm">
    <%@ include file="serverDefaultPresentation_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
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
  <script src="../funcTestsUtil.js" type="text/javascript"></script>
  <script type="text/javascript">
    function fillInvalidData() {
      var formName = "testForm:";
      var field = O$(formName + "validDR");
      field.value = 5;
      field = O$(formName + "equal1");
      field.value = "text";
      field = O$(formName + "equal2");
      field.value = "another text";
      field = O$(formName + "url");
      field.value = "not url";
      field = O$(formName + "email");
      field.value = "not email";
      field = O$(formName + "regExp");
      field.value = "not number";
      field = O$(formName + "custom");
      field.value = "not 10";
    }

    function fillValidData() {
      var formName = "testForm:";
      var field = O$(formName + "validDR");
      field.value = 0.1;
      field = O$(formName + "equal1");
      field.value = "password";
      field = O$(formName + "equal2");
      field.value = "password";
      field = O$(formName + "url");
      field.value = "http://www.teamdev.com";
      field = O$(formName + "email");
      field.value = "support@teamdev.com";
      field = O$(formName + "regExp");
      field.value = "-1.3";
      field = O$(formName + "custom");
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
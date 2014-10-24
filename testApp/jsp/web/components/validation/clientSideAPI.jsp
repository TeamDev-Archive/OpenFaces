<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <title>Client Side API</title>

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

    function validateComponentsByIDs() {
      O$.validateById('testForm:required');
      O$.validateById('testForm:c');
      O$.validateById('testForm:dch');
      O$.validateById('testForm:tls');
      O$.validateById('testForm:ddf');
      O$.validateById('testForm:validDR');
      O$.validateById('testForm:equal2');
      O$.validateById('testForm:url');
      O$.validateById('testForm:email');
      O$.validateById('testForm:regExp');
      O$.validateById('testForm:custom');
      return false;
    }

    function validateComponents() {
      O$.validate(O$('testForm:required'));
      O$.validate(O$('testForm:c'));
      O$.validate(O$('testForm:dch'));
      O$.validate(O$('testForm:tls'));
      O$.validate(O$('testForm:ddf'));
      O$.validate(O$('testForm:validDR'));
      O$.validate(O$('testForm:equal2'));
      O$.validate(O$('testForm:url'));
      O$.validate(O$('testForm:email'));
      O$.validate(O$('testForm:regExp'));
      O$.validate(O$('testForm:custom'));
      return false;
    }
  </script>
</head>

<body>
<f:view>
  <h:form id="testForm">
    <%@ include file="clientSideAPI_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
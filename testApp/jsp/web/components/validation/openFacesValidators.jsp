<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <title>Testing OpenFaces validators</title>
  <script type="text/javascript">
    function fillInvalidData(formName) {
      var field = document.getElementById(formName + "equal1");
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
      var field = document.getElementById(formName + "equal1");
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
  <a href="../../../../main.jsp" style="float: right;">Return to main page</a>
  <br/>
  <a href="../../../../components_test.jsp" style="float: right;">Return to validation testing main page</a>
  <table width="100%">
    <tr>
      <td width="50%">
        <h:form id="serverValidation">
          <%@ include file="openFacesValidatorsServer_core.xhtml" %>
        </h:form>
      </td>
      <td width="50%">
        <h:form id="clientValidation">
          <%@ include file="openFacesValidatorsClient_core.xhtml" %>
        </h:form>
      </td>
    </tr>
  </table>
</f:view>

</body>
</html>
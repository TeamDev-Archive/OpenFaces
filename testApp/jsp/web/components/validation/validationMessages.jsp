<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <title>Validation messages</title>
  <script type="text/javascript">
    function fillInvalidData(formName) {
      var field = O$(formName + ":required");
      field.value = "";
      field = O$(formName + ":validDR");
      field.value = 5;
      field = O$(formName + ":url");
      field.value = "not url";
      field = O$(formName + ":ddf");
      field.value = "";

    }

    function fillValidData(formName) {
      var field = O$(formName + ":required");
      field.value = "text";
      field = O$(formName + ":validDR");
      field.value = 0.1;
      field = O$(formName + ":url");
      field.value = "http://www.teamdev.com";
      field = O$(formName + ":ddf");
      field.value = "text";

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
          <%@ include file="validationMessagesServer_core.xhtml" %>
        </h:form>
      </td>
      <td width="50%">
        <h:form id="clientValidation">
          <%@ include file="validationMessagesClient_core.xhtml" %>
        </h:form>
      </td>
    </tr>
  </table>
</f:view>

</body>
</html>
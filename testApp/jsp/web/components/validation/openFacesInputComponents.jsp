<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <script src="../funcTestsUtil.js" type="text/javascript"></script>
  <title>OpenFaces input components</title>
</head>

<body>
<f:view>
  <a href="../../../../main.jsp" style="float: right;">Return to main page</a>
  <br/>
  <a href="../../../../components_test.jsp" style="float: right;">Return to validation testing main page</a>
  <table>
    <tr>
      <td>
        <h3>Client validation (client validation = onSubmit)</h3>
      </td>
      <td>
        <h3>Server validation (client validation = off)</h3>
      </td>
    </tr>
    <tr>
      <td>
        <h:form id="clientValidation">
          <%@ include file="openFacesInputComponentsClient_core.xhtml" %>
        </h:form>
      </td>
      <td>
        <h:form id="serverValidation">
          <%@ include file="openFacesInputComponentsServer_core.xhtml" %>
        </h:form>
      </td>
    </tr>
  </table>
</f:view>

</body>
</html>
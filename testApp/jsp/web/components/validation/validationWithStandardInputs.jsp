<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <script src="../asd.js" type="text/javascript"></script>
  <title>Standard input components with OpenFaces validation</title>
</head>

<body>
<f:view>
  <a href="../../../../main.jsp" style="float: right;">Return to main page</a>
  <br/>
  <a href="../../../../components_test.jsp" style="float: right;">Return to validation testing main page</a>
  <br/>
  <table>
    <tr>
      <td width="50%">
        <h:form id="serverValidation">
          <%@ include file="validationWithStandardInputsServer_core.xhtml" %>
        </h:form>
      </td>
      <td width="50%">
        <h:form id="clientValidation">
          <%@ include file="validationWithStandardInputsClient_core.xhtml" %>
        </h:form>
      </td>
    </tr>
  </table>
</f:view>

</body>
</html>
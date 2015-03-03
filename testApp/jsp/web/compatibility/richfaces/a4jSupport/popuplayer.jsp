<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%> 
<html>
<head>
  <script src="../../funcTestsUtil.js" type="text/javascript"></script>

  <title>a4j:support tag with client-side events of PopupLayer component</title>
</head>

<body>
<f:view>
  <h:form id="form1">
   <%@ include file="popuplayer_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
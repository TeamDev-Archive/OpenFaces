<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%> 
<html>
<head>
  <title>OpenFaces components in the Richfaces Panel component</title>
</head>

<body>
<f:view>
  <h:form id="form1">
   <%@ include file="richPanel_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head><title>JSFC-1105 Client-side validation does'n work for RegExpValidator with "[-\+]?[0-9]*\.?[0-9]*"
  pattern</title></head>

<body>
<f:view>
  <h:form id="form1">
   <%@ include file="JSFC_1105_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
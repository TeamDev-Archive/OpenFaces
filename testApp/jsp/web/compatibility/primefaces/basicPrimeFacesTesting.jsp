<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://primefaces.org/ui" prefix="p" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<f:view>
  <h:head>
    <title>Basic Prime Faces Testing</title>
  </h:head>

  <h:body>

  <h:form id="form">
    <%@ include file="basicPrimeFacesTesting_core.xhtml" %>
  </h:form>
  </h:body>
</f:view>
</html>

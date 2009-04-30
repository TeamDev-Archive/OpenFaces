<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>Calendar Test</title>
  <script type="text/javascript" src="../../funcTestsUtil.js"></script>
  <link rel="STYLESHEET" type="text/css" href="../../main.css"/>
</head>


<body id="styledCalendarPage">
<h2 style="background: #e0e0e0; padding-bottom: 5px; border-bottom: 1px solid black;">Calendar component demo page</h2>
<f:view>
  <h:form id="formID">
   <%@ include file="calendarLocales_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
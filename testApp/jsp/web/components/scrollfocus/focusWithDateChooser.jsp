<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<html>
<head>
  <title>o:focus with DateChooser</title>
  <script src="../funcTestsUtil.js" type="text/javascript"></script>
<link rel="STYLESHEET" type="text/css" href="../../main.css"/>
</head>
<body>
<f:view>
  <h:form id="formID">
    <%@ include file="focusWithDateChooser_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
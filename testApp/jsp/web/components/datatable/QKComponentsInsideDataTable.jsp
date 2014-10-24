<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>All Components Inside DataTable</title>
  <style type="text/css">
    .container {
      vertical-align: top;
      padding: 20px 20px 20px 20px;
      background: #FCF0CD;
      border-left: 1px solid darkgray;
      border-top: 1px solid darkgray;
      border-bottom: 2px solid darkgray;
      border-right: 2px solid darkgray;
    }
  </style>
  <script src="../funcTestsUtil.js" type="text/javascript"></script>
<link rel="STYLESHEET" type="text/css" href="../../main.css"/>
</head>

<body>
<f:view>
  <h:form id="fn">
   <%@ include file="QKComponentsInsideDataTable_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
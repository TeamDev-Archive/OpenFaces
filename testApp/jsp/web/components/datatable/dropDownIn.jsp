<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>DropDown Inside DataTable</title>
  <script src="../asd.js" type="text/javascript"></script>

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
</head>

<link rel="STYLESHEET" type="text/css" href="../../main.css"/>
<body>
<f:view>
  <h:form id="fn">
   <%@ include file="dropDownIn_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
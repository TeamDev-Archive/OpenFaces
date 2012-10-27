<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>Tomahawk Components in TabbedPane</title>
  <script type="text/javascript" src="../funcTestsUtil.js"></script>

  <link rel="stylesheet" type="text/css" href="basic.css" />
  <style type="text/css">
    .container{
      vertical-align:top;
      padding:10px 10px 10px 10px;
      border:1px solid darkgray;
    }
    .container_one{
      background:beige;
      vertical-align:top;
      padding:10px 10px 10px 10px;
      border:1px solid darkgray;
    }
  </style>
</head>

<body>
<f:view>
  <h:form id="fn">
   <%@ include file="withTabbedPane_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
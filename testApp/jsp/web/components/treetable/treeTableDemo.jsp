<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>TreeTable Demo</title>
  <style>
    .siteMapLink {
      font-family: verdana, Helvetica, sans-serif;
      font-size: 10pt;
      margin-left: 4px;
      border-bottom: 1px dotted rgb( 17, 86, 185 );
      text-decoration: none;
      color: rgb( 17, 86, 185 );
    }

    .siteMapLink:hover {
      font-family: verdana, Helvetica, sans-serif;
      font-size: 10pt;
      margin-left: 4px;
      border-bottom-style: none;
      text-decoration: underline;
      color: rgb( 17, 86, 185 );
    }
  </style>

  <script type="text/javascript">
    function updatePermissionsBtn() {
      O$('form1:permissionsBtn').disabled = O$('form1:permissionsTreeTable').isSelectionEmpty;
    }
  </script>
</head>

<body onload="updatePermissionsBtn();">
<h2 style="background: #e0e0e0; padding-bottom: 5px; border-bottom: 1px solid black;">TreeTable Component Demo</h2>
<a href="../../../main.jsp" style="float: right">Return to main page</a>

<f:view>
  <h:form id="form1">
   <%@ include file="treeTableDemo_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
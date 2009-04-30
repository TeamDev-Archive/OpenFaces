<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>DropDown Client-based Suggestion Functionality</title>
  <script type="text/javascript" src="../../funcTestsUtil.js"></script>
  <link rel="STYLESHEET" type="text/css" href="../../main.css"/>
  <style type="text/css">
    .demo_font {
      font-family: Tahoma, Verdana, Arial, Helvetica, sans-serif;
      font-size: 11px;
    }

    .colorRect {
      display: block;
      font-size: 0;
      width: 12px;
      height: 12px;
      border: 1px solid gray;
    }
  </style>
</head>

<body>
<f:view>
  <h:form id="formID">
   <%@ include file="dropDownClientSuggestion_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>Table Test</title>
  <style type="text/css">
    .cls1 {width: 400px; }
    .selection1 {color: blue;}
    .headCol {text-align: left; padding-left: 15px; width: 100%;}
    .paginatorCol {text-align: right; }
  </style>

  <style type="text/css">
    .rowRollover{background: silver;}
    .requestsHeader{background: gray;}
    .requestsTable{width: 100%;}
    .requestsSelection{background: #e0e8ff;}
    .tab_active{font-family: Tahoma;}
  </style>

</head>

<body>
<f:view>
  <h:form id="form1">
   <%@ include file="tableTest_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
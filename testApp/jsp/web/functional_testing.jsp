<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>Components Test</title>
  <style type="text/css">
    .componentName {
      display: block;
      width: 50%;
      border-bottom: 1px solid black;
      font-size: 16px;
      background: lightgrey;
    }
  </style>
</head>

<body>
<f:view>
  <h:form>
   <%@ include file="functional_testing_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
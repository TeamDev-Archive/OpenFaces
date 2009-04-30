<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>Equal Validator Test</title>
  <link href="validator.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="validator.js"></script>
</head>

<body>
<f:view>
  <h:form>
   <%@ include file="equalValidatorTest_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
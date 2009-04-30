<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://jboss.com/products/seam/taglib" prefix="s" %>

<html>
<head>
  <title></title>
</head>

<body>
<f:view>
  <a href="../../../../JBossSeamTesting.jsp">Return to main page</a>

  <p>Renders a file upload control.</p>
  <h:form enctype="multipart/form-data">
    <%@ include file="FileUpload_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
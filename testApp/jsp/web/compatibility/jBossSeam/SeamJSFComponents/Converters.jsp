<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib uri="http://jboss.com/products/seam/taglib" prefix="s" %>
<html>
<head>
  <title></title>
  <script type="text/javascript">
    function fillInvalidData(formName) {
      var field = document.getElementById(formName + ":dateConv");
      field.value = "12/02/2007";
      field = document.getElementById(formName + ":dateConv2");
      field.value = "12/02/2007";
    }

    function fillValidData(formName) {
      var field = document.getElementById(formName + ":dateConv");
      field.value = "Jun-07-2006 12:00";
      field = document.getElementById(formName + ":dateConv2");
      field.value = "Thursday, June 8, 2006 10:00 PM";
    }

  </script>
</head>


<body>
<f:view>
  <h:form id="server">
    <%@ include file="ConvertersServer_core.xhtml" %>
  </h:form>
  <h:form id="client">
    <%@ include file="ConvertersClient_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
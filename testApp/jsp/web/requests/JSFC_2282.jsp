<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <title>JSFC-2282 Confirmation is non-modal if "modalLayerStyle" attribute not specified (IE)</title>
  <script type="text/javascript">
    function test() {
      if (O$.getElementStyle(document.getElementById('fm:cn::blockingLayer'), 'background-image').indexOf('clear.gif') >= 0) {
        alert('OK');
      }
    }
  </script>
</head>

<body>
<f:view>
  <h:form id="fm">
    <%@ include file="JSFC_2282_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
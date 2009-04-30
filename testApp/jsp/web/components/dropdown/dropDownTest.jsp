<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>DropDown Test</title>
  <link rel="STYLESHEET" type="text/css" href="../../main.css"/>
  <script language="JavaScript">
    function doShake() {
      var i;
      var d = 1;
      for (i = 0; i < 50; i++) {
        setTimeout("window.moveBy(10*" + d + ",10*" + d + ");", i * 25);
        window.moveBy(10 * d, 10 * d);
        d = d * -1;
      }
    }
  </script>
</head>

<body>
<f:view>
  <h:form id="form1">
   <%@ include file="dropDownTest_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
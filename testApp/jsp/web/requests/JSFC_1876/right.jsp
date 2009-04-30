<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
    <title>Right</title>
    <link rel="stylesheet" type="text/css" href="main.css"/>
    <link rel="stylesheet" type="text/css" href="ms.css"/>

    <script type="text/javascript" src="util.js"></script>
  </head>

  <body style="background-image:url('background_streg.jpg');background-repeat:repeat-x;background-position:top;background-color:#ffffff;">
<f:view>
  <h:form>
   <%@ include file="right_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
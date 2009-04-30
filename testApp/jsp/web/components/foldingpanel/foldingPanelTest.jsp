<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>FoldingPanel Test</title>
  <style type="text/css">
    .fpStyle {
      border: 1px dotted gray;
    }

    .fpCaptionStyle {
      background-color: black;
      color: white;
      font-family: Tahoma;
      font-size: 12px;
      font-weight: bold;
    }

    .fpContentStyle {
      font-family: Tahoma;
      font-size: 12px;
      color: green;
    }
  </style>
</head>

<body>
<f:view>
  <h:form>
   <%@ include file="foldingPanelTest_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
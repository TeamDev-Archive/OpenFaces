<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <title>Check functionality of 'Today' and 'None' buttons</title>
  <script src="../funcTestsUtil.js" type="text/javascript"></script>
  <script type="text/javascript">
    function printTodayStyle(dateChooserID, outputDiv) {
      var mousedownEvt = O$.createEvent('mousedown');
      getControl(dateChooserID + buttonSuffix).onmousedown(mousedownEvt);
      printInfo('color: ' + O$.getElementStyle(getControl(dateChooserID + todaySuffix), 'color') + '; ' +
                'cursor: ' + O$.getElementStyle(getControl(dateChooserID + todaySuffix), 'cursor'), outputDiv, false);
      getControl(dateChooserID + buttonSuffix).onmousedown(mousedownEvt);
      return false;
    }

    function printNoneStyle(dateChooserID, outputDiv) {
      var mousedownEvt = O$.createEvent('mousedown');
      getControl(dateChooserID + buttonSuffix).onmousedown(mousedownEvt);
      printInfo('color: ' + O$.getElementStyle(getControl(dateChooserID + noneSuffix), 'color') + '; ' +
                'cursor: ' + O$.getElementStyle(getControl(dateChooserID + noneSuffix), 'cursor'), outputDiv, false);
      getControl(dateChooserID + buttonSuffix).onmousedown(mousedownEvt);
      return false;
    }
  </script>
  <link rel="STYLESHEET" type="text/css" href="../../main.css"/>
</head>

<body>
<f:view>
  <h:form id="formID">
    <%@ include file="dateChooserTodayNoneButton_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
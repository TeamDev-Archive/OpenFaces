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
  <script src="../asd.js" type="text/javascript"></script>

  <script type="text/javascript">
    function printSelectedDate(calendarId, outputDiv) {
      var dateString = new Object();
      var date = O$(calendarId).getSelectedDate();

      if (date != null) {
        var month = date.getMonth() + 1;
        if (month < 10) {
          month = "0" + month;
        }
        var day = date.getDate();
        var year = date.getFullYear();
        dateString = day + '.' + month + '.' + year;
      } else {
        dateString = 'null';
      }
      printInfo(dateString, outputDiv, false);
      return false;
    }
  </script>
  <link rel="STYLESHEET" type="text/css" href="../../main.css"/>
</head>
<body>
<f:view>
  <h:form id="formID">
    <%@ include file="calendarTodayNoneButtons_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
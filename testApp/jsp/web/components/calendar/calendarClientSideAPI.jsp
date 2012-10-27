<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <title>Calendar client-side API</title>
  <script src="../funcTestsUtil.js" type="text/javascript"></script>
  <script type="text/javascript">
    function getActualSelectedDate(){
      var calendar = getControl('formID:calendarAPI::body');
      var calendarRows = calendar.childNodes;
      var actualDate = "";

      // loop starts with 1 because zero row is day names
      for (var i = 1; i < calendarRows.length; i++) {
        var row = calendarRows[i];
        var week = row.childNodes;
        for (var j = 0; j < week.length; j++) {
          var date = week[j].childNodes[0];
          var dateClass = date.className;
          if (dateClass.indexOf('selectedDate') != -1) {
            actualDate = date.textContent;
            break;
          }
        }
      }
      return actualDate;
    }

    function printGetDateResult() {
      var actualDate = getActualSelectedDate();
      var getSelectedDateResult = O$('formID:calendarAPI').getSelectedDate().getDate();
      var equalSign;
      if(actualDate == getSelectedDateResult){
        equalSign = "=";
      }else{
        equalSign = "!=";
      }
      printInfo(actualDate + equalSign + getSelectedDateResult, 'dateGetterOutput', false);
    }

    function printSetDateResult() {
      var date = new Date();
      date.setFullYear(2007,10,20);
      O$('formID:calendarAPI').setSelectedDate(date);
      var actualDate = getActualSelectedDate();
      if(actualDate == '20' && getControl('formID:calendarAPI--month').textContent=='November' && getControl('formID:calendarAPI--year').textContent=='2007'){
        printInfo('works well :-)', 'dateSetterOutput', false);
      }else{
        printInfo('there are errors!', 'dateSetterOutput', false);
      }
    }
  </script>
  <link rel="STYLESHEET" type="text/css" href="../../main.css"/>
  <style type="text/css">
    .selectedDate {
      background: blue !important;
    }
  </style>
</head>

<body id="styledCalendarPage">
<f:view>
  <h:form id="formID">
   <%@ include file="calendarClientSideAPI_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
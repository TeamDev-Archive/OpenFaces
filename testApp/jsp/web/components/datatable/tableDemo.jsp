<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <title>Table Demo</title>

  <style type="text/css">
    .defaultTableStyle {
      font: 10pt Tahoma;
      cursor: default;
    }

    .defaultHeaderStyle {
      background: #d0d0d0;
    }

    .lnk1 {
      color: blue;
      font: 10pt Tahoma;
    }

    .lnk1:hover {
      color: red;
      font: 10pt Tahoma;
    }
  </style>
  <script type="text/javascript">
    function checkReadUnreadKeys(evt) {
      var e = evt ? evt : window.event;
      if (e.altKey || e.ctrlKey || e.shiftKey) return;
      if (e.keyCode == 82)
        document.getElementById("form1:markAsReadBtn").click();
      if (e.keyCode == 85)
        document.getElementById("form1:markAsUnreadBtn").click();
    }

    function showCurrentIndex() {
      var table = document.getElementById("form1:checkBoxColumnTable");
      var idx = table.getSelectedRowIndex();
      var rowIndex = document.getElementById("form1:rowIndex");
      rowIndex.innerHTML = "table.getSelectedRowIndex() returns: " + idx;
      O$.ajaxReload(['form1:checkedUsers'], {execute: ['form1:checkBoxColumnTable']});
    }
  </script>
</head>

<body onload="showCurrentIndex();" onclick="O$('form1:selectColumnsPopup').hide()">
<h2 style="background: #e0e0e0; padding-bottom: 5px; border-bottom: 1px solid black;">Table Component Demo</h2>
<f:view>
  <h:form id="form1">
    <%@ include file="tableDemo_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
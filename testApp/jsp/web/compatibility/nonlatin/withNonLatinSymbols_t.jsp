<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
  <style type="text/css">
    .fieldsLayouter_class {
      border-bottom: 2px solid gray;
      border-left: 1px solid gray;
      border-right: 2px solid gray;
      border-top: 1px solid gray;
      width: 320px;
    }

    .event_name {
      background: Beige;
      padding: 5px 5px 5px 5px;
      border: 1px solid darkgray;
      text-align: center;
      width: 30%;
    }

    .date_chooser {
      padding: 5px 5px 5px 5px;
      border: 1px solid darkgray;
      text-align: center;
      width: 30%;
    }
  </style>
  <script>
    function runAlert(value) {
      alert(value);
      return false;
    }
    function fillValidData() {
      var field = document.getElementById("fn:customValidatorField");
      field.value = "ТимДэв";
    }
  </script>
  <title>OpenFaces components with non-latin symbols</title>
  <script type="text/javascript" src="utility.js"></script>
</head>

<body>
<f:view>
  <h:form id="fn">
   <%@ include file="withNonLatinSymbols_t_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
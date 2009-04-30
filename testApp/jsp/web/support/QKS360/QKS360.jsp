<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <style type="text/css">
    body {
      position: fixed;
      width: 1200px;
      margin: 0 auto;
      padding: 25px 20px 0 20px;
      background: #666666 scroll no-repeat top center;

      font-family: "Cordia New";
      font-size: 14px;
      color: #503f14;
    }

    .datatable {
      background: #f3fafb;
      font: 16px "Cordia New";
      border: #503f14 1px solid;
      color: #503f14;
      padding: 10px;
    }

    .mainarea {
      width: 95%;
      background: #b7c8d3;
      padding-top: 30px;
      padding: 10px;
      border-spacing: 10px;
      border-bottom: #503f14 1px solid;
      border-left: #503f14 1px solid;
      border-right: #503f14 1px solid;
      border-top: none;
      position: relative;

    }

  </style>
  <title></title>
</head>

<body>
<f:view>
  <h:form>
    <%@ include file="QKS360_core.xhtml" %>
  </h:form>
  <h:panelGrid columns="1" styleClass="mainarea" border="0" cellpadding="0" cellspacing="0">
    <f:subview id="areasTableView">
      <h:form id="tableForm">
        <%@ include file="QKS360Table_core.xhtml" %>
      </h:form>
    </f:subview>
  </h:panelGrid>
</f:view>

</body>
</html>
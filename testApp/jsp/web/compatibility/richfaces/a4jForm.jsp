<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<html>
<head>
  <title>Ajax4jsf's form</title>
</head>

<body onload="fillValidData('form1:');">
<f:view>
  <a4j:form id="form1" ajaxSubmit="true" reRender="ouputPanel">
    <a4j:outputPanel id="ouputPanel">
      <%@ include file="a4j_core.xhtml" %>
      <%@ include file="openFacesComponents/calendar.xhtml" %>
      <%@ include file="openFacesComponents/chart.xhtml" %>
      <%@ include file="openFacesComponents/confirmation.xhtml" %>
      <%@ include file="openFacesComponents/datatable.xhtml" %>
      <%@ include file="openFacesComponents/datechooser.xhtml" %>
      <%@ include file="openFacesComponents/dropdownfield.xhtml" %>
      <%@ include file="openFacesComponents/dynamicimage.xhtml" %>
      <%@ include file="openFacesComponents/foldingpanel.xhtml" %>
      <%@ include file="openFacesComponents/hintlabel.xhtml" %>
      <%@ include file="openFacesComponents/popuplayer.xhtml" %>
      <%@ include file="openFacesComponents/suggestionfield.xhtml" %>
      <%@ include file="openFacesComponents/tabbedpane.xhtml" %>
      <%@ include file="openFacesComponents/tabset.xhtml" %>
      <%@ include file="openFacesComponents/treetable.xhtml" %>
      <%@ include file="openFacesComponents/twolistselection.xhtml" %>
      <%@ include file="openFacesComponents/validation.xhtml" %>
    </a4j:outputPanel>
  </a4j:form>
</f:view>

</body>
</html>
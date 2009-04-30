<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<jsp:useBean id="testData" class="org.openfaces.component.chart.PlainModel" scope="session"/>
<jsp:useBean id="testDataModel" class="org.openfaces.renderkit.DefaultImageDataModel" scope="session"/>

<html>
<head>
  <title>Demography</title>
</head>

<body>
<script language="JavaScript">
  <!--
  function go_there()
  {
    var where_to = confirm("Do you really want to view new information?");
    return where_to;
  }
  //-->
</script>

<h2 style="background: #e0e0e0; padding-bottom: 5px; border-bottom: 1px solid black;">Chart component tests</h2>
<a href="../../../main.jsp">Return to main page</a>

<f:view>
  <h:form id="form1" style="background:white; font-family: Verdana; color:gray">
    <%@ include file="demography_core.xhtml" %>
  </h:form>


</f:view>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<html>
<head>
  <title></title>
</head>

<body>
<f:view>
  <h:form>
    <a href="../../../../SupportPages.jsp">back</a>

    <div id="menu">

    <%@ include file="QKS340_core.xhtml" %>
  </h:form>
  </div>

  <div id="content">
    <a4j:outputPanel id="opContent">
      <h:form rendered="#{QKS340.rendered}">
        <%@ include file="QKS340Content_core.xhtml" %>
      </h:form>
    </a4j:outputPanel>
  </div>
</f:view>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html>
<head>
  <title>SuggestionField with a4j:loadBundle</title>
</head>

<body>
<f:view locale="#{TabSetLocaleChangerBean.selectedLocale}">
  <h:form id="form1">
    <%@ include file="suggestionfield_core.xhtml" %>
  </h:form>
</f:view>

</body>
</html>
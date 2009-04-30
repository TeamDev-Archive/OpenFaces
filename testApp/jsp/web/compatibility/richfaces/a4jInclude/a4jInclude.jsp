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
  <table>
    <tr>
      <td valign="top" width="200px">
        <h:form>
          <%@ include file="a4jInclude_core.xhtml" %>
        </h:form>
      </td>
      <td valign="top">
        <a4j:outputPanel id="CONTENT">
          <a4j:include id="a4jInclude" viewId="#{A4jIncludeBean.includedPage}"/>
        </a4j:outputPanel>
      </td>
    </tr>
  </table>
</f:view>

</body>
</html>
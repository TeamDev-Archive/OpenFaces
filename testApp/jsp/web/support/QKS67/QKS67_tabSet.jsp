<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head><title>TabbedSet Test</title></head>

<body>
<f:view>
  <h:form>
     <a href="main.jsf">Back to the main page</a>
  <br/>

		<h:outputText value="Tab Set Test"/>
		<o:tabSet onchange="document.forms[0].submit()"
					 selectedIndex="#{TabSetListBean.selectedIndex}">
			<o:tabSetItem>
				<h:outputText value="Tab 1"/>
			</o:tabSetItem>
			<o:tabSetItem>
				<h:outputText value="Tab 2"/>
			</o:tabSetItem>
		</o:tabSet>
  </h:form>
</f:view>

</body>
</html>
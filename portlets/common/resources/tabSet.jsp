<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<f:view>
  <h:form>
    <!--<link rel='stylesheet' type='text/css' id='main_css'-->
    <!--href='/PortletsTestApp/openFacesResources/org/openfaces/renderkit/default.css'/>-->
    <h:commandButton action="sbmt" value="Submit"/>
    <h:commandLink value="back" action="index"/>
    <o:tabSet emptySpaceStyle="background: beige;"
              rolloverSelectedTabStyle="border: 2px dashed blue;"
              tabStyle="background: azure; border: 1px dotted darkgreen;"
              rolloverTabStyle="background: brown; font-weight: bold; border: 2px dotted green;"
              selectedTabStyle="background: pink; border: 1px solid black;"
              style="border: 3px solid lightGreen;">
      <o:tabSetItem>
        <h:outputText value="Client"/>
      </o:tabSetItem>
      <o:tabSetItem>
        <h:outputText value="Server"/>
      </o:tabSetItem>
    </o:tabSet>
    <o:tabSet alignment="bottomOrRight" placement="left" style="height:100px;">
      <o:tabSetItem>
        <h:outputText value="Client"/>
      </o:tabSetItem>
      <o:tabSetItem>
        <h:outputText value="Server"/>
      </o:tabSetItem>
    </o:tabSet>
  </h:form>
</f:view>

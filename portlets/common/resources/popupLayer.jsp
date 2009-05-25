<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<f:view>
  <h:form id="form1">
    <!--<link rel='stylesheet' type='text/css' id='main_css'-->
    <!--href='/PortletsTestApp/openFacesResources/org/openfaces/renderkit/default.css'/>-->
    <h:commandButton action="sbmt" value="Submit"/>
    <h:commandLink value="back" action="index"/>
    <br/>
    <h:commandButton value="Show styled draggable popupLayer" type="button" onclick="O$('form1:pl1').show(); return false;"/>
    <br/>
    <o:popupLayer id="pl1"
                  hideOnOuterClick="true"
                  draggable="true"
                  height="200px"
                  width="500px"
                  hidingTimeout="30000"
                  modal="false"
                  style="color: orange; background-color: yellow; border: 2px solid red;"
                  rolloverStyle="color: white; background-color: blue; border: 2px solid purple;">
      <h:outputText value="Text in the PopupLayer component"/>
      <f:verbatim>
        hideOnOuterClick="true"<br/>
        draggable="true"<br/>
        height="200px" <br/>
        width="500px" <br/>
        hidingTimeout="30000" <br/>
        modal="false" <br/>
        styles
      </f:verbatim>
    </o:popupLayer>
    <o:popupLayer id="pl2"
                  draggable="true"
                  height="250px"
                  width="250px"
                  modal="false"
                  visible="true"
                  style="border: 4px dotted blue;"
                  top="100"
                  left="100">
      <h:outputText value="Visible by load PopupLayer"/>
      <f:verbatim>
        draggable="true" <br/>
        height="250px" <br/>
        width="250px" <br/>
        modal="false" <br/>
        visible="true" <br/>
        top="100" <br/>
        left="100" <br/>
      </f:verbatim>
      <h:commandButton value="Hide popupLayer" type="button" onclick="O$('form1:pl2').hide(); return false;"/>
    </o:popupLayer>
    <h:commandButton value="Show centered and modal popup" type="button" onclick="O$('form1:pl3').showCentered(); return false;"/>
    <o:popupLayer id="pl3"
                  draggable="true"
                  height="200px"
                  width="500px"
                  modal="true"
                  modalLayerStyle="background-color:orange;">
      <h:outputText value="Text in the PopupLayer component"/>
      <f:verbatim>
        draggable="true" <br/>
        height="200px" <br/>
        width="500px" <br/>
        modal="true"<br/>
        modalLayerStyle="background-color:orange;"<br/>
      </f:verbatim>
      <h:commandButton value="Hide popupLayer" type="button" onclick="O$('form1:pl3').hide(); return false;"/>
    </o:popupLayer>
  </h:form>
</f:view>

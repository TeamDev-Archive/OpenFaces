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
    <o:twoListSelection
            headerStyle="font-family: Tahoma; font-weight: bold; font-size: 12px; color: blue;"
            listStyle="background: buttonface; font-family: 'ParkAvenue BT' Tahoma; color: purple; font-size: 20px; "
            value="#{util.selectedItems}"
            leftListboxHeader="Available items"
            rightListboxHeader="Selected items"
            allowSorting="true"
            onremove="alert('onremove');"
            addText="Add ->"
            removeText="<- Remove"
            addAllText="Add All ->>"
            removeAllText="<<- Remove All"
            moveUpText="^ Move Up"
            moveDownText="v Move Down"
            buttonStyle="font-family: Arial; font-size: 15pt; color: DarkGreen; width: 170px"
            >
      <f:selectItems value="#{util.availableItems}"/>
    </o:twoListSelection>
  </h:form>
</f:view>

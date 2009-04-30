<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://openfaces.org/" prefix="o"%>
<%@taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a4j"%>

<h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="windowPopupContent">
	<a4j:outputPanel id="a4jNewUser">
		<h:panelGrid columns="2" cellpadding="2" cellspacing="0">
		<h:outputText value="Name: " styleClass="bold_text"/>
		<h:inputText id="name" value="#{QKS545.name}" required="true" size="30"/>
		<h:outputText value=" "/>
		<h:outputText value=" "/>
		</h:panelGrid>
	</a4j:outputPanel>
</h:panelGrid>
<h:panelGrid columns="2" cellpadding="0" cellspacing="0" styleClass="windowPopupFooter">
	<h:panelGroup style="text-align: left; width:100%;">
		<a4j:commandButton id="newUserButton" action="#{QKS545.saveNewUser}" value="Save" styleClass="button" reRender="a4jNewUser, a4jInfoBoxNewUser, a4jDeleteUsers"/>
	</h:panelGroup>
	<h:panelGroup style="text-align: right; width:100%;">
		<a4j:outputPanel id="a4jInfoBoxNewUser">
			<h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="successPanelGrid" rendered="#{QKS545.successFlag}">
				<h:outputText value="Success" styleClass="success_text"/>
			</h:panelGrid>
			<h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="errorPanelGrid" rendered="#{QKS545.errorFlag}">
				<h:outputText value="#{QKS545.errorText}" styleClass="warning_text"/>
			</h:panelGrid>
		</a4j:outputPanel>
	</h:panelGroup>
</h:panelGrid>


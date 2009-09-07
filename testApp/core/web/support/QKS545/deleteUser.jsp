<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://openfaces.org/" prefix="o"%>
<%@taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a4j"%>

<h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="windowPopupContent">
	<a4j:outputPanel id="a4jDeleteUsers">
		<o:dataTable id="DeleteUsersDataTable"
					 var="delUser"
					 value="#{QKS545.data}"
					 rowKey="#{delUser.name}"
					 style="width: 100%;">
			<o:checkboxColumn id="checked" rowDatas="#{QKS545.checkedUsers}" >
				<f:facet name="header">
					<o:selectAllCheckbox/>
				</f:facet>
			</o:checkboxColumn>
		    <o:column>
				  <f:facet name="header">
			    	<h:outputText value="Name" />
			    </f:facet>
          <f:facet name="subHeader">
            <o:inputTextFilter expression="#{delUser.name}"/>
          </f:facet>
			    <h:outputText value="#{delUser.name}" />
		    </o:column>
		</o:dataTable>
	</a4j:outputPanel>
</h:panelGrid>
<h:panelGrid columns="2" cellpadding="0" cellspacing="0" styleClass="windowPopupFooter">
	<h:panelGroup style="text-align: left; width:100%;">
		<a4j:commandButton id="deleteUserButton" action="#{QKS545.deleteUsers}" value="Delete" styleClass="button" reRender="a4jDeleteUsers, a4jInfoBoxDeleteUsers, groupDetails, a4jAdminVehiclesTable, a4jAdminUsersTable"/>
	</h:panelGroup>
	<h:panelGroup style="text-align: right; width:100%;">
		<a4j:outputPanel id="a4jInfoBoxDeleteUsers">
			<h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="successPanelGrid" rendered="#{QKS545.successFlag}">
				<h:outputText value="Success" styleClass="success_text"/>
			</h:panelGrid>
			<h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="errorPanelGrid" rendered="#{QKS545.errorFlag}">
				<h:outputText value="#{QKS545.errorText}" styleClass="warning_text"/>
			</h:panelGrid>
		</a4j:outputPanel>
	</h:panelGroup>
</h:panelGrid>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>
<%@taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a4j"%>

<h:panelGrid columns="2" cellpadding="0" cellspacing="0" styleClass="windowPopupHeader">
		<h:panelGroup style="text-align: left; width:100%;">
			<h:outputText value="Admin - Users in group"/>
		</h:panelGroup>
		<h:panelGroup style="text-align: right; width:100%;">
			<h:graphicImage value="/images/popupWindow/close.png" onclick="O$('addUserForm:userListPopup').hide();" style="cursor: pointer;"/>
		</h:panelGroup>
	</h:panelGrid>
	<h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="windowPopupContent">
		<o:dataTable id="UserListDataTable"
					 var="user"
					 value="#{QKS524.person}"
					 rowKey="#{user.name}">
		  <o:checkboxColumn id="checked" rowDatas="#{QKS524.checkedUsers}">
		    <f:facet name="header">
		      <o:selectAllCheckbox />
		    </f:facet>
		  </o:checkboxColumn>
	    <o:column filterExpression="#{user.name}"
	              filterKind="searchField">
	      <f:facet name="header">
	        <h:outputText value="Name" />
	      </f:facet>
	      <h:outputText value="#{user.name}" />
	    </o:column>
		</o:dataTable>
		<a4j:commandButton action="#{QKS524.addUsers}" value="Reload" styleClass="button" reRender="UserListDataTable"/>
	</h:panelGrid>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://openfaces.org/" prefix="o"%>

<f:loadBundle basename="messages" var="msg" />

<o:popupLayer id="ownerSearchPopup" left="200" top="110" width="675px"
	 modal="true" styleClass="PopupField" >

	<h:panelGrid border="0" cellpadding="10" cellspacing="0" width="93%"
		columns="1">




		<h:panelGrid border="0" cellpadding="6" cellspacing="0" columns="1">
			<h:panelGroup>
				<h:commandButton value="Close" immediate="true"
					onclick="O$(ownerSearchPopupId).hide(); return false;"
					image="/images/close.gif" />
			</h:panelGroup>
		</h:panelGrid>

	</h:panelGrid>
</o:popupLayer>


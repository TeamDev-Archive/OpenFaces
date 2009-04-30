<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://openfaces.org/" prefix="o"%>
<%@taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a4j"%>

<f:verbatim>
	<a4j:commandButton 	action="#{UserBean.resetFlags}"
						type="button"
						value="Admin Users"
						styleClass="button"
						onclick="O$('loadingForm:loadingPopup').showCentered();"
						oncomplete="O$('loadingForm:loadingPopup').hide();
									O$('adminUserPopup').showCentered();"
						reRender="a4jAdminUsersTabbedPane"/>
	<a4j:commandButton 	action="#{GroupDataTableBean.resetFlags}"
						type="button"
						value="Manage Users"
						styleClass="button"
						onclick="O$('loadingForm:loadingPopup').showCentered();"
						oncomplete="O$('loadingForm:loadingPopup').hide();
									O$('addUserForm:userListPopup').showCentered();"
						reRender="a4jAddUser, a4jInfoBoxAddUser"/>
</f:verbatim>
<a4j:outputPanel id="a4jAdminUsersTable">

</a4j:outputPanel>
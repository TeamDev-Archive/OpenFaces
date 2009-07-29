<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://openfaces.org/" prefix="o" %>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Email</title>
    <link rel="stylesheet" href="css/style.css" type="text/css" media="screen,projection"/>
    <link rel="shortcut icon" href="favicon.ico" type="image/vnd.microsoft.icon"/>
    <link rel="icon" href="favicon.ico" type="image/vnd.microsoft.icon"/>
		<link rel="stylesheet" href="treetable.css" type="text/css" media="screen,projection"/>
		<link rel="stylesheet" href="css/mail.css" type="text/css" media="screen,projection"/>
		<script src="treetable.js" type="text/javascript"></script>
		<script type="text/javascript">
			function addSendButton(ed) {
				ed.addButton("sendMail", {
					title: "Send",
					image: "images/mail/send.png",
					onclick: function() {
						O$("form:editor").onsend();
					}
				});
			}
			function saveMessage(ed) {
				O$("form:editor").onsave2();
			}
		</script>
	</head>
<body>
<f:view>
    <h:form id="form">
    	<o:window id="newMessage" modal="true" width="50%" height="50%">
    		<f:facet name="caption"><h:outputText value="New Message"/></f:facet>
    		<table style="width:100%;height:100%">
    			<tr>
    				<td><h:outputText value="To: "/></td>
    				<td width="100%"><h:inputText value="#{EMailBean.messageAddress}" style="width:90%"/></td>
    			</tr>
    			<tr>
    				<td><h:outputText value="Subject: "/></td>
    				<td width="100%"><h:inputText value="#{EMailBean.messageSubject}" style="width:90%"/></td>
    			</tr>
    			<tr>
    				<td height="100%" colspan="2">
			    		<rich:editor id="editor" viewMode="visual" theme="advanced" value="#{EMailBean.messageText}" style="height:100%">
			    			<f:param name="plugins" value="save,table"/>
			
			                <f:param name="theme_advanced_buttons1"
			                         value="sendMail,save,|,cut,copy,paste,|,undo,redo,|,link,unlink,anchor,image"/>
			                <f:param name="theme_advanced_buttons2"
			                         value="fontselect,fontsizeselect,forecolor,|,bold,italic,underline,|,justifyleft,justifycenter,justifyright,|,bullist,numlist,outdent,indent"/>
			    			<f:param name="theme_advanced_buttons3" value="tablecontrols,|,hr,removeformat,visualaid,|,sub,sup"/>
			    			<f:param name="theme_advanced_toolbar_location" value="top"/>
			    			<f:param name="theme_advanced_toolbar_align" value="left"/>
			
			    			<f:param name="width" value="100%"/>
			    			<f:param name="height" value="100%"/>
			    			
			    			<f:param name="setup" value="addSendButton"/>
			    			
			    			<f:param name="save_onsavecallback" value="saveMessage"/>
			    		</rich:editor>
    				</td>
    			</tr>
    		</table>
    	</o:window>
		<o:reloadComponents for="editor" actionListener="#{EMailBean.saveMailListener}" disableDefault="true" event="onsave2" componentIds="emailsTreeTable" onajaxend="O$('form:newMessage').hide();"/>
		<o:reloadComponents for="editor" actionListener="#{EMailBean.sendMailListener}" disableDefault="true" event="onsend" componentIds="emailsTreeTable" onajaxend="O$('form:newMessage').hide();"/>

    	<o:borderLayoutPanel>
    		<o:sidePanel alignment="top" resizable="false" collapsible="false" size="72px">
                <rich:toolBar height="64px" styleClass="navigation-panel-mail" itemSeparator="none">
                	<rich:toolBarGroup style="width:215px">
	    				<h:graphicImage url="images/logo.png" alt="OpenFaces"/>
	    				<h:outputText value="Email" styleClass="caption"/>
                	</rich:toolBarGroup>
                	<rich:toolBarGroup styleClass="buttons">
	                    <h:outputLink value="#" onclick="O$('form:newMessage').showCentered(); return false;">
		    				<h:graphicImage url="images/mail/navigation-mail-new.png"/>
		    				<h:outputText value="New message"/>
	                    </h:outputLink>
	                    <h:outputLink value="#">
		    				<h:graphicImage url="images/mail/navigation-mail-reply.png"/>
		    				<h:outputText value="Reply"/>
	                    </h:outputLink>
	                    <h:outputLink value="#">
		    				<h:graphicImage url="images/mail/navigation-mail-forward.png"/>
		    				<h:outputText value="Forward"/>
	                    </h:outputLink>
		    		</rich:toolBarGroup>
                	<rich:toolBarGroup location="right" styleClass="link">
		    			<h:outputLink value="calendar.jsf" styleClass="calendar">
		    				<h:graphicImage value="images/titles/navigation-calendar.png"/>
		    			</h:outputLink>
		    			<h:outputLink value="tasks.jsf" styleClass="tasks">
		    				<h:graphicImage value="images/titles/navigation-tasks.png"/>
		    			</h:outputLink>
		    		</rich:toolBarGroup>
                </rich:toolBar>
    		</o:sidePanel>
    		<o:sidePanel alignment="left" size="230px" contentClass="Sidebar" >
                <rich:tree id="emailFolders" switchType="ajax" ajaxSubmitSelection="true" styleClass="email-folders">
                    <rich:recursiveTreeNodesAdaptor roots="#{EMailBean.drafts}" var="folder" nodes="#{folder.children}" >
                        <rich:treeNode data="#{folder}" nodeSelectListener="#{EMailBean.nodeSelectListener}" reRender="emailsTreeTable">
							<h:outputText value="Drafts" />
							<f:facet name="icon">
								<h:graphicImage url="/images/mail/sidebar-mail-drafts.png" styleClass="icon" />
							</f:facet>
							<f:facet name="iconLeaf">
								<h:graphicImage url="/images/mail/sidebar-mail-drafts.png" styleClass="icon" />
							</f:facet>
                        </rich:treeNode>
                    </rich:recursiveTreeNodesAdaptor>
                    <rich:recursiveTreeNodesAdaptor roots="#{EMailBean.inbox}" var="folder" nodes="#{folder.children}">
                        <rich:treeNode data="#{folder}" nodeSelectListener="#{EMailBean.nodeSelectListener}" reRender="emailsTreeTable">
                        	<h:outputText value="#{folder.name}"/>
		                   	<f:facet name="icon">
		                   		<h:graphicImage url="/images/mail/sidebar-mail-#{folder.subfolder ? 'folder' : 'inbox'}.png" styleClass="icon"/>
		                   	</f:facet>
		                   	<f:facet name="iconLeaf">
		                   		<h:graphicImage url="/images/mail/sidebar-mail-#{folder.subfolder ? 'folder' : 'inbox'}.png" styleClass="icon"/>
		                   	</f:facet>
                        </rich:treeNode>
                    </rich:recursiveTreeNodesAdaptor>
                    <rich:recursiveTreeNodesAdaptor roots="#{EMailBean.sent}" var="folder" nodes="#{folder.children}" >
                        <rich:treeNode data="#{folder}" nodeSelectListener="#{EMailBean.nodeSelectListener}" reRender="emailsTreeTable">
							<h:outputText value="Sent" />
							<f:facet name="icon">
								<h:graphicImage url="/images/mail/sidebar-mail-sent.png" styleClass="icon" />
							</f:facet>
							<f:facet name="iconLeaf">
								<h:graphicImage url="/images/mail/sidebar-mail-sent.png" styleClass="icon" />
							</f:facet>
                        </rich:treeNode>
                    </rich:recursiveTreeNodesAdaptor>
					<f:facet name="iconCollapsed">
                   		<h:graphicImage url="/images/mail/sidebar-tree-collapsed.png" styleClass="icon-collapsed"/>
                   	</f:facet>
                   	<f:facet name="iconExpanded">
                   		<h:graphicImage url="/images/mail/sidebar-tree-opened.png" styleClass="icon-expanded"/>
                   	</f:facet>
                </rich:tree>

	            <div class="SidebarFooter">
	                <a class="ButtonPageSource" onclick="O$('form:pageSource').showCentered(); return false">
	                    <span>View page source</span>
	                </a>
	
	                <div class="Copyright">
	                    <p>&copy;&nbsp;TeamDev Ltd. | OpenFaces.org</p>
	                </div>
	            </div>
    		</o:sidePanel>
            <o:treeTable id="emailsTreeTable"
                         var="email"
                         style="width:100%;border:1px solid white;table-layout:fixed;max-height:300px;overflow:scroll"
                         expansionState="allExpanded"
                         sortAscending="#{EMailBean.selection.sortAscending}"
                         sortColumnId="#{EMailBean.selection.sortedColumnId}"
                         rolloverRowStyle="background: #b6cfec;"
                         horizontalGridLines="1px solid #eef0f2"
                         headerRowClass="TableHeader"
                         sortedAscendingImageUrl="images/treetable/sort_a.gif"
                         sortedDescendingImageUrl="images/treetable/sort_d.gif"
                         sortedColumnHeaderStyle="background: url('images/treetable/header_selected.gif') repeat-x;"
                         sortedColumnClass="SortedColumn"
                         focusedStyle="border: 1px dotted black !important;"
                         columnIdVar="columnId" nodeLevelVar="level">
                <o:row condition="#{level == 0}" style="background: white !important;">
                    <o:cell span="6" styleClass="category_name">
                        <h:outputText value="#{email}" style="padding-left: 5px;"/>
                    </o:cell>
                </o:row>
                <o:singleNodeSelection
                        style="background:url('images/treetable/selection.gif') repeat-x #168aff !important; color: white !important;"
                        nodeData="#{EMailBean.selection.selectedEMail}" >
                </o:singleNodeSelection>
                <o:dynamicTreeStructure nodeChildren="#{EMailBean.selection.EMailsTreeChildren}"/>
                <o:treeColumn id="importance"
                              expandedToggleImageUrl="images/treetable/expanded4.gif"
                              collapsedToggleImageUrl="images/treetable/collapsed4.gif"
                              width="32px"
                              headerStyle="text-align: right !important;"
                              sortingExpression="#{EMailBean.selection.sortByImportance}"
                              levelIndent="10px">
                    <f:facet name="header">
                        <h:graphicImage url="images/treetable/sort_prioity.gif"/>
                    </f:facet>
                    <h:graphicImage url="#{EMailBean.selection.importanceIcon}"/>
                </o:treeColumn>
                <o:column id="attachment"
                          width="32px"
                          style="text-align: center;"
                          headerStyle="text-align: center !important;"
                          sortingExpression="#{EMailBean.selection.sortByAttachmentExpression}">
                    <f:facet name="header">
                        <h:graphicImage url="images/treetable/attachment.gif"/>
                    </f:facet>
                    <h:graphicImage url="images/treetable/attachment.gif"
                                    rendered="#{email.hasAttachment}"/>
                </o:column>
                <o:column width="16px"
                          style="text-align: center;">
                    <f:facet name="header">
                        <h:outputText value=""/>
                    </f:facet>
                    <h:graphicImage url="images/treetable/letter.gif"/>
                </o:column>
                <o:column id="sender"
                          width="21%"
                          style="padding-left: 5px;"
                          sortingExpression="#{EMailBean.selection.sortBySenderExpression}">
                    <f:facet name="header">
                        <h:outputText value="#{EMailBean.folder.incoming ? 'From' : 'To'}"/>
                    </f:facet>
                    <h:outputText value="#{email.sender}" style="margin-left: 5px;"/>
                </o:column>
                <o:column id="subject"
                          width="61%"
                          bodyStyle="text-align: left;"
                          style="padding-left: 5px;"
                          sortingExpression="#{EMailBean.selection.sortBySubjectExpression}">
                    <f:facet name="header">
                        <h:outputText value="Subject"/>
                    </f:facet>
                    <h:outputText value="#{email.subject}" style="margin-left: 5px;"/>
                </o:column>
                <o:column id="date"
                          width="16%"
                          style="padding-left: 5px;"
                          sortingExpression="#{EMailBean.selection.sortByDateExpression}">
                    <f:facet name="header">
                        <h:outputText value="#{EMailBean.folder.incoming ? 'Received' : 'Sent'}"/>
                    </f:facet>
                    <%--h:outputText value="#{email.receivedDate}" style="margin-left: 5px;"
                                  converter="#{EMailBean.selection.receivedDateConverter}"/--%>
                </o:column>
            </o:treeTable>
            <o:reloadComponents for="emailsTreeTable" event="onchange" componentIds="emailDetails"
                                disableDefault="false"/>
            <h:panelGroup layout="block" id="emailDetails">
            	<h:panelGroup rendered="#{EMailBean.selection.selectedEMail != null}">
	            	<div><h:outputText value="Subject: #{EMailBean.selection.selectedEMail.subject}" /></div>
	            	<div><h:outputText value="From: #{EMailBean.selection.selectedEMail.sender}" /></div>
                    <h:panelGroup layout="block" style="overflow:scroll; border-top:solid 1px black;"/>
            	</h:panelGroup>
            </h:panelGroup>
    	</o:borderLayoutPanel>

        <o:popupLayer id="pageSource"
                      styleClass="SourceView"
                      modal="true"
                      modalLayerClass="SourceViewModalLayer">
            <h:commandButton onclick="O$('form:pageSource').hide(); return false" value="hide"/>
            <rich:insert src="/mail.jsp" highlight="xhtml"/>
        </o:popupLayer>

    </h:form>
</f:view>
</body>
</html>
/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.richfaces.demo.beans;

import java.util.Date;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.richfaces.component.html.HtmlTreeNode;
import org.richfaces.event.NodeSelectedEvent;

public final class EMailBean {

	private final EMailFolderBean inbox =
		new EMailFolderBean("Inbox", false, new EMailFolderBean[] {
				new EMailFolderBean("Dev", true,  true),
				new EMailFolderBean("Entertainment", true, true),
		}, true);
	private final EMailFolderBean sent = new EMailFolderBean("Sent", false, false);
	private final EMailFolderBean drafts = new EMailFolderBean("Drafts", false, false);

	private EMailFolderBean selectedFolder = inbox;

	private String messageText = "";
	private String messageAddress = "";
	private String messageSubject = "";

	public void nodeSelectListener(NodeSelectedEvent event) {
		HtmlTreeNode node = (HtmlTreeNode) event.getComponent();
		selectedFolder = (EMailFolderBean) node.getData();
	}
	
	public void saveMailListener(AjaxBehaviorEvent event) {
		drafts.getContent().addEMail(new EMail(messageAddress, messageSubject, new Date(), EMailImportance.NORMAL, false, "Drafts", messageText));
	}
	
	public void sendMailListener(AjaxBehaviorEvent event) {
		sent.getContent().addEMail(new EMail(messageAddress, messageSubject, new Date(), EMailImportance.NORMAL, false, "Sent", messageText));
	}
	
	public EMailFolderBean getFolder() {
		return selectedFolder;
	}
	
	public EMailsTreeTableBean getSelection() {
		return selectedFolder.getContent();
	}
	
	public String getMessageAddress() {
		return messageAddress;
	}
	
	public void setMessageAddress(String messageAddress) {
		this.messageAddress = messageAddress;
	}
	
	public String getMessageSubject() {
		return messageSubject;
	}
	
	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}
	
	public String getMessageText() {
		return messageText;
	}
	
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	
	public EMailFolderBean getDrafts() {
		return drafts;
	}
	
	public EMailFolderBean getInbox() {
		return inbox;
	}
	
	public EMailFolderBean getSent() {
		return sent;
	}
	
	public Object getSelectedEMail() {
		return selectedFolder.getContent().getSelectedEMail();
	}
	
	public void setSelectedEMail(Object selectedEMail) {
		selectedFolder.getContent().setSelectedEMail(selectedEMail);
	}
}

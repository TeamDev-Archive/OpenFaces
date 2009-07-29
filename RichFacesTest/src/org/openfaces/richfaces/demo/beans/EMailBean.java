package org.openfaces.richfaces.demo.beans;

import java.util.Date;

import javax.faces.event.ActionEvent;

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
	
	public void saveMailListener(ActionEvent event) {
		drafts.getContent().addEMail(new EMail(messageAddress, messageSubject, new Date(), EMailImportance.NORMAL, false, "Drafts"));
	}
	
	public void sendMailListener(ActionEvent event) {
		sent.getContent().addEMail(new EMail(messageAddress, messageSubject, new Date(), EMailImportance.NORMAL, false, "Sent"));
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
}

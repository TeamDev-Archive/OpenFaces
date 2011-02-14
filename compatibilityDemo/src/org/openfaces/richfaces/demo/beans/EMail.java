/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.richfaces.demo.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Darya Shumilina
 */
public class EMail implements Serializable {
    private final String sender;
    private final String subject;
    private final Date receivedDate;
    private final EMailImportance importance;
    private final boolean hasAttachment;
    private final String path;
    private final String content;

    public EMail(String sender, String subject, Date receivedDate, EMailImportance importance, boolean hasAttachment, String path, String content) {
    	this.sender = sender;
    	this.subject = subject;
    	this.receivedDate = receivedDate;
    	this.importance = importance;
    	this.hasAttachment = hasAttachment;
    	this.path = path;
    	this.content = content;
    }
    
    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public EMailImportance getImportance() {
        return importance;
    }

    public boolean getHasAttachment() {
        return hasAttachment;
    }

    public String getPath() {
    	return path;
    }
    
    public String getContent() {
    	return content;
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EMail eMail = (EMail) o;

        if (hasAttachment != eMail.hasAttachment) return false;
        if (importance != null ? !importance.equals(eMail.importance) : eMail.importance != null) return false;
        if (receivedDate != null ? !receivedDate.equals(eMail.receivedDate) : eMail.receivedDate != null)
            return false;
        if (sender != null ? !sender.equals(eMail.sender) : eMail.sender != null) return false;
        if (subject != null ? !subject.equals(eMail.subject) : eMail.subject != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (receivedDate != null ? receivedDate.hashCode() : 0);
        result = 31 * result + (importance != null ? importance.hashCode() : 0);
        result = 31 * result + (hasAttachment ? 1 : 0);
        return result;
    }
}
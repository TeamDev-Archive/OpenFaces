/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.screenshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class ForumMessage implements Serializable {
    private static int messagesCreated;
    private static Map messagesById = new HashMap();

    private Object id = "message" + messagesCreated++;
    private String subject;
    private Date date;
    private String author;
    private String mail;
    private List replies;

    public static void setMessageCreatedNull() {
        messagesCreated = 0;
    }

    {
        messagesById.put(id, this);
    }

    public ForumMessage(String subject, Date date, String author, String mail, List replies) {
        this.subject = subject;
        this.date = date;
        this.author = author;
        this.mail = mail;
        this.replies = replies != null ? new ArrayList(replies) : null;
    }

    public Object getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public Date getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getMail() {
        return mail;
    }

    public List getReplies() {
        return replies;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setReplies(List replies) {
        this.replies = replies;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public static ForumMessage messageById(String msgId) {
        return (ForumMessage) messagesById.get(msgId);
    }

    public void addReply(ForumMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("message shouldn't be null");
        }
        if (replies == null) {
            replies = new ArrayList();
        }
        replies.add(message);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ForumMessage that = (ForumMessage) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    public boolean hasReplies() {
        return replies != null;

    }
}

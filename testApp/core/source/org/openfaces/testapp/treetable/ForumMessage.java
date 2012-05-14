/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.treetable;

import org.openfaces.testapp.datatable.User;

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
    private static Map<Object, ForumMessage> messagesById = new HashMap<Object, ForumMessage>();
    private Object id = "messagem" + messagesCreated++;
    private String subject;
    private Date date;
    private User author;
    private List replies;
    private ForumMessage parent;

    {
        messagesById.put(id, this);
    }

    public ForumMessage(String subject, Date date, User author, List replies) {
        this.subject = subject;
        this.date = date;
        this.author = author;
        this.replies = replies != null ? new ArrayList(replies) : null;
        if (replies != null)
            for (Object reply : replies) {
                ForumMessage message = (ForumMessage) reply;
                message.setParent(this);
            }
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

    public User getAuthor() {
        return author;
    }

    public List getReplies() {
        return replies;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public static ForumMessage messageById(String msgId) {
        return messagesById.get(msgId);
    }

    public void addReply(ForumMessage message) {
        if (message == null)
            throw new IllegalArgumentException("message shouldn't be null");
        if (replies == null)
            replies = new ArrayList();
        replies.add(message);
        message.setParent(this);
    }

    public void removeReply(ForumMessage message) {
        if (message == null)
            throw new IllegalArgumentException("message shouldn't be null");
        if (replies == null || !replies.contains(message))
            throw new IllegalArgumentException("There are no replies to this message to remove from");
        if (!replies.remove(message))
            throw new IllegalArgumentException("There's no such reply to remove for this message");
    }

    public ForumMessage getParent() {
        return parent;
    }

    public void setParent(ForumMessage parent) {
        this.parent = parent;
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

}

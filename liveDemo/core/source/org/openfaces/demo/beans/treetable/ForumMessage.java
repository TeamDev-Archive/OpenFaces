/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.treetable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumMessage implements Serializable {
    private static int messagesCreated;
    private static Map<Object, ForumMessage> messagesById = new HashMap<Object, ForumMessage>();

    private Object id = "message" + messagesCreated++;
    private String subject;
    private Date date;
    private String author;
    private List<ForumMessage> replies;

    {
        messagesById.put(id, this);
    }

    public ForumMessage(String subject, Date date, String author, List<ForumMessage> replies) {
        this.subject = subject;
        this.date = date;
        this.author = author;
        this.replies = replies != null ? new ArrayList<ForumMessage>(replies) : null;
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

    public List<ForumMessage> getReplies() {
        return replies;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean hasReplies() {
        return replies != null && replies.size() > 0;
    }

    public static ForumMessage messageById(String msgId) {
        return messagesById.get(msgId);
    }

    public void addReply(ForumMessage message) {
        if (message == null)
            throw new IllegalArgumentException("message shouldn't be null");
        if (replies == null)
            replies = new ArrayList<ForumMessage>();
        replies.add(message);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ForumMessage that = (ForumMessage) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }
}

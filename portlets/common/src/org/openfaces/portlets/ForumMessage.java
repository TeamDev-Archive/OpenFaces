package org.openfaces.portlets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumMessage implements Serializable {
  private static int ourMessagesCreated;
  private static Map myMessagesById = new HashMap();

  private Object myId = "message" + ForumMessage.ourMessagesCreated++;
  private String mySubject;
  private Date myDate;
  private String myAuthor;
  private List myReplies;

  {
    ForumMessage.myMessagesById.put(myId, this);
  }

  public ForumMessage(String subject, Date date, String author, List replies) {
    mySubject = subject;
    myDate = date;
    myAuthor = author;
    myReplies = replies != null ? new ArrayList(replies) : null;
  }

  public Object getId() {
    return myId;
  }

  public String getSubject() {
    return mySubject;
  }

  public Date getDate() {
    return myDate;
  }

  public String getAuthor() {
    return myAuthor;
  }

  public List getReplies() {
    return myReplies;
  }

  public void setSubject(String subject) {
    mySubject = subject;
  }

  public static ForumMessage messageById(String msgId) {
    return (ForumMessage) ForumMessage.myMessagesById.get(msgId);
  }

  public void addReply(ForumMessage message) {
    if (message == null)
      throw new IllegalArgumentException("message shouldn't be null");
    if (myReplies == null)
      myReplies = new ArrayList();
    myReplies.add(message);
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final ForumMessage that = (ForumMessage) o;

    if (myId != null ? !myId.equals(that.myId) : that.myId != null) return false;

    return true;
  }

  public int hashCode() {
    return (myId != null ? myId.hashCode() : 0);
  }
}

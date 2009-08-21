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
package org.openfaces.portlets;

import org.openfaces.component.table.ExpansionState;
import org.openfaces.component.table.SeveralLevelsExpanded;
import org.openfaces.util.FacesUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ForumTreeTableBean implements Serializable {
  private static List ourRootMessages = new ArrayList();
  private ExpansionState myForumTreeTableExpansionState = new SeveralLevelsExpanded(1);
  private ForumMessage mySelectedNode;
  private List mySelectedNodes = new ArrayList();

  static {
    String u1 = "William Green";
    String u2 = "Jane White";
    String u3 = "Chris Lee";
    String u4 = "Dean Genius";
    String u5 = "Christian Smile";
    String u6 = "Gary Efficient";
    ForumTreeTableBean.ourRootMessages.add(new ForumMessage("What's New in JDK 6?", ForumTreeTableBean.createDate(2005, 8, 23, 2, 51), u1, Arrays.asList(new Object[]{
            new ForumMessage("Re: What's New in JDK 6?", ForumTreeTableBean.createDate(2005, 8, 23, 20, 1), u5, Arrays.asList(new Object[]{
                    new ForumMessage("System-tray support", ForumTreeTableBean.createDate(2006, 1, 9, 2, 51), u4, Arrays.asList(new Object[]{
                            new ForumMessage("Re: System-tray support", ForumTreeTableBean.createDate(2006, 2, 14, 20, 1), u2, null)}))
            })),
            new ForumMessage("LCD-optimized text display", ForumTreeTableBean.createDate(2005, 10, 4, 20, 1), u6, Arrays.asList(new Object[]{
                    new ForumMessage("Re: LCD-optimized text display", ForumTreeTableBean.createDate(2005, 10, 7, 2, 51), u4, Arrays.asList(new Object[]{
                            new ForumMessage("Re: LCD-optimized text display", ForumTreeTableBean.createDate(2005, 11, 8, 20, 1), u5, null),
                            new ForumMessage("Re: LCD-optimized text display", ForumTreeTableBean.createDate(2006, 0, 11, 12, 36), u6, null)}))
            })),
            new ForumMessage("Re: What's New in JDK 6", ForumTreeTableBean.createDate(2005, 10, 11, 12, 36), u3, null),
            new ForumMessage("JSR 199: Java Compiler API", ForumTreeTableBean.createDate(2005, 10, 14, 12, 36), u6, Arrays.asList(new Object[]{
                    new ForumMessage("Re: JSR 199: Java Compiler API", ForumTreeTableBean.createDate(2006, 2, 27, 12, 36), u1, null)
            }))})));
    ForumTreeTableBean.ourRootMessages.add(new ForumMessage("Re: Math expressions parsing algorithm", ForumTreeTableBean.createDate(2006, 2, 12, 8, 13), u1, Arrays.asList(new Object[]{
            new ForumMessage("Re: Math expressions parsing algorithm", ForumTreeTableBean.createDate(2006, 2, 19, 9, 47), u4, Arrays.asList(new Object[]{
                    new ForumMessage("Re: Math expressions parsing algorithm", ForumTreeTableBean.createDate(2006, 3, 4, 1, 43), u3, Arrays.asList(new Object[]{
                            new ForumMessage("Re: Math expressions parsing algorithm", ForumTreeTableBean.createDate(2006, 3, 4, 0, 12), u2, null)}))}))})));
    ForumTreeTableBean.ourRootMessages.add(new ForumMessage("RE: Most popular design patterns", ForumTreeTableBean.createDate(2006, 0, 24, 2, 51), u4, Arrays.asList(new Object[]{
            new ForumMessage("Singleton", ForumTreeTableBean.createDate(2006, 0, 24, 20, 1), u5, Arrays.asList(new Object[]{
                    new ForumMessage("Re: Singleton", ForumTreeTableBean.createDate(2006, 2, 4, 21, 15), u2, null)})),
            new ForumMessage("Re: Most popular design patterns", ForumTreeTableBean.createDate(2006, 1, 30, 12, 36), u6, null)})));
    ForumTreeTableBean.ourRootMessages.add(new ForumMessage("What development methodology do you use?", ForumTreeTableBean.createDate(2006, 9, 4, 2, 51), u3, Arrays.asList(new Object[]{
            new ForumMessage("Re: What development methodology do you use?", ForumTreeTableBean.createDate(2006, 9, 7, 20, 1), u2, null),
            new ForumMessage("Re: What development methodology do you use?", ForumTreeTableBean.createDate(2006, 9, 10, 12, 36), u6, null)})));
    ForumTreeTableBean.ourRootMessages.add(new ForumMessage("Scaling an image", ForumTreeTableBean.createDate(2005, 6, 7, 8, 13), u2, Arrays.asList(new Object[]{
            new ForumMessage("Re: Scaling an image", ForumTreeTableBean.createDate(2005, 6, 8, 9, 47), u3, Arrays.asList(new Object[]{
                    new ForumMessage("Re: Scaling an image", ForumTreeTableBean.createDate(2005, 6, 17, 1, 43), u3, Arrays.asList(new Object[]{
                            new ForumMessage("Re: Scaling an image", ForumTreeTableBean.createDate(2005, 7, 3, 0, 12), u5, null)}))}))})));
    ForumTreeTableBean.ourRootMessages.add(new ForumMessage("Need to create a simple database web application", ForumTreeTableBean.createDate(2005, 11, 7, 8, 13), u4, Arrays.asList(new Object[]{
            new ForumMessage("Re: Need to create a simple database web application", ForumTreeTableBean.createDate(2005, 11, 8, 9, 47), u1, null)})));
  }

  public ForumTreeTableBean() {
  }

  public static Date createDate(int year, int month, int day, int hour, int minute) {
    Calendar instance = Calendar.getInstance();
    instance.set(Calendar.YEAR, year);
    instance.set(Calendar.MONTH, month);
    instance.set(Calendar.DAY_OF_MONTH, day);
    instance.set(Calendar.HOUR_OF_DAY, hour);
    instance.set(Calendar.MINUTE, minute);
    instance.set(Calendar.SECOND, 0);
    instance.set(Calendar.MILLISECOND, 0);
    return instance.getTime();
  }

  public ExpansionState getForumTreeTableExpansionState() {
    return myForumTreeTableExpansionState;
  }

  public void setForumTreeTableExpansionState(ExpansionState forumTreeTableExpansionState) {
    myForumTreeTableExpansionState = forumTreeTableExpansionState;
  }

  public List getNodeChildren() {
    ForumMessage message = (ForumMessage) FacesUtil.getRequestMapValue("message");
    return message != null ? message.getReplies() : ForumTreeTableBean.ourRootMessages;
  }

  public String getDateCategory() {
    ForumMessage message = (ForumMessage) FacesUtil.getRequestMapValue("message");
    Date date = message.getDate();
    return formatDate(date);
  }

  private String formatDate(Date date) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
    return simpleDateFormat.format(date);
  }


  public ForumMessage getSelectedNode() {
    return mySelectedNode;
  }

  public void setSelectedNode(ForumMessage selectedNode) {
    mySelectedNode = selectedNode;
  }

  public List getSelectedNodes() {
    return mySelectedNodes;
  }

  public void setSelectedNodes(List selectedNodes) {
    mySelectedNodes = selectedNodes;
  }

}

/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.screenshot;

import org.openfaces.util.Faces;
import org.openfaces.component.table.ExpansionState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TreeTableBean {
    private List<ForumMessage> rootMessages = new ArrayList<ForumMessage>();
    private ExpansionState forumTreeTableExpansionState;
    private List<ForumMessage> selectedNodeDatas = new ArrayList<ForumMessage>();
    private List selectedNodeDatas2 = new ArrayList();

    public TreeTableBean() {
        String u1 = "Will Green";
        String u2 = "Jane White";
        String u3 = "Chris Lee";
        String u4 = "Dean Genius";
        String u5 = "Jonh Smile";
        String u6 = "Gary Blackt";
        ForumMessage.setMessageCreatedNull();
        ForumMessage forumMessage = new ForumMessage("Re: System-tray support", createDate(2006, 2, 14, 20, 1), u2, null, null);
        rootMessages.add(new ForumMessage("JDK 6", createDate(2005, 8, 23, 2, 51), u1, null, Arrays.asList(
                    new ForumMessage("Re: JDK 6", createDate(2005, 8, 23, 20, 1), u5, null, Arrays.asList(
                            new ForumMessage("System-tray support", createDate(2006, 1, 9, 2, 51), u4, null, Arrays.asList(forumMessage)))),
                    new ForumMessage("LCD-optimized text display", createDate(2005, 10, 4, 20, 1), u6, null, Arrays.asList(
                            new ForumMessage("Re: LCD-optimized text display", createDate(2005, 10, 7, 2, 51), u4, null, Arrays.asList(
                                    new ForumMessage("Re: LCD-optimized text display", createDate(2005, 11, 8, 20, 1), u5, null, null),
                            new ForumMessage("Re: LCD-optimized text display", createDate(2006, 0, 11, 12, 36), u6, null, null))))),
                    new ForumMessage("Re: JDK 6", createDate(2005, 10, 11, 12, 36), u3, null, null),
                    new ForumMessage("JSR 199: Java Compiler API", createDate(2005, 10, 14, 12, 36), u6, null, Arrays.asList(
                            new ForumMessage("Re: JSR 199: Java Compiler API", createDate(2006, 2, 27, 12, 36), u1, null, null))))));
        rootMessages.add(new ForumMessage("Re:Parsing algorithm", createDate(2006, 2, 12, 8, 13), u1, null, Arrays.asList(
                    new ForumMessage("Re: Parsing algorithm", createDate(2006, 2, 19, 9, 47), u4, null, Arrays.asList(
                            new ForumMessage("Re: Parsing algorithm", createDate(2006, 3, 4, 1, 43), u3, null, Arrays.asList(
                                    new ForumMessage("Re: Parsing algorithm", createDate(2006, 3, 4, 0, 12), u2, null, null))))))));
        rootMessages.add(new ForumMessage("RE: Design patterns", createDate(2006, 0, 24, 2, 51), u4, null, Arrays.asList(
                    new ForumMessage("Singleton", createDate(2006, 0, 24, 20, 1), u5, null, Arrays.asList(
                            new ForumMessage("Re: Singleton", createDate(2006, 2, 4, 21, 15), u2, null, null))),
                    new ForumMessage("Re: Design patterns", createDate(2005, 1, 30, 12, 36), u6, null, null))));
        rootMessages.add(new ForumMessage("What methodology?", createDate(2006, 9, 4, 2, 51), u3, null, Arrays.asList(
                    new ForumMessage("Re: What methodology?", createDate(2006, 9, 7, 20, 1), u2, null, null),
                    new ForumMessage("Re: What methodology?", createDate(2006, 9, 10, 12, 36), u6, null, null))));
        rootMessages.add(new ForumMessage("Scaling an image", createDate(2005, 6, 7, 8, 13), u2, null, Arrays.asList(
                    new ForumMessage("Re: Scaling an image", createDate(2005, 6, 8, 9, 47), u3, null, Arrays.asList(
                            new ForumMessage("Re: Scaling an image", createDate(2005, 6, 17, 1, 43), u3, null, Arrays.asList(
                                    new ForumMessage("Re: Scaling an image", createDate(2005, 7, 3, 0, 12), u5, null, null))))))));
        rootMessages.add(new ForumMessage("Create an application", createDate(2005, 11, 7, 8, 13), u4, null, Arrays.asList(
                    new ForumMessage("Re: Create an application", createDate(2005, 11, 8, 9, 47), u1, null, null))));
        ForumMessage fm = new ForumMessage("This message should be selected", createDate(2006, 0, 11, 12, 36), u6, null, null);
        rootMessages.add(fm);
        selectedNodeDatas.add(fm);
        selectedNodeDatas.add(forumMessage);
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
        return forumTreeTableExpansionState;
    }

    public void setForumTreeTableExpansionState(ExpansionState forumTreeTableExpansionState) {
        this.forumTreeTableExpansionState = forumTreeTableExpansionState;
    }

    public List getRootMessages() {
        return rootMessages;
    }

    public List getNodeChildren() {
        ForumMessage message = (ForumMessage) Faces.var("message");
        return message != null
                ? message.getReplies()
                : rootMessages;
    }


    public String getDateCategory() {
        ForumMessage message = (ForumMessage) Faces.var("message");
        Date date = message.getDate();

        return formatDate(date);
    }

    private String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(date);
    }

    public List getSelectedNodeDatas2() {
        return selectedNodeDatas2;
    }

    public void setSelectedNodeDatas2(List selectedNodeDatas2) {
        this.selectedNodeDatas2 = selectedNodeDatas2;
    }


    public List<ForumMessage> getSelectedNodeDatas() {
        return selectedNodeDatas;
    }

    public void setSelectedNodeDatas(List<ForumMessage> selectedNodeDatas) {
        this.selectedNodeDatas = selectedNodeDatas;
    }

    private boolean booleanSwitch;

    public boolean isBooleanSwitch() {
        return booleanSwitch;
    }

    public void setBooleanSwitch(boolean booleanSwitch) {
        this.booleanSwitch = booleanSwitch;
    }

}

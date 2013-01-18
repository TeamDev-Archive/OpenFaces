/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.treetable;

import org.openfaces.util.Faces;
import org.openfaces.component.table.ExpansionState;
import org.openfaces.component.table.SeveralLevelsExpanded;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ForumTreeTableBean implements Serializable {
    private static List<ForumMessage> rootMessages = new ArrayList<ForumMessage>();

    static {
        String u1 = "William Green";
        String u2 = "Jane White";
        String u3 = "Chris Lee";
        String u4 = "Dean Genius";
        String u5 = "Christian Smile";
        String u6 = "Gary Efficient";
        rootMessages.add(new ForumMessage("What's New in JDK 6?", createDate(2007, 8, 23, 2, 51), u1, Arrays.asList(
                new ForumMessage("Re: What's New in JDK 6?", createDate(2007, 8, 23, 20, 1), u5, Arrays.asList(
                        new ForumMessage("System-tray support", createDate(2008, 1, 9, 2, 51), u4, Arrays.asList(
                                new ForumMessage("Re: System-tray support", createDate(2008, 2, 14, 20, 1), u2, null))))),
                new ForumMessage("LCD-optimized text display", createDate(2007, 10, 4, 20, 1), u6, Arrays.asList(
                        new ForumMessage("Re: LCD-optimized text display", createDate(2007, 10, 7, 2, 51), u4, Arrays.asList(
                                new ForumMessage("Re: LCD-optimized text display", createDate(2007, 11, 8, 20, 1), u5, null),
                                new ForumMessage("Re: LCD-optimized text display", createDate(2008, 0, 11, 12, 36), u6, null))))),
                new ForumMessage("Re: What's New in JDK 6", createDate(2007, 10, 11, 12, 36), u3, null),
                new ForumMessage("JSR 199: Java Compiler API", createDate(2007, 10, 14, 12, 36), u6, Arrays.asList(
                        new ForumMessage("Re: JSR 199: Java Compiler API", createDate(2008, 2, 27, 12, 36), u1, null))))));
        rootMessages.add(new ForumMessage("Re: Math expressions parsing algorithm", createDate(2008, 2, 12, 8, 13), u1, Arrays.asList(
                new ForumMessage("Re: Math expressions parsing algorithm", createDate(2008, 2, 19, 9, 47), u4, Arrays.asList(
                        new ForumMessage("Re: Math expressions parsing algorithm", createDate(2008, 3, 4, 1, 43), u3, Arrays.asList(
                                new ForumMessage("Re: Math expressions parsing algorithm", createDate(2008, 3, 4, 0, 12), u2, null))))))));
        rootMessages.add(new ForumMessage("RE: Most popular design patterns", createDate(2008, 0, 24, 2, 51), u4, Arrays.asList(
                new ForumMessage("Singleton", createDate(2008, 0, 24, 20, 1), u5, Arrays.asList(
                        new ForumMessage("Re: Singleton", createDate(2008, 2, 4, 21, 15), u2, null))),
                new ForumMessage("Re: Most popular design patterns", createDate(2008, 1, 30, 12, 36), u6, null))));
        rootMessages.add(new ForumMessage("What development methodology do you use?", createDate(2008, 9, 4, 2, 51), u3, Arrays.asList(
                new ForumMessage("Re: What development methodology do you use?", createDate(2008, 9, 7, 20, 1), u2, null),
                new ForumMessage("Re: What development methodology do you use?", createDate(2009, 4, 15, 14, 10), u1, Arrays.asList(
                        new ForumMessage("Re: What development methodology do you use?", createDate(2009, 5, 7, 8, 30), u2, null))),
                new ForumMessage("Re: What development methodology do you use?", createDate(2008, 9, 10, 12, 36), u6, null))));
        rootMessages.add(new ForumMessage("Scaling an image", createDate(2007, 6, 7, 8, 13), u2, Arrays.asList(
                new ForumMessage("Re: Scaling an image", createDate(2007, 6, 8, 9, 47), u3, Arrays.asList(
                        new ForumMessage("Re: Scaling an image", createDate(2007, 6, 17, 1, 43), u3, Arrays.asList(
                                new ForumMessage("Re: Scaling an image", createDate(2007, 7, 3, 0, 12), u5, null))))))));
        rootMessages.add(new ForumMessage("Need to create a simple database web application", createDate(2007, 11, 7, 8, 13), u4, Arrays.asList(
                new ForumMessage("Re: Need to create a simple database web application", createDate(2007, 11, 8, 9, 47), u1, null))));
        rootMessages.add(new ForumMessage("OpenFaces Roadmap", createDate(2009, 11, 3, 17, 25), u3, Arrays.asList(
                new ForumMessage("Re: OpenFaces Roadmap", createDate(2009, 11, 9, 11, 54), u2, Arrays.asList(
                        new ForumMessage("Re: OpenFaces Roadmap", createDate(2009, 11, 9, 17, 5), u3, null))))));
        rootMessages.add(new ForumMessage("Mojarra vs. MyFaces", createDate(2009, 5, 24, 12, 17), u6, Arrays.asList(
                new ForumMessage("Re: Mojarra vs. MyFaces", createDate(2009, 5, 24, 12, 32), u2, null),
                new ForumMessage("Re: Mojarra vs. MyFaces", createDate(2009, 5, 24, 19, 20), u5, Arrays.asList(
                        new ForumMessage("Re: Mojarra vs. MyFaces", createDate(2009, 5, 24, 23, 23), u6, Arrays.asList(
                                new ForumMessage("Re: Mojarra vs. MyFaces", createDate(2009, 5, 25, 33, 26), u5, null)
                        ))
                )),
                new ForumMessage("Re: Mojarra vs. MyFaces", createDate(2009, 5, 25, 17, 3), u1, null),
                new ForumMessage("Re: Mojarra vs. MyFaces", createDate(2009, 5, 28, 10, 45), u4, null))));
    }

    private ExpansionState forumTreeTableExpansionState = new SeveralLevelsExpanded(1);

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
        return forumTreeTableExpansionState;
    }

    public void setForumTreeTableExpansionState(ExpansionState forumTreeTableExpansionState) {
        this.forumTreeTableExpansionState = forumTreeTableExpansionState;
    }

    public List<ForumMessage> getNodeChildren() {
        ForumMessage message = getMessage();
        return message != null ? message.getReplies() : rootMessages;
    }

    public boolean getNodeHasChildren() {
        ForumMessage message = getMessage();
        return message.hasReplies();
    }

    private ForumMessage getMessage() {
        return Faces.var("message", ForumMessage.class);
    }


    public String getDateCategory() {
        ForumMessage message = getMessage();
        Date date = message.getDate();
        return formatDate(date);
    }

    private String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(date);
    }

}

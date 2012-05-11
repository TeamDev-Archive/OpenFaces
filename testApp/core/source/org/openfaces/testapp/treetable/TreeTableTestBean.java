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

import org.openfaces.util.Faces;
import org.openfaces.component.table.AllNodesCollapsed;
import org.openfaces.component.table.AllNodesExpanded;
import org.openfaces.component.table.ExpansionState;
import org.openfaces.component.table.SeveralLevelsExpanded;
import org.openfaces.testapp.datatable.EmailTableDemoBean;
import org.openfaces.testapp.datatable.User;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * @author Dmitry Pikhulya
 */
public class TreeTableTestBean {

    Logger logger = Logger.getLogger(TreeTableTestBean.class.getName());

    private Object selectedNodeData1;
    private Object selectedNodeId1;
    private Object selectedNodeData2;
    private Object selectedNodeId2;

    private List selectedNodeIds1;
    private List selectedNodeDatas2;

    private List<ForumMessage> rootMessages = new ArrayList<ForumMessage>();
    private User anonymousUser;

    private ExpansionState forumTreeTableExpansionState;
    private ExpansionState treeTable2ExpansionState;

    private int forumExpansionLevel;

    public TreeTableTestBean() {
        int i = 0;
        User guillaume = new User(String.valueOf(i++), "Guillaume ALLEON", null, null);
        User dan = new User(String.valueOf(i++), "dan", null, null);
        User makc = new User(String.valueOf(i++), "makc.the.great@gmail.com", null, null);
        User u1 = new User(String.valueOf(i++), "peter.the.best@yahoo.com", null, null);
        User u2 = new User(String.valueOf(i++), "Gruber", null, null);
        User u3 = new User(String.valueOf(i++), "Anonymous", null, null);
        anonymousUser = u3;

        rootMessages.add(new ForumMessage("Mesh intersection", EmailTableDemoBean.createDate(2005, 10, 4, 2, 51), guillaume, Arrays.asList(new Object[]{
                new ForumMessage("Re: Mesh intersection", EmailTableDemoBean.createDate(2005, 10, 4, 20, 1), dan, Arrays.asList(new Object[]{
                        new ForumMessage("Re: Mesh intersection", EmailTableDemoBean.createDate(2005, 10, 20, 21, 15), guillaume, null)})),
                new ForumMessage("Re: Mesh intersection", EmailTableDemoBean.createDate(2005, 10, 10, 12, 36), makc, null)})));
        rootMessages.add(new ForumMessage("Re: Multisampling, Supersampling source code for dummies?", EmailTableDemoBean.createDate(2005, 10, 7, 8, 13), u2, Arrays.asList(new Object[]{
                new ForumMessage("Re: Multisampling, Supersampling source code for dummies?", EmailTableDemoBean.createDate(2005, 10, 8, 9, 47), u1, Arrays.asList(new Object[]{
                        new ForumMessage("Re: Multisampling, Supersampling source code for dummies?", EmailTableDemoBean.createDate(2005, 10, 15, 1, 43), u3, Arrays.asList(new Object[]{
                                new ForumMessage("Re: Multisampling, Supersampling source code for dummies?", EmailTableDemoBean.createDate(2005, 11, 3, 0, 12), u2, null)}))}))})));
    }

    public List getNodeChildren() {
        ForumMessage message = getMessage();
        if (message == null)
            return rootMessages;
        else
            return message.getReplies();
    }


    private String masterMessageId;

    public void reply() {
        ForumMessage message = getMessage();
        try {
            masterMessageId = message != null ? (String) message.getId() : "null";
            newMessageSubject = message != null ? "Re: " + message.getSubject() : "";
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().redirect("newForumMessage.jsp");
            facesContext.responseComplete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        ForumMessage message = getMessage();
        ForumMessage parent = message.getParent();
        if (parent == null)
            rootMessages.remove(message);
        else
            parent.removeReply(message);
    }

    private ForumMessage getMessage() {
        return (ForumMessage) Faces.var("message");
    }

    private String newMessageSubject;

    public String getNewMessageSubject() {
        return newMessageSubject;
    }

    public void setNewMessageSubject(String newMessageSubject) {
        this.newMessageSubject = newMessageSubject;
    }

    public String postNewMessage() {
        if (masterMessageId == null) {
            logger.info("masterMessageId == null");
            return "treeTableDemo";
        }
        ForumMessage masterMessage = ForumMessage.messageById(masterMessageId);
        masterMessageId = null;
        ForumMessage newMessage = new ForumMessage(newMessageSubject, new Date(), anonymousUser, null);
        masterMessage.addReply(newMessage);
        return "treeTableDemo";
    }

    public String cancelNewMessage() {
        new ForumMessage(newMessageSubject, new Date(), anonymousUser, null);
        return "treeTableDemo";
    }

    public String getPageNameFromPageInfo() {
        String pageInfo = (String) Faces.var("pageInfo");
        return (String) new StringTokenizer(pageInfo, "|", false).nextElement();
    }

    public String getPageUrlFromPageInfo() {
        String pageInfo = (String) Faces.var("pageInfo");
        StringTokenizer stringTokenizer = new StringTokenizer(pageInfo, "|", false);
        stringTokenizer.nextElement();
        return (String) stringTokenizer.nextElement();
    }

    public Object getSelectedNodeData1() {
        return selectedNodeData1;
    }

    public void setSelectedNodeData1(Object selectedNodeData1) {
        this.selectedNodeData1 = selectedNodeData1;
    }

    public Object getSelectedNodeId1() {
        return selectedNodeId1;
    }

    public void setSelectedNodeId1(Object selectedNodeId1) {
        this.selectedNodeId1 = selectedNodeId1;
    }

    public Object getSelectedNodeData2() {
        return selectedNodeData2;
    }

    public void setSelectedNodeData2(Object selectedNodeData2) {
        this.selectedNodeData2 = selectedNodeData2;
    }

    public Object getSelectedNodeId2() {
        return selectedNodeId2;
    }

    public void setSelectedNodeId2(Object selectedNodeId2) {
        this.selectedNodeId2 = selectedNodeId2;
    }


    public List getSelectedNodeIds1() {
        return selectedNodeIds1;
    }

    public void setSelectedNodeIds1(List selectedNodeIds1) {
        this.selectedNodeIds1 = selectedNodeIds1;
    }

    public List getSelectedNodeDatas2() {
        return selectedNodeDatas2;
    }

    public void setSelectedNodeDatas2(List selectedNodeDatas2) {
        this.selectedNodeDatas2 = selectedNodeDatas2;
    }

    public ExpansionState getForumTreeTableExpansionState() {
        return forumTreeTableExpansionState;
    }

    public void setForumTreeTableExpansionState(ExpansionState forumTreeTableExpansionState) {
        this.forumTreeTableExpansionState = forumTreeTableExpansionState;
    }

    public ExpansionState getTreeTable2ExpansionState() {
        return treeTable2ExpansionState;
    }

    public void setTreeTable2ExpansionState(ExpansionState treeTable2ExpansionState) {
        this.treeTable2ExpansionState = treeTable2ExpansionState;
    }

    public int getForumExpansionLevel() {
        return forumExpansionLevel;
    }

    public void setForumExpansionLevel(int forumExpansionLevel) {
        this.forumExpansionLevel = forumExpansionLevel;
    }

    public void forumExpandUpToLevel() {
        forumTreeTableExpansionState = new SeveralLevelsExpanded(forumExpansionLevel);
    }

    public void forumExpandAll() {
        forumTreeTableExpansionState = new AllNodesExpanded();
    }

    public void forumCollapseAll() {
        forumTreeTableExpansionState = new AllNodesCollapsed();
    }

    public boolean isAnonymousMessage() {
        ForumMessage message = getMessage();
        String userName = message.getAuthor().getUserName();
        return userName.equals("Anonymous");
    }

    public String getDynamicRowStyle() {
        int level = (Integer) Faces.var("level");
        if (level > 10)
            level = 10;
        double color = 1.0 - (1.0 / 20) * level;
        int intColor = (int) Math.round(color * 255);
        String hexColor = Integer.toString(intColor, 16);
        if (hexColor.length() == 1)
            hexColor = "0" + hexColor;
        String colorStr = "#" + hexColor + hexColor + hexColor;
        return "background: " + colorStr;
    }

    public boolean isTodayPost() {
        ForumMessage message = getMessage();
        Date date = message.getDate();
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        Calendar todayNight = GregorianCalendar.getInstance();
        todayNight.set(Calendar.HOUR_OF_DAY, 0);
        todayNight.set(Calendar.MINUTE, 0);
        todayNight.set(Calendar.SECOND, 0);
        todayNight.set(Calendar.MILLISECOND, 0);
        return c.after(todayNight);
    }

    private String bigNodesToggleVeritcalAlign = "middle";

    public String getBigNodesToggleVeritcalAlign() {
        return bigNodesToggleVeritcalAlign;
    }

    public void alignTop() {
        bigNodesToggleVeritcalAlign = "top";
    }

    public void alignBottom() {
        bigNodesToggleVeritcalAlign = "bottom";
    }

    public void alignMiddle() {
        bigNodesToggleVeritcalAlign = "middle";
    }

    private boolean booleanSwitch;

    public boolean isBooleanSwitch() {
        return booleanSwitch;
    }

    public void setBooleanSwitch(boolean booleanSwitch) {
        this.booleanSwitch = booleanSwitch;
    }

    public void test() {
        logger.info("test");
    }

    public void linkClicked(ActionEvent event) {
        ForumMessage message = Faces.var("message", ForumMessage.class);
        message.setSubject("-changed-");
    }

}

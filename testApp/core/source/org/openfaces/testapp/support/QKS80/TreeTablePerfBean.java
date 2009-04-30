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

package org.openfaces.testapp.support.QKS80;

import org.openfaces.util.FacesUtil;
import org.openfaces.component.table.AllNodesCollapsed;
import org.openfaces.component.table.ExpansionState;
import org.openfaces.testapp.screenshot.ForumMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TreeTablePerfBean {

    private ExpansionState forumTreeTableExpansionState = new AllNodesCollapsed();
    private List selectedNodeDatas2;

    public TreeTablePerfBean() {
    }


    public ExpansionState getForumTreeTableExpansionState() {
        return forumTreeTableExpansionState;
    }

    public void setForumTreeTableExpansionState(ExpansionState forumTreeTableExpansionState) {
        this.forumTreeTableExpansionState = forumTreeTableExpansionState;
    }

    public List<ForumMessage> getNodeChildren() {
        ForumMessage message = (ForumMessage) FacesUtil.getRequestMapValue("message");
        int level = (Integer) FacesUtil.getRequestMapValue("level");
        int count;
        switch (level) {
            case -1:
                count = 20;
                break;
            case 0:
                count = 50;
                break;
            case 1:
                count = 500;
                break;
            default:
                count = 0;
        }

        List<ForumMessage> result = new ArrayList<ForumMessage>(count);
        for (int i = 0; i < count; i++) {
            result.add(new ForumMessage(getSubject(message, i), new Date(), "me", null, new ArrayList()));
        }
        return result;
    }

    private String getSubject(ForumMessage parentMessage, int i) {
        return parentMessage == null ? "" : (parentMessage.getSubject() + ".") + i;
    }

    public boolean isNodeHasChildren() {
        int level = (Integer) FacesUtil.getRequestMapValue("level");
        switch (level) {
            case -1:
                return true;
            case 0:
                return true;
            case 1:
                return true;
            default:
                return false;
        }
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

    public List getSelectedNodeDatas2() {
        return selectedNodeDatas2;
    }

    public void setSelectedNodeDatas2(List selectedNodeDatas2) {
        this.selectedNodeDatas2 = selectedNodeDatas2;
    }

    private boolean booleanSwitch;

    public boolean isBooleanSwitch() {
        return booleanSwitch;
    }

    public void setBooleanSwitch(boolean booleanSwitch) {
        this.booleanSwitch = booleanSwitch;
    }

}

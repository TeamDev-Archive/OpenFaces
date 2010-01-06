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

package org.openfaces.testapp.support.QKS30;

import org.openfaces.util.FacesUtil;
import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.TreeTable;
import org.openfaces.testapp.datatable.User;
import org.openfaces.testapp.datatable.UserCategory;
import org.openfaces.testapp.screenshot.ForumMessage;

import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class QKS30 {
    private DataTable dataTable;
    private List<User> dataTableList = new ArrayList<User>();
    private int rowCount = 0;
    private List filteredRows = new ArrayList();
    private TreeTable treeTable;
    private List rootMessages = new ArrayList();
    private int nodeCount = 0;


    public QKS30() {
        dataTableList.add(new User("user1", "John Smith", UserCategory.ENTHUSIAST, null));
        dataTableList.add(new User("user2", "Jane White", UserCategory.ENTHUSIAST, null));
        dataTableList.add(new User("user3", "Chris Lee", UserCategory.ENTHUSIAST, null));
        dataTableList.add(new User("user4", "Albert Ordinary", UserCategory.ENTHUSIAST, null));
        dataTableList.add(new User("user5", "William Green", UserCategory.ENTHUSIAST, null));
        dataTableList.add(new User("user6", "Christina Strange", UserCategory.ENTHUSIAST, null));
        dataTableList.add(new User("user7", "Bushy Toy", UserCategory.ENTHUSIAST, null));
        dataTableList.add(new User("user8", "James Erratic", UserCategory.ENTHUSIAST, null));

        String u1 = "Will Green";
        String u2 = "Jane White";
        String u3 = "Chris Lee";
        String u4 = "Dean Genius";
        String u5 = "Jonh Smile";
        String u6 = "Gary Blackt";
        rootMessages.add(new ForumMessage("JDK 6", new Date(), u1, null, Arrays.asList(new Object[]{
                new ForumMessage("Re: JDK 6", new Date(), u5, null, Arrays.asList(new Object[]{
                        new ForumMessage("System-tray support", new Date(), u4, null, Arrays.asList(new Object[]{
                                new ForumMessage("Re: System-tray support", new Date(), u2, null, null)}))
                })),
                new ForumMessage("LCD-optimized text display", new Date(), u6, null, Arrays.asList(new Object[]{
                        new ForumMessage("Re: LCD-optimized text display", new Date(), u4, null, Arrays.asList(new Object[]{
                                new ForumMessage("Re: LCD-optimized text display", new Date(), u5, null, null),
                                new ForumMessage("Re: LCD-optimized text display", new Date(), u6, null, null)}))
                })),
                new ForumMessage("Re: JDK 6", new Date(), u3, null, null),
                new ForumMessage("JSR 199: Java Compiler API", new Date(), u6, null, Arrays.asList(new Object[]{
                        new ForumMessage("Re: JSR 199: Java Compiler API", new Date(), u1, null, null)
                }))})));
        rootMessages.add(new ForumMessage("Re:Parsing algorithm", new Date(), u1, null, Arrays.asList(new Object[]{
                new ForumMessage("Re: Parsing algorithm", new Date(), u4, null, Arrays.asList(new Object[]{
                        new ForumMessage("Re: Parsing algorithm", new Date(), u3, null, Arrays.asList(new Object[]{
                                new ForumMessage("Re: Parsing algorithm", new Date(), u2, null, null)}))}))})));
        rootMessages.add(new ForumMessage("RE: Design patterns", new Date(), u4, null, Arrays.asList(new Object[]{
                new ForumMessage("Singleton", new Date(), u5, null, Arrays.asList(new Object[]{
                        new ForumMessage("Re: Singleton", new Date(), u2, null, null)})),
                new ForumMessage("Re: Design patterns", new Date(), u6, null, null)})));
        rootMessages.add(new ForumMessage("What methodology?", new Date(), u3, null, Arrays.asList(new Object[]{
                new ForumMessage("Re: What methodology?", new Date(), u2, null, null),
                new ForumMessage("Re: What methodology?", new Date(), u6, null, null)})));
        rootMessages.add(new ForumMessage("Scaling an image", new Date(), u2, null, Arrays.asList(new Object[]{
                new ForumMessage("Re: Scaling an image", new Date(), u3, null, Arrays.asList(new Object[]{
                        new ForumMessage("Re: Scaling an image", new Date(), u3, null, Arrays.asList(new Object[]{
                                new ForumMessage("Re: Scaling an image", new Date(), u5, null, null)}))}))})));
        rootMessages.add(new ForumMessage("Create an application", new Date(), u4, null, Arrays.asList(new Object[]{
                new ForumMessage("Re: Create an application", new Date(), u1, null, null)})));
    }


    public int getRowCount() {
        rowCount = dataTable.getTotalRowCount();
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<User> getDataTableList() {
        return dataTableList;
    }

    public void setDataTableList(List<User> dataTableList) {
        this.dataTableList = dataTableList;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }


    public List getFilteredRows() {
        return filteredRows;
    }

    public void setFilteredRows(List filteredRows) {
        this.filteredRows = filteredRows;
    }

    public void updateDataTable(ActionEvent event) {
        int i = 0;
        while (true) {
            dataTable.setRowIndex(i++);
            if (!dataTable.isRowAvailable()) break;
            filteredRows.add(dataTable.getRowData());
        }
    }


    public TreeTable getTreeTable() {
        return treeTable;
    }

    public void setTreeTable(TreeTable treeTable) {
        this.treeTable = treeTable;
    }

    public List getNodeChildren() {
        ForumMessage message = (ForumMessage) FacesUtil.getRequestMapValue("treetest");
        return message != null ? message.getReplies() : rootMessages;
    }

    public int getNodeCount() {
        nodeCount = treeTable.getRowCount();
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }


    public List getRootMessages() {
        return rootMessages;
    }

    public void setRootMessages(List rootMessages) {
        this.rootMessages = rootMessages;
    }
}

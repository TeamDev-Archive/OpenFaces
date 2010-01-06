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
package org.openfaces.testapp.treetable;

import org.openfaces.util.FacesUtil;
import org.openfaces.component.table.AllNodesCollapsed;
import org.openfaces.component.table.ExpansionState;
import org.openfaces.component.table.TreePath;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Darya Shumilina
 */
public class TreeTableBean {

    public static final String ENTHUSIAST = "Enthusiast";
    public static final String SPECIALIST = "Specialist";
    public static final String GURU = "Guru";
    public static final String OTHER = "Other";

    private List<Serializable> testTreeTablePermissions = new ArrayList<Serializable>();
    private List<TestTreeTableUser> users = new ArrayList<TestTreeTableUser>();
    private TestTreeTableUser currentUser;
    private TestTreeTableUser editedUser;
    private List editedTestTreeTablePermissions;

    private TestTreeTableUser selectedUser;
    private List<TestTreeTableUser> smallUserList;
    private List checkedUsers;
    private List<TestTreeTableUser> usersForFiltering;
    private static final String ALL_ITEMS = "<All>";

    private TreePath treeDemoSelectedNodePath;
    private Object nodeData;
    private List<TreePath> treePaths = new ArrayList<TreePath>();
    private List nodeDatas = new ArrayList();
    private TreePath documentBrowsingPath;
    private TreePath networkAccessPath;

    private String nodeDatasAsString;
    private String nodePathsAsString;
    private StringComparator stringComparator;

    public TreeTableBean() {
        stringComparator = new StringComparator();

        TestTreeTablePermission userManagement = new TestTreeTablePermission("1", "User Management");
        TestTreeTablePermission documentBrowsing = new TestTreeTablePermission("2", "Document Browsing");
        TestTreeTablePermission documentCreation = new TestTreeTablePermission("3", "Document Creation");
        TestTreeTablePermission documentModification = new TestTreeTablePermission("4", "Document Modification");
        TestTreeTablePermission documentDeletion = new TestTreeTablePermission("5", "Document Deletion");
        TestTreeTablePermission networkAccess = new TestTreeTablePermission("6", "Network Access");

        treeDemoSelectedNodePath = new TreePath(userManagement, treeDemoSelectedNodePath);
        nodeData = new TestTreeTablePermission("1", "User Management");

        documentBrowsingPath = new TreePath(documentBrowsing, documentBrowsingPath);
        networkAccessPath = new TreePath(networkAccess, networkAccessPath);
        treePaths.add(documentBrowsingPath);
        treePaths.add(networkAccessPath);

        nodeDatas.add(new TestTreeTablePermission("2", "Document Browsing"));
        nodeDatas.add(new TestTreeTablePermission("5", "Document Deletion"));

        testTreeTablePermissions.add(userManagement);
        testTreeTablePermissions.add(documentBrowsing);
        testTreeTablePermissions.add(documentCreation);
        testTreeTablePermissions.add(documentModification);
        testTreeTablePermissions.add(documentDeletion);
        testTreeTablePermissions.add(networkAccess);

        users.add(new TestTreeTableUser("1", "Guest", OTHER, Arrays.asList(documentBrowsing)));
        users.add(new TestTreeTableUser("2", "Manager", GURU, Arrays.asList(userManagement)));
        users.add(new TestTreeTableUser("3", "Administrator", SPECIALIST, Arrays.asList(userManagement, documentCreation)));
        users.add(new TestTreeTableUser("4", "Semen Semenych", ENTHUSIAST, Arrays.asList(documentDeletion)));
        users.add(new TestTreeTableUser("5", "Ivan Ivanych", GURU, Arrays.asList(documentCreation, documentModification, documentDeletion, networkAccess)));
        smallUserList = new ArrayList<TestTreeTableUser>(users);
        for (int i = 0; i < 10; i++) {
            if (i == 7) {
                usersForFiltering = new ArrayList<TestTreeTableUser>(users);
            }
            users.add(new TestTreeTableUser("RegularUser " + i, "Regular User " + +(i + 1), SPECIALIST, Arrays.asList(documentBrowsing)));
        }

        for (int i = 0; i < 3; i++) {
            usersForFiltering.add(new TestTreeTableUser("UnknownUser" + i, "Unknown User " + (i + 1), null, new ArrayList<TestTreeTablePermission>()));
        }
    }

    public List<Serializable> getTestTreeTablePermissions() {
        return testTreeTablePermissions;
    }

    public void setTestTreeTablePermissions(List<Serializable> TestTreeTablePermissions) {
        testTreeTablePermissions = TestTreeTablePermissions;
    }

    public List<TestTreeTableUser> getUsers() {
        return users;
    }

    public void setUsers(List<TestTreeTableUser> users) {
        this.users = users;
    }

    public TestTreeTableUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(TestTreeTableUser currentUser) {
        this.currentUser = currentUser;
    }

    public List getEditedTestTreeTablePermissions() {
        return editedTestTreeTablePermissions;
    }

    public void setEditedTestTreeTablePermissions(List editedTestTreeTablePermissions) {
        this.editedTestTreeTablePermissions = editedTestTreeTablePermissions;
    }

    public void userChanged() {
        editedUser = currentUser;

    }

    public List<TestTreeTablePermission> getCurrentUserTestTreeTablePermissions() {
        if (editedUser == null)
            return null;
        return editedUser.getTestTreeTablePermissions();
    }

    public void setCurrentUserTestTreeTablePermissions(List<TestTreeTablePermission> value) {
        if (value == null) {
            throw new NullPointerException("value shouldn't be null");
        }
        if (editedUser == null) {
            return;
        }
        editedUser.setTestTreeTablePermissions(value);
    }

    public List<TestTreeTableUser> getSmallUserList() {
        return smallUserList;
    }

    public void setSmallUserList(List<TestTreeTableUser> smallUserList) {
        this.smallUserList = smallUserList;
    }

    public List getCheckedUsers() {
        return checkedUsers;
    }

    public void setCheckedUsers(List checkedUsers) {
        this.checkedUsers = checkedUsers;
    }

    public TestTreeTableUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(TestTreeTableUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String getSelectedUserName() {
        if (selectedUser == null) {
            return "<no user selected>";
        }
        return selectedUser.getUserName();
    }

    public String getAdvancedUserFilter() {
        TestTreeTableUser user = (TestTreeTableUser) FacesUtil.getRequestMapValue("user");
        if (smallUserList.contains(user)) {
            return "Advanced Users";
        } else {
            return "Regular Users";
        }
    }

    public List<TestTreeTableUser> getUsersForFiltering() {
        if (manualFilterValue.equals(ALL_ITEMS))
            return usersForFiltering;
        List<TestTreeTableUser> list = new ArrayList<TestTreeTableUser>();
        for (TestTreeTableUser user : usersForFiltering) {
            if (user.getUserName().toUpperCase().startsWith(manualFilterValue)) {
                list.add(user);
            }
        }
        return list;
    }

    private List<Serializable> getUsersWithTestTreeTablePermission(TestTreeTablePermission TestTreeTablePermission) {
        List<Serializable> users = new ArrayList<Serializable>();
        for (TestTreeTableUser user : this.users) {
            if (user.getTestTreeTablePermissions().contains(TestTreeTablePermission)) {
                users.add(user);
            }
        }
        return users;
    }

    // ----- TreeTableDemo

    private TreePath[] treeDemoSelectedNodePaths;

    public TreePath[] getTreeDemoSelectedNodePaths() {
        return treeDemoSelectedNodePaths;
    }

    public void setTreeDemoSelectedNodePaths(TreePath[] paths) {
        treeDemoSelectedNodePaths = paths;
    }

    public List<Serializable> getUsersTreeChildren() {
        Object node = FacesUtil.getRequestMapValue("node");
        if (node == null)
            return testTreeTablePermissions;
        if (node instanceof TestTreeTablePermission) {
            TestTreeTablePermission TestTreeTablePermission = (TestTreeTablePermission) node;
            return getUsersWithTestTreeTablePermission(TestTreeTablePermission);
        }
        return null;
    }

    public Object getUsersTreeNodeKey() {
        Object node = FacesUtil.getRequestMapValue("node");
        String id = (node instanceof TestTreeTablePermission) ? ((TestTreeTablePermission) node).getId() : ((TestTreeTableUser) node).getId();
        return Arrays.asList(new Object[]{node.getClass(), id});
    }

    public String treeDemoRemoveTestTreeTablePermission() {
        List<TreePath> paths = new ArrayList<TreePath>(Arrays.asList(treeDemoSelectedNodePaths));
        for (TreePath nodePath : paths) {
            if (nodePath.getLevel() == 0) {
                continue;
            }
            TestTreeTableUser user = (TestTreeTableUser) nodePath.getValue();
            TestTreeTablePermission TestTreeTablePermission = (TestTreeTablePermission) nodePath.getParentPath().getValue();
            user.getTestTreeTablePermissions().remove(TestTreeTablePermission);
        }
        treeDemoSelectedNodePaths = null;
        return null;
    }

    public List<SelectItem> getManualFilterItems() {
        List<SelectItem> listItems = new ArrayList<SelectItem>();
        SortedSet<String> values = new TreeSet<String>();
        for (TestTreeTableUser user : usersForFiltering) {
            String name = user.getUserName();
            String value = name != null ? name.substring(0, 1).toUpperCase() : "<empty>";
            values.add(value);
        }
        listItems.add(new SelectItem(ALL_ITEMS, ALL_ITEMS, null));
        for (String s : values) {
            listItems.add(new SelectItem(s, s, null));
        }
        return listItems;
    }

    private String manualFilterValue = ALL_ITEMS;


    public String getManualFilterValue() {
        return manualFilterValue;
    }

    public void setManualFilterValue(String manualFilterValue) {
        this.manualFilterValue = manualFilterValue;
    }

    public TreePath getTreeDemoSelectedNodePath() {
        return treeDemoSelectedNodePath;
    }

    public void setTreeDemoSelectedNodePath(TreePath treeDemoSelectedNodePath) {
        this.treeDemoSelectedNodePath = treeDemoSelectedNodePath;
    }

    public Object getNodeData() {
        return nodeData;
    }

    public void setNodeData(Object nodeData) {
        this.nodeData = nodeData;
    }

    public List<TreePath> getTreePaths() {
        return treePaths;
    }

    public void setTreePaths(List<TreePath> treePaths) {
        this.treePaths = treePaths;
    }

    public List getNodeDatas() {
        return nodeDatas;
    }

    public void setNodeDatas(List nodeDatas) {
        this.nodeDatas = nodeDatas;
    }

    public TreePath getDocumentBrowsingPath() {
        return documentBrowsingPath;
    }

    public void setDocumentBrowsingPath(TreePath documentBrowsingPath) {
        this.documentBrowsingPath = documentBrowsingPath;
    }

    public String getNodeDatasAsString() {
        StringBuilder result = new StringBuilder();
        for (Object currentItem : nodeDatas) {
            if (currentItem instanceof TestTreeTablePermission) {
                TestTreeTablePermission currentItemTTTP = (TestTreeTablePermission) currentItem;
                result.append(currentItemTTTP.getName());
            } else if (currentItem instanceof TestTreeTableUser) {
                TestTreeTableUser currentItenTTTU = (TestTreeTableUser) currentItem;
                result.append(currentItenTTTU.getUserName());
            }
        }
        nodeDatasAsString = result.toString();
        return nodeDatasAsString;
    }

    public void setNodeDatasAsString(String nodeDatasAsString) {
        this.nodeDatasAsString = nodeDatasAsString;
    }

    public String getNodePathsAsString() {
        StringBuilder result = new StringBuilder();
        for (TreePath myTreePath : treePaths) {
            Object currentValue = myTreePath.getValue();
            if (currentValue instanceof TestTreeTablePermission) {
                TestTreeTablePermission currentItemTTTP = (TestTreeTablePermission) currentValue;
                result.append(currentItemTTTP.getName());
            } else if (currentValue instanceof TestTreeTableUser) {
                TestTreeTableUser currentItenTTTU = (TestTreeTableUser) currentValue;
                result.append(currentItenTTTU.getUserName());
            }
        }
        nodePathsAsString = result.toString();
        return nodePathsAsString;
    }

    public void setNodePathsAsString(String nodePathsAsString) {
        this.nodePathsAsString = nodePathsAsString;
    }

    public StringComparator getStringComparator() {
        return stringComparator;
    }

    public class TestTreeTableUser implements Serializable {
        private String id;
        private String userName;
        private String category;
        private List<TestTreeTablePermission> testTreeTablePermissions;

        public TestTreeTableUser(String id, String userName, String category, List<TestTreeTablePermission> testTreeTablePermissions) {
            this.id = id;
            this.userName = userName;
            setTestTreeTablePermissions(testTreeTablePermissions);
            this.category = category;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getId() {
            return id;
        }

        public String getUserName() {
            return userName;
        }

        public List<TestTreeTablePermission> getTestTreeTablePermissions() {
            return testTreeTablePermissions;
        }

        public void setTestTreeTablePermissions(List<TestTreeTablePermission> testTreeTablePermissions) {
            this.testTreeTablePermissions = testTreeTablePermissions != null ? new ArrayList<TestTreeTablePermission>(testTreeTablePermissions) : null;
        }
    }

    public class TestTreeTablePermission implements Serializable {
        private String id;
        private String name;

        public TestTreeTablePermission(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TestTreeTablePermission that = (TestTreeTablePermission) o;

            if (!id.equals(that.id)) {
                return false;
            }
            if (!name.equals(that.name)) {
                return false;
            }

            return true;
        }

        public int hashCode() {
            int result;
            result = id.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }
    }

    public class StringComparator implements Comparator {

        public StringComparator() {
        }

        public int compare(Object obj1, Object obj2) {
            String c1 = (String) obj1;
            String c2 = (String) obj2;
            Comparable value1 = turnInsideOut(c1);
            Comparable<String> value2 = turnInsideOut(c2);
            return value1.compareTo(value2);
        }

        private String turnInsideOut(String source) {
            char[] sourceAsCharArray = source.toCharArray();
            List<Character> temp = new ArrayList<Character>();
            for (char c : sourceAsCharArray) {
                temp.add(c);
            }
            Collections.reverse(temp);
            StringBuilder builder = new StringBuilder();
            for (Character character : temp) {
                builder.append(character);
            }
            return builder.toString();
        }

    }

    private ExpansionState expansionState1 = new AllNodesCollapsed();

    public ExpansionState getExpansionState1() {
        return expansionState1;
    }

    public void setExpansionState1(ExpansionState expansionState1) {
        this.expansionState1 = expansionState1;
    }

    public void collapseExpansionState1() {
        expansionState1 = new AllNodesCollapsed();
    }

}
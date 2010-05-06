/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.datatable;

import org.openfaces.util.Faces;
import org.openfaces.component.table.TreePath;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Dmitry Pikhulya
 */
public class UserPermissionsTableDemoBean {
    private List<Serializable> permissions = new ArrayList<Serializable>();
    private List<User> users = new ArrayList<User>();
    private User currentUser;
    private User editedUser;
    private List editedPermissions;

    private User selectedUser;
    private List<User> smallUserList;
    private List checkedUsers;
    private List<User> usersForFiltering;
    private static final String ALL_ITEMS = "<All>";
    private User testUser;

    public UserPermissionsTableDemoBean() {
        Permission userManagement = new Permission("1", "User Management");
        Permission documentBrowsing = new Permission("2", "Document Browsing");
        Permission documentCreation = new Permission("3", "Document Creation");
        Permission documentModification = new Permission("4", "Document Modification");
        Permission documentDeletion = new Permission("5", "Document Deletion");
        Permission networkAccess = new Permission("6", "Network Access");
        permissions.add(userManagement);
        permissions.add(documentBrowsing);
        permissions.add(documentCreation);
        permissions.add(documentModification);
        permissions.add(documentDeletion);
        permissions.add(networkAccess);

        users.add(new User("1", "Guest", UserCategory.OTHER, Arrays.asList(new Object[]{documentBrowsing})));
        users.add(new User("2", "Manager", UserCategory.GURU, Arrays.asList(new Object[]{userManagement})));
        users.add(new User("3", "Administrator", UserCategory.SPECIALIST, Arrays.asList(new Object[]{userManagement, documentCreation})));
        users.add(new User("4", "Semen Semenych", UserCategory.ENTHUSIAST, Arrays.asList(new Object[]{documentDeletion})));
        users.add(new User("5", "Ivan Ivanych", UserCategory.GURU, Arrays.asList(new Object[]{documentCreation, documentModification, documentDeletion, networkAccess})));
        smallUserList = new ArrayList<User>(users);
        for (int i = 0; i < 17; i++) {
            if (i == 7) {
                usersForFiltering = new ArrayList<User>(users);
            }
            users.add(new User("RegularUser" + i, "Regular User " + (i + 1), UserCategory.SPECIALIST, Arrays.asList(new Object[]{documentBrowsing})));
        }

        testUser = users.get(3);

        for (int i = 0; i < 3; i++) {
            usersForFiltering.add(new User("UnknownUser" + i, "Unknown User " + (i + 1), null, Arrays.asList(new Object[]{})));
        }
    }

    public List<Serializable> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Serializable> permissions) {
        this.permissions = permissions;
    }

    public User findUserByName(String name) {
        for (User user : users) {
            if (user.getUserName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List getEditedPermissions() {
        return editedPermissions;
    }

    public void setEditedPermissions(List editedPermissions) {
        this.editedPermissions = editedPermissions;
    }

    public void userChanged() {
        editedUser = currentUser;

    }

    public List getCurrentUserPermissions() {
        if (editedUser == null) {
            return null;
        }
        return editedUser.getPermissions();
    }

    public void setCurrentUserPermissions(List value) {
        if (value == null) {
            throw new NullPointerException("value shouldn't be null");
        }
        if (editedUser == null) {
            return;
        }
        editedUser.setPermissions(value);

    }

    public List<User> getSmallUserList() {
        return smallUserList;
    }

    public void setSmallUserList(List<User> smallUserList) {
        this.smallUserList = smallUserList;
    }

    public List getCheckedUsers() {
        return checkedUsers;
    }

    public void setCheckedUsers(List checkedUsers) {
        this.checkedUsers = checkedUsers;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String getSelectedUserName() {
        if (selectedUser == null) {
            return "<no user selected>";
        }
        return selectedUser.getUserName();
    }

    public String getAdvancedUserFilter() {
        User user = Faces.var("user", User.class);
        if (smallUserList.contains(user)) {
            return "Advanced Users";
        } else {
            return "Regular Users";
        }
    }

    public List<User> getUsersForFiltering() {
        if (manualFilterValue.equals(ALL_ITEMS)) {
            return usersForFiltering;
        }
        List<User> list = new ArrayList<User>();
        for (User user : usersForFiltering) {
            if (user.getUserName().toUpperCase().startsWith(manualFilterValue)) {
                list.add(user);
            }
        }
        return list;
    }

    private List<Serializable> getUsersWithPermission(Permission permission) {
        List<Serializable> users = new ArrayList<Serializable>();
        for (User user : this.users) {
            if (user.getPermissions().contains(permission)) {
                users.add(user);
            }
        }
        return users;
    }

    // ----- TreeTableDemo

    private TreePath[] treeDemoSelectedNodePaths;

    public List<TreePath> getTreeDemoSelectedNodePaths() {
        if (treeDemoSelectedNodePaths == null) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(treeDemoSelectedNodePaths);
    }

    public void setTreeDemoSelectedNodePaths(List paths) {
        treeDemoSelectedNodePaths = (TreePath[]) paths.toArray(new TreePath[paths.size()]);
    }

    public List<Serializable> getUsersTreeChildren() {
        Object node = Faces.var("node");
        if (node == null) {
            return permissions;
        }
        if (node instanceof Permission) {
            Permission permission = (Permission) node;
            return getUsersWithPermission(permission);
        }
        return null;
    }

    public Object getUsersTreeNodeKey() {
        Object node = Faces.var("node");
        String id = (node instanceof Permission) ? ((Permission) node).getId() : ((User) node).getId();
        return Arrays.asList(new Object[]{node.getClass(), id});
    }

    public String treeDemoRemovePermission() {
        List<TreePath> paths = new ArrayList<TreePath>(Arrays.asList(treeDemoSelectedNodePaths));
        for (TreePath nodePath : paths) {
            if (nodePath.getLevel() == 0) {
                continue;
            }
            User user = (User) nodePath.getValue();
            Permission permission = (Permission) nodePath.getParentPath().getValue();
            user.getPermissions().remove(permission);
        }
        treeDemoSelectedNodePaths = null;
        return null;
    }

    public List<SelectItem> getManualFilterItems() {
        List<SelectItem> listItems = new ArrayList<SelectItem>();
        SortedSet<String> values = new TreeSet<String>();
        for (User user : usersForFiltering) {
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


    public User getTestUser() {
        return testUser;
    }
}

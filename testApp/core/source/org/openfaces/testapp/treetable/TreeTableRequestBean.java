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
package org.openfaces.testapp.treetable;

import org.openfaces.util.Faces;
import org.openfaces.component.table.TreePath;
import org.openfaces.testapp.datatable.Permission;
import org.openfaces.testapp.datatable.User;
import org.openfaces.testapp.datatable.UserCategory;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Darya Shumilina
 */
public class TreeTableRequestBean {

    private List permissions = new ArrayList();
    private List<User> users = new ArrayList<User>();
    private User currentUser;
    private User editedUser;
    private List editedPermissions;

    private User selectedUser;
    private List<User> smallUserList;
    private List checkedUsers;
    private List<User> usersForFiltering;
    private static final String ALL_ITEMS = "<All>";


    public TreeTableRequestBean() {
        Random rand = new Random();
        Permission userManagement = new Permission("1", "User Management #" + String.valueOf(rand.nextInt(37000)));
        Permission documentBrowsing = new Permission("2", "Document Browsing #" + String.valueOf(rand.nextInt(37000)));
        Permission documentCreation = new Permission("3", "Document Creation #" + String.valueOf(rand.nextInt(37000)));
        Permission documentModification = new Permission("4", "Document Modification #" + String.valueOf(rand.nextInt(37000)));
        Permission documentDeletion = new Permission("5", "Document Deletion #" + String.valueOf(rand.nextInt(37000)));
        Permission networkAccess = new Permission("6", "Network Access #" + String.valueOf(rand.nextInt(37000)));
        permissions.add(userManagement);
        permissions.add(documentBrowsing);
        permissions.add(documentCreation);
        permissions.add(documentModification);
        permissions.add(documentDeletion);
        permissions.add(networkAccess);

        users.add(new User("1", "Guest #" + String.valueOf(rand.nextInt(37000)), UserCategory.OTHER, Arrays.asList(new Object[]{documentBrowsing})));
        users.add(new User("2", "Manager #" + String.valueOf(rand.nextInt(37000)), UserCategory.GURU, Arrays.asList(new Object[]{userManagement})));
        users.add(new User("3", "Administrator #" + String.valueOf(rand.nextInt(37000)), UserCategory.SPECIALIST, Arrays.asList(new Object[]{userManagement, documentCreation})));
        users.add(new User("4", "Semen Semenych #" + String.valueOf(rand.nextInt(37000)), UserCategory.ENTHUSIAST, Arrays.asList(new Object[]{documentDeletion})));
        users.add(new User("5", "Ivan Ivanych #" + String.valueOf(rand.nextInt(37000)), UserCategory.GURU, Arrays.asList(new Object[]{documentCreation, documentModification, documentDeletion, networkAccess})));
        smallUserList = new ArrayList<User>(users);
        for (int i = 0; i < 10; i++) {
            if (i == 7)
                usersForFiltering = new ArrayList<User>(users);
            users.add(new User("RegularUser " + i, "Regular User " + "#" + String.valueOf(rand.nextInt(37000)), UserCategory.SPECIALIST, Arrays.asList(new Object[]{documentBrowsing})));
        }

        for (int i = 0; i < 3; i++) {
            usersForFiltering.add(new User("UnknownUser" + i, "Unknown User " + (i + 1), null, Arrays.asList(new Object[]{})));
        }
    }

    public List getPermissions() {
        return permissions;
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions;
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
        if (editedUser == null)
            return null;
        return editedUser.getPermissions();
    }

    public void setCurrentUserPermissions(List value) {
        if (value == null)
            throw new NullPointerException("value shouldn't be null");
        if (editedUser == null)
            return;
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
        if (selectedUser == null)
            return "<no user selected>";
        return selectedUser.getUserName();
    }

    public String getAdvancedUserFilter() {
        User user = (User) Faces.var("user");
        if (smallUserList.contains(user))
            return "Advanced Users";
        else
            return "Regular Users";
    }

    public List<User> getUsersForFiltering() {
        if (manualFilterValue.equals(ALL_ITEMS))
            return usersForFiltering;
        List<User> list = new ArrayList<User>();
        for (User user : usersForFiltering) {
            if (user.getUserName().toUpperCase().startsWith(manualFilterValue)) {
                list.add(user);
            }
        }
        return list;
    }

    private List getUsersWithPermission(Permission permission) {
        List<Serializable> users = new ArrayList<Serializable>();
        for (User user : this.users) {
            if (user.getPermissions().contains(permission)) {
                users.add(user);
            }
        }
        return users;
    }

    // ----- TreeTableDemo

    private List<TreePath> treeDemoSelectedNodePaths;

    public List<TreePath> getTreeDemoSelectedNodePaths() {
        return treeDemoSelectedNodePaths;
    }

    public void setTreeDemoSelectedNodePaths(List<TreePath> treeDemoSelectedNodePaths) {
        this.treeDemoSelectedNodePaths = treeDemoSelectedNodePaths;
    }

    public List getUsersTreeChildren() {
        Object node = Faces.var("node");
        if (node == null)
            return permissions;
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
        List paths = new ArrayList(Arrays.asList(treeDemoSelectedNodePaths));
        for (Object path : paths) {
            TreePath nodePath = (TreePath) path;
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
}
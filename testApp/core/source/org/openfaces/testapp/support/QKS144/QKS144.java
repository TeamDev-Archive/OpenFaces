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

package org.openfaces.testapp.support.QKS144;

import org.openfaces.testapp.datatable.User;
import org.openfaces.testapp.datatable.UserCategory;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;


public class QKS144 {
    public List users = new ArrayList();
    public List selectedUsers = new ArrayList();

    public QKS144() {
        users.add(new SelectItem(new User("1", "Name1", UserCategory.OTHER, null), "User1", "Description1"));
        users.add(new SelectItem(new User("2", "Name2", UserCategory.OTHER, null), "User2", "Description2"));
        users.add(new SelectItem(new User("3", "Name3", UserCategory.OTHER, null), "User3", "Description3"));
        users.add(new SelectItem(new User("4", "Name4", UserCategory.OTHER, null), "User4", "Description4"));
        users.add(new SelectItem(new User("5", "Name5", UserCategory.OTHER, null), "User5", "Description5"));
    }


    public List getUsers() {
        return users;
    }

    public List getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List selectedUsers) {
        this.selectedUsers = selectedUsers;
    }
}

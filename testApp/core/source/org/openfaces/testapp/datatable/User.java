/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.datatable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class User implements Serializable {
    private String id;
    private String userName;
    private UserCategory category;
    private List permissions;

    public User(String id, String userName, UserCategory category, List permissions) {
        this.id = id;
        this.userName = userName;
        setPermissions(permissions);
        this.category = category;
    }

    public UserCategory getCategory() {
        return category;
    }

    public void setCategory(UserCategory category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public List getPermissions() {
        return permissions;
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions != null ? new ArrayList(permissions) : null;
    }
}

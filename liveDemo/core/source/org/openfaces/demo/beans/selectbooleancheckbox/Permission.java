/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.selectbooleancheckbox;

import java.util.Arrays;
import java.util.Collection;

public enum Permission {

    VIEW_POSTS("View Posts"),
    CREATE_POSTS("Create Posts", VIEW_POSTS),
    EDIT_ANY_POST("Edit Any Post", CREATE_POSTS),
    DELETE_OWN_POST("Delete Own Post", CREATE_POSTS),
    DELETE_ANY_POST("Delete Any Post", DELETE_OWN_POST),

    CREATE_ATTACHEMENTS("Create Attachements", VIEW_POSTS),
    REMOVE_ATTACHEMENTS("Remove Attachements", CREATE_ATTACHEMENTS),

    CREATE_NEWS("Create News", CREATE_POSTS),
    VIEW_HIDDEN_TIMED_NEWS("View Hidden Timed News", VIEW_POSTS),
    DELETE_NEWS("Delete News", CREATE_NEWS),

    ADD_COMMENTS("Add Comments", VIEW_POSTS),
    REMOVE_COMMENTS("Remove Comments", ADD_COMMENTS);

    private String label;
    private Collection<Permission> dependent;

    Permission(String label, Permission... dependent) {
        this.label = label;
        this.dependent = Arrays.asList(dependent);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Collection<Permission> getDependent() {
        return dependent;
    }

    public void setDependent(Collection<Permission> dependent) {
        this.dependent = dependent;
    }


}

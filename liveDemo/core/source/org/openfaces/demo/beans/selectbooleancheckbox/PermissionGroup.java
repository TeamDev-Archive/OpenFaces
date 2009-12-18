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

package org.openfaces.demo.beans.selectbooleancheckbox;

import java.util.Collection;
import java.util.Arrays;

public enum PermissionGroup {

    ADMINISTRATOR("Administrator"),
    USER("User", ADMINISTRATOR),
    ANONYMOUS("Anonymous", ADMINISTRATOR, USER);

    private String label;
    private Collection<PermissionGroup> dependent;

    PermissionGroup(String label, PermissionGroup... dependent) {
        this.label = label;
        this.dependent = Arrays.asList(dependent);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Collection<PermissionGroup> getDependent() {
        return dependent;
    }

    public void setDependent(Collection<PermissionGroup> dependent) {
        this.dependent = dependent;
    }

}

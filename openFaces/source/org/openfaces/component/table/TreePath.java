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
package org.openfaces.component.table;

import java.io.Serializable;

/**
 * @author Dmitry Pikhulya
 */
public class TreePath implements Serializable {
    private Object value;
    private TreePath parentPath;

    public TreePath(Object value, TreePath parentPath) {
        this.value = value;
        this.parentPath = parentPath;
    }

    public Object getValue() {
        return value;
    }

    public TreePath getParentPath() {
        return parentPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TreePath treePath = (TreePath) o;

        if (value != null ? !value.equals(treePath.value) : treePath.value != null) return false;
        if (parentPath != null ? !parentPath.equals(treePath.parentPath) : treePath.parentPath != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (value != null ? value.hashCode() : 0);
        result = 29 * result + (parentPath != null ? parentPath.hashCode() : 0);
        return result;
    }

    public int getLevel() {
        if (parentPath == null)
            return 0;
        return parentPath.getLevel() + 1;
    }
}

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
public class GroupHeaderOrFooter<C extends GroupHeaderOrFooter> implements Serializable, Comparable<C> {
    private RowGroup rowGroup;

    public GroupHeaderOrFooter(RowGroup rowGroup) {
        this.rowGroup = rowGroup;
    }

    public RowGroup getRowGroup() {
        return rowGroup;
    }

    public int compareTo(GroupHeaderOrFooter that) {
        if (that == null) return 1;
        RowGroup thisValue = this.getRowGroup();
        RowGroup thatValue = that.getRowGroup();
        if (thisValue == null) {
            return thatValue != null ? -1 : 0;
        }
        if (thatValue == null) return 1;
        return ((Comparable) thisValue).compareTo(thatValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (!this.getClass().equals(obj.getClass()))
            return false;
        GroupHeaderOrFooter that = (GroupHeaderOrFooter) obj;
        RowGroup thisValue = this.getRowGroup();
        RowGroup thatValue = that.getRowGroup();
        if (thisValue == null)
            return thatValue == null;

        return thisValue.equals(thatValue);
    }

    @Override
    public int hashCode() {
        return rowGroup != null ? rowGroup.hashCode() : 0;
    }
}

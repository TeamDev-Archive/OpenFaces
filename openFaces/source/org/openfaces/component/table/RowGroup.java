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
public class RowGroup implements Serializable, Comparable<RowGroup> {
    private String columnId;
    private Object groupingValue;
    private RowGroup parentGroup;

    public RowGroup() {
    }

    public RowGroup(String columnId, Object groupingValue, RowGroup parentGroup) {
        this.columnId = columnId;
        this.groupingValue = groupingValue;
        this.parentGroup = parentGroup;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public Object getGroupingValue() {
        return groupingValue;
    }

    public void setGroupingValue(Object groupingValue) {
        this.groupingValue = groupingValue;
    }

    public RowGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(RowGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    public int compareTo(RowGroup that) {
        if (that == null) return 1;
        Object thisValue = this.getGroupingValue();
        Object thatValue = that.getGroupingValue();
        if (thisValue == null) {
            return thatValue != null ? -1 : 0;
        }
        if (thatValue == null) return 1;
        return ((Comparable) thisValue).compareTo(thatValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RowGroup rowGroup = (RowGroup) o;

        if (columnId != null ? !columnId.equals(rowGroup.columnId) : rowGroup.columnId != null) return false;
        if (groupingValue != null ? !groupingValue.equals(rowGroup.groupingValue) : rowGroup.groupingValue != null)
            return false;
        if (parentGroup != null ? !parentGroup.equals(rowGroup.parentGroup) : rowGroup.parentGroup != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = columnId != null ? columnId.hashCode() : 0;
        result = 31 * result + (groupingValue != null ? groupingValue.hashCode() : 0);
        result = 31 * result + (parentGroup != null ? parentGroup.hashCode() : 0);
        return result;
    }
}

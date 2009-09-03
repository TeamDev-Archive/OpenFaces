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
package org.openfaces.component.table;

/**
 * @author Dmitry Pikhulya
 */
public abstract class ColumnFilterCriterion extends FilterCriterion {
    private String columnId;

    protected ColumnFilterCriterion() {
    }

    public String getColumnId() {
        return columnId;
    }

    void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnFilterCriterion that = (ColumnFilterCriterion) o;

        if (columnId != null ? !columnId.equals(that.columnId) : that.columnId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return columnId != null ? columnId.hashCode() : 0;
    }
}

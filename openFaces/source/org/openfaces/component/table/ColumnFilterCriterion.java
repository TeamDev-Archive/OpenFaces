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

    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnFilterCriterion))
            return false;
        ColumnFilterCriterion that = (ColumnFilterCriterion) obj;
        if (this.columnId == null) {
            return that.columnId == null;
        } else {
            return this.columnId.equals(that.columnId);
        }
    }

    @Override
    public int hashCode() {
        if(columnId != null) {
            return columnId.hashCode();
        }
        return super.hashCode();
    }
}

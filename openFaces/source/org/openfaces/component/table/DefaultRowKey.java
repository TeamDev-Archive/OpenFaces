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
package org.openfaces.component.table;

import java.io.Serializable;

/**
 * @author Dmitry Pikhulya
 */
public class DefaultRowKey implements Serializable {
    private int rowIndex;
    private int rowIndexInOriginalList;

    public DefaultRowKey(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public DefaultRowKey(int rowIndex, int rowIndexInOriginalList) {
        this.rowIndex = rowIndex;
        this.rowIndexInOriginalList = rowIndexInOriginalList;
    }

    public int getRowIndex() {
        return rowIndex;
    }


    public int getRowIndexInOriginalList() {
        return rowIndexInOriginalList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final DefaultRowKey that = (DefaultRowKey) o;

        if (rowIndexInOriginalList != that.rowIndexInOriginalList) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return rowIndex;
    }
}

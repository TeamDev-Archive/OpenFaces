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

/**
 * Defines the possible scopes of data for such operations as extracting table data programmatically, and exporting
 * table's data.
 */
public enum DataScope {
    /**
     * Only the currently displayed rows in the order that they are displayed. That is, if DataTable pagination is
     * turned on, then only the current page rows are considered.
     */
    DISPLAYED_ROWS,

    /**
     * All rows on all table's pages, in their current sorting order. If filters are enabled for the table, then
     * only the filtered rows are considered.
     */
    FILTERED_ROWS
}

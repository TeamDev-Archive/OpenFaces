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
package org.openfaces.renderkit.table;

import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;

/**
 * @author Dmitry Pikhulya
 */
public class CellCoordinates {
    private final int rowIndex;
    private final int rowCellIndex;

    public CellCoordinates(int rowIndex, int rowCellIndex) {
        this.rowIndex = rowIndex;
        this.rowCellIndex = rowCellIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getRowCellIndex() {
        return rowCellIndex;
    }

    public JSONObject asJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("row", rowIndex);
            obj.put("cell", rowCellIndex);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
}

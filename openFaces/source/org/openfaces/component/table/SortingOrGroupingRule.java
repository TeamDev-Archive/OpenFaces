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

import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.org.json.JSONString;

import java.io.Serializable;

/**
 * @author Dmitry Pikhulya
 */
public abstract class SortingOrGroupingRule implements Serializable, JSONString {
    private static final String JSON_FIELD_COLUMN_ID = "columnId";
    private static final String JSON_FIELD_ASCENDING = "ascending";
    private String columnId;
    private boolean ascending = true;

    public SortingOrGroupingRule() {
    }

    public SortingOrGroupingRule(String columnId, boolean ascending) {
        this.columnId = columnId;
        this.ascending = ascending;
    }

    public SortingOrGroupingRule(JSONObject jsonObj) throws JSONException {
        if (jsonObj.isNull(JSON_FIELD_COLUMN_ID) || jsonObj.isNull(JSON_FIELD_ASCENDING))
            throw new IllegalArgumentException("Improper JSONObject passed to " + getClass().getName() +
                    " constructor. Ensure that both \"" + JSON_FIELD_COLUMN_ID + "\" and \"" + JSON_FIELD_ASCENDING +
                    "\" fields are provided");
        columnId = jsonObj.getString(JSON_FIELD_COLUMN_ID);
        ascending = jsonObj.getBoolean(JSON_FIELD_ASCENDING);
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public String toJSONString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JSON_FIELD_COLUMN_ID, columnId);
            obj.put(JSON_FIELD_ASCENDING, ascending);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return obj.toString();
    }
}

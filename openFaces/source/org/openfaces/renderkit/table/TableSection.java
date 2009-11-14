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
public abstract class TableSection extends TableElement {
    public TableSection(TableElement parent) {
        super(parent);
    }

    public JSONObject getInitParam() {
        JSONObject result = new JSONObject();

        try {

            fillInitParam(result);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    protected abstract void fillInitParam(JSONObject result) throws JSONException;
}

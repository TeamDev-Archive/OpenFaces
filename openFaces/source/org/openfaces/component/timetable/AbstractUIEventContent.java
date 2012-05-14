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

package org.openfaces.component.timetable;

import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.ConvertibleToJSON;

import javax.faces.component.UIComponentBase;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Roman Porotnikov
 */
public abstract class AbstractUIEventContent extends UIComponentBase implements ConvertibleToJSON, Serializable {

    protected abstract String getType();

    protected abstract JSONObject getData() throws JSONException;

    public JSONObject toJSONObject(Map params) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        obj.put("data", getData());
        return obj;
    }

}

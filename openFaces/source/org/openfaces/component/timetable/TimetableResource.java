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
package org.openfaces.component.timetable;

import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.ConvertibleToJSON;

import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class TimetableResource implements ConvertibleToJSON, Cloneable {
    private String id;
    private Object object; // todo: is object property really needed? consider removing, or maybe adding it to TimetableEvent as well
    private String name;

    public TimetableResource(String resource) {
        object = resource;
        id = resource;
        name = resource;
    }

    public TimetableResource(Object object, String id, String name) {
        this.object = object;
        this.id = id;
        this.name = name;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject toJSONObject(Map params) throws JSONException {
        JSONObject resourceObj = new JSONObject();
        resourceObj.put("id", getId());
        resourceObj.put("name", getName());
        return resourceObj;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

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
import org.openfaces.util.DataUtil;

import java.awt.*;
import java.util.Date;
import java.util.Map;

/**
 * An instance of this class specifies a single event in a timetable. Each event in a timetable should have a unique
 * id.
 *
 * @author Dmitry Pikhulya
 */
public class TimetableEvent extends AbstractTimetableEvent {
    private String name;
    private String description;
    private Color color;

    public TimetableEvent() {
    }

    public TimetableEvent(String id, Date start, Date end, String name) {
        this(id, start, end, name, null, null, null);
    }

    public TimetableEvent(String id, Date start, Date end, String name, String description) {
        this(id, start, end, name, description, null, null);
    }

    public TimetableEvent(String id, Date start, Date end, String name, String description, Color color) {
        this(id, start, end, name, description, color, null);
    }

    public TimetableEvent(String id, Date start, Date end, String name, String description, Color color, String resourceId) {
        super(id, resourceId, start, end);
        this.name = name;
        this.description = description;
        this.color = color;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void copyStateFrom(AbstractTimetableEvent e) {
        TimetableEvent timetableEvent = (TimetableEvent) e;
        super.copyStateFrom(timetableEvent);
        name = timetableEvent.getName();
        description = timetableEvent.getDescription();
        color = timetableEvent.getColor();
    }

    @Override
    public JSONObject toJSONObject(Map params) throws JSONException {
        JSONObject obj = super.toJSONObject(params);
        obj.put("name", name);
        obj.put("description", description);
        obj.put("color", DataUtil.colorAsHtmlColor(color));
        return obj;
    }

}

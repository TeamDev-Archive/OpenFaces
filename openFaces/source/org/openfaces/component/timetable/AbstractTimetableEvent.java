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
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.util.ConvertibleToJSON;
import org.openfaces.util.DataUtil;

import javax.faces.FacesException;
import java.awt.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Timetable id should uniquely identify an event among all other events of this timetable.
 *
 * @author Dmitry Pikhulya
 */
public abstract class AbstractTimetableEvent implements ConvertibleToJSON, Cloneable, Serializable {
    private String id;
    private String resourceId;
    private Date start;
    private Date end;
    private Map<String, Object> customProperties;

    protected AbstractTimetableEvent() {
    }

    protected AbstractTimetableEvent(String id, String resourceId, Date start, Date end) {
        if (id != null && !DataUtil.isValidObjectId(id))
            throw new FacesException("Timetable id must be formatted as a number or a java identifier: " + id);
        this.id = id;
        this.resourceId = resourceId;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id != null && !DataUtil.isValidObjectId(id))
            throw new FacesException("Timetable id must be formatted as a number or a java identifier: " + id);
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setCustomProperty(String property, Object value) {
        if (customProperties == null)
            customProperties = new HashMap<String, Object>(4);
        customProperties.put(property, value);
    }

    public Object getCustomProperty(String property) {
        if (customProperties == null)
            customProperties = new HashMap<String, Object>(4);
        return customProperties.get(property);
    }

    public Map<String, Object> getCustomProperties() {
        return customProperties;
    }

    public JSONObject toJSONObject(Map params) throws JSONException {
        TimeZone timeZone = (TimeZone) params.get("timeZone");
        if (timeZone == null)
            throw new IllegalArgumentException("TimetableEvent.toJSONObject requires the 'timeZone' parameter to be passed in the params map");
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        if (resourceId != null)
            obj.put("resourceId", resourceId);
        obj.put("startStr", DataUtil.formatDateTimeForJs(start, timeZone));
        obj.put("endStr", DataUtil.formatDateTimeForJs(end, timeZone));
        if (customProperties != null) {
            JSONObject properties = new JSONObject();
            obj.put("customProperties", properties);
            Set<Map.Entry<String, Object>> entries = customProperties.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                String property = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Date)
                    value = DataUtil.formatDateTimeForJs((Date) value, timeZone);
                else if (value instanceof Color)
                    value = CSSUtil.formatColor((Color) value);
                properties.put(property, value);
            }
        }
        return obj;
    }


    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void copyStateFrom(AbstractTimetableEvent e) {
        resourceId = e.getResourceId();
        start = e.getStart();
        end = e.getEnd();
    }
}

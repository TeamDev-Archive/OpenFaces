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

package org.openfaces.renderkit.panel;

import java.util.HashMap;
import java.util.Map;

public class JSEventsObject {
    private Map<String, String> events = new HashMap<String, String>();

    public JSEventsObject() {
    }

    public JSEventsObject(String eventName, String scriptStr) {
        this.putOpt(eventName, scriptStr);
    }

    public JSEventsObject(Map<String, String> events) {
        this.events.putAll(events);
    }

    public boolean putOpt(String eventName, String scriptStr) {
        if (eventName == null || eventName.length() == 0 || scriptStr == null || scriptStr.length() == 0)
            return false;
        events.put(eventName, scriptStr);
        return true;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('{');
        for (String key : events.keySet()) {
            result.append("\"").append(key).append("\" : function(event){").append(events.get(key)).append("},");
        }
        if (result.length() > 1)
            result.deleteCharAt(result.length() - 1);
        result.append('}');
        return result.toString();
    }

    public static String JSEventObject(String eventName, String scriptStr) { // todo: refactor this class to use JSON API
        if (eventName == null || eventName.length() == 0 || scriptStr == null || scriptStr.length() == 0)
            return "{}";
        return "{\"" + eventName + "\" : function(event){" + scriptStr + "}}";
    }

    public static String JSEventObject(Map<String, ?> events) {
        StringBuilder result = new StringBuilder();
        result.append('{');
        for (Map.Entry<String, ?> key : events.entrySet()) {
            result.append("\"").append(key.getKey()).append("\" : function(event){").append(key.getValue().toString()).append("},");
        }
        if (result.length() > 1)
            result.deleteCharAt(result.length() - 1);
        result.append('}');
        return result.toString();
    }

}


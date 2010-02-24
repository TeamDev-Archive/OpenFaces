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
package org.openfaces.util;

import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONObject;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Dmitry Pikhulya
 */
public class FunctionCallScript extends Script {
    private String script;

    public FunctionCallScript(String functionName, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(functionName).append('(');
        for (int i = 0, count = params.length; i < count; i++) {
            writeParam(sb, params[i]);
            if (i < count - 1)
                sb.append(", ");
        }
        sb.append(")");

        script = sb.toString();
    }

    private void writeParam(StringBuilder sb, Object param) {
        if (param == null) {
            sb.append("null");
        } else if (param instanceof Number || param instanceof Boolean) {
            sb.append(param);
        } else if (param instanceof String) {
            sb.append(escapeStringForJSAndQuote((String) param));
        } else if (param instanceof JSONObject || param instanceof JSONArray) {
            sb.append(param);
        } else if (param instanceof UIComponent) {
            String componentId = ((UIComponent) param).getClientId(FacesContext.getCurrentInstance());
            sb.append(escapeStringForJSAndQuote(componentId));
        } else if (param instanceof Script) {
            sb.append(((Script) param).getScript());
        } else if (param instanceof Iterable || param.getClass().isArray()) {
            if (param.getClass().isArray())
                param = Arrays.asList((Object[]) param);
            sb.append('[');
            Iterator iterator = ((Iterable) param).iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                writeParam(sb, item);
                if (iterator.hasNext())
                    sb.append(", ");
            }
            sb.append(']');
        } else {
            sb.append(escapeStringForJSAndQuote(param.toString()));
        }
    }

    public static String escapeStringForJSAndQuote(String str) {
        if (str == null)
            return "null";
        else
            return '\'' + Rendering.escapeStringForJS(str) + '\'';
    }


    public String getScript() {
        return script;
    }
}

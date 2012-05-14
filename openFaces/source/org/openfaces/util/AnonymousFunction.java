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
package org.openfaces.util;

/**
 * @author Dmitry Pikhulya
 */
public class AnonymousFunction extends Script {
    private String[] paramNames;
    private String bodyScript;

    public AnonymousFunction(Script bodyScript, String... paramNames) {
        this(bodyScript.toString(), paramNames);
    }

    public AnonymousFunction(String bodyScript, String... paramNames) {
        this.paramNames = paramNames;
        this.bodyScript = bodyScript;
    }

    public String getScript() {
        StringBuilder result = new StringBuilder("function(");
        for (int i = 0, count = paramNames.length; i < count; i++) {
            String paramName = paramNames[i];
            result.append(paramName);
            if (i < count - 1)
                result.append(",");
        }
        result.append("){").append(bodyScript).append("}");
        return result.toString();
    }
}

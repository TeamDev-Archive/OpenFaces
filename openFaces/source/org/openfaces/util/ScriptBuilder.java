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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class ScriptBuilder extends Script {
    private StringBuilder scriptBuilder = new StringBuilder();

    public ScriptBuilder() {
    }

    public ScriptBuilder(String s) {
        append(s);
    }

    public ScriptBuilder(Script s) {
        append(s);
    }

    public String getScript() {
        return scriptBuilder.toString();
    }

    public ScriptBuilder append(Script script) {
        if (script == null)
            throw new IllegalArgumentException("script shouldn't be null");
        scriptBuilder.append(script);
        return this;
    }

    public ScriptBuilder append(Object rawScript) {
        return append(rawScript.toString());
    }

    public ScriptBuilder append(String rawScript) {
        scriptBuilder.append(rawScript);
        return this;
    }

    public ScriptBuilder dot() {
        scriptBuilder.append('.');
        return this;
    }

    public ScriptBuilder semicolon() {
        scriptBuilder.append(';');
        return this;
    }

    public ScriptBuilder functionCall(String functionName, Object... params) {
        return append(new FunctionCallScript(functionName, params));
    }

    public ScriptBuilder anonymousFunction(String bodyScript, String... paramNames) {
        return append(new AnonymousFunction(bodyScript, paramNames));
    }

    public ScriptBuilder newInstance(String constructorFunctionName, Object... params) {
        return append(new NewInstanceScript(constructorFunctionName, params));
    }

    public ScriptBuilder initScript(FacesContext context, UIComponent component, String initFunctionName, Object... params) {
        int paramCount = params.length;
        Object[] extendedParams = new Object[paramCount + 1];
        extendedParams[0] = component.getClientId(context);
        System.arraycopy(params, 0, extendedParams, 1, paramCount);
        return functionCall(initFunctionName, extendedParams).semicolon();
    }

    public ScriptBuilder onLoadScript(Script script) {
        return functionCall("O$.addLoadEvent", new AnonymousFunction(script));
    }

    public ScriptBuilder O$(UIComponent component) {
        return functionCall("O$", component);
    }

    public ScriptBuilder O$(String id) {
        return functionCall("O$", id);
    }

}

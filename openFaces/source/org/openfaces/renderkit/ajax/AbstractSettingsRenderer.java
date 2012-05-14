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
package org.openfaces.renderkit.ajax;

import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.RawScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Eugene Goncharov
 */
public abstract class AbstractSettingsRenderer extends RendererBase {

    protected void processEvent(FacesContext context, UIComponent component, String eventName, String eventHandlerJavascript) throws IOException {
        String eventFunction = getEventFunction(eventHandlerJavascript);

        String eventHandlingJavascript = getEventScript(eventName, eventFunction);
        renderInitScript(context, eventHandlingJavascript);
    }

    protected void renderInitScript(FacesContext context, String javaScript) throws IOException {
        AjaxUtil.renderAjaxSupport(context);
        Rendering.renderInitScript(context, new ScriptBuilder().onLoadScript(new RawScript(javaScript)));
    }

    protected String getEventScript(String eventName, String eventFunction) {
        String javaScript = "O$.Ajax.setCommonAjaxEventHandler('" + eventName + "'," + eventFunction + ");";
        return javaScript;
    }

    private String getComponentId(UIComponent component) {
        String resultId = null;

        while (component != null) {
            if (resultId == null) {
                resultId = component.getId();
            } else {
                if (component.getId() != null) {
                    resultId = component.getId() + ":" + resultId;
                }
            }
            component = component.getParent();
        }
        return resultId;
    }

    protected String getEventFunction(String eventHandlerJavascript) {
        if (!eventHandlerJavascript.startsWith("function")) {
            return "function(event){" + eventHandlerJavascript + "}";
        }
        return eventHandlerJavascript;
    }

    /**
     * This method checks that parent component of AjaxSettings is valid for adding event processing to all ajax request processing from it.
     *
     * @param parentComponent
     * @return
     */
    private boolean isParentComponentValid(UIComponent parentComponent) {
        return !(parentComponent instanceof UIViewRoot) && !(parentComponent instanceof UIForm);
    }

    protected String getRedirectLocationOnSessionExpired(FacesContext context) {
        UIViewRoot viewRoot = context.getViewRoot();
        String actionURL = context.getApplication().getViewHandler().getActionURL(context, viewRoot.getViewId());
        actionURL = context.getExternalContext().encodeActionURL(actionURL);
        return actionURL;
    }
}

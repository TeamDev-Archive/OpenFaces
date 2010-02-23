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
package org.openfaces.renderkit;

import org.openfaces.component.OUICommand;
import org.openfaces.component.ajax.AjaxInitializer;
import org.openfaces.util.FunctionCallScript;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */

public class RendererBase extends Renderer {

    protected void writeAttribute(ResponseWriter writer, String name, String value) throws IOException {
        RenderingUtil.writeAttribute(writer, name, value);
    }

    protected String writeIdAttribute(FacesContext facesContext, UIComponent component) throws IOException {
        return RenderingUtil.writeIdAttribute(facesContext, component);
    }

    protected void writeNameAttribute(FacesContext facesContext, UIComponent component) throws IOException {
        RenderingUtil.writeNameAttribute(facesContext, component);
    }

    protected void writeAttribute(ResponseWriter writer, String name, int value, int emptyValue) throws IOException {
        RenderingUtil.writeAttribute(writer, name, value, emptyValue);
    }

    protected boolean writeEventsWithAjaxSupport(FacesContext context, ResponseWriter writer, OUICommand command) throws IOException {
        return writeEventsWithAjaxSupport(context, writer, command, false);
    }

    protected boolean writeEventsWithAjaxSupport(FacesContext context, ResponseWriter writer, OUICommand command, boolean submitIfNoAjax) throws IOException {
        String userClickHandler = command.getOnclick();
        Script componentClickHandler = null;
        Iterable<String> render = command.getRender();
        Iterable<String> execute = command.getExecute();
        boolean ajaxJsRequired = false;
        if (render != null || (execute != null && execute.iterator().hasNext())) {
            if (render == null)
                throw new FacesException("'execute' attribute can't be specified without the 'render' attribute. Component id: " + command.getId());

            AjaxInitializer initializer = new AjaxInitializer();
            componentClickHandler = new ScriptBuilder().functionCall("O$._ajaxReload",
                    initializer.getRenderArray(context, command, render),
                    initializer.getAjaxParams(context, command)).semicolon().append("return false;");
            ajaxJsRequired = true;
        } else {
            if (submitIfNoAjax) {
                componentClickHandler = new FunctionCallScript("O$.submitWithParam", command, command, true);
            }
        }
        String clickHandler = RenderingUtil.joinScripts(
                userClickHandler,
                componentClickHandler != null ? componentClickHandler.toString() : null);
        if (!RenderingUtil.isNullOrEmpty(clickHandler))
            writer.writeAttribute("onclick", clickHandler, null);
        RenderingUtil.writeStandardEvents(writer, command, true);
        return ajaxJsRequired;
    }
}
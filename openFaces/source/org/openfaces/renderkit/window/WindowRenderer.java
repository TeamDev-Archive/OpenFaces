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
package org.openfaces.renderkit.window;

import org.openfaces.component.window.AbstractWindow;
import org.openfaces.component.window.Window;
import org.openfaces.renderkit.ComponentWithCaptionRenderer;
import org.openfaces.util.Rendering;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class WindowRenderer extends AbstractWindowRenderer {
    @Override
    protected void encodeContentPane(FacesContext context, AbstractWindow abstractWindow) throws IOException {
        Window win = (Window) abstractWindow;
        String clientId = win.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
//        writer.startElement("div", win);
//        writer.writeAttribute("class", "o_windowContentContainer", null);
        writer.startElement("div", win);
        writer.writeAttribute("id", clientId + MIDDLE_AREA_SUFFIX, null);
        Rendering.writeStyleAndClassAttributes(writer, win.getContentStyle(), win.getContentClass(), getDefaultContentClass());
        ComponentWithCaptionRenderer.renderChildren(context, abstractWindow);
        encodeCustomContent(context, abstractWindow);
        writer.endElement("div");
//        writer.endElement("div");
    }

    @Override
    protected String getDefaultContentClass() {
        return "o_window_content";
    }


    @Override
    protected String getDefaultTableClass() {
        return "o_window_table";
    }

    @Override
    protected String getDefaultHeight() {
        return "200px";
    }

}

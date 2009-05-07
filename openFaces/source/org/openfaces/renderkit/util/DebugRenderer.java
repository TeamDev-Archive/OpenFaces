/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.util;

import org.openfaces.component.window.AbstractWindow;
import org.openfaces.renderkit.window.WindowRenderer;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.ComponentUtil;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class DebugRenderer extends WindowRenderer {

    @Override
    protected void encodeContentPane(FacesContext context, AbstractWindow abstractWindow) throws IOException {
        HtmlOutputText log = (HtmlOutputText) ComponentUtil.getChildBySuffix(abstractWindow, "log");
        if (log == null)
            ComponentUtil.createChildComponent(context, abstractWindow, HtmlOutputText.COMPONENT_TYPE, "log");

        super.encodeContentPane(context, abstractWindow);
        RenderingUtil.renderInitScript(context, new ScriptBuilder().initScript(context, abstractWindow, "O$._initDebug"),
                new String[]{
                        ResourceUtil.getInternalResourceURL(context, DebugRenderer.class, "debug.js")
                });
    }
}

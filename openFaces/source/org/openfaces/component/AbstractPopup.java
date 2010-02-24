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
package org.openfaces.component;

import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.
 *
 * @author Kharchenko
 */
public abstract class AbstractPopup extends UIPanel {
    public static final String COMPONENT_TYPE = "org.openfaces.Popup";
    public static final String COMPONENT_FAMILY = "org.openfaces.Popup";

    private static final String POPUP_STYLE = "position: absolute; visibility: hidden;";

    protected AbstractPopup() {
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (!isRendered()) return;
        encodeOpeningTags(context);
    }

    protected void encodeOpeningTags(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", this);
        writer.writeAttribute("id", getClientId(context), "id");
        writer.writeAttribute("style", getPopupStyle(), null);
    }

    protected String getPopupStyle() {
        return POPUP_STYLE;
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        if (!isRendered()) return;
        encodeContent(context);
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        if (!isRendered()) return;
        encodeClosingTags(context);

        renderInitScript(context);
    }

    protected void encodeClosingTags(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
    }

    /**
     * Abstract method for popup's content rendering
     */
    protected abstract void encodeContent(FacesContext context) throws IOException;

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private void renderInitScript(FacesContext context) throws IOException {
        boolean useDisplayNoneByDefault = getUseDisplayNoneByDefault();
        Script initScript = new ScriptBuilder().initScript(context, this, "O$.Popup._init",
                useDisplayNoneByDefault).semicolon();
        Rendering.renderInitScript(context, initScript,
                Resources.getUtilJsURL(context),
                Resources.getInternalURL(context, RendererBase.class, "popup.js"));
    }

    protected boolean getUseDisplayNoneByDefault() {
        return false;
    }

}

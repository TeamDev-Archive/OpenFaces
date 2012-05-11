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
package org.openfaces.component;

import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
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
public class SimplePopup extends UIPanel {
    public static final String COMPONENT_TYPE = "org.openfaces.Popup";
    public static final String COMPONENT_FAMILY = "org.openfaces.Popup";

    private static final String POPUP_STYLE = "position: absolute; visibility: hidden;";

    private String styleClass;
    /* The purpose of storing a single UIComponent in a field and not saving it in state is a use-case when
       SimplePopup is created temporarily to embed a single component during the rendering time and then thrown away,
       the child-parent hierarchy of the appropriate component should stay unaffected, hence this component is rendered
       manually here without adding it as a child.
     */
    private UIComponent component;

    public SimplePopup() {
    }

    public SimplePopup(String styleClass, UIComponent component) {
        setStyleClass(styleClass);
        this.component = component;
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[] {
                super.saveState(context),
                styleClass
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        int i = 0;
        super.restoreState(context, stateArray[i++]);
        styleClass = (String) stateArray[i++];

    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
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
        String styleClass = getStyleClass();
        if (styleClass != null)
            writer.writeAttribute("class", styleClass, null);
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
    protected void encodeContent(FacesContext context) throws IOException {
        Rendering.renderChildren(context, this);
        if (component != null)
            component.encodeAll(context);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private void renderInitScript(FacesContext context) throws IOException {
        boolean useDisplayNoneByDefault = getUseDisplayNoneByDefault();
        Script initScript = new ScriptBuilder().initScript(context, this, "O$.Popup._init",
                useDisplayNoneByDefault).semicolon();
        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "popup.js"));
    }

    protected boolean getUseDisplayNoneByDefault() {
        return false;
    }

}

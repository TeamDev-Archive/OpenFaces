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

import org.openfaces.component.LoadingMode;
import org.openfaces.component.panel.LayeredPane;
import org.openfaces.component.panel.SubPanel;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class LayeredPaneRenderer extends MultiPageContainerRenderer {

    protected String getSelectionHiddenFieldSuffix() {
        return Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "index";
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;
        ResponseWriter writer = context.getResponseWriter();
        LayeredPane layeredPane = (LayeredPane) component;

        LoadingMode loadingMode = layeredPane.getLoadingMode();
        if (LoadingMode.AJAX_LAZY.equals(loadingMode) || LoadingMode.AJAX_ALWAYS.equals(loadingMode))
            AjaxUtil.prepareComponentForAjax(context, component);

        List<SubPanel> allItems = layeredPane.getSubPanels(true);

        writer.startElement("table", layeredPane);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("id", layeredPane.getClientId(context), "id");

        Rendering.writeComponentClassAttribute(writer, layeredPane);

        Rendering.writeStandardEvents(writer, layeredPane);

        writer.startElement("tr", layeredPane);
        writer.startElement("td", layeredPane);

        String containerClass = getContainerClass(context, layeredPane);

        encodePane(context, layeredPane, allItems, containerClass);

        encodeScriptsAndStyles(context, layeredPane, containerClass, allItems);
        Rendering.encodeClientActions(context, layeredPane);

        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private void encodeScriptsAndStyles(
            FacesContext context,
            LayeredPane layeredPane,
            String containerClass,
            List<SubPanel> allItems
    ) throws IOException {
        LoadingMode loadingMode = layeredPane.getLoadingMode();

        ScriptBuilder sb = new ScriptBuilder();
        sb.initScript(context, layeredPane, "O$.LayeredPane._init",
                Rendering.getRolloverClass(context, layeredPane),
                containerClass,
                loadingMode,
                allItems.size(),
                layeredPane.getSelectedIndex());

        Rendering.renderInitScript(context, sb,
                Resources.getUtilJsURL(context),
                Resources.getInternalURL(context, TabbedPaneRenderer.class, "multiPage.js"),
                Resources.getInternalURL(context, TabbedPaneRenderer.class, "layeredPane.js"));

        Styles.renderStyleClasses(context, layeredPane);
    }


}

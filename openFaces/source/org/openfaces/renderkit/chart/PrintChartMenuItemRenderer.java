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
package org.openfaces.renderkit.chart;

import org.openfaces.component.chart.Chart;
import org.openfaces.component.command.MenuItem;
import org.openfaces.component.command.PopupMenu;
import org.openfaces.renderkit.command.MenuItemRenderer;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import java.io.IOException;

@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class PrintChartMenuItemRenderer extends MenuItemRenderer implements ComponentSystemEventListener {
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        if (event instanceof PostAddToViewEvent)
            Resources.includeJQuery();
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        MenuItem menuItem = (MenuItem) component;
        if (menuItem.getValue() == null)
            menuItem.setValue("Print");

        menuItem.setOnclick(new ScriptBuilder().functionCall("O$.ChartMenu._printChart",
                getChart("<o:printChartMenuItem>", menuItem)).getScript());
        if (menuItem.getIconUrl() == null)
            menuItem.setIconUrl(Resources.internalURL(
                    context, "chart/print.png"));

        super.encodeBegin(context, component);
        Resources.renderJSLinkIfNeeded(context,
                Resources.internalURL(context, "thirdparty/printelement/printElement.js"));
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);
    }

    protected Chart getChart(String tagName, MenuItem menuItem) {
        UIComponent parent = menuItem.getParent();
        while (parent != null && (parent instanceof MenuItem || parent instanceof PopupMenu))
            parent = parent.getParent();
        if (!(parent instanceof Chart))
            throw new FacesException(tagName + " can only be added as a child of " +
                    "the <o:chart> component (either directly or as its sub-menu).");
        return (Chart) parent;
    }
}

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
package org.openfaces.renderkit.timetable;

import org.openfaces.component.timetable.DayTable;
import org.openfaces.component.timetable.EventPreview;
import org.openfaces.component.window.PopupLayer;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class EventPreviewRenderer extends RendererBase {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        EventPreview eventPreview = (EventPreview) component;
        String clientId = eventPreview.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", eventPreview);
        writer.writeAttribute("id", clientId, null);

        PopupLayer popupLayer = Components.getOrCreateFacet(
                context, component, PopupLayer.COMPONENT_TYPE, "_popupLayer", PopupLayer.class);
        popupLayer.setId(eventPreview.getId() + Rendering.SERVER_ID_SUFFIX_SEPARATOR + "popupLayer");
        popupLayer.setStyle(eventPreview.getStyle());
        popupLayer.setStyleClass(eventPreview.getStyleClass());
        popupLayer.setHideOnOuterClick(true);
        popupLayer.encodeAll(context);

        DayTable dayTable = (DayTable) eventPreview.getParent();
        Rendering.renderInitScript(context,
                new ScriptBuilder().initScript(context, eventPreview, "O$._initEventPreview",
                        dayTable,
                        eventPreview.getShowingDelay(),
                        Styles.getCSSClass(context, eventPreview, eventPreview.getStyle(), StyleGroup.regularStyleGroup(), eventPreview.getStyleClass(), "o_eventPreview"),
                        Styles.getCSSClass(context, eventPreview, eventPreview.getEventNameStyle(), StyleGroup.regularStyleGroup(), eventPreview.getEventNameClass()),
                        Styles.getCSSClass(context, eventPreview, eventPreview.getEventDescriptionStyle(), eventPreview.getEventDescriptionClass()),
                        eventPreview.getHorizontalAlignment(),
                        eventPreview.getVerticalAlignment(),
                        eventPreview.getHorizontalDistance(),
                        eventPreview.getVerticalDistance()),
                Resources.getUtilJsURL(context),
                Resources.getInternalURL(context, DayTableRenderer.class, "dayTable.js"));

        writer.endElement("div");

        Styles.renderStyleClasses(context, component);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}

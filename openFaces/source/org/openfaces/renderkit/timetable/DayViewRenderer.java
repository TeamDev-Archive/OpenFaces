/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.timetable;

import org.openfaces.component.timetable.MonthTable;
import org.openfaces.util.Resources;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class DayViewRenderer extends org.openfaces.renderkit.RendererBase {

    private static String BUTTON_SUFFIX = "::button";
    private static String EXPANDED_VIEW_SUFFIX = "::expandedDayView";


    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);

    }



    protected void encodeButton(FacesContext context, UIComponent component, String buttonOrientation ) throws IOException {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        MonthTable fieldComponent = (MonthTable) component;
        ResponseWriter writer = context.getResponseWriter();

        // get all ids (main, button, popup)
        String clientId = fieldComponent.getClientId(currentInstance);
        String buttonId = clientId + EXPANDED_VIEW_SUFFIX + BUTTON_SUFFIX;

        writer.writeAttribute("nowrap", "nowrap", null);

        // Render drop down button
        writer.writeAttribute("id", buttonId + "::" + buttonOrientation, null);
        writer.writeAttribute("align", "center", null);
        writer.writeAttribute("valign", "middle", null);

        String imageUrl;
        String buttonImageUrl = (String) fieldComponent.getAttributes().get("buttonImageUrl");
        if (buttonOrientation.equals("up"))
            imageUrl = Resources.getURL(context, buttonImageUrl, null, "input/dropButton.gif");
        else
            imageUrl = Resources.getURL(context, buttonImageUrl, null, "input/dropButton.gif");
        writer.startElement("img", fieldComponent);
        writer.writeAttribute("id", buttonId +"::" + buttonOrientation + "::img", null);
        writer.writeAttribute("src", imageUrl, null);
        writer.endElement("img");
    }



    private void encodeExpandedDayView(FacesContext context, final MonthTable timetableView, String clientId) throws IOException {
        String expandDayViewId = clientId + EXPANDED_VIEW_SUFFIX;
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", timetableView);
        writer.writeAttribute("id", expandDayViewId , null);
        writer.writeAttribute("style", "background-color: red; position: absolute;width:50px;height:50px;padding:2px;z-index:150;", null);

        writer.startElement("div", timetableView);
        encodeButton(context, timetableView, "up" );
        writer.endElement("div");

        writer.startElement("div", timetableView);
        writer.writeAttribute("style", "background-color: green; height: 100%;margin-top: -10px; margin-bottom: -10px; overflow:hidden;position: relative;", null);
        writer.writeAttribute("id", expandDayViewId + "::eventBlock" , null);
        writer.endElement("div");


        writer.startElement("div", timetableView);
        encodeButton(context, timetableView, "down" );
        writer.endElement("div");

        writer.endElement("div");
    }

    /*   Styles.addStyleJsonParam(context, timetableView, stylingParams, "moreLinkElementClass",
                timetableView.getMoreLinkElementStyle(), timetableView.getMoreLinkElementClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "moreLinkClass",
                timetableView.getMoreLinkStyle(), timetableView.getMoreLinkClass());
        Rendering.addJsonParam(stylingParams, "moreLinkText", timetableView.getMoreLinkText());

       Styles.addStyleJsonParam(context, timetableView, stylingParams, "dayViewButtonClass",
                timetableView.getDayViewButtonStyle(), timetableView.getDayViewButtonClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "dayViewRolloverButtonClass",
                timetableView.getDayViewRolloverButtonStyle(), timetableView.getDayViewRolloverButtonClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "dayViewPressedButtonClass",
                timetableView.getDayViewPressedButtonStyle(), timetableView.getDayViewPressedButtonClass());*/

}

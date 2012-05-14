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
package org.openfaces.renderkit.timetable;

import org.openfaces.component.LoadingMode;
import org.openfaces.component.panel.LayeredPane;
import org.openfaces.component.panel.SubPanel;
import org.openfaces.component.timetable.DayTable;
import org.openfaces.component.timetable.MonthTable;
import org.openfaces.component.timetable.Timetable;
import org.openfaces.component.timetable.TimetableView;
import org.openfaces.component.timetable.WeekTable;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;
import org.openfaces.util.Faces;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TimetableRenderer extends TimetableRendererBase {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Timetable timetable = (Timetable) component;

        if (!component.isRendered())
            return;

        Rendering.registerDateTimeFormatObject(timetable.getLocale());
        AjaxUtil.prepareComponentForAjax(context, timetable);

        timetable.setEvent(null);
        String clientId = timetable.getClientId(context);
        writer.startElement("table", timetable);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("class", Styles.getCSSClass(context,
                timetable, timetable.getStyle(), "o_timetableView", timetable.getStyleClass()), null);
        Rendering.writeStandardEvents(writer, timetable);
        writer.startElement("tbody", timetable);

        renderHeader(context, timetable);

        writer.startElement("tr", timetable);
        writer.writeAttribute("class", "o_timetableView_tableRow", null);
        writer.startElement("td", timetable);
        writer.writeAttribute("style", "height: 100%", null);

        LayeredPane layeredPane = getLayeredPane(timetable);
        layeredPane.encodeAll(context);

        List<String> viewIds = new ArrayList<String>();
        for (UIComponent c : layeredPane.getChildren()) {
            SubPanel subPanel = (SubPanel) c;
            if (subPanel.getChildCount() != 1) throw new IllegalStateException();
            VirtualContainer container = (VirtualContainer) subPanel.getChildren().get(0);
            TimetableView timetableView = (TimetableView) container.getVirtualChild();
            String viewId = timetableView.getClientId(context);
            viewIds.add(viewId);
        }

        Rendering.renderInitScript(context, new ScriptBuilder().initScript(context, timetable, "O$.Timetable._init",
                layeredPane,
                viewIds,
                timetable.getViewType(),
                Rendering.getEventsParam(timetable, "onviewtypechange")

        ), getTimetableJsURL(context));

        writer.endElement("td");
        writer.endElement("tr");

        renderFooter(context, timetable);

        writer.endElement("tbody");
        writer.endElement("table");

        Styles.renderStyleClasses(context, timetable);
    }

    public static String getTimetableJsURL(FacesContext context) {
        return Resources.internalURL(context, "timetable/timetable.js");
    }

    @Override
    protected void writeHeaderTableAttributes(ResponseWriter writer) throws IOException {
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("border", "0", null);
    }

    @Override
    protected void writeHeaderRightAreaAttributes(ResponseWriter writer, TimetableView timetableView) throws IOException {
        Timetable timetable = (Timetable) timetableView;
        Rendering.writeStyleAndClassAttributes(writer, timetable, "headerRight", "o_timetable_headerRightCell");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    private LayeredPane getLayeredPane(final Timetable timetable) {
        LayeredPane layeredPane = Components.getChildWithClass(timetable, LayeredPane.class, "layeredPane");
        layeredPane.setStyle("width: 100%");
        layeredPane.setContainerStyle("padding: 0");
        layeredPane.setLoadingMode(LoadingMode.CLIENT);
        if (layeredPane.getChildCount() == 0) {
            List<UIComponent> children = layeredPane.getChildren();
            children.add(new SubPanel(null, new MonthTableVirtualContainer()));
            children.add(new SubPanel(null, new WeekTableVirtualContainer()));
            children.add(new SubPanel(null, new DayTableVirtualContainer()));
        }

        Timetable.ViewType currentViewType = timetable.getViewType();
        List<UIComponent> children = layeredPane.getChildren();
        int viewIndex = 0;
        for (int i = 0, count = children.size(); i < count; i++) {
            SubPanel subPanel = (SubPanel) children.get(i);
            if (subPanel.getChildCount() != 1)
                throw new IllegalArgumentException("One child component expected, but was " + subPanel.getChildCount() + "; panel index: " + i);
            VirtualContainer container = (VirtualContainer) subPanel.getChildren().get(0);
            TimetableView viewInThisPanel = (TimetableView) container.getVirtualChild();
            Timetable.ViewType viewTypeType = viewInThisPanel.getType();
            if (viewTypeType == currentViewType) {
                viewIndex = i;
                break;
            }

        }
        layeredPane.setSelectedIndex(viewIndex);
        return layeredPane;
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        Timetable timetable = (Timetable) component;
        String viewStr = Faces.requestParam(timetable.getClientId(context) + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "view");
        Timetable.ViewType viewType = Timetable.ViewType.valueOf(viewStr.toUpperCase());
        timetable.setViewType(viewType);
    }

    public static abstract class TimetableViewVirtualContainer<C extends UIComponent> extends VirtualContainer<C> {
        private Timetable timetable;

        protected Timetable getTimetable() {
            if (timetable == null)
                timetable = Components.getParentWithClass(this, Timetable.class);
            return timetable;
        }

    }

    public static class MonthTableVirtualContainer extends TimetableViewVirtualContainer<MonthTable> {
        @Override
        protected MonthTable getVirtualChild() {
            return getTimetable().getMonthView();
        }
    }

    public static class WeekTableVirtualContainer extends TimetableViewVirtualContainer<WeekTable> {
        @Override
        protected WeekTable getVirtualChild() {
            return getTimetable().getWeekView();
        }
    }

    public static class DayTableVirtualContainer extends TimetableViewVirtualContainer<DayTable> {
        @Override
        protected DayTable getVirtualChild() {
            return getTimetable().getDayView();
        }
    }
}

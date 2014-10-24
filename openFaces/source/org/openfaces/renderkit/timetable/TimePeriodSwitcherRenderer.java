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
import org.openfaces.component.timetable.AbstractSwitcher;
import org.openfaces.component.timetable.TimePeriodSwitcher;
import org.openfaces.component.timetable.Timetable;
import org.openfaces.component.timetable.TimetableView;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

public class TimePeriodSwitcherRenderer extends RendererBase {

    private static String LAYERED_PANE_SUFFIX = "_layeredPane";
    public static String POPUP_SUFFIX = "_popup";
    public static String CALENDAR_SUFFIX = "_calendar";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        TimePeriodSwitcher switcher = (TimePeriodSwitcher) component;
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("span", switcher);
        writeIdAttribute(context, switcher);
        LayeredPane layeredPane = getLayeredPane(switcher);
        layeredPane.encodeAll(context);

        Timetable timetable = switcher.getTimetableView();
        Rendering.renderInitScript(context, new ScriptBuilder().initScript(context, switcher,
                "O$.TimePeriodSwitcher._init", timetable),
                Resources.utilJsURL(context),
                TimetableRenderer.getTimetableJsURL(context));

        writer.endElement("span");
    }

    private LayeredPane getLayeredPane(TimePeriodSwitcher switcher) {
        FacesContext context = FacesContext.getCurrentInstance();

        LayeredPane layeredPane = Components.getChildWithClass(switcher, LayeredPane.class, "layeredPane");
        layeredPane.setStyleClass("o_timetablePeriodSwitcher_lp");
        layeredPane.setId(switcher.getId() + LAYERED_PANE_SUFFIX);
        layeredPane.setLoadingMode(LoadingMode.CLIENT);
        if (layeredPane.getChildCount() == 0) {
            List<UIComponent> children = layeredPane.getChildren();
            children.add(new SubPanel(null, switcher.getMonthSwitcher()));
            children.add(new SubPanel(null, switcher.getWeekSwitcher()));
            children.add(new SubPanel(null, switcher.getDaySwitcher()));
        }

        Timetable timetable = switcher.getTimetableView();

        Timetable.ViewType currentViewType = timetable.getViewType();
        List<UIComponent> children = layeredPane.getChildren();
        int viewIndex = 0;
        for (int i = 0, count = children.size(); i < count; i++) {
            SubPanel subPanel = (SubPanel) children.get(i);
            if (subPanel.getChildCount() != 1)
                throw new IllegalArgumentException("One child component expected, but was " + subPanel.getChildCount() + "; panel index: " + i);
            AbstractSwitcher viewInThisPanel = (AbstractSwitcher) subPanel.getChildren().get(0);
            Timetable.ViewType viewType = viewInThisPanel.getApplicableViewType();
            TimetableView appropriateView = timetable.getViewByType(viewType);
            viewInThisPanel.setFor(":" + appropriateView.getClientId(context));
            if (viewType == currentViewType) {
                viewIndex = i;
            }

        }
        layeredPane.setSelectedIndex(viewIndex);
        return layeredPane;
    }


    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }
}

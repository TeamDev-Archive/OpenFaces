/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.chart;

import org.openfaces.component.chart.ChartMenu;
import org.openfaces.component.chart.PrintChartMenuItem;
import org.openfaces.component.chart.SaveChartMenuItem;
import org.openfaces.renderkit.command.PopupMenuRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class ChartMenuRenderer extends PopupMenuRenderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ChartMenu menu = (ChartMenu) component;
        if (menu.getChildren().size() == 0) {
            List<UIComponent> menuItems = menu.getChildren();
            menuItems.add(new SaveChartMenuItem());
            menuItems.add(new PrintChartMenuItem());
        }

        super.encodeBegin(context, component);
    }


}

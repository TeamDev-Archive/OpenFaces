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
package org.openfaces.taglib.internal.chart;

import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartTitle;
import org.openfaces.component.chart.ChartViewType;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class ChartTag extends AbstractStyledComponentTag {

    public static final String RENDERER_TYPE = "org.openfaces.ChartRenderer";

    public String getComponentType() {
        return "org.openfaces.Chart";
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        Chart chart = (Chart) component;

        setStringProperty(chart, "textStyle");
        setPropertyBinding(chart, "model");
        setEnumerationProperty(chart, "view", ChartViewType.class);

        String titleText = getPropertyValue("titleText");
        if (titleText != null) {
            ChartTitle title = new ChartTitle();
            setStringProperty(title, "text", titleText, "titleText");
            chart.getChildren().add(title);
            title.setTextStyle("font-size: 14pt;");
        }

        setIntProperty(chart, "height");
        setIntProperty(chart, "width");
        setBooleanProperty(chart, "legendVisible");
    }
}

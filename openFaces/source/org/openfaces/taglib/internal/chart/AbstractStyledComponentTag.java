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

import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public abstract class AbstractStyledComponentTag extends AbstractComponentTag {

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent uiComponent) {
        super.setComponentProperties(facesContext, uiComponent);
        checkJFreeChart();
        setStringProperty(uiComponent, "style");
    }

    private static void checkJFreeChart() {
        try {
            Class.forName("org.jfree.chart.JFreeChart");
            Class.forName("org.jfree.JCommon");
        } catch (ClassNotFoundException exc) {
            throw new RuntimeException("Cannot find JFreeChart library. Make sure jfreechart.jar and jcommon.jar libraries are available.", exc);
        }
    }
}

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
package org.openfaces.component.chart;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class GradientLineAreaFill extends UIComponentBase implements LineAreaFill {
    public static final String COMPONENT_TYPE = "org.openfaces.GradientLineAreaFill";
    public static final String COMPONENT_FAMILY = "org.openfaces.GradientLineAreaFill";

    private Double minValueTransparency;
    private Double maxValueTransparency;

    public GradientLineAreaFill() {
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
    }

    public double getMinValueTransparency() {
        return ValueBindings.get(this, "minValueTransparency", minValueTransparency, 0.5);
    }

    public void setMinValueTransparency(double minValueTransparency) {
        this.minValueTransparency = minValueTransparency;
    }

    public double getMaxValueTransparency() {
        return ValueBindings.get(this, "maxValueTransparency", maxValueTransparency, 0.6);
    }

    public void setMaxValueTransparency(double maxValueTransparency) {
        this.maxValueTransparency = maxValueTransparency;
    }
}

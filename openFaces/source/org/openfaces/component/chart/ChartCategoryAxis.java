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

import javax.faces.context.FacesContext;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartCategoryAxis extends ChartAxis {

    private CategoryAxisLabelPosition position = CategoryAxisLabelPosition.STANDARD;
    private Double lowerMargin;
    private Double upperMargin;
    private Double categoryMargin;

    public CategoryAxisLabelPosition getPosition() {
        return ValueBindings.get(this, "position", position, CategoryAxisLabelPosition.STANDARD,
                CategoryAxisLabelPosition.class);
    }

    public void setPosition(CategoryAxisLabelPosition position) {
        this.position = position;
    }
    
    public Double getCategoryMargin() {
        return ValueBindings.get(this, "categoryMargin", categoryMargin, null, Double.class);
    }

    public void setCategoryMargin(Double categoryMargin) {
        this.categoryMargin = categoryMargin;
    }

    public Double getLowerMargin() {
        return ValueBindings.get(this, "lowerMargin", lowerMargin, null, Double.class);
    }

    public void setLowerMargin(Double lowerMargin) {
        this.lowerMargin = lowerMargin;
    }

    public Double getUpperMargin() {
        return ValueBindings.get(this, "upperMargin", upperMargin, null, Double.class);
    }

    public void setUpperMargin(Double upperMargin) {
        this.upperMargin = upperMargin;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                saveAttachedState(context, position),
                categoryMargin,
                lowerMargin,
                upperMargin
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        position = (CategoryAxisLabelPosition) restoreAttachedState(facesContext, state[i++]);
        categoryMargin = (Double) state[i++];
        lowerMargin = (Double) state[i++];
        upperMargin = (Double) state[i++];
    }

}

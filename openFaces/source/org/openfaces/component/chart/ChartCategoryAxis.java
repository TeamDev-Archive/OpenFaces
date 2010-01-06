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
package org.openfaces.component.chart;

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartCategoryAxis extends ChartAxis {

    private CategoryAxisLabelPosition position = CategoryAxisLabelPosition.STANDARD;

    public CategoryAxisLabelPosition getPosition() {
        return ValueBindings.get(this, "position", position, CategoryAxisLabelPosition.STANDARD,
                CategoryAxisLabelPosition.class);
    }

    public void setPosition(CategoryAxisLabelPosition position) {
        this.position = position;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                saveAttachedState(context, position)
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        position = (CategoryAxisLabelPosition) restoreAttachedState(facesContext, state[i++]);

    }

}

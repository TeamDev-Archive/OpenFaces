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
package org.openfaces.component.table;

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class SelectionColumn extends BaseColumn {
    public static final String COMPONENT_TYPE = "org.openfaces.SelectionColumn";
    public static final String COMPONENT_FAMILY = "org.openfaces.SelectionColumn";

    private static final String DEFAULT_CLASS = "o_selection_column";

    private Boolean sortable;

    public SelectionColumn() {
        setRendererType("org.openfaces.SelectionColumnRenderer");
        getAttributes().put("defaultStyle", DEFAULT_CLASS);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context), sortable};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        super.restoreState(context, state[0]);
        sortable = (Boolean) state[1];
    }

    public boolean getSortable() {
        return ValueBindings.get(this, "sortable", sortable, false);
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    @Override
    protected boolean getDefaultMenuAllowed() {
        return false;
    }
}

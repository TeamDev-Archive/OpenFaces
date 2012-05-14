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

public class HierarchicalNodeSelection extends MultipleNodeSelection {
    public static final String COMPONENT_TYPE = "org.openfaces.HierarchicalNodeSelection";
    public static final String COMPONENT_FAMILY = "org.openfaces.HierarchicalNodeSelection";

    private Boolean trackLeafNodesOnly;

    public HierarchicalNodeSelection() {
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                trackLeafNodesOnly

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        trackLeafNodesOnly = (Boolean) state[i++];
    }

    @Override
    public Mode getSelectionMode() {
        return Mode.HIERARCHICAL;
    }

    public boolean getTrackLeafNodesOnly() {
        return ValueBindings.get(this, "trackLeafNodesOnly", trackLeafNodesOnly, true);
    }

    public void setTrackLeafNodesOnly(boolean trackLeafNodesOnly) {
        this.trackLeafNodesOnly = trackLeafNodesOnly;
    }
}

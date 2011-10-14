/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.table;

import org.openfaces.component.OUIComponentBase;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

public class RowGrouping extends OUIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.RowGrouping";
    public static final String COMPONENT_FAMILY = "org.openfaces.RowGrouping";

    private List<GroupingRule> groupingRules = new ArrayList<GroupingRule>();

    public RowGrouping() {
        setRendererType("org.openfaces.RowGroupingRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                saveAttachedState(context, groupingRules)

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        groupingRules = (List<GroupingRule>) restoreAttachedState(context, state[i++]);
    }

    public List<GroupingRule> getGroupingRules() {
        return groupingRules;
    }

    public void setGroupingRules(List<GroupingRule> groupingRules) {
        if (groupingRules == null)
            throw new IllegalArgumentException("groupingRules cannot be null");
        this.groupingRules = groupingRules;
    }

}

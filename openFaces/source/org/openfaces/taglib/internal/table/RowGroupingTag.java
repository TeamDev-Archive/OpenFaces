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
package org.openfaces.taglib.internal.table;

import org.openfaces.component.table.RowGrouping;
import org.openfaces.component.table.RowGroupingSelectionMode;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class RowGroupingTag extends AbstractComponentTag {

    public String getComponentType() {
        return RowGrouping.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.RowGroupingRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        setValueExpressionProperty(component, "groupingRules");
        setStringProperty(component, "columnHeaderVar");
        setStringProperty(component, "groupingValueVar");
        setStringProperty(component, "groupingValueStringVar");
        setValueExpressionProperty(component, "groupHeaderText");
        setBooleanProperty(component, "groupOnHeaderClick");
        setBooleanProperty(component, "hideGroupingColumns");
        setExpansionStateProperty(component, "expansionState");
        setEnumerationProperty(component, "selectionMode", RowGroupingSelectionMode.class);
        setStringProperty(component, "groupHeaderRowStyle");
        setStringProperty(component, "groupHeaderRowClass");
        setStringProperty(component, "groupFooterRowStyle");
        setStringProperty(component, "groupFooterRowClass");
        setStringProperty(component, "inGroupHeaderRowStyle");
        setStringProperty(component, "inGroupHeaderRowClass");
        setStringProperty(component, "inGroupFooterRowStyle");
        setStringProperty(component, "inGroupFooterRowClass");
        setBooleanProperty(component, "inGroupFootersCollapsible");
        setBooleanProperty(component, "groupFootersCollapsible");
    }
}

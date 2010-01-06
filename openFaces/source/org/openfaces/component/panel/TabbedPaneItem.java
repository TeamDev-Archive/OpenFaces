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
package org.openfaces.component.panel;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Andrew Palval
 */
public class TabbedPaneItem extends UIComponentBase implements Serializable {
    public static final String COMPONENT_TYPE = "org.openfaces.TabbedPaneItem";
    public static final String COMPONENT_FAMILY = "org.openfaces.TabbedPaneItem";

    private static final String TAB_FACET_NAME = "tab";

    public TabbedPaneItem() {
    }

    public TabbedPaneItem(UIComponent tab, UIComponent... children) {
        setTab(tab);
        getChildren().addAll(Arrays.asList(children));
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public UIComponent getTab() {
        return getFacet(TAB_FACET_NAME);
    }

    public void setTab(UIComponent tab) {
        getFacets().put(TAB_FACET_NAME, tab);
    }

}

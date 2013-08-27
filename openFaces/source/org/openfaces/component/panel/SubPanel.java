/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.panel;

import org.openfaces.component.select.TabAlignment;
import org.openfaces.component.select.TabPlacement;
import org.openfaces.util.ValueBindings;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Andrew Palval
 */
public class SubPanel extends AbstractPanelWithCaption implements Serializable {
    public static final String COMPONENT_TYPE = "org.openfaces.SubPanel";
    public static final String COMPONENT_FAMILY = "org.openfaces.SubPanel";

    private boolean disabled;

    public boolean isDisabled() {
        return ValueBindings.get(this, "disabled", disabled, false);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public SubPanel() {
    }

    public SubPanel(UIComponent caption) {
        setCaptionFacet(caption);
    }

    public SubPanel(UIComponent caption, UIComponent... children) {
        if (caption != null)
            setCaptionFacet(caption);
        getChildren().addAll(Arrays.asList(children));
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                disabled,
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        disabled = (Boolean) values[i++];
    }

}

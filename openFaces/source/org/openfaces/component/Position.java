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
package org.openfaces.component;

import org.openfaces.component.HorizontalAlignment;
import org.openfaces.component.VerticalAlignment;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class Position extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.Position";
    public static final String COMPONENT_FAMILY = "org.openfaces.Position";
    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;
    private String horizontalDistance;
    private String verticalDistance;
    private String by;

    public Position() {
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                by,
                horizontalAlignment,
                verticalAlignment,
                horizontalDistance,
                verticalDistance,
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        by = (String) state[i++];
        horizontalAlignment = (HorizontalAlignment) state[i++];
        verticalAlignment = (VerticalAlignment) state[i++];
        horizontalDistance = (String) state[i++];
        verticalDistance = (String) state[i++];
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return ValueBindings.get(this, "horizontalAlignment", horizontalAlignment, HorizontalAlignment.class);
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public VerticalAlignment getVerticalAlignment() {
        return ValueBindings.get(this, "verticalAlignment", verticalAlignment, VerticalAlignment.class);
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public String getHorizontalDistance() {
        return ValueBindings.get(this, "horizontalDistance", horizontalDistance);
    }

    public void setHorizontalDistance(String horizontalDistance) {
        this.horizontalDistance = horizontalDistance;
    }

    public String getVerticalDistance() {
        return ValueBindings.get(this, "verticalDistance", verticalDistance);
    }

    public void setVerticalDistance(String verticalDistance) {
        this.verticalDistance = verticalDistance;
    }

    public String getBy() {
        return ValueBindings.get(this, "by", by);
    }

    public void setBy(String by) {
        this.by = by;
    }
}

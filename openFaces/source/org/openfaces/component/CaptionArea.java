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

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class CaptionArea extends UIPanel {
    public static final String COMPONENT_TYPE = "org.openfaces.CaptionArea";
    public static final String COMPONENT_FAMILY = "org.openfaces.CaptionArea";

    private Side alignment;

    public CaptionArea() {
        setRendererType("org.openfaces.CaptionAreaRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Side getAlignment() {
        return ValueBindings.get(this, "alignment",
                alignment, Side.RIGHT, Side.class);
    }

    public void setAlignment(Side alignment) {
        this.alignment = alignment;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                alignment
        };
    }

    @Override
    public void restoreState(FacesContext context, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(context, state[i++]);
        alignment = (Side) state[i++];
    }
}

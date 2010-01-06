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

import org.openfaces.component.OUIPanel;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Alexey Tarasyuk
 */

public class BorderLayoutPanel extends OUIPanel {
    public static final String COMPONENT_TYPE = "org.openfaces.BorderLayoutPanel";
    public static final String COMPONENT_FAMILY = "org.openfaces.BorderLayoutPanel";

    private String contentStyle;
    private String contentClass;
    private String oncontentresize;

    public BorderLayoutPanel() {
        setRendererType("org.openfaces.BorderLayoutPanelRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getContentStyle() {
        return ValueBindings.get(this, "contentStyle", contentStyle);
    }

    public void setContentStyle(String contentStyle) {
        this.contentStyle = contentStyle;
    }

    public String getContentClass() {
        return ValueBindings.get(this, "contentClass", contentClass);
    }

    public void setContentClass(String contentClass) {
        this.contentClass = contentClass;
    }

    public String getOncontentresize() {
        return ValueBindings.get(this, "oncontentresize", oncontentresize);
    }

    public void setOncontentresize(String oncontentresize) {
        this.oncontentresize = oncontentresize;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                contentStyle, contentClass, oncontentresize
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        contentStyle = (String) values[i++];
        contentClass = (String) values[i++];
        oncontentresize = (String) values[i];
    }

}
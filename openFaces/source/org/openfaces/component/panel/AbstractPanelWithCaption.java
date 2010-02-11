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

import org.openfaces.component.ComponentWithCaption;
import org.openfaces.component.OUIPanel;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractPanelWithCaption extends OUIPanel implements ComponentWithCaption {
    private static final String CAPTION_FACET_NAME = "caption";

    private String caption;

    private String captionStyle;
    private String captionClass;
    private String contentStyle;
    private String contentClass;

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                caption,
                captionStyle,
                captionClass,
                contentStyle,
                contentClass};
    }

    @Override
    public void restoreState(FacesContext context, Object object) {
        Object[] values = (Object[]) object;
        int i = 0;
        super.restoreState(context, values[i++]);
        caption = (String) values[i++];
        captionStyle = (String) values[i++];
        captionClass = (String) values[i++];
        contentStyle = (String) values[i++];
        contentClass = (String) values[i++];
    }

    public String getCaption() {
        return ValueBindings.get(this, "caption", caption);
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public UIComponent getCaptionFacet() {
        return getFacet(CAPTION_FACET_NAME);
    }

    public void setCaptionFacet(UIComponent component) {
        getFacets().put(CAPTION_FACET_NAME, component);
    }

    public String getCaptionStyle() {
        return ValueBindings.get(this, "captionStyle", captionStyle);
    }

    public void setCaptionStyle(String captionStyle) {
        this.captionStyle = captionStyle;
    }

    public String getCaptionClass() {
        return ValueBindings.get(this, "captionClass", captionClass);
    }

    public void setCaptionClass(String captionClass) {
        this.captionClass = captionClass;
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


}

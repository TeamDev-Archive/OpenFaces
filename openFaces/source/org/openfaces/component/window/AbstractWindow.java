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
package org.openfaces.component.window;

import org.openfaces.component.ComponentWithCaption;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class AbstractWindow extends PopupLayer implements ComponentWithCaption {
    private static final String CAPTION_FACET_NAME = "caption";

    private String caption;

    private String captionStyle;
    private String captionClass;
    private String contentStyle;
    private String contentClass;

    private Boolean draggableByContent;
    private Boolean resizable;
    private String minWidth;
    private String minHeight;

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

    protected String getDefaultCaptionText() {
        return null;
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

    public boolean isResizable() {
        return ValueBindings.get(this, "resizable", resizable, isResizableByDefault());
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public boolean isDraggableByContent() {
        return ValueBindings.get(this, "draggableByContent", draggableByContent, isDraggableByContentByDefault());
    }

    public void setDraggableByContent(boolean draggableByContent) {
        this.draggableByContent = draggableByContent;
    }

    public String getMinWidth() {
        return ValueBindings.get(this, "minWidth", minWidth, getDefaultMinWidth());
    }

    public void setMinWidth(String minWidth) {
        this.minWidth = minWidth;
    }

    public String getMinHeight() {
        return ValueBindings.get(this, "minHeight", minHeight, getDefaultMinHeight());
    }

    protected String getDefaultMinWidth() {
        return "150px";
    }

    protected String getDefaultMinHeight() {
        return "80px";
    }

    public void setMinHeight(String minHeight) {
        this.minHeight = minHeight;
    }

    protected boolean getShowCloseButtonByDefault() {
        return false;
    }

    protected boolean isDraggableByContentByDefault() {
        return false;
    }

    protected boolean isResizableByDefault() {
        return true;
    }


    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                resizable,
                minWidth,
                minHeight,
                captionStyle,
                captionClass,
                contentStyle,
                contentClass,
                draggableByContent,
                caption};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object state[] = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        resizable = (Boolean) state[i++];
        minWidth = (String) state[i++];
        minHeight = (String) state[i++];
        captionStyle = (String) state[i++];
        captionClass = (String) state[i++];
        contentStyle = (String) state[i++];
        contentClass = (String) state[i++];
        draggableByContent = (Boolean) state[i++];
        caption = (String) state[i++];
    }

}

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

import org.openfaces.component.OUIPanel;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * The PopupLayer component is a container for other JSF components that is displayed
 * over the page contents. The size, placement, timeout value, and modality of the component
 * can be easily customized. A convenient API lets you manage the display behavior of the
 * PopupLayer component on the client side.
 *
 * @author Andrew Palval
 */
public class PopupLayer extends OUIPanel {
    public static final String COMPONENT_TYPE = "org.openfaces.PopupLayer";
    public static final String COMPONENT_FAMILY = "org.openfaces.PopupLayer";

    private Boolean visible;
    private Boolean hideOnOuterClick;
    private Boolean hideOnEsc;
    private Integer hidingTimeout;
    private Boolean modal;

    private String left;
    private String top;
    private String width;
    private String height;

    private String modalLayerStyle;
    private String modalLayerClass;

    private String onshow;
    private String onhide;

    private String anchorElementId;
    private String anchorX;
    private String anchorY;

    private Boolean draggable;
    private String ondragstart;
    private String ondragend;

    public PopupLayer() {
        setRendererType("org.openfaces.PopupLayerRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getModalLayerStyle() {
        return ValueBindings.get(this, "modalLayerStyle", modalLayerStyle);
    }

    public void setModalLayerStyle(String modalLayerStyle) {
        this.modalLayerStyle = modalLayerStyle;
    }

    public String getModalLayerClass() {
        return ValueBindings.get(this, "modalLayerClass", modalLayerClass);
    }

    public void setModalLayerClass(String modalLayerClass) {
        this.modalLayerClass = modalLayerClass;
    }

    public boolean getHideOnOuterClick() {
        return ValueBindings.get(this, "hideOnOuterClick", hideOnOuterClick, false);
    }

    public void setHideOnOuterClick(boolean hideOnOuterClick) {
        this.hideOnOuterClick = hideOnOuterClick;
    }

    public boolean getHideOnEsc() {
        return ValueBindings.get(this, "hideOnEsc", hideOnEsc, true);
    }

    public void setHideOnEsc(boolean hideOnEsc) {
        this.hideOnEsc = hideOnEsc;
    }

    public boolean isModal() {
        return ValueBindings.get(this, "modal", modal, false);
    }

    public void setModal(boolean modal) {
        this.modal = modal;
    }

    public boolean isVisible() {
        return ValueBindings.get(this, "visible", visible, false);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getOndragstart() {
        return ondragstart;
    }

    public void setOndragstart(String ondragstart) {
        this.ondragstart = ondragstart;
    }

    public String getOndragend() {
        return ondragend;
    }

    public void setOndragend(String ondragend) {
        this.ondragend = ondragend;
    }

    public boolean getDraggable() {
        return ValueBindings.get(this, "draggable", draggable, isDraggableByDefault());
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    protected boolean isDraggableByDefault() {
        return false;
    }

    protected String getDefaultWidth() {
        return null;
    }

    protected String getDefaultHeight() {
        return null;
    }

    public String getAnchorElementId() { // todo: is this attrubte needed?
        return ValueBindings.get(this, "anchorElementId", anchorElementId);
    }

    public void setAnchorElementId(String anchorElementId) {
        this.anchorElementId = anchorElementId;
    }

    public String getAnchorX() {
        return ValueBindings.get(this, "anchorX", anchorX);
    }

    public void setAnchorX(String anchorX) {
        this.anchorX = anchorX;
    }

    public String getAnchorY() {
        return ValueBindings.get(this, "anchorY", anchorY);
    }

    public void setAnchorY(String anchorY) {
        this.anchorY = anchorY;
    }

    public String getLeft() {
        return ValueBindings.get(this, "left", left);
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getTop() {
        return ValueBindings.get(this, "top", top);
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getWidth() {
        return ValueBindings.get(this, "width", width, getDefaultWidth());
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return ValueBindings.get(this, "height", height, getDefaultHeight());
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getOnshow() {
        return ValueBindings.get(this, "onshow", onshow);
    }

    public void setOnshow(String onshow) {
        this.onshow = onshow;
    }

    public String getOnhide() {
        return ValueBindings.get(this, "onhide", onhide);
    }

    public void setOnhide(String onhide) {
        this.onhide = onhide;
    }

    public int getHidingTimeout() {
        return ValueBindings.get(this, "hidingTimeout", hidingTimeout, -1);
    }

    public void setHidingTimeout(int hidingTimeout) {
        this.hidingTimeout = hidingTimeout;
    }

    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        if (left != null && ValueBindings.set(this, "left", left))
            left = null;
        if (top != null && ValueBindings.set(this, "top", top))
            top = null;
        if (width != null && ValueBindings.set(this, "width", width))
            width = null;
        if (height != null && ValueBindings.set(this, "height", height))
            height = null;
        if (visible != null && ValueBindings.set(this, "visible", visible))
            visible = null;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),

                left,
                top,
                width,
                height,
                modalLayerStyle,
                modalLayerClass,
                onshow,
                onhide,
                hidingTimeout,

                anchorElementId,
                anchorX,
                anchorY,

                draggable,

                ondragstart,
                ondragend,

                visible,
                hideOnOuterClick,
                hideOnEsc,
                modal,
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        left = (String) values[i++];
        top = (String) values[i++];
        width = (String) values[i++];
        height = (String) values[i++];
        modalLayerStyle = (String) values[i++];
        modalLayerClass = (String) values[i++];
        onshow = (String) values[i++];
        onhide = (String) values[i++];
        hidingTimeout = (Integer) values[i++];

        anchorElementId = (String) values[i++];
        anchorX = (String) values[i++];
        anchorY = (String) values[i++];

        draggable = (Boolean) values[i++];

        ondragstart = (String) values[i++];
        ondragend = (String) values[i++];

        visible = (Boolean) values[i++];
        hideOnOuterClick = (Boolean) values[i++];
        hideOnEsc = (Boolean) values[i++];
        modal = (Boolean) values[i++];
    }
}

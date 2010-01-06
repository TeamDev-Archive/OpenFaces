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

package org.openfaces.taglib.jsp.window;

import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.taglib.internal.window.PopupLayerTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Andrew Palval
 */
public class PopupLayerJspTag extends AbstractComponentJspTag {

    public PopupLayerJspTag() {
        super(new PopupLayerTag());
    }

    public PopupLayerJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setModalLayerStyle(ValueExpression modalLayerStyle) {
        getDelegate().setPropertyValue("modalLayerStyle", modalLayerStyle);
    }

    public void setModalLayerClass(ValueExpression modalLayerClass) {
        getDelegate().setPropertyValue("modalLayerClass", modalLayerClass);
    }

    public void setModal(ValueExpression modal) {
        getDelegate().setPropertyValue("modal", modal);
    }

    public void setHideOnOuterClick(ValueExpression hideOnOuterClick) {
        getDelegate().setPropertyValue("hideOnOuterClick", hideOnOuterClick);
    }

    public void setHideOnEsc(ValueExpression hideOnEsc) {
        getDelegate().setPropertyValue("hideOnEsc", hideOnEsc);
    }

    public void setVisible(ValueExpression visible) {
        getDelegate().setPropertyValue("visible", visible);
    }

    public void setOndragstart(ValueExpression ondragstart) {
        getDelegate().setPropertyValue("ondragstart", ondragstart);
    }

    public void setOndragend(ValueExpression ondragend) {
        getDelegate().setPropertyValue("ondragend", ondragend);
    }

    public void setDraggable(ValueExpression draggable) {
        getDelegate().setPropertyValue("draggable", draggable);
    }

    public void setAnchorElementId(ValueExpression anchorElementId) {
        getDelegate().setPropertyValue("anchorElementId", anchorElementId);
    }

    public void setAnchorX(ValueExpression anchorX) {
        getDelegate().setPropertyValue("anchorX", anchorX);
    }

    public void setAnchorY(ValueExpression anchorY) {
        getDelegate().setPropertyValue("anchorY", anchorY);
    }

    public void setLeft(ValueExpression left) {
        getDelegate().setPropertyValue("left", left);
    }

    public void setTop(ValueExpression top) {
        getDelegate().setPropertyValue("top", top);
    }

    public void setWidth(ValueExpression width) {
        getDelegate().setPropertyValue("width", width);
    }

    public void setHeight(ValueExpression height) {
        getDelegate().setPropertyValue("height", height);
    }

    public void setOnshow(ValueExpression onshow) {
        getDelegate().setPropertyValue("onshow", onshow);
    }

    public void setOnhide(ValueExpression onhide) {
        getDelegate().setPropertyValue("onhide", onhide);
    }

    public void setHidingTimeout(ValueExpression hidingTimeout) {
        getDelegate().setPropertyValue("hidingTimeout", hidingTimeout);
    }
}

/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.validation;

import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Vladimir Korenev
 */
public class FloatingIconMessage extends OUIMessage {
    public static final String COMPONENT_TYPE = "org.openfaces.FloatingIconMessage";

    private static final String DEFAULT_RENDERER_TYPE = "org.openfaces.FloatingIconMessage";
    private static final String DEFAULT_IMAGE_URL = "error_icon.gif";
    private static final int DEFAULT_OFFSET_TOP = -4;
    private static final int DEFAULT_OFFSET_LEFT = -4;

    private String imageUrl;
    private Integer offsetTop;
    private Integer offsetLeft;
    private Boolean noImage;
    private Boolean noStyle;
    private String clientIdFor;

    private boolean runtimeDefined = false;

    public FloatingIconMessage() {
        setRendererType(DEFAULT_RENDERER_TYPE);
        setShowSummary(false);
    }

    public FloatingIconMessage(FloatingIconMessage bubble, boolean runtime) {
        runtimeDefined = runtime;
        setRendererType(DEFAULT_RENDERER_TYPE);
        if (bubble == null)
            return;
        if (bubble.getImageUrl() != null)
            imageUrl = bubble.getImageUrl();
        if (bubble.getOffsetLeft() != DEFAULT_OFFSET_LEFT)
            offsetLeft = bubble.getOffsetLeft();
        if (bubble.getOffsetTop() != DEFAULT_OFFSET_TOP)
            offsetTop = bubble.getOffsetTop();
        noImage = bubble.isNoImage();
        noStyle = bubble.isNoStyle();
        if (bubble.getStyle() != null)
            style = bubble.getStyle();
        if (bubble.getStyleClass() != null)
            styleClass = bubble.getStyleClass();
        setShowSummary(bubble.isShowSummary());
        setShowDetail(bubble.isShowDetail());
    }

    public boolean isRuntimeDefined() {
        return runtimeDefined;
    }

    public String getClientIdFor() {
        return clientIdFor;
    }

    public void setClientIdFor(String clientIdFor) {
        this.clientIdFor = clientIdFor;
    }

    public boolean isNoImage() {
        return ValueBindings.get(this, "noImage", noImage, false);
    }

    public boolean isNoStyle() {
        return ValueBindings.get(this, "noStyle", noStyle, false);
    }

    public void setNoImage(boolean noImage) {
        this.noImage = noImage;
    }

    public void setNoStyle(boolean noStyle) {
        this.noStyle = noStyle;
    }

    public String getImageUrl() {
        return ValueBindings.get(this, "imageUrl", imageUrl,
                ResourceUtil.getInternalResourceURL(getFacesContext(), this.getClass(), DEFAULT_IMAGE_URL));
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getOffsetTop() {
        return ValueBindings.get(this, "offsetTop", offsetTop, DEFAULT_OFFSET_TOP);
    }

    public void setOffsetTop(int offsetTop) {
        this.offsetTop = offsetTop;
    }

    public int getOffsetLeft() {
        return ValueBindings.get(this, "offsetLeft", offsetLeft, DEFAULT_OFFSET_LEFT);
    }

    public void setOffsetLeft(int offsetLeft) {
        this.offsetLeft = offsetLeft;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                imageUrl,
                offsetTop,
                offsetLeft,
                noImage,
                noStyle
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        imageUrl = (String) values[i++];
        offsetTop = (Integer) values[i++];
        offsetLeft = (Integer) values[i++];
        noImage = (Boolean) values[i++];
        noStyle = (Boolean) values[i++];
    }


}

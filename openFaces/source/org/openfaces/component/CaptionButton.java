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
package org.openfaces.component;

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class CaptionButton extends OUICommand {
    public static final String COMPONENT_TYPE = "org.openfaces.CaptionButton";
    public static final String COMPONENT_FAMILY = "org.openfaces.CaptionButton";

    private String pressedStyle;
    private String pressedClass;

    private String imageUrl;
    private String rolloverImageUrl;
    private String pressedImageUrl;

    private String hint;

    public CaptionButton() {
        setRendererType("org.openfaces.CaptionButtonRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPressedStyle() {
        return ValueBindings.get(this, "pressedStyle", pressedStyle);
    }

    public void setPressedStyle(String pressedStyle) {
        this.pressedStyle = pressedStyle;
    }

    public String getPressedClass() {
        return ValueBindings.get(this, "pressedClass", pressedClass);
    }

    public void setPressedClass(String pressedClass) {
        this.pressedClass = pressedClass;
    }

    public String getRolloverImageUrl() {
        return rolloverImageUrl;
    }

    public void setRolloverImageUrl(String rolloverImageUrl) {
        this.rolloverImageUrl = rolloverImageUrl;
    }

    public String getPressedImageUrl() {
        return pressedImageUrl;
    }

    public void setPressedImageUrl(String pressedImageUrl) {
        this.pressedImageUrl = pressedImageUrl;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                pressedStyle,
                pressedClass,
                imageUrl,
                rolloverImageUrl,
                pressedImageUrl,
                hint
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        pressedStyle = (String) values[i++];
        pressedClass = (String) values[i++];
        imageUrl = (String) values[i++];
        rolloverImageUrl = (String) values[i++];
        pressedImageUrl = (String) values[i++];
        hint = (String) values[i++];
    }

}

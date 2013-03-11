/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.timetable;

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

public class ScrollButton extends org.openfaces.component.OUIPanel {
    public static final String COMPONENT_TYPE = "org.openfaces.ScrollButton";
    public static final String COMPONENT_FAMILY = "org.openfaces.ScrollButton";

    private String buttonStyle;
    private String buttonClass;
    private String rolloverButtonStyle;
    private String rolloverButtonClass;
    private String buttonImg;
    private ScrollDirection scrollDirection;

    public ScrollButton() {
        setRendererType("org.openfaces.ScrollButtonRenderer");
    }

    public ScrollButton(ScrollDirection scrollDirection) {
        this();
        this.scrollDirection = scrollDirection;
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
            super.saveState(context),
            buttonStyle, buttonClass, rolloverButtonStyle,
            rolloverButtonClass, buttonImg, scrollDirection
        };
    }


    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        buttonStyle = (String) state[i++];
        buttonClass = (String) state[i++];
        rolloverButtonStyle = (String) state[i++];
        rolloverButtonClass = (String) state[i++];
        buttonImg = (String) state[i++];
        scrollDirection = (ScrollDirection) state[i++];
    }

    public String getButtonStyle() {
        return ValueBindings.get(this, "buttonStyle", buttonStyle);
    }

    public void setButtonStyle(String buttonStyle) {
        this.buttonStyle = buttonStyle;
    }

    public String getButtonClass() {
        return ValueBindings.get(this, "buttonClass", buttonClass);
    }

    public void setButtonClass(String buttonClass) {
        this.buttonClass = buttonClass;
    }

    public String getRolloverButtonStyle() {
        return ValueBindings.get(this, "rolloverButtonStyle", rolloverButtonStyle);
    }

    public void setRolloverButtonStyle(String rolloverButtonStyle) {
        this.rolloverButtonStyle = rolloverButtonStyle;
    }

    public String getRolloverButtonClass() {
        return ValueBindings.get(this, "rolloverButtonClass", rolloverButtonClass);
    }

    public void setRolloverButtonClass(String rolloverButtonClass) {
        this.rolloverButtonClass = rolloverButtonClass;
    }

    public String getButtonImg() {
        return ValueBindings.get(this, "buttonImg", buttonImg, "");
    }

    public void setButtonImg(String downButtonImg) {
        this.buttonImg = downButtonImg;
    }

    public ScrollDirection getScrollDirection() {
        return ValueBindings.get(this, "scrollDirection", scrollDirection, ScrollDirection.UP,
                ScrollDirection.class);
    }

    public void setScrollDirection(ScrollDirection scrollDirection) {
        this.scrollDirection = scrollDirection;
    }

}

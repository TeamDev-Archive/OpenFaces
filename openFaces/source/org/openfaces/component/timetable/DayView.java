/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
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

public class DayView extends org.openfaces.component.OUIPanel {
    public static final String COMPONENT_TYPE = "org.openfaces.DayView";
    public static final String COMPONENT_FAMILY = "org.openfaces.DayView";
    private String moreLinkText;
    private String moreLinkStyle;
    private String moreLinkClass;
    private String buttonStyle;
    private String buttonClass;
    private String rolloverButtonStyle;
    private String rolloverButtonClass;
    private String downButtonImg;
    private String upButtonImg;


    public DayView() {
        setRendererType("org.openfaces.DayViewRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
            super.saveState(context),

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);

    }

    public String getMoreLinkText() {
        return ValueBindings.get(this, "moreLinkText", moreLinkText, "More");
    }

    public void setMoreLinkText(String moreLinkText) {
        this.moreLinkText = moreLinkText;
    }

    public String getMoreLinkStyle() {
        return ValueBindings.get(this, "moreLinkStyle", moreLinkStyle);
    }

    public void setMoreLinkStyle(String moreLinkStyle) {
        this.moreLinkStyle = moreLinkStyle;
    }

    public String getMoreLinkClass() {
        return ValueBindings.get(this, "moreLinkClass", moreLinkClass);
    }

    public void setMoreLinkClass(String moreLinkClass) {
        this.moreLinkClass = moreLinkClass;
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

    public String getDownButtonImg() {
        return ValueBindings.get(this, "downButtonImg", downButtonImg, "");
    }

    public void setDownButtonImg(String downButtonImg) {
        this.downButtonImg = downButtonImg;
    }

    public String getUpButtonImg() {
        return ValueBindings.get(this, "upButtonImg", upButtonImg, "");
    }

    public void setUpButtonImg(String upButtonImg) {
        this.upButtonImg = upButtonImg;
    }
}

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
package org.openfaces.component.filter;

import org.openfaces.component.CompoundComponent;
import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class TextSearchFilter extends ExpressionFilter implements CompoundComponent {
    public static final String SEARCH_COMPONENT_SUFFIX = "searchComponent";

    private String rolloverStyle;
    private String rolloverClass;
    private String focusedStyle;
    private String focusedClass;
    private Integer maxlength;

    protected UIComponent createSearchComponent(FacesContext context) {
        return Components.createChildComponent(context, this, getInputComponentType(), SEARCH_COMPONENT_SUFFIX);
    }

    protected abstract String getInputComponentType();

    public UIComponent getSearchComponent() {
        createSubComponents(getFacesContext());
        List children = getChildren();
        if (children.size() != 1) {
            String message = "TextSearchFilter should have exactly one child component - " +
                    "the search component. children.size = " + children.size();
            throw new IllegalStateException(message);
        }
        UIComponent searchComponent = (UIComponent) children.get(0);
        return searchComponent;
    }

    public void createSubComponents(FacesContext context) {
        String createdKey = "_subComponentsCreated";
        if (!getAttributes().containsKey(createdKey)) {
            getAttributes().put(createdKey, true);
            createSearchComponent(context);
        }
    }

    public String getFocusedStyle() {
        return ValueBindings.get(this, "focusedStyle", focusedStyle);
    }

    public String getFocusedClass() {
        return ValueBindings.get(this, "focusedClass", focusedClass);
    }

    public void setFocusedStyle(String focusedStyle) {
        this.focusedStyle = focusedStyle;
    }

    public void setFocusedClass(String focusedClass) {
        this.focusedClass = focusedClass;
    }

    public String getRolloverStyle() {
        return ValueBindings.get(this, "rolloverStyle", rolloverStyle);
    }

    public void setRolloverStyle(String rolloverStyle) {
        this.rolloverStyle = rolloverStyle;
    }

    public String getRolloverClass() {
        return ValueBindings.get(this, "rolloverClass", rolloverClass);
    }

    public void setRolloverClass(String rolloverClass) {
        this.rolloverClass = rolloverClass;
    }

    public int getMaxlength() {
        return ValueBindings.get(this, "maxlength", maxlength, Integer.MIN_VALUE);
    }

    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }


    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                focusedStyle,
                focusedClass,
                rolloverStyle,
                rolloverClass,
                maxlength
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        focusedStyle = (String) values[i++];
        focusedClass = (String) values[i++];
        rolloverStyle = (String) values[i++];
        rolloverClass = (String) values[i++];
        maxlength = (Integer) values[i++];
    }


}

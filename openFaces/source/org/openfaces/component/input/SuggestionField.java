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
package org.openfaces.component.input;

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * The SuggestionField component is an input component that shows a list of suggestions
 * based on user input and completes user input right in the input field. It has a similar
 * functionality to DropDownField, but unlike DropDownField it looks like a plain text field.
 * You can create a multi-column drop-down list and embed JSF components into it.
 */
public class SuggestionField extends DropDownFieldBase {
    public static final String COMPONENT_TYPE = "org.openfaces.SuggestionField";
    public static final String COMPONENT_FAMILY = "org.openfaces.SuggestionField";

    private Boolean manualListOpeningAllowed;

    public SuggestionField() {
        setRendererType("org.openfaces.SuggestionFieldRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    protected SuggestionMode getDefaultSuggestionMode() {
        return SuggestionMode.STRING_START;
    }

    @Override
    protected boolean getDefaultAutoComplete() {
        return true;
    }

    @Override
    protected int getDefaultSuggestionMinChars() {
        return 2;
    }

    public boolean getManualListOpeningAllowed() {
        return ValueBindings.get(this, "manualListOpeningAllowed", manualListOpeningAllowed, false);
    }

    public void setManualListOpeningAllowed(boolean manualListOpeningAllowed) {
        this.manualListOpeningAllowed = manualListOpeningAllowed;
    }

    @Override
    public void setSize(int size) {
        super.setSize(size);
    }

    @Override
    public int getSize() {
        return super.getSize();
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                manualListOpeningAllowed
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        manualListOpeningAllowed = (Boolean) stateArray[1];
    }
}


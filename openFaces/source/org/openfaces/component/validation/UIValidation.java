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

package org.openfaces.component.validation;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * <p><strong>UIValidation</strong></p> is a {@link javax.faces.component.UIComponent} that provides an ability
 * to validate all of it's child JSF components which are instances of {@link javax.faces.component.EditableValueHolder}.
 *
 * @author Eugene Goncharov
 */
public class UIValidation extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.validation.AnnotationValidation";
    public static final String COMPONENT_FAMILY = "org.openfaces.validation.AnnotationValidation";
    private boolean myValidatorsAdded;

    public UIValidation() {
        setRendererType("org.openfaces.validation.AnnotationValidationRenderer");
    }

    public String getFamily() {
        return "org.openfaces.validation.AnnotationValidation";
    }

    public void setValidatorsAdded(boolean isValidatorAdded) {
        myValidatorsAdded = isValidatorAdded;
    }

    public boolean isValidatorsAdded() {
        return myValidatorsAdded;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, myValidatorsAdded};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        myValidatorsAdded = (Boolean) stateArray[1];
    }

    // for debug purposes only
    @Override
    public void processValidators(FacesContext arg0) {
        super.processValidators(arg0);
    }

    @Override
    public void processRestoreState(FacesContext arg0, Object arg1) {
        super.processRestoreState(arg0, arg1);
    }

    @Override
    public void processUpdates(FacesContext arg0) {
        super.processUpdates(arg0);
    }

    @Override
    public Object processSaveState(FacesContext arg0) {
        return super.processSaveState(arg0);
    }

}

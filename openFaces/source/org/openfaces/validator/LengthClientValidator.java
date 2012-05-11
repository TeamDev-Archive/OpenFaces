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

package org.openfaces.validator;

import org.openfaces.util.MessageUtil;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.LengthValidator;

/**
 * @author Vladimir Korenev
 */
public class LengthClientValidator extends AbstractClientValidator {
    private LengthValidator lengthValidator;

    public LengthClientValidator() {
        addJavascriptLibrary(new ValidationJavascriptLibrary("lengthValidator.js"));
    }

    public void setLengthValidator(LengthValidator lengthValidator) {
        this.lengthValidator = lengthValidator;
    }

    public String getJsValidatorName() {
        return "O$._LengthValidator";
    }

    @Override
    protected Object[] getJsValidatorParametersAsString(FacesContext context, UIComponent component) {
        int minimum = lengthValidator.getMinimum();
        int maximum = lengthValidator.getMaximum();

        Object[] maxArgs = {maximum, component.getId()};
        FacesMessage maxErrorMessage = MessageUtil.getMessage(context, FacesMessage.SEVERITY_ERROR,
                LengthValidator.MAXIMUM_MESSAGE_ID, maxArgs);
        String maxSummary = maxErrorMessage.getSummary();
        String maxDetail = maxErrorMessage.getDetail();

        Object[] minArgs = {minimum, component.getId()};
        FacesMessage minErrorMessage = MessageUtil.getMessage(context, FacesMessage.SEVERITY_ERROR,
                LengthValidator.MINIMUM_MESSAGE_ID, minArgs);
        String minSummary = minErrorMessage.getSummary();
        String minDetail = minErrorMessage.getDetail();

        return new Object[]{
                minimum,
                maximum,
                minSummary,
                minDetail,
                maxSummary,
                maxDetail
        };
    }


}

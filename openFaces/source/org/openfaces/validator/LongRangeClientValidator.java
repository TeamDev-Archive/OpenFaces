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
import javax.faces.validator.LongRangeValidator;

/**
 * @author Vladimir Korenev
 */
public class LongRangeClientValidator extends AbstractClientValidator {
    private LongRangeValidator longRangeValidator;

    public LongRangeClientValidator() {
        addJavascriptLibrary(new ValidationJavascriptLibrary("longRangeValidator.js"));
    }

    public void setLongRangeValidator(LongRangeValidator longRangeValidator) {
        this.longRangeValidator = longRangeValidator;
    }

    public String getJsValidatorName() {
        return "O$._LongRangeValidator";
    }

    @Override
    protected Object[] getJsValidatorParametersAsString(FacesContext context, UIComponent component) {
        long minimum = longRangeValidator.getMinimum();
        long maximum = longRangeValidator.getMaximum();
        String rangeSummary = null;
        String rangeDetail = null;
        if (minimum != Long.MIN_VALUE && maximum != Long.MAX_VALUE) {
            Object[] args = {minimum, maximum, component.getId()};
            FacesMessage message = MessageUtil.getMessage(context, FacesMessage.SEVERITY_ERROR,
                    LongRangeValidator.NOT_IN_RANGE_MESSAGE_ID, args);
            rangeSummary = message.getSummary();
            rangeDetail = message.getDetail();
        } else if (minimum != Long.MIN_VALUE) {
            Object[] args = {minimum, component.getId()};
            FacesMessage message = MessageUtil.getMessage(context, FacesMessage.SEVERITY_ERROR,
                    LongRangeValidator.MINIMUM_MESSAGE_ID, args);
            rangeSummary = message.getSummary();
            rangeDetail = message.getDetail();

        } else if (maximum != Long.MAX_VALUE) {
            Object[] args = {maximum, component.getId()};
            FacesMessage message = MessageUtil.getMessage(context, FacesMessage.SEVERITY_ERROR,
                    LongRangeValidator.MAXIMUM_MESSAGE_ID, args);
            rangeSummary = message.getSummary();
            rangeDetail = message.getDetail();
        }

        Object[] args = {component.getId()};
        FacesMessage message = MessageUtil.getMessage(context, FacesMessage.SEVERITY_ERROR,
                LongRangeValidator.TYPE_MESSAGE_ID, args);
        String typeSummary = message.getSummary();
        String typeDetail = message.getDetail();

        return new Object[]{
                minimum,
                maximum,
                rangeSummary,
                rangeDetail,
                typeSummary,
                typeDetail
        };

    }


}

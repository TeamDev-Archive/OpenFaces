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
package org.openfaces.validator;

import org.openfaces.util.MessageUtil;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.DoubleRangeValidator;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class DoubleRangeClientValidator extends AbstractClientValidator {
    private DoubleRangeValidator doubleRangeValidator;
    private static final double PRECISION = 0.0000001;

    public DoubleRangeClientValidator() {
        addJavascriptLibrary(new ValidationJavascriptLibrary("doubleRangeValidator.js", this.getClass()));
    }

    public void setDoubleRangeValidator(DoubleRangeValidator doubleRangeValidator) {
        this.doubleRangeValidator = doubleRangeValidator;
    }

    public String getJsValidatorName() {
        return "O$._DoubleRangeValidator";
    }

    @Override
    protected Object[] getJsValidatorParametersAsString(FacesContext context, UIComponent component) {
        double minimum = doubleRangeValidator.getMinimum();
        double maximum = doubleRangeValidator.getMaximum();
        String rangeSummary = null;
        String rangeDetail = null;
        if (Math.abs(minimum - Double.MIN_VALUE) > PRECISION && Math.abs(maximum - Double.MAX_VALUE) > PRECISION) {
            Object[] args = {minimum, maximum, component.getId()};
            FacesMessage message = MessageUtil.getMessage(context, FacesMessage.SEVERITY_ERROR,
                    DoubleRangeValidator.NOT_IN_RANGE_MESSAGE_ID, args);
            rangeSummary = message.getSummary();
            rangeDetail = message.getDetail();

        } else if (Math.abs(minimum - Double.MIN_VALUE) > PRECISION) {
            Object[] args = {minimum, component.getId()};
            FacesMessage message = MessageUtil.getMessage(context, FacesMessage.SEVERITY_ERROR,
                    DoubleRangeValidator.MINIMUM_MESSAGE_ID, args);
            rangeSummary = message.getSummary();
            rangeDetail = message.getDetail();
        } else if (Math.abs(maximum - Double.MAX_VALUE) > PRECISION) {
            Object[] args = {maximum, component.getId()};
            FacesMessage message = MessageUtil.getMessage(context, FacesMessage.SEVERITY_ERROR,
                    DoubleRangeValidator.MAXIMUM_MESSAGE_ID, args);
            rangeSummary = message.getSummary();
            rangeDetail = message.getDetail();
        }
        Object[] args = {component.getId()};
        FacesMessage message = MessageUtil.getMessage(context, FacesMessage.SEVERITY_ERROR,
                DoubleRangeValidator.TYPE_MESSAGE_ID, args);
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

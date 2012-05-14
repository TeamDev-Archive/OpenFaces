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

import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.util.MessageUtil;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public abstract class AbstractCustomValidator extends AbstractClientValidator implements Validator, ClientValidator, StateHolder {
    private static final String CUSTOM_VALIDATOR_MESSAGE_ID = "org.openfaces.CustomValidatorMessage";
    private static final String CUSTOM_VALIDATOR_MESSAGE_SUMMARY = "''{0}'': Custom validator error";
    private static final String CUSTOM_VALIDATOR_MESSAGE_DETAIL = "''{0}'': Custom validator error";

    private String summary;
    private String detail;
    private List<String> params;

    protected AbstractCustomValidator() {
        addJavascriptLibrary(new ValidationJavascriptLibrary("customValidator.js"));
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public List<String> getParams() {
        if (params == null) params = new ArrayList<String>();
        return params;
    }

    public String getSummary() {
        return (String) getDynamicValue(summary);
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return (String) getDynamicValue(detail);
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public final void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Object[] args = new Object[]{context, component, value};
        boolean result = customServerValidate(context, component, value, args);
        if (!result) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, getFormattedSummary(component), getFormattedDetail(component)));
        }

    }

    public abstract boolean customServerValidate(FacesContext context, UIComponent component, Object value, Object[] args);

    public Object[] getImplicitParams(UIComponent uiComponent) {
        String id = uiComponent.getId();
        if (id == null && FacesContext.getCurrentInstance() != null)
            id = uiComponent.getClientId(FacesContext.getCurrentInstance());
        return new Object[]{id};
    }

    public Object saveState(FacesContext facesContext) {
        return new Object[]{
                summary,
                detail,
                UIComponentBase.saveAttachedState(facesContext, params),
        };
    }

    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        summary = (String) state[i++];
        detail = (String) state[i++];
        params = (List<String>) UIComponentBase.restoreAttachedState(facesContext, state[i++]);
    }

    public boolean isTransient() {
        return false;
    }

    public void setTransient(boolean newTransientValue) {
        throw new UnsupportedOperationException("method is unsupported for CustomValidator");
    }

    protected Object getDynamicValue(String value) {
        if (value == null)
            return null;

        if (!AbstractComponentTag.isValueReference(value))
            return value;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ValueExpression expression = facesContext.getApplication().getExpressionFactory().createValueExpression(elContext, value, Object.class);
        return expression.getValue(elContext);
    }

    private Object[] getDynamicParams(List<String> params, UIComponent component) {
        Object[] implicitParams = getImplicitParams(component);
        if (params == null)
            return implicitParams;

        if (params.isEmpty()) {
            return implicitParams;
        }

        int implicitParamsSize = implicitParams.length;

        Object[] dynamicParams = new Object[params.size() + implicitParamsSize];

        if (implicitParamsSize > 0) {
            System.arraycopy(implicitParams, 0, dynamicParams, 0, implicitParams.length);
        }

        for (int i = 0; i < params.size(); i++) {
            dynamicParams[i + implicitParamsSize] = getDynamicValue(params.get(i));
        }
        return dynamicParams;
    }

    private String getFormattedMessage(String message, Object[] params) {
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        MessageFormat format = new MessageFormat(message, locale);
        return format.format(params);
    }

    protected String getFormattedSummary(UIComponent uiComponent) {
        Object[] params = getDynamicParams(this.params, uiComponent);
        String summary = getSummary();
        if (summary != null)
            return getFormattedMessage(summary, params);

        FacesMessage message = MessageUtil.getMessage(FacesContext.getCurrentInstance(),
                FacesMessage.SEVERITY_ERROR,
                getValidatorMessageID(), params);

        if (message != null && message.getSummary() != null)
            return message.getSummary();

        return getFormattedMessage(CUSTOM_VALIDATOR_MESSAGE_SUMMARY, params);

    }

    protected String getFormattedDetail(UIComponent uiComponent) {
        Object[] params = getDynamicParams(this.params, uiComponent);
        String detail = getDetail();
        if (detail != null)
            return getFormattedMessage(detail, params);

        FacesMessage message = MessageUtil.getMessage(FacesContext.getCurrentInstance(),
                FacesMessage.SEVERITY_ERROR,
                getValidatorMessageID(), params);

        if (message != null && message.getDetail() != null)
            return message.getDetail();

        return getFormattedMessage(CUSTOM_VALIDATOR_MESSAGE_DETAIL, params);
    }


    public String getValidatorMessageID() {
        return CUSTOM_VALIDATOR_MESSAGE_ID;
    }

    public String getFamily() {
        return null;
    }
}

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
package org.openfaces.taglib.jsp;

import org.openfaces.taglib.internal.validation.AbstractCustomValidatorTag;
import org.openfaces.util.Environment;
import org.openfaces.util.ReflectionUtil;
import org.openfaces.validator.AbstractCustomValidator;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public abstract class AbstractCustomValidatorJspTag extends ValidatorELTag {
    private AbstractCustomValidatorTag delegate;
    private String validatorId;
    private ValueExpression binding;

    protected AbstractCustomValidatorJspTag(AbstractCustomValidatorTag delegate, String validatorId) {
        this.delegate = delegate;
        this.delegate.setExpressionCreator(AbstractComponentJspTag.JSP_EXPRESSION_CREATOR);
        this.validatorId = validatorId;
    }

    public AbstractCustomValidatorTag getDelegate() {
        return delegate;
    }

    public void setBinding(ValueExpression binding) {
        this.binding = binding;
    }

    public int doStartTag() throws JspException {
        if (!Environment.isMyFaces())
            return super.doStartTag();

        Object resultComponentTag = ReflectionUtil.invokeMethod("javax.faces.webapp.UIComponentELTag",
                "getParentUIComponentClassicTagBase", new Class[]{PageContext.class}, new Object[]{pageContext}, null);
        if (resultComponentTag == null)
            throw new JspException("no parent UIComponentTag found");

        boolean isCreated = (Boolean) ReflectionUtil.invokeMethod(
                resultComponentTag.getClass(), "getCreated", resultComponentTag);

        if (!isCreated)
            return Tag.SKIP_BODY;

        Validator validator = createValidator();

        UIComponent component = (UIComponent) ReflectionUtil.invokeMethod(
                resultComponentTag.getClass(), "getComponentInstance", resultComponentTag);

        if (component == null)
            throw new JspException("parent UIComponentTag has no UIComponent");
        if (!(component instanceof EditableValueHolder))
            throw new JspException("UIComponent is no ValueHolder");

        ((EditableValueHolder) component).addValidator(validator);

        return Tag.SKIP_BODY;
    }

    public void setSummary(ValueExpression summary) {
        getDelegate().setPropertyValue("summary", summary);
    }

    public void setDetail(ValueExpression detail) {
        getDelegate().setPropertyValue("detail", detail);
    }


    public List<String> getParams() {
        return getDelegate().getParams();
    }

    public void setParams(List<String> params) {
        getDelegate().setParams(params);
    }

    protected final void setProperties(AbstractCustomValidator validator) {
        getDelegate().setProperties(validator);
    }

    public void invokeSetProperties(AbstractCustomValidator abstractCustomValidator) {
        setProperties(abstractCustomValidator);
    }

    protected Validator createValidator() throws JspException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        Validator validator;
        if (binding != null)
            validator = (Validator) binding.getValue(elContext);
        else
            validator = null;

        if (validator == null) {
            validator = facesContext.getApplication().createValidator(validatorId);
            if (binding != null && validator != null)
                binding.setValue(elContext, validator);
        }

        setProperties((AbstractCustomValidator) validator);
        return validator;
    }

}

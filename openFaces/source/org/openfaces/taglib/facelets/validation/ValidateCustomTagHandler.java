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
package org.openfaces.taglib.facelets.validation;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletHandler;
import com.sun.facelets.tag.CompositeFaceletHandler;
import com.sun.facelets.tag.MetaRuleset;
import com.sun.facelets.tag.MetaTagHandler;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagException;
import com.sun.facelets.tag.jsf.ConverterConfig;
import com.sun.facelets.tag.jsf.ValidatorConfig;
import org.openfaces.taglib.facelets.FaceletsExpressionCreator;
import org.openfaces.taglib.facelets.PropertyHandlerMetaRule;
import org.openfaces.taglib.internal.validation.AbstractCustomValidatorTag;
import org.openfaces.taglib.internal.validation.ValidateCustomTag;
import org.openfaces.validator.AbstractCustomValidator;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ValidateCustomTagHandler extends MetaTagHandler {
    private PropertyHandlerMetaRule metaRule;
    private TagAttribute binding;
    private List<String> params;


    public ValidateCustomTagHandler(ConverterConfig config) {
        super(config);
        binding = getAttribute("binding");
        metaRule = new PropertyHandlerMetaRule(new ValidateCustomTag());
    }

    public ValidateCustomTagHandler(ValidatorConfig config) {
        super(config);
        binding = getAttribute("binding");
        metaRule = new PropertyHandlerMetaRule(new ValidateCustomTag());
    }

    public ValidateCustomTagHandler(TagConfig config) {
        super(config);
        binding = getAttribute("binding");
        metaRule = new PropertyHandlerMetaRule(new ValidateCustomTag());
    }

    public void setMetaRule(PropertyHandlerMetaRule metaRule) {
        this.metaRule = metaRule;
    }


    public String getValidatorId() {
        return "org.openfaces.Custom";
    }

    protected Validator createValidator(FaceletContext faceletsContext) {
        FacesContext facesContext = faceletsContext.getFacesContext();
        Application application = facesContext.getApplication();
        return application.createValidator(getValidatorId());
    }

    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset metaRuleset = super.createMetaRuleset(type);
        metaRuleset.addRule(metaRule);
        return metaRuleset;
    }

    protected void setAttributes(FaceletContext faceletContext, Object object) {
        super.setAttributes(faceletContext, object);

        AbstractCustomValidatorTag tag = (AbstractCustomValidatorTag) metaRule.getTag();
        tag.setParams(params);

        tag.setExpressionCreator(new FaceletsExpressionCreator(faceletContext) {
            protected TagAttribute getAttribute(String attributeName) {
                return ValidateCustomTagHandler.this.getAttribute(attributeName);
            }
        });
        tag.setProperties((AbstractCustomValidator) object);
    }

    public void apply(FaceletContext context, UIComponent parent) throws IOException, FacesException, ELException {
        if (parent == null || !(parent instanceof EditableValueHolder))
            throw new TagException(tag, "Parent not an instance of EditableValueHolder: " + parent);
        if (parent.getParent() == null) {
            EditableValueHolder editableValueHolder = (EditableValueHolder) parent;
            ValueExpression valueExpression = null;
            Validator validator = null;
            if (binding != null) {
                valueExpression = binding.getValueExpression(context, javax.faces.validator.Validator.class);
                validator = (Validator) valueExpression.getValue(context);
            }
            if (validator == null) {
                validator = createValidator(context);
                if (valueExpression != null)
                    valueExpression.setValue(context, validator);
            }
            if (validator == null)
                throw new TagException(tag, "No Validator was created");

            if (nextHandler instanceof CompositeFaceletHandler) {
                addParameters(context, (CompositeFaceletHandler) nextHandler);
            } else if (nextHandler instanceof MessageParameterTagHandler) {
                addParameter(context, (MessageParameterTagHandler) nextHandler);
            }

            setAttributes(context, validator);
            editableValueHolder.addValidator(validator);
        }
    }

    private void addParameter(FaceletContext context, MessageParameterTagHandler paramHandler) throws IOException {
        if (paramHandler == null)
            return;

        UIInput tempValueHolder = new HtmlInputText();
        paramHandler.apply(context, tempValueHolder);
        if (params == null) params = new ArrayList<String>();
        params.add(tempValueHolder.getValue().toString());
    }

    private void addParameters(FaceletContext context, CompositeFaceletHandler compositeFaceletHandler) throws IOException {
        if (compositeFaceletHandler == null)
            return;

        for (int i = 0; i < compositeFaceletHandler.getHandlers().length; i++) {
            FaceletHandler handler = compositeFaceletHandler.getHandlers()[i];
            if (handler instanceof MessageParameterTagHandler) {
                addParameter(context, (MessageParameterTagHandler) handler);
            }
        }

    }

}

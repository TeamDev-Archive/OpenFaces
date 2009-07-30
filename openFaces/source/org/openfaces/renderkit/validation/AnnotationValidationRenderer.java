/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.renderkit.validation;

import org.openfaces.component.validation.UIValidation;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.RenderingUtil;
import org.openfaces.validation.CoreValidator;
import org.openfaces.validation.core.CoreValidatorImpl;
import org.openfaces.validation.validators.AnnotationValidator;
import org.openfaces.validator.ClientValidator;
import org.hibernate.validator.InvalidValue;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import java.io.IOException;
import java.util.List;

/**
 * <p><strong>AnnotationValidationRenderer</strong></p> is a JSF renderer of {@link UIValidation} component.
 * During rendering of {@link UIValidation} component, an instance of {@link AnnotationValidator} is added to all of its' child components.
 *
 * @author Eugene Goncharov
 */
public class AnnotationValidationRenderer extends RendererBase {

    private static CoreValidator ourValidator = new CoreValidatorImpl();

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        super.encodeBegin(facesContext, uiComponent);
    }

    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        if (component.isRendered()) {
            processChildren(context, component);
        }
    }


    protected void processChildren(FacesContext context, UIComponent component) throws IOException {
        UIValidation validateAll = (UIValidation) component;
        if (!validateAll.isValidatorsAdded()) {
            addValidators(validateAll.getChildren(), context);
            validateAll.setValidatorsAdded(true);
        }

        RenderingUtil.renderChildren(context, component);
    }

    private boolean isValueRequired(FacesContext context, Object child) {
        ELContext elContext = context.getELContext();
        ValueExpression valueExpression = ((UIComponent) child)
                .getValueExpression("value");

        if (valueExpression != null) {
            Object value = valueExpression.getValue(elContext);

            InvalidValue[] invalidValues = null;
            try {
                invalidValues = ourValidator.validate(
                        valueExpression, elContext, null);
            } catch (ELException ele) {
                // Attempt to set null value failed,this means that value is
                // required for this component
                return true;
            } finally {
                // restore the value
                valueExpression.setValue(elContext, value);
            }

            if (invalidValues != null && invalidValues.length > 0) {
                // Attempt to set null value failed,this means that value is
                // required for this component
                return true;
            }
        }

        return false;
    }


    private void addValidators(List<UIComponent> children, FacesContext context) {
        for (Object child : children) {
            if (child instanceof EditableValueHolder) {
                EditableValueHolder evh = (EditableValueHolder) child;
                if (evh.getValidators().length == 0) {

                    evh.addValidator(new AnnotationValidator());
                    if (isValueRequired(context, child)) {
                        evh.setRequired(true);
                        if (child instanceof UIInput) {
                            ((UIInput) child)
                                    .setRequiredMessage("Value is required for component with id "
                                            + ((UIInput) child)
                                            .getId() + "");
                        }
                    }

                }
            } else if (child instanceof UIComponent) {
                addValidators(((UIComponent) child).getChildren(), context);
            }
        }
    }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        if (component.isRendered()) {
            UIValidation validateAll = (UIValidation) component;


            renderClientValidators(validateAll.getChildren(), context);

        }
    }

    private void renderClientValidators(List<UIComponent> children, FacesContext context) {
        for (Object child : children) {
            if (child instanceof EditableValueHolder) {
                EditableValueHolder evh = (EditableValueHolder) child;
                ELContext elContext = context.getELContext();

                List<ClientValidator> clientValidators = ourValidator.getClientValidatorsForComponent(((UIComponent) child), elContext);

                if (clientValidators != null) {
                    for (ClientValidator validator : clientValidators) {

                        // Render client validator scripts here
                    }
                }

            } else if (child instanceof UIComponent) {
                renderClientValidators(((UIComponent) child).getChildren(), context);
            }
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}

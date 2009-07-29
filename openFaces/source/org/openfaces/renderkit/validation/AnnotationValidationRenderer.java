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

import javax.el.ELContext;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
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

        RenderingUtil.renderChildren(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        if (component.isRendered()) {
            UIValidation validateAll = (UIValidation) component;

            if (!validateAll.isValidatorsAdded()) {
                renderClientValidators(validateAll.getChildren(), context);
            }
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

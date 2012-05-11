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

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.component.UIMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ClientValidationSupport extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.ClientValidatorSupport";
    public static final String COMPONENT_FAMILY = "org.openfaces.ClientValidatorSupport";

    private ClientValidationMode clientValidation;

    private Boolean useDefaultClientValidationPresentation;
    private Boolean useDefaultServerValidationPresentation;

    private UIMessage defaultPresentation;

    public ClientValidationSupport() {
        setRendererType(null);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public UIMessage getDefaultPresentation() {
        return defaultPresentation;
    }

    public void setDefaultPresentation(UIMessage defaultPresentation) {
        this.defaultPresentation = defaultPresentation;
    }


    public ClientValidationMode getClientValidation() {
        Object boundPropertyValue = ValueBindings.get(this, "clientValidation", clientValidation, Object.class);
        if (boundPropertyValue == null) return null;
        if (boundPropertyValue instanceof String) {
            return ClientValidationMode.fromString((String) boundPropertyValue);
        } else if (boundPropertyValue instanceof ClientValidationMode) {
            return (ClientValidationMode) boundPropertyValue;
        }
        return null;
    }

    public void setClientValidation(ClientValidationMode clientValidation) {
        this.clientValidation = clientValidation;
    }

    public Boolean getUseDefaultClientValidationPresentation() {
        Object boundPropertyValue = ValueBindings.get(this, "useDefaultClientValidationPresentation",
                useDefaultClientValidationPresentation, Object.class);
        if (boundPropertyValue == null) return null;
        if (boundPropertyValue instanceof String) {
            String s = (String) boundPropertyValue;
            if (s.equalsIgnoreCase("true")) return Boolean.TRUE;
            if (s.equalsIgnoreCase("false")) return Boolean.FALSE;
            return null;
        } else if (boundPropertyValue instanceof Boolean) {
            return (Boolean) boundPropertyValue;
        }
        return null;
    }

    public void setUseDefaultClientValidationPresentation(Boolean useDefaultClientValidationPresentation) {
        this.useDefaultClientValidationPresentation = useDefaultClientValidationPresentation;
    }

    public Boolean getUseDefaultServerValidationPresentation() {
        Object boundPropertyValue
                = ValueBindings.get(this, "useDefaultServerValidationPresentation", useDefaultServerValidationPresentation, Object.class);
        if (boundPropertyValue == null) return null;
        if (boundPropertyValue instanceof String) {
            String s = (String) boundPropertyValue;
            if (s.equalsIgnoreCase("true")) return Boolean.TRUE;
            if (s.equalsIgnoreCase("false")) return Boolean.FALSE;
            return null;
        } else if (boundPropertyValue instanceof Boolean) {
            return (Boolean) boundPropertyValue;
        }
        return null;
    }

    public void setUseDefaultServerValidationPresentation(Boolean useDefaultServerValidationPresentation) {
        this.useDefaultServerValidationPresentation = useDefaultServerValidationPresentation;
    }

    @Override
    public Object saveState(FacesContext facesContext) {
        Object superState = super.saveState(facesContext);

        return new Object[]{
                superState,
                clientValidation,
                useDefaultClientValidationPresentation,
                useDefaultServerValidationPresentation,
                saveAttachedState(facesContext, defaultPresentation)
        };
    }

    @Override
    public void setParent(UIComponent parent) {
        super.setParent(parent);
        if (parent != null) {
            if (!(parent instanceof UIForm)) {
                MessageFormat message = new MessageFormat("Invalid parent component for ClientValidationSupport with id {0}. Required: {1}. Currently defined: {2}");
                Object args[] = new Object[]{getId(), UIForm.class, parent.getClass()};
                throw new IllegalStateException(message.format(args));
            }
        }
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        clientValidation = (ClientValidationMode) state[i++];
        useDefaultClientValidationPresentation = (Boolean) state[i++];
        useDefaultServerValidationPresentation = (Boolean) state[i++];
        defaultPresentation = (UIMessage) restoreAttachedState(facesContext, state[i++]);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        List children = getChildren();
        if (children.size() > 1) {
            throw new RuntimeException("There should be only one default presentation component defined.");
        }
        if (children.size() > 0) {
            UIComponent child = (UIComponent) children.get(0);
            if (child instanceof UIMessage) {
                defaultPresentation = (UIMessage) child;
            } else {
                throw new RuntimeException("Presentation component should be of UIMessage type. But " + child.getClass()
                        + " was found.");
            }
        } else {
            //todo: get application default presentation and use
        }
    }
}

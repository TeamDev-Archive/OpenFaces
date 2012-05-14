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

import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.NewInstanceScript;
import org.openfaces.util.Script;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class CustomValidator extends AbstractCustomValidator {

    public static final String VALIDATOR_ID = "org.openfaces.Custom";

    private String clientFunction;
    private MethodExpression serverFunction;

    public String getClientFunction() {
        return clientFunction;
    }

    public void setClientFunction(String clientFunction) {
        this.clientFunction = clientFunction;
    }

    public MethodExpression getServerFunction() {
        return serverFunction;
    }

    public void setServerFunction(MethodExpression serverFunction) {
        this.serverFunction = serverFunction;
    }

    public boolean customServerValidate(FacesContext context, UIComponent component, Object value, Object[] args) {
        MethodExpression serverFunction = getServerFunction();
        if (serverFunction == null)
            return true;
        return (Boolean) serverFunction.invoke(context.getELContext(), args);
    }

    @Override
    public Script getClientScript(FacesContext context, UIComponent component) {
        String dynamicClientFunction = (String) getDynamicValue(clientFunction);

        return new NewInstanceScript("O$._CustomValidator",
                getFormattedSummary(component),
                getFormattedDetail(component),
                new AnonymousFunction(
                        dynamicClientFunction != null ? dynamicClientFunction : ("return true;"),
                        "input", "value"));
    }

    @Override
    public Object saveState(FacesContext facesContext) {
        Object superState = super.saveState(facesContext);

        return new Object[]{
                superState,
                UIComponentBase.saveAttachedState(facesContext, clientFunction),
                UIComponentBase.saveAttachedState(facesContext, serverFunction),
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        clientFunction = (String) UIComponentBase.restoreAttachedState(facesContext, state[i++]);
        serverFunction = (MethodExpression) UIComponentBase.restoreAttachedState(facesContext, state[i++]);
    }

    public String getJsValidatorName() {
        return "O$._CustomValidator";
    }
}

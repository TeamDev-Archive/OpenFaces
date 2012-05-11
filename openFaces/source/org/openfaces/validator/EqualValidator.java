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

import org.openfaces.util.NewInstanceScript;
import org.openfaces.util.Script;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class EqualValidator extends AbstractCustomValidator {
    public static final String VALIDATOR_ID = "org.openfaces.Equal";

    private static final String EQUAL_VALIDATOR_MESSAGE_ID = "org.openfaces.EqualValidatorMessage";

    private String _for;

    public String getFor() {
        return _for;
    }

    public void setFor(String _for) {
        this._for = _for;
    }

    public EqualValidator() {
        addJavascriptLibrary(new ValidationJavascriptLibrary("equalValidator.js"));
    }

    public boolean customServerValidate(FacesContext context, UIComponent component, Object value, Object[] args) {
        EditableValueHolder sample = (EditableValueHolder) component.findComponent(_for);

        if (value == null)
            return true;

        if (sample.getValue() == null || !sample.getValue().toString().equals(value.toString()))
            return false;

        return true;
    }

    public String getJsValidatorName() {
        return "O$._EqualValidator";
    }

    @Override
    public Script getClientScript(FacesContext context, UIComponent component) {
        UIComponent forComponent = getForComponent(_for, component);
        return new NewInstanceScript(getJsValidatorName(),
                getFormattedSummary(component),
                getFormattedDetail(component),
                forComponent != null ? forComponent.getClientId(context) : null);
    }

    @Override
    public String getValidatorMessageID() {
        return EQUAL_VALIDATOR_MESSAGE_ID;
    }

    @Override
    public Object saveState(FacesContext facesContext) {
        Object superState = super.saveState(facesContext);

        return new Object[]{
                superState,
                _for
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        _for = (String) state[i++];
    }

    @Override
    public Object[] getImplicitParams(UIComponent uiComponent) {
        String id = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            UIComponent sample = uiComponent.findComponent(_for);
            id = sample.getId();
            if (id == null)
                id = sample.getClientId(context);
        }

        Object[] params = super.getImplicitParams(uiComponent);
        if (params.length == 0)
            return new Object[]{id};

        Object[] result = new Object[params.length + 1];
        System.arraycopy(params, 0, result, 0, params.length);
        result[result.length - 1] = id;
        return result;
    }

    public static UIComponent getForComponent(String id, UIComponent component) {
        return component.findComponent(id);
    }
}

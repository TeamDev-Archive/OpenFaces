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

import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class RegexValidator extends AbstractRegexValidator {
    public static final String VALIDATOR_ID = "org.openfaces.RegularExpression";

    private static final String REGEX_VALIDATOR_MESSAGE_ID = "org.openfaces.RegexValidatorMessage";

    private String pattern;

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        if (pattern == null)
            return null;

        if (!(AbstractComponentTag.isValueReference(pattern)))
            return pattern;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ValueExpression ve = facesContext.getApplication().getExpressionFactory().createValueExpression(elContext, pattern, Object.class);
        return (String) ve.getValue(elContext);
    }

    @Override
    public String getValidatorMessageID() {
        return REGEX_VALIDATOR_MESSAGE_ID;
    }

    @Override
    public Object saveState(FacesContext facesContext) {
        Object superState = super.saveState(facesContext);

        return new Object[]{
                superState,
                pattern

        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        pattern = (String) state[i++];
    }

    public String getType() {
        return "regexp";
    }
}

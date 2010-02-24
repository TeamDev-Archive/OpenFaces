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

import org.openfaces.util.NewInstanceScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Script;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public abstract class AbstractRegexValidator extends AbstractCustomValidator {

    public abstract String getPattern();

    public abstract String getType();

    protected AbstractRegexValidator() {
        addJavascriptLibrary(new ValidationJavascriptLibrary("regexValidator.js", this.getClass()));
    }

    public boolean customServerValidate(FacesContext context, UIComponent component, Object value, Object[] args) {
        String strValue = Rendering.convertToString(context, component, value);
        if (isTrimNeeded()) {
            strValue = strValue.trim();
        }
        Pattern p = Pattern.compile(getPattern());
        Matcher matcher = p.matcher(strValue);
        return matcher.matches();
    }

    protected boolean isTrimNeeded() {
        return false;
    }

    public Script getClientScript(FacesContext context, UIComponent component) {
        String jsPattern = '^' + getPattern() + '$';
        return new NewInstanceScript(getJsValidatorName(),
                getFormattedSummary(component),
                getFormattedDetail(component),
                jsPattern,
                isTrimNeeded(),
                getType());
    }

    public String getJsValidatorName() {
        return "O$._RegexValidator";
    }

}

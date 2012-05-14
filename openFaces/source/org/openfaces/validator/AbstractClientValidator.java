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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladimir Korenev
 */
public abstract class AbstractClientValidator implements ClientValidator {

    private List<ValidationJavascriptLibrary> javascriptLibraryUrls;
    private static final Object[] EMPTY_ARRAY = new Object[0];

    protected AbstractClientValidator() {
        addJavascriptLibrary(new ValidationJavascriptLibrary("validatorUtil.js"));
    }

    public Script getClientScript(FacesContext context, final UIComponent component) {
        return new NewInstanceScript(getJsValidatorName(), getJsValidatorParametersAsString(context, component));
    }

    public void addJavascriptLibrary(ValidationJavascriptLibrary library) {
        if (library == null) {
            return;
        }
        if (javascriptLibraryUrls == null) {
            javascriptLibraryUrls = new ArrayList<ValidationJavascriptLibrary>();
        }
        javascriptLibraryUrls.add(library);
    }

    public ValidationJavascriptLibrary[] getJavascriptLibraries() {
        if (javascriptLibraryUrls == null) {
            return null;
        }
        if (javascriptLibraryUrls.isEmpty()) {
            return null;
        }

        ValidationJavascriptLibrary[] urls = new ValidationJavascriptLibrary[javascriptLibraryUrls.size()];
        int i = 0;
        for (ValidationJavascriptLibrary s : javascriptLibraryUrls) {
            urls[i++] = s;
        }

        return urls;

    }

    protected Object[] getJsValidatorParametersAsString(FacesContext context, UIComponent component) {
        return EMPTY_ARRAY;
    }
}
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
package org.openfaces.renderkit.validation;

import org.openfaces.util.RawScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class ValidatorUtil {
    private ValidatorUtil() {
    }

    public static void renderPresentationExistsForAllInputComponents(FacesContext context) throws IOException {
        Rendering.renderInitScript(context, new RawScript("O$._presentationExistsForAllComponents();"),
                Resources.utilJsURL(context),
                getValidatorUtilJsUrl(context));
    }

    public static void renderPresentationExistsForComponent(String componentClientId, FacesContext context) throws IOException {
        Rendering.renderInitScript(context,
                new ScriptBuilder().functionCall("O$._presentationExistsForComponent", componentClientId).semicolon(),
                Resources.utilJsURL(context),
                getValidatorUtilJsUrl(context));
    }

    public static String getValidatorUtilJsUrl(FacesContext context) {
        return Resources.internalURL(context, "validation/validatorUtil.js");
    }
}

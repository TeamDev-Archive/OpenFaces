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
package org.openfaces.renderkit;

import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Kharchenko
 */
public interface AjaxPortionRenderer {
    /**
     * This method is required for inner components rendering in case when parent component does not know
     * which HTML appearance inner component should have. For example some components may have additional
     * initialization javascript along with their HTML representations.
     *
     * @param context     instance of {@link javax.faces.context.FacesContext}
     * @param component   parent component for which inner controls should be rendered
     * @param portionName actually a server-side id for the inner component to be rendered. In further, there
     *                    may be an extension for this method to support not only inner components rendering, but data
     *                    retrieval too. Or maybe other method will be declared.
     * @param jsonParam   the JSON object that is passed as a parameter to O$.Ajax.requestComponentPortions on the client-side,
     *                    or null if no parameter was passed.
     * @throws IOException   propagates any IO exceptions that might occur inside of this method
     * @throws JSONException in case of any JSON manipulation failure. It is declared as a checked exception here just
     *                       to free the implementations of this method from having to catch them. Invoker of this method
     *                       will just re-throw this exception as RuntimeException.
     */
    public JSONObject encodeAjaxPortion(
            FacesContext context,
            UIComponent component,
            String portionName,
            JSONObject jsonParam
    )
            throws IOException, JSONException;
}

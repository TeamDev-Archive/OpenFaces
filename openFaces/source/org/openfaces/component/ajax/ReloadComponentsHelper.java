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
package org.openfaces.component.ajax;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIClientActionHelper;
import org.openfaces.org.json.JSONArray;
import org.openfaces.util.RawScript;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;

import javax.faces.context.FacesContext;
import java.util.List;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.
 * 
 * @author Dmitry Pikhulya
 */
public class ReloadComponentsHelper extends OUIClientActionHelper {

    protected String getClientActionScript(FacesContext context, OUIClientAction action) {
        ReloadComponents reloadComponents = (ReloadComponents) action;
        final String id = reloadComponents.getId();
        ReloadComponentsInitializer reloadComponentsInitializer = new ReloadComponentsInitializer() {
            protected Object getActionSourceIdParam(FacesContext context, ReloadComponents reloadComponents) {
                return new RawScript("O$._actionSourceIds['" + id + "']");
            }

            protected Object getReloadComponentsIdParam(FacesContext context, ReloadComponents reloadComponents) {
                return new RawScript("O$._actionIds['" + id + "']");
            }

            protected Object getSubmittedComponentIdsParam(FacesContext context, ReloadComponents reloadComponents, List<String> submittedComponentIds) {
                return new RawScript("O$._submitIds['" + id + "']");
            }
        };

        JSONArray componentIdsArray = reloadComponentsInitializer.getComponentIdsArray(context, action, reloadComponents.getComponentIds());
        String idExpression = "O$._reloadIds['" + id + "']";
        Script componentIds = new RawScript("(" + idExpression + " ? " + idExpression + " : " + componentIdsArray.toString() + ")" );

        ScriptBuilder buf = new ScriptBuilder();
        buf.functionCall("O$.reloadComponents",
                componentIds,
                reloadComponentsInitializer.getReloadParams(context, reloadComponents)).semicolon();
        if (reloadComponents.getDisableDefault()) {
            buf.append("return false;");
        }
        return buf.toString();
    }

}

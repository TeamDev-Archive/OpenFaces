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
package org.openfaces.component.ajax;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIClientActionHelper;
import org.openfaces.component.OUICommand;
import org.openfaces.org.json.JSONArray;
import org.openfaces.util.RawScript;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;

import javax.faces.context.FacesContext;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.
 * 
 * @author Dmitry Pikhulya
 */
public class AjaxHelper extends OUIClientActionHelper {

    protected String getClientActionScript(FacesContext context, OUIClientAction action) {
        Ajax ajax = (Ajax) action;

         if (ajax.isDisabled()){
            return null;
        }
        ScriptBuilder buf = new ScriptBuilder();
        String onevent = ajax.getOnevent();
        if (onevent != null){
            buf.append(onevent);
            buf.semicolon();
        }
        
        final String id = ajax.getId();
        AjaxInitializer ajaxInitializer = new AjaxInitializer() {

            @Override
            protected Object getAjaxComponentParam(FacesContext context, OUICommand ajax) {
                return new RawScript("O$._actionIds['" + id + "']");
            }

            @Override
            protected Object getExecuteParam(FacesContext context, OUICommand ajax, Iterable<String> execute) {
                return new RawScript("O$._executeIds['" + id + "']");
            }
        };

        JSONArray renderArray = ajaxInitializer.getRenderArray(context, (OUICommand) action, ajax.getRender());
        String idExpression = "O$._renderIds['" + id + "']";
        Script render = new RawScript("(" + idExpression + " ? " + idExpression + " : " + renderArray.toString() + ")" );

        buf.functionCall("O$._ajaxReload",
                render,
                ajaxInitializer.getAjaxParams(context, ajax)).semicolon();
        return buf.toString();
    }

}

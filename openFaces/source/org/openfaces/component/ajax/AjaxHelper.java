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
package org.openfaces.component.ajax;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIClientActionHelper;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;
import java.util.Collections;
import java.util.Set;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.
 *
 * @author Dmitry Pikhulya
 */
public class AjaxHelper extends OUIClientActionHelper implements ClientBehavior {
    private Ajax ajax;

    public AjaxHelper(Ajax ajax) {
        this.ajax = ajax;
    }

    public String getClientActionScript(FacesContext context, OUIClientAction action) {
        Ajax ajax = (Ajax) action;
        if (ajax.isDisabled()) return "";

        ScriptBuilder buf = new ScriptBuilder();
        AjaxInitializer ajaxInitializer = new AjaxInitializer();
        buf.functionCall("O$.Ajax._reload",
                ajaxInitializer.getRenderArray(context, ajax, ajax.getRender()),
                ajaxInitializer.getAjaxParams(context, ajax)).semicolon();
        return buf.toString();
    }

    public String getScript(ClientBehaviorContext behaviorContext) {
        return getClientActionScript(behaviorContext.getFacesContext(), ajax);
    }

    public void decode(FacesContext context, UIComponent component) {
    }

    public Set<ClientBehaviorHint> getHints() {
        return Collections.singleton(ClientBehaviorHint.SUBMITTING);
    }

    public void broadcast(BehaviorEvent event) {
    }


}

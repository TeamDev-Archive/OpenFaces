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
package org.openfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;

/**
 * @author Dmitry Pikhulya
 */
public class AjaxActionEvent extends AjaxBehaviorEvent {
    private Object ajaxResult;

    public AjaxActionEvent(UIComponent component, Behavior behavior) {
        super(component, behavior);
    }

    /**
     * @return the request result object as described in the setResult method.
     */
    public Object getAjaxResult() {
        return ajaxResult;
    }

    /**
     * Specifies the value that can optionally be sent to the client-side by the server action processing code. This
     * feature can be utilized for implementing the application-specific logic which requires some data to be sent along
     * with the Ajax response.
     * <p/>
     * A non-null value specified for this property will be assigned to the <code>AjaxRequest</code> object's
     * <code>ajaxResult</code> property.
     *
     * @param ajaxResult any primitive type, String, any Iterable instance or a Map<String, Object>.
     */
    public void setAjaxResult(Object ajaxResult) {
        this.ajaxResult = ajaxResult;
    }
}

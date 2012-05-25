/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.table;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public abstract class StampStateHolder<S> {
    private UIComponent component;
    private Map<String, S> stampStates = new HashMap<String, S>();

    public StampStateHolder(UIComponent component) {
        this.component = component;
    }

    public S getStampState(FacesContext context) {
        String clientId = component.getClientId(context);
        S s = stampStates.get(clientId);
        if (s == null) {
            s = newStampState();
            stampStates.put(clientId, s);
        }
        return s;
    }

    protected abstract S newStampState();

}

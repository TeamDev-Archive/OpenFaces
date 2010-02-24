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
package org.openfaces.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.util.Map;
import java.util.Set;

public class RendererRequestPhaseListener implements PhaseListener {

    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        Map styleClassesMap = Styles.getRegisteredStyleClassesMap(context);
        Set<UIComponent> components = styleClassesMap.keySet();
        for (UIComponent component : components) {
            if (component.isRendered()) {
                Rendering.logWarning(context, "Styles were not rendered for " + component);
            }
        }
    }

    public void beforePhase(PhaseEvent event) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}

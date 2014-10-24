/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.richfaces.demo.beans;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public final class PostbackPhaseListener implements PhaseListener {

    public static final String POSTBACK_ATTRIBUTE_NAME = PostbackPhaseListener.class.getName();

    public void afterPhase(PhaseEvent arg0) {}

	public void beforePhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        requestMap.put(POSTBACK_ATTRIBUTE_NAME, Boolean.TRUE);
	}

	public PhaseId getPhaseId() {
        return PhaseId.APPLY_REQUEST_VALUES;
	}

    public static boolean isPostback() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            ExternalContext externalContext = facesContext.getExternalContext();
            if (externalContext != null) {
                return Boolean.TRUE.equals(
                        externalContext.getRequestMap().get(POSTBACK_ATTRIBUTE_NAME));
            }
        }
        
        return false;
    }
}

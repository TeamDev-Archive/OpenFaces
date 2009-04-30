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

package org.openfaces.testapp.support.QKS340;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class QKS340PhaseListener implements PhaseListener {

    public void beforePhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        ExternalContext externalContext = facesContext.getExternalContext();
        Map params = externalContext.getRequestParameterMap();
        Map requestMap = externalContext.getRequestMap();
        if (requestMap.containsKey("redirected")) {
            return;
        }

        Object scope = params.get("NAV_FORM:SCOPE");
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        String requestURI = request.getRequestURI();

        if ("5".equals(scope)) {
            requestMap.put("redirected", Boolean.TRUE);
            dispatchRequest(facesContext, "/demo/support/QKS340/dataGrid.jsp");
            return;
        }

        if (requestURI.endsWith(".jsp")) {
            return;
        }

        if (scope == null) {
            dispatchRequest(facesContext, "/demo/support/QKS340/main.jsp");
        }

    }

    private void dispatchRequest(FacesContext facesContext, String url) {
        try {
            facesContext.getExternalContext().dispatch(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        facesContext.responseComplete();
    }

    public void afterPhase(PhaseEvent event) {
    }


    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}

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

import org.openfaces.component.validation.ValidatorPhaseListener;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

public abstract class PhaseListenerBase implements PhaseListener {
    /**
     * There's the MyFaces bug with notifying phase listeners twice under portlets. So we just ignore the repeated
     * notifications from the same request.
     * <p/>
     * See http://issues.apache.org/jira/browse/MYFACES-1338
     */
    protected boolean checkPortletMultipleNotifications(PhaseEvent phaseEvent, boolean beforePhaseNotifcation) {
        FacesContext facesContext = phaseEvent.getFacesContext();
        if (!AjaxUtil.isPortletRequest(facesContext))
            return false;

        Object request = facesContext.getExternalContext().getRequest();
        RequestFacade requestFacade = RequestFacade.getInstance(request);
        String phasePassedKey = this.getClass().getName() + ".OF_PHASE_PASSED:" + phaseEvent.getPhaseId().getOrdinal() + ":" + beforePhaseNotifcation;
        if (requestFacade.getAttribute(phasePassedKey) != null)
            return true;
        requestFacade.setAttribute(phasePassedKey, Boolean.TRUE);
        return false;
    }

    protected void checkOurPhaseListenerInvokedOnce(PhaseEvent event) {
        PhaseId phaseId = event.getPhaseId();
        FacesContext facesContext = event.getFacesContext();
        RequestFacade request = RequestFacade.getInstance(facesContext.getExternalContext().getRequest());
        String phaseNotificationKey = this.getClass().getName() + ":phase_passed:" + phaseId.getOrdinal();
        if (request.getAttribute(phaseNotificationKey) != null) {
            Lifecycle lifecycle = getLifecycle();
            PhaseListener[] phaseListeners = lifecycle.getPhaseListeners();
            int ownReferencesFound = 0;
            for (PhaseListener phaseListener : phaseListeners) {
                if (phaseListener instanceof ValidatorPhaseListener)
                    ownReferencesFound++;
            }
            if (ownReferencesFound > 1 && !AjaxUtil.isPortletRenderRequest(facesContext)) {
                throw new IllegalStateException("Second notification for the same phase in the same request occured. phaseId.ordinal: " + phaseId.getOrdinal() +
                        "; phaseId = " + phaseId + "; More than one ValidatorPhaseListener is found to be registered (" + ownReferencesFound + "). Check that only one JSF implementation is deployed with your application.");
            }
        }
        request.setAttribute(phaseNotificationKey, "PhaseId: " + phaseId);
    }

    protected static Lifecycle getLifecycle() {
        LifecycleFactory lf = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        return lf.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    }
}

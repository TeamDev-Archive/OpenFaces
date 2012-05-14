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
package org.openfaces.util;

import org.openfaces.component.validation.ValidatorPhaseListener;

import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import java.util.Map;

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

        ExternalContext extContext = facesContext.getExternalContext();
        String phasePassedKey = this.getClass().getName() + ".OF_PHASE_PASSED:" + phaseEvent.getPhaseId().getOrdinal() + ":" + beforePhaseNotifcation;
        if (extContext.getRequestMap().get(phasePassedKey) != null)
            return true;
        extContext.getRequestMap().put(phasePassedKey, Boolean.TRUE);
        return false;
    }

    protected void checkEnvironment(PhaseEvent event) {
        checkOurPhaseListenerInvokedOnce(event);

        checkMojarraVersion();
    }

    private void checkMojarraVersion()  {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> applicationMap = context.getExternalContext().getApplicationMap();
        String alreadyChecked = getClass().getName() + ".checkMojarraVersion()._alreadyChecked";
        if (applicationMap.containsKey(alreadyChecked)) return;
        applicationMap.put(alreadyChecked, true);

        String mojarraVersion = Environment.getMojarraVersion(context);
        if (mojarraVersion.equals("")) {
//            Log.log("OpenFaces warning: couldn't find Mojarra manifest file to check Mojarra version (Mojarra 2.0.3 or higher is required)");
            return;
        }
        if (mojarraVersion.compareTo("2.0.3") < 0) {
            String message = "OpenFaces requires Mojarra version 2.0.3 or higher, but the following version was found: " + mojarraVersion;
            Log.log("OpenFaces error: " + message);
            throw new IllegalStateException(message);
        }
    }

    protected void checkOurPhaseListenerInvokedOnce(PhaseEvent event) {
        PhaseId phaseId = event.getPhaseId();
        FacesContext context = event.getFacesContext();
        ExternalContext extContext = context.getExternalContext();
        String phaseNotificationKey = this.getClass().getName() + ":phase_passed:" + phaseId.getOrdinal();
        if (extContext.getRequestMap().get(phaseNotificationKey) != null) {
            Lifecycle lifecycle = getLifecycle();
            PhaseListener[] phaseListeners = lifecycle.getPhaseListeners();
            int ownReferencesFound = 0;
            for (PhaseListener phaseListener : phaseListeners) {
                if (phaseListener instanceof ValidatorPhaseListener)
                    ownReferencesFound++;
            }
            if (ownReferencesFound > 1 && !AjaxUtil.isPortletRenderRequest(context)) {
                throw new IllegalStateException("Second notification for the same phase in the same request occurred. phaseId.ordinal: " + phaseId.getOrdinal() +
                        "; phaseId = " + phaseId + "; More than one " + this.getClass().getName() +
                        " is found to be registered (" + ownReferencesFound + "). Check that only one JSF " +
                        "implementation is deployed with your application's classpath, and openfaces.jar is not " +
                        "duplicated in application's and server's libraries.");
            }
        }
        extContext.getRequestMap().put(phaseNotificationKey, "PhaseId: " + phaseId);

    }

    protected static Lifecycle getLifecycle() {
        LifecycleFactory lf = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        return lf.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    }
}

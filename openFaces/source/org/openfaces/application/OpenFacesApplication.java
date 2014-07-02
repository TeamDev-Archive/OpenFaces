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
package org.openfaces.application;

import org.openfaces.util.UtilPhaseListener;

import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.SystemEvent;
import java.util.Map;

/**
 * @author Eugene Goncharov
 */
@SuppressWarnings({"RawUseOfParameterizedType", "deprecation"})
public class OpenFacesApplication extends ApplicationWrapper {
    private static final String CONSTRUCTING_VIEW_KEY = OpenFacesApplication.class.getName() + ".appendingHeaderContent";

    public OpenFacesApplication(Application application) {
        super(application);
    }

    @Override
    public void publishEvent(FacesContext context, Class<? extends SystemEvent> systemEventClass, Object source) {
        boolean postAddToViewEvent = PostAddToViewEvent.class.isAssignableFrom(systemEventClass);

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Object prevConstructingView = null;
        if (postAddToViewEvent)
            prevConstructingView = requestMap.put(CONSTRUCTING_VIEW_KEY, Boolean.TRUE);
        super.publishEvent(context, systemEventClass, source);

        if (postAddToViewEvent && source instanceof UIViewRoot)
            viewRootAddedToView(context);

        if (postAddToViewEvent) {
            if (prevConstructingView == null)
                requestMap.remove(CONSTRUCTING_VIEW_KEY);
            else
                requestMap.put(CONSTRUCTING_VIEW_KEY, prevConstructingView);
        }
    }

    @Override
    public void publishEvent(
            FacesContext context,
            Class<? extends SystemEvent> systemEventClass,
            Class<?> sourceBaseType,
            Object source) {
        boolean postAddToViewEvent = PostAddToViewEvent.class.isAssignableFrom(systemEventClass);

        Map<String, Object> requestMap = null; // avoid invoking getRequestMap() here for MyFaces compatibility (see http://requests.openfaces.org/browse/OF-65) 
        Object prevConstructingView = null;
        if (postAddToViewEvent) {
            requestMap = context.getExternalContext().getRequestMap();
            prevConstructingView = requestMap.put(CONSTRUCTING_VIEW_KEY, Boolean.TRUE);
        }
        super.publishEvent(context, systemEventClass, sourceBaseType, source);
        if (postAddToViewEvent && source instanceof UIViewRoot)
            viewRootAddedToView(context);

        if (postAddToViewEvent) {
            if (prevConstructingView == null)
                requestMap.remove(CONSTRUCTING_VIEW_KEY);
            else
                requestMap.put(CONSTRUCTING_VIEW_KEY, prevConstructingView);
        }
    }

    private void headAddedToView(FacesContext context) {
        if (context.isPostback() && context.getCurrentPhaseId() == PhaseId.RESTORE_VIEW) return;
        UtilPhaseListener.appendHeaderContent(context);
    }

    public static boolean isConstructingView(FacesContext context) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        return requestMap.containsKey(CONSTRUCTING_VIEW_KEY);
    }

    private void viewRootAddedToView(FacesContext context) {
        headAddedToView(context);
    }

}

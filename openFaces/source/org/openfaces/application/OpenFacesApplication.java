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

    private static   ThreadLocal<Boolean> constructingView = new ThreadLocal<Boolean>();

    @Override
    public void publishEvent(FacesContext context, Class<? extends SystemEvent> systemEventClass, Object source) {
        boolean postAddToViewEvent = PostAddToViewEvent.class.isAssignableFrom(systemEventClass);

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Boolean prevConstructingViewBool = null;
        if (postAddToViewEvent){
            prevConstructingViewBool = constructingView.get();
            constructingView.set(Boolean.TRUE);
        }
        super.publishEvent(context, systemEventClass, source);

        if (postAddToViewEvent && source instanceof UIViewRoot)
            viewRootAddedToView(context);

        if (postAddToViewEvent) {
            if (prevConstructingViewBool == null){
                requestMap.remove(CONSTRUCTING_VIEW_KEY);
                constructingView.remove();
            }else{
                constructingView.set(prevConstructingViewBool);
            }
        }
    }

    @Override
    public void publishEvent(
            FacesContext context,
            Class<? extends SystemEvent> systemEventClass,
            Class<?> sourceBaseType,
            Object source) {
        boolean postAddToViewEvent = PostAddToViewEvent.class.isAssignableFrom(systemEventClass);


        Boolean prevConstructingViewBool = null;
        if (postAddToViewEvent) {
            prevConstructingViewBool = constructingView.get();
            constructingView.set(Boolean.TRUE);
        }
        super.publishEvent(context, systemEventClass, sourceBaseType, source);
        if (postAddToViewEvent && source instanceof UIViewRoot)
            viewRootAddedToView(context);

        if (postAddToViewEvent) {
            if (prevConstructingViewBool == null){
                constructingView.remove();
            }else{
                constructingView.set(prevConstructingViewBool);
            }
        }
    }

    private void headAddedToView(FacesContext context) {
        if (context.isPostback() && context.getCurrentPhaseId() == PhaseId.RESTORE_VIEW) return;
        UtilPhaseListener.appendHeaderContent(context);
    }

    public static boolean isConstructingView(FacesContext context) {
        return (constructingView.get()!=null);
    }

    private void viewRootAddedToView(FacesContext context) {
        headAddedToView(context);
    }

}

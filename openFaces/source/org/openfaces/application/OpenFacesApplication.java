/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.application;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlHead;
import javax.faces.context.FacesContext;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene Goncharov
 */
@SuppressWarnings({"RawUseOfParameterizedType", "deprecation"})
public class OpenFacesApplication extends ApplicationWrapper {
    private final List<String> viewHandlers = new ArrayList<String>();

    public OpenFacesApplication(Application application) {
        super(application);
    }


    @Override
    public void setViewHandler(ViewHandler viewHandler) {
        wrapped.setViewHandler(viewHandler);
//        final String viewHandlerClass = viewHandler.getClass().getName();
//        if (viewHandlers.contains(viewHandlerClass) &&
//                !viewHandlerClass.equalsIgnoreCase("org.ajax4jsf.application.AjaxViewHandler")) {
//            return;
//        }
//
//        AjaxViewHandler ajaxViewHandler = viewHandler instanceof AjaxViewHandler
//                ? (AjaxViewHandler) viewHandler
//                : new AjaxViewHandler(viewHandler);
//        wrapped.setViewHandler(ajaxViewHandler);
//
//        viewHandlers.add(viewHandlerClass);
    }

    @SuppressWarnings({"AssignmentToMethodParameter"})
    @Override
    public UIComponent createComponent(String componentType) throws FacesException {
//        if (Environment.isTrinidad() && componentType.equals(UIViewRoot.COMPONENT_TYPE)) {
//            componentType = AjaxViewRoot.COMPONENT_TYPE;
//        }

        return wrapped.createComponent(componentType);
    }

    @Override
    public void publishEvent(FacesContext context, Class<? extends SystemEvent> systemEventClass, Object source) {
        super.publishEvent(context, systemEventClass, source);
        if (PreRenderViewEvent.class.isAssignableFrom(systemEventClass))
            preRenderView(context);
        if (PostAddToViewEvent.class.isAssignableFrom(systemEventClass) && source instanceof HtmlHead)
            headAddedToView(context);
    }

    @Override
    public void publishEvent(FacesContext context, Class<? extends SystemEvent> systemEventClass, Class<?> sourceBaseType, Object source) {
        super.publishEvent(context, systemEventClass, sourceBaseType, source);
        if (PreRenderViewEvent.class.isAssignableFrom(systemEventClass))
            preRenderView(context);
        if (PostAddToViewEvent.class.isAssignableFrom(systemEventClass) && source instanceof HtmlHead)
            headAddedToView(context);
    }

    private void preRenderView(FacesContext context) {
    }

    private void headAddedToView(FacesContext context) {
    }

}

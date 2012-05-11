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
package org.openfaces.application;

import org.openfaces.ajax.AjaxViewHandler;
import org.openfaces.ajax.AjaxViewRoot;
import org.openfaces.util.Environment;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
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
        final String viewHandlerClass = viewHandler.getClass().getName();
        if (viewHandlers.contains(viewHandlerClass) &&
                !viewHandlerClass.equalsIgnoreCase("org.ajax4jsf.application.AjaxViewHandler")) {
            return;
        }

        AjaxViewHandler ajaxViewHandler = viewHandler instanceof AjaxViewHandler
                ? (AjaxViewHandler) viewHandler
                : new AjaxViewHandler(viewHandler);
        wrapped.setViewHandler(ajaxViewHandler);

        viewHandlers.add(viewHandlerClass);
    }

    @SuppressWarnings({"AssignmentToMethodParameter"})
    @Override
    public UIComponent createComponent(String componentType) throws FacesException {
        if (Environment.isTrinidad() && componentType.equals(UIViewRoot.COMPONENT_TYPE)) {
            componentType = AjaxViewRoot.COMPONENT_TYPE;
        }

        return wrapped.createComponent(componentType);
    }

}

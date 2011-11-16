/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.ajax;

import org.openfaces.util.AjaxUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dmitry Pikhulya
 */
public class AjaxRequest {

    private Set<String> reloadedComponentIds = new HashSet<String>();
    private FacesContext context;
    private Object ajaxResult;

    private AjaxRequest(FacesContext context) {
        this.context = context;
    }

    public static AjaxRequest getInstance() {
        return getInstance(FacesContext.getCurrentInstance());
    }

    /**
     * @return an instance of the AjaxRequest class corresponding to the current Ajax request or null,
     *         if the current request is not an Ajax request
     */
    public static AjaxRequest getInstance(FacesContext context) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String key = AjaxRequest.class.getName();
        AjaxRequest instance = (AjaxRequest) requestMap.get(key);
        if (instance != null)
            return instance;
        if (!AjaxUtil.isAjaxRequest(context))
            return null;
        instance = new AjaxRequest(context);
        requestMap.put(key, instance);
        return instance;
    }

    /**
     * Makes a component with the specified id to be reloaded during this Ajax request. This method should be invoked
     * before the "render response" phase, usually during the "invoke application" phase.
     *
     * @param clientId client id of a component which should be reloaded
     */
    public void addReloadedComponent(String clientId) {
        reloadedComponentIds.add(clientId);
    }

    /**
     * Makes the specified component to be reloaded during this Ajax request. This method should be invoked
     * before the "render response" phase, usually during the "invoke application" phase.
     *
     * @param component a component which should be reloaded
     */
    public void addReloadedComponent(UIComponent component) {
        String componentId = component.getClientId(context);
        reloadedComponentIds.add(componentId);
    }

    public Set<String> getReloadedComponentIds() {
        return reloadedComponentIds;
    }

    /**
     * @return the Ajax request result value as described in the <code>setAjaxResult</code> method.
     */
    public Object getAjaxResult() {
        return ajaxResult;
    }

    /**
     * Specifies the value that can optionally be sent to the client-side by the server action processing code. This
     * feature can be utilized for implementing the application-specific logic which requires some data to be sent along
     * with the Ajax response. The value specified with this method will be available through the <code>ajaxResult</code>
     * field of the event object passed to the client-side <code>onajaxend</code> event.
     *
     * @param ajaxResult any primitive type, String, any Iterable instance or a Map<String, Object>.
     */
    public void setAjaxResult(Object ajaxResult) {
        this.ajaxResult = ajaxResult;
    }
}

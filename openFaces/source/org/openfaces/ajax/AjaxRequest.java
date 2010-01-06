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
package org.openfaces.ajax;

import org.openfaces.util.AjaxUtil;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * @author Dmitry Pikhulya
 */
public class AjaxRequest {

    private Set<String> reloadedComponentIds = new HashSet<String>();
    private FacesContext context;

    private AjaxRequest(FacesContext context) {
        this.context = context;
    }

    public static AjaxRequest getInstance() {
        return getInstance(FacesContext.getCurrentInstance());
    }

    public static AjaxRequest getInstance(FacesContext context) {
        Map<String,Object> requestMap = context.getExternalContext().getRequestMap();
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

    public void addReloadedComponent(String clientId) {
        reloadedComponentIds.add(clientId);
    }

    public void addReloadedComponent(UIComponent component) {
        String componentId = component.getClientId(context);
        reloadedComponentIds.add(componentId);
    }

    public Set<String> getReloadedComponentIds() {
        return reloadedComponentIds;
    }
}

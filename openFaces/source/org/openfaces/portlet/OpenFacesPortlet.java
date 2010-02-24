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
package org.openfaces.portlet;

import org.openfaces.util.Environment;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;

/**
 * @author Eugene Goncharov
 */
public class OpenFacesPortlet extends GenericPortlet {
    private static OpenFacesJSFPortlet myFacesDelegate;
    private static OpenFacesMyFacesGenericPortlet myMyFacesDelegate;
    private static boolean myIsRI;

    public OpenFacesPortlet() {
        myIsRI = Environment.isRI();
        if (myIsRI) {
            myFacesDelegate = new OpenFacesJSFPortlet();
        } else {
            myMyFacesDelegate = new OpenFacesMyFacesGenericPortlet();
        }
    }

    public void init(PortletConfig portletConfig) throws PortletException {
        if (myIsRI) {
            myFacesDelegate.init(portletConfig);
        } else {
            myMyFacesDelegate.init(portletConfig);
        }
    }

    public void init() throws PortletException {
        if (!myIsRI) {
            myMyFacesDelegate.init();
        }
    }

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
            throws PortletException, PortletSecurityException, IOException {
        if (myIsRI) {
            myFacesDelegate.processAction(actionRequest, actionResponse);
        } else {
            myMyFacesDelegate.processAction(actionRequest, actionResponse);
        }
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse)
            throws PortletException, PortletSecurityException, IOException {
        if (myIsRI) {
            myFacesDelegate.render(renderRequest, renderResponse);
        } else {
            myMyFacesDelegate.render(renderRequest, renderResponse);
        }
    }


    protected void doView(javax.portlet.RenderRequest renderRequest, javax.portlet.RenderResponse renderResponse)
            throws javax.portlet.PortletException, javax.portlet.PortletSecurityException, java.io.IOException {
        if (!myIsRI) {
            myMyFacesDelegate.doView(renderRequest, renderResponse);
        }
    }

    protected void doHelp(javax.portlet.RenderRequest renderRequest, javax.portlet.RenderResponse renderResponse)
            throws javax.portlet.PortletException, javax.portlet.PortletSecurityException, java.io.IOException {
        if (!myIsRI) {
            myMyFacesDelegate.doHelp(renderRequest, renderResponse);
        }
    }

    protected void doEdit(javax.portlet.RenderRequest renderRequest, javax.portlet.RenderResponse renderResponse)
            throws javax.portlet.PortletException, javax.portlet.PortletSecurityException, java.io.IOException {
        if (!myIsRI) {
            myMyFacesDelegate.doEdit(renderRequest, renderResponse);
        }
    }


    public void destroy() {
        myFacesDelegate.destroy();
    }
}

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

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

public abstract class RequestFacade {
    public abstract String getHeader(String s);

    public abstract String getParameter(String s);

    public abstract Object getAttribute(String s);

    public abstract void setAttribute(String key, Object value);

    public abstract InputStream getInputStream() throws IOException;

    public abstract Locale getLocale();

    private static boolean considerPortlets = true;

    public static RequestFacade getInstance(Object request) {
        if (request == null) throw new NullPointerException("request");

        if (considerPortlets)
            try {
                // portlets configuration should be checked first because a portlet request can both extend HttpServletRequest and
                // implement PortletRequest (as is the case in Apache JetSpeed 2.0)
                if (request instanceof PortletRequest) {
                    return new PortletRequestFacade((PortletRequest) request);
                }
            } catch (NoClassDefFoundError e) {
                considerPortlets = false;
            } catch (IllegalAccessError e) {
                if (e.getMessage().indexOf("PortletRequest") == -1) // we don't check the package here because NoClassDefFoundError and Oracle's AnnotatedNoClassDefFoundError separate packages with different characters (JSFC-2380)
                    throw e;
                else {
                    considerPortlets = false;
                    // This case is reported in QKS-1990 for JDeveloper 10.1.3.0 (reportedly 10.1.3.1 works fine)
                    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                    Map<String, Object> applicationMap = externalContext.getApplicationMap();
                    String messageLoggedKey = "org.openfaces.util.RequestFacade.getInstance.IAE_logged";
                    if (!applicationMap.containsKey(messageLoggedKey)) {
                        applicationMap.put(messageLoggedKey, Boolean.TRUE);
                        externalContext.log("OpenFaces library warning: couldn't check \"instanceof javax.portlet.PortletRequest\"", e);
                    }
                }
            }

        if (request instanceof HttpServletRequest) {
            return new HttpRequestFacade((HttpServletRequest) request);
        }

        throw new IllegalStateException("Wrong request type. " +
                "Expected javax.servlet.http.HttpServletRequest or javax.portlet.PortletRequest, found: "
                + request.getClass().getName());
    }

    public abstract String getCharacterEncoding();

    private static final class HttpRequestFacade extends RequestFacade {

        private HttpServletRequest request;

        private HttpRequestFacade(HttpServletRequest request) {
            this.request = request;
        }

        public Locale getLocale() {
            return request.getLocale();
        }


        public String getHeader(String s) {
            return request.getHeader(s);
        }

        public String getParameter(String s) {
            return request.getParameter(s);
        }

        public InputStream getInputStream() throws IOException {
            return request.getInputStream();
        }

        public Object getAttribute(String s) {
            return request.getAttribute(s);
        }

        public void setAttribute(String key, Object value) {
            request.setAttribute(key, value);
        }

        public String getCharacterEncoding() {
            return request.getCharacterEncoding();
        }
    }

    public static final class PortletRequestFacade extends RequestFacade {

        private PortletRequest request;

        private PortletRequestFacade(PortletRequest request) {
            this.request = request;
        }

        public Locale getLocale() {
            return request.getLocale();
        }

        public String getHeader(String s) {
            return request.getProperty(s);
        }

        public String getParameter(String s) {
            return request.getParameter(s);
        }

        public InputStream getInputStream() throws IOException {
            if (request instanceof ActionRequest) {
                return ((ActionRequest) request).getPortletInputStream();
            }
            throw new IllegalStateException("Cannot obtain inputStream from request of type " + request.getClass().getName());
        }

        public Object getAttribute(String s) {
            return request.getAttribute(s);
        }

        public void setAttribute(String key, Object value) {
            request.setAttribute(key, value);
        }

        public String getCharacterEncoding() {
            if (request instanceof ActionRequest) {
                return ((ActionRequest) request).getCharacterEncoding();
            }
            return null;
        }
    }
}

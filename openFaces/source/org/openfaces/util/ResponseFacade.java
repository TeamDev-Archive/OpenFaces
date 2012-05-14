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
import javax.portlet.ActionResponse;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

public abstract class ResponseFacade implements AbstractResponseFacade {
    public abstract String getContentType();

    public abstract void setContentType(String s);

    public abstract Writer getWriter() throws IOException;

    public abstract OutputStream getOutputStream() throws IOException;

    public abstract String getCharacterEncoding();

    public abstract void setCharacterEncoding(String s);

    private static boolean considerPortlets = true;

    public static ResponseFacade getInstance(Object response) {
        if (response == null) throw new NullPointerException("response");
        if (considerPortlets)
            try {
                // portlets configuration should be checked first because a portlet request can both extend HttpServletResponse and
                // implement RenderResponse/ActionResponse (as is the case in Apache JetSpeed 2.0)
                if (response instanceof RenderResponse) {
                    return new RenderResponseFacade((RenderResponse) response);
                } else if (response instanceof ActionResponse) {
                    return new ActionResponseFacade((ActionResponse) response);
                } else if (response instanceof ResourceResponse) {
                    return new ResourceResponseFacade((ResourceResponse) response);
                }
            } catch (NoClassDefFoundError e) {
                considerPortlets = false;
            } catch (IllegalAccessError e) {
                if (e.getMessage().indexOf("RenderResponse") == -1) // we don't check the package here because NoClassDefFoundError and Oracle's AnnotatedNoClassDefFoundError separate packages with different characters (JSFC-2380)
                    throw e;
                else {
                    considerPortlets = false;
                    // This case is reported in QKS-1990 for JDeveloper 10.1.3.0 (reportedly 10.1.3.1 works fine)
                    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                    Map<String, Object> applicationMap = externalContext.getApplicationMap();
                    String messageLoggedKey = "org.openfaces.util.ResponseFacade.getInstance.IAE_logged";
                    if (!applicationMap.containsKey(messageLoggedKey)) {
                        applicationMap.put(messageLoggedKey, Boolean.TRUE);
                        externalContext.log("OpenFaces library warning: couldn't check \"instanceof javax.portlet.RenderResponse\"", e);
                    }
                }
            }

        if (response instanceof HttpServletResponse) {
            return new HttpResponseFacade((HttpServletResponse) response);
        }
        throw new IllegalStateException("Wrong response type. " +
                "Expected javax.servlet.http.HttpServletResponse or javax.portlet.RenderResponse, found: "
                + response.getClass().getName());
    }

    private static final class HttpResponseFacade extends ResponseFacade {
        private HttpServletResponse response;

        private HttpResponseFacade(HttpServletResponse response) {
            this.response = response;
        }

        public String getContentType() {
            return response.getContentType();
        }

        public void setContentType(String s) {
            response.setContentType(s);
        }

        public Writer getWriter() throws IOException {
            return response.getWriter();
        }

        public OutputStream getOutputStream() throws IOException {
            return response.getOutputStream();
        }

        public String getCharacterEncoding() {
            return response.getCharacterEncoding();
        }

        public void setCharacterEncoding(String s) {
            response.setCharacterEncoding(s);
        }
    }

    public static final class RenderResponseFacade extends ResponseFacade {
        private RenderResponse response;

        private RenderResponseFacade(RenderResponse response) {
            this.response = response;
        }

        public String getContentType() {
            return response.getContentType();
        }

        public void setContentType(String s) {
            response.setContentType(s);
        }

        public Writer getWriter() throws IOException {
            return response.getWriter();
        }

        public OutputStream getOutputStream() throws IOException {
            return response.getPortletOutputStream();
        }

        public String getCharacterEncoding() {
            return response.getCharacterEncoding();
        }

        public void setCharacterEncoding(String s) {
        }
    }

    public static final class ActionResponseFacade extends ResponseFacade {
        private ActionResponse response;

        private ActionResponseFacade(ActionResponse response) {
            this.response = response;
        }

        public String getContentType() {
            throw new UnsupportedOperationException("ActionResponseFacade.getContentType()");
        }

        public void setContentType(String s) {
            throw new UnsupportedOperationException("ActionResponseFacade.setContentType(" + s + ")");
        }

        public Writer getWriter() throws IOException {
            throw new UnsupportedOperationException("ActionResponseFacade.getWriter()");
        }

        public OutputStream getOutputStream() throws IOException {
            throw new UnsupportedOperationException("ActionResponseFacade.getOutputStream()");
        }

        public String getCharacterEncoding() {
            throw new UnsupportedOperationException("ActionResponseFacade.getCharacterEcoding");
        }

        public void setCharacterEncoding(String s) {
            throw new UnsupportedOperationException("ActionResponseFacade.setCharacterEcoding");
        }

        public void setRenderParameter(String param, String value) {
            response.setRenderParameter(param, value);
        }
    }

    public static final class ResourceResponseFacade extends ResponseFacade {

        private ResourceResponse response;

        private ResourceResponseFacade(ResourceResponse response) {
            this.response = response;
        }

        public String getContentType() {
            return response.getContentType();
        }

        public void setContentType(String s) {
            response.setContentType(s);
        }

        public Writer getWriter() throws IOException {
            return response.getWriter();
        }

        public OutputStream getOutputStream() throws IOException {
            return response.getPortletOutputStream();
        }

        public String getCharacterEncoding() {
            return response.getCharacterEncoding();
        }

        public void setCharacterEncoding(String s) {
        }
    }
}

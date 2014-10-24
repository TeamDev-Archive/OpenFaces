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

import org.openfaces.util.Log;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class OpenFacesResourceHandler extends ResourceHandlerWrapper {
    public static final String DYNAMIC_RESOURCE_IDENTIFIER = "/openfaces/dynamicresource";

    private ResourceHandler wrapped;

    public OpenFacesResourceHandler() {
    }

    public OpenFacesResourceHandler(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResourceHandler getWrapped() {
        return wrapped;
    }

    @Override
    public Resource createResource(String resourceName) {
        if (isExternalResource(resourceName)) {
            return new ExternalResource(resourceName);
        }

        if (resourceName.startsWith(DYNAMIC_RESOURCE_IDENTIFIER)) {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            boolean download = requestMap.containsKey(getDownloadRequestedKey());
            return new DynamicResource(resourceName, download);
        }

        return super.createResource(resourceName);
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        if (resourceName.startsWith(DYNAMIC_RESOURCE_IDENTIFIER))
            return new DynamicResource(resourceName);
        Resource resource = super.createResource(resourceName, libraryName);
        if (resource == null) return null;

        return libraryName != null && libraryName.contains("openfaces")
                ? new OpenFacesResource(resource)
                : resource;
    }

    @Override
    public String getRendererTypeForResourceName(String resourceName) {
        if (!isExternalResource(resourceName))
            return super.getRendererTypeForResourceName(resourceName);

        if (resourceName.endsWith(".js")) {
            return "javax.faces.resource.Script";
        } else if (resourceName.endsWith(".css")) {
            return "javax.faces.resource.Stylesheet";
        } else {
            if (resourceName.contains(".js"))
                return "javax.faces.resource.Script";
            else if (resourceName.contains(".css"))
                return "javax.faces.resource.Stylesheet";
            else
                return null;
        }
    }

    private boolean isExternalResource(String resourceName) {
        return resourceName.contains("://");
    }

    @Override
    public Resource createResource(String resourceName, String libraryName, String contentType) {
        if (resourceName.startsWith(DYNAMIC_RESOURCE_IDENTIFIER))
            return new DynamicResource(resourceName);
        Resource resource = super.createResource(resourceName, libraryName, contentType);
        if (resource == null) return null;

        return libraryName != null && libraryName.contains("openfaces")
                ? new OpenFacesResource(resource)
                : resource;
    }

    @Override
    public boolean isResourceRequest(FacesContext context) {
        if (isDynamicResourceRequest(context)) return true;
        return super.isResourceRequest(context);
    }

    private boolean isDynamicResourceRequest(FacesContext context) {
        String path = getDecodedResourcePath(context.getExternalContext());
        if (path == null) {
            // todo: a temporary workaround for OF-203 (getRequestPathInfo returns null under JBoss PortletBridge)
            Log.log(context, "isDynamicResourceRequest failed to detect request path");
            return false;
        }
        if ((path.indexOf(DYNAMIC_RESOURCE_IDENTIFIER) != -1))
            return true;
        return false;
    }

    private String getDecodedResourcePath(ExternalContext extCtx) {
    	if (extCtx.getRequest() instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) extCtx.getRequest();
            String uri = request.getPathInfo();
            if (uri == null)
                uri = request.getRequestURI();
            return uri;
        } else {
    		return extCtx.getRequestPathInfo();
    	}
    }

    @Override
    public void handleResourceRequest(FacesContext context) throws IOException {
        if (isDynamicResourceRequest(context))
            handleDynamicResourceRequest(context);
        else
            super.handleResourceRequest(context);
    }

    private String getResourceName(FacesContext context) {
        String servletMapping = getServletMapping(context);
        boolean prefixMapped = servletMapping.startsWith("/");
        String path;
        if (prefixMapped) {
            path = context.getExternalContext().getRequestPathInfo();
        } else {
            path = context.getExternalContext().getRequestServletPath();
            int extIdx = path.lastIndexOf(".");
            if (extIdx > 0)
                path = path.substring(0, extIdx);
        }
        if (!path.startsWith(RESOURCE_IDENTIFIER)) {
            context.getExternalContext().setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        String resourceName = path.substring(RESOURCE_IDENTIFIER.length() + 1);
        return "/" + resourceName;

    }


    private void handleDynamicResourceRequest(FacesContext context) throws IOException {
        String resourceName = getResourceName(context);
        if (resourceName == null)
            return;

        ExternalContext externalContext = context.getExternalContext();
        try {
            boolean download = externalContext.getRequestParameterMap().containsKey("download");
            if (download)
                externalContext.getRequestMap().put(getDownloadRequestedKey(), Boolean.TRUE);
            Resource resource = createResource(resourceName);
            InputStream is = resource.getInputStream();
            if (is == null) {
                externalContext.setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            byte[] buf = new byte[4096];
            OutputStream os = externalContext.getResponseOutputStream();
            String contentType = resource.getContentType();
            if (contentType != null)
                externalContext.setResponseContentType(contentType);
            Map<String, String> headers = resource.getResponseHeaders();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                externalContext.setResponseHeader(name, value);
            }

            while (true) {
                int bytesRead = is.read(buf);
                if (bytesRead == -1) break;
                os.write(buf, 0, bytesRead);
            }

            is.close();
            os.close();
        } catch (IOException exception) {
            externalContext.setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private String getDownloadRequestedKey() {
        return OpenFacesResourceHandler.class.getName() + ".dynamicResourceDownloadRequested";
    }

    public static String getServletMapping(FacesContext context) {
        String servletPath = context.getExternalContext().getRequestServletPath();
        if (servletPath == null)
            return null;
        if (servletPath.length() == 0)
            return "/*";

        String pathInfo = context.getExternalContext().getRequestPathInfo();
        if (pathInfo != null)
            return servletPath;
        if (servletPath.indexOf(".") == -1)
            return servletPath;

        int extIdx = servletPath.lastIndexOf('.');
        return servletPath.substring(extIdx);
    }
}

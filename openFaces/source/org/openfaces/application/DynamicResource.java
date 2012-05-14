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

import org.openfaces.renderkit.output.DynamicImageRenderer;
import org.openfaces.util.DynamicImagePool;
import org.openfaces.util.Log;

import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class DynamicResource extends Resource {
    private boolean download;

    public DynamicResource(String resourceName) {
        this(resourceName, false);
    }

    public DynamicResource(String resourceName, boolean download) {
        this.download = download;
        setResourceName(resourceName);

        int extIdx = resourceName.lastIndexOf(".");

        String type = extIdx != -1 ? resourceName.substring(extIdx + 1) : "";
        if (type.equals("gif"))
            setContentType("image/gif");
        else if (type.equals("png"))
            setContentType("image/png");
        else if (type.equals("jpg") || type.equals("jpeg"))
            setContentType("image/jpeg");
        else
            setContentType("image");
        setLibraryName("openfaces");
    }

    @Override
    public InputStream getInputStream() throws IOException {
        String resourceName = getResourceName();
        if (!resourceName.startsWith(OpenFacesResourceHandler.DYNAMIC_RESOURCE_IDENTIFIER)) {
            Log.log("Dynamic resource name doesn't start with a proper prefix: " + resourceName);
            return null;
        }
        int extIdx = resourceName.lastIndexOf(".");
        String resourceId = resourceName.substring(
                OpenFacesResourceHandler.DYNAMIC_RESOURCE_IDENTIFIER.length() + 1,
                extIdx != -1 ? extIdx : resourceName.length());
        if (resourceId == null) {
            Log.log("Couldn't retrieve dynamic resource id from resourceName: " + resourceName);
            return null;
        }

        FacesContext context = FacesContext.getCurrentInstance();
        DynamicImagePool pool = (DynamicImagePool) context.getExternalContext().getSessionMap().get(DynamicImageRenderer.IMAGE_POOL);
        if (pool == null) {
            Log.log("Couldn't retrieve image pool on processing a dynamic image request");
            return null;
        }

        byte[] image = pool.getImage(resourceId);
        if (image == null) {
            Log.log("Image was not found in pool: " + resourceId);
            return null;
        }
        return new ByteArrayInputStream(image);
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        HashMap<String, String> map = new HashMap<String, String>();
        if (download) {
            String resourceName = getResourceName();
            int idx = resourceName.lastIndexOf("/") + 1;
            String fileName = idx != -1 ? resourceName.substring(idx) : resourceName; 
            map.put("Content-Disposition", "attachment; filename=" + fileName);
        }
        return map;
    }


    @Override
    public String getRequestPath() {
        FacesContext context = FacesContext.getCurrentInstance();
        String servletMapping = OpenFacesResourceHandler.getServletMapping(context);
        String resourceName = getResourceName();
        StringBuilder sb = new StringBuilder();
        if (servletMapping.startsWith("/"))
            sb.append(servletMapping).append(ResourceHandler.RESOURCE_IDENTIFIER).append(resourceName);
        else
            sb.append(ResourceHandler.RESOURCE_IDENTIFIER).append(resourceName).append(servletMapping);

        ViewHandler viewHandler = context.getApplication().getViewHandler();
        return viewHandler.getResourceURL(context, sb.toString());
    }

    @Override
    public URL getURL() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            return context.getExternalContext().getResource(getRequestPath());
        } catch (MalformedURLException e) {
            throw new FacesException(e);
        }
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        return true;
    }
}

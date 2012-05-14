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

import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class ExternalResource extends Resource {
    public ExternalResource(String url) {
        setResourceName(url);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRequestPath() {
        return getResourceName();
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
        throw new UnsupportedOperationException();
    }
}

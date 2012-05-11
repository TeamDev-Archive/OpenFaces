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

import org.openfaces.util.Resources;

import javax.faces.application.Resource;
import javax.faces.application.ResourceWrapper;

/**
 * @author Dmitry Pikhulya
 */
public class OpenFacesResource extends ResourceWrapper {
    private Resource wrapped;

    public OpenFacesResource(Resource wrapped) {
        if (wrapped == null)
            throw new IllegalArgumentException("wrapped param can't be null");
        this.wrapped = wrapped;
    }

    @Override
    public Resource getWrapped() {
        return wrapped;
    }

    @Override
    public String getRequestPath() {
        String requestPath = super.getRequestPath();
        requestPath += "&ofver=" + Resources.getVersionString();
        return requestPath;
    }

    @Override
    public String getContentType() {
        return getWrapped().getContentType();
    }

    @Override
    public void setContentType(String contentType) {
        getWrapped().setContentType(contentType);
    }

    @Override
    public String getLibraryName() {
        return getWrapped().getLibraryName();
    }

    @Override
    public void setLibraryName(String libraryName) {
        getWrapped().setLibraryName(libraryName);
    }

    @Override
    public String getResourceName() {
        return getWrapped().getResourceName();
    }

    @Override
    public void setResourceName(String resourceName) {
        getWrapped().setResourceName(resourceName);
    }
}
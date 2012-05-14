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

package org.openfaces.renderkit.timetable;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public abstract class VirtualContainer<C extends UIComponent> extends UIComponentBase {

    @Override
    public String getFamily() {
        return "org.openfaces.VirtualContainer";
    }

    protected abstract C getVirtualChild();

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        getVirtualChild().encodeBegin(context);
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        getVirtualChild().encodeChildren(context);
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        getVirtualChild().encodeEnd(context);
    }

    @Override
    public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback) throws FacesException {
        boolean processed = super.invokeOnComponent(context, clientId, callback);
        if (processed) return true;

        processed = getVirtualChild().invokeOnComponent(context, clientId, callback);
        return processed;
    }
}

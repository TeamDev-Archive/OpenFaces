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
package org.openfaces.component.validation;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import java.io.IOException;

/**
 * @author Eugene Goncharov
 */
public class RenderKitTrinidadProxy extends ValidationSupportRenderKit implements Service.Provider, ExtendedRenderKitService {

    public RenderKitTrinidadProxy(RenderKit renderKit) {
        super(renderKit);
    }

    public Object getService(Class tClass) {
        return ((Service.Provider) renderKit).getService(tClass);
    }

    public boolean isStateless(FacesContext context) {
        return ((ExtendedRenderKitService) renderKit).isStateless(context);
    }

    public void addScript(FacesContext context, String script) {
        ((ExtendedRenderKitService) renderKit).addScript(context, script);
    }

    public void encodeScripts(FacesContext context) throws IOException {
        ((ExtendedRenderKitService) renderKit).encodeScripts(context);
    }

    public boolean shortCircuitRenderView(FacesContext context) throws IOException {
        return ((ExtendedRenderKitService) renderKit).shortCircuitRenderView(context);
    }

    public void encodeBegin(FacesContext context) throws IOException {
        ((ExtendedRenderKitService) renderKit).encodeBegin(context);
    }

    public void encodeEnd(FacesContext context) throws IOException {
        ((ExtendedRenderKitService) renderKit).encodeEnd(context);
    }

    public void encodeFinally(FacesContext context) {
        ((ExtendedRenderKitService) renderKit).encodeFinally(context);
    }
}

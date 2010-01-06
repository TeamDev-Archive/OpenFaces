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

import oracle.adf.view.faces.render.DialogRenderKitService;
import oracle.adf.view.faces.render.ExtendedRenderKitService;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import java.io.IOException;
import java.util.Map;

/**
 * @author Vladimir Kurganov
 */
public class RenderKitADFFacesProxy extends ValidationSupportRenderKit implements ExtendedRenderKitService,
        DialogRenderKitService {

    public RenderKitADFFacesProxy(RenderKit renderKit) {
        super(renderKit);
    }

    public boolean launchDialog(FacesContext facesContext, UIViewRoot uiViewRoot, UIComponent uiComponent, Map stringObjectMap, boolean b, Map stringObjectMap1) {
        return ((DialogRenderKitService) renderKit).launchDialog(facesContext, uiViewRoot, uiComponent, stringObjectMap, b, stringObjectMap1);
    }

    public boolean returnFromDialog(FacesContext facesContext, Object o) {
        return ((DialogRenderKitService) renderKit).returnFromDialog(facesContext, o);
    }

    public boolean isReturning(FacesContext facesContext, UIComponent uiComponent) {
        return ((DialogRenderKitService) renderKit).isReturning(facesContext, uiComponent);
    }

    public void encodeScripts(FacesContext facesContext) throws IOException {
        ((ExtendedRenderKitService) renderKit).encodeScripts(facesContext);
    }

    public boolean shortCircuitRenderView(FacesContext facesContext) throws IOException {
        return ((ExtendedRenderKitService) renderKit).shortCircuitRenderView(facesContext);
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        ((ExtendedRenderKitService) renderKit).encodeBegin(facesContext);
    }

    public void encodeEnd(FacesContext facesContext) throws IOException {
        ((ExtendedRenderKitService) renderKit).encodeEnd(facesContext);
    }

    public void encodeFinally(FacesContext facesContext) {
        ((ExtendedRenderKitService) renderKit).encodeFinally(facesContext);
    }
}
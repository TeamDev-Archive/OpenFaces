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

import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import java.io.OutputStream;
import java.io.Writer;

public class ValidationSupportRenderKit extends RenderKit {
    protected RenderKit renderKit;

    public ValidationSupportRenderKit(RenderKit renderKit) {
        this.renderKit = renderKit;
    }

    public void addRenderer(String family, String rendererType, Renderer renderer) {
        renderKit.addRenderer(family, rendererType, renderer);
    }

    public Renderer getRenderer(String family, String rendererType) {
        return renderKit.getRenderer(family, rendererType);
    }

    public ResponseStateManager getResponseStateManager() {
        return renderKit.getResponseStateManager();
    }

    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String characterEncoding) {
        ResponseWriter responseWriter = renderKit.createResponseWriter(writer, contentTypeList, characterEncoding);
        return new ValidationSupportResponseWriter(responseWriter);
    }

    public ResponseStream createResponseStream(OutputStream out) {
        return renderKit.createResponseStream(out);
    }

}

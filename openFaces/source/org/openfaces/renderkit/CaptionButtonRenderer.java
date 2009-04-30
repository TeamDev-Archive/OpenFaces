/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit;

import org.openfaces.component.CaptionButton;
import org.openfaces.component.ComponentWithCaption;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class CaptionButtonRenderer extends RendererBase {
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        CaptionButton btn = (CaptionButton) component;
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("td", btn);
        RenderingUtil.writeComponentClassAttribute(writer, btn, "o_captionButton");
        writeIdAttribute(context, btn);
        writeStandardEvents(writer, btn);

        writer.startElement("img", btn);
        String hint = btn.getHint();
        if (hint != null && hint.length() > 0) {
            writeAttribute(writer, "title", hint);
        }

        renderAdditionalContent(context, btn);
        List params = getInitParams(context, btn);
        ScriptBuilder script = new ScriptBuilder();
        script.initScript(context, btn, getInitFunctionName(), params.toArray());
        List<String> jsLibraries = getJsLibraries(context);
        RenderingUtil.renderInitScript(context, script, jsLibraries.toArray(new String[jsLibraries.size()]));

        StyleUtil.renderStyleClasses(context, component);

        writer.endElement("img");

        writer.endElement("td");
    }

    protected List<String> getJsLibraries(FacesContext context) {
        List<String> jsLibraries = new ArrayList<String>();
        jsLibraries.add(ResourceUtil.getUtilJsURL(context));
        jsLibraries.add(ResourceUtil.getInternalResourceURL(context, CaptionButtonRenderer.class, "captionButton.js"));
        return jsLibraries;
    }

    protected List<Object> getInitParams(FacesContext context, CaptionButton btn) throws IOException {
        List<Object> params = new ArrayList<Object>();
        boolean supportActionAttribute = btn.getActionExpression() != null;
        params.add(supportActionAttribute);
        addCaptionButtonInitParams(context, params, btn);
        return params;
    }

    protected String getInitFunctionName() {
        return "O$._initCaptionButton";
    }

    protected void renderAdditionalContent(FacesContext context, CaptionButton btn) throws IOException {
    }

    protected void addCaptionButtonInitParams(FacesContext context, List<Object> params, CaptionButton btn) {
        UIComponent componentWithCaption = btn.getParent();
        while (componentWithCaption != null && !(componentWithCaption instanceof ComponentWithCaption))
            componentWithCaption = componentWithCaption.getParent();
        checkContainerType((ComponentWithCaption) componentWithCaption);
        params.add(componentWithCaption);
        params.add(StyleUtil.getCSSClass(context, btn, btn.getRolloverStyle(), StyleGroup.rolloverStyleGroup(), btn.getRolloverClass(), getDefaultRolloverClass()));
        params.add(StyleUtil.getCSSClass(context, btn, btn.getPressedStyle(), StyleGroup.selectedStyleGroup(), btn.getPressedClass(), getDefaultPressedClass()));
        params.add(getImageUrl(context, btn));
        params.add(getRolloverImageUrl(context, btn));
        params.add(getPressedImageUrl(context, btn));
    }

    protected void checkContainerType(ComponentWithCaption componentWithCaption) {
    }

    protected String getImageUrl(FacesContext context, CaptionButton btn) {
        String imageUrl = btn.getImageUrl();
        if (imageUrl == null)
            return getDefaultImageUrl(context);
        else
            return ResourceUtil.getApplicationResourceURL(context, imageUrl);
    }

    protected String getRolloverImageUrl(FacesContext context, CaptionButton btn) {
        String imageUrl = btn.getRolloverImageUrl();
        if (imageUrl == null)
            return getDefaultRolloverImageUrl(context);
        else
            return ResourceUtil.getApplicationResourceURL(context, imageUrl);
    }

    protected String getPressedImageUrl(FacesContext context, CaptionButton btn) {
        String imageUrl = btn.getPressedImageUrl();
        if (imageUrl == null)
            return getDefaultPressedImageUrl(context);
        else
            return ResourceUtil.getApplicationResourceURL(context, imageUrl);
    }

    protected String getDefaultImageUrl(FacesContext context) {
        return null;
    }

    protected String getDefaultRolloverImageUrl(FacesContext context) {
        return null;
    }

    protected String getDefaultPressedImageUrl(FacesContext context) {
        return null;
    }

    protected String getDefaultRolloverClass() {
        return "o_captionButton_rollover";
    }

    protected String getDefaultPressedClass() {
        return "o_captionButton_pressed";
    }

    public void decode(FacesContext context, UIComponent component) {
        Map<String, String> requestParameters = context.getExternalContext().getRequestParameterMap();
        String key = component.getClientId(context) + "::clicked";
        if (requestParameters.containsKey(key)) {
            component.queueEvent(new ActionEvent(component));
        }
    }
}

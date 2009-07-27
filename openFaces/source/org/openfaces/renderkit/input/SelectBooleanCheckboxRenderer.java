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
package org.openfaces.renderkit.input;

import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getCurrentImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getDisabledSelectedImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getDisabledUndefinedImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getDisabledUnselectedImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getPressedSelectedImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getPressedUndefinedImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getPressedUnselectedImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getRolloverSelectedImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getRolloverUndefinedImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getRolloverUnselectedImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getSelectedImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getUndefinedImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.getUnselectedImageUrl;
import static org.openfaces.renderkit.input.SelectBooleanCheckboxImageManager.hasImages;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.openfaces.component.input.SelectBooleanCheckbox;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.BooleanObjectConverter;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;

/**
 * @author Roman Porotnikov
 */
public class SelectBooleanCheckboxRenderer extends RendererBase {

    protected static final String TAG_NAME = "input";

    protected static final String STATE_SUFFIX = "::state";

    protected static final String SELECTED_STATE = "on";
    protected static final String UNSELECTED_STATE = "off";
    protected static final String UNDEFINED_STATE = "nil";

    protected static final String TRUE_STRING = "true";
    protected static final String FALSE_STRING = "false";
    protected static final String NULL_STRING = "null";

    protected static final String DEFAULT_IMAGE_STYLE = "cursor:default";


    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        SelectBooleanCheckbox checkbox = (SelectBooleanCheckbox) component;
        setUpConverter(checkbox);
        ComponentUtil.generateIdIfNotSpecified(component);
        super.encodeBegin(context, component);
        if (!component.isRendered()) return;
        renderInputComponent(context, checkbox);
        StyleUtil.renderStyleClasses(context, checkbox);
    }

    protected void renderInputComponent(FacesContext facesContext, SelectBooleanCheckbox checkbox) throws IOException {
        if (isRenderedWithImage(checkbox)) {
            renderWithImage(facesContext, checkbox);
        } else {
            renderWithHtmlCheckbox(facesContext, checkbox);
        }
    }

    protected void renderWithHtmlCheckbox(FacesContext facesContext, SelectBooleanCheckbox checkbox) throws IOException {
      ResponseWriter writer = facesContext.getResponseWriter();

      writer.startElement(TAG_NAME, checkbox);
      writeAttribute(writer, "type", "checkbox");

      String clientId = checkbox.getClientId(facesContext);
      writeAttribute(writer, "id", clientId);
      writeAttribute(writer, "name", clientId);

      writeCommonAttributes(writer, checkbox);

      if (checkbox.isDisabled()) {
          writeAttribute(writer, "disabled", "disabled");
      }

      if (checkbox.isSelected()) {
          writeAttribute(writer, "checked", "checked");
      }

      writer.endElement(TAG_NAME);

      StyleUtil.renderStyleClasses(facesContext, checkbox);

      renderInitScript(facesContext, checkbox, null);

    }

    protected void renderWithImage(FacesContext facesContext, SelectBooleanCheckbox checkbox) throws IOException {

        ResponseWriter writer = facesContext.getResponseWriter();

        // <input type="image" ...

        writer.startElement(TAG_NAME, checkbox);

        writeAttribute(writer, "type", "image");

        String clientId = checkbox.getClientId(facesContext);
        writeAttribute(writer, "id", clientId);

        writeAttribute(writer, "src", getCurrentImageUrl(facesContext, checkbox));

        writeCommonAttributes(writer, checkbox);

        writeAttribute(writer, "style", DEFAULT_IMAGE_STYLE); // TODO style processing

        writer.endElement(TAG_NAME);

        // <input type="hidden" ...

        writer.startElement(TAG_NAME, checkbox);
        writeAttribute(writer, "type", "hidden");

        String stateClientId = clientId + STATE_SUFFIX;
        writeAttribute(writer, "name", stateClientId);
        writeAttribute(writer, "id", stateClientId);
        writeAttribute(writer, "value", getStateFieldValue(checkbox));

        writer.endElement(TAG_NAME);

        // init script

        JSONObject imagesObj = new JSONObject();

        try {
            {
                JSONObject selectedImagesObj = new JSONObject();
                selectedImagesObj.put("plain", getSelectedImageUrl(facesContext, checkbox));
                selectedImagesObj.put("rollover", getRolloverSelectedImageUrl(facesContext, checkbox));
                selectedImagesObj.put("pressed", getPressedSelectedImageUrl(facesContext, checkbox));
                selectedImagesObj.put("disabled", getDisabledSelectedImageUrl(facesContext, checkbox));
                imagesObj.put("on", selectedImagesObj);
            }
            {
                JSONObject unselectedImagesObj = new JSONObject();
                unselectedImagesObj.put("plain", getUnselectedImageUrl(facesContext, checkbox));
                unselectedImagesObj.put("rollover", getRolloverUnselectedImageUrl(facesContext, checkbox));
                unselectedImagesObj.put("pressed", getPressedUnselectedImageUrl(facesContext, checkbox));
                unselectedImagesObj.put("disabled", getDisabledUnselectedImageUrl(facesContext, checkbox));
                imagesObj.put("off", unselectedImagesObj);
            }
            {
                JSONObject undefinedImagesObj = new JSONObject();
                undefinedImagesObj.put("plain", getUndefinedImageUrl(facesContext, checkbox));
                undefinedImagesObj.put("rollover", getRolloverUndefinedImageUrl(facesContext, checkbox));
                undefinedImagesObj.put("pressed", getPressedUndefinedImageUrl(facesContext, checkbox));
                undefinedImagesObj.put("disabled", getDisabledUndefinedImageUrl(facesContext, checkbox));
                imagesObj.put("nil", undefinedImagesObj);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        StyleUtil.renderStyleClasses(facesContext, checkbox);

        renderInitScript(facesContext, checkbox, imagesObj);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        SelectBooleanCheckbox checkbox = (SelectBooleanCheckbox) component;

        if (!checkbox.isRendered() || checkbox.isDisabled()) {
            return;
        }

        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();

        String clientId = checkbox.getClientId(context);

        if (isRenderedWithImage(checkbox)) {
            clientId += STATE_SUFFIX;
        }

        String submittedValue;

        if (requestMap.containsKey(clientId)) {
            String requestValue = requestMap.get(clientId);
            if (requestValue.equalsIgnoreCase("on") || requestValue.equalsIgnoreCase("yes") || requestValue.equalsIgnoreCase(TRUE_STRING)) {
                submittedValue = TRUE_STRING;
            } else if (checkbox.isTriStateAllowed() && (requestValue.equalsIgnoreCase("nil") || requestValue.equalsIgnoreCase(NULL_STRING))) {
                submittedValue = NULL_STRING;
            } else {
                submittedValue = FALSE_STRING;
            }
        } else {
            submittedValue = (checkbox.isTriStateAllowed() ? NULL_STRING : FALSE_STRING);
        }

        checkbox.setSubmittedValue(submittedValue);
    }

    protected String getStateFieldValue(SelectBooleanCheckbox checkbox) {
        if (checkbox.isDefined()) {
            if (checkbox.isSelected()) {
                return SELECTED_STATE;
            } else {
                return UNSELECTED_STATE;
            }
        } else {
            return UNDEFINED_STATE;
        }
    }

    protected boolean isRenderedWithImage(SelectBooleanCheckbox checkbox) {
        return checkbox.isTriStateAllowed() || hasImages(checkbox);
    }

    protected void writeCommonAttributes(ResponseWriter writer, SelectBooleanCheckbox checkbox) throws IOException {
        writeAttribute(writer, "title", checkbox.getTitle());
        writeAttribute(writer, "accesskey", checkbox.getAccesskey());
        writeAttribute(writer, "dir", checkbox.getDir());
        writeAttribute(writer, "lang", checkbox.getLang());
        writeAttribute(writer, "onchange", checkbox.getOnchange());
        writeAttribute(writer, "onselect", checkbox.getOnselect());
        writeAttribute(writer, "tabindex", checkbox.getTabindex());
        writeStandardEvents(writer, checkbox);
    }

    protected void renderInitScript(FacesContext facesContext, SelectBooleanCheckbox checkbox, JSONObject imagesObj) throws IOException {
        Script initScript = new ScriptBuilder().initScript(facesContext, checkbox, "O$.Checkbox._init",
                checkbox.isTriStateAllowed() ? 3 : 2,
                imagesObj,
                checkbox.isDisabled()
        );

        RenderingUtil.renderInitScript(facesContext, initScript,
                new String[] {
                ResourceUtil.getUtilJsURL(facesContext),
                ResourceUtil.getInternalResourceURL(facesContext, SelectBooleanCheckboxRenderer.class, "checkbox.js")
            }
        );
    }

    protected void setUpConverter(SelectBooleanCheckbox checkbox) {
        if (checkbox.getConverter() == null) {
            BooleanObjectConverter converter = new BooleanObjectConverter();
            checkbox.setConverter(converter);
        }
    }

}

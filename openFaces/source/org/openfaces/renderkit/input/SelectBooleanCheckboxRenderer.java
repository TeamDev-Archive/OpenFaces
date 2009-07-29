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
import org.openfaces.component.input.SelectBooleanCheckbox.BooleanObjectValue;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
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

    protected static final String PLAIN_EFFECT = "plain";
    protected static final String ROLLOVER_EFFECT = "rollover";
    protected static final String PRESSED_EFFECT = "pressed";
    protected static final String DISABLED_EFFECT = "disabled";

    protected static final String STYLE_CLASS_KEY = "styleClass";
    protected static final String ROLLOVER_CLASS_KEY = "rolloverClass";
    protected static final String FOCUSED_CLASS_KEY = "focusedClass";
    protected static final String SELECTED_CLASS_KEY = "selectedClass";
    protected static final String UNSELECTED_CLASS_KEY = "unselectedClass";

    protected static final String DEFAULT_IMAGE_CLASS = "o_checkbox_image";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        SelectBooleanCheckbox checkbox = (SelectBooleanCheckbox) component;
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
        String styleClass = StyleUtil.getCSSClass(facesContext, checkbox, checkbox.getStyle(), StyleGroup.regularStyleGroup(), checkbox.getStyleClass(), null);
        String rolloverStyleClass = StyleUtil.getCSSClass(facesContext, checkbox, checkbox.getRolloverStyle(), StyleGroup.regularStyleGroup(1), checkbox.getRolloverClass(), null);
        String focusedStyleClass = StyleUtil.getCSSClass(facesContext, checkbox, checkbox.getFocusedStyle(), StyleGroup.regularStyleGroup(2), checkbox.getFocusedClass(), null);
        String selectedStyleClass = StyleUtil.getCSSClass(facesContext, checkbox, checkbox.getSelectedStyle(), StyleGroup.regularStyleGroup(3), checkbox.getSelectedClass(), null);
        String unselectedStyleClass = StyleUtil.getCSSClass(facesContext, checkbox, checkbox.getUnselectedStyle(), StyleGroup.regularStyleGroup(4), checkbox.getUnselectedClass(), null);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(TAG_NAME, checkbox);
        writeAttribute(writer, "type", "checkbox");

        String clientId = checkbox.getClientId(facesContext);
        writeAttribute(writer, "id", clientId);
        writeAttribute(writer, "name", clientId);

        writeAttribute(writer, "class", styleClass);

        writeCommonAttributes(writer, checkbox);

        if (checkbox.isDisabled()) {
            writeAttribute(writer, "disabled", "disabled");
        }

        if (checkbox.isSelected()) {
            writeAttribute(writer, "checked", "checked");
        }

        writer.endElement(TAG_NAME);

        StyleUtil.renderStyleClasses(facesContext, checkbox);

        JSONObject stylesObj = new JSONObject();
        try {
            stylesObj.put(STYLE_CLASS_KEY, styleClass);
            stylesObj.put(ROLLOVER_CLASS_KEY, rolloverStyleClass);
            stylesObj.put(FOCUSED_CLASS_KEY, focusedStyleClass);
            stylesObj.put(SELECTED_CLASS_KEY, selectedStyleClass);
            stylesObj.put(UNSELECTED_CLASS_KEY, unselectedStyleClass);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        renderInitScript(facesContext, checkbox, null, stylesObj);
    }


    protected void renderWithImage(FacesContext facesContext, SelectBooleanCheckbox checkbox) throws IOException {
        String userStyleClass = StyleUtil.getCSSClass(facesContext, checkbox, checkbox.getStyle(), StyleGroup.regularStyleGroup(), checkbox.getStyleClass(), null);
        String userRolloverStyleClass = StyleUtil.getCSSClass(facesContext, checkbox, checkbox.getRolloverStyle(), StyleGroup.regularStyleGroup(1), checkbox.getRolloverClass(), null);
        String userFocusedStyleClass = StyleUtil.getCSSClass(facesContext, checkbox, checkbox.getFocusedStyle(), StyleGroup.regularStyleGroup(2), checkbox.getFocusedClass(), null);
        String userSelectedStyleClass = StyleUtil.getCSSClass(facesContext, checkbox, checkbox.getSelectedStyle(), StyleGroup.regularStyleGroup(3), checkbox.getSelectedClass(), null);
        String userUnselectedStyleClass = StyleUtil.getCSSClass(facesContext, checkbox, checkbox.getUnselectedStyle(), StyleGroup.regularStyleGroup(4), checkbox.getUnselectedClass(), null);

        String styleClass = StyleUtil.mergeClassNames(userStyleClass, DEFAULT_IMAGE_CLASS);
        String rolloverStyleClass = StyleUtil.mergeClassNames(userRolloverStyleClass, DEFAULT_IMAGE_CLASS);
        String focusedStyleClass = StyleUtil.mergeClassNames(userFocusedStyleClass, DEFAULT_IMAGE_CLASS);
        String selectedStyleClass = StyleUtil.mergeClassNames(userSelectedStyleClass, DEFAULT_IMAGE_CLASS);
        String unselectedStyleClass = StyleUtil.mergeClassNames(userUnselectedStyleClass, DEFAULT_IMAGE_CLASS);

        ResponseWriter writer = facesContext.getResponseWriter();

        // <input type="image" ...

        writer.startElement(TAG_NAME, checkbox);

        writeAttribute(writer, "type", "image");

        String clientId = checkbox.getClientId(facesContext);
        writeAttribute(writer, "id", clientId);

        writeAttribute(writer, "src", getCurrentImageUrl(facesContext, checkbox));

        writeCommonAttributes(writer, checkbox);

        writeAttribute(writer, "class", styleClass);

        writer.endElement(TAG_NAME);

        // <input type="hidden" ...

        writer.startElement(TAG_NAME, checkbox);
        writeAttribute(writer, "type", "hidden");

        String stateClientId = clientId + STATE_SUFFIX;
        writeAttribute(writer, "name", stateClientId);
        writeAttribute(writer, "id", stateClientId);
        writeAttribute(writer, "value", getStateFieldValue(checkbox));

        writer.endElement(TAG_NAME);

        StyleUtil.renderStyleClasses(facesContext, checkbox);

        JSONObject imagesObj = new JSONObject();

        try {
            {
                JSONObject selectedImagesObj = new JSONObject();
                selectedImagesObj.put(PLAIN_EFFECT, getSelectedImageUrl(facesContext, checkbox));
                selectedImagesObj.put(ROLLOVER_EFFECT, getRolloverSelectedImageUrl(facesContext, checkbox));
                selectedImagesObj.put(PRESSED_EFFECT, getPressedSelectedImageUrl(facesContext, checkbox));
                selectedImagesObj.put(DISABLED_EFFECT, getDisabledSelectedImageUrl(facesContext, checkbox));
                imagesObj.put(SELECTED_STATE, selectedImagesObj);
            }
            {
                JSONObject unselectedImagesObj = new JSONObject();
                unselectedImagesObj.put(PLAIN_EFFECT, getUnselectedImageUrl(facesContext, checkbox));
                unselectedImagesObj.put(ROLLOVER_EFFECT, getRolloverUnselectedImageUrl(facesContext, checkbox));
                unselectedImagesObj.put(PRESSED_EFFECT, getPressedUnselectedImageUrl(facesContext, checkbox));
                unselectedImagesObj.put(DISABLED_EFFECT, getDisabledUnselectedImageUrl(facesContext, checkbox));
                imagesObj.put(UNSELECTED_STATE, unselectedImagesObj);
            }
            {
                JSONObject undefinedImagesObj = new JSONObject();
                undefinedImagesObj.put(PLAIN_EFFECT, getUndefinedImageUrl(facesContext, checkbox));
                undefinedImagesObj.put(ROLLOVER_EFFECT, getRolloverUndefinedImageUrl(facesContext, checkbox));
                undefinedImagesObj.put(PRESSED_EFFECT, getPressedUndefinedImageUrl(facesContext, checkbox));
                undefinedImagesObj.put(DISABLED_EFFECT, getDisabledUndefinedImageUrl(facesContext, checkbox));
                imagesObj.put(UNDEFINED_STATE, undefinedImagesObj);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONObject stylesObj = new JSONObject();
        try {
            stylesObj.put(STYLE_CLASS_KEY, styleClass);
            stylesObj.put(ROLLOVER_CLASS_KEY, rolloverStyleClass);
            stylesObj.put(FOCUSED_CLASS_KEY, focusedStyleClass);
            stylesObj.put(SELECTED_CLASS_KEY, selectedStyleClass);
            stylesObj.put(UNSELECTED_CLASS_KEY, unselectedStyleClass);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        renderInitScript(facesContext, checkbox, imagesObj, stylesObj);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        SelectBooleanCheckbox checkbox = (SelectBooleanCheckbox) component;

        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();

        String clientId = checkbox.getClientId(context);

        if (isRenderedWithImage(checkbox)) {
            clientId += STATE_SUFFIX;
        }

        BooleanObjectValue submittedValue;

        if (requestMap.containsKey(clientId)) {
            String requestValue = requestMap.get(clientId);
            if (requestValue.equalsIgnoreCase(SELECTED_STATE) || requestValue.equalsIgnoreCase("yes") || requestValue.equalsIgnoreCase("true")) {
                submittedValue = BooleanObjectValue.TRUE;
            } else if (checkbox.isTriStateAllowed() && (requestValue.equalsIgnoreCase(UNDEFINED_STATE) || requestValue.equalsIgnoreCase("null"))) {
                submittedValue = BooleanObjectValue.NULL; // normal null means no value submitted
            } else {
                submittedValue = BooleanObjectValue.FALSE;
            }
        } else {
            submittedValue = (checkbox.isTriStateAllowed() ? BooleanObjectValue.NULL : BooleanObjectValue.FALSE);
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

    protected void renderInitScript(FacesContext facesContext, SelectBooleanCheckbox checkbox, JSONObject imagesObj, JSONObject stylesObj) throws IOException {
        Script initScript = new ScriptBuilder().initScript(facesContext, checkbox, "O$.Checkbox._init",
                imagesObj,
                stylesObj,
                checkbox.isTriStateAllowed(),
                checkbox.isDisabled()
        );

        RenderingUtil.renderInitScript(facesContext, initScript,
                new String[] {
                ResourceUtil.getUtilJsURL(facesContext),
                ResourceUtil.getInternalResourceURL(facesContext, SelectBooleanCheckboxRenderer.class, "checkbox.js")
            }
        );
    }

 }

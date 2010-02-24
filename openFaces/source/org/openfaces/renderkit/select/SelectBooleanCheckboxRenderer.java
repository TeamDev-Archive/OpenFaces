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
package org.openfaces.renderkit.select;

import org.openfaces.component.select.SelectBooleanCheckbox;
import org.openfaces.component.select.SelectBooleanCheckbox.BooleanObjectValue;
import static org.openfaces.component.select.SelectBooleanCheckbox.SELECTED_STATE;
import static org.openfaces.component.select.SelectBooleanCheckbox.UNDEFINED_STATE;
import static org.openfaces.component.select.SelectBooleanCheckbox.UNSELECTED_STATE;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import static org.openfaces.renderkit.select.SelectBooleanCheckboxImageManager.*;
import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Roman Porotnikov
 */
public class SelectBooleanCheckboxRenderer extends RendererBase {
    private static final Iterable<String> DEFAULT_STATE_LIST = Arrays.asList(SELECTED_STATE, UNSELECTED_STATE);

    private static final String TAG_NAME = "input";

    private static final String STATE_SUFFIX = "::state";

    private static final String PLAIN_EFFECT = "plain";
    private static final String ROLLOVER_EFFECT = "rollover";
    private static final String PRESSED_EFFECT = "pressed";
    private static final String DISABLED_EFFECT = "disabled";

    private static final String STYLE_CLASS_KEY = "styleClass";
    private static final String ROLLOVER_CLASS_KEY = "rolloverClass";
    private static final String FOCUSED_CLASS_KEY = "focusedClass";
    private static final String SELECTED_CLASS_KEY = "selectedClass";
    private static final String UNSELECTED_CLASS_KEY = "unselectedClass";
    private static final String UNDEFINED_CLASS_KEY = "undefinedClass";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        SelectBooleanCheckbox checkbox = (SelectBooleanCheckbox) component;
        Components.generateIdIfNotSpecified(component);
        super.encodeBegin(context, component);
        if (!component.isRendered()) return;
        renderInputComponent(context, checkbox);
        Styles.renderStyleClasses(context, checkbox);
    }

    protected void renderInputComponent(FacesContext facesContext, SelectBooleanCheckbox checkbox) throws IOException {
        boolean triStateAllowed = checkbox.isTriStateAllowed();
        if (isRenderedWithImage(checkbox, triStateAllowed)) {
            renderWithImage(facesContext, checkbox, triStateAllowed);
        } else {
            renderWithHtmlCheckbox(facesContext, checkbox, triStateAllowed);
        }
    }

    protected void renderWithHtmlCheckbox(FacesContext facesContext, SelectBooleanCheckbox checkbox, boolean triStateAllowed) throws IOException {
        String styleClass = Styles.getCSSClass(facesContext, checkbox, checkbox.getStyle(), StyleGroup.regularStyleGroup(), checkbox.getStyleClass(), null);
        String rolloverStyleClass = Styles.getCSSClass(facesContext, checkbox, checkbox.getRolloverStyle(), StyleGroup.regularStyleGroup(1), checkbox.getRolloverClass(), null);
        String focusedStyleClass = Styles.getCSSClass(facesContext, checkbox, checkbox.getFocusedStyle(), StyleGroup.regularStyleGroup(2), checkbox.getFocusedClass(), null);
        String selectedStyleClass = Styles.getCSSClass(facesContext, checkbox, checkbox.getSelectedStyle(), StyleGroup.regularStyleGroup(3), checkbox.getSelectedClass(), null);
        String unselectedStyleClass = Styles.getCSSClass(facesContext, checkbox, checkbox.getUnselectedStyle(), StyleGroup.regularStyleGroup(4), checkbox.getUnselectedClass(), null);

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

        writeAttribute(writer, "onchange", checkbox.getOnchange());

        writer.endElement(TAG_NAME);

        Styles.renderStyleClasses(facesContext, checkbox);

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

        renderInitScript(facesContext, checkbox, null, stylesObj, null, triStateAllowed);
    }


    protected void renderWithImage(FacesContext facesContext, SelectBooleanCheckbox checkbox, boolean triStateAllowed) throws IOException {
        String styleClass = Styles.mergeClassNames(
                Styles.getCSSClass(facesContext, checkbox, checkbox.getStyle(), StyleGroup.regularStyleGroup(), checkbox.getStyleClass(), null),
                "o_checkbox_image");
        String rolloverStyleClass = Styles.getCSSClass(facesContext, checkbox, checkbox.getRolloverStyle(), StyleGroup.regularStyleGroup(1), checkbox.getRolloverClass(), null);
        String focusedStyleClass = Styles.getCSSClass(facesContext, checkbox, checkbox.getFocusedStyle(), StyleGroup.regularStyleGroup(2), checkbox.getFocusedClass(), null);
        String selectedStyleClass = Styles.getCSSClass(facesContext, checkbox, checkbox.getSelectedStyle(), StyleGroup.regularStyleGroup(3), checkbox.getSelectedClass(), null);
        String unselectedStyleClass = Styles.getCSSClass(facesContext, checkbox, checkbox.getUnselectedStyle(), StyleGroup.regularStyleGroup(4), checkbox.getUnselectedClass(), null);
        String undefinedStyleClass = Styles.getCSSClass(facesContext, checkbox, checkbox.getUndefinedStyle(), StyleGroup.regularStyleGroup(5), checkbox.getUndefinedClass(), null);

        ResponseWriter writer = facesContext.getResponseWriter();

        // <input type="image" ...

        writer.startElement(TAG_NAME, checkbox);

        writeAttribute(writer, "type", "image");

        String clientId = checkbox.getClientId(facesContext);
        writeAttribute(writer, "id", clientId);

        writeAttribute(writer, "src", getCurrentImageUrl(facesContext, checkbox));

        writeCommonAttributes(writer, checkbox);

        writer.endElement(TAG_NAME);

        // <input type="hidden" ...

        writer.startElement(TAG_NAME, checkbox);
        writeAttribute(writer, "type", "hidden");

        String stateClientId = clientId + STATE_SUFFIX;
        writeAttribute(writer, "name", stateClientId);
        writeAttribute(writer, "id", stateClientId);
        writeAttribute(writer, "value", getStateFieldValue(checkbox, triStateAllowed));

        writer.endElement(TAG_NAME);

        Styles.renderStyleClasses(facesContext, checkbox);

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
            stylesObj.put(UNDEFINED_CLASS_KEY, undefinedStyleClass);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        AnonymousFunction onchangeFunction = null;
        String onchange = checkbox.getOnchange();

        if (onchange != null) {
            onchangeFunction = new AnonymousFunction(onchange, "event");
        }

        renderInitScript(facesContext, checkbox, imagesObj, stylesObj, onchangeFunction, triStateAllowed);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        SelectBooleanCheckbox checkbox = (SelectBooleanCheckbox) component;

        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();

        String clientId = checkbox.getClientId(context);
        boolean triStateAllowed = checkbox.isTriStateAllowed();
        if (isRenderedWithImage(checkbox, triStateAllowed)) {
            clientId += STATE_SUFFIX;
        }

        BooleanObjectValue submittedValue;

        if (requestMap.containsKey(clientId)) {
            String requestValue = requestMap.get(clientId);
            if (requestValue.equalsIgnoreCase(SELECTED_STATE) || requestValue.equalsIgnoreCase("yes") || requestValue.equalsIgnoreCase("on") || requestValue.equalsIgnoreCase("true")) {
                submittedValue = BooleanObjectValue.TRUE;
            } else if (triStateAllowed && (requestValue.equalsIgnoreCase(UNDEFINED_STATE) || requestValue.equalsIgnoreCase("null"))) {
                submittedValue = BooleanObjectValue.NULL; // normal null means no value submitted
            } else {
                submittedValue = BooleanObjectValue.FALSE;
            }
        } else {
            submittedValue = (triStateAllowed ? BooleanObjectValue.NULL : BooleanObjectValue.FALSE);
        }

        checkbox.setSubmittedValue(submittedValue);
    }

    private void validateStateList(Iterable<String> stateList) {
        for (String state : stateList) {
            if (!state.equals(SELECTED_STATE) &&
                    !state.equals(UNSELECTED_STATE) &&
                    !state.equals(UNDEFINED_STATE)) {
                throw new FacesException("The stateList attribute of <o:selectBooleanCheckbox> tag can contain only one of the following string contants: \"" +
                        SELECTED_CLASS_KEY + "\", \"" + UNSELECTED_STATE + "\", \"" + UNDEFINED_STATE + "\".");
            }
        }
    }

    private Iterable<String> getStateList(SelectBooleanCheckbox checkbox, boolean triStateAllowed) {
        if (triStateAllowed) {
            Iterable<String> stateList = checkbox.getStateList();
            if (stateList == null) {
                return DEFAULT_STATE_LIST;
            } else {
                validateStateList(stateList);
                return stateList;
            }
        }
        return DEFAULT_STATE_LIST;
    }

    protected String getStateFieldValue(SelectBooleanCheckbox checkbox, boolean triStateAllowed) {
        if (checkbox.isDefined() || !triStateAllowed) {
            if (checkbox.isSelected()) {
                return SELECTED_STATE;
            } else {
                return UNSELECTED_STATE;
            }
        } else {
            return UNDEFINED_STATE;
        }
    }

    protected boolean isRenderedWithImage(SelectBooleanCheckbox checkbox, boolean triStateAllowed) {
        return triStateAllowed || hasImages(checkbox);
    }

    protected void writeCommonAttributes(ResponseWriter writer, SelectBooleanCheckbox checkbox) throws IOException {
        writeAttribute(writer, "title", checkbox.getTitle());
        writeAttribute(writer, "accesskey", checkbox.getAccesskey());
        writeAttribute(writer, "dir", checkbox.getDir());
        writeAttribute(writer, "lang", checkbox.getLang());
        writeAttribute(writer, "onselect", checkbox.getOnselect());
        writeAttribute(writer, "tabindex", checkbox.getTabindex());
        Rendering.writeStandardEvents(writer, checkbox);
    }

    protected void renderInitScript(FacesContext facesContext, SelectBooleanCheckbox checkbox,
                                    JSONObject imagesObj, JSONObject stylesObj, AnonymousFunction onchangeFunction, boolean triStateAllowed)
            throws IOException {


        Iterable<String> stateList = getStateList(checkbox, triStateAllowed);
        Script initScript = new ScriptBuilder().initScript(facesContext, checkbox, "O$.Checkbox._init",
                imagesObj,
                stylesObj,
                stateList,

                checkbox.isDisabled(),
                onchangeFunction
        );

        Rendering.renderInitScript(facesContext, initScript,
                Resources.getUtilJsURL(facesContext),
                Resources.getInternalURL(facesContext, SelectBooleanCheckboxRenderer.class, "checkbox.js"));
    }

}

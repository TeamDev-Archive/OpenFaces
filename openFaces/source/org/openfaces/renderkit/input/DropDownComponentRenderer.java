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
package org.openfaces.renderkit.input;

import org.openfaces.component.Side;
import org.openfaces.component.input.DropDownComponent;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.InitScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Andrew Palval
 */
public abstract class DropDownComponentRenderer extends RendererBase {
    public static final String POPUP_SUFFIX = Rendering.SERVER_ID_SUFFIX_SEPARATOR + "popup";
    public static final String FIELD_SUFFIX = "::field";
    protected static final String PROMPT_VISIBLE_SUFFIX = "::promptVisible";

    public static final String BUTTON_SUFFIX = "::button";

    private static final String DEFAULT_FIELD_CLASS = "o_dropdown_field";
    private static final String DEFAULT_CLASS = "o_dropdown";
    private static final String DEFAULT_LIST_CLASS = "o_dropdown_list";
    private static final String DEFAULT_BUTTON_CLASS = "o_dropdown_button o_combo_button";
    protected static final String DEFAULT_PROMPT_CLASS = "o_dropdown_prompt";

    private static final String DEFAULT_DISABLED_CLASS = "o_dropdown_disabled";
    private static final String DEFAULT_DISABLED_FIELD_CLASS = "o_combo_field_disabled";
    protected static final String DEFAULT_DISABLED_BUTTON_CLASS = "o_combo_button_disabled";
    protected static final String DEFAULT_BUTTON_ROLLOVER_CLASS = "o_combo_button_rollover";
    protected static final String DEFAULT_BUTTON_PRESSED_CLASS = "o_combo_button_pressed";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        DropDownComponent dropDownComponent = (DropDownComponent) component;

        registerJS(context, component);

        String clientId = dropDownComponent.getClientId(context);

        // Start render drop down
        // Render first tag
        ResponseWriter writer = context.getResponseWriter();
        encodeRootElementStart(writer, dropDownComponent);
        writer.writeAttribute("id", clientId, "id");

        Rendering.writeStandardEvents(writer, dropDownComponent);

        encodeFieldContentsStart(context, dropDownComponent);
    }

    protected void encodeFieldContentsStart(FacesContext context, DropDownComponent dropDownComponent) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tr", dropDownComponent);
        writer.writeAttribute("height", "100%", null);
        writer.startElement("td", dropDownComponent);
        Side buttonAlignment = (Side) dropDownComponent.getAttributes().get("buttonAlignment");
        if (buttonAlignment.equals(Side.RIGHT)) {
            encodeField(context, dropDownComponent);
            writer.endElement("td");
            writer.startElement("td", dropDownComponent);
            encodeButton(context, dropDownComponent);
        } else {
            encodeButton(context, dropDownComponent);
            writer.endElement("td");
            writer.startElement("td", dropDownComponent);
            encodeField(context, dropDownComponent);
        }

        writer.startElement("div", dropDownComponent);
        // do not propagate clicks from drop-down list
        writer.writeAttribute("onclick", "event.cancelBubble = true; return false;", null);
    }

    protected void encodeRootElementStart(ResponseWriter writer, DropDownComponent dropDownComponent) throws IOException {
        writer.startElement("table", dropDownComponent);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
    }

    protected void encodeStatePrompt(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        DropDownComponent fieldComponent = (DropDownComponent) component;
        String value = Rendering.convertToString(context, fieldComponent, fieldComponent.getValue());

        writer.startElement("input", fieldComponent);
        writeAttribute(writer, "type", "hidden");
        writeAttribute(writer, "id", getFieldClientId(context, (DropDownComponent) component) + PROMPT_VISIBLE_SUFFIX);
        writeAttribute(writer, "name", getFieldClientId(context, (DropDownComponent) component) + PROMPT_VISIBLE_SUFFIX);
        if (value != null && value.length() > 0)
            writeAttribute(writer, "value", "false");
        else
            writeAttribute(writer, "value", "true");
        writer.endElement("input");
    }

    protected void encodeField(FacesContext context, UIComponent component) throws IOException {
        DropDownComponent fieldComponent = (DropDownComponent) component;
        ResponseWriter writer = context.getResponseWriter();

        String fieldId = getFieldClientId(context, fieldComponent);

        writer.writeAttribute("style", "width: 100%; height: 100%", null);

        writer.startElement("input", fieldComponent);
        writer.writeAttribute("id", fieldId, null);
        writer.writeAttribute("value", Rendering.convertToString(context, fieldComponent, fieldComponent.getValue())  , "");
        writer.writeAttribute("name", fieldId, null);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("autocomplete", "off", null);
        writeDefaultFieldStyle(context, writer, fieldComponent);

        if (fieldComponent.isDisabled())
            writer.writeAttribute("disabled", "disabled", null);

        if (fieldComponent.isReadonly())
            writer.writeAttribute("readonly", "readonly", null);

        writeFieldAttributes(writer, fieldComponent);
        writer.endElement("input");

        encodeStatePrompt(context, component);
    }

    protected void writeDefaultFieldStyle(FacesContext context, ResponseWriter writer, DropDownComponent dropDown) throws IOException {
    }

    protected void writeFieldAttributes(ResponseWriter writer, DropDownComponent fieldComponent) throws IOException {
        writeAttribute(writer, "accesskey", fieldComponent.getAccesskey());
        writeAttribute(writer, "tabindex", fieldComponent.getTabindex());
        writeAttribute(writer, "title", fieldComponent.getTitle());
    }

    protected String getFieldClientId(FacesContext context, DropDownComponent dropDown) {
        String clientId = dropDown.getClientId(context);
        return clientId + FIELD_SUFFIX;
    }

    protected void encodeButton(FacesContext context, UIComponent component) throws IOException {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        DropDownComponent fieldComponent = (DropDownComponent) component;
        ResponseWriter writer = context.getResponseWriter();

        // get all ids (main, button, popup)
        String clientId = fieldComponent.getClientId(currentInstance);
        String buttonId = clientId + BUTTON_SUFFIX;

        writer.writeAttribute("nowrap", "nowrap", null);

        // Render drop down button
        writer.writeAttribute("id", buttonId, null);
        writer.writeAttribute("align", "center", null);
        writer.writeAttribute("valign", "middle", null);
        writeAdditionalButtonAttributes(context, writer, fieldComponent);

        String imageUrl;
        if (fieldComponent.isReadonly()) {
            String disabledButtonImageUrl = (String) fieldComponent.getAttributes().get("disabledButtonImageUrl");
            imageUrl = Resources.getURL(context, disabledButtonImageUrl, "input/disabledDropButton.gif");
        } else {
            String buttonImageUrl = (String) fieldComponent.getAttributes().get("buttonImageUrl");
            imageUrl = Resources.getURL(context, buttonImageUrl, "input/dropButton.gif");
        }
        writer.startElement("img", fieldComponent);
        writer.writeAttribute("id", buttonId + "::img", null);
        writer.writeAttribute("src", imageUrl, null);
        writer.endElement("img");
    }

    protected void writeAdditionalButtonAttributes(FacesContext context, ResponseWriter writer, DropDownComponent dropDown) throws IOException {

    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent uiComponent) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        DropDownComponent dropDown = (DropDownComponent) uiComponent;
        ResponseWriter writer = context.getResponseWriter();

        encodeScriptsAndStyles(context, dropDown);
        encodeFieldContentsEnd(writer);
        encodeRootElementEnd(writer);

        writer.flush();
        release(dropDown);
    }

    protected void encodeRootElementEnd(ResponseWriter writer) throws IOException {
        writer.endElement("table");
    }

    protected void encodeFieldContentsEnd(ResponseWriter writer) throws IOException {
        writer.endElement("div");

        writer.endElement("td");
        writer.endElement("tr");
    }

    protected void encodeScriptsAndStyles(FacesContext facesContext, UIComponent component) throws IOException {
        DropDownComponent dropDown = (DropDownComponent) component;
        // Set field text by script to prevent field expanding after submit in IE 6.0 (if field text is long)
        //    another way - use "table-layout: fixed", but it causes strange behavior in IE,
        //    if dropdown width is set to 100% and component is placed into table without concrete width.
        String fieldText = getFieldText(facesContext, dropDown);

        renderEndTags(facesContext, dropDown);

        String promptTextClass = Styles.getCSSClass(facesContext,
                dropDown, dropDown.getPromptTextStyle(), StyleGroup.regularStyleGroup(1), dropDown.getPromptTextClass(),
                DEFAULT_PROMPT_CLASS);

        String promptText = dropDown.getPromptText();

        List<Object> params = new ArrayList<Object>();
        params.add(fieldText);
        params.addAll(getInputStyles(facesContext, dropDown));
        params.addAll(getButtonAndListStyles(facesContext, dropDown));
        params.add(dropDown.isDisabled());
        params.add(dropDown.isReadonly());
        params.add(promptText);
        params.add(promptTextClass);

        Object pullPopupFromContainerObj = dropDown.getAttributes().get("pullPopupFromContainer");
        boolean pullPopupFromContainer = pullPopupFromContainerObj != null &&
                (pullPopupFromContainerObj.equals("true") || pullPopupFromContainerObj.equals(Boolean.TRUE));
        params.add(pullPopupFromContainer);
        ScriptBuilder buf = new ScriptBuilder().initScript(facesContext, dropDown, "O$.DropDown._init",
                params.toArray());

        InitScript commonInitScript = new InitScript(buf, new String[]{
                Resources.utilJsURL(facesContext),
                getDropdownJsURL(facesContext)
        });

        InitScript componentSpecificInitScript = renderInitScript(facesContext, dropDown);
        Rendering.renderInitScripts(facesContext, commonInitScript, componentSpecificInitScript);

        if (isAutomaticStyleRenderingNeeded())
            Styles.renderStyleClasses(facesContext, dropDown);
    }

    protected String getDropdownJsURL(FacesContext context) {
        return Resources.internalURL(context, "input/dropdown.js");
    }

    protected boolean isAutomaticStyleRenderingNeeded() {
        return true;
    }

    protected String getFieldText(FacesContext facesContext, DropDownComponent dropDown) {
        return Rendering.getStringValue(facesContext, dropDown);
    }

    protected String getFocusedClass(FacesContext context, DropDownComponent dropDown) {
        return Styles.getCSSClass(context, dropDown, dropDown.getFocusedStyle(), StyleGroup.selectedStyleGroup(), dropDown.getFocusedClass(), null);
    }

    protected List<String> getInputStyles(FacesContext context, DropDownComponent dropDown) throws IOException {
        String styleClass = getInitialStyleClass(context, dropDown);
        String rolloverClass = Styles.getCSSClass(context, dropDown, dropDown.getRolloverStyle(), StyleGroup.rolloverStyleGroup(), dropDown.getRolloverClass());
        String focusedClass = getFocusedClass(context, dropDown);

        Map<String,Object> attrs = dropDown.getAttributes();
        String fieldClass = getFieldClass(context, dropDown);
        String fieldRolloverClass = Styles.getCSSClass(context, dropDown,
                (String) attrs.get("rolloverFieldStyle"), StyleGroup.rolloverStyleGroup(),
                (String) attrs.get("rolloverFieldClass"));

        String disabledClass = Styles.getCSSClass(context, dropDown, dropDown.getDisabledStyle(),
                StyleGroup.disabledStyleGroup(), dropDown.getDisabledClass(), getDefaultDisabledClass());

        String disabledFieldClass = Styles.getCSSClass(context, dropDown, dropDown.getDisabledFieldStyle(),
                StyleGroup.disabledStyleGroup(), dropDown.getDisabledFieldClass(), getDefaultDisabledFieldClass());

        return Arrays.asList(
                styleClass,
                rolloverClass,
                disabledClass,
                fieldClass,
                fieldRolloverClass,
                disabledFieldClass,
                focusedClass
        );
    }

    protected String getFieldClass(FacesContext context, DropDownComponent dropDown) {
        Map<String, Object> attrs = dropDown.getAttributes();
        return Styles.getCSSClass(context, dropDown,
                (String) attrs.get("fieldStyle"), StyleGroup.regularStyleGroup(),
                (String) attrs.get("fieldClass"), getDefaultFieldClass());
    }

    private List<String> getButtonAndListStyles(FacesContext context, DropDownComponent dropDown) throws IOException {
        Map<String, Object> attrs = dropDown.getAttributes();
        String buttonStyleClass = getButtonClass(context, dropDown);
        String buttonRolloverStyleClass = Styles.getCSSClass(context, dropDown,
                (String) attrs.get("rolloverButtonStyle"), StyleGroup.rolloverStyleGroup(),
                (String) attrs.get("rolloverButtonClass"), DEFAULT_BUTTON_ROLLOVER_CLASS);
        String buttonPressedStyleClass = Styles.getCSSClass(context, dropDown,
                (String) attrs.get("pressedButtonStyle"), StyleGroup.rolloverStyleGroup(2),
                (String) attrs.get("pressedButtonClass"), DEFAULT_BUTTON_PRESSED_CLASS);

        String listStyleClass = Styles.getCSSClass(context, dropDown,
                (String) attrs.get("listStyle"), DEFAULT_LIST_CLASS,
                (String) attrs.get("listClass"));
        listStyleClass = Styles.mergeClassNames(listStyleClass, DefaultStyles.CLASS_DROP_DOWN_LIST);

        String popupRolloverStyleClass = Styles.getCSSClass(context, dropDown,
                (String) attrs.get("rolloverListStyle"), StyleGroup.rolloverStyleGroup(),
                (String) attrs.get("rolloverListClass"));

        String buttonDisabledClass = Styles.getCSSClass(context, dropDown,
                (String) attrs.get("disabledButtonStyle"), StyleGroup.disabledStyleGroup(),
                (String) attrs.get("disabledButtonClass"), DEFAULT_DISABLED_BUTTON_CLASS);

        String buttonDisabledImageUrl = Resources.getURL(context,
                (String) attrs.get("disabledButtonImageUrl"), "input/disabledDropButton.gif");

        return Arrays.asList(
                buttonStyleClass,
                buttonRolloverStyleClass,
                buttonPressedStyleClass,
                buttonDisabledClass,
                buttonDisabledImageUrl,
                listStyleClass,
                popupRolloverStyleClass);
    }

    protected String getButtonClass(FacesContext context, DropDownComponent dropDown) {
        Map<String, Object> attrs = dropDown.getAttributes();
        return Styles.getCSSClass(context, dropDown,
                (String) attrs.get("buttonStyle"), StyleGroup.regularStyleGroup(),
                (String) attrs.get("buttonClass"), DEFAULT_BUTTON_CLASS);
    }

    protected String getInitialStyleClass(FacesContext context, DropDownComponent dropDown) {
        return Styles.getCSSClass(context, dropDown, dropDown.getStyle(), StyleGroup.regularStyleGroup(),
                dropDown.getStyleClass(), getDefaultDropDownClass());
    }

    protected String getDefaultDisabledFieldClass() {
        return DEFAULT_DISABLED_FIELD_CLASS;
    }

    protected String getDefaultDisabledClass() {
        return DEFAULT_DISABLED_CLASS;
    }

    protected String getDefaultDropDownClass() {
        return DEFAULT_CLASS;
    }

    protected String getDefaultFieldClass() {
        return DEFAULT_FIELD_CLASS;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    protected void registerJS(FacesContext facesContext, UIComponent component) throws IOException {
    }

    protected void renderEndTags(FacesContext facesContext, DropDownComponent dropDown) throws IOException {
    }

    protected abstract InitScript renderInitScript(FacesContext context, DropDownComponent dropDown) throws IOException;

    protected void release(DropDownComponent dropDown) {
    }

}

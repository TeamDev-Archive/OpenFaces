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

import org.openfaces.component.HorizontalAlignment;
import org.openfaces.component.input.DropDownComponent;
import org.openfaces.util.InitScript;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.StyleGroup;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrew Palval
 */
public abstract class DropDownComponentRenderer extends RendererBase {
    public static final String POPUP_SUFFIX = RenderingUtil.SERVER_ID_SUFFIX_SEPARATOR + "popup";
    public static final String FIELD_SUFFIX = "::field";
    protected static final String STATE_PROMPT_SUFFIX = "::statePrompt";

    public static final String BUTTON_SUFFIX = "::button";

    private static final String DEFAULT_FIELD_CLASS = "o_dropdown_field";
    private static final String DEFAULT_CLASS = "o_dropdown";
    private static final String DEFAULT_LIST_CLASS = "o_dropdown_list";
    private static final String DEFAULT_BUTTON_CLASS = "o_dropdown_button";
    protected static final String DEFAULT_PROMPT_CLASS = "o_dropdown_prompt";

    private static final String DEFAULT_DISABLED_CLASS = "o_dropdown_disabled";
    private static final String DEFAULT_DISABLED_FIELD_CLASS = "o_combo_field_disabled";
    protected static final String DEFAULT_DISABLED_BUTTON_CLASS = "o_combo_button_disabled";
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

        writeStandardEvents(writer, dropDownComponent);

        encodeFieldContentsStart(context, dropDownComponent);
    }

    protected void encodeFieldContentsStart(FacesContext context, DropDownComponent dropDownComponent) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tr", dropDownComponent);
        writer.writeAttribute("height", "100%", null);
        writer.startElement("td", dropDownComponent);
        HorizontalAlignment buttonAlignment = (HorizontalAlignment) dropDownComponent.getAttributes().get("buttonAlignment");
        if (buttonAlignment.equals(HorizontalAlignment.RIGHT)) {
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

    protected void encodeField(FacesContext context, UIComponent component) throws IOException {
        DropDownComponent fieldComponent = (DropDownComponent) component;
        ResponseWriter writer = context.getResponseWriter();

        // get all ids (main, field))
        String fieldId = getFieldClientId(context, fieldComponent);

        writer.writeAttribute("width", "100%", null);
        writer.writeAttribute("height", "100%", null);

        // Write field tag
        writer.startElement("input", fieldComponent);
        writer.writeAttribute("id", fieldId, null);
        writer.writeAttribute("name", fieldId, null);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("autocomplete", "off", null);

        if (fieldComponent.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", null);
        }

        writeFieldAttributes(writer, fieldComponent);
        writer.endElement("input");

        String value = RenderingUtil.convertToString(context, fieldComponent, fieldComponent.getValue());

        writer.startElement("input", fieldComponent);
        writeAttribute(writer, "type", "hidden");
        writeAttribute(writer, "id", getFieldClientId(context, (DropDownComponent) component) + STATE_PROMPT_SUFFIX);
        writeAttribute(writer, "name", getFieldClientId(context, (DropDownComponent) component) + STATE_PROMPT_SUFFIX);
        if (value != null && value.length() > 0)
            writeAttribute(writer, "value", "false");
        else
            writeAttribute(writer, "value", "true");
        writer.endElement("input");
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

        // get all ids (main, button, popup))
        String clientId = fieldComponent.getClientId(currentInstance);
        String buttonId = clientId + BUTTON_SUFFIX;


        writer.writeAttribute("nowrap", "nowrap", null);
//    writer.writeAttribute("width", "1%", null);

        // Render drop down button
        writer.writeAttribute("id", buttonId, null);

        writer.writeAttribute("align", "center", null);
        writer.writeAttribute("valign", "middle", null);
        String imageUrl;
        if (fieldComponent.isDisabled()) {
            String disabledButtonImageUrl = (String) fieldComponent.getAttributes().get("disabledButtonImageUrl");
            imageUrl = ResourceUtil.getResourceURL(context, disabledButtonImageUrl, DropDownFieldRenderer.class, "disabledDropButton.gif");
        } else {
            String buttonImageUrl = (String) fieldComponent.getAttributes().get("buttonImageUrl");
            imageUrl = ResourceUtil.getResourceURL(context, buttonImageUrl, DropDownFieldRenderer.class, "dropButton.gif");
        }
        writer.startElement("img", fieldComponent);
        writer.writeAttribute("src", imageUrl, null);
        writer.endElement("img");
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
        // Set field text by script to prevent field expanding after summit in IE 6.0 (if field text is long)
        //    another way - use "table-layout: fixed", but it causes strange behavior in IE,
        //    if dropdown width is set to 100% and component is placed into table without concrete width.
        String fieldText = getFieldText(facesContext, dropDown);

        renderEndTags(facesContext, dropDown);

        String promptTextStyleClass = StyleUtil.getCSSClass(facesContext,
                dropDown, dropDown.getPromptTextStyle(), StyleGroup.regularStyleGroup(1), dropDown.getPromptTextClass(),
                DEFAULT_PROMPT_CLASS);

        String promptText = dropDown.getPromptText();
        promptText = promptText == null ? null : "'" + promptText + "'";

        List<Object> params = new ArrayList<Object>();
        params.add(fieldText);
        params.addAll(rendererInputStyles(facesContext, dropDown));
        params.addAll(renderButtonAndListStyles(facesContext, dropDown));
        params.add(dropDown.isDisabled());
        params.add(promptText);
        params.add(promptTextStyleClass);
        ScriptBuilder buf = new ScriptBuilder().initScript(facesContext, dropDown, "O$.DropDown._init", params.toArray());

        InitScript commonInitScript = new InitScript(buf, new String[]{
                ResourceUtil.getUtilJsURL(facesContext),
                getDropDownJsURL(facesContext)
        });

        InitScript componentSpecificInitScript = renderInitScript(facesContext, dropDown);
        RenderingUtil.renderInitScripts(facesContext, new InitScript[]{commonInitScript, componentSpecificInitScript});

        if (isAutomaticStyleRenderingNeeded())
            StyleUtil.renderStyleClasses(facesContext, dropDown);
    }

    protected String getDropDownJsURL(FacesContext context) {
        return ResourceUtil.getInternalResourceURL(context, DropDownComponentRenderer.class, "dropdown.js");
    }

    protected boolean isAutomaticStyleRenderingNeeded() {
        return true;
    }

    protected String getFieldText(FacesContext facesContext, DropDownComponent dropDown) {
        return RenderingUtil.getStringValue(facesContext, dropDown);
    }

    protected List<String> rendererInputStyles(FacesContext context, DropDownComponent dropDown) throws IOException {
        // Render main style declaration if it is exist
        String styleClass = getInitialStyleClass(context, dropDown);
        String rolloverStyleClass = StyleUtil.getCSSClass(context, dropDown, dropDown.getRolloverStyle(), StyleGroup.rolloverStyleGroup(), dropDown.getRolloverClass());
        String focusedStyleClass = StyleUtil.getCSSClass(context, dropDown, dropDown.getFocusedStyle(), StyleGroup.selectedStyleGroup(), dropDown.getFocusedClass(), null);


        String fieldStyle = (String) dropDown.getAttributes().get("fieldStyle");
        String fieldClass = (String) dropDown.getAttributes().get("fieldClass");
        String fieldStyleClass = StyleUtil.getCSSClass(context, dropDown, fieldStyle, StyleGroup.regularStyleGroup(), fieldClass, getDefaultFieldClass());
        String rolloverFieldStyle = (String) dropDown.getAttributes().get("rolloverFieldStyle");
        String rolloverFieldClass = (String) dropDown.getAttributes().get("rolloverFieldClass");
        String fieldRolloverStyleClass = StyleUtil.getCSSClass(context, dropDown, rolloverFieldStyle, StyleGroup.rolloverStyleGroup(), rolloverFieldClass);

        if (dropDown.isDisabled()) {
            rolloverStyleClass = fieldRolloverStyleClass = "";
            String disabledStyleClass = StyleUtil.getCSSClass(context, dropDown, dropDown.getDisabledStyle(),
                    StyleGroup.disabledStyleGroup(), dropDown.getDisabledClass(), getDefaultDisabledClass());
            String disabledFieldStyleClass = StyleUtil.getCSSClass(context, dropDown, dropDown.getDisabledFieldStyle(),
                    StyleGroup.disabledStyleGroup(), dropDown.getDisabledFieldClass(), getDefaultDisabledFieldClass());

            if (RenderingUtil.isNullOrEmpty(dropDown.getDisabledStyle()) && RenderingUtil.isNullOrEmpty(dropDown.getDisabledClass())) {
                styleClass = StyleUtil.mergeClassNames(disabledStyleClass, styleClass);
            } else {
                styleClass = StyleUtil.mergeClassNames(disabledStyleClass, StyleUtil.getCSSClass(context, dropDown, null
                        , StyleGroup.regularStyleGroup(), null, getDefaultDropDownClass()));
            }

            if (RenderingUtil.isNullOrEmpty(dropDown.getDisabledFieldStyle()) && RenderingUtil.isNullOrEmpty(dropDown.getDisabledFieldClass())) {
                fieldStyleClass = StyleUtil.mergeClassNames(disabledFieldStyleClass, fieldStyleClass);
            } else {
                fieldStyleClass = StyleUtil.mergeClassNames(disabledFieldStyleClass, StyleUtil.getCSSClass(context, dropDown, null,
                        StyleGroup.regularStyleGroup(), null, getDefaultFieldClass()));
            }
        }

        return Arrays.asList(
                styleClass,
                rolloverStyleClass,
                fieldStyleClass,
                fieldRolloverStyleClass,
                focusedStyleClass
        );
    }

    private List<String> renderButtonAndListStyles(FacesContext context, DropDownComponent dropDown) throws IOException {
        // Render main style declaration if it is exist

        String buttonStyle = (String) dropDown.getAttributes().get("buttonStyle");
        String buttonClass = (String) dropDown.getAttributes().get("buttonClass");
        String buttonStyleClass = StyleUtil.getCSSClass(context, dropDown, buttonStyle, StyleGroup.regularStyleGroup(), buttonClass, DEFAULT_BUTTON_CLASS);
        String rolloverButtonStyle = (String) dropDown.getAttributes().get("rolloverButtonStyle");
        String rolloverButtonClass = (String) dropDown.getAttributes().get("rolloverButtonClass");
        String buttonRolloverStyleClass = StyleUtil.getCSSClass(context, dropDown, rolloverButtonStyle, StyleGroup.rolloverStyleGroup(), rolloverButtonClass);
        String pressedButtonStyle = (String) dropDown.getAttributes().get("pressedButtonStyle");
        String pressedButtonClass = (String) dropDown.getAttributes().get("pressedButtonClass");
        String buttonPressedStyleClass = StyleUtil.getCSSClass(context, dropDown, pressedButtonStyle, StyleGroup.rolloverStyleGroup(2), pressedButtonClass, DEFAULT_BUTTON_PRESSED_CLASS);

        String listClass = (String) dropDown.getAttributes().get("listClass");
        String listStyle = (String) dropDown.getAttributes().get("listStyle");
        String listStyleClass = StyleUtil.getCSSClass(context, dropDown, listStyle, DEFAULT_LIST_CLASS, listClass);
        listStyleClass = StyleUtil.mergeClassNames(listStyleClass, DefaultStyles.CLASS_DROP_DOWN_LIST);

        String rolloverListClass = (String) dropDown.getAttributes().get("rolloverListClass");
        String rolloverListStyle = (String) dropDown.getAttributes().get("rolloverListStyle");
        String popupRolloverStyleClass = StyleUtil.getCSSClass(context, dropDown, rolloverListStyle, StyleGroup.rolloverStyleGroup(), rolloverListClass);

        if (dropDown.isDisabled()) {
            buttonRolloverStyleClass = popupRolloverStyleClass = "";
            String disabledButtonStyle = (String) dropDown.getAttributes().get("disabledButtonStyle");
            String disabledButtonClass = (String) dropDown.getAttributes().get("disabledButtonClass");
            String disabledButtonStyleClass = StyleUtil.getCSSClass(context, dropDown, disabledButtonStyle,
                    StyleGroup.disabledStyleGroup(), disabledButtonClass, DEFAULT_DISABLED_BUTTON_CLASS);

            if (RenderingUtil.isNullOrEmpty(disabledButtonStyle) && RenderingUtil.isNullOrEmpty(disabledButtonClass)) {
                buttonStyleClass = StyleUtil.mergeClassNames(disabledButtonStyleClass, buttonStyleClass);
            } else {
                buttonStyleClass = StyleUtil.mergeClassNames(disabledButtonStyleClass, StyleUtil.getCSSClass(context,
                        dropDown, null, StyleGroup.regularStyleGroup(), null, DEFAULT_BUTTON_CLASS));
            }
        }

        return Arrays.asList(
                buttonStyleClass,
                buttonRolloverStyleClass,
                buttonPressedStyleClass,
                listStyleClass,
                popupRolloverStyleClass);
    }

    protected String getInitialStyleClass(FacesContext context, DropDownComponent dropDown) {
        return StyleUtil.getCSSClass(context, dropDown, dropDown.getStyle(), StyleGroup.regularStyleGroup(), dropDown.getStyleClass(), getDefaultDropDownClass());
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

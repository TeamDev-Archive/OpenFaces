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

import org.openfaces.component.input.DropDownComponent;
import org.openfaces.component.input.Spinner;
import org.openfaces.util.InitScript;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.ComponentUtil;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.IntegerConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpinnerRenderer extends DropDownComponentRenderer {
    private static final String DEFAULT_FIELD_CLASS = "o_spinner_field";
    private static final String DEFAULT_CLASS = "o_spinner";

    protected static final String FIELD_SUFFIX = "::field";

    private static final String INCREASE_BUTTON_SUFFIX = "::increase_button";
    private static final String DECREASE_BUTTON_SUFFIX = "::decrease_button";
    private static final String DEFAULT_BUTTON_CLASS = "o_spinner_button";

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        setUpConverter((Spinner) component);
        ComponentUtil.generateIdIfNotSpecified(component);
        super.encodeBegin(context, component);
    }

    private void setUpConverter(Spinner spinner) {
        if (spinner.getConverter() == null) {
            IntegerConverter converter = new IntegerConverter();
            spinner.setConverter(converter);
        }
    }

    protected InitScript renderInitScript(FacesContext context, DropDownComponent dropDown) throws IOException {
        Spinner spinner = (Spinner) dropDown;
        if (spinner.getStep() <= 0) {
            throw new FacesException("The 'step' attribute of component " + getClass().getName() + " should be greater then 0.");
        }

        String buttonStyle = (String) dropDown.getAttributes().get("buttonStyle");
        String buttonClass = (String) dropDown.getAttributes().get("buttonClass");
        String buttonStyleClass = StyleUtil.getCSSClass(context, dropDown, buttonStyle, DEFAULT_BUTTON_CLASS, buttonClass);
        String rolloverButtonStyle = (String) dropDown.getAttributes().get("rolloverButtonStyle");
        String rolloverButtonClass = (String) dropDown.getAttributes().get("rolloverButtonClass");
        String buttonRolloverStyleClass = StyleUtil.getCSSClass(context, dropDown, rolloverButtonStyle, StyleGroup.rolloverStyleGroup(), rolloverButtonClass);
        String pressedButtonStyle = (String) dropDown.getAttributes().get("pressedButtonStyle");
        String pressedButtonClass = (String) dropDown.getAttributes().get("pressedButtonClass");
        String buttonPressedStyleClass = StyleUtil.getCSSClass(context, dropDown, pressedButtonStyle, StyleGroup.rolloverStyleGroup(2), pressedButtonClass, DEFAULT_BUTTON_PRESSED_CLASS);

        if (dropDown.isDisabled()) {
            buttonRolloverStyleClass = "";
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

        ScriptBuilder sb = new ScriptBuilder().initScript(context, spinner, "O$._initSpinner",
                spinner.getMinValue(),
                spinner.getMaxValue(),
                spinner.getStep(),
                spinner.isCycled(),
                buttonStyleClass,
                buttonRolloverStyleClass,
                buttonPressedStyleClass,
                spinner.isDisabled(),
                spinner.getOnchange());

        return new InitScript(sb, new String[]{
                ResourceUtil.getUtilJsURL(context),
                getDropDownJsURL(context),
                ResourceUtil.getInternalResourceURL(context, SpinnerRenderer.class, "spinner.js")
        });
    }

    protected void writeFieldAttributes(ResponseWriter writer, DropDownComponent fieldComponent) throws IOException {
        super.writeFieldAttributes(writer, fieldComponent);
        Spinner spinner = ((Spinner) fieldComponent);
        if (!spinner.getTypingAllowed())
            writeAttribute(writer, "readonly", String.valueOf(!spinner.getTypingAllowed()));
    }

    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) {
        return RenderingUtil.convertFromString(context, component, (String) submittedValue);
    }

    public void decode(FacesContext context, UIComponent component) {
        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        DropDownComponent dropDownComponent = (DropDownComponent) component;
        String fieldId = getFieldClientId(context, dropDownComponent);

        String value = requestMap.get(fieldId);
        String state = requestMap.get(fieldId + STATE_PROMPT_SUFFIX);

        if ("false".equals(state) && value != null) {
            dropDownComponent.setSubmittedValue(value);
        }

    }

    protected void encodeButton(FacesContext context, UIComponent component) throws IOException {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        Spinner fieldComponent = (Spinner) component;
        ResponseWriter writer = context.getResponseWriter();

        String clientId = fieldComponent.getClientId(currentInstance);
        String increaseButtonId = clientId + INCREASE_BUTTON_SUFFIX;
        String decreaseButtonId = clientId + DECREASE_BUTTON_SUFFIX;
        encodeRootElementStart(writer, fieldComponent);
        writer.writeAttribute("nowrap", "nowrap", null);
        writer.writeAttribute("height", "100%", null);
        writer.writeAttribute("width", "100%", null);

        // Render increase button
        encodeButton(context, component, increaseButtonId, "disabledIncreaseButtonImageUrl",
                "disabledIncreaseButton.gif", "increaseButtonImageUrl", "increaseButton.gif");
        // Render decrease button
        encodeButton(context, component, decreaseButtonId, "disabledDecreaseButtonImageUrl",
                "disabledDecreaseButton.gif", "decreaseButtonImageUrl", "decreaseButton.gif");

        encodeRootElementEnd(writer);
    }

    protected void encodeButton(FacesContext context, UIComponent component, String buttonID, String disabledImageUrl,
                                String disabledImage, String imageUrl, String image) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Spinner fieldComponent = (Spinner) component;

        writer.startElement("tr", fieldComponent);
        writer.startElement("td", fieldComponent);

        writer.writeAttribute("id", buttonID, null);
        writer.writeAttribute("align", "center", null);
        writer.writeAttribute("valign", "middle", null);
        String resultImageUrl;
        if (fieldComponent.isDisabled()) {
            String disabledButtonImageUrl = (String) fieldComponent.getAttributes().get(disabledImageUrl);
            resultImageUrl = ResourceUtil.getResourceURL(context, disabledButtonImageUrl, SpinnerRenderer.class, disabledImage);
        } else {
            String buttonImageUrl = (String) fieldComponent.getAttributes().get(imageUrl);
            resultImageUrl = ResourceUtil.getResourceURL(context, buttonImageUrl, SpinnerRenderer.class, image);
        }
        writer.startElement("img", fieldComponent);
        writer.writeAttribute("src", resultImageUrl, null);
        writer.endElement("img");
        writer.endElement("td");
        writer.endElement("tr");
    }

    public void encodeScriptsAndStyles(FacesContext facesContext, UIComponent component) throws IOException {
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

        List<String> params = new ArrayList<String>();
        params.add(fieldText);
        params.addAll(rendererInputStyles(facesContext, dropDown));
        params.add(promptText);
        params.add(promptTextStyleClass);
        ScriptBuilder buf = new ScriptBuilder().initScript(facesContext, dropDown, "O$._initDropDownInput", params.toArray());

        InitScript commonInitScript = new InitScript(buf, new String[]{
                ResourceUtil.getUtilJsURL(facesContext),
                getDropDownJsURL(facesContext)
        });

        InitScript componentSpecificInitScript = renderInitScript(facesContext, dropDown);
        RenderingUtil.renderInitScripts(facesContext, new InitScript[]{commonInitScript, componentSpecificInitScript});

        if (isAutomaticStyleRenderingNeeded())
            StyleUtil.renderStyleClasses(facesContext, dropDown);
    }

    protected boolean isAutomaticStyleRenderingNeeded() {
        return true;
    }

    protected String getDefaultDropDownClass() {
        return DEFAULT_CLASS;
    }

    protected String getDefaultFieldClass() {
        return DEFAULT_FIELD_CLASS;
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        super.encodeChildren(context, component);
        RenderingUtil.encodeClientActions(context, component);
    }

}

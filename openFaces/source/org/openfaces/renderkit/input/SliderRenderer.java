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

import org.openfaces.component.FillDirection;
import org.openfaces.component.chart.Orientation;
import org.openfaces.component.input.Slider;
import org.openfaces.component.input.TextFieldState;
import org.openfaces.component.input.TicksAlignment;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.NumberConverter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : roman.nikolaienko
 */
public class SliderRenderer extends RendererBase {

    private static final String DEFAULT_RIGHT_BOTTOM_BUTTON_SUFFIX = "::rightBottomButton";
    private static final String DEFAULT_LEFT_TOP_BUTTON_SUFFIX = "::leftTopButton";
    private static final String DEFAULT_TEXT_FIELD_SUFFIX = "::textField";
    private static final String DEFAULT_BAR_SUFFIX = "::bar";
    private static final String DEFAULT_HANDLE_SUFFIX = "::handle";
    private static final String DEFAULT_TICKS_SUFFIX = "::ticks";
    private static final String DEFAULT_LEFT_BOTTOM_TICKS = "LT";
    private static final String DEFAULT_RIGHT_TOP_TICKS = "RB";
    private static final String DEFAULT_A_TICK_SUFFIX = "::tick";
    private static final String DEFAULT_WORK_SPACE_SUFFIX = "::workspace";
    private static final String DEFAULT_ELEMENT_IMAGE_ID = "::image";
    private static final String DEFAULT_ELEMENT_TEXT_ID = "::text";

    private static final String DEFAULT_MAJOR_TICK_URL = "input/slider_majorTick.png";
    private static final String DEFAULT_MINOR_TICK_URL = "input/slider_minorTick.png";

    private static final String DEFAULT_HANDLE_URL = "input/slider-active-hor.png";
    private static final String DEFAULT_HANDLE_ROLLOVER_URL = "input/slider-active-hor_rollover.png";
    private static final String DEFAULT_HANDLE_DISABLED_URL = "input/slider-inactive-hor.png";

    private static final String DEFAULT_BAR_URL = "input/active-hor-mid.png";
    private static final String DEFAULT_BAR_DISABLED_URL = "input/inactive-hor-mid.png";

    private static final String DEFAULT_BAR_START_URL = "input/active-hor-start.png";
    private static final String DEFAULT_BAR_START_DISABLED_URL = "input/inactive-hor-start.png";
    private static final String DEFAULT_BAR_END_URL = "input/active-hor-end.png";
    private static final String DEFAULT_BAR_END_DISABLED_URL = "input/inactive-hor-end.png";

    private static final String DEFAULT_RB_BUTTON_URL = "input/slider_increaseButton.png";
    private static final String DEFAULT_RB_BUTTON_ROLLOVER_URL = "input/slider_increaseButton_rollover.png";
    private static final String DEFAULT_RB_BUTTON_DISABLED_URL = "input/slider_disabledIncreaseButton.png";

    private static final String DEFAULT_LT_BUTTON_URL = "input/slider_decreaseButton.png";
    private static final String DEFAULT_LT_BUTTON_ROLLOVER_URL = "input/slider_decreaseButton_rollover.png";
    private static final String DEFAULT_LT_BUTTON_DISABLED_URL = "input/slider_disabledDecreaseButton.png";

    private static final String DEFAULT_VERTICAL_MAJOR_TICK_URL = "input/slider_majorTick_vertical.png";
    private static final String DEFAULT_VERTICAL_MINOR_TICK_URL = "input/slider_minorTick_vertical.png";

    private static final String DEFAULT_VERTICAL_HANDLE_URL = "input/slider-active-ver.png";
    private static final String DEFAULT_VERTICAL_HANDLE_ROLLOVER_URL = "input/slider-active-ver_rollover.png";
    private static final String DEFAULT_VERTICAL_HANDLE_DISABLED_URL = "input/slider-inactive-ver.png";

    private static final String DEFAULT_VERTICAL_BAR_URL = "input/active-ver-mid.png";
    private static final String DEFAULT_VERTICAL_BAR_DISABLED_URL = "input/inactive-ver-mid.png";
    private static final String DEFAULT_VERTICAL_BAR_START_URL = "input/active-ver-start.png";
    private static final String DEFAULT_VERTICAL_BAR_START_DISABLED_URL = "input/inactive-ver-start.png";
    private static final String DEFAULT_VERTICAL_BAR_END_URL = "input/active-ver-end.png";
    private static final String DEFAULT_VERTICAL_BAR_END_DISABLED_URL = "input/inactive-ver-end.png";

    private static final String DEFAULT_VERTICAL_RB_BUTTON_URL = "input/slider_decreaseButton_vertical.png";
    private static final String DEFAULT_VERTICAL_RB_BUTTON_ROLLOVER_URL = "input/slider_decreaseButton_vertical_rollover.png";
    private static final String DEFAULT_VERTICAL_RB_BUTTON_DISABLED_URL = "input/slider_disabledDecreaseButton_vertical.png";

    private static final String DEFAULT_VERTICAL_LT_BUTTON_URL = "input/slider_increaseButton_vertical.png";
    private static final String DEFAULT_VERTICAL_LT_BUTTON_ROLLOVER_URL = "input/slider_increaseButton_vertical_rollover.png";
    private static final String DEFAULT_VERTICAL_LT_BUTTON_DISABLED_URL = "input/slider_disabledIncreaseButton_vertical.png";

    private static final String DEFAULT_CLASS = "o_slider";
    private static final String HORIZONTAL_CLASS = "o_slider_horizontal";
    private static final String HORIZONTAL_CENTER_CLASS = "o_slider_horizontal_center";
    private static final String VERTICAL_CLASS = "o_slider_vertical";
    private static final String VERTICAL_CENTER_CLASS = "o_slider_vertical_center";

    private static final String DEFAULT_DISABLED_CLASS = "o_slider_disabled";
    private static final String DEFAULT_FOCUSED_CLASS = "o_slider_focused";
    private static final String DEFAULT_TOOLTIP_CLASS = "o_slider_tooltip";

    private static final String DEFAULT_TEXT_FIELD_CLASS = "o_slider_text_field";
    private static final String DEFAULT_DISABLED_TEXT_FIELD_CLASS = "o_slider_disabled_text_field";

    private static final String DEFAULT_ACTIVE_ELEMENT_ROLLOVER_CLASS = "o_slider_active_element_rollover";


    private String getTooltipStyleClass(FacesContext context, Slider slider) throws IOException {
        String tooltipStyle = (String) slider.getAttributes().get("tooltipStyle");
        String tooltipClass = (String) slider.getAttributes().get("tooltipClass");
        return Styles.getCSSClass(context, slider, tooltipStyle, DEFAULT_TOOLTIP_CLASS, tooltipClass);
    }

    private boolean isIntegerTooltipType(FacesContext context, Slider slider) {
        ValueExpression value = slider.getValueExpression("value");
        if (value != null) {
            Class typeOfValue = value.getType(context.getELContext());
            if (typeOfValue.equals(Double.class) || typeOfValue.equals(Float.class)
                    || typeOfValue.equals(BigDecimal.class)) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private String getTextFieldStyleClass(FacesContext context, Slider slider) throws IOException {
        String textFieldStyle = (String) slider.getAttributes().get("textFieldStyle");
        String textFieldClass = (String) slider.getAttributes().get("textFieldClass");
        String textFieldStyleClass = Styles.getCSSClass(context, slider, textFieldStyle, DEFAULT_TEXT_FIELD_CLASS,
                textFieldClass);

        if (slider.isDisabled()) {
            String disabledTextFieldStyle = (String) slider.getAttributes().get("textFieldDisabledStyle");
            String disabledTextFieldClass = (String) slider.getAttributes().get("textFieldDisabledClass");

            String disabledTextFieldStyleClass = Styles.getCSSClass(context, slider, disabledTextFieldStyle,
                    StyleGroup.disabledStyleGroup(), disabledTextFieldClass, DEFAULT_DISABLED_TEXT_FIELD_CLASS);

            if (Rendering.isNullOrEmpty(disabledTextFieldStyle) && Rendering.isNullOrEmpty(disabledTextFieldClass)) {
                textFieldStyleClass = Styles.mergeClassNames(disabledTextFieldStyleClass, textFieldStyleClass);
            } else {
                textFieldStyleClass = Styles.mergeClassNames(disabledTextFieldClass, Styles.getCSSClass(context,
                        slider, null, StyleGroup.regularStyleGroup(), null, DEFAULT_TEXT_FIELD_CLASS));
            }
        }
        return textFieldStyleClass;
    }

    private String getDefaultClass() {
        return DEFAULT_CLASS + ' ' + DefaultStyles.getTextColorClass();
    }

    private String getSliderFocusedStyleClass(FacesContext context, Slider slider) throws IOException {
        String sliderStyle = slider.getFocusedStyle();
        String sliderClass = slider.getFocusedClass();
        return Styles.getCSSClass(context, slider, sliderStyle, DEFAULT_FOCUSED_CLASS, sliderClass);
    }

    private void setUpConverter(Slider slider) {
        Converter converter = slider.getConverter();
        if (converter == null) {
            NumberConverter numberConverter = new NumberConverter();
            slider.setConverter(numberConverter);
        }
    }

    private String getSliderStyleClass(FacesContext context, Slider slider)
            throws IOException {
        String sliderStyleClass = Styles.getCSSClass(context, slider, slider.getStyle(), StyleGroup.regularStyleGroup(), slider.getStyleClass(),
                getDefaultClass());
        sliderStyleClass = Styles.mergeClassNames(DefaultStyles.CLASS_INITIALLY_INVISIBLE, sliderStyleClass);
        boolean isCenter = slider.getTicksAlignment().equals(TicksAlignment.CENTER);
        if (slider.getOrientation().equals(Orientation.HORIZONTAL)) {
            sliderStyleClass = Styles.mergeClassNames(sliderStyleClass, isCenter ? HORIZONTAL_CENTER_CLASS : HORIZONTAL_CLASS);
        } else {
            sliderStyleClass = Styles.mergeClassNames(sliderStyleClass, isCenter ? VERTICAL_CENTER_CLASS : VERTICAL_CLASS);
        }
        if (slider.isDisabled()) {
            String disabledSliderStyleClass = Styles.getCSSClass(context, slider, null, DEFAULT_DISABLED_CLASS);
            sliderStyleClass = Styles.mergeClassNames(disabledSliderStyleClass, sliderStyleClass);
        }
        return sliderStyleClass;
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        setUpConverter((Slider) component);
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;
        AjaxUtil.prepareComponentForAjax(context, component);
        Components.generateIdIfNotSpecified(component);
        Slider slider = (Slider) component;
        // checkNumericParameters(context, slider);
        String clientId = slider.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        encodeRootElementStart(writer, slider);
        writer.writeAttribute("id", clientId, "id");
        Rendering.writeStandardEvents(writer, slider);
        writeAttribute(writer, "onchanging", slider.getOnchanging());
        writeAttribute(writer, "cellpadding", "0");
        writeAttribute(writer, "cellspacing", "0");
        writeAttribute(writer, "border", "0");

        String sliderStyleClass = getSliderStyleClass(context, slider);
        writeAttribute(writer, "class", sliderStyleClass);

        //   String centerCorrection = "";
        //   if (slider.getTicksAlignment().equals(TicksAlignment.CENTER)) {
        //       centerCorrection = slider.getOrientation().equals(Orientation.HORIZONTAL) ? "height:80px;" : "width:80px;";
        //   }
        writeAttribute(writer, "style", slider.getStyle());//centerCorrection + slider.getStyle());

        if (slider.getOrientation().equals(Orientation.HORIZONTAL)) {
            writer.startElement("tr", slider);
        }
        List<String> imagesUrls = getAllImagesList(context, slider);
        Rendering.renderPreloadImagesScript(context, imagesUrls, false);
        encodeContent(context, slider);
        Rendering.encodeClientActions(context, slider);
    }

    private void checkNumericParameters(FacesContext context, Slider slider) throws IOException {
        String valueString = Rendering.getStringValue(context, slider);
        Number vv = (Number) Rendering.convertFromString(context, slider, valueString.length() == 0 ? "0" : valueString);
        double value = vv.doubleValue();
        double minValue = slider.getMinValue().doubleValue();
        double maxValue = slider.getMaxValue().doubleValue();
        double minorTickSpacing = slider.getMinorTickSpacing().doubleValue();
        double majorTickSpacing = slider.getMajorTickSpacing().doubleValue();

        if (value < minValue || value > maxValue) {
            throw new FacesException("The 'value' attribute of <o:slider> with id " + slider.getClientId(context) +
                    " should be between minValue = " + minValue + " and maxValue = " + maxValue + ", but was " + value);
        }

        if (minValue > maxValue) {
            throw new FacesException("The 'minValue' attribute value of <o:slider> with id " + slider.getClientId(context) +
                    " should be less than 'maxValue' = " + maxValue + " attribute value, but was " + minValue);
        }

        if (minorTickSpacing > majorTickSpacing) {
            throw new FacesException("The 'minorTickSpacing' attribute value of <o:slider> with id " + slider.getClientId(context) +
                    " should be less than 'majorTickSpacing' = " + majorTickSpacing + " attribute value, but was " + minorTickSpacing);
        }

        if (minorTickSpacing > maxValue - minValue) {
            throw new FacesException("The 'minorTickSpacing' attribute value of <o:slider> with id " + slider.getClientId(context) +
                    " should be less than difference of 'maxValue - minValue' = " + (maxValue - minValue) + ", but was " + minorTickSpacing);
        }

        if (minorTickSpacing <= 0) {
            throw new FacesException("The 'minorTickSpacing' attribute value of <o:slider> with id " + slider.getClientId(context) +
                    " should be greater than 0, but was " + minorTickSpacing);
        }

        if (majorTickSpacing <= 0) {
            throw new FacesException("The 'majorTickSpacing' attribute value of <o:slider> with id " + slider.getClientId(context) +
                    " should be greater than 0, but was " + majorTickSpacing);
        }
    }

    private void encodeContent(FacesContext context, Slider slider) throws IOException {
        boolean isHorizontal = slider.getOrientation().equals(Orientation.HORIZONTAL);
        boolean isInverted = slider.getFillDirection().equals(FillDirection.FROM_END);
        if ((isInverted && isHorizontal) || (!isInverted && !isHorizontal)) {
            encodeTextField(context, slider);
        }
        if (slider.isControlButtonsVisible()) {
            encodeButton(context, slider, DEFAULT_LEFT_TOP_BUTTON_SUFFIX, DEFAULT_LT_BUTTON_DISABLED_URL,
                    DEFAULT_LT_BUTTON_URL, DEFAULT_VERTICAL_LT_BUTTON_DISABLED_URL,
                    DEFAULT_VERTICAL_LT_BUTTON_URL, true);
        }
        encodeWorkSpace(context, slider);
        if (slider.isControlButtonsVisible()) {
            encodeButton(context, slider, DEFAULT_RIGHT_BOTTOM_BUTTON_SUFFIX, DEFAULT_RB_BUTTON_DISABLED_URL,
                    DEFAULT_RB_BUTTON_URL, DEFAULT_VERTICAL_RB_BUTTON_DISABLED_URL,
                    DEFAULT_VERTICAL_RB_BUTTON_URL, false);
        }
        if ((!isInverted && isHorizontal) || (isInverted && !isHorizontal)) {
            encodeTextField(context, slider);
        }
        encodeInitScript(context, slider);
    }

    private String getElementPositioning(Slider slider) {
        boolean isHorizontal = slider.getOrientation().equals(Orientation.HORIZONTAL);
        return isHorizontal ? "vertical-align:top;" :
                "text-align:left;vertical-align:middle;";
    }

    private void encodeTextField(FacesContext context, UIComponent component) throws IOException {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        Slider slider = (Slider) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = slider.getClientId(currentInstance);
        String textFieldId = clientId + DEFAULT_TEXT_FIELD_SUFFIX;
        boolean isHorizontal = slider.getOrientation().equals(Orientation.HORIZONTAL);
        boolean isOff = slider.getTextFieldState().equals(TextFieldState.OFF);
        if (isHorizontal) {
            writer.startElement("td", slider);
        } else {
            writer.startElement("tr", slider);
            writer.startElement("td", slider);
        }
        if (!isOff) {
            writer.writeAttribute("style", "overflow:hidden;border:0 none;margin:0;padding:5px;text-align:center;vertical-align:middle;", null);
            writer.startElement("div", slider);
        } else {
            writer.writeAttribute("style", "display:none;", null);
        }
        writer.startElement("input", slider);
        writer.writeAttribute("id", textFieldId, null);
        writer.writeAttribute("name", textFieldId, null);
        switch (slider.getTextFieldState()) {
            case OFF:
                writeAttribute(writer, "type", "hidden");
                break;
            case READ_ONLY:
                writeAttribute(writer, "class", getTextFieldStyleClass(context, slider));
                writeAttribute(writer, "type", "text");
                writer.writeAttribute("style", slider.getTextFieldStyle() + "cursor: default;", null);
                writer.writeAttribute("readonly", "readonly", null);
                break;
            case WRITE_ENABLED:
                writeAttribute(writer, "class", getTextFieldStyleClass(context, slider));
                writer.writeAttribute("style", slider.getTextFieldStyle(), null);
                writeAttribute(writer, "type", "text");
                break;
        }
        writer.writeAttribute("value", "", null);
        writer.writeAttribute("autocomplete", "off", null);
        if (slider.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", null);
        }
        writer.endElement("input");
        if (!isOff) {
            writer.endElement("div");
        }
        writer.endElement("td");
        if (slider.getOrientation().equals(Orientation.VERTICAL)) {
            writer.endElement("tr");
        }
    }

    private void encodeWorkSpace(FacesContext context, UIComponent component) throws IOException {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        Slider slider = (Slider) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = slider.getClientId(currentInstance);
        String workSpaceId = clientId + DEFAULT_WORK_SPACE_SUFFIX;

        if (slider.getOrientation().equals(Orientation.VERTICAL)) {
            writer.startElement("tr", slider);
        }
        writer.startElement("td", slider);

        writer.writeAttribute("style", "margin:auto;", null);

        writer.startElement("div", slider);
        writer.writeAttribute("style", "z-index:50;position:relative;margin:0;padding:0;border:0 none;", null);
        encodeHandle(currentInstance, slider);
        writer.endElement("div");

        writer.startElement("table", slider);
        writer.writeAttribute("id", workSpaceId, null);
        writeAttribute(writer, "cellpadding", "0");
        writeAttribute(writer, "cellspacing", "0");
        writeAttribute(writer, "border", "0");
        writer.startElement("tr", slider);
        if (!slider.getTicksAlignment().equals(TicksAlignment.RIGHT_OR_BOTTOM)) {
            encodeTicks(currentInstance, slider, DEFAULT_LEFT_BOTTOM_TICKS, true);
        }
        if (slider.getOrientation().equals(Orientation.HORIZONTAL)) {
            writer.endElement("tr");
            writer.startElement("tr", slider);
        }
        encodeBar(currentInstance, slider);
        if (slider.getOrientation().equals(Orientation.HORIZONTAL)) {
            writer.endElement("tr");
            writer.startElement("tr", slider);
        }
        if (!slider.getTicksAlignment().equals(TicksAlignment.LEFT_OR_TOP)) {
            encodeTicks(currentInstance, slider, DEFAULT_RIGHT_TOP_TICKS, false);
        }
        writer.endElement("tr");
        writer.endElement("table");
        writer.endElement("td");
        if (slider.getOrientation().equals(Orientation.VERTICAL)) {
            writer.endElement("tr");
        }
    }

    private void encodeHandle(FacesContext context, UIComponent component) throws IOException {
        Slider slider = (Slider) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = slider.getClientId(context);
        String handleId = clientId + DEFAULT_HANDLE_SUFFIX;
        writer.startElement("div", slider);
        writer.writeAttribute("id", handleId, null);
        writer.writeAttribute("style", "position: absolute;z-index: 100;border:0 none;margin:0;padding:0;", null);
        boolean isDisabled = slider.isDisabled();
        String defaultImageUrl = null;

        switch (slider.getOrientation()) {
            case HORIZONTAL: {
                defaultImageUrl = isDisabled ? DEFAULT_HANDLE_DISABLED_URL : DEFAULT_HANDLE_URL;
                break;
            }
            case VERTICAL:
                defaultImageUrl = isDisabled ? DEFAULT_VERTICAL_HANDLE_DISABLED_URL : DEFAULT_VERTICAL_HANDLE_URL;
                break;
        }
        String handleImageUrl = (String) slider.getAttributes().get(isDisabled ?
                "dragHandleDisabledImageUrl" : "dragHandleImageUrl");
        handleImageUrl = Resources.getURL(context, handleImageUrl, defaultImageUrl);
        writer.startElement("img", slider);
        writer.writeAttribute("id", handleId + DEFAULT_ELEMENT_IMAGE_ID, null);
        writer.writeAttribute("style", "display:block;position:absolute;z-index:102;border:0 none;filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + handleImageUrl + "' width:expression(1); height:expression(1); ", null);
        writer.writeAttribute("src", handleImageUrl, null);
        writer.endElement("img");
        writer.endElement("div");
    }

    private boolean getTicksAndLabelVisibility(Slider slider, boolean isTick, boolean isTopLeft) {
        boolean result = false;
        switch (slider.getTicksAlignment()) {
            case CENTER: {
                result = isTick ? (slider.isTicksVisible()) :
                        (slider.isTicksLabelsVisible());
                break;
            }
            case LEFT_OR_TOP: {
                result = isTick ? (isTopLeft && slider.isTicksVisible()) :
                        (isTopLeft && slider.isTicksLabelsVisible());
                break;
            }
            case RIGHT_OR_BOTTOM:
                result = isTick ? (!isTopLeft && slider.isTicksVisible()) :
                        (!isTopLeft && slider.isTicksLabelsVisible());
                break;
        }
        return result;
    }

    private void encodeTicks(FacesContext context, UIComponent component, String id, boolean isTopLeft) throws IOException {
        Slider slider = (Slider) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = slider.getClientId(context);
        String ticksId = clientId + DEFAULT_TICKS_SUFFIX + id;
        String tickId = ticksId + DEFAULT_A_TICK_SUFFIX;
        boolean isHorizontal = slider.getOrientation().equals(Orientation.HORIZONTAL);

        String defaultMajorImageUrl = isHorizontal ? DEFAULT_MAJOR_TICK_URL : DEFAULT_VERTICAL_MAJOR_TICK_URL;
        String defaultMinorImageUrl = isHorizontal ? DEFAULT_MINOR_TICK_URL : DEFAULT_VERTICAL_MINOR_TICK_URL;
        String majorTicksImageUrl = (String) slider.getAttributes().get("majorTickImageUrl");
        String minorTicksImageUrl = (String) slider.getAttributes().get("minorTickImageUrl");
        majorTicksImageUrl = Resources.getURL(context, majorTicksImageUrl, defaultMajorImageUrl);
        minorTicksImageUrl = Resources.getURL(context, minorTicksImageUrl, defaultMinorImageUrl);

        String floatStyle = isHorizontal ? "left;" : "none;";

        writer.startElement("td", slider);
        writer.writeAttribute("id", ticksId, null);
        writer.writeAttribute("style", "overflow:visible;border:0 none;padding:0;margin:0;", null);

        String ticksVisibility = !getTicksAndLabelVisibility(slider, true, isTopLeft) ? "hidden;" : null;
        String ticksLabelsVisibility = !getTicksAndLabelVisibility(slider, false, isTopLeft) ? "hidden;" : null;

        boolean isFromStart = slider.getFillDirection().equals(FillDirection.FROM_START);

        String labelValue;

        double minValue = slider.getMinValue().doubleValue();
        double maxValue = slider.getMaxValue().doubleValue();
        double minTick = slider.getMinorTickSpacing().doubleValue();
        double maxTick = slider.getMajorTickSpacing().doubleValue();
        NumberFormat format = NumberFormat.getInstance(context.getViewRoot().getLocale());
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(2);
        NumberConverter converter = getNumberConverter(context, slider);

        double curTick = minValue;
        if (slider.isTicksLabelsVisible() || slider.isTicksVisible()) {
            for (int counterTicks = 0; curTick <= maxValue; counterTicks++) {
                writer.startElement("div", slider);
                writer.writeAttribute("id", tickId + counterTicks, null);
                writer.writeAttribute("style", "position:relative;z-index:1;border:0 none;padding:0;margin:0; float: " + floatStyle + "font-size:6pt;", null);
                if (isHorizontal) {
                    labelValue = format.format(isFromStart ? (curTick) : (maxValue - curTick + minValue));
                } else {
                    labelValue = format.format(!isFromStart ? (curTick) : (maxValue - curTick + minValue));
                }
                writer.writeAttribute("title", labelValue, null);
                if (slider.isTicksLabelsVisible()) {
                    if (Math.round(curTick / maxTick * 10000) % 10000 == 0) {
                        encodeLabelTick(writer, slider, labelValue, ticksLabelsVisibility, tickId + counterTicks + DEFAULT_ELEMENT_TEXT_ID);
                    }
                }

                if (slider.isTicksVisible()) {
                    encodeTickImage(writer, slider, minorTicksImageUrl, ticksVisibility, tickId + counterTicks + DEFAULT_ELEMENT_IMAGE_ID, isHorizontal);
                }
                curTick = Math.round(100000 * (curTick + minTick)) / 100000.;
                writer.endElement("div");
                if (!isHorizontal) {
                    encodeClearFloatDiv(writer, slider);
                }
            }
        }
        writer.endElement("td");
    }

    private void encodeClearFloatDiv(ResponseWriter writer, Slider slider) throws IOException {
        writer.startElement("div", slider);
        writer.writeAttribute("style", "clear:both;", null);
        writer.endElement("div");
    }

    private void encodeTickImage(ResponseWriter writer, Slider slider, String imageUrl, String visibility,
                                 String id, boolean isHorizontal) throws IOException {
        String display = (isHorizontal) ? "inline;" : "block;";

        writer.startElement("img", slider);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("style", "position:relative;z-index:1;border:0 none;padding:0;margin:0;display:" + display
                + (visibility == null ? "" : "visibility:" + visibility), null);
        writer.writeAttribute("src", imageUrl, null);
        writer.endElement("img");
    }

    private void encodeLabelTick(ResponseWriter writer, UIComponent slider, Object value, String visibility,
                                 String id) throws IOException {
        writer.startElement("div", slider);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("style", "position:absolute;z-index:3;border:0 none;margin:0;padding:0;padding-right:1px;padding-left:1px;white-space: nowrap;cursor:default;" + (visibility == null ? "" : "visibility:" + visibility), null);
        writer.writeText(" " + value + " ", null);
        writer.endElement("div");
    }

    private void encodeBar(FacesContext context, UIComponent component) throws IOException {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        Slider slider = (Slider) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = slider.getClientId(currentInstance);
        String barId = clientId + DEFAULT_BAR_SUFFIX;

        boolean isDisabled = slider.isDisabled();
        boolean isHorizontal = slider.getOrientation().equals(Orientation.HORIZONTAL);
        String defaultImageUrl = isHorizontal ?
                (isDisabled ? DEFAULT_BAR_DISABLED_URL : DEFAULT_BAR_URL) :
                (isDisabled ? DEFAULT_VERTICAL_BAR_DISABLED_URL : DEFAULT_VERTICAL_BAR_URL);
        String defaultStartImageUrl = isHorizontal ?
                (isDisabled ? DEFAULT_BAR_START_DISABLED_URL : DEFAULT_BAR_START_URL) :
                (isDisabled ? DEFAULT_VERTICAL_BAR_START_DISABLED_URL : DEFAULT_VERTICAL_BAR_START_URL);
        String defaultEndImageUrl = isHorizontal ?
                (isDisabled ? DEFAULT_BAR_END_DISABLED_URL : DEFAULT_BAR_END_URL) :
                (isDisabled ? DEFAULT_VERTICAL_BAR_END_DISABLED_URL : DEFAULT_VERTICAL_BAR_END_URL);

        String barImageUrl = (String) slider.getAttributes().get(isDisabled ? "barDisabledImageUrl" : "barImageUrl");
        barImageUrl = Resources.getURL(context, barImageUrl, defaultImageUrl);

        String barStartImageUrl = (String) slider.getAttributes().get(isDisabled ? "barStartDisabledImageUrl" : "barStartImageUrl");
        barStartImageUrl = Resources.getURL(context, barStartImageUrl, defaultStartImageUrl);

        String barEndImageUrl = (String) slider.getAttributes().get(isDisabled ? "barEndDisabledImageUrl" : "barEndImageUrl");
        barEndImageUrl = Resources.getURL(context, barEndImageUrl, defaultEndImageUrl);

        writer.startElement("td", slider);
        writer.startElement("table", slider);
        writeAttribute(writer, "cellpadding", "0");
        writeAttribute(writer, "cellspacing", "0");
        writeAttribute(writer, "border", "0");
        writer.writeAttribute("id", barId, null);
        writer.writeAttribute("style", isHorizontal ? "width:100%;" : "height:100%;", null);
        writer.startElement("tr", slider);

        writer.startElement("td", slider);
        writer.writeAttribute("style", isHorizontal ? "text-align: left;" :
                "vertical-align: top;", null);
        writer.startElement("div", slider);
        writer.startElement("img", slider);
        writer.writeAttribute("id", barId + DEFAULT_ELEMENT_IMAGE_ID, null);
        writer.writeAttribute("style", "display:block;padding:0;margin:0;border:0 none;filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src=" + barStartImageUrl + " width:expression(1); height:expression(1); ", null);
        writer.writeAttribute("src", barStartImageUrl, null);
        writer.endElement("img");
        writer.endElement("div");
        writer.endElement("td");

        if (!isHorizontal) {
            writer.endElement("tr");
            writer.startElement("tr", slider);

        }
        writer.startElement("td", slider);
        writer.writeAttribute("style", isHorizontal ? "width:100%;background: url('" + barImageUrl + "') center repeat-x;" :
                ("height:100%;background: url('" + barImageUrl + "') center repeat-y;"), null);
        writer.endElement("td");

        if (!isHorizontal) {
            writer.endElement("tr");
            writer.startElement("tr", slider);
        }
        writer.startElement("td", slider);
        writer.writeAttribute("style", isHorizontal ? "text-align: right;" :
                "vertical-align: bottom;", null);

        writer.startElement("img", slider);
        writer.writeAttribute("style", "display:block;padding:0;margin:0;border:0 none;filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src=" + barEndImageUrl + " width:expression(1); height:expression(1); ", null);
        writer.writeAttribute("src", barEndImageUrl, null);
        writer.endElement("img");
        writer.endElement("td");
        writer.endElement("tr");

        writer.endElement("table");

        writer.endElement("td");
    }

    private void encodeButton(FacesContext context, UIComponent component, String defaultSuffix,
                              String defaultDisabledUrl, String defaultUrl,
                              String defaultDisabledUrl_vertical, String defaultUrl_vertical, boolean isLT) throws IOException {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        Slider slider = (Slider) component;
        ResponseWriter writer = context.getResponseWriter();
        String buttonId = slider.getClientId(currentInstance) + defaultSuffix;
        String defaultImageUrl = null;
        boolean isHorizontal = slider.getOrientation().equals(Orientation.HORIZONTAL);
        if (isHorizontal) {
            writer.startElement("td", slider);
            writer.writeAttribute("align", "center", null);
        } else {
            writer.startElement("tr", slider);
            writer.startElement("td", slider);
        }

        writer.writeAttribute("style", "border:0 none;margin:0;padding:5px;overflow:hidden;" + getElementPositioning(slider), null);
        writer.startElement("div", slider);
        writer.writeAttribute("id", buttonId, null);
        boolean isDisabled = slider.isDisabled();
        boolean isInverted = slider.getFillDirection().equals(FillDirection.FROM_END);
        String title = "";
        switch (slider.getOrientation()) {
            case HORIZONTAL: {
                defaultImageUrl = isDisabled ? defaultDisabledUrl : defaultUrl;
                title = isLT ? (!isInverted ? "decrease button" : "increase button") :
                        (isInverted ? "decrease button" : "increase button");
                break;
            }
            case VERTICAL:
                defaultImageUrl = isDisabled ? defaultDisabledUrl_vertical : defaultUrl_vertical;
                title = !isLT ? (!isInverted ? "decrease button" : "increase button") :
                        (isInverted ? "decrease button" : "increase button");
                break;
        }
        writer.writeAttribute("title", title, null);
        String buttonImageUrl;
        if (isLT) {
            buttonImageUrl = (String) slider.getAttributes().get(isDisabled ? "leftTopButtonDisabledImageUrl" :
                    "leftTopButtonImageUrl");
        } else {
            buttonImageUrl = (String) slider.getAttributes().get(isDisabled ? "rightBottomButtonDisabledImageUrl" :
                    "rightBottomButtonImageUrl");
        }
        buttonImageUrl = Resources.getURL(context, buttonImageUrl, defaultImageUrl);
        writer.startElement("img", slider);
        writer.writeAttribute("id", buttonId + DEFAULT_ELEMENT_IMAGE_ID, null);
        writer.writeAttribute("style", "padding:0;margin:0;border:0 none;display:block;filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + buttonImageUrl + "' width:expression(1); height:expression(1); ", null);
        writer.writeAttribute("src", buttonImageUrl, null);
        writer.endElement("img");
        writer.endElement("div");
        writer.endElement("td");
        if (slider.getOrientation().equals(Orientation.VERTICAL)) {
            writer.endElement("tr");
        }
    }

    private List<String> getAllImagesList(FacesContext context, Slider slider) throws IOException {
        List<String> imagesUrls = new ArrayList<String>();
        boolean isHorizontal = slider.getOrientation().equals(Orientation.HORIZONTAL);

        String handleImageUrl = (String) slider.getAttributes().get("dragHandleImageUrl");
        handleImageUrl = Resources.getURL(context, handleImageUrl, isHorizontal ? DEFAULT_HANDLE_URL : DEFAULT_VERTICAL_HANDLE_URL);
        imagesUrls.add(handleImageUrl);

        String handleImageUrl_disabled = (String) slider.getAttributes().get("dragHandleDisabledImageUrl");
        handleImageUrl_disabled = Resources.getURL(context, handleImageUrl_disabled, isHorizontal ? DEFAULT_HANDLE_DISABLED_URL : DEFAULT_VERTICAL_HANDLE_DISABLED_URL);
        imagesUrls.add(handleImageUrl_disabled);

        String handleImageUrl_rollover = (String) slider.getAttributes().get("dragHandleRolloverImageUrl");
        handleImageUrl_rollover = Resources.getURL(context, handleImageUrl_rollover, isHorizontal ? DEFAULT_HANDLE_ROLLOVER_URL : DEFAULT_VERTICAL_HANDLE_ROLLOVER_URL);
        imagesUrls.add(handleImageUrl_rollover);

        String leftTopButtonImageUrl = (String) slider.getAttributes().get("leftTopButtonImageUrl");
        leftTopButtonImageUrl = Resources.getURL(context, leftTopButtonImageUrl, isHorizontal ? DEFAULT_LT_BUTTON_URL : DEFAULT_VERTICAL_LT_BUTTON_URL);
        imagesUrls.add(leftTopButtonImageUrl);

        String leftTopButtonImageUrl_disabled = (String) slider.getAttributes().get("leftTopButtonDisabledImageUrl");
        leftTopButtonImageUrl_disabled = Resources.getURL(context, leftTopButtonImageUrl_disabled, isHorizontal ? DEFAULT_LT_BUTTON_DISABLED_URL : DEFAULT_VERTICAL_LT_BUTTON_DISABLED_URL);
        imagesUrls.add(leftTopButtonImageUrl_disabled);

        String leftTopButtonImageUrl_rollover = (String) slider.getAttributes().get("leftTopButtonRolloverImageUrl");
        leftTopButtonImageUrl_rollover = Resources.getURL(context, leftTopButtonImageUrl_rollover, isHorizontal ? DEFAULT_LT_BUTTON_ROLLOVER_URL : DEFAULT_VERTICAL_LT_BUTTON_ROLLOVER_URL);
        imagesUrls.add(leftTopButtonImageUrl_rollover);

        String rightBottomImageUrl = (String) slider.getAttributes().get("rightBottomButtonImageUrl");
        rightBottomImageUrl = Resources.getURL(context, rightBottomImageUrl, isHorizontal ? DEFAULT_RB_BUTTON_URL : DEFAULT_VERTICAL_RB_BUTTON_URL);
        imagesUrls.add(rightBottomImageUrl);

        String rightBottomImageUrl_disabled = (String) slider.getAttributes().get("rightBottomButtonDisabledImageUrl");
        rightBottomImageUrl_disabled = Resources.getURL(context, rightBottomImageUrl_disabled, isHorizontal ? DEFAULT_RB_BUTTON_DISABLED_URL : DEFAULT_VERTICAL_RB_BUTTON_DISABLED_URL);
        imagesUrls.add(rightBottomImageUrl_disabled);

        String rightBottomImageUrl_rollover = (String) slider.getAttributes().get("rightBottomButtonRolloverImageUrl");
        rightBottomImageUrl_rollover = Resources.getURL(context, rightBottomImageUrl_rollover, isHorizontal ? DEFAULT_RB_BUTTON_ROLLOVER_URL : DEFAULT_VERTICAL_RB_BUTTON_ROLLOVER_URL);
        imagesUrls.add(rightBottomImageUrl_rollover);

        String majorTicksImageUrl = (String) slider.getAttributes().get("majorTickImageUrl");
        majorTicksImageUrl = Resources.getURL(context, majorTicksImageUrl, isHorizontal ? DEFAULT_MAJOR_TICK_URL : DEFAULT_VERTICAL_MAJOR_TICK_URL);
        imagesUrls.add(majorTicksImageUrl);

        String minorTicksImageUrl = (String) slider.getAttributes().get("minorTickImageUrl");
        minorTicksImageUrl = Resources.getURL(context, minorTicksImageUrl, isHorizontal ? DEFAULT_MINOR_TICK_URL : DEFAULT_VERTICAL_MINOR_TICK_URL);
        imagesUrls.add(minorTicksImageUrl);

        String barImageUrl = (String) slider.getAttributes().get("barImageUrl");
        barImageUrl = Resources.getURL(context, barImageUrl, isHorizontal ? DEFAULT_BAR_URL : DEFAULT_VERTICAL_BAR_URL);
        imagesUrls.add(barImageUrl);

        String barImageUrl_disabled = (String) slider.getAttributes().get("barDisabledImageUrl");
        barImageUrl_disabled = Resources.getURL(context, barImageUrl_disabled, isHorizontal ? DEFAULT_BAR_DISABLED_URL : DEFAULT_VERTICAL_BAR_DISABLED_URL);
        imagesUrls.add(barImageUrl_disabled);

        String barStartImageUrl = (String) slider.getAttributes().get("barStartImageUrl");
        barStartImageUrl = Resources.getURL(context, barStartImageUrl, isHorizontal ? DEFAULT_BAR_START_URL : DEFAULT_VERTICAL_BAR_START_URL);
        imagesUrls.add(barStartImageUrl);

        String barStartImageUrl_disabled = (String) slider.getAttributes().get("barStartDisabledImageUrl");
        barStartImageUrl_disabled = Resources.getURL(context, barStartImageUrl_disabled, isHorizontal ? DEFAULT_BAR_START_DISABLED_URL : DEFAULT_VERTICAL_BAR_START_DISABLED_URL);
        imagesUrls.add(barStartImageUrl_disabled);

        String barEndImageUrl = (String) slider.getAttributes().get("barStartImageUrl");
        barEndImageUrl = Resources.getURL(context, barEndImageUrl, isHorizontal ? DEFAULT_BAR_END_URL : DEFAULT_VERTICAL_BAR_END_URL);
        imagesUrls.add(barEndImageUrl);

        String barEndImageUrl_disabled = (String) slider.getAttributes().get("barStartDisabledImageUrl");
        barEndImageUrl_disabled = Resources.getURL(context, barEndImageUrl_disabled, isHorizontal ? DEFAULT_BAR_END_DISABLED_URL : DEFAULT_VERTICAL_BAR_END_DISABLED_URL);
        imagesUrls.add(barEndImageUrl_disabled);

        return imagesUrls;
    }

    protected void encodeInitScript(FacesContext context, UIComponent component) throws IOException {
        Slider slider = (Slider) component;
        boolean isHorizontal = slider.getOrientation().equals(Orientation.HORIZONTAL);
        String rolloverAEStyle = (String) slider.getAttributes().get("activeElementRolloverStyle");
        String rolloverAEClass = (String) slider.getAttributes().get("activeElementRolloverClass");
        String activeElementRolloverStyleClass = Styles.getCSSClass(context, slider, rolloverAEStyle, StyleGroup.rolloverStyleGroup(), rolloverAEClass, DEFAULT_ACTIVE_ELEMENT_ROLLOVER_CLASS);

        String handleImageUrl = (String) slider.getAttributes().get("dragHandleImageUrl");
        handleImageUrl = Resources.getURL(context, handleImageUrl, isHorizontal ? DEFAULT_HANDLE_URL : DEFAULT_VERTICAL_HANDLE_URL);

        String handleImageUrl_rollover = (String) slider.getAttributes().get("dragHandleRolloverImageUrl");
        handleImageUrl_rollover = Resources.getURL(context, handleImageUrl_rollover, isHorizontal ? DEFAULT_HANDLE_ROLLOVER_URL : DEFAULT_VERTICAL_HANDLE_ROLLOVER_URL);

        String leftTopButtonImageUrl = (String) slider.getAttributes().get("leftTopButtonImageUrl");
        leftTopButtonImageUrl = Resources.getURL(context, leftTopButtonImageUrl, isHorizontal ? DEFAULT_LT_BUTTON_URL : DEFAULT_VERTICAL_LT_BUTTON_URL);

        String leftTopButtonImageUrl_rollover = (String) slider.getAttributes().get("leftTopButtonRolloverImageUrl");
        leftTopButtonImageUrl_rollover = Resources.getURL(context, leftTopButtonImageUrl_rollover, isHorizontal ? DEFAULT_LT_BUTTON_ROLLOVER_URL : DEFAULT_VERTICAL_LT_BUTTON_ROLLOVER_URL);

        String rightBottomImageUrl = (String) slider.getAttributes().get("rightBottomButtonImageUrl");
        rightBottomImageUrl = Resources.getURL(context, rightBottomImageUrl, isHorizontal ? DEFAULT_RB_BUTTON_URL : DEFAULT_VERTICAL_RB_BUTTON_URL);

        String rightBottomImageUrl_rollover = (String) slider.getAttributes().get("rightBottomButtonRolloverImageUrl");
        rightBottomImageUrl_rollover = Resources.getURL(context, rightBottomImageUrl_rollover, isHorizontal ? DEFAULT_RB_BUTTON_ROLLOVER_URL : DEFAULT_VERTICAL_RB_BUTTON_ROLLOVER_URL);


        JSONObject options;

        try {
            options = createFormatOptions(context, slider);
        } catch (JSONException e) {
            throw new FacesException(e);
        }

        Script initScript = new ScriptBuilder().initScript(context, slider, "O$.Slider._init",
                slider.getValue(), slider.getMinValue(), slider.getMaxValue(),
                slider.getMinorTickSpacing(), slider.getMajorTickSpacing(),
                slider.getOrientation(), slider.getFillDirection(),
                slider.isDisabled(), slider.isTooltipEnabled(),
                slider.isBarCanChangeValue(), slider.isBarVisible(),
                slider.getTextFieldState(), slider.isSnapToTicks(),
                slider.getOnchange(), slider.getOnchanging(),
                getTextFieldStyleClass(context, slider),
                getTooltipStyleClass(context, slider),
                isIntegerTooltipType(context, slider),
                getSliderStyleClass(context, slider),
                getSliderFocusedStyleClass(context, slider),
                activeElementRolloverStyleClass,
                slider.getBarAutoRepeatClickDelay(),
                options,
                slider.getTransitionPeriod(),
                handleImageUrl, handleImageUrl_rollover,
                leftTopButtonImageUrl, leftTopButtonImageUrl_rollover,
                rightBottomImageUrl, rightBottomImageUrl_rollover
        );

        Rendering.renderInitScript(context, initScript,
                Resources.ajaxUtilJsURL(context),
                Resources.internalURL(context, "input/slider.js"),
                Resources.internalURL(context, "util/dojo.js")
        );

        Styles.renderStyleClasses(context, slider);
    }

    private NumberConverter getNumberConverter(FacesContext context, Slider slider)
            throws IOException {
        Converter converter = slider.getConverter();
        if (!(converter instanceof NumberConverter)) {
            throw new FacesException("Unsupported converter class of <o:slider> with id " + slider.getClientId(context) +
                    " : " + converter.getClass().getName());
        }
        return (NumberConverter) converter;
    }

    private JSONObject createFormatOptions(FacesContext context, Slider slider)
            throws JSONException, IOException {
        NumberConverter numberConverter = getNumberConverter(context, slider);
        JSONObject options = new JSONObject();
        options.put("currency", numberConverter.getCurrencyCode());
        options.put("locale", numberConverter.getLocale().toString());
        options.put("pattern", numberConverter.getPattern());
        options.put("round", numberConverter.getMaxFractionDigits());
        options.put("symbol", numberConverter.getCurrencySymbol());
        options.put("type", numberConverter.getType());

        JSONObject customs = Resources.getNumberFormatSettings(numberConverter.getLocale());
        options.put("customs", customs);
        return options;
    }

    public Object getConvertedValue(FacesContext context, UIComponent component, Object value)
            throws ConverterException {
        return Rendering.convertFromString(context, (Slider) component, (String) value);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Slider slider = (Slider) component;

        if (slider.getOrientation().equals(Orientation.HORIZONTAL)) {
            writer.endElement("tr");
        }

        encodeRootElementEnd(writer);
        writer.flush();
    }

    private void encodeRootElementStart(ResponseWriter writer, Slider slider) throws IOException {
        writer.startElement("table", slider);
    }

    private void encodeRootElementEnd(ResponseWriter writer) throws IOException {
        writer.endElement("table");
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        Rendering.encodeClientActions(context, component);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        Slider slider = (Slider) component;
        String fieldId = slider.getClientId(context) + DEFAULT_TEXT_FIELD_SUFFIX;
        String value = requestMap.get(fieldId);
        if (null != value) {
            slider.setValue(value);
        }
    }

}

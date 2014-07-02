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

import org.openfaces.component.calendar.Calendar;
import org.openfaces.component.calendar.DateRanges;
import org.openfaces.component.input.DateChooser;
import org.openfaces.component.input.DateChooserPopup;
import org.openfaces.component.input.DropDownComponent;
import org.openfaces.renderkit.calendar.CalendarRenderer;
import org.openfaces.renderkit.validation.ValidatorUtil;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;
import org.openfaces.util.DataUtil;
import org.openfaces.util.InitScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Pavel Kaplin
 */
public class DateChooserRenderer extends DropDownComponentRenderer {

    @Override
    protected void registerJS(FacesContext context, UIComponent component) throws IOException {
        super.registerJS(context, component);
        DateChooser dateChooser = (DateChooser) component;
        Locale locale = dateChooser.getLocale();
        if (locale == null) {
            locale = context.getExternalContext().getRequestLocale();
        }
        Rendering.registerDateTimeFormatObject(locale);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Rendering.decodeBehaviors(context, component);

        Map requestMap = context.getExternalContext().getRequestParameterMap();
        String clientId = component.getClientId(context) + FIELD_SUFFIX;
        String value = (String) requestMap.get(clientId);

        String state = (String) requestMap.get(clientId + PROMPT_VISIBLE_SUFFIX);

        if ((state != null && state.equals("false")) && value != null) {
            ((EditableValueHolder) component).setSubmittedValue(value);
        }
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) {
        Date convertedValue = (Date) Rendering.convertFromString(context, (DateChooser) component, (String) submittedValue);
        DateChooser dataChooser = (DateChooser) component;
        boolean keepTime = dataChooser.isKeepTime();
        if (!keepTime) {
            return convertedValue;
        }
        Date currentValue = (Date) dataChooser.getValue();
        TimeZone timeZone = dataChooser.getTimeZone();
        DataUtil.copyDateKeepingTime(convertedValue, currentValue, timeZone);
        return currentValue;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        setUpConverter((DateChooser) component);
        Components.generateIdIfNotSpecified(component);
        super.encodeBegin(context, component);
    }

    @Override
    protected void encodeRootElementStart(ResponseWriter writer, DropDownComponent dropDownComponent) throws IOException {
        super.encodeRootElementStart(writer, dropDownComponent);
        writeAttribute(writer, "style", "visibility: hidden;");
    }

    private void setUpConverter(DateChooser dateChooser) {
        String converterSpecifiedExplicitlyAttr = "_converterSpecifiedExplicitly";
        Boolean converterSpecifiedExplicitly = (Boolean) dateChooser.getAttributes().get(converterSpecifiedExplicitlyAttr);
        if (converterSpecifiedExplicitly == null) {
            Converter explicitlySpecifiedConverter = dateChooser.getConverter();
            converterSpecifiedExplicitly = explicitlySpecifiedConverter != null;
            if (converterSpecifiedExplicitly && !(explicitlySpecifiedConverter instanceof DateTimeConverter))
                throw new IllegalStateException("Improper type converter has been specified. DateTimeConverter is expected. The actual type of the specified converter is: " + explicitlySpecifiedConverter.getClass().getName());
            dateChooser.getAttributes().put(converterSpecifiedExplicitlyAttr, converterSpecifiedExplicitly);
        }
        if (converterSpecifiedExplicitly) return;

        DateTimeConverter converter = new DateTimeConverter();
        converter.setPattern(dateChooser.getPattern());
        converter.setDateStyle(dateChooser.getDateFormat());
        converter.setLocale(dateChooser.getLocale());

        TimeZone timeZone = (dateChooser.getTimeZone() != null)
                ? dateChooser.getTimeZone()
                : TimeZone.getDefault();
        converter.setTimeZone(timeZone);
        converter.clearInitialState();

        dateChooser.setConverter(converter);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        DateChooser dateChooser = (DateChooser) component;
        DateChooserPopup popup = Components.getChildWithClass(dateChooser, DateChooserPopup.class, POPUP_SUFFIX);

        List<UIComponent> children = dateChooser.getChildren();
        Calendar c = popup.getCalendar();

        c.getAttributes().put(CalendarRenderer.HIDE_DEFAULT_FOCUS_KEY, Boolean.TRUE);

        if (dateChooser.isValid()) {
            c.setValue(dateChooser.getValue());
        }
        c.setStyle(dateChooser.getCalendarStyle());
        c.setTimeZone(dateChooser.getTimeZone());
        c.setDayStyle(dateChooser.getDayStyle());
        c.setRolloverDayStyle(dateChooser.getRolloverDayStyle());
        c.setInactiveMonthDayStyle(dateChooser.getInactiveMonthDayStyle());
        c.setRolloverInactiveMonthDayStyle(dateChooser.getRolloverInactiveMonthDayStyle());
        c.setSelectedDayStyle(dateChooser.getSelectedDayStyle());
        c.setRolloverSelectedDayStyle(dateChooser.getRolloverSelectedDayStyle());
        c.setTodayStyle(dateChooser.getTodayStyle());
        c.setRolloverTodayStyle(dateChooser.getRolloverTodayStyle());
        c.setDisabledDayStyle(dateChooser.getDisabledDayStyle());
        c.setRolloverDisabledDayStyle(dateChooser.getRolloverDisabledDayStyle());
        c.setWeekendDayStyle(dateChooser.getWeekendDayStyle());
        c.setRolloverWeekendDayStyle(dateChooser.getRolloverWeekendDayStyle());
        c.setDaysHeaderStyle(dateChooser.getDaysHeaderStyle());
        c.setHeaderStyle(dateChooser.getHeaderStyle());
        c.setFooterStyle(dateChooser.getFooterStyle());

        c.setStyleClass(dateChooser.getCalendarClass());
        c.setDayClass(dateChooser.getDayClass());
        c.setRolloverDayClass(dateChooser.getRolloverDayClass());
        c.setInactiveMonthDayClass(dateChooser.getInactiveMonthDayClass());
        c.setRolloverInactiveMonthDayClass(dateChooser.getRolloverInactiveMonthDayClass());
        c.setSelectedDayClass(dateChooser.getSelectedDayClass());
        c.setRolloverSelectedDayClass(dateChooser.getRolloverSelectedDayClass());
        c.setTodayClass(dateChooser.getTodayClass());
        c.setRolloverTodayClass(dateChooser.getRolloverTodayClass());
        c.setDisabledDayClass(dateChooser.getDisabledDayClass());
        c.setRolloverDisabledDayClass(dateChooser.getRolloverDisabledDayClass());
        c.setWeekendDayClass(dateChooser.getWeekendDayClass());
        c.setRolloverWeekendDayClass(dateChooser.getRolloverWeekendDayClass());
        c.setDaysHeaderClass(dateChooser.getDaysHeaderClass());
        c.setHeaderClass(dateChooser.getHeaderClass());
        c.setFooterClass(dateChooser.getFooterClass());

        c.setFirstDayOfWeek(dateChooser.getFirstDayOfWeek());
        c.setShowFooter(dateChooser.isShowFooter());

        c.setFocusedStyle(dateChooser.getCalendarStyle());
        c.setFocusedClass(dateChooser.getCalendarClass());
        c.setFocusable(true);

        Locale locale = dateChooser.getLocale();
        if (locale == null) {
            locale = context.getExternalContext().getRequestLocale();
        }
        c.setLocale(locale);

        c.setTodayText(dateChooser.getTodayText());
        c.setNoneText(dateChooser.getNoneText());

        for (UIComponent child : children) {
            if (child instanceof DateRanges) {
                c.getChildren().add(child);
            }
        }

        popup.encodeAll(context);
        Rendering.encodeClientActions(context, component);
    }

    protected InitScript renderInitScript(FacesContext context, DropDownComponent dropDown) throws IOException {
        DateChooser dc = (DateChooser) dropDown;

        String pattern = null;
        Converter c = dc.getConverter();
        if (c != null && c instanceof DateTimeConverter) {
            DateTimeConverter dtc = (DateTimeConverter) c;
            pattern = dtc.getPattern();
        }

        // Related to JSFC-2042. Send date adjusted to GMT on client. It'll be used to set correct date to calendar
        String formatDate = null;
        Object value = dc.getValue();
        if (value != null) {
            Date date = (Date) value;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            TimeZone timeZone = (dc.getTimeZone() != null)
                    ? dc.getTimeZone()
                    : TimeZone.getDefault();
            dateFormat.setTimeZone(timeZone);
            formatDate = dateFormat.format(date);
        }

        ScriptBuilder sb = new ScriptBuilder().initScript(context, dc, "O$.DateChooser._init",
                pattern,
                formatDate,
                dc.getLocale(),
                Rendering.getChangeHandlerScript(dc));

        return new InitScript(sb, new String[]{
                Resources.utilJsURL(context),
                Resources.jsonJsURL(context),
                getDropdownJsURL(context),
                Resources.internalURL(context, "validation/requestHelper.js"),
                Resources.internalURL(context, "input/dateChooser.js"),
                ValidatorUtil.getValidatorUtilJsUrl(context)
        });
    }

    @Override
    protected void writeFieldAttributes(ResponseWriter writer, DropDownComponent fieldComponent) throws IOException {
        super.writeFieldAttributes(writer, fieldComponent);
        DateChooser dateChooser = ((DateChooser) fieldComponent);
        if (!dateChooser.isTypingAllowed())
            writeAttribute(writer, "readonly", String.valueOf(!dateChooser.isTypingAllowed()));
    }


}

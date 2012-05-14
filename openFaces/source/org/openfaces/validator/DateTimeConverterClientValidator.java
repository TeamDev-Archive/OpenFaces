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
package org.openfaces.validator;

import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.Resources;
import org.openfaces.util.MessageUtil;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class DateTimeConverterClientValidator extends AbstractClientValidator implements AjaxSupportedConverter {
    private static final String CONVERSION_MESSAGE_ID = "javax.faces.convert.DateTimeConverter.CONVERSION";

    private static final String TYPE_DATE = "date";
    private static final String TYPE_TIME = "time";
    private static final String TYPE_BOTH = "both";
    private static final String STYLE_DEFAULT = "default";
    private static final String STYLE_MEDIUM = "medium";
    private static final String STYLE_SHORT = "short";
    private static final String STYLE_LONG = "long";
    private static final String STYLE_FULL = "full";

    private DateTimeConverter dateTimeConverter;


    public String getJsValidatorName() {
        return "O$._DateTimeConverterValidator";
    }

    public DateTimeConverterClientValidator() {
        addJavascriptLibrary(new ValidationJavascriptLibrary("dateTimeConverterValidator.js"));
        addJavascriptLibrary(new ValidationJavascriptLibrary("requestHelper.js"));
        addJavascriptLibrary(new ValidationJavascriptLibrary("/" + Resources.JSON_JS_PATH));
    }

    public void setDateTimeConverter(DateTimeConverter dateTimeConverter) {
        this.dateTimeConverter = dateTimeConverter;
    }

    public DateTimeConverter getDateTimeConverter() {
        return dateTimeConverter;
    }

    protected Object[] getJsValidatorParametersAsString(FacesContext context, UIComponent component) {
        Object[] args = {"specified value", component.getId()};
        FacesMessage message = MessageUtil.getMessage(context, FacesMessage.SEVERITY_ERROR,
                new String[]{CONVERSION_MESSAGE_ID, UIInput.CONVERSION_MESSAGE_ID}, args);

//    TimeZone timeZone = dateTimeConverter.getTimeZone();
//    String timeZoneId = "";
//    if (timeZone != null) {
//      timeZoneId = Rendering.escapeStringForJS(dateTimeConverter.getTimeZone().getID());
//    }

        Locale locale = dateTimeConverter.getLocale();
        String slocale = "";
        if (locale != null) {
            slocale = dateTimeConverter.getLocale().toString();
        }

        DateFormat dateFormat = getDateFormat(dateTimeConverter);
        String pattern = dateFormat instanceof SimpleDateFormat ? ((SimpleDateFormat) dateFormat).toPattern() : "";
        pattern = pattern.replaceAll("E{1,3}", "E");
        pattern = pattern.replaceAll("E{4,}", "EE");
        pattern = pattern.replaceAll("a{2,}", "a");
        pattern = pattern.replaceAll("M{4,}", "MMM");
        return new String[]{
                message.getSummary(),
                message.getDetail(),
                pattern,
                slocale
//                            + "new O$.DateTimeConverter(" +
//                            "'" + slocale + "'," +
//                            "'" + Rendering.escapeStringForJS(dateTimeConverter.getPattern()) + "'," +
//                            "'" + Rendering.escapeStringForJS(dateTimeConverter.getDayStyle()) + "'," +
//                            "'" + Rendering.escapeStringForJS(dateTimeConverter.getType()) + "'," +
//                            "'" + timeZoneId + "'," +
//                            "'" + Rendering.escapeStringForJS(dateTimeConverter.getTimeStyle()) + "'," +
//                            "'" + Rendering.escapeStringForJS(this.getClass().getName()) + "'" +
//                            ")"
        };
    }

    public Converter getConverter(FacesContext context, JSONObject jConverter) {
        try {
            String locale = jConverter.getString("locale");
            String pattern = jConverter.getString("pattern");
            String timeStyle = jConverter.getString("timeStyle");
            String timeZone = jConverter.getString("timeZone");
            String type = jConverter.getString("type");
            String dayStyle = jConverter.getString("dayStyle");

            String validatorId = jConverter.getString("validatorId");

            Converter result = context.getApplication().createConverter(validatorId);
            if (result instanceof DateTimeConverter) {
                //apply properties
                DateTimeConverter nc = (DateTimeConverter) result;


                if (locale != null && locale.length() > 0)
                    nc.setLocale(new Locale(locale));


                if (pattern != null && pattern.length() > 0)
                    nc.setPattern(pattern);

                if (dayStyle != null && dayStyle.length() > 0)
                    nc.setDateStyle(dayStyle);

                if (type != null && type.length() > 0)
                    nc.setType(type);

                if (timeStyle != null && timeStyle.length() > 0)
                    nc.setTimeStyle(timeStyle);

                if (timeZone != null && timeZone.length() > 0)
                    nc.setTimeZone(TimeZone.getTimeZone(timeZone));
            }

            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private DateFormat getDateFormat(DateTimeConverter converter) {
        String type = converter.getType();
        DateFormat format;
        if (converter.getPattern() != null) {
            try {
                format = new SimpleDateFormat(converter.getPattern(), converter.getLocale());
            }
            catch (IllegalArgumentException iae) {
                throw new ConverterException("Invalid pattern", iae);
            }
        } else if (type.equals(TYPE_DATE)) {
            format = DateFormat.getDateInstance(calcStyle(converter.getDateStyle()), converter.getLocale());
        } else if (type.equals(TYPE_TIME)) {
            format = DateFormat.getTimeInstance(calcStyle(converter.getTimeStyle()), converter.getLocale());
        } else if (type.equals(TYPE_BOTH)) {
            format = DateFormat.getDateTimeInstance(calcStyle(converter.getDateStyle()),
                    calcStyle(converter.getTimeStyle()),
                    converter.getLocale());
        } else {
            throw new ConverterException("invalid type '" + converter.getType() + "'");
        }

        // format cannot be lenient (JSR-127)
        format.setLenient(false);
        return format;
    }

    private int calcStyle(String name) {
        if (name.equals(STYLE_DEFAULT)) {
            return DateFormat.DEFAULT;
        }
        if (name.equals(STYLE_MEDIUM)) {
            return DateFormat.MEDIUM;
        }
        if (name.equals(STYLE_SHORT)) {
            return DateFormat.SHORT;
        }
        if (name.equals(STYLE_LONG)) {
            return DateFormat.LONG;
        }
        if (name.equals(STYLE_FULL)) {
            return DateFormat.FULL;
        }

        throw new ConverterException("invalid style '" + name + "'");
    }
}


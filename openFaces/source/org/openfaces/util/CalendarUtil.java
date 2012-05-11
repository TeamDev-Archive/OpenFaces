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
package org.openfaces.util;

import org.openfaces.component.input.DateChooser;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

/**
 * @author Andrew Palval
 */
public class CalendarUtil {

    public static Locale getLocaleFromString(String localString) {
        Locale locale = null;
        StringTokenizer localeTokens = new StringTokenizer(localString, "_");
        int paramsCount = localeTokens.countTokens();
        switch (paramsCount) {
            case 1:
                locale = new Locale(localeTokens.nextToken());
                break;
            case 2:
                locale = new Locale(localeTokens.nextToken(), localeTokens.nextToken());
                break;
            case 3:
                locale = new Locale(localeTokens.nextToken(), localeTokens.nextToken(), localeTokens.nextToken());
        }
        return locale;
    }

    public static Locale getBoundPropertyValueAsLocale(UIComponent component, String property, Locale fieldValue) {
        return getBoundPropertyValueAsLocale(component, null, property, fieldValue);
    }

    public static Locale getBoundPropertyValueAsLocale(UIComponent component, UIComponent parentComponent,
                                                       String property, Locale fieldValue) {
        Locale defaultLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return getBoundPropertyValueAsLocale(component, parentComponent, property, defaultLocale, fieldValue);
    }

    public static Locale getBoundPropertyValueAsLocale(UIComponent component,
                                                       String property, Locale defaultLocale, Locale fieldValue) {
        return getBoundPropertyValueAsLocale(component, null, property, defaultLocale, fieldValue);
    }

    public static Locale getBoundPropertyValueAsLocale(UIComponent component, UIComponent parentComponent,
                                                       String property, Locale defaultLocale, Locale fieldValue) {
        Object propertyValue = ValueBindings.get(component, parentComponent,
                property, fieldValue, defaultLocale, Object.class);
        if (propertyValue == null) {
            return FacesContext.getCurrentInstance().getViewRoot().getLocale();
        } else if (propertyValue instanceof Locale) {
            return (Locale) propertyValue;
        } else {
            return CalendarUtil.getLocaleFromString(propertyValue.toString());
        }
    }

    public static String getDatePattern(UIComponent component, String dateFormat, String pattern, Locale locale) {
        SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance(
                convertToDateFormatStyle(dateFormat), locale);
        return ValueBindings.get(component, "pattern", pattern, sdf.toPattern());
    }

    public static SimpleDateFormat getSimpleDateFormat(String dateFormat, String defaultDateFormat, String pattern,
                                                       String defaultPattern, Locale locale, TimeZone timeZone) {
        SimpleDateFormat sdf;
        if (pattern != null) {
            sdf = new SimpleDateFormat(pattern, locale);
        } else if (dateFormat != null) {
            sdf = (SimpleDateFormat) DateFormat.getDateInstance(convertToDateFormatStyle(dateFormat), locale);
        } else if (defaultPattern != null) {
            sdf = new SimpleDateFormat(defaultPattern, locale);
        } else {
            sdf = (SimpleDateFormat) DateFormat.getDateInstance(convertToDateFormatStyle(defaultDateFormat), locale);
        }
        sdf.setTimeZone(timeZone);
        return sdf;
    }

    private static int convertToDateFormatStyle(String dateFormat) {
        if (dateFormat.equals(DateChooser.FORMAT_MEDIUM)) {
            return DateFormat.MEDIUM;
        } else if (dateFormat.equals(DateChooser.FORMAT_SHORT)) {
            return DateFormat.SHORT;
        } else if (dateFormat.equals(DateChooser.FORMAT_LONG)) {
            return DateFormat.LONG;
        } else if (dateFormat.equals(DateChooser.FORMAT_FULL)) {
            return DateFormat.FULL;
        } else {
            throw new IllegalArgumentException("Wrong date format: " + dateFormat);
        }
    }

}

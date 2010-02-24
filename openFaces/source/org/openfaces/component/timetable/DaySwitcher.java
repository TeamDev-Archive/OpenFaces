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
package org.openfaces.component.timetable;

import org.openfaces.component.OUIComponentBase;
import org.openfaces.util.CalendarUtil;
import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Natalia Zolochevska
 */
public class DaySwitcher extends OUIComponentBase {

    public static final String COMPONENT_TYPE = "org.openfaces.DaySwitcher";
    public static final String COMPONENT_FAMILY = "org.openfaces.DaySwitcher";

    public static final String DEFAULT_DATE_FORMAT = "long";
    public static final String DEFAULT_SUP_PATTERN = "EEEE";

    private String _for;
    private DayTable dayTable;

    private String dateFormat;
    private String pattern;
    private String upperDateFormat;
    private String upperPattern;
    private Locale locale;
    private TimeZone timeZone;

    private Boolean enabled;

    private String previousButtonStyle;
    private String previousButtonClass;
    private String previousButtonRolloverStyle;
    private String previousButtonRolloverClass;
    private String previousButtonPressedStyle;
    private String previousButtonPressedClass;
    private String previousButtonImageUrl;

    private String nextButtonStyle;
    private String nextButtonClass;
    private String nextButtonRolloverStyle;
    private String nextButtonRolloverClass;
    private String nextButtonPressedStyle;
    private String nextButtonPressedClass;
    private String nextButtonImageUrl;

    private String textStyle;
    private String textClass;
    private String textRolloverStyle;
    private String textRolloverClass;

    private String upperTextStyle;
    private String upperTextClass;
    private String upperTextRolloverStyle;
    private String upperTextRolloverClass;

    public String getFor() {
        return _for;
    }

    public void setFor(String _for) {
        this._for = _for;
    }

    public DayTable getDayTable() {
        if (dayTable == null) {
            String _for = getFor();
            if (_for != null) {
                UIComponent referredComponent = Components.referenceIdToComponent(this, _for);
                if (referredComponent != null && !(referredComponent instanceof DayTable))
                    throw new FacesException("DaySwitcher's \"for\" attribute must refer to a DayTable component.");
                dayTable = (DayTable) referredComponent;
            } else {
                UIComponent component = getParent();
                while (component != null && !(component instanceof DayTable))
                    component = component.getParent();
                dayTable = (DayTable) component;
            }
        }
        return dayTable;
    }

    public String getDateFormat() {
        return ValueBindings.get(this, "dateFormat", dateFormat);
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getPattern() {
        return ValueBindings.get(this, "pattern", pattern);
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getUpperDateFormat() {
        return ValueBindings.get(this, "upperDateFormat", upperDateFormat);
    }

    public void setUpperDateFormat(String upperDateFormat) {
        this.upperDateFormat = upperDateFormat;
    }

    public String getUpperPattern() {
        return ValueBindings.get(this, "upperPattern", upperPattern);
    }

    public void setUpperPattern(String upperPattern) {
        this.upperPattern = upperPattern;
    }

    public Locale getLocale() {
        DayTable dayTable = getDayTable();
        Locale defaultLocale = dayTable != null ? dayTable.getLocale() : null;
        return CalendarUtil.getBoundPropertyValueAsLocale(this, "locale", defaultLocale, locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public TimeZone getTimeZone() {
        TimeZone timeZone = getDayTable().getTimeZone();
        return ValueBindings.get(this, "timeZone", this.timeZone, timeZone, TimeZone.class);
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isEnabled() {
        return ValueBindings.get(this, "enabled", enabled, true);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPreviousButtonStyle() {
        return ValueBindings.get(this, "previousButtonStyle", previousButtonStyle);
    }

    public void setPreviousButtonStyle(String previousButtonStyle) {
        this.previousButtonStyle = previousButtonStyle;
    }

    public String getPreviousButtonClass() {
        return ValueBindings.get(this, "previousButtonClass", previousButtonClass);
    }

    public void setPreviousButtonClass(String previousButtonClass) {
        this.previousButtonClass = previousButtonClass;
    }

    public String getPreviousButtonRolloverStyle() {
        return ValueBindings.get(this, "previousButtonRolloverStyle", previousButtonRolloverStyle);
    }

    public void setPreviousButtonRolloverStyle(String previousButtonRolloverStyle) {
        this.previousButtonRolloverStyle = previousButtonRolloverStyle;
    }

    public String getPreviousButtonRolloverClass() {
        return ValueBindings.get(this, "previousButtonRolloverClass", previousButtonRolloverClass);
    }

    public void setPreviousButtonRolloverClass(String previousButtonRolloverClass) {
        this.previousButtonRolloverClass = previousButtonRolloverClass;
    }

    public String getPreviousButtonPressedStyle() {
        return ValueBindings.get(this, "previousButtonPressedStyle", previousButtonPressedStyle);
    }

    public void setPreviousButtonPressedStyle(String previousButtonPressedStyle) {
        this.previousButtonPressedStyle = previousButtonPressedStyle;
    }

    public String getPreviousButtonPressedClass() {
        return ValueBindings.get(this, "previousButtonPressedClass", previousButtonPressedClass);
    }

    public void setPreviousButtonPressedClass(String previousButtonPressedClass) {
        this.previousButtonPressedClass = previousButtonPressedClass;
    }

    public String getPreviousButtonImageUrl() {
        return ValueBindings.get(this, "previousButtonImageUrl", previousButtonImageUrl);
    }

    public void setPreviousButtonImageUrl(String previousButtonImageUrl) {
        this.previousButtonImageUrl = previousButtonImageUrl;
    }

    public String getNextButtonStyle() {
        return ValueBindings.get(this, "nextButtonStyle", nextButtonStyle);
    }

    public void setNextButtonStyle(String nextButtonStyle) {
        this.nextButtonStyle = nextButtonStyle;
    }

    public String getNextButtonClass() {
        return ValueBindings.get(this, "nextButtonClass", nextButtonClass);
    }

    public void setNextButtonClass(String nextButtonClass) {
        this.nextButtonClass = nextButtonClass;
    }

    public String getNextButtonRolloverStyle() {
        return ValueBindings.get(this, "nextButtonRolloverStyle", nextButtonRolloverStyle);
    }

    public void setNextButtonRolloverStyle(String nextButtonRolloverStyle) {
        this.nextButtonRolloverStyle = nextButtonRolloverStyle;
    }

    public String getNextButtonRolloverClass() {
        return ValueBindings.get(this, "nextButtonRolloverClass", nextButtonRolloverClass);
    }

    public void setNextButtonRolloverClass(String nextButtonRolloverClass) {
        this.nextButtonRolloverClass = nextButtonRolloverClass;
    }

    public String getNextButtonPressedStyle() {
        return ValueBindings.get(this, "nextButtonPressedStyle", nextButtonPressedStyle);
    }

    public void setNextButtonPressedStyle(String nextButtonPressedStyle) {
        this.nextButtonPressedStyle = nextButtonPressedStyle;
    }

    public String getNextButtonPressedClass() {
        return ValueBindings.get(this, "nextButtonPressedClass", nextButtonPressedClass);
    }

    public void setNextButtonPressedClass(String nextButtonPressedClass) {
        this.nextButtonPressedClass = nextButtonPressedClass;
    }

    public String getNextButtonImageUrl() {
        return ValueBindings.get(this, "nextButtonImageUrl", nextButtonImageUrl);
    }

    public void setNextButtonImageUrl(String nextButtonImageUrl) {
        this.nextButtonImageUrl = nextButtonImageUrl;
    }

    public String getTextStyle() {
        return ValueBindings.get(this, "textStyle", textStyle);
    }

    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
    }

    public String getTextClass() {
        return ValueBindings.get(this, "textClass", textClass);
    }

    public void setTextClass(String textClass) {
        this.textClass = textClass;
    }

    public String getTextRolloverStyle() {
        return ValueBindings.get(this, "textRolloverStyle", textRolloverStyle);
    }

    public void setTextRolloverStyle(String textRolloverStyle) {
        this.textRolloverStyle = textRolloverStyle;
    }

    public String getTextRolloverClass() {
        return ValueBindings.get(this, "textRolloverClass", textRolloverClass);
    }

    public void setTextRolloverClass(String textRolloverClass) {
        this.textRolloverClass = textRolloverClass;
    }

    public String getUpperTextStyle() {
        return ValueBindings.get(this, "upperTextStyle", upperTextStyle);
    }

    public void setUpperTextStyle(String upperTextStyle) {
        this.upperTextStyle = upperTextStyle;
    }

    public String getUpperTextClass() {
        return ValueBindings.get(this, "upperTextClass", upperTextClass);
    }

    public void setUpperTextClass(String upperTextClass) {
        this.upperTextClass = upperTextClass;
    }

    public String getUpperTextRolloverStyle() {
        return ValueBindings.get(this, "upperTextRolloverStyle", upperTextRolloverStyle);
    }

    public void setUpperTextRolloverStyle(String upperTextRolloverStyle) {
        this.upperTextRolloverStyle = upperTextRolloverStyle;
    }

    public String getUpperTextRolloverClass() {
        return ValueBindings.get(this, "upperTextRolloverClass", upperTextRolloverClass);
    }

    public void setUpperTextRolloverClass(String upperTextRolloverClass) {
        this.upperTextRolloverClass = upperTextRolloverClass;
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                _for,
                dateFormat,
                pattern,
                upperDateFormat,
                upperPattern,
                locale,
                timeZone,

                enabled,

                previousButtonStyle,
                previousButtonClass,
                previousButtonRolloverStyle,
                previousButtonRolloverClass,
                previousButtonPressedStyle,
                previousButtonPressedClass,
                previousButtonImageUrl,

                nextButtonStyle,
                nextButtonClass,
                nextButtonRolloverStyle,
                nextButtonRolloverClass,
                nextButtonPressedStyle,
                nextButtonPressedClass,
                nextButtonImageUrl,

                textStyle,
                textClass,
                textRolloverStyle,
                textRolloverClass,

                upperTextStyle,
                upperTextClass,
                upperTextRolloverStyle,
                upperTextRolloverClass

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        _for = (String) state[i++];

        dateFormat = (String) state[i++];
        pattern = (String) state[i++];
        upperDateFormat = (String) state[i++];
        upperPattern = (String) state[i++];
        locale = (Locale) state[i++];
        timeZone = (TimeZone) state[i++];

        enabled = (Boolean) state[i++];

        previousButtonStyle = (String) state[i++];
        previousButtonClass = (String) state[i++];
        previousButtonRolloverStyle = (String) state[i++];
        previousButtonRolloverClass = (String) state[i++];
        previousButtonPressedStyle = (String) state[i++];
        previousButtonPressedClass = (String) state[i++];
        previousButtonImageUrl = (String) state[i++];

        nextButtonStyle = (String) state[i++];
        nextButtonClass = (String) state[i++];
        nextButtonRolloverStyle = (String) state[i++];
        nextButtonRolloverClass = (String) state[i++];
        nextButtonPressedStyle = (String) state[i++];
        nextButtonPressedClass = (String) state[i++];
        nextButtonImageUrl = (String) state[i++];

        textStyle = (String) state[i++];
        textClass = (String) state[i++];
        textRolloverStyle = (String) state[i++];
        textRolloverClass = (String) state[i++];

        upperTextStyle = (String) state[i++];
        upperTextClass = (String) state[i++];
        upperTextRolloverStyle = (String) state[i++];
        upperTextRolloverClass = (String) state[i++];
    }


}
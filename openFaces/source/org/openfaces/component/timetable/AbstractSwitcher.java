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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Roman Gorodischer
 */
public abstract class AbstractSwitcher<E extends TimetableView> extends OUIComponentBase {
    private String _for;
    private E timetableView;

    private String dateFormat;
    private String pattern;

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

    private Class genericParameterType;
    private final Object lock = new Object();

    public String getFor() {
        return _for;
    }

    public void setFor(String _for) {
        this._for = _for;
    }

    @SuppressWarnings("unchecked") 
    public E getTimetableView() {
        if (timetableView == null) {
            String _for = getFor();
            if (_for != null) {
                UIComponent referredComponent = Components.referenceIdToComponent(this, _for);
                if (referredComponent != null && !(referredComponent.getClass().equals(getGenericParameterType())))
                    throw new FacesException(getTimetableNotFoundMsg());
                timetableView = (E) referredComponent;
            } else {
                UIComponent component = getParent();
                while (component != null && !(component.getClass().equals(getGenericParameterType())))
                    component = component.getParent();
                timetableView = (E) component;
            }
        }
        return timetableView;
    }

    protected abstract String getTimetableNotFoundMsg();


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

    public Locale getLocale() {
        E timetableView = getTimetableView();
        Locale defaultLocale = timetableView != null ? timetableView.getLocale() : null;
        return CalendarUtil.getBoundPropertyValueAsLocale(this, "locale", defaultLocale, locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public TimeZone getTimeZone() {
        TimeZone timeZone = getTimetableView().getTimeZone();
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

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                _for,
                dateFormat,
                pattern,
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

    }

    private Class getGenericParameterType() {
        if (genericParameterType == null) {
            synchronized (lock) {
                if (genericParameterType == null) {
                    genericParameterType = findGenericParameterType();
                }
            }
        }
        return genericParameterType;
    }

    private Class findGenericParameterType() {
        Type superclassType = getClass().getGenericSuperclass();
        if (ParameterizedType.class.isAssignableFrom(superclassType.getClass())) {
            ParameterizedType parameterizedType = (ParameterizedType) superclassType;
            if (AbstractSwitcher.class == parameterizedType.getRawType()) {
                return (Class) parameterizedType.getActualTypeArguments()[0];
            }
        }
        throw new IllegalArgumentException("Class should directly extend AbstractSwitcher");
    }

}

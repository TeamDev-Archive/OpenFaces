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

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.
 *
 * @author Dmitry Pikhulya
 */
public class ValueBindings {
    private ValueBindings() {
    }

    public static String get(UIComponent component, String property, String fieldValue) {
        return get(component, property, fieldValue, (String) null);
    }

    public static String get(UIComponent component, UIComponent parentComponent, String property, String fieldValue) {
        return get(component, parentComponent, property, fieldValue, null, false, String.class);
    }


    public static String get(UIComponent component, String property, String fieldValue, String defaultValue) {
        return get(component, property, fieldValue, defaultValue, String.class);
    }

    public static String get(UIComponent component, UIComponent parentComponent, String property, String fieldValue, String defaultValue) {
        return get(component, parentComponent, property, fieldValue, defaultValue, false, String.class);
    }


    public static boolean get(UIComponent component, String property, Boolean fieldValue, boolean defaultValue) {
        return get(component, property, fieldValue, defaultValue, Boolean.class);
    }

    public static boolean get(UIComponent component, UIComponent parentComponent, String property,
                              Boolean fieldValue, boolean defaultValue) {
        return get(component, parentComponent, property, fieldValue, defaultValue, false, Boolean.class);
    }

    public static int get(UIComponent component, String property, Integer fieldValue, int defaultValue) {
        return get(component, property, fieldValue, defaultValue, Integer.class);
    }

    public static int get(UIComponent component, UIComponent parentComponent, String property, Integer fieldValue, int defaultValue) {
        return get(component, parentComponent, property, fieldValue, defaultValue, false, Integer.class);
    }

    public static double get(UIComponent component, String property, Double fieldValue, double defaultValue) {
        return get(component, property, fieldValue, defaultValue, Double.class);
    }

    public static double get(UIComponent component, UIComponent parentComponent, String property, Double fieldValue, double defaultValue) {
        return get(component, parentComponent, property, fieldValue, defaultValue, false, Double.class);
    }

    public static <T> T get(UIComponent component, String property, T fieldValue, Class<T> cls) {
        return get(component, property, fieldValue, null, cls);
    }

    public static <T> T get(UIComponent component, UIComponent parentComponent, String property,
                            T fieldValue, Class<T> cls) {
        return get(component, parentComponent, property, fieldValue, null, false, cls);
    }


    public static <T> T get(UIComponent component, String property, T fieldValue, T defaultValue, Class<T> cls) {
        return get(component, property, fieldValue, defaultValue, false, cls);
    }

    public static <T> T get(UIComponent component, UIComponent parentComponent, String property, T fieldValue, T defaultValue, Class<T> cls) {
        return get(component, property, fieldValue, defaultValue, false, cls);
    }

    public static <T> T get(UIComponent component, UIComponent parentComponent, String property, T fieldValue,
                            T defaultValue, boolean allowNullBinding, Class<T> cls) {
        T ownValue = get(component, property, fieldValue, parentComponent != null ? null : defaultValue, allowNullBinding, cls);
        if (parentComponent == null || ownValue != null)
            return ownValue;
        return (T) parentComponent.getAttributes().get(property);
    }

    public static <T> T get(UIComponent component, String property, T fieldValue,
                            T defaultValue, boolean allowNullBinding, Class<T> cls) {
        if (fieldValue != null)
            return fieldValue;
        ValueExpression ve = component.getValueExpression(property);
        if (ve == null)
            return defaultValue;
        Object value = ve.getValue(FacesContext.getCurrentInstance().getELContext());
        if (value == null) {
            return allowNullBinding ? null : defaultValue;
        }

        checkValueClass(value, cls, component, ve);
        return (T) value;
    }

    public static void checkValueClass(Object value, Class expectedClass, UIComponent component, ValueExpression expression) {
        Class valueClass = value.getClass();
        if (!expectedClass.isAssignableFrom(valueClass))
            throw new ClassCastException("The value received through expression \"" + expression.getExpressionString() +
                    "\" of component with id=\"" + component.getId() + "\" should be of type \"" + expectedClass.getName() +
                    "\" but was of type: \"" + valueClass.getName() + "\"");
    }

    public static boolean set(UIComponent component, String propertyName, int value) {
        ValueExpression ve = component.getValueExpression(propertyName);
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        if (ve == null || ve.isReadOnly(elContext))
            return false;

        ve.setValue(elContext, value);
        return true;
    }

    public static boolean set(UIComponent component, String propertyName, boolean value) {
        ValueExpression ve = component.getValueExpression(propertyName);
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        if (ve == null || ve.isReadOnly(elContext))
            return false;

        ve.setValue(elContext, value);
        return true;
    }

    public static boolean set(UIComponent component, String propertyName, Object value) {
        ValueExpression ve = component.getValueExpression(propertyName);
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        if (ve == null || ve.isReadOnly(elContext))
            return false;

        ve.setValue(elContext, value);
        return true;
    }

    public static void setFromList(UIComponent component, String propertyName, List propertyValueAsList) {
        ValueExpression ve = component.getValueExpression(propertyName);
        if (ve == null)
            return;
        FacesContext context = FacesContext.getCurrentInstance();

        setValueExpressionFromList(context, ve, propertyName, propertyValueAsList);
    }

    private static void setValueExpressionFromList(FacesContext context, ValueExpression valueExpression,
                                                   String propertyName, List propertyValueAsList) {
        Object adaptedValue;
        ELContext elContext = context.getELContext();
        if (propertyValueAsList == null)
            adaptedValue = null;
        else {
            Class expressionType = valueExpression.getType(elContext);
            if (expressionType == null || List.class.isAssignableFrom(expressionType))
                adaptedValue = propertyValueAsList;
            else if (Set.class.isAssignableFrom(expressionType))
                adaptedValue = new HashSet(propertyValueAsList);
            else if (expressionType.isArray()) {
                Class componentType = expressionType.getComponentType();
                int elementCount = propertyValueAsList.size();
                adaptedValue = Array.newInstance(componentType, elementCount);
                for (int i = 0; i < elementCount; i++) {
                    try {
                        Array.set(adaptedValue, i, propertyValueAsList.get(i));
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else
                throw new RuntimeException("Unsupported expression type: " + expressionType + "; property: " + propertyName + "; only lists, sets or arrays are supported.");
        }

        valueExpression.setValue(elContext, adaptedValue);
    }

    public static void writeValueExpression(ObjectOutput out, ValueExpression expression) throws IOException {
        out.writeObject(expression);
    }

    public static ValueExpression readValueExpression(ObjectInput in) throws IOException, ClassNotFoundException {
        return (ValueExpression) in.readObject();
    }

    public static ValueExpression createValueExpression(FacesContext context, String expressionString) {
        ELContext elContext = context.getELContext();
        Application application = context.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ValueExpression result = expressionFactory.createValueExpression(
                elContext,
                expressionString,
                Object.class);
        return result;
    }
}

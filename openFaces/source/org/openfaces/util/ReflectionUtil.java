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
package org.openfaces.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Eugene Goncharov
 */
public class ReflectionUtil {

    public static Object invokeMethod(String className,
                                      String methodName,
                                      Class[] methodParameterClasses,
                                      Object[] methodParameterObjects,
                                      Object invokeOnObject) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Object result = null;
        try {
            Class reflectionClass = Class.forName(className);
            result = invokeMethod(reflectionClass, methodName, methodParameterClasses, methodParameterObjects, invokeOnObject);
        } catch (ClassNotFoundException e) {
            externalContext.log("Class " + className + " was not found.");
        }
        return result;
    }

    public static Object invokeMethod(Class clazz,
                                      String methodName,
                                      Class[] methodParameterClasses,
                                      Object[] methodParameterObjects,
                                      Object invokeOnObject) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Object result = null;
        String className = clazz.getName();
        try {
            Method reflectionMethod = clazz.getDeclaredMethod(methodName, methodParameterClasses);
            reflectionMethod.setAccessible(true);
            result = reflectionMethod.invoke(invokeOnObject, methodParameterObjects);
        } catch (NoSuchMethodException e) {
            externalContext.log("Method " + methodName + "was not found in class " + className + " .");
        } catch (IllegalAccessException e) {
            externalContext.log("IllegalAccessException during access to " + methodName + " in class " + className + " .");
        } catch (InvocationTargetException e) {
            externalContext.log("InvocationTargetException during access to " + methodName + " in class " + className + " .");
        }
        return result;
    }

    public static Object invokeMethod(Class clazz,
                                      String methodName,
                                      Object invokeOnObject) {
        return invokeMethod(clazz, methodName, new Class[]{}, new Object[]{}, invokeOnObject);
    }

    public static Object getStaticFieldValue(Class ajaxContextClass, String fieldName) {
        if (ajaxContextClass == null) {
            return null;
        }
        try {
            Field field = ajaxContextClass.getField(fieldName);
            if (field != null) {
                return field.get(null);
            }
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}

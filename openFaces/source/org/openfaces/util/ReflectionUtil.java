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
package org.openfaces.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

    public static Object readProperty(Object obj, String propertyName) {
        int propertySeparatorIndex = propertyName.indexOf(".");
        if (propertySeparatorIndex != -1) {
            String immediatePropertyName = propertyName.substring(0, propertySeparatorIndex);
            Object immediatePropertyValue = readProperty(obj, immediatePropertyName);
            String subpropertyName = propertyName.substring(propertySeparatorIndex + 1);
            return readProperty(immediatePropertyValue, subpropertyName);
        }
        /*if (obj instanceof Map) {
            return ((Map) obj).get(propertyName);
        }
        if (obj.getClass().isArray() || obj instanceof List) {
            int index;
                index = Integer.valueOf(propertyName);
            if (obj.getClass().isArray()){
                return ((Object[])obj)[index];
            }else{
                return ((List)obj).get(index);
            }
        }*/
        PropertyDescriptor propertyDescriptor = null;
        try {
            PropertyDescriptor[] propertyDescriptors =
                    Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor pd : propertyDescriptors) {
                if (propertyName.equals(pd.getName())) {
                    propertyDescriptor = pd;
                    break;
                }
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        if (propertyDescriptor == null)
            throw new IllegalArgumentException("There's no property named '" + propertyName + "' in class " + obj.getClass().getName());
        try {
            return propertyDescriptor.getReadMethod().invoke(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class definePropertyType(Class clazz, String propertyName) {
        int propertySeparatorIndex = propertyName.indexOf(".");
        if (propertySeparatorIndex != -1) {
            String immediatePropertyName = propertyName.substring(0, propertySeparatorIndex);
            Class immediatePropertyType = definePropertyType(clazz, immediatePropertyName);
            String subpropertyName = propertyName.substring(propertySeparatorIndex + 1);
            return definePropertyType(immediatePropertyType, subpropertyName);
        }
        PropertyDescriptor propertyDescriptor = null;
        try {
            PropertyDescriptor[] propertyDescriptors =
                    Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            for (PropertyDescriptor pd : propertyDescriptors) {
                if (propertyName.equals(pd.getName())) {
                    propertyDescriptor = pd;
                    break;
                }
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        if (propertyDescriptor == null)
            throw new IllegalArgumentException("There's no property named '" + propertyName + "' in class " + clazz.getName());
        return propertyDescriptor.getReadMethod().getReturnType();

    }

    public final static boolean isNumberType(final Class<?> type) {
        return type == Long.TYPE || type == Double.TYPE ||
                type == Byte.TYPE || type == Short.TYPE ||
                type == Integer.TYPE || type == Float.TYPE ||
                Number.class.isAssignableFrom(type);
    }

    public static Class getGenericParameterClass(Class actualClass) {
        return getGenericParameterClass(actualClass, 0);
    }

    public static Class getGenericParameterClass(Class actualClass, int parameterIndex) {
        Type type = ((ParameterizedType) actualClass.getGenericSuperclass()).getActualTypeArguments()[parameterIndex];
        return type instanceof Class ? (Class) type : null;
    }

}

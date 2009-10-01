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
package org.openfaces.component.filter.criterion;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Dmitry Pikhulya
 */
public class NamedPropertyLocator extends PropertyLocator {
    private String name;

    public NamedPropertyLocator(String name) {
        this.name = name;
    }

    public Object getPropertyValue(Object obj) {
        return readProperty(obj, name);
    }

    private Object readProperty(Object obj, String propertyName) {
        int propertySeparatorIndex = propertyName.indexOf(".");
        if (propertySeparatorIndex != -1) {
            String immediatePropertyName = propertyName.substring(0, propertySeparatorIndex);
            Object immediatePropertyValue = readProperty(obj, immediatePropertyName);
            String subpropertyName = propertyName.substring(propertySeparatorIndex + 1);
            return readProperty(immediatePropertyValue, subpropertyName);
        }
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

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamedPropertyLocator that = (NamedPropertyLocator) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}

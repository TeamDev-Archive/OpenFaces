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

package org.openfaces.component.filter;

import org.openfaces.util.ReflectionUtil;

import java.io.Serializable;

import static java.lang.Character.*;


public class SimplePropertyLocatorFactory implements PropertyLocatorFactory {

    public PropertyLocator create(Object expression) {
        return new SimplePropertyLocator(expression);
    }


    public static class SimplePropertyLocator extends PropertyLocator implements Serializable {
        public SimplePropertyLocator(Object expression) {
            super(expression);
        }


        public Object getPropertyValue(Object obj) {
            return ReflectionUtil.readProperty(obj, convertPropertyNameToValidForm((String) expression));
        }

        private String convertPropertyNameToValidForm(String propertyName) {
            if (propertyName.contains(" ")) {
                propertyName = propertyName.replaceAll(" ", "");
            }
            char firstChar = propertyName.charAt(0);
            if (isUpperCase(firstChar)) {
                propertyName = propertyName.replace(firstChar, toLowerCase(firstChar));
            }
            return propertyName;
        }
    }
}

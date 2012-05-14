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
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import java.beans.FeatureDescriptor;
import java.util.Iterator;

/**
 * @author Roman Porotnikov
 */
public class NullTypeELResolver extends ELResolver {

    private static ThreadLocal<Boolean> _active = new ThreadLocal<Boolean>();

    public static void setActive(boolean active) {
        _active.set(active);
    }

    public static boolean isActive() {
        Boolean active = _active.get();
        return active != null && active;
    }

    public Class<?> getType(ELContext context, Object base, Object property)
            throws NullPointerException, PropertyNotFoundException, ELException {

        if (isActive()) {
            if (base != null && property != null) {
                context.setPropertyResolved(true);
            }
        }

        return null;
    }

    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return null;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    public Object getValue(ELContext context, Object base, Object property)
            throws NullPointerException, PropertyNotFoundException, ELException {
        return null;
    }

    public boolean isReadOnly(ELContext context, Object base, Object property)
            throws NullPointerException, PropertyNotFoundException, ELException {
        return false;
    }

    public void setValue(ELContext context, Object base, Object property, Object value)
            throws NullPointerException, PropertyNotFoundException, PropertyNotWritableException, ELException {
    }

}

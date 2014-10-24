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

package org.openfaces.component.table;

import java.util.Comparator;

/**
 * @author Dmitry Pikhulya
 */
public class AnyIntegerType extends OrdinalType {

    @Override
    public boolean isApplicableForClass(Class valueClass) {
        return Byte.class.equals(valueClass) || byte.class.equals(valueClass) ||
               Short.class.equals(valueClass) || short.class.equals(valueClass) ||
               Integer.class.equals(valueClass) || int.class.equals(valueClass) ||
               Long.class.equals(valueClass) || long.class.equals(valueClass);
    }

    @Override
    public Object add(Object value1, Object value2) {
        return ((Number) value1).longValue() + ((Number) value2).longValue();
    }

    @Override
    public Object divide(Object value, double by) {
        return ((Number) value).doubleValue() / by;
    }

}

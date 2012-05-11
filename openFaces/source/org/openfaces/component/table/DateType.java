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

import java.util.Date;

/**
 * @author Dmitry Pikhulya
 */
public class DateType extends OrdinalType {
    @Override
    public boolean isApplicableForClass(Class valueClass) {
        return Date.class.isAssignableFrom(valueClass);
    }

    @Override
    public Object add(Object value1, Object value2) {
        return new Date(((Date) value1).getTime() + ((Date) value2).getTime());
    }

    @Override
    public Object divide(Object value, double by) {
        long dateMillis = ((Date) value).getTime();
        return new Date(Math.round(dateMillis / by));
    }
}

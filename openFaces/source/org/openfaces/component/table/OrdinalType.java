/*
 * OpenFaces - JSF Component Library 2.0
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

/**
 * @author Dmitry Pikhulya
 */
public abstract class OrdinalType {
    public abstract boolean isApplicableForClass(Class valueClass);

    /**
     * The sum of two values. This method does not explicitly checks the types of the values and expects both values to
     * be of a proper type applicable to this OrdinalType instance (as defined by the isApplicableForClass method)
     */
    public abstract Object add(Object value1, Object value2);

    /**
     * @return the zero value for the type described by this instance, if applicable, or null otherwise
     */
    public abstract Object getZero();
}

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

import javax.faces.convert.Converter;
import java.util.TimeZone;

public abstract class FilterPropertyBase implements FilterProperty {

    public abstract String getTitle();

    public abstract FilterType getType();

    public String getName() {
        return getTitle();
    }

    public Object getDataProvider() {
        return null;
    }

    public Converter getConverter() {
        return null;
    }

    public Number getMaxValue() {
        return null;
    }

    public Number getMinValue() {
        return null;
    }

    public Number getStep() {
        return null;
    }

    public String getPattern() {
        return null;
    }

    public TimeZone getTimeZone() {
        return null;
    }

    public boolean isCaseSensitive() {
        return false;
    }

    public PropertyLocator getPropertyLocator() {
        return PropertyLocator.getDefaultInstance(getName());
    }

}

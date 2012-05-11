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
import java.io.Serializable;
import java.util.TimeZone;

public interface FilterProperty extends Serializable {   

    String getName();

    FilterType getType();

    String getTitle();

    Object getDataProvider();

    Converter getConverter();

    Number getMaxValue();

    Number getMinValue();

    Number getStep();

    String getPattern();

    TimeZone getTimeZone();

    boolean isCaseSensitive();

    PropertyLocator getPropertyLocator();
}

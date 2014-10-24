/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.filter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: max.yurin
 */
public final class FilterPropertyContextHolder {
    private static ThreadLocal<LinkedHashMap<String, FilterProperty>> FILTER_PROPERTIES =
            new ThreadLocal<LinkedHashMap<String, FilterProperty>>();
    private static ThreadLocal<List<FilterProperty>> PROPERTIES = new ThreadLocal<List<FilterProperty>>();

    private FilterPropertyContextHolder(){}

    public static void setProperties(List<FilterProperty> properties){
        if(PROPERTIES.get() == null){
            PROPERTIES.set(new ArrayList<FilterProperty>());
        }
        if(properties != null && properties.get(0) != null && properties.get(0).getTitle() != null) {
            PROPERTIES.set(properties);
        }
    }

    public static List<FilterProperty> getProperties() {
        return PROPERTIES.get();
    }
}

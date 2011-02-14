/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.daytable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Alexey Chystoprudov
 */
class DateToSuffixMap extends HashMap<Date, String> {

    private static final long serialVersionUID = -592799956585340603L;

    public String get(Object key) {
        if (!(key instanceof Date)) {
            return null;
        }
        Date date = (Date) key;
        String dayOfMonth = (new SimpleDateFormat("dd")).format(date);
        String result;
        if (dayOfMonth.endsWith("1") && !dayOfMonth.equals("11")) {
            result = "st";
        } else if (dayOfMonth.endsWith("2") && !dayOfMonth.equals("12")) {
            result = "nd";
        } else if (dayOfMonth.endsWith("3") && !dayOfMonth.equals("13")) {
            result = "rd";
        } else {
            result = "th";
        }
        return result;
    }
}
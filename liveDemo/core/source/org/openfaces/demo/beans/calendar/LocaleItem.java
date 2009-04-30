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
package org.openfaces.demo.beans.calendar;

import java.io.Serializable;
import java.util.Locale;

public class LocaleItem implements Serializable {
    private String localeName;
    private Locale locale;
    private String todayText;
    private String noneText;
    private String wkString;

    public LocaleItem(String localeName, Locale locale, String todayText, String noneText, String wkString) {
        this.localeName = localeName;
        this.locale = locale;
        this.todayText = todayText;
        this.noneText = noneText;
        this.wkString = wkString;
    }

    public String getLocaleName() {
        return localeName;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getTodayText() {
        return todayText;
    }

    public String getNoneText() {
        return noneText;
    }

    public String getWkString() {
        return wkString;
    }

    public String toString() {
        return getLocaleName();
    }
}

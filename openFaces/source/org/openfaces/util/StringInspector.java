/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.util;

/**
 * This class allows optimized case-insensitive substring search. The usual toUpperCase-based search fails on texts
 * containing symbols those are converted to upper case as more than one character. For example the unicode symbol
 * \u00df is converted to upper case as two letters: SS. This class provides correct results for such strings as well.
 */
public class StringInspector {
    private String str;
    private String uppercaseStr;

    public StringInspector(String str) {
        this.str = str;
        boolean smartSearchRequired = str.toUpperCase().length() != str.length();
        uppercaseStr = !smartSearchRequired ? str.toUpperCase() : null;
    }

    private StringInspector(String str, String uppercaseStr) {
        this.str = str;
        this.uppercaseStr = uppercaseStr;
    }

    public int indexOfIgnoreCase(String str) {
        return indexOfIgnoreCase(str, 0);
    }

    public int indexOfIgnoreCase(String str, int fromIndex) {
        if (uppercaseStr != null)
            return uppercaseStr.indexOf(str.toUpperCase(), fromIndex);

        int subStringLength = str.length();
        for (int i = fromIndex, lastCharToCheck = this.str.length() - subStringLength; i <= lastCharToCheck; i++) {
            if (this.str.regionMatches(true, i, str, 0, subStringLength))
                return i;
        }

        return -1;
    }

    public StringInspector substring(int beginIndex) {
        return new StringInspector(str.substring(beginIndex), uppercaseStr != null ? uppercaseStr.substring(beginIndex) : null);
    }

    public StringInspector substring(int beginIndex, int endIndex) {
        return new StringInspector(str.substring(beginIndex, endIndex), uppercaseStr != null ? uppercaseStr.substring(beginIndex, endIndex) : null);
    }

    @Override
    public String toString() {
        return str;
    }

    public StringInspector concatenate(StringInspector stringInspector) {
        return new StringInspector(str + stringInspector.str,
                uppercaseStr == null || stringInspector.uppercaseStr == null
                        ? null
                        : uppercaseStr + stringInspector.uppercaseStr);
    }
}

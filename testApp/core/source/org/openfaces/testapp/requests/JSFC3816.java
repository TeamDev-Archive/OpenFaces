/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.requests;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ilya Musihin
 */
public class JSFC3816 {

    private String value;

    private List<String> suggests = new ArrayList<String>();

    public JSFC3816() {
        suggests.add("a'a");
        suggests.add("a%a");
        suggests.add("a&a");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getSuggests() {
        return suggests;
    }

    public void setSuggests(List<String> suggests) {
        this.suggests = suggests;
    }
}

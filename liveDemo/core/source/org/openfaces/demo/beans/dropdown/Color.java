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

package org.openfaces.demo.beans.dropdown;

import java.io.Serializable;

public class Color implements Serializable {
    private String name;
    private int r;
    private int g;
    private int b;
    private String hex;

    public Color(String name, int r, int g, int b, String hex) {
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.hex = hex;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public String getHex() {
        return hex;
    }

    public String getName() {
        return name;
    }

}

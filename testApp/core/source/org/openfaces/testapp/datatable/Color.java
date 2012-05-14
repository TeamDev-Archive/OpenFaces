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

package org.openfaces.testapp.datatable;

public class Color {

    private int id;
    private String colorName;
    private int r;
    private int g;
    private int b;
    private String hex;

    public Color(int id, String colorName, int r, int g, int b, String hex) {
        this.id = id;
        this.colorName = colorName;
        this.r = r;
        this.g = g;
        this.b = b;
        this.hex = hex;
    }

    public Color(String colorName, int r, int g, int b, String hex) {
        id = -1;
        this.colorName = colorName;
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
        return colorName;
    }

    public int getId() {
        return id;
    }
}

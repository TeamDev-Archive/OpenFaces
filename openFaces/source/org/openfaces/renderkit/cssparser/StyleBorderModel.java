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
package org.openfaces.renderkit.cssparser;

import java.awt.*;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class StyleBorderModel {
    private Color color = null;
    private Integer width = 1;
    private String style = "solid"; //todo(question): are all border types supported? don't we need an enumeration here? No: supported are the following: none, solid, dashed, dotted

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public StyleBorderModel() {
    }

    public StyleBorderModel(Color color, Integer width, String style) {
        this.color = color;
        this.width = width;
        this.style = style;
    }

    public boolean isNone() {
        return color == null || style.equalsIgnoreCase("none");
    }

}

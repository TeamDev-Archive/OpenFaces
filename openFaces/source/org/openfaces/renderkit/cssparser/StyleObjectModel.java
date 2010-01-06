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
package org.openfaces.renderkit.cssparser;

import java.awt.*;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class StyleObjectModel {
    private Color color;
    private Color background;
    private StyleFontModel font;
    private StyleBorderModel border;
    private Integer width;
    private Integer height;

    /**
     * Margins for four sides: top, right, bottom, left
     */
    private Integer[] margins = new Integer[4];

    public int getMargin(int sideNo) {
        if (margins[sideNo] == null)
            return 0;

        return margins[sideNo];
    }

    public void setMargings(Integer[] margins) {
        System.arraycopy(margins, 0, this.margins, 0, margins.length);
    }

    public void setMargin(int sideNo, int d) {
        margins[sideNo] = d;
    }

    public boolean isNullMargin(int sideNo) {
        return margins[sideNo] == null;
    }


    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }


    public StyleBorderModel getBorder() {
        return border;
    }

    public void setBorder(StyleBorderModel border) {
        this.border = border;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public StyleFontModel getFont() {
        return font;
    }

    public void setFont(StyleFontModel model) {
        font = model;
    }


}

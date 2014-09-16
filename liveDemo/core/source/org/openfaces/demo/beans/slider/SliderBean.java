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

package org.openfaces.demo.beans.slider;

/**
 * @author: Andrey Loboda
 * @date : 26.09.11
 */
public class SliderBean {
    private Integer width;
    private Integer colorBlue;
    private Integer colorRed;
    private Integer colorGreen;
    private Integer fontSize;
    private Integer opacity;


    public SliderBean() {
        width = 80;
        colorBlue = 0;
        colorRed = 0;
        colorGreen = 0;
        fontSize = 14;
        opacity=100;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getColorBlue() {
        return colorBlue;
    }

    public void setColorBlue(Integer colorBlue) {
        this.colorBlue = colorBlue;
    }

    public Integer getColorRed() {
        return colorRed;
    }

    public void setColorRed(Integer colorRed) {
        this.colorRed = colorRed;
    }

    public Integer getColorGreen() {
        return colorGreen;
    }

    public void setColorGreen(Integer colorGreen) {
        this.colorGreen = colorGreen;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getOpacity() {
        return opacity;
    }

    public void setOpacity(Integer opacity) {
        this.opacity = opacity;
    }
}

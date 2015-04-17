/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.inspector.css;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

/**
 * @author Max Yurin
 */
public class CssWrapper {
    private WebElement element;

    public CssWrapper(WebElement element) {
        this.element = element;
    }

    public String style() {
        return element.getAttribute("style");
    }

    public String backgroundColor() {
        return element.getCssValue("background-color");
    }

    public String fontWeight() {
        return element.getCssValue("font-weight");
    }

    public String getCssValue(String cssName) {
        return element.getCssValue(cssName);
    }

    public String cssClass() {
        return element.getAttribute("class");
    }

    public String color(){
        return adaptColor(element.getCssValue("color"));
    }

    public Dimension size() {
        final int width = Integer.parseInt(element.getCssValue("width"));
        final int height = Integer.parseInt(element.getCssValue("height"));

        return new Dimension(width, height);
    }

    public String borderStyle(Border property) {
        String value = property == Border.ALL ? "" : property.getValue();

        return element.getCssValue(value + "-style");
    }

    public String borderWidth(Border property) {
        String value = property == Border.ALL ? "" : property.getValue();

        return element.getCssValue("border" + value + "-width");
    }

    public String borderColor(Border property) {
        String value = property == Border.ALL ? "" : property.getValue();

        return adaptColor(element.getCssValue(value + "-color"));
    }


    private String adaptColor(String color) {
        String[] hexValue = new String[0];

        if (color.contains("rgb(")) {
            hexValue = color.replace("rgb(", "").replace(")", "").split(",");
        } else if (color.contains("rgba(")) {
            hexValue = color.replace("rgba(", "").replace(")", "").split(",");
        }

        final int hex1 = Integer.parseInt(hexValue[0].trim());
        final int hex2 = Integer.parseInt(hexValue[1].trim());
        final int hex3 = Integer.parseInt(hexValue[2].trim());

        return String.format("#%02x%02x%02x", hex1, hex2, hex3);
    }
}

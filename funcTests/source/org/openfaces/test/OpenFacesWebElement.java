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

package org.openfaces.test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxWebElement;
import org.openqa.selenium.interactions.internal.Coordinates;

import java.awt.*;
import java.util.List;

/**
 * @author Vladislav Lubenskiy
 */
public class OpenFacesWebElement implements WebElement {

    private FirefoxWebElement baseElement;

    public OpenFacesWebElement(WebElement baseElement) {
        this.baseElement = (FirefoxWebElement) baseElement;
    }

    /**
     * @return Returns position relative to viewport
     */
    public Point position() {
        Coordinates coors = baseElement.getCoordinates();
        Point point = new Point(coors.getLocationInViewPort().getX(), coors.getLocationInViewPort().getY());
        return point;
    }

    public Dimension size() {
        Dimension size = new Dimension(baseElement.getSize().getWidth(), baseElement.getSize().getHeight());
        return size;
    }

    public boolean isVisible() {
        return isDisplayed();
    }

    public String calculateStyleProperty(String propertyName) {
        return getCssValue(propertyName);
    }

    public String className() {
        return getAttribute("class");
    }

    public String attribute(String attributeName) {
        return getAttribute(attributeName);
    }

    public String evalBooleanExpression(String expression) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) baseElement.getWrappedDriver();
        String result = String.valueOf(jsExecutor.executeScript("return !!(arguments[0]." + expression + ");", baseElement));
        return result;
    }
    /////////////////
    public void click() {
        baseElement.click();
    }

    public void submit() {
        baseElement.submit();
    }

    public void sendKeys(CharSequence... charSequences) {
        baseElement.sendKeys(charSequences);
    }

    public void clear() {
        baseElement.clear();
    }

    public String getTagName() {
        return baseElement.getTagName();
    }

    public String getAttribute(String s) {
        return baseElement.getAttribute(s);
    }

    public boolean isSelected() {
        return baseElement.isSelected();
    }

    public boolean isEnabled() {
        return baseElement.isEnabled();
    }

    public String getText() {
        return baseElement.getText();
    }

    public List<WebElement> findElements(By by) {
        return baseElement.findElements(by);
    }

    public WebElement findElement(By by) {
        return baseElement.findElement(by);
    }

    public boolean isDisplayed() {
        return baseElement.isDisplayed();
    }

    public org.openqa.selenium.Point getLocation() {
        return baseElement.getLocation();
    }

    public org.openqa.selenium.Dimension getSize() {
        return baseElement.getSize();
    }

    public String getCssValue(String s) {
        return baseElement.getCssValue(s);
    }
}

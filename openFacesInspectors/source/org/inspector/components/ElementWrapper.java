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

package org.inspector.components;

import org.apache.commons.lang3.StringUtils;
import org.inspector.css.CssWrapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;

import java.util.List;

/**
 * @author Max Yurin
 */
public class ElementWrapper{
    private String id = "";
    private WebDriver driver;
    private WebElement element;
    private CssWrapper cssWrapper;

    public ElementWrapper(WebDriver webDriver, String elementId, String type) {
        this.driver = webDriver;
        this.id = elementId;
        By locator = findBy(elementId);
        this.element = driver.findElement(locator);

        checkTagNameExists(element, type);
        this.cssWrapper = new CssWrapper(element());
    }

    public ElementWrapper(WebDriver driver, WebElement element, String type) {
        this.driver = driver;
        this.element = element;
        this.id = element.getAttribute("id");

        checkTagNameExists(element, type);
        this.cssWrapper = new CssWrapper(element());
    }

    private void checkTagNameExists(WebElement element, String type) {
        if (!element.getTagName().equalsIgnoreCase(type)) {
//            throw new ElementInspectorException("Element should be [" + element.getTagName() + "] but was [" + type + "]");
        }
    }

    public CssWrapper css() {
        return cssWrapper;
    }

    public WebDriver driver() {
        return driver;
    }

    public WebElement element() {
        return element;
    }

    public String id() {
        return id;
    }

    public String attribute(String name) {
        return element.getAttribute(name);
    }

    public WebElement findElement(By by) {
        return element.findElement(by);
    }

    public List<WebElement> findElements(By by) {
        return element.findElements(by);
    }

    protected By findBy(String id) {
        return By.id(id);
    }

    public WebElement subElement(String xpath) {
        return findElement(By.xpath(".//" + xpath));
    }

    public List<WebElement> subElements(String xpath) {
        return findElements(By.xpath(".//" + xpath));
    }

    public WebElement getParent(String tagName) {
        WebElement element = element();

        if (StringUtils.isBlank(tagName)) {
            return null;
        }

        while (!element().getTagName().equalsIgnoreCase(tagName)) {
            element = element().findElement(By.xpath(".."));
        }

        return element;
    }

    public Coordinates getCoordinates() {
        return ((Locatable) element()).getCoordinates();
    }

    protected int parseInt(String value, int defaultValue) {
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}

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
import org.inspector.navigator.DocumentReadyCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * @author Max Yurin
 */
public class ElementWrapper {
    private WebDriver driver;
    private WebElement element;
    private By locator;

    public ElementWrapper(WebDriver webDriver, String elementId, String type) {
        this(webDriver, webDriver.findElement(By.id(elementId)), type);

        this.locator = By.id(elementId);
    }

    public ElementWrapper(WebDriver driver, WebElement element, String type) {
        this.driver = driver;
        this.element = element;

        checkTagNameExists(element, type);
    }

    private void checkTagNameExists(WebElement element, String type) {
        if (!element.getTagName().equalsIgnoreCase(type)) {
            throw new RuntimeException("Element should be [" + element.getTagName() + "] but was [" + type + "]");
        }
    }

    public CssWrapper css() {
        return new CssWrapper(element());
    }

    public WebDriver driver() {
        return driver;
    }

    public WebElement element() {
        if (locator != null) {
            final Wait<WebDriver> wait = new WebDriverWait(driver(), 2000).ignoring(StaleElementReferenceException.class);

            this.element = wait.until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    final WebElement webElement = webDriver.findElement(locator);
                    return webElement != null && webElement.isDisplayed() ? webElement : null;
                }
            });
        }

        return this.element;
    }

    private ExpectedCondition<Boolean> stalenessOf(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                try {
                    return element.isEnabled();
                } catch (StaleElementReferenceException e) {
                    return true;
                }
            }
        };
    }

    public String id() {
        return attribute("id");
    }

    public MouseWrapper mouse() {
        return new MouseWrapper(driver(), element());
    }

    public KeyboardWrapper keyboard() {
        return new KeyboardWrapper(driver(), element());
    }

    public void click() {
        mouse().click();
    }

    public String attribute(String name) {
        return element().getAttribute(name);
    }

    public WebElement findElement(By by) {
        try {
            return element().findElement(by);
        } catch (StaleElementReferenceException e) {
            return element().findElement(by);
        }
    }

    public List<WebElement> findElements(By by) {
        try {
            return element().findElements(by);
        } catch (StaleElementReferenceException e) {
            return element().findElements(by);
        }
    }

    protected By findById(String id) {
        return By.id(id);
    }

    protected boolean isEnabled() {
        return element().isEnabled();
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

    protected void waitForCommandExecute() {
        final WebDriverWait wait = new WebDriverWait(driver(), 200);

        final DocumentReadyCondition condition = new DocumentReadyCondition();
        condition.apply(driver());

        wait.until(condition);
    }

    protected int parseInt(String value, int defaultValue) {
        if (StringUtils.isNumeric(value)) {
            return Integer.parseInt(value.trim());
        }

        return defaultValue;
    }
}

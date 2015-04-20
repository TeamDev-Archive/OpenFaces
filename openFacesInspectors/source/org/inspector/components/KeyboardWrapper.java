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

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * @author Max Yurin
 */
public class KeyboardWrapper {
    private Actions actions;
    private WebDriver driver;
    private WebElement element;

    public KeyboardWrapper(WebDriver driver, WebElement element) {
        this.element = element;
        this.driver = driver;
        this.actions = new Actions(driver);
    }

    public void keyPress(Keys keys) {
        actions.sendKeys(element,keys).perform();
    }

    public void keyUp(Keys controlKey, Keys key){
        actions.keyUp(element, controlKey).sendKeys(element,key).perform();
    }

    public void keyUpWithControlPressed(Keys keys) {
        actions.keyUp(element, Keys.CONTROL).sendKeys(element,keys).perform();
    }

    public void keyUpWithAltPressed(Keys keys) {
        actions.keyUp(element, Keys.ALT).sendKeys(element, keys).perform();
    }

    public void keyUpWithShiftPressed(Keys keys) {
        actions.keyUp(element, Keys.SHIFT).sendKeys(element, keys).perform();
    }

    public void keyUpJs(Keys keys) {
        ((JavascriptExecutor) driver).executeScript(element.getAttribute("onkeyup"), element);
    }

    public void keyDown(Keys keys) {
        actions.keyDown(element, Keys.CONTROL).sendKeys(element,keys).perform();
    }
}

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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;

/**
 * @author Max Yurin
 */
public class MouseWrapper {
    private Mouse mouse;
    private Actions actions;
    private WebDriver driver;
    private WebElement element;

    public MouseWrapper(WebDriver driver, WebElement element) {
        this.mouse = ((HasInputDevices) driver).getMouse();
        this.element = element;
        this.driver = driver;
        this.actions = new Actions(driver);
    }

    public void click() {
        element.click();
    }

    public void clickAndHold(){
        actions.clickAndHold(element).perform();
    }

    public void mouseOver() {
        mouseMove(element);
    }

    public void focus() {
//        mouseMove(element());
        //Workaround
//        final JavascriptExecutor executor = (JavascriptExecutor) driver();
//        executor.executeScript("var x = document.getElementById(\'" + id() + "\'); x.focus();");

        element.click();
    }

    public void blur() {
        final JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("var x = document.getElementById(\'" + element.getAttribute("id") + "\'); x.blur();");
    }

    public void mouseMove(WebElement element) {
        this.mouse.mouseMove(getCoordinates(element));
//        this.actions.moveToElement(element).perform();
    }

    public void mouseUp() {
//        getMouse().mouseUp(getCoordinates());

        //Workaround for last selenium
        this.actions.release(element).perform();
    }

    public void mouseDown() {
        this.mouse.mouseDown(getCoordinates());
    }

    public void doubleClick() {
        this.mouse.doubleClick(getCoordinates());
    }

    public Coordinates getCoordinates() {
        return getCoordinates(element);
    }

    private Coordinates getCoordinates(WebElement element) {
        return ((Locatable) element).getCoordinates();
    }

}

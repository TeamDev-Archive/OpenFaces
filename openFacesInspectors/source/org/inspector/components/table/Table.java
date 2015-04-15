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

package org.inspector.components.table;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author Max Yurin
 */
public class Table extends TableRowContainer {
    public static final String TAG_NAME = "TABLE";
    public static final By TBODY = By.xpath(TableBody.TAG_NAME);
    public static final By THEAD = By.xpath(TableFooter.TAG_NAME);
    public static final By TFOOTER = By.xpath(TableHeader.TAG_NAME);

    public Table(WebDriver webDriver, String id) {
        super(webDriver, id, TAG_NAME);
    }

    public Table(WebDriver webDriver, By locator) {
        super(webDriver, webDriver.findElement(locator), TAG_NAME);
    }

    public TableHeader head() {
        return new TableHeader(driver(), findElement(THEAD));
    }

    public TableBody body() {
        return new TableBody(driver(), findElement(TBODY));
    }

    public TableFooter footer() {
        return new TableFooter(driver(), findElement(TFOOTER));
    }

    public String width() {
        return element().getCssValue("width");
    }

    public String height() {
        return element().getCssValue("height");
    }

    public Column column(int index) {
        return null;
    }

    public void toggleNodes() {

    }

    public void expandNodes() {

    }

    public boolean isEmpty() {
        return false;
    }

    public void clear() {

    }
}

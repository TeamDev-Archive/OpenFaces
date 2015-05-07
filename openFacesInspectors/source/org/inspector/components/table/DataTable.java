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

import org.inspector.api.ComboBox;
import org.inspector.api.DropDown;
import org.inspector.api.Filter;
import org.inspector.api.Input;
import org.inspector.api.Table;
import org.inspector.components.ElementWrapper;
import org.inspector.components.filters.ComboboxFilter;
import org.inspector.components.filters.DropDownFilter;
import org.inspector.components.filters.InputTextFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Max Yurin
 */
public class DataTable extends ElementWrapper implements Table {
    public static final By TBODY = By.xpath(TableBody.TAG_NAME);
    public static final By THEAD = By.xpath(TableHeader.TAG_NAME);
    public static final By TFOOTER = By.xpath(TableFooter.TAG_NAME);

    public DataTable(WebDriver webDriver, String id) {
        super(webDriver, id, Table.TAG_NAME);
    }

    public DataTable(WebDriver webDriver, By locator) {
        super(webDriver, webDriver.findElement(locator), Table.TAG_NAME);
    }

    @Override
    public TableHeader header() {
        return new TableHeader(driver(), findElement(THEAD));
    }

    @Override
    public TableBody body() {
        return new TableBody(driver(), findElement(TBODY));
    }

    @Override
    public TableFooter footer() {
        return new TableFooter(driver(), findElement(TFOOTER));
    }

    @Override
    public String width() {
        return element().getCssValue("width");
    }

    @Override
    public String height() {
        return element().getCssValue("height");
    }

    @Override
    public Filter filter(Filter.FilterType type) {
        WebElement element;

        switch (type) {
            case INPUTTEXT:
                element = header().findElement(By.tagName(Input.TAG_NAME));
                return new InputTextFilter(driver(), element.getAttribute("id"));
            case DROPDOWN:
                element = header().findElement(By.tagName(DropDown.TAG_NAME));
                return new DropDownFilter(driver(), element.getAttribute("id"));
            case COMBOBOX:
                element = header().findElement(By.tagName(ComboBox.TAG_NAME));
                return new ComboboxFilter(driver(), element.getAttribute("id"));
            default:
                throw new IllegalArgumentException("Uncompatible filter type");
        }
    }
}

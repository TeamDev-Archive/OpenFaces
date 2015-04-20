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

import org.inspector.api.Chart;
import org.inspector.api.Control;
import org.inspector.api.DateChooser;
import org.inspector.api.Input;
import org.inspector.components.table.Table;
import org.openqa.selenium.WebDriver;

/**
 * @author Max Yurin
 */
public class ControlFactory {
    private WebDriver driver;

    public ControlFactory(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public Control getAjaxCommandLink(String id) {
        return new AjaxCommandLink(driver, id);
    }

    public Control getCommandLink(String id) {
        return new CommandLink(driver, id);
    }

    public CompositeFilter getCompositeFilter(String id) {
        return new CompositeFilter(driver, id);
    }

    public DateChooser getCalendar(String id) {
        return new Calendar(driver, id);
    }

    public Chart getChart(String id) {
        return new ChartImpl(driver, id);
    }

    public Input getInputText(String id) {
        return new InputText(driver, id);
    }

    public Input getInputTextArea(String id) {
        return new InputTextArea(driver, id);
    }

    public Table getDataTable(String id) {
        return new Table(driver, id);
    }

    public Pagination getPagination(String id){
        return new Pagination(driver, id);
    }

    public TabSet getTabSet(String id){
        return new TabSet(driver, id);
    }
}

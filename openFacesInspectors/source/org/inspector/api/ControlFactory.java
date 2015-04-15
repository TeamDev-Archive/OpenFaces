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

package org.inspector.api;

import org.inspector.components.AjaxCommandLink;
import org.inspector.components.Calendar;
import org.inspector.components.ChartImpl;
import org.inspector.components.CommandLink;
import org.inspector.components.CompositeFilter;
import org.inspector.components.InputText;
import org.inspector.components.InputTextArea;
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
}

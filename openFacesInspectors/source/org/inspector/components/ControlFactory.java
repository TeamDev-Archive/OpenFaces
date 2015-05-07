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
import org.inspector.api.DropDown;
import org.inspector.api.Input;
import org.inspector.components.input.DropDownImpl;
import org.inspector.components.input.InputText;
import org.inspector.components.input.InputTextArea;
import org.inspector.components.table.DataTable;
import org.inspector.components.table.Pagination;
import org.inspector.webriver.WebDriverManager;
import org.openqa.selenium.WebDriver;

/**
 * @author Max Yurin
 */
public class ControlFactory {
    public ControlFactory() {
    }

    public WebDriver getDriver() {
        return WebDriverManager.getWebDriver();
    }

    public Control getAjaxCommandLink(String id) {
        return new AjaxCommandLink(getDriver(), id);
    }

    public Control getCommandLink(String id) {
        return new CommandLink(getDriver(), id);
    }

    public DateChooser getCalendar(String id) {
        return new Calendar(getDriver(), id);
    }

    public Chart getChart(String id) {
        return new ChartImpl(getDriver(), id);
    }

    public Input getInputText(String id) {
        return new InputText(getDriver(), id);
    }

    public Input getInputTextArea(String id) {
        return new InputTextArea(getDriver(), id);
    }

    public DataTable getDataTable(String id) {
        return new DataTable(getDriver(), id);
    }

    public Pagination getPagination(String id) {
        return new Pagination(getDriver(), id);
    }

    public DropDown getDropDown(String id){
        return new DropDownImpl(getDriver(), id);
    }

    public TabSet getTabSet(String id) {
        return new TabSet(getDriver(), id);
    }
}

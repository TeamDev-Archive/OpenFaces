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

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.inspector.components.ElementWrapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Max Yurin
 */
public class TableRowContainer extends ElementWrapper {
    public static final By ROWS = By.xpath(TableRow.TAG_NAME);

    public TableRowContainer(WebDriver webDriver, String id, String type) {
        super(webDriver, id, type);
    }

    public TableRowContainer(WebDriver driver, WebElement element, String type) {
        super(driver, element, type);
    }

    public TableRow nextRow() {
        return rows().iterator().next();
    }

    public List<TableRow> rows() {
        return newArrayList(
                Lists.transform(element().findElements(ROWS), new Function<WebElement, TableRow>() {
                    @Override
                    public TableRow apply(WebElement element) {
                        return new TableRow(driver(), element);
                    }
                }));
    }

    public int rowsCount() {
        return findElements(ROWS).size();
    }

    public TableRow row(int index) {
        return rows().get(index);
    }
}

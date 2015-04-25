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
public class TableCellContainer extends ElementWrapper {
    public static final By CELLS = By.xpath(TableCell.TAG_NAME);

    public TableCellContainer(WebDriver driver, WebElement element, String type) {
        super(driver, element, type);
    }

    public TableCellContainer(WebDriver webDriver, String elementId, String type) {
        super(webDriver, elementId, type);
    }

    public TableCell nextCell() {
        return cells().iterator().next();
    }

    public List<WebElement> iterator() {
        return element().findElements(CELLS);
    }

    public TableCell cell(int index) {
        return cells().get(index);
    }

    public List<TableCell> cells() {
        final List<WebElement> elements = element().findElements(CELLS);

        return newArrayList(
                Lists.transform(elements, new Function<WebElement, TableCell>() {
                    @Override
                    public TableCell apply(WebElement element) {
                        return new TableCell(driver(), element);
                    }
                }));
    }
}

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

import org.inspector.components.ElementWrapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Iterator;
import java.util.List;

/**
 * @author Max Yurin
 */
public class TableCellContainer extends ElementWrapper implements Iterable<TableCell> {
    public static final By CELLS = By.xpath(TableCell.TAG_NAME);
    private List<WebElement> cells;
    private int index = 0;

    public TableCellContainer(WebDriver driver, WebElement element, String type) {
        super(driver, element, type);
    }

    public TableCellContainer(WebDriver webDriver, String elementId, String type) {
        super(webDriver, elementId, type);
    }

    public Iterator<TableCell> cellIterator() {
        return iterator();
    }

    public TableCell nextCell() {
        return iterator().next();
    }

    @Override
    public Iterator<TableCell> iterator() {
        this.cells = element().findElements(CELLS);

        return new Iterator<TableCell>() {
            @Override
            public boolean hasNext() {
                return index < cells.size();
            }

            @Override
            public TableCell next() {
                return new TableCell(driver(), cells.get(index++));
            }

            @Override
            public void remove() {
                cells.remove(index);
            }
        };
    }
}

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

package org.inspector.components.popup;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.inspector.api.Popup;
import org.inspector.components.table.DataTable;
import org.inspector.components.table.TableCell;
import org.inspector.components.table.TableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author Max Yurin
 */
public class PopupBasedOnTable extends DataTable implements Popup {
    public PopupBasedOnTable(WebDriver webDriver, By locator) {
        super(webDriver, locator);
    }

    @Override
    public void select(final String desiredValue) {
        checkPopupIsVisible();

        final TableRow tableRow = Iterables.find(this.body().rows(), new Predicate<TableRow>() {
            @Override
            public boolean apply(TableRow row) {
                return row.nextCell().text().equals(desiredValue);
            }
        });
        tableRow.nextCell().click();
    }

    @Override
    public String select(int index) {
        checkPopupIsVisible();

        final TableRow row = this.body().row(index);
        if (row != null) {

            final TableCell tableCell = row.nextCell();
            if (tableCell != null) {
                final String text = tableCell.text();
                tableCell.element().click();
                return text;
            }
        }
        return null;
    }

    private void checkPopupIsVisible() {
        if (!this.isDisplayed()) {
            throw new RuntimeException("Popup is not displayed!");
        }
    }

    @Override
    public String getSelectedOption() {
        final WebElement element = findElement(By.xpath("//select[@id='" + id() + "']//option[@selected='selected']"));
        return element.getText();
    }

    @Override
    public List<String> getItemsList() {
        return Lists.transform(this.body().rows(), new Function<TableRow, String>() {
            @Override
            public String apply(TableRow row) {
                return row.nextCell().text();
            }
        });
    }
}

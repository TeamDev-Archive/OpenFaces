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

import org.apache.commons.lang.StringUtils;
import org.inspector.css.Border;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.inspector.css.Border.*;

/**
 * @author Max Yurin
 */
public class TableRow extends TableCellContainer {
    public static final String TAG_NAME = "tr";
    private static final By CELLS = By.xpath(TableCell.TAG_NAME);

    public TableRow(WebDriver webDriver, String id) {
        super(webDriver, id, TAG_NAME);
    }

    public TableRow(WebDriver driver, WebElement element) {
        super(driver, element, TAG_NAME);
    }

    public int cellCount() {
        return findElements(CELLS).size();
    }

//    public TableCell cell(int index) {
////        return new TableCell(driver(), findElements(CELLS).get(index));
//        return super.cell(index);
//    }

    public WebElement treeTableToggle() {
        return findElement(By.className("o_treetable_folding"));
    }

    public void assertCellParams(TableCellParams[] paramsArray) {
        assertThat("Checking number of cells: ", paramsArray.length, is(cellCount()));
        final List<TableCell> cells = cells();

        for (int i = 0; i < paramsArray.length; i++) {
            final TableCellParams param = paramsArray[i];
            final TableCell cell = cells.get(i);

            assertThat("Checking cell text: ", cell.text(), is(param.getText()));
            assertThat("Checking cell col span: ", cell.colSpan(), is(param.getColSpan()));
            assertThat("Checking cell row span: ", cell.rowSpan(), is(param.getRowSpan()));

            assertStyle(param.getStyle());
        }
    }

    private void assertStyle(String styleDeclaration) {
        for (String style : styleDeclaration.split(";")) {
            style = style.trim();
            if (style.isEmpty()) {
                continue;
            }
            final String[] pair = style.split(":");
            final String name = pair[0];
            final String value = pair[1];

            assertStyleProperty(name, value);
        }
    }

    private void assertStyleProperty(String name, String value) {
        if (name.contains("border")) {
            assertBorderProperty(name, value);
        } else if (name.contains("background") && !name.contains("url(")) {
//            assertBackgroundColor(value);
        } else if (name.contains("color")) {
            assertColorProperty(value);
        } else if (name.contains("font-weight")) {
            assertFontWeightProperty(value);
        }
    }

    private void assertFontWeightProperty(String value) {
        assertThat("Checking fontWeight", value, is(equalTo(css().fontWeight())));
    }

    private void assertColorProperty(String value) {
        assertThat("Checking color property: ", value, is(css().color()));
    }

    private void assertBackgroundColor(String value) {
        final String backgroundColor = css().backgroundColor();

        assertThat("Checking background color: ", value, is(equalTo(backgroundColor)));
    }

    private void assertBorderProperty(String name, String value) {
        final Border border = getBorderFor(name);
        final String[] values = value.split(" ");
        final String width = "?".equals(values[0]) ? null : values[0];
        final String style = "?".equals(values[1]) ? null : values[1];
        final String color = "?".equals(values[2]) ? null : values[2];
        final String message = "Checking " + border.getValue() + " property";

        if (StringUtils.isNotBlank(width)) {
            assertThat(message + "-width", width, is(not(css().borderWidth(border))));
        }

        if (StringUtils.isNotBlank(color)) {
            assertThat(message + "-color", color, is(not(css().borderColor(border))));
        }

        if (StringUtils.isNotBlank(style)) {
            assertThat(message + "-style", style, is(not(css().borderStyle(border))));
        }
    }

    private Border getBorderFor(String name) {
        if (name.contains(BORDER_TOP.getValue())) {
            return BORDER_TOP;
        } else if (name.contains(BORDER_LEFT.getValue())) {
            return BORDER_LEFT;
        } else if (name.contains(BORDER_RIGHT.getValue())) {
            return BORDER_RIGHT;
        } else if (name.contains(BORDER_BOTTOM.getValue())) {
            return BORDER_BOTTOM;
        }
        return ALL;
    }
}

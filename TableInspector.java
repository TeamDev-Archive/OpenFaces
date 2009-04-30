/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.test.html;

import junit.framework.Assert;
import org.openfaces.test.ElementByLocatorInspector;
import org.openfaces.test.ElementByReferenceInspector;
import org.openfaces.test.ElementInspector;

import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TableInspector extends ElementByReferenceInspector {
    private TableSectionInspector headerInspector;
    private TableSectionInspector bodyInspector;
    private TableSectionInspector footerInspector;

    public TableInspector(ElementInspector tableElement) {
        super(tableElement);
    }

    public TableInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public TableSectionInspector header() {
        if (headerInspector == null)
            headerInspector = createSectionInspector("THEAD");
        return headerInspector;
    }

    public TableSectionInspector body() {
        if (bodyInspector == null) {
            bodyInspector = createSectionInspector("TBODY");
            if (!bodyInspector.elementExists()) {
                List<ElementInspector> rows = getElementsByTagName("tr");
                bodyInspector = new TableSectionInspector(this, rows);
            }
        }
        return bodyInspector;
    }

    public TableSectionInspector footer() {
        if (footerInspector == null)
            footerInspector = createSectionInspector("TFOOT");
        return footerInspector;
    }

    protected TableSectionInspector createSectionInspector(String sectionTagName) {
        return new TableSectionInspector(this, sectionTagName);
    }

    public int getColumnCount() {
        return childNodesByName("COL").size();
    }

    public TableColumnInspector column(int columnIndex) {
        return new TableColumnInspector(this, columnIndex);
    }

    public void assertBodyRowTexts(String... expectedRowTexts) {
        int rowCount = expectedRowTexts.length;
        Assert.assertEquals(rowCount, body().rowCount());
        for (int i = 0; i < rowCount; i++) {
            String expectedText = expectedRowTexts[i];
            body().row(i).assertText(expectedText);
        }
    }
}
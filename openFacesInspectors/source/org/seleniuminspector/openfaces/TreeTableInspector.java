/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.seleniuminspector.openfaces;

import org.openfaces.renderkit.table.TreeTableRenderer;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.LoadingMode;
import org.seleniuminspector.html.TableSectionInspector;

import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TreeTableInspector extends AbstractTableInspector {
    public TreeTableInspector(String locator) {
        super(locator);
    }

    public TreeTableInspector(ElementInspector tableElement) {
        super(tableElement);
    }

    protected TableSectionInspector createSectionInspector(String sectionTagName) {
        return new TreeTableSectionInspector(this, sectionTagName);
    }

    public TreeTableRowInspector headerRow(int rowIndex) {
        return (TreeTableRowInspector) header().row(rowIndex);
    }

    public TreeTableRowInspector bodyRow(int rowIndex) {
        return (TreeTableRowInspector) body().row(rowIndex);
    }

    public TreeTableRowInspector footerRow(int rowIndex) {
        return (TreeTableRowInspector) footer().row(rowIndex);
    }

    /**
     * Expands all nodes provided that all nodes are collapsed before calling this method. Expansion is made by clicking
     * the expansion toggles.
     * <p/>
     * Note that this method doesn't check whether nodes are realy collapsed before invoking this function and clicks all
     * expansion toggles blindly, so unwanted behavior could take place if any of the nodes was expanded before invoking
     * this function.
     */
    public void expandAllNodes(LoadingMode loadingMode) {
        TableSectionInspector body = body();
        for (int i = 0; i < body.rowCount(); i++) {
            TreeTableRowInspector row = (TreeTableRowInspector) body.row(i);
            ElementInspector expansionToggle = row.expansionToggle();
            if (expansionToggle != null) {
                expansionToggle.click();
                loadingMode.waitForLoad();
            }
        }
    }

    /**
     * Toggles all nodes in the inspected TreeTable, that is expanded nodes become collapsed, and collapsed ones become
     * expanded. Note that this method works in the simplest way possible for performance reasons: it just finds all image
     * elements inside of the inspected TreeTable, and clicks each of these images.
     *
     * @param loadingMode
     */
    public void toggleAllNodes(LoadingMode loadingMode) {
        int i = 0;
        while (true) {
            List<ElementInspector> toggles = getElementsByTagName("img");
            if (i >= toggles.size())
                break;
            ElementInspector toggle = toggles.get(i);
            if (TreeTableRenderer.DEFAULT_TOGGLE_CLASS_NAME.equals(toggle.className())) {
                toggle.click();
                loadingMode.waitForLoad();
            }
            i++;
        }

    }
}

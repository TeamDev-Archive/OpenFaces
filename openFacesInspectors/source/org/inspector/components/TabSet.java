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

import org.apache.commons.lang3.StringUtils;
import org.inspector.components.table.DataTable;
import org.inspector.components.table.TableCell;
import org.openqa.selenium.WebDriver;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Max Yurin
 */
public class TabSet extends DataTable {
    private String componentId;

    public TabSet(WebDriver webDriver, String elementId) {
        super(webDriver, elementId);
        this.componentId = elementId;
    }

    public void openTab(String id) {
        final Collection<TableCell> tabs = getTabs();
        for (TableCell tab : tabs) {
            if (tab.id().equals(componentId + "::" + id)) {
                tab.click();
            }
        }
    }

    public Collection<TableCell> getTabs() {
        final List<TableCell> cells = super.body().nextRow().cells();
        Collection<TableCell> tabs = newArrayList();

        for (TableCell cell : cells) {
            if (StringUtils.isNotEmpty(cell.id())) {
                tabs.add(cell);
            }
        }

        return tabs;
    }
}

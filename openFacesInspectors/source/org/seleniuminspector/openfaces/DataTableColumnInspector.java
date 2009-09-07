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
package org.seleniuminspector.openfaces;


import org.junit.Assert;
import org.seleniuminspector.LoadingMode;
import org.seleniuminspector.html.TableColumnInspector;

import java.lang.reflect.Constructor;

/**
 * @author Andrii Gorbatov
 */
public class DataTableColumnInspector extends TableColumnInspector {
    private LoadingMode loadingMode;

    public DataTableColumnInspector(AbstractTableInspector dataTable, int columnIndex, LoadingMode loadingMode) {
        super(dataTable, columnIndex);

        this.loadingMode = loadingMode;
    }

    /**
     * Simple method that just clicks at sortable column header and watch is ajax error happened.
     */
    public void makeSorting() {
        headerCell(0).click();
        loadingMode.waitForLoad();
        //true if alert with the message about ajax error absent
        Assert.assertFalse(getSelenium().isAlertPresent());
    }


    public <T extends AbstractFilterInspector> T filter(Class<T> filterTypeClass, String filterId) {
        T filter;

        try {
            Constructor<T> filterConstructor = filterTypeClass.getConstructor(String.class, LoadingMode.class);
            filter = filterConstructor.newInstance(filterId, loadingMode);
        } catch (Exception ex) {
            throw new RuntimeException("Filter inspector creating failure", ex);
        }

        return filter;
    }

//    private String getFilterXPath(AbstractFilterInspector.FilterType filterType) {
//
//        TableCellInspector headerCell = headerCell(1);
//        List<ElementInspector> children = headerCell.childNodes();
//        for (ElementInspector child : children) {
//            String id = child.id();
//            int x = 32 + 2;
//        }
//
//        StringBuilder filterXPath = new StringBuilder("//");
//        filterXPath.append("*[@id='").append(id()).append("']");
//        filterXPath.append(" and (contains(@id, '").append(RendererUtil.CLIENT_ID_SUFFIX_SEPARATOR).append("') = false)");
//        filterXPath.append(" and contains(@id, '").append(filterType.getSuffix()).append("')");
//
//        return filterXPath.toString();
//    }
}

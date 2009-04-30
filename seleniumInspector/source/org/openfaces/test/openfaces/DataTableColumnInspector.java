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
package org.openfaces.test.openfaces;


import org.junit.Assert;
import org.openfaces.test.ElementInspector;
import org.openfaces.test.html.TableCellInspector;
import org.openfaces.test.html.TableColumnInspector;
import org.openfaces.util.RenderingUtil;

import java.lang.reflect.Constructor;
import java.util.List;

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
        loadingMode.waitForLoadCompletion();
        //true if alert with the message about ajax error absent
        Assert.assertFalse(getSelenium().isAlertPresent());
    }


    public <T extends DataTableFilterInspector> T filter(Class<T> filterTypeClass) {
        T filter = null;

        ElementInspector rawFilter = null;

        if (filterTypeClass == DropDownFieldFilterInspector.class) {
            rawFilter = getRawFilter(DataTableFilterInspector.FilterType.DROP_DOWN_FIELD);
        } else if (filterTypeClass == SearchFieldFilterInspector.class) {
            rawFilter = getRawFilter(DataTableFilterInspector.FilterType.SEARCH_FIELD);
        } else if (filterTypeClass == ComboBoxFilterInspector.class) {
            rawFilter = getRawFilter(DataTableFilterInspector.FilterType.COMBO_BOX);
        }

        if (rawFilter != null) {
            try {
                Constructor<T> filterConstructor = filterTypeClass.getConstructor(String.class, LoadingMode.class);
                filter = filterConstructor.newInstance(rawFilter.id(), loadingMode);
            } catch (Exception ex) {
                throw new RuntimeException("Filter inspector creating failure", ex);
            }
        }

        return filter;
    }

    private ElementInspector getRawFilter
            (DataTableFilterInspector.FilterType
                    filterType) {

        for (int i = 0; i < getHeaderCellCount(); i++) {
            TableCellInspector headerCell = headerCell(i);
            List<ElementInspector> children = headerCell.childNodes();
            for (ElementInspector child : children) {
                if (child.id().contains(filterType.getSuffix())
                        && !child.id().contains(RenderingUtil.CLIENT_ID_SUFFIX_SEPARATOR)) {
                    return child;
                }
            }
        }

        return null;
    }

//    private String getFilterXPath(DataTableFilterInspector.FilterType filterType) {
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

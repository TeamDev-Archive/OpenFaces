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

package org.openfaces.testapp.datatable;

import org.openfaces.util.FacesUtil;
import org.openfaces.component.table.FilterCriterion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ColorListBean {

    private ColorDB colorDB = new ColorDB();
    private List<String> colorsNames = new ArrayList<String>();

    public ColorListBean() {
        colorsNames.add("white");
        colorsNames.add("black");
        colorsNames.add("gray");
        colorsNames.add("red");
        colorsNames.add("orange");
        colorsNames.add("yellow");
        colorsNames.add("green");
        colorsNames.add("blue");
        colorsNames.add("violet");
    }

    public int getRowCount() {
        Map<String, FilterCriterion> filterCriteria = (Map) FacesUtil.getRequestMapValue("filterCriteria");
        return colorDB.getFilteredColorCount(filterCriteria);
    }

    public List getColorList() {
        Map<String, FilterCriterion> filterCriteria = (Map) FacesUtil.getRequestMapValue("filterCriteria");
        boolean sortAscending = (Boolean) FacesUtil.getRequestMapValue("sortAscending");
        String sortColumnId = (String) FacesUtil.getRequestMapValue("sortColumnId");
        int pageStart = (Integer) FacesUtil.getRequestMapValue("pageStart");
        int pageSize = (Integer) FacesUtil.getRequestMapValue("pageSize");
        return colorDB.findColorsForPage(filterCriteria, sortColumnId, sortAscending, pageStart, pageSize);
    }

    public Color getColorByKey() {
        Integer key = (Integer) FacesUtil.getRequestMapValue("rowKey");
        return colorDB.getColorByKey(key);
    }

    public List<String> getColorsNames() {
        return colorsNames;
    }

    public List<String> getQueries() {
        return colorDB.getQueries();
    }
}

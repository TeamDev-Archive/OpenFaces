/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.datatable;

import org.openfaces.component.filter.CompositeFilterCriterion;
import org.openfaces.util.FacesUtil;

import java.util.ArrayList;
import java.util.List;

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
        CompositeFilterCriterion filterCriteria = FacesUtil.var("filterCriteria", CompositeFilterCriterion.class);
        return colorDB.getFilteredColorCount(filterCriteria);
    }

    public List getColorList() {
        CompositeFilterCriterion filterCriteria = FacesUtil.var("filterCriteria", CompositeFilterCriterion.class);
        boolean sortAscending = FacesUtil.var("sortAscending", Boolean.class);
        String sortColumnId = FacesUtil.var("sortColumnId", String.class);
        int pageStart = FacesUtil.var("pageStart", Integer.class);
        int pageSize = FacesUtil.var("pageSize", Integer.class);
        return colorDB.findColorsForPage(filterCriteria, sortColumnId, sortAscending, pageStart, pageSize);
    }

    public Color getColorByKey() {
        Integer key = FacesUtil.var("rowKey", Integer.class);
        return colorDB.getColorByKey(key);
    }

    public List<String> getColorsNames() {
        return colorsNames;
    }

    public List<String> getQueries() {
        return colorDB.getQueries();
    }
}

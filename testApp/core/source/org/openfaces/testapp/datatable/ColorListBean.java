/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
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
import org.openfaces.util.Faces;

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
        CompositeFilterCriterion filterCriteria = Faces.var("filterCriteria", CompositeFilterCriterion.class);
        return colorDB.getFilteredColorCount(filterCriteria);
    }

    public List getColorList() {
        CompositeFilterCriterion filterCriteria = Faces.var("filterCriteria", CompositeFilterCriterion.class);
        boolean sortAscending = Faces.var("sortAscending", Boolean.class);
        String sortColumnId = Faces.var("sortColumnId", String.class);
        int pageStart = Faces.var("pageStart", Integer.class);
        int pageSize = Faces.var("pageSize", Integer.class);
        return colorDB.findColorsForPage(filterCriteria, sortColumnId, sortAscending, pageStart, pageSize);
    }

    public Color getColorByKey() {
        Integer key = Faces.var("rowKey", Integer.class);
        return colorDB.getColorByKey(key);
    }

    public List<String> getColorsNames() {
        return colorsNames;
    }

    public List<String> getQueries() {
        return colorDB.getQueries();
    }
}

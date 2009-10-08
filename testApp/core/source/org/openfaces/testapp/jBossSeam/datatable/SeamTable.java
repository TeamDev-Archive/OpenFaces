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

package org.openfaces.testapp.jBossSeam.datatable;

import org.openfaces.component.filter.ExpressionFilterCriterion;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */

@Local
public interface SeamTable {
    public void findItems();

    public void destroy();

    public String sortByForthColumn();

    public List<String> getFilterValues();

    public ExpressionFilterCriterion getFilterValue();

    public void setFilterValue(ExpressionFilterCriterion filterValue);

    public String getCurrentItem();

    public void setCurrentItem(String currentItem);

    public void select();

    public DataTableItem getSingleSelectionItem();

    public void setSingleSelectionItem(DataTableItem singleSelectionItem);

    public List<DataTableItem> getMultipleSelectionItems();

    public String getCol1Value();

    public String getCol2Value();

    public Date getCol3Value();

    public int getCol4Value();

    public void selectItem();

    public void setMultipleSelectionItems(List<DataTableItem> multipleSelectionItems);
}

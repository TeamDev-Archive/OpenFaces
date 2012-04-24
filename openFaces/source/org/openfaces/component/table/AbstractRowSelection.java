/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.table;

import org.openfaces.component.table.impl.TableDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andrii.loboda
 */
public abstract class AbstractRowSelection extends AbstractTableSelection {

    protected AbstractRowSelection() {
    }

    protected AbstractRowSelection(TableDataModel model) {
        super(model);
    }

    @Override
    protected List<Integer> getSelectionColumnIndexes(AbstractTable table) {
        List<Integer> result = new ArrayList<Integer>();
        List<BaseColumn> columns = table.getRenderedColumns();
        for (int i = 0, colIndex = 0, colCount = columns.size(); i < colCount; i++) {
            BaseColumn column = columns.get(i);
            if (column instanceof SelectionColumn) {
                result.add(colIndex);
            }
            colIndex++;
        }
        return result;
    }

    @Override
    protected List<?> convertFieldValue(String fieldValue) {
        fieldValue = fieldValue.substring(1, fieldValue.length() - 1);
        String[] items = fieldValue.split(",");
        int itemCount = items.length;
        if (itemCount == 1 && items[0].equals(""))
            itemCount = 0;
        List<Integer> indexes = new ArrayList<Integer>(itemCount);
        for (int i = 0; i < itemCount; i++) {
            String item = items[i];
            indexes.add(Integer.valueOf(item));
        }
        return indexes;
    }

    @Override
    public String getSelectableItems() {
        return "rows";
    }
}

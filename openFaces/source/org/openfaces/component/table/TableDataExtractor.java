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

import org.openfaces.component.table.export.CellComponentsDataExtractor;
import org.openfaces.component.table.export.CellDataExtractor;
import org.openfaces.component.table.export.ComponentDataExtractor;
import org.openfaces.component.table.export.SelectValueExtractor;
import org.openfaces.component.table.export.UICommandDataExtractor;
import org.openfaces.component.table.export.UIInstructionsDataExtractor;
import org.openfaces.component.table.export.ValueHolderDataExtractor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Natalia.Zolochevska@Teamdev.com
 */
public class TableDataExtractor {

    public static final String EXTRACTED_DATA_ATTRIBUTE = "extractedData";

    private List<ComponentDataExtractor> defaultComponentExtractors = new LinkedList<ComponentDataExtractor>();
    private List<ComponentDataExtractor> componentExtractors = new LinkedList<ComponentDataExtractor>();
    private List<CellDataExtractor> cellDataExtractors = new LinkedList<CellDataExtractor>();

    private DataScope scope;

    public TableDataExtractor(DataScope scope, Class... extractionIgnoredClasses) {
        defaultComponentExtractors.add(new UICommandDataExtractor());
        defaultComponentExtractors.add(new SelectValueExtractor());
        defaultComponentExtractors.add(new ValueHolderDataExtractor());
        defaultComponentExtractors.add(new UIInstructionsDataExtractor());
        componentExtractors.addAll(defaultComponentExtractors);
        cellDataExtractors.add(new CellComponentsDataExtractor(componentExtractors, extractionIgnoredClasses));
        this.scope = scope;
    }

    public List<ComponentDataExtractor> getComponentExtractors() {
        return componentExtractors;
    }

    public List<CellDataExtractor> getCellDataExtractors() {
        return cellDataExtractors;
    }


    /**
     * Extracts the data displayed in the table, including row datas, the list of columns, and displayed cell texts as
     * the TableData instance. The list and order of rows and columns reflects the current state of the table component
     * itself.
     */
    public TableData extract(AbstractTable table) {
        List<BaseColumn> columns;
        if (table instanceof DataTable) {
            columns = ((DataTable)table).getGroupedAndRenderedColumns();
        } else {
            columns = table.getRenderedColumns();
        }

        Iterable<Object> rowDatas = table.getRowDatas(scope);
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        List<TableRowData> tableRowDatas = new ArrayList<TableRowData>();

        for (Object rowData : rowDatas) {
            if (rowData instanceof GroupHeader)
                continue;

            Object prevVarValue = requestMap.put(table.getVar(), rowData);
            List<Object> cellDatas = calculateCellDatas(rowData, columns);
            tableRowDatas.add(new TableRowData(rowData, cellDatas));
            requestMap.put(table.getVar(), prevVarValue);
        }
        List<ColumnGroup> columnGroups = new ArrayList<ColumnGroup>();
        for (UIComponent component : table.getChildren()) {
            if (component instanceof ColumnGroup)
                columnGroups.add((ColumnGroup) component);
        }
        return new TableData(columns, tableRowDatas, columnGroups);
    }

    private List<Object> calculateCellDatas(Object rowData, List<BaseColumn> columns){
        List<Object> cellDatas = new ArrayList<Object>();
        for (BaseColumn column : columns) {
            Object cellValue = null;
            for (CellDataExtractor cellExtractor : cellDataExtractors) {
                if (cellExtractor.isApplicableFor(rowData, column)) {
                    cellValue = cellExtractor.getData(rowData, column);
                    break;
                }
            }
            cellDatas.add(cellValue);
        }
        return cellDatas;
    }

}

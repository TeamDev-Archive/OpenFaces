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

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class XLSXTableDataFormatter extends TableDataFormatter {
    protected String getContentType() {
        return "text/xlsx";
    }

    @Override
    protected boolean isBinaryContent() {
        return true;
    }

    @Override
    protected void writeFileContent(TableData tableData, PrintWriter writer, OutputStream outputStream) {
//        Workbook wb = new XSSFWorkbook();
//
//        FileOutputStream file = new FileOutputStream();
//
//        PrintWriter writer = new PrintWriter(stringWriter);
//        try {
//            writeCSVLine(writer, mapList(tableData.getTableColumnDatas(), new Mapper<TableColumnData, String>() {
//                public String map(TableColumnData obj) {
//                    return obj.getColumnHeader();
//                }
//            }));
//            List<TableRowData> rowDatas = tableData.getTableRowDatas();
//            for (TableRowData rowData : rowDatas) {
//                writeCSVLine(writer, rowData.getCellStrings());
//            }
//            writer.flush();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public String getDefaultFileExtension() {
        return "xlsx";
    }

    private <I, O> List<O> mapList(List<I> srcList, Mapper<I, O> mapper) {
        List<O> result = new ArrayList<O>();
        for (I i : srcList) {
            O o = mapper.map(i);
            result.add(o);
        }
        return result;
    }

    private interface Mapper<I, O> {
        public O map(I obj);
    }

}
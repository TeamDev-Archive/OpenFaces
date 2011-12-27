/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.table;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class CSVTableExporter extends TableExporter {

    protected String getContentType() {
        return "text/csv";
    }

    @Override
    protected boolean isBinaryContent() {
        return false;
    }

    @Override
    protected void writeFileContent(TableData tableData, PrintWriter writer, OutputStream outputStream) {
        try {
            writeCSVLine(writer, mapList(tableData.getTableColumnDatas(), new Mapper<TableColumnData, String>() {
                public String map(TableColumnData obj) {
                    return obj.getColumnHeader();
                }
            }));
            List<TableRowData> rowDatas = tableData.getTableRowDatas();
            for (TableRowData rowData : rowDatas) {
                writeCSVLine(writer, rowData.getCellStrings());
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDefaultFileExtension() {
        return "csv";
    }

    private void writeCSVLine(PrintWriter writer, List<String> strings) throws IOException {
        boolean first = true;
        for (String string : strings) {
            if (!first)
                writer.write(',');
            else
                first = false;

            writer.print('"');
            writer.print(string.replaceAll("\"", "\"\""));
            writer.print('"');
        }
        writer.print("\n");
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

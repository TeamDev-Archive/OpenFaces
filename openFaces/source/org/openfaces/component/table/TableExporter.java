/*
 * OpenFaces - JSF Component Library 2.0
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

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Dmitry Pikhulya
 */
public abstract class TableExporter {
    public void sendResponse(FacesContext context, TableData tableData, String fileName) {
        StringWriter stringWriter = new StringWriter();
        writeFileContent(tableData, stringWriter);
        String csvFile = stringWriter.toString();

        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setContentType(getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        PrintWriter responseWriter;
        try {
            responseWriter = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        responseWriter.write(csvFile);
        responseWriter.close();
        context.responseComplete();
    }

    protected abstract String getContentType();

    protected abstract void writeFileContent(TableData tableData, StringWriter stringWriter);

    public abstract String getDefaultFileExtension();
}

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Dmitry Pikhulya
 */
public abstract class TableExporter {
    public void sendResponse(FacesContext context, TableData tableData, String fileName) {
        boolean binaryContent = isBinaryContent();
        StringWriter stringWriter = !binaryContent ? new StringWriter() : null;
        ByteArrayOutputStream byteArrayOutputStream = binaryContent ? new ByteArrayOutputStream() : null;
        PrintWriter printWriter = new PrintWriter(stringWriter);
        writeFileContent(tableData, printWriter, byteArrayOutputStream);
        printWriter.flush();

        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setContentType(getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        if (!binaryContent) {
            String stringFileContent = stringWriter.toString();
            PrintWriter responseWriter;
            try {
                responseWriter = response.getWriter();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            responseWriter.write(stringFileContent);
            responseWriter.close();
        } else {
            byte[] binaryFileContent = byteArrayOutputStream.toByteArray();
            OutputStream responseOutputStream;
            try {
                responseOutputStream = response.getOutputStream();
                responseOutputStream.write(binaryFileContent);
                responseOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        context.responseComplete();
    }

    protected abstract String getContentType();

    protected abstract boolean isBinaryContent();
    protected abstract void writeFileContent(TableData tableData, PrintWriter writer, OutputStream outputStream);

    public abstract String getDefaultFileExtension();
}

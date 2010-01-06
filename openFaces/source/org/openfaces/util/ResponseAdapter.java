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
package org.openfaces.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author Eugene Goncharov
 */
public class ResponseAdapter implements AbstractResponseFacade {
    private String contentType;
    private OutputStream outputStream;
    private Writer writer;
    private String characterEncoding;

    public ResponseAdapter() {
    }

    public ResponseAdapter(ResponseFacade response) {
        characterEncoding = response.getCharacterEncoding();
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public Writer getWriter() throws IOException {
        if (writer == null) {
            writer = new StringWriter(1000);
        }
        return writer;
    }

    public OutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = new ByteArrayOutputStream(1000);
        }
        return outputStream;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String encoding) {
        characterEncoding = encoding;
    }
}

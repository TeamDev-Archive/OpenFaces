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
package org.openfaces.ajax.plugins;

import org.openfaces.util.ResponseWrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * @author Kharchenko
 */
public class AjaxPluginResponseWrapper extends HttpServletResponseWrapper {
    private PrintWriter writer;
    private final ByteArrayOutputStream stream;

    public AjaxPluginResponseWrapper(HttpServletResponse httpServletResponse) {
        super(httpServletResponse);
        stream = new ByteArrayOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            // TODO [sanders] (Apr 2, 2009, 10:16 PM): Consider wrapping with BufferedWriter
            OutputStreamWriter osw = new OutputStreamWriter(stream, Charset.forName(getCharacterEncoding()));
            writer = new PrintWriter(osw, true);
        }
        return writer;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ResponseWrapper.ServletOutputStream(stream);
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return stream;
    }

}


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

package org.openfaces.ajax;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Eugene Goncharov
 */
final class ServletStreamWriter extends ServletOutputStream {

    private final PrintWriter writer;

    private final byte[] buff = new byte[1024];

    private int point;

    private String charset;

    public ServletStreamWriter(PrintWriter writer, String charset) {
        super();
        this.writer = writer;
        this.charset = charset;
        this.point = 0;
    }

    public ServletStreamWriter(PrintWriter writer) {
        this.writer = writer;
        this.point = 0;
    }

    @Override
    public void write(int b) throws IOException {
        buff[point++] = (byte) b;
        if (point == buff.length) {
            String string = null == charset ? new String(buff) : new String(buff, charset);
            writer.write(string);
        }
    }

    /**
     * @return Returns the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param charset The charset to set.
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /* (non-Javadoc)
    * @see java.io.OutputStream#close()
    */
    @Override
    public void close() throws IOException {
        this.flush();
        super.close();
    }

    /* (non-Javadoc)
    * @see java.io.OutputStream#flush()
    */
    @Override
    public void flush() throws IOException {
        if (point > 0) {
            String string = null == charset ? new String(buff, 0, point) : new String(buff, 0, point, charset);
            writer.write(string);
            point = 0;
        }
    }

    /* (non-Javadoc)
    * @see java.io.OutputStream#write(byte[], int, int)
    */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.flush();
        String string = null == charset ? new String(b, off, len) : new String(b, off, len, charset);
        writer.write(string);
    }
}


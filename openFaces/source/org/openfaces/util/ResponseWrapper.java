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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrew Palval
 */
public class ResponseWrapper extends HttpServletResponseWrapper {
    private PrintWriter outWriter;
    private ByteArrayOutputStream outStream;
    private String redirectLocation;
    private HashMap<String, String> headers = new HashMap<String, String>();
    private List<Cookie> cookies = new ArrayList<Cookie>();

    public ResponseWrapper(HttpServletResponse httpServletResponse) {
        super(httpServletResponse);
        outStream = new ByteArrayOutputStream(1000);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outWriter == null) {
            OutputStreamWriter writer = new OutputStreamWriter(outStream, Charset.forName(getCharacterEncoding()));
            outWriter = new PrintWriter(writer, true);
        }
        return outWriter;
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
        super.addCookie(cookie);
    }

    public Collection<Cookie> getCookies() {
        return cookies;
    }

    @Override
    public void addHeader(String name, String value) {
        if ("Content-Type".equals(name)) {
            setContentType(value);
        }

        headers.put(name, value);
        super.addHeader(name, value);
    }

    /* (non-Javadoc)
    * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
    */
    @Override
    public void setHeader(String name, String value) {
        // HACK - weblogic do not use setContentType, instead directly set header !
        if ("Content-Type".equals(name)) {
            setContentType(value);
        }

        headers.put(name, value);
        super.setHeader(name, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void sendRedirect(String redirectLocation) throws IOException {
        // TODO Auto-generated method stub
        this.redirectLocation = redirectLocation;
        super.sendRedirect(redirectLocation);
    }

    public String getRedirectLocation() {
        return redirectLocation;
    }


    /* (non-Javadoc)
    * @see javax.servlet.ServletResponseWrapper#reset()
    */
    public void resetResponse() {
        this.resetBuffer();
        OutputStreamWriter writer = new OutputStreamWriter(outStream, Charset.forName(getCharacterEncoding()));
        outWriter = new PrintWriter(writer, true);
        outStream = new ByteArrayOutputStream(1000);
        super.reset();

        Map<String, String> headers = getHeaders();
        for (Map.Entry<String, String> key : headers.entrySet()) {
            super.setHeader(key.getKey(), key.getValue());
        }
    }

    @Override
    public javax.servlet.ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream(outStream);
    }

    public String getOutputAsString() {
        flushStreams();
        try {
            String responseString = outStream.toString(getCharacterEncoding());
            return responseString;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getOutputAsByteArray() {
        return outStream.toByteArray();
    }

    public int getOutputSize() {
        return outStream.size();
    }

    private void flushStreams() {
        if (outWriter != null)
            outWriter.flush();
        try {
            outStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static class ServletOutputStream extends javax.servlet.ServletOutputStream {
        private ByteArrayOutputStream outStream;

        public ServletOutputStream(ByteArrayOutputStream outStream) {
            this.outStream = outStream;
        }

        public void write(int b) throws IOException {
            outStream.write(b);
        }

        public void write(byte[] bytes) throws IOException {
            outStream.write(bytes);
        }

        public void write(byte[] bytes, int off, int len) {
            outStream.write(bytes, off, len);
        }
    }
}

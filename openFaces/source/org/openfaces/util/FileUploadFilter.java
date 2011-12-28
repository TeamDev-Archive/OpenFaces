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

package org.openfaces.util;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class FileUploadFilter implements Filter {

    private ServletContext servletContext;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest hRequest = (HttpServletRequest) request;

        //Check whether we're dealing with a multipart request
        boolean isMultipart = (hRequest.getHeader("content-type") != null &&
                hRequest.getHeader("content-type").contains("multipart/form-data"));

        if (!isMultipart) {
            chain.doFilter(request, response);
        } else {
            //We're dealing with a multipart request - we have to wrap the request.
            String tempDir = servletContext.getInitParameter("org.openfaces.fileUpload.tempDir");
            if (tempDir == null) {
                tempDir = "C:\\temp";
            }

            String maxSizeString = servletContext.getInitParameter("org.openfaces.fileUpload.maxFileSize");
            long maxSizeOfFile = (maxSizeString != null) ? Long.parseLong(maxSizeString) * 1024 : Long.MAX_VALUE;

            FileUploadRequestWrapper wrapper = new FileUploadRequestWrapper(hRequest, tempDir, maxSizeOfFile);
            request.setAttribute("fileUploadRequest", true);
            chain.doFilter(wrapper, response);
        }
    }

    public void destroy() {
    }

    public void init(FilterConfig config) throws ServletException {
        servletContext = config.getServletContext();
    }


}
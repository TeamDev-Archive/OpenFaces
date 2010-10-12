/*
 * OpenFaces - JSF Component Library 3.0
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

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kharchenko
 */
public class ResourceFilter_ {
    public static final String INTERNAL_RESOURCE_PATH = "/openFacesResources/";
    public static final String RUNTIME_INIT_LIBRARY_PATH = INTERNAL_RESOURCE_PATH + "of_ajaxInitLib/";

    private static final String RESOURCE_CACHE = ResourceFilter_.class.getName() + ".resourcesCache";

    private static final String RESET_CSS_CONTEXT_PARAM = "org.openfaces.resetCSS";
    private static final String RESET_CSS_CONTEXT_PARAM_DEFAULT_VALUE = "default";


    /**
     * Returns path of the requested resource within servlet context after any container decoding, such as removing
     * jsessionid suffixes.
     */
    private String getDecodedResourcePath(HttpServletRequest request) {
        String uri = request.getPathInfo();
        if (uri == null)
            uri = request.getRequestURI();
        return uri;
    }

    private byte[] getStreamAsByteArray(InputStream stream) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        BufferedInputStream bStream = new BufferedInputStream(stream);
        byte[] array = new byte[1024];
        int bytesRead = 0;
        while (bytesRead != -1) {
            bytesRead = bStream.read(array);
            if (bytesRead != -1) {
                byteStream.write(array, 0, bytesRead);
            }
        }
        return byteStream.toByteArray();
    }

    private Map getCache(ServletContext servletContext) {
        Map cache = (Map) servletContext.getAttribute(RESOURCE_CACHE);
        if (cache == null) {
            cache = new HashMap();
            servletContext.setAttribute(RESOURCE_CACHE, cache);
        }
        return cache;
    }


    private void renderResponseWithHeader(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            String responseString) throws IOException {
        int headerInsertionPos;
        boolean hasHeaderTag = false;

        StringInspector responseStringInspector = new StringInspector(responseString);
        if ((headerInsertionPos = responseStringInspector.indexOfIgnoreCase("<head")) >= 0) {
            int headerOpeningTagEnd = responseString.indexOf('>', headerInsertionPos);
            if (headerOpeningTagEnd != -1) {
                headerInsertionPos = headerOpeningTagEnd + 1;
                hasHeaderTag = true;
            } else
                headerInsertionPos = 0;
        } else {
            headerInsertionPos = responseStringInspector.indexOfIgnoreCase("<body");
            if (headerInsertionPos == -1) {
                int htmlStart = responseStringInspector.indexOfIgnoreCase("<html");
                if (htmlStart != -1)
                    headerInsertionPos = responseString.indexOf('>', htmlStart) + 1;
                else
                    headerInsertionPos = 0;
            }
        }

        PrintWriter writer = servletResponse.getWriter();
        if (headerInsertionPos > 0) {
            writer.write(responseString.substring(0, headerInsertionPos));
        }

        boolean skipHeaderInsertion = servletRequest.getParameterMap().containsKey("skipHeaderInsertion");
        if (!skipHeaderInsertion) {
            if (!hasHeaderTag) {
                writer.write("<head>");
            }

            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String resetCss = httpServletRequest.getSession().getServletContext().getInitParameter(RESET_CSS_CONTEXT_PARAM);
            if (resetCss != null) {
                resetCss = resetCss.trim();
                String resetCssUrl;

                if (resetCss.equals(RESET_CSS_CONTEXT_PARAM_DEFAULT_VALUE)) {
                    resetCssUrl = httpServletRequest.getContextPath() + INTERNAL_RESOURCE_PATH + "org/openfaces/renderkit/reset" + "-" + Resources.getVersionString() + ".css";
                } else if (!resetCss.startsWith("/") && !resetCss.startsWith("http://")) {
                    resetCssUrl = httpServletRequest.getContextPath() + "/" + resetCss;
                } else {
                    resetCssUrl = resetCss;
                }

                writer.print("<link type=\"text/css\" href=\"");
                writer.print(resetCssUrl);
                writer.println("\" rel=\"stylesheet\"/>");
            }

            writer.print("<link type=\"text/css\" href=\"");
            String defaultCssUrl = httpServletRequest.getContextPath() + INTERNAL_RESOURCE_PATH + Resources.META_INF_RESOURCES_ROOT.substring(1) +
                    "default" + "-" + Resources.getVersionString() + ".css"; // render default.css
            writer.print(defaultCssUrl);
            writer.println("\" rel=\"stylesheet\"/>");

            List<String> jsLibraries = (List<String>) servletRequest.getAttribute(Resources.HEADER_JS_LIBRARIES);

            if (jsLibraries != null) {
                for (String jsFileUrl : jsLibraries) {
                    writer.print("<script src=\"");
                    writer.print(jsFileUrl);
                    writer.println("\" type=\"text/javascript\"></script>");
                }
            }


            if (!hasHeaderTag) {
                writer.write("</head>");
            }
        }

        StringInspector restOfResponse;
        if (headerInsertionPos == -1)
            restOfResponse = responseStringInspector;
        else
            restOfResponse = responseStringInspector.substring(headerInsertionPos);

        StringBuilder onLoadScripts = (StringBuilder) servletRequest.getAttribute(Rendering.ON_LOAD_SCRIPTS_KEY);
        if (onLoadScripts == null)
            onLoadScripts = new StringBuilder();
        else {
            if (onLoadScripts.length() > 0)
                onLoadScripts.insert(0, "<script type=\"text/javascript\">\n").append("\n</script>\n");
        }

        if (onLoadScripts.length() == 0) {
            writer.write(restOfResponse.toString());
            return;
        }
        int bodyCloseTagPos = restOfResponse.indexOfIgnoreCase("</body>");
        if (bodyCloseTagPos == -1) {
            writer.write(restOfResponse.toString());
            writer.write(onLoadScripts.toString());
        } else {
            String bodyContents = restOfResponse.toString().substring(0, bodyCloseTagPos);
            writer.write(bodyContents);
            writer.write(onLoadScripts.toString());
            String bodyCloseTag = restOfResponse.toString().substring(bodyCloseTagPos);
            writer.write(bodyCloseTag);
        }
    }

    private void processRuntimeInitLibrary(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestUri = getDecodedResourcePath(request);
        HttpSession session = request.getSession();
        int prefixIdx = requestUri.indexOf(RUNTIME_INIT_LIBRARY_PATH);
        String sessionMapKey = requestUri.substring(prefixIdx);
        String initializationScripts = (String) session.getAttribute(sessionMapKey);
        if (initializationScripts == null) {
            Enumeration sessionAttributes = session.getAttributeNames();
            while (sessionAttributes.hasMoreElements()) {
                String sessionAttribute = (String) sessionAttributes.nextElement();
                if (sessionAttribute.indexOf(sessionMapKey) != -1) {
                    initializationScripts = (String) session.getAttribute(sessionAttribute);
                    session.removeAttribute(sessionAttribute);
                    break;
                }
            }
        }
        session.removeAttribute(requestUri);
        if (initializationScripts != null && initializationScripts.length() > 0) {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(initializationScripts.getBytes());
            outputStream.close();
        } else {
            //todo: what to do?
        }
    }

}

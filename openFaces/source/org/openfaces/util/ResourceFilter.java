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

import org.openfaces.ajax.AjaxViewHandler;
import org.openfaces.renderkit.output.DynamicImageRenderer;

import javax.faces.application.ViewExpiredException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kharchenko
 */
public class ResourceFilter implements Filter {
    public static final String INTERNAL_RESOURCE_PATH = "/openFacesResources/";
    public static final String RUNTIME_INIT_LIBRARY_PATH = INTERNAL_RESOURCE_PATH + "of_ajaxInitLib/";

    private static final String PROCESSING_FILTER = "OF:ResourceFilter.doFilter_executing";
    private static final long DEFAULT_LAST_MODIFIED_TIME = System.currentTimeMillis();
    private static final String RESOURCE_CACHE = ResourceFilter.class.getName() + ".resourcesCache";

    private static final String RESET_CSS_CONTEXT_PARAM = "org.openfaces.resetCSS";
    private static final String RESET_CSS_CONTEXT_PARAM_DEFAULT_VALUE = "default";

    private ServletContext servletContext;

    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        if (servletRequest.getAttribute(PROCESSING_FILTER) != null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        servletRequest.setAttribute(PROCESSING_FILTER, "true");

        HttpServletResponse response = (HttpServletResponse) servletResponse;

        boolean internalResourceRequested = isInternalResourceRequested((HttpServletRequest) servletRequest);
        // todo: runtime-library-init-script is a special case of internal resource (according to url) - refactor this
        boolean runtimeLibraryInitScriptsRequested = isRuntimeInitScriptsRequested((HttpServletRequest) servletRequest);
        if (runtimeLibraryInitScriptsRequested) {
            processRuntimeInitLibrary((HttpServletRequest) servletRequest, response);
        } else if (internalResourceRequested) {
            processInternalResource((HttpServletRequest) servletRequest, response, filterChain, servletContext);
        } else if (isJavaScriptLibraryRequested((HttpServletRequest) servletRequest)) {
//      int sizeBefore = response.getOutputSize();
            filterChain.doFilter(servletRequest, response);
//      int sizeAfter = response.getOutputSize();
//            boolean jsWasServed = true;//sizeAfter - sizeBefore > 0;
//            if (jsWasServed) {
            // this (jsWasServed) check is needed for a case when the filter returns the 304 "Not Modified" Http result
            // without writing the output to the stream (was reproduced on WebLogic Portal for its own js-files).
            // we can't detect the http result so we check if anything was written to the outputStream
            String libName = getDecodedResourcePath((HttpServletRequest) servletRequest);
            ServletOutputStream outputStream = response.getOutputStream();
            appendJavaScriptLoadingScript(libName, outputStream);
//            }
        } else {
            ResponseWrapper responseWrapper = new ResponseWrapper(response);
            try {
                filterChain.doFilter(servletRequest, responseWrapper);
            } catch (ServletException e) {
                if (e.getRootCause() instanceof Exception) {
                    Exception exception = (Exception) e.getRootCause();

                    while (exception instanceof ServletException) {
                        exception = (Exception) ((ServletException) exception).getRootCause();
                    }

                    if (exception instanceof ViewExpiredException && AjaxUtil.isAjaxRequest(RequestFacade.getInstance(servletRequest))) {
                        String requestURI = getDecodedResourcePath((HttpServletRequest) servletRequest);
                        String ajaxParameters;
                        StringBuilder stringBuffer = new StringBuilder();
                        Enumeration parameters = servletRequest.getParameterNames();
                        while (parameters.hasMoreElements()) {
                            String parameterName = (String) parameters.nextElement();
                            if (parameterName.equalsIgnoreCase(AjaxUtil.PARAM_RENDER)
                                    || parameterName.equalsIgnoreCase(AjaxUtil.AJAX_REQUEST_MARKER))
                                stringBuffer.append(parameterName).append("=").
                                        append(servletRequest.getParameter(parameterName)).append("&");
                        }

                        ajaxParameters = stringBuffer.toString();

                        if (ajaxParameters != null && ajaxParameters.length() > 0) {
                            String redirectURL = requestURI + "?" + ajaxParameters + "of_sessionExpiration=true";

                            String url = response.encodeRedirectURL(redirectURL);
                            response.sendRedirect(url);
                        }
                    } else {
                        throw new ServletException(e); // todo: shouldn't it be just "throw e" ? needs to be tested.
                    }
                }
            }
            // todo (optimize): seems that servletResponse.getContentType() can be used to skip trying to process header includes for text/html pages
            String responseString = responseWrapper.getOutputAsString();

            String errorOccured = (String) servletRequest.getAttribute(AjaxViewHandler.ERROR_OCCURRED);
            if (errorOccured != null) {
                String errorMessage = (String) servletRequest.getAttribute(AjaxViewHandler.ERROR_MESSAGE_HEADER);
                response.setHeader(AjaxViewHandler.ERROR_MESSAGE_HEADER, errorMessage);
                servletResponse.setContentType("text/xml;charset=UTF-8");
                return;
            }

            String sessionExpiredResponse = (String) servletRequest.getAttribute(AjaxViewHandler.SESSION_EXPIRED_RESPONSE);
            if (sessionExpiredResponse != null) {
                responseString = sessionExpiredResponse;
            }

            if (responseString.length() == 0)
                return;

            if (Resources.isHeaderIncludesRegistered(servletRequest)) {
                renderResponseWithHeader(servletRequest, servletResponse, responseString);
                return;
            }

            if (sessionExpiredResponse != null) {
                servletResponse.setContentType("text/xml;charset=UTF-8");
                String location = (String) servletRequest.getAttribute(AjaxViewHandler.LOCATION_HEADER);
                if (servletResponse instanceof HttpServletResponse) {
                    response.setHeader(AjaxViewHandler.LOCATION_HEADER, location);
                }
                servletResponse.getOutputStream().write(responseString.getBytes());
                return;
            }
            servletResponse.getOutputStream().write(responseWrapper.getOutputAsByteArray());
        }

    }

    public void destroy() {
    }

    private void appendJavaScriptLoadingScript(String jsLibName, OutputStream os) throws IOException {
        String result = "\n\n//AUTO GENERATED CODE\n\nwindow['_of_loadedLibrary:" + jsLibName + "'] = true;";
        os.write(result.getBytes("UTF-8"));
    }

    private void processInternalResource(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, ServletContext servletContext) throws IOException, ServletException {
        String uri = getDecodedResourcePath(request);

        int packageStartIndex = uri.indexOf(INTERNAL_RESOURCE_PATH) + INTERNAL_RESOURCE_PATH.length() - 1;
        String resourcePathWithVersion = uri.substring(packageStartIndex);
        String resourcePath;
        boolean isDynamicImageResource = Rendering.isDynamicResource(uri);
        if (!isDynamicImageResource) {
            int versionPrefixIdx = resourcePathWithVersion.lastIndexOf("-");
            if (versionPrefixIdx != -1)
                resourcePath = resourcePathWithVersion.substring(0, versionPrefixIdx) +
                        resourcePathWithVersion.substring(resourcePathWithVersion.lastIndexOf("."));
            else {
                // some rare internal resources are allowed to come without version,
                // e.g. for images referred to from default.css (see clear.gif as an example)
                resourcePath = resourcePathWithVersion;
            }
        } else {
            resourcePath = resourcePathWithVersion;
        }
        InputStream inputStream;

        if (isDynamicImageResource) {
            inputStream = getDynamicImageAsInputStream(request, servletContext);
            response.setContentType(getImageContentType(uri));
        } else {
            if (!resourcePath.startsWith("/org/openfaces")) {
                // SECURITY ISSUE: DO NOT LET TO ACCESS /openFacesResources/ HOME AS IT PROVIDES SERVER'S FILES
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            URL resourceURL = Rendering.class.getResource(resourcePath);
            if (resourceURL == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            File resourceFile = getResourceFile(resourceURL);
            long lastModified = resourceFile != null ? resourceFile.lastModified() : -1;

            if (request.getDateHeader("If-Modified-Since") != -1) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }

            if (lastModified != -1) {
                lastModified -= lastModified % 1000;
                long requestModifiedSince = request.getDateHeader("If-Modified-Since");
                if (lastModified <= requestModifiedSince) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
            }
            byte[] resource = getCachedResource(servletContext, resourcePath);
            if (resource == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            inputStream = new ByteArrayInputStream(resource);

            if (resourcePath.endsWith(".js")) {
                response.setContentType("text/javascript");
                // this can be added after GZip of all javascript files
//        response.setContentType("application/x-javascript");
//        response.addHeader("Content-Encoding", "gzip");
            } else if (resourcePath.endsWith(".css"))
                response.setContentType("text/css");
            else if (resourcePath.endsWith(".gif"))
                response.setContentType("image/gif");
            else if (resourcePath.endsWith(".png"))
                response.setContentType("image/png");
            else if (resourcePath.endsWith(".jpg") || resourcePath.endsWith(".jpeg"))
                response.setContentType("image/jpeg");
            else if (resourcePath.endsWith(".xml") || resourcePath.endsWith(".xsl"))
                response.setContentType("text/xml");

            response.setDateHeader("Last-Modified", lastModified != -1 ? lastModified : DEFAULT_LAST_MODIFIED_TIME);
            response.setHeader("Cache-Control", "must-revalidate");
//      response.setHeader("ETag", String.valueOf(lastModified != -1 ? lastModified : DEFAULT_LAST_MODIFIED_TIME));
        }

        if (inputStream != null) {
            OutputStream outputStream = response.getOutputStream();
            int result;
            while ((result = inputStream.read()) != -1) {
                outputStream.write(result);
            }
            if (uri.endsWith(".js"))
                appendJavaScriptLoadingScript(getDecodedResourcePath(request), outputStream);
            outputStream.close();
        } else {
            filterChain.doFilter(request, response);
        }
    }

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
        int readed = 0;
        while (readed != -1) {
            readed = bStream.read(array);
            if (readed != -1) {
                byteStream.write(array, 0, readed);
            }
        }
        return byteStream.toByteArray();
    }

    private byte[] getCachedResource(ServletContext servletContext, String resourcePath) throws IOException {
        Map cache = getCache(servletContext);
        byte[] resource = (cache.get(resourcePath) != null)
                ? ((byte[]) cache.get(resourcePath))
                : null;
        if (resource == null) {
            InputStream inputStream = Rendering.class.getResourceAsStream(resourcePath);
            try {
                resource = getStreamAsByteArray(inputStream);
                cache.put(resourcePath, resource);
            } finally {
                inputStream.close();
            }
        }
        return resource;
    }

    private Map getCache(ServletContext servletContext) {
        Map cache = (Map) servletContext.getAttribute(RESOURCE_CACHE);
        if (cache == null) {
            cache = new HashMap();
            servletContext.setAttribute(RESOURCE_CACHE, cache);
        }
        return cache;
    }

    private File getResourceFile(URL resourceUrl) {
        String protocol = resourceUrl.getProtocol();
        if (protocol.equals("file"))
            return new File(resourceUrl.getPath());

        if (!protocol.equals("jar"))
            return null;

        String path = resourceUrl.getPath();
        if (!path.startsWith("file"))
            return null;

        path = path.substring("file".length() + 1);
        path = path.substring(0, path.indexOf("!"));
        return new File(path);
    }

    private String getImageContentType(String uri) {
        if (uri.toLowerCase().indexOf(".gif") > -1)
            return "image/gif";

        if (uri.toLowerCase().indexOf(".png") > -1)
            return "image/png";

        if ((uri.toLowerCase().indexOf(".jpg") > -1) || uri.toLowerCase().indexOf(".jpeg") > -1)
            return "image/jpeg";

        return "image";
    }

    private InputStream getDynamicImageAsInputStream(HttpServletRequest request, ServletContext servletContext) {
        String sid = request.getParameter("id");
        if (sid == null) {
            servletContext.log("Couldn't retrieve dynamic image id from request: " + getDecodedResourcePath(request));
            return null;
        }

        HttpSession session = request.getSession();
        DynamicImagePool pool = (DynamicImagePool) session.getAttribute(DynamicImageRenderer.IMAGE_POOL);
        if (pool == null) {
            servletContext.log("Couldn't retrieve image pool on processing a dynamic image request");
            return null;
        }

        byte[] image = pool.getImage(sid);
        if (image == null) {
            servletContext.log("Image was not found in pool: " + sid);
            return null;
        }
        return new ByteArrayInputStream(image);
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
        String defaultCssUrl = httpServletRequest.getContextPath() + INTERNAL_RESOURCE_PATH + "org/openfaces/renderkit/default" + "-" + Resources.getVersionString() + ".css"; // render default.css
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

    private boolean isRuntimeInitScriptsRequested(HttpServletRequest request) {
        return getDecodedResourcePath(request).indexOf(RUNTIME_INIT_LIBRARY_PATH) != -1;
    }

    private boolean isJavaScriptLibraryRequested(HttpServletRequest request) {
        String requestURI = getDecodedResourcePath(request);
        return requestURI.toLowerCase().endsWith(".js");
    }

    /**
     * Checks whether request is for internal resource or not.
     *
     * @param request incoming request.
     * @return <code>true</code> - if request is for internal resource. <code>false</code> otherwise.
     */
    private boolean isInternalResourceRequested(HttpServletRequest request) {
        return getDecodedResourcePath(request).indexOf(INTERNAL_RESOURCE_PATH) != -1;
    }
}

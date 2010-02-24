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
package org.openfaces.ajax.plugins.tomahawk113;

import org.openfaces.ajax.plugins.AbstractAjaxPlugin;
import org.openfaces.ajax.plugins.AjaxPluginIncludes;
import org.openfaces.ajax.plugins.AjaxPluginResponseWrapper;
import org.openfaces.util.Log;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Kharchenko
 */
public class Tomahawk113AjaxPlugin extends AbstractAjaxPlugin {
    private static final String ADD_RESOURCE_FACTORY_CLASS_NAME = "org.apache.myfaces.renderkit.html.util.AddResourceFactory";
    private static final String ADD_RESOURCE_CLASS_NAME = "org.apache.myfaces.renderkit.html.util.AddResource";
    private static final String EXTENSIONS_RESPONSE_WRAPPER_CLASS_NAME = "org.apache.myfaces.webapp.filter.ExtensionsResponseWrapper";
    private static final String PARSE_RESPONSE_METHOD_NAME = "parseResponse";
    private static final String GET_INSTANCE_METHOD_NAME = "getInstance";
    private static final String WRITE_WITH_FULL_HEADER_METHOD_NAME = "writeWithFullHeader";
    private static final String WRITE_RESPONSE_METHOD_NAME = "writeResponse";

    @Override
    public AjaxPluginIncludes getIncludes(HttpServletRequest request, AjaxPluginResponseWrapper responseWrapper) {
        String renderedResponse = getRenderedResponseFromLibrary(request, responseWrapper);
        return extractIncludes(renderedResponse);
    }

    private String getRenderedResponseFromLibrary(HttpServletRequest request, AjaxPluginResponseWrapper response) {
        try {
            Class addResFactoryClass = Class.forName(ADD_RESOURCE_FACTORY_CLASS_NAME);
            Class addResClass = Class.forName(ADD_RESOURCE_CLASS_NAME);
            Method getInstanceMethod = addResFactoryClass.getDeclaredMethod(GET_INSTANCE_METHOD_NAME, HttpServletRequest.class);
            Object addResourceInstance = getInstanceMethod.invoke(addResFactoryClass, request);
            Method parseResponseMethod = addResClass.getDeclaredMethod(PARSE_RESPONSE_METHOD_NAME,
                    HttpServletRequest.class, String.class, HttpServletResponse.class);
            Method writeWithFullHeaderMethod = addResClass.getDeclaredMethod(WRITE_WITH_FULL_HEADER_METHOD_NAME,
                    HttpServletRequest.class, HttpServletResponse.class);
            Method writeResponseMethod = addResClass.getDeclaredMethod(WRITE_RESPONSE_METHOD_NAME,
                    HttpServletRequest.class, HttpServletResponse.class);

            Class extRespWrapperClass = Class.forName(EXTENSIONS_RESPONSE_WRAPPER_CLASS_NAME);
            Constructor extRespWrapperConstructor = extRespWrapperClass.getConstructor(HttpServletResponse.class);
            Object extRespWrapperInstance = extRespWrapperConstructor.newInstance(response);
            Method getOutputStreamMethod = extRespWrapperClass.getDeclaredMethod("getOutputStream");
            ServletOutputStream stream = (ServletOutputStream) getOutputStreamMethod.invoke(extRespWrapperInstance);
            stream.print("<html><head></head><body></body></html>");

            parseResponseMethod.invoke(addResourceInstance, request, extRespWrapperInstance.toString(), response);
            writeWithFullHeaderMethod.invoke(addResourceInstance, request, response);
            writeResponseMethod.invoke(addResourceInstance, request, response);
            response.getWriter().flush();
            ByteArrayOutputStream byteArrayOutputStream = response.getByteArrayOutputStream();
            return byteArrayOutputStream.toString(response.getCharacterEncoding());
        } catch (ClassNotFoundException e) {
            // absence of Tomahawk is a valid case
            return null;
        } catch (NoSuchMethodException e) {
            Log.log("", e);
            return null;
        } catch (IllegalAccessException e) {
            Log.log("", e);
            return null;
        } catch (InvocationTargetException e) {
            Log.log("", e);
            return null;
        } catch (InstantiationException e) {
            Log.log("", e);
            return null;
        } catch (IOException e) {
            Log.log("", e);
            return null;
        }
    }
}

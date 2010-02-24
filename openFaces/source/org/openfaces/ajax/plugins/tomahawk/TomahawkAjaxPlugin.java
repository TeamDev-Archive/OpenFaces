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
package org.openfaces.ajax.plugins.tomahawk;

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
public class TomahawkAjaxPlugin extends AbstractAjaxPlugin {
    private final static String ADD_RESOURCE_CLASS_NAME_1_1_0_OR_1_1_1 = "org.apache.myfaces.component.html.util.AddResource";
    private final static String WRITE_WITH_FULL_HEADER_METHOD_NAME = "writeWithFullHeader";
    private final static String EXT_RESPONSE_WRAPPER_CLASS_NAME_1_1_0_OR_1_1_1 = "org.apache.myfaces.component.html.util.ExtensionsResponseWrapper";

    @Override
    public AjaxPluginIncludes getIncludes(HttpServletRequest request, AjaxPluginResponseWrapper responseWrapper) {
        String renderedResponse = getRenderedResponseOutput(request, responseWrapper);
        return extractIncludes(renderedResponse);
    }

    private String getRenderedResponseOutput(HttpServletRequest request, AjaxPluginResponseWrapper response) {
        try {
            Class<?> addResourceClass = Class.forName(ADD_RESOURCE_CLASS_NAME_1_1_0_OR_1_1_1);

            Class<?> extResponseWrapperClass = Class.forName(EXT_RESPONSE_WRAPPER_CLASS_NAME_1_1_0_OR_1_1_1);

            Method writeWithFullHeader = addResourceClass.getDeclaredMethod(WRITE_WITH_FULL_HEADER_METHOD_NAME,
                    HttpServletRequest.class,
                    extResponseWrapperClass,
                    HttpServletResponse.class);
            Constructor<?> extRespWrapperConstructor
                    = extResponseWrapperClass.getConstructor(HttpServletResponse.class);
            Object extRespWrapperInstance = extRespWrapperConstructor.newInstance(response);

            Method getOutputStreamMethod = extResponseWrapperClass.getDeclaredMethod("getOutputStream");
            ServletOutputStream stream = (ServletOutputStream) getOutputStreamMethod.invoke(extRespWrapperInstance);
            stream.print("<html><head></head><body></body></html>");

            writeWithFullHeader.invoke(addResourceClass, request, extRespWrapperInstance, response);
            response.getWriter().flush();
            ByteArrayOutputStream byteArrayOutputStream = response.getByteArrayOutputStream();
            String result = byteArrayOutputStream.toString(response.getCharacterEncoding());
            return result;

        } catch (ClassNotFoundException e) {
            // absence of Tomahawk is a valid case
            return null;
        } catch (NoSuchMethodException e) {
            Log.log("", e);
            return null;
        } catch (InstantiationException e) {
            Log.log("", e);
            return null;
        } catch (IllegalAccessException e) {
            Log.log("", e);
            return null;
        } catch (InvocationTargetException e) {
            Log.log("", e);
            return null;
        } catch (IOException e) {
            Log.log("", e);
            return null;
        }
    }

}

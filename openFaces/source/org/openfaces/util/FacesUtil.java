/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.util;

import javax.el.ELContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 *
 * Class intended to retrieve various parameters from request. 
 *
 * @author Dmitry Pikhulya
 */
public class FacesUtil {
    private static final SimpleDateFormat DATE_PARAMS_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private FacesUtil() {
    }

    /**
     * This method retrieve value from request map by key.
     *
     * @see javax.faces.context.ExternalContext#getRequestMap() More about request map
     * @param requestMapKey - key
     * @return corresponding value from request map
     */
    public static Object getRequestMapValue(String requestMapKey) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Map requestMap = externalContext.getRequestMap();
        Object value = requestMap.get(requestMapKey);
        return value;
    }

    public static <T>T getRequestMapValue(String requestMapKey, Class<T> cls) {
        Object value = getRequestMapValue(requestMapKey);
        if (value == null)
            return null;
        if (!cls.isAssignableFrom(value.getClass()))
            throw new ClassCastException("Improper type for request map value " + requestMapKey + ". Requested " + cls.getName() + ", but was " + value.getClass());
        return (T) value;
    }


    public static <T>T var(String varName, Class<T> cls) {
        Object value = var(varName);
        if (value == null)
            return null;
        if (!cls.isAssignableFrom(value.getClass()))
            throw new ClassCastException("Improper type for variable " + varName + ". Requested " + cls.getName() + ", but was " + value.getClass());
        return (T) value;
    }

    public static Object var(String varName) {
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        return elContext.getELResolver().getValue(elContext, null, varName);
    }

    /**
     * This method retrieve value from request parameters map by key.
     *
     * @see javax.faces.context.ExternalContext#getRequestParameterMap() More about request parameters map 
     * @param requestParameterMapKey - key
     * @return corresponding value from request parameters map
     */
    public static Object getRequestParameterMapValue(String requestParameterMapKey) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Map requestParameterMap = externalContext.getRequestParameterMap();
        Object value = requestParameterMap.get(requestParameterMapKey);
        return value;
    }

    /**
     * This method retrieve value from request parameters map by key, convert it into {@link java.util.Date Date}
     * using "dd/MM/yyyy HH:mm" format and return. If requested value doesn't represent date, of has another format
     * the {@link java.lang.RuntimeException RuntimeException} is thrown.
     *
     * @see javax.faces.context.ExternalContext#getRequestParameterMap() More about request parameters map
     * @param requestParameterMapKey - key
     * @return corresponding {@link java.util.Date Date} object from request parameters map
     */
    public static Date getRequestParameterMapValueAsDate(String requestParameterMapKey) {
        String paramValue = (String) getRequestParameterMapValue(requestParameterMapKey);
        if (paramValue == null)
            return null;

        Date result;
        try {
            result = DATE_PARAMS_FORMAT.parse(paramValue);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

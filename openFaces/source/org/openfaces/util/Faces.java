/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
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
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * This class contains some utilities that simplify performing operations which are commonly used in JSF application
 * development, such as looking up variable values, components, etc.
 *
 * @author Dmitry Pikhulya
 */
public class Faces {
    private static final SimpleDateFormat DATE_PARAMS_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private Faces() {
    }

    /**
     * Locates a component with the specified client-side Id and class.
     * @param clientId       client-side Id of the component, e.g. form:usersTable
     * @param componentClass expected class of the component whose id is passed to the first parameter, e.g. DataTable.class
     * @return               a component with the specified client id, or null of the component was not found.
     */
    public static <T> T component(String clientId, Class<T> componentClass) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = context.getViewRoot().findComponent(":" + clientId);
        if (component == null) return null;

        if (!componentClass.isAssignableFrom(component.getClass()))
            throw new IllegalArgumentException("A component with the specified client id (" + clientId + ") is of " +
                    "different type (" + component.getClass().getName() + "), than the one that was expected (" +
                    componentClass.getClass().getName() + ")");
        return (T) component;
    }

    /**
     * Returns the value of the specified variable in the current expression context and casts it to the specified class.
     * This method returns the same value as would be returned by the #{varName} expression and is an easy way to get
     * variable value in a backing bean if you can't write the value expression on your page directly and need to
     * implement a more complex logic in the backing bean.
     *
     * @param varName      variable name
     * @param expectedType expected variable type
     * @return variable value
     * @throws ClassCastException if the variable value is not of the expected type
     */
    public static <T> T var(String varName, Class<T> expectedType) throws ClassCastException {
        Object value = var(varName);
        if (value == null)
            return null;
        if (!expectedType.isAssignableFrom(value.getClass()))
            throw new ClassCastException("Improper type for variable " + varName + ". Requested " + expectedType.getName() + ", but was " + value.getClass());
        return (T) value;
    }

    /**
     * Returns the value of the specified variable in the current expression context. This method returns the same value
     * as would be returned by the #{varName} expression and is an easy way to get variable value in a backing bean if
     * you can't write the value expression on your page directly and need to implement a more complex logic in the
     * backing bean.
     *
     * @param varName variable name
     * @return variable value
     * @throws ClassCastException if the variable value is not of the expected type
     */
    public static Object var(String varName) {
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        return elContext.getELResolver().getValue(elContext, null, varName);
    }

    /**
     * Retrieves a value from the request parameters map and converts it to the specified type.
     *
     * @param paramName    - request parameter name
     * @param expectedType - expected parameter type
     * @return corresponding value from request parameters map
     * @see javax.faces.context.ExternalContext#getRequestParameterMap() More about request parameters map
     */
    public static <T> T requestParam(String paramName, Class<T> expectedType) {
        String value = requestParam(paramName);
        if (value == null)
            return null;
        if (expectedType.equals(String.class)) {
            return (T) value;
        } else if (expectedType.equals(Date.class)) {
            try {
                return (T) DATE_PARAMS_FORMAT.parse(value);
            } catch (ParseException e) {
                throw new RuntimeException("Request parameter \"" + paramName + "\" is expected to have date format of \"" +
                        DATE_PARAMS_FORMAT.toPattern() + "\", but the following value couldn't be parsed \"" + value + "\"", e);
            }
        } else if (expectedType.equals(Integer.class)) {
            try {
                return (T) (Integer) Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Request parameter \"" + paramName + "\" is expected to be an integer number " +
                        "string, but the following value was found: \"" + value + "\"", e);
            }
        } else if (expectedType.equals(Boolean.class)) {
            return (T) (Boolean) Boolean.parseBoolean(value);
        } else if (expectedType.equals(Double.class)) {
            try {
                return (T) (Double) Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Request parameter \"" + paramName + "\" is expected to be an double number " +
                        "string, but the following value was found: \"" + value + "\"", e);
            }
        } else
            throw new IllegalArgumentException("Unexpected expectedType value: " + expectedType.getName() + "; string, boolean, int, and double values are supported. Consider parsing a string manually.");
    }

    /**
     * Retrieves a value from the request parameters map.
     *
     * @param requestParameterMapKey - key
     * @return corresponding value from request parameters map
     * @see javax.faces.context.ExternalContext#getRequestParameterMap() More about request parameters map
     */
    public static String requestParam(String requestParameterMapKey) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Map<String, String> requestParameterMap = externalContext.getRequestParameterMap();
        return requestParameterMap.get(requestParameterMapKey);
    }

}

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

import org.openfaces.component.table.*;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class ApplicationParams {
    private static final String PARAM_ORDINAL_TYPES = "org.openfaces.ordinalTypes";
    private static final String PARAM_SUMMARY_FUNCTIONS = "org.openfaces.summaryFunctions";

    private static List<OrdinalType> ordinalTypes;
    private static List<SummaryFunction> summaryFunctions;

    public static List<OrdinalType> getOrdinalTypes() {
        if (ordinalTypes != null) return ordinalTypes;

        ordinalTypes = new ArrayList<OrdinalType>(Arrays.asList(
                new AnyIntegerType(),
                new AnyFloatingPointType(),
                new DateType()
        ));
        ordinalTypes.addAll(0, createInstancesFromClassNamesParam(PARAM_ORDINAL_TYPES, OrdinalType.class));
        return ordinalTypes;
    }

    public static List<SummaryFunction> getSummaryFunctions() {
        if (summaryFunctions != null) return summaryFunctions;

        summaryFunctions = new ArrayList<SummaryFunction>(Arrays.asList(
                new SumFunction(),
                new AvgFunction(),
                new MinFunction(),
                new MaxFunction()
        ));
        summaryFunctions.addAll(createInstancesFromClassNamesParam(PARAM_SUMMARY_FUNCTIONS, SummaryFunction.class));

        // make the "Count" function to be the last one
        summaryFunctions.add(new CountFunction());
        return summaryFunctions;
    }

    private static <E> List<E> createInstancesFromClassNamesParam(String paramName, Class<E> expectedClass) {
        List<E> targetList = new ArrayList<E>();
        String ordinalTypesParam = getApplicationInitParameter(paramName);

        if (ordinalTypesParam == null) return targetList;

        String[] classNames = ordinalTypesParam.split(",");
        for (String className : classNames) {
            className = className.trim();
            Class<?> classByName;
            try {
                classByName = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new FacesException("Invalid value of " + paramName + " application context " +
                        "parameter. Couldn't find class with the name specified: " + className +
                        "; please specify a list of valid fully-qualified class names separated with commas");
            }

            if (!expectedClass.isAssignableFrom(classByName))
                throw new FacesException("Invalid value of " + paramName + " application context " +
                        "parameter. The class specified does not extend the " + expectedClass.getName() +
                        " class.");
            Class<E> verifiedClass = (Class<E>) classByName;
            Constructor<E> defaultConstructor;
            try {
                defaultConstructor = verifiedClass.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new FacesException("All classes specified in the " + paramName + " application " +
                        "context parameter should have a no-arg constructor, but the following type does not have" +
                        "such a constructor: " + className);
            }
            try {
                E ordinalType = (E) defaultConstructor.newInstance();
                targetList.add(ordinalType);
            } catch (Exception e) {
                throw new RuntimeException("An error when processing the " + paramName + " application " +
                        "context parameter. Exception while constructing an instance of " + className, e);
            }

        }
        return targetList;
    }

    private static String getApplicationInitParameter(String paramName) {
        return getApplicationInitParameter(paramName, String.class);
    }

    public static <T> T getApplicationInitParameter(String paramName, Class<T> expectedType) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Map<String, Object> applicationMap = externalContext.getApplicationMap();

        String paramStorageKey = Environment.class.getName() + ".getApplicationInitParameter.param:" + paramName;
        Object paramValue;
        if (applicationMap.containsKey(paramStorageKey)) {
            paramValue = applicationMap.get(paramStorageKey);
        } else {
            String paramAsString = externalContext.getInitParameter(paramName);
            if (paramAsString == null)
                paramValue = null;
            else if (expectedType.equals(String.class))
                paramValue = paramAsString;
            else if (expectedType.equals(Boolean.class))
                paramValue = Boolean.parseBoolean(paramAsString);
            else if (expectedType.equals(Integer.class))
                paramValue = Integer.parseInt(paramAsString);
            else if (expectedType.equals(Long.class))
                paramValue = Long.parseLong(paramAsString);
            else if (expectedType.equals(Double.class))
                paramValue = Double.parseDouble(paramAsString);
            else if (expectedType.equals(Float.class))
                paramValue = Float.parseFloat(paramAsString);
            else
                throw new IllegalArgumentException("Unsupported expectedType value: " + expectedType.getName());

            applicationMap.put(paramStorageKey, paramValue);
        }

        return (T) paramValue;


    }

    public static SummaryFunction getSummaryFunctionByName(String functionStr) {
        List<SummaryFunction> registeredFunctions = getSummaryFunctions();
        SummaryFunction fn = null;
        for (SummaryFunction function : registeredFunctions) {
            String name = function.getName();
            if (functionStr.equals(name.toLowerCase())) {
                fn = function;
                break;
            }
        }
        return fn;
    }

    private static Boolean iterationIndexWorkaround;

    public static boolean getIterationIndexWorkaround() {
        if (iterationIndexWorkaround == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            iterationIndexWorkaround = Rendering.getBooleanContextParam(context,
                    "org.openfaces.resetIterationIndexesOnAddingComponents", true);
        }
        return iterationIndexWorkaround;
    }
}

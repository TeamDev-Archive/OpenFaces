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

package org.openfaces.component.filter;

import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONBuilder extends FilterCriterionProcessor {

    private static final String TYPE = "type";
    private static final String CRITERIA = "criteria";

    private static final String PROPERTY_LOCATOR_EXPRESSION = "property";
    private static final String PARAMETERS = "parameters";
    private static final String FILTER_CONDITION_NAME = "condition";
    private static final String INVERSE = "inverse";

    private static final String PARAMETER_TYPE = "type";
    private static final String PARAMETER_VALUE = "value";

    private static JSONBuilder instance;

    private static enum CriterionType {
        AND, OR, EXPRESSION
    }

    private static enum ParameterType {
        DATE, NUMBER, TEXT, BOOLEAN
    }

    public static FilterCriterion parse(JSONObject jsonObject) throws JSONException {
        return parse(jsonObject, null);
    }
    
    public static FilterCriterion parse(JSONObject jsonObject, PropertyLocatorFactory locatorFactory) throws JSONException {
        String jsonCriterionType = jsonObject.getString(TYPE);
        CriterionType criterionType = CriterionType.valueOf(jsonCriterionType);
        if (criterionType != null) {
            switch (criterionType) {
                case EXPRESSION:
                    return parseExpression(jsonObject, locatorFactory);
                case AND:
                    return new AndFilterCriterion(parseComposite(jsonObject, locatorFactory));
                case OR:
                    return new OrFilterCriterion(parseComposite(jsonObject, locatorFactory));

            }
        }
        throw new IllegalArgumentException("Unknown criterion type: " + jsonCriterionType);
    }

    private static List<FilterCriterion> parseComposite(JSONObject jsonObject, PropertyLocatorFactory locatorFactory) throws JSONException {
        List<FilterCriterion> result = new ArrayList<FilterCriterion>();
        JSONArray criteria = jsonObject.getJSONArray(CRITERIA);
        for (int i = 0; i < criteria.length(); i++) {
            result.add(parse(criteria.getJSONObject(i), locatorFactory));
        }
        return result;
    }

    private static ExpressionFilterCriterion parseExpression(JSONObject jsonObject, PropertyLocatorFactory locatorFactory) throws JSONException {

        PropertyLocator propertyLocator;
        String expression = jsonObject.getString(PROPERTY_LOCATOR_EXPRESSION);
        if (locatorFactory == null) {
            propertyLocator = PropertyLocator.getDefaultInstance(expression);
        } else {
            propertyLocator = locatorFactory.create(expression);
        }
        Map<String, Object> parameters = new HashMap<String, Object>();
        JSONObject jsonParameters = jsonObject.getJSONObject(PARAMETERS);
        for (Iterator keyIterator = jsonParameters.keys(); keyIterator.hasNext();) {
            String key = (String) keyIterator.next();
            Object parameterValue = parseParameter(jsonParameters.getJSONObject(key));
            parameters.put(key, parameterValue);
        }

        FilterCondition condition = FilterCondition.byName(jsonObject.getString(FILTER_CONDITION_NAME));
        boolean inverse = jsonObject.getBoolean(INVERSE);
        ExpressionFilterCriterion result = new ExpressionFilterCriterion(propertyLocator, condition, parameters, inverse);
        return result;
    }

    private static Object parseParameter(JSONObject jsonObject) throws JSONException {
        String jsonParameterType = jsonObject.getString(PARAMETER_TYPE);
        ParameterType type = ParameterType.valueOf(jsonParameterType.toUpperCase());
        if (type != null) {
            switch (type) {
                case NUMBER:
                    return jsonObject.getDouble(PARAMETER_VALUE);
                case BOOLEAN:
                    return jsonObject.getBoolean(PARAMETER_VALUE);
                case DATE:
                    return new Date(jsonObject.getLong(PARAMETER_VALUE));
                case TEXT:
                    return jsonObject.getString(PARAMETER_VALUE);
            }
        }
        throw new IllegalArgumentException("Unknown parameter type: " + jsonParameterType);
    }

    public static JSONObject build(FilterCriterion filterCriterion) {
        return (JSONObject) filterCriterion.process(getInstance());
    }


    @Override
    public JSONObject process(ExpressionFilterCriterion criterion) {
        try {
            JSONObject result = new JSONObject();
            result.put(TYPE, CriterionType.EXPRESSION);
            result.put(PROPERTY_LOCATOR_EXPRESSION, criterion.getPropertyLocator().getExpression());

            JSONObject jsonParameters = new JSONObject();
            Map<String, Object> parameters = criterion.getParameters();
            for (String parameterName : parameters.keySet()) {
                Object parameterValue = criterion.getParameters().get(parameterName);
                JSONObject jsonParameterValue = new JSONObject();
                ParameterType parameterType;
                if (parameterValue instanceof Date) {
                    parameterType = ParameterType.DATE;
                    parameterValue = ((Date) parameterValue).getTime();
                } else if (isNumberType(parameterValue.getClass())) {
                    parameterType = ParameterType.NUMBER;
                } else if (isBooleanType(parameterValue.getClass())) {
                    parameterType = ParameterType.BOOLEAN;
                } else {
                    parameterType = ParameterType.TEXT;
                    parameterValue = parameterValue.toString();
                }

                jsonParameterValue.put(PARAMETER_TYPE, parameterType);
                jsonParameterValue.put(PARAMETER_VALUE, parameterValue);
                jsonParameters.put(parameterName, jsonParameterValue);
            }
            result.put(PARAMETERS, jsonParameters);
            result.put(FILTER_CONDITION_NAME, criterion.getCondition().getName());
            result.put(INVERSE, criterion.isInverse());
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject process(AndFilterCriterion criterion) {
        return process(criterion, CriterionType.AND);

    }

    @Override
    public JSONObject process(OrFilterCriterion criterion) {
        return process(criterion, CriterionType.OR);
    }

    private JSONObject process(CompositeFilterCriterion criterion, CriterionType type) {
        try {
            JSONObject result = new JSONObject();
            result.put(TYPE, type);
            JSONArray criteria = new JSONArray();
            for (FilterCriterion childCriterion : criterion.getCriteria()) {
                criteria.put(childCriterion.process(this));
            }
            result.put(CRITERIA, criteria);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static JSONBuilder getInstance() {
        if (instance == null)
            instance = new JSONBuilder();
        return instance;
    }


    private static boolean isNumberType(final Class<?> type) {
        return type == Long.TYPE || type == Double.TYPE ||
                type == Byte.TYPE || type == Short.TYPE ||
                type == Integer.TYPE || type == Float.TYPE ||
                Number.class.isAssignableFrom(type);
    }


    private static boolean isBooleanType(final Class<?> type) {
        return type == Boolean.TYPE || type == Boolean.class;
    }
}

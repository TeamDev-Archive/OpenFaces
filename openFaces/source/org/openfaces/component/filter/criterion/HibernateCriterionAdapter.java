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

package org.openfaces.component.filter.criterion;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class HibernateCriterionAdapter {

    private static HashMap<String, Collection<PropertyFilterCriterion>> buildCriterionsMap(Collection<PropertyFilterCriterion> criterionList) {
        HashMap<String, Collection<PropertyFilterCriterion>> result = new HashMap<String, Collection<PropertyFilterCriterion>>();
        for (PropertyFilterCriterion criterion : criterionList) {
            String property = criterion.getProperty();
            Collection<PropertyFilterCriterion> list = result.get(property);
            if (list == null) {
                list = new ArrayList<PropertyFilterCriterion>();
                result.put(property, list);
            }
            list.add(criterion);
        }
        return result;
    }


    private static Criterion convertToHibernateCriteria(PropertyFilterCriterion propertyFilterCriterion) {
        String property = propertyFilterCriterion.getProperty();
        Object parameter = propertyFilterCriterion.getParameter();
        Criterion result;
        switch (propertyFilterCriterion.getOperation()) {
            case EXACT: {
                if (parameter == null) {
                    result = Restrictions.isNull(property);
                    break;
                } else if (parameter instanceof String) {
                    Boolean caseSensitive = (Boolean) propertyFilterCriterion.getParameter(1);
                    if (caseSensitive == null || !caseSensitive) {
                        result = Restrictions.like(property, parameter).ignoreCase();
                    } else {
                        result = Restrictions.like(property, parameter);
                    }
                    break;
                } else {
                    result = Restrictions.eq(property, parameter);
                    break;
                }
            }
            case EQ:
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameter(1);
                    Date dayStart = ParametersInterpretator.dayStart((Date) parameter, timeZone);
                    Date dayEnd = ParametersInterpretator.dayEnd((Date) parameter, timeZone);
                    result = Restrictions.and(Restrictions.ge(property, dayStart), Restrictions.le(property, dayEnd));
                    break;
                } else {
                    result = Restrictions.eq(property, parameter);
                    break;
                }
            case GE:
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameter(1);
                    parameter = ParametersInterpretator.dayStart((Date) parameter, timeZone);
                }
                result = Restrictions.ge(property, parameter);
                break;
            case GT:
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameter(1);
                    parameter = ParametersInterpretator.dayEnd((Date) parameter, timeZone);
                }
                result = Restrictions.gt(property, parameter);
                break;
            case LE:
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameter(1);
                    parameter = ParametersInterpretator.dayEnd((Date) parameter, timeZone);
                }
                result = Restrictions.le(property, parameter);
                break;
            case LT:
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameter(1);
                    parameter = ParametersInterpretator.dayStart((Date) parameter, timeZone);
                }
                result = Restrictions.lt(property, parameter);
                break;
            case EQUALS: {
                if (parameter == null) {
                    parameter = "";
                }
                Boolean caseSensitive = (Boolean) propertyFilterCriterion.getParameter(1);                
                if (!caseSensitive) {
                    result = Restrictions.like(property, parameter.toString().toLowerCase()).ignoreCase();
                } else {
                    result = Restrictions.like(property, parameter);
                }
                break;
            }
            case BEGINS: {
                Boolean caseSensitive = (Boolean) propertyFilterCriterion.getParameter(1);
                if (!caseSensitive) {
                    result = Restrictions.like(property, parameter.toString().toLowerCase() + "%").ignoreCase();
                } else {
                    result = Restrictions.like(property, parameter + "%");
                }
                break;
            }
            case ENDS: {
                Boolean caseSensitive = (Boolean) propertyFilterCriterion.getParameter(1);
                if (!caseSensitive) {
                    result = Restrictions.like(property, "%" + parameter.toString().toLowerCase()).ignoreCase();
                } else {
                    result = Restrictions.like(property, "%" + parameter);
                }
                break;
            }
            case CONTAINS: {
                Boolean caseSensitive = (Boolean) propertyFilterCriterion.getParameter(1);
                if (!caseSensitive) {
                    result = Restrictions.like(property, "%" + parameter.toString().toLowerCase() + "%").ignoreCase();
                } else {
                    result = Restrictions.like(property, "%" + parameter + "%");
                }
                break;
            }

            case BETWEEN: {
                Object parameter2 = propertyFilterCriterion.getParameter(1);
                if (parameter instanceof Date && parameter2 instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameter(2);
                    Date periodStart = ParametersInterpretator.dayStart((Date) parameter, timeZone);
                    Date periodEnd = ParametersInterpretator.dayEnd((Date) parameter2, timeZone);
                    result = Restrictions.and(Restrictions.ge(property, periodStart), Restrictions.le(property, periodEnd));
                    break;
                } else {
                    result = Restrictions.between(property, parameter, parameter2);
                    break;
                }
            }
            default:
                throw new UnsupportedOperationException("Unsupported operation type: " + propertyFilterCriterion.getOperation());
        }
        if (parameter != null) {
            result = Restrictions.and(Restrictions.isNotNull(property), result);
        }
        return (propertyFilterCriterion.isInverse()) ? Restrictions.not(result) : result;
    }

    public static Criterion convertToHibernateCriterion(List<PropertyFilterCriterion> criterionList) {
        Conjunction conjuction = Restrictions.conjunction();
        HashMap<String, Collection<PropertyFilterCriterion>> map = buildCriterionsMap(criterionList);
        for (Collection<PropertyFilterCriterion> list : map.values()) {
            Disjunction disjunction = Restrictions.disjunction();
            for (PropertyFilterCriterion propertyFilterCriterion : list) {
                Criterion criterion = convertToHibernateCriteria(propertyFilterCriterion);
                disjunction.add(criterion);
            }
            conjuction.add(disjunction);
        }
        return conjuction;
    }
}

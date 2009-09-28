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
import java.util.TimeZone;
import java.util.List;

public class HibernateCriterionAdapter {

    private static HashMap<PropertyLocator, Collection<PropertyFilterCriterion>> buildCriterionsMap(Collection<PropertyFilterCriterion> criterionList) {
        HashMap<PropertyLocator, Collection<PropertyFilterCriterion>> result = new HashMap<PropertyLocator, Collection<PropertyFilterCriterion>>();
        for (PropertyFilterCriterion criterion : criterionList) {
            PropertyLocator propertyLocator = criterion.getPropertyLocator();
            Collection<PropertyFilterCriterion> list = result.get(propertyLocator);
            if (list == null) {
                list = new ArrayList<PropertyFilterCriterion>();
                result.put(propertyLocator, list);
            }
            list.add(criterion);
        }
        return result;
    }


    private static Criterion convertToHibernateCriteria(PropertyFilterCriterion propertyFilterCriterion) {
        PropertyLocator propertyLocator = propertyFilterCriterion.getPropertyLocator();
        if (! (propertyLocator instanceof NamedPropertyLocator))
            throw new IllegalArgumentException("HibernateCriterionAdapter can work only with NamedPropertyLocators, but the following locator was found: " + propertyLocator.getClass().getName());
        String property = ((NamedPropertyLocator) propertyLocator).getName();
        Object parameter = propertyFilterCriterion.getArg1();
        Criterion result;
        switch (propertyFilterCriterion.getOperation()) {
            case EXACT: {
                if (parameter == null) {
                    result = Restrictions.isNull(property);
                    break;
                } else if (parameter instanceof String) {
                    boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                    if (!caseSensitive) {
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
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
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
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    parameter = ParametersInterpretator.dayStart((Date) parameter, timeZone);
                }
                result = Restrictions.ge(property, parameter);
                break;
            case GT:
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    parameter = ParametersInterpretator.dayEnd((Date) parameter, timeZone);
                }
                result = Restrictions.gt(property, parameter);
                break;
            case LE:
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    parameter = ParametersInterpretator.dayEnd((Date) parameter, timeZone);
                }
                result = Restrictions.le(property, parameter);
                break;
            case LT:
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    parameter = ParametersInterpretator.dayStart((Date) parameter, timeZone);
                }
                result = Restrictions.lt(property, parameter);
                break;
            case EQUALS: {
                if (parameter == null) {
                    parameter = "";
                }
                boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                if (!caseSensitive) {
                    result = Restrictions.like(property, parameter.toString().toLowerCase()).ignoreCase();
                } else {
                    result = Restrictions.like(property, parameter);
                }
                break;
            }
            case BEGINS: {
                boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                if (!caseSensitive) {
                    result = Restrictions.like(property, parameter.toString().toLowerCase() + "%").ignoreCase();
                } else {
                    result = Restrictions.like(property, parameter + "%");
                }
                break;
            }
            case ENDS: {
                boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                if (!caseSensitive) {
                    result = Restrictions.like(property, "%" + parameter.toString().toLowerCase()).ignoreCase();
                } else {
                    result = Restrictions.like(property, "%" + parameter);
                }
                break;
            }
            case CONTAINS: {
                boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                if (!caseSensitive) {
                    result = Restrictions.like(property, "%" + parameter.toString().toLowerCase() + "%").ignoreCase();
                } else {
                    result = Restrictions.like(property, "%" + parameter + "%");
                }
                break;
            }

            case BETWEEN: {
                Object parameter2 = propertyFilterCriterion.getArg2();
                if (parameter instanceof Date && parameter2 instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
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
        HashMap<PropertyLocator, Collection<PropertyFilterCriterion>> map = buildCriterionsMap(criterionList);
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

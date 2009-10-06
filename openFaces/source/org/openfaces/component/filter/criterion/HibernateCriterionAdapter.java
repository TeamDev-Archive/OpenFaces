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
import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.FilterCriterionProcessor;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HibernateCriterionAdapter extends FilterCriterionProcessor {

    private static HibernateCriterionAdapter instance;

    public static HibernateCriterionAdapter getInstance() {
        if (instance == null)
            instance = new HibernateCriterionAdapter();
        return instance;
    }

    public static Criterion convertToHibernateCriterion(FilterCriterion filterCriterion) {
        return (Criterion) filterCriterion.process(getInstance());
    }

    public Object process(PropertyFilterCriterion criterion) {
        return convertToHibernateCriteria(criterion);
    }

    public Object process(AndFilterCriterion criterion) {
        List<FilterCriterion> criteria = criterion.getCriteria();
        Conjunction conjunction = Restrictions.conjunction();
        for (FilterCriterion filterCriterion : criteria) {
            Criterion hibernateCriterion = (Criterion) filterCriterion.process(this);
            conjunction.add(hibernateCriterion);
        }
        return conjunction;
    }

    public Object process(OrFilterCriterion criterion) {
        List<FilterCriterion> criteria = criterion.getCriteria();

        Disjunction disjunction = Restrictions.disjunction();
        for (FilterCriterion filterCriterion : criteria) {
            Criterion hibernateCriterion = (Criterion) filterCriterion.process(this);
            disjunction.add(hibernateCriterion);
        }
        return disjunction;
    }


    private static Criterion convertToHibernateCriteria(PropertyFilterCriterion propertyFilterCriterion) {
        String property = propertyFilterCriterion.getExpressionStr();
        Object parameter = propertyFilterCriterion.getArg1();
        Criterion result;
        switch (propertyFilterCriterion.getOperation()) {
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
                    result = Restrictions.isNull(property);
                    break;
                }

                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    Date dayStart = ParametersInterpretator.dayStart((Date) parameter, timeZone);
                    Date dayEnd = ParametersInterpretator.dayEnd((Date) parameter, timeZone);
                    result = Restrictions.and(Restrictions.ge(property, dayStart), Restrictions.le(property, dayEnd));
                    break;
                } else if (parameter instanceof String) {
                    boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                    if (!caseSensitive) {
                        result = Restrictions.like(property, parameter.toString().toLowerCase()).ignoreCase();
                    } else {
                        result = Restrictions.like(property, parameter);
                    }
                    break;
                } else {
                    result = Restrictions.eq(property, parameter);
                    break;
                }
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

}

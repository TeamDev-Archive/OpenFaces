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

package org.openfaces.component.filter;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.FilterCriterionProcessor;
import org.openfaces.component.filter.FilterCondition;
import org.openfaces.component.filter.FilterConditionProcessor;

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


    private static Criterion convertToHibernateCriteria(final PropertyFilterCriterion propertyFilterCriterion) {
        final String property = propertyFilterCriterion.getExpressionStr();
        final Object parameter = propertyFilterCriterion.getArg1();
        FilterCondition condition = propertyFilterCriterion.getCondition();
        Criterion result = (Criterion) condition.process(new FilterConditionProcessor() {
            @Override
            public Object processEmpty() {
                return Restrictions.isNull(property);
            }

            @Override
            public Object processEquals() {
                if (parameter == null) {
                    return Restrictions.isNull(property);
                }

                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    Date dayStart = ParametersInterpreter.dayStart((Date) parameter, timeZone);
                    Date dayEnd = ParametersInterpreter.dayEnd((Date) parameter, timeZone);
                    return Restrictions.and(Restrictions.ge(property, dayStart), Restrictions.le(property, dayEnd));
                } else if (parameter instanceof String) {
                    boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                    if (!caseSensitive) {
                        return Restrictions.like(property, parameter.toString().toLowerCase()).ignoreCase();
                    } else {
                        return Restrictions.like(property, parameter);
                    }
                } else {
                    return Restrictions.eq(property, parameter);
                }
            }

            @Override
            public Object processContains() {
                boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                if (!caseSensitive) {
                    return Restrictions.like(property, "%" + parameter.toString().toLowerCase() + "%").ignoreCase();
                } else {
                    return Restrictions.like(property, "%" + parameter + "%");
                }
            }

            @Override
            public Object processBegins() {
                boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                if (!caseSensitive) {
                    return Restrictions.like(property, parameter.toString().toLowerCase() + "%").ignoreCase();
                } else {
                    return Restrictions.like(property, parameter + "%");
                }
            }

            @Override
            public Object processEnds() {
                boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                if (!caseSensitive) {
                    return Restrictions.like(property, "%" + parameter.toString().toLowerCase()).ignoreCase();
                } else {
                    return Restrictions.like(property, "%" + parameter);
                }
            }

            @Override
            public Object processLess() {
                Object correctedParameter = parameter;
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    correctedParameter = ParametersInterpreter.dayStart((Date) parameter, timeZone);
                }
                return Restrictions.lt(property, correctedParameter);
            }

            @Override
            public Object processGreater() {
                Object correctedParameter = parameter;
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    correctedParameter = ParametersInterpreter.dayEnd((Date) parameter, timeZone);
                }
                return Restrictions.gt(property, correctedParameter);
            }

            @Override
            public Object processLessOrEqual() {
                Object correctedParameter = parameter;
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    correctedParameter = ParametersInterpreter.dayEnd((Date) parameter, timeZone);
                }
                return Restrictions.le(property, correctedParameter);
            }

            @Override
            public Object processGreaterOrEqual() {
                Object correctedParameter = parameter;
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    correctedParameter = ParametersInterpreter.dayStart((Date) parameter, timeZone);
                }
                return Restrictions.ge(property, correctedParameter);
            }

            @Override
            public Object processBetween() {
                Object parameter2 = propertyFilterCriterion.getArg2();
                if (parameter instanceof Date && parameter2 instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    Date periodStart = ParametersInterpreter.dayStart((Date) parameter, timeZone);
                    Date periodEnd = ParametersInterpreter.dayEnd((Date) parameter2, timeZone);
                    return Restrictions.and(Restrictions.ge(property, periodStart), Restrictions.le(property, periodEnd));
                } else {
                    return Restrictions.between(property, parameter, parameter2);
                }
            }
        });


        if (parameter != null) {
            result = Restrictions.and(Restrictions.isNotNull(property), result);
        }
        return (propertyFilterCriterion.isInverse()) ? Restrictions.not(result) : result;
    }

}

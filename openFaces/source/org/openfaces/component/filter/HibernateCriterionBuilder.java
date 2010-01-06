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

package org.openfaces.component.filter;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HibernateCriterionBuilder extends FilterCriterionProcessor {

    private static HibernateCriterionBuilder instance;

    public static HibernateCriterionBuilder getInstance() {
        if (instance == null)
            instance = new HibernateCriterionBuilder();
        return instance;
    }

    public static Criterion build(FilterCriterion filterCriterion) {
        return (Criterion) filterCriterion.process(getInstance());
    }

    public Object process(ExpressionFilterCriterion criterion) {
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


    private static Criterion convertToHibernateCriteria(final ExpressionFilterCriterion expressionFilterCriterion) {
        final String property = expressionFilterCriterion.getExpressionStr();
        final Object parameter = expressionFilterCriterion.getArg1();
        FilterCondition condition = expressionFilterCriterion.getCondition();
        Criterion result = condition.process(new FilterConditionProcessor<Criterion>() {
            @Override
            public Criterion processEmpty() {
                return Restrictions.isNull(property);
            }

            @Override
            public Criterion processEquals() {
                if (parameter == null) {
                    return Restrictions.isNull(property);
                }

                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) expressionFilterCriterion.getParameters().get("timeZone");
                    Date dayStart = ParametersInterpreter.dayStart((Date) parameter, timeZone);
                    Date dayEnd = ParametersInterpreter.dayEnd((Date) parameter, timeZone);
                    return Restrictions.and(Restrictions.ge(property, dayStart), Restrictions.le(property, dayEnd));
                } else if (parameter instanceof String) {
                    boolean caseSensitive = expressionFilterCriterion.isCaseSensitive();
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
            public Criterion processContains() {
                boolean caseSensitive = expressionFilterCriterion.isCaseSensitive();
                if (!caseSensitive) {
                    return Restrictions.like(property, "%" + parameter.toString().toLowerCase() + "%").ignoreCase();
                } else {
                    return Restrictions.like(property, "%" + parameter + "%");
                }
            }

            @Override
            public Criterion processBegins() {
                boolean caseSensitive = expressionFilterCriterion.isCaseSensitive();
                if (!caseSensitive) {
                    return Restrictions.like(property, parameter.toString().toLowerCase() + "%").ignoreCase();
                } else {
                    return Restrictions.like(property, parameter + "%");
                }
            }

            @Override
            public Criterion processEnds() {
                boolean caseSensitive = expressionFilterCriterion.isCaseSensitive();
                if (!caseSensitive) {
                    return Restrictions.like(property, "%" + parameter.toString().toLowerCase()).ignoreCase();
                } else {
                    return Restrictions.like(property, "%" + parameter);
                }
            }

            @Override
            public Criterion processLess() {
                Object correctedParameter = parameter;
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) expressionFilterCriterion.getParameters().get("timeZone");
                    correctedParameter = ParametersInterpreter.dayStart((Date) parameter, timeZone);
                }
                return Restrictions.lt(property, correctedParameter);
            }

            @Override
            public Criterion processGreater() {
                Object correctedParameter = parameter;
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) expressionFilterCriterion.getParameters().get("timeZone");
                    correctedParameter = ParametersInterpreter.dayEnd((Date) parameter, timeZone);
                }
                return Restrictions.gt(property, correctedParameter);
            }

            @Override
            public Criterion processLessOrEqual() {
                Object correctedParameter = parameter;
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) expressionFilterCriterion.getParameters().get("timeZone");
                    correctedParameter = ParametersInterpreter.dayEnd((Date) parameter, timeZone);
                }
                return Restrictions.le(property, correctedParameter);
            }

            @Override
            public Criterion processGreaterOrEqual() {
                Object correctedParameter = parameter;
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) expressionFilterCriterion.getParameters().get("timeZone");
                    correctedParameter = ParametersInterpreter.dayStart((Date) parameter, timeZone);
                }
                return Restrictions.ge(property, correctedParameter);
            }

            @Override
            public Criterion processBetween() {
                Object parameter2 = expressionFilterCriterion.getArg2();
                if (parameter instanceof Date && parameter2 instanceof Date) {
                    TimeZone timeZone = (TimeZone) expressionFilterCriterion.getParameters().get("timeZone");
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
        return (expressionFilterCriterion.isInverse()) ? Restrictions.not(result) : result;
    }

}

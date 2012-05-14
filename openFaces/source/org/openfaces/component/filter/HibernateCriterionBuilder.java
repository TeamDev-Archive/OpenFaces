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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openfaces.util.Faces;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HibernateCriterionBuilder extends FilterCriterionProcessor {

    private static HibernateCriterionBuilder instance;

    /**
     * Returns hibernate Criterion instance that corresponds to the passed FilterCriterion instance.
     */
    public static Criterion build(FilterCriterion filterCriterion) {
        return (Criterion) filterCriterion.process(getInstance());
    }

    /**
     * You can invoke this function from the DataTable's data providing method (a method that is bound to the DataTable's
     * "value" attribute) to implement the hibernate-based custom data providing mode.
     * <p/>
     * This method creates the hibernate Criteria object with the current DataTable custom data providing parameters
     * that are automatically retrieved from the request scope by this method. That is the returned Criteria will
     * contain the current table's filtering, sorting and pagination parameters.
     * <p/>
     * NOTE: the table should have the "id" attribute specified for all sortable columns. The id value should correspond
     * to the appropriate property name by which sorting should be performed, or more formally, the value of the "id"
     * attribute will be passed to the Order.asc(String propertyName) and Order.desc(String propertyName) methods when
     * populating the Criteria object.
     * <p/>
     * You can use any of the buildCriteria() methods or the fillCriteria() method depending on the needs of your
     * application.
     *
     * @see @fillCriteria
     */
    public static Criteria buildCriteria(Session session, Class persistentClass) {
        Criteria criteria = session.createCriteria(persistentClass);
        fillCriteria(criteria);
        return criteria;
    }

    /**
     * You can invoke this function from the DataTable's data providing method (a method that is bound to the DataTable's
     * "value" attribute) to implement the hibernate-based custom data providing mode.
     * <p/>
     * This method creates the hibernate Criteria object with the current DataTable custom data providing parameters
     * that are automatically retrieved from the request scope by this method. That is the returned Criteria will
     * contain the current table's filtering, sorting and pagination parameters.
     * <p/>
     * NOTE: the table should have the "id" attribute specified for all sortable columns. The id value should correspond
     * to the appropriate property name by which sorting should be performed, or more formally, the value of the "id"
     * attribute will be passed to the Order.asc(String propertyName) and Order.desc(String propertyName) methods when
     * populating the Criteria object.
     * <p/>
     * You can use any of the buildCriteria() methods or the fillCriteria() method depending on the needs of your
     * application.
     *
     * @see @fillCriteria
     */
    public static Criteria buildCriteria(Session session, String entityName) {
        Criteria criteria = session.createCriteria(entityName);
        fillCriteria(criteria);
        return criteria;
    }

    /**
     * You can invoke this function from the DataTable's data providing method (a method that is bound to the DataTable's
     * "value" attribute) to implement the hibernate-based custom data providing mode.
     * <p/>
     * This method creates the hibernate Criteria object with the current DataTable custom data providing parameters
     * that are automatically retrieved from the request scope by this method. That is the returned Criteria will
     * contain the current table's filtering, sorting and pagination parameters.
     * <p/>
     * NOTE: the table should have the "id" attribute specified for all sortable columns. The id value should correspond
     * to the appropriate property name by which sorting should be performed, or more formally, the value of the "id"
     * attribute will be passed to the Order.asc(String propertyName) and Order.desc(String propertyName) methods when
     * populating the Criteria object.
     * <p/>
     * You can use any of the buildCriteria() methods or the fillCriteria() method depending on the needs of your
     * application.
     *
     * @see @fillCriteria
     */
    public static Criteria buildCriteria(Session session, Class persistentClass, String alias) {
        Criteria criteria = session.createCriteria(persistentClass, alias);
        fillCriteria(criteria);
        return criteria;
    }

    /**
     * You can invoke this function from the DataTable's data providing method (a method that is bound to the DataTable's
     * "value" attribute) to implement the hibernate-based custom data providing mode.
     * <p/>
     * This method creates the hibernate Criteria object with the current DataTable custom data providing parameters
     * that are automatically retrieved from the request scope by this method. That is the returned Criteria will
     * contain the current table's filtering, sorting and pagination parameters.
     * <p/>
     * NOTE: the table should have the "id" attribute specified for all sortable columns. The id value should correspond
     * to the appropriate property name by which sorting should be performed, or more formally, the value of the "id"
     * attribute will be passed to the Order.asc(String propertyName) and Order.desc(String propertyName) methods when
     * populating the Criteria object.
     * <p/>
     * You can use any of the buildCriteria() methods or the fillCriteria() method depending on the needs of your
     * application.
     *
     * @see @fillCriteria
     */
    public static Criteria buildCriteria(Session session, String entityName, String alias) {
        Criteria criteria = session.createCriteria(entityName, alias);
        fillCriteria(criteria);
        return criteria;
    }

    /**
     * You can invoke this function from the DataTable's data providing method (a method that is bound to the DataTable's
     * "value" attribute) to implement the hibernate-based custom data providing mode.
     * <p/>
     * This method fills the passed hibernate Criteria object with the current DataTable custom data providing
     * parameters that are automatically retrieved from the request scope by this method. That is the Criteria object
     * will be configured with the current table's filtering, sorting and pagination parameters.
     * <p/>
     * NOTE: the table should have the "id" attribute specified for all sortable columns. The id value should correspond
     * to the appropriate property name by which sorting should be performed, or more formally, the value of the "id"
     * attribute will be passed to the Order.asc(String propertyName) and Order.desc(String propertyName) methods when
     * populating the Criteria object.
     * <p/>
     * You can use any of the buildCriteria() methods or the fillCriteria() method depending on the needs of your
     * application.
     *
     * @see @buildCriteria
     */
    public static void fillCriteria(Criteria criteria) {
        fillCriteria(criteria, false);
    }

    private static void fillCriteria(Criteria criteria, boolean ignorePaginationParams) {
        fillCriteria(criteria, ignorePaginationParams, true);
    }

    private static void fillCriteria(Criteria criteria, boolean ignorePaginationParams, boolean addOrderBy) {
        CompositeFilterCriterion filterCriteria = Faces.var("filterCriteria", CompositeFilterCriterion.class);
        boolean sortAscending = Faces.var("sortAscending", Boolean.class);
        String sortColumnId = Faces.var("sortColumnId", String.class);
        Integer pageStart = Faces.var("pageStart", Integer.class);
        Integer pageSize = Faces.var("pageSize", Integer.class);

        if (filterCriteria != null) {
            Criterion criterion = build(filterCriteria);
            criteria.add(criterion);
        }
        if (!ignorePaginationParams && pageStart != null) {
            criteria.setFirstResult(pageStart);
            criteria.setMaxResults(pageSize);
        }
        if (sortColumnId != null && addOrderBy)
            criteria.addOrder(sortAscending ? Order.asc(sortColumnId) : Order.desc(sortColumnId));
    }

    /**
     * This method should be invoked from a method bound to the DataTable's "totalRowCount" attribute to implement the
     * hibernate-based custom data providing mode.
     *
     * @return the total number of rows without the pagination parameters (which is required by DataTable to properly
     *         calculate the number of pages).
     */
    public static int getRowCount(Session session, String entityName) {
        Criteria criteria = session.createCriteria(entityName);
        return getRowCount(criteria);
    }

    /**
     * This method should be invoked from a method bound to the DataTable's "totalRowCount" attribute to implement the
     * hibernate-based custom data providing mode.
     *
     * @return the total number of rows without the pagination parameters (which is required by DataTable to properly
     *         calculate the number of pages).
     */
    public static int getRowCount(Session session, Class persistentClass) {
        Criteria criteria = session.createCriteria(persistentClass);
        return getRowCount(criteria);
    }

    /**
     * This method should be invoked from a method bound to the DataTable's "totalRowCount" attribute to implement the
     * hibernate-based custom data providing mode.
     *
     * @return the total number of rows without the pagination parameters (which is required by DataTable to properly
     *         calculate the number of pages).
     */
    public static int getRowCount(Session session, String entityName, String alias) {
        Criteria criteria = session.createCriteria(entityName, alias);
        return getRowCount(criteria);
    }

    /**
     * This method should be invoked from a method bound to the DataTable's "totalRowCount" attribute to implement the
     * hibernate-based custom data providing mode.
     *
     * @return the total number of rows without the pagination parameters (which is required by DataTable to properly
     *         calculate the number of pages).
     */
    public static int getRowCount(Session session, Class persistentClass, String alias) {
        Criteria criteria = session.createCriteria(persistentClass, alias);
        return getRowCount(criteria);
    }

    /**
     * This method should be invoked from a method bound to the DataTable's "totalRowCount" attribute to implement the
     * hibernate-based custom data providing mode.
     *
     * @return the total number of rows without the pagination parameters (which is required by DataTable to properly
     *         calculate the number of pages).
     */
    public static int getRowCount(Criteria criteria) {
        fillCriteria(criteria, true, false);
        criteria.setProjection(Projections.rowCount());
        return ((Number) criteria.list().get(0)).intValue();
    }

    /**
     * Returns an instance of the HibernateCriterionBuilder class, which is used by the <code>build</code> method
     * internally. This method shouldn't normally be invoked by application developers.
     */
    public static HibernateCriterionBuilder getInstance() {
        if (instance == null)
            instance = new HibernateCriterionBuilder();
        return instance;
    }

    /**
     * Used by the <code>build</code> method internally. This method shouldn't normally be invoked by application
     * developers.
     */
    public Object process(ExpressionFilterCriterion criterion) {
        return convertToHibernateCriteria(criterion);
    }

    /**
     * Used by the <code>build</code> method internally. This method shouldn't normally be invoked by application
     * developers.
     */
    public Object process(AndFilterCriterion criterion) {
        List<FilterCriterion> criteria = criterion.getCriteria();
        Conjunction conjunction = Restrictions.conjunction();
        for (FilterCriterion filterCriterion : criteria) {
            Criterion hibernateCriterion = (Criterion) filterCriterion.process(this);
            conjunction.add(hibernateCriterion);
        }
        return conjunction;
    }

    /**
     * Used by the <code>build</code> method internally. This method shouldn't normally be invoked by application
     * developers.
     */
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

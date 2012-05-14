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

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.functors.AllPredicate;
import org.apache.commons.collections.functors.AnyPredicate;
import org.apache.commons.collections.functors.NotPredicate;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class PredicateBuilder extends FilterCriterionProcessor {
    private static final Comparator<Number> NUMBER_COMPARATOR = new NumberComparator();
    private static final Comparator<Date> COMPARABLE_COMPARATOR = new ComparableComparator();

    private static Comparator getComparatorForParameter(Object parameter) {
        if (parameter instanceof Number) {
            return NUMBER_COMPARATOR;
        } else if (parameter instanceof Comparable) {
            return COMPARABLE_COMPARATOR;
        } else {
            throw new IllegalArgumentException("Unsupported parameter class: " + parameter.getClass());
        }
    }

    private static PredicateBuilder instance;

    /**
     * Converts the specified <code>FilterCriterion</code> object into the Commons Collections Predicate object, which
     * can in turn be used for manual filtering of objects against the specified criterion.
     */
    public static Predicate build(FilterCriterion criterion) {
        return (Predicate) criterion.process(getInstance());
    }

    /**
     * Returns an instance of the PredicateBuilder class, which is used by the <code>build</code> method
     * internally. This method shouldn't normally be invoked by application developers.
     */
    public static PredicateBuilder getInstance() {
        if (instance == null)
            instance = new PredicateBuilder();
        return instance;
    }

    /**
     * Used by the <code>build</code> method internally. This method shouldn't normally be invoked by application
     * developers.
     */
    public Object process(ExpressionFilterCriterion criterion) {
        return convertToPredicate(criterion);
    }

    /**
     * Used by the <code>build</code> method internally. This method shouldn't normally be invoked by application
     * developers.
     */
    public Object process(AndFilterCriterion criterion) {
        List<FilterCriterion> criteria = criterion.getCriteria();
        Predicate[] predicates = new Predicate[criteria.size()];
        for (int i = 0; i < criteria.size(); i++) {
            FilterCriterion filterCriterion = criteria.get(i);
            predicates[i] = (Predicate) filterCriterion.process(this);
        }
        return new AllPredicate(predicates);
    }

    /**
     * Used by the <code>build</code> method internally. This method shouldn't normally be invoked by application
     * developers.
     */
    public Object process(OrFilterCriterion criterion) {
        List<FilterCriterion> criteria = criterion.getCriteria();
        Predicate[] predicates = new Predicate[criteria.size()];
        for (int i = 0; i < criteria.size(); i++) {
            FilterCriterion filterCriterion = criteria.get(i);
            predicates[i] = (Predicate) filterCriterion.process(this);
        }
        return new AnyPredicate(predicates);
    }

    private static Predicate convertToPredicate(final ExpressionFilterCriterion expressionFilterCriterion) {
        final FilterCondition condition = expressionFilterCriterion.getCondition();

        final Object parameter = expressionFilterCriterion.getArg1();
        if (parameter == null && condition != FilterCondition.EMPTY)
            return TruePredicate.getInstance();

        final Predicate predicateFunctor = condition.process(new FilterConditionProcessor<Predicate>() {
            public Predicate processEmpty() {
                return new Predicate() {
                    public boolean evaluate(Object o) {
                        return o == null || o.equals("");
                    }
                };
            }

            public Predicate processEquals() {
                Comparator comparator = getComparatorForParameter(parameter);
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) expressionFilterCriterion.getParameters().get("timeZone");
                    Date dayStart = ParametersInterpreter.dayStart((Date) parameter, timeZone);
                    Predicate preficateForBefore = new ComparePredicate(dayStart, FilterCondition.GREATER_OR_EQUAL, comparator);
                    Date dayEnd = ParametersInterpreter.dayEnd((Date) parameter, timeZone);
                    Predicate preficateForAfter = new ComparePredicate(dayEnd, FilterCondition.LESS_OR_EQUAL, comparator);
                    return new AllPredicate(new Predicate[]{preficateForBefore, preficateForAfter});
                } else if (parameter instanceof String) {
                    boolean caseSensitive = expressionFilterCriterion.isCaseSensitive();
                    return new AbstractStringPredicate(parameter.toString(), caseSensitive) {
                        public boolean evaluate(String parameter, String value) {
                            return value.equals(parameter);
                        }
                    };
                } else {
                    return new ComparePredicate(parameter, condition, comparator);
                }
            }

            public Predicate processContains() {
                boolean caseSensitive = expressionFilterCriterion.isCaseSensitive();
                return new AbstractStringPredicate(parameter.toString(), caseSensitive) {
                    public boolean evaluate(String parameter, String value) {
                        return value.contains(parameter);
                    }
                };
            }

            public Predicate processBegins() {
                boolean caseSensitive = expressionFilterCriterion.isCaseSensitive();
                return new AbstractStringPredicate(parameter.toString(), caseSensitive) {
                    public boolean evaluate(String parameter, String value) {
                        return value.startsWith(parameter);
                    }
                };
            }

            public Predicate processEnds() {
                boolean caseSensitive = expressionFilterCriterion.isCaseSensitive();
                return new AbstractStringPredicate(parameter.toString(), caseSensitive) {
                    public boolean evaluate(String parameter, String value) {
                        return value.endsWith(parameter);
                    }
                };
            }

            public Predicate processLess() {
                Comparator comparator = getComparatorForParameter(parameter);
                Object correctedParameter = parameter;
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) expressionFilterCriterion.getParameters().get("timeZone");
                    correctedParameter = ParametersInterpreter.dayStart((Date) parameter, timeZone);
                }
                return new ComparePredicate(correctedParameter, condition, comparator);
            }

            public Predicate processGreater() {
                Comparator comparator = getComparatorForParameter(parameter);
                Object correctedParameter = parameter;
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) expressionFilterCriterion.getParameters().get("timeZone");
                    correctedParameter = ParametersInterpreter.dayEnd((Date) parameter, timeZone);
                }
                return new ComparePredicate(correctedParameter, condition, comparator);
            }

            public Predicate processLessOrEqual() {
                Comparator comparator = getComparatorForParameter(parameter);
                Object correctedParameter = parameter;
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) expressionFilterCriterion.getParameters().get("timeZone");
                    correctedParameter = ParametersInterpreter.dayEnd((Date) parameter, timeZone);
                }
                return new ComparePredicate(correctedParameter, condition, comparator);
            }

            public Predicate processGreaterOrEqual() {
                Comparator comparator = getComparatorForParameter(parameter);
                Object correctedParameter = parameter;
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) expressionFilterCriterion.getParameters().get("timeZone");
                    correctedParameter = ParametersInterpreter.dayStart((Date) parameter, timeZone);
                }
                return new ComparePredicate(correctedParameter, condition, comparator);
            }

            public Predicate processBetween() {
                Comparator comparator = getComparatorForParameter(parameter);
                Object parameter1 = parameter;
                Object parameter2 = expressionFilterCriterion.getArg2();
                if (parameter2 == null)
                    return TruePredicate.getInstance();
                if (parameter1 instanceof Date && parameter2 instanceof Date) {
                    TimeZone timeZone = (TimeZone) expressionFilterCriterion.getParameters().get("timeZone");
                    parameter1 = ParametersInterpreter.dayStart((Date) parameter1, timeZone);
                    parameter2 = ParametersInterpreter.dayEnd((Date) parameter1, timeZone);
                }
                Predicate preficateForBefore = new ComparePredicate(parameter1, FilterCondition.GREATER_OR_EQUAL, comparator);
                Predicate preficateForAfter = new ComparePredicate(parameter2, FilterCondition.LESS_OR_EQUAL, comparator);
                return new AllPredicate(new Predicate[]{preficateForBefore, preficateForAfter});
            }
        });

        final PropertyLocator propertyLocator = expressionFilterCriterion.getPropertyLocator();
        Predicate predicate = new Predicate() {
            public boolean evaluate(Object o) {
                return predicateFunctor.evaluate(propertyLocator.getPropertyValue(o));
            }
        };

        return (expressionFilterCriterion.isInverse()) ? new NotPredicate(predicate) : predicate;
    }


    private static class NumberComparator implements Comparator<Number> {
        public int compare(Number o1, Number o2) {
            if (o1 == null && o2 != null)
                return new Double(0).compareTo(o2.doubleValue());
            if (o1 != null && o2 == null)
                return new Double(o1.doubleValue()).compareTo(0d);
            return new Double(o1.doubleValue())
                    .compareTo(o2.doubleValue());
        }
    }

    private static abstract class AbstractStringPredicate implements Predicate {
        private String parameter;
        private boolean caseSensitive;

        protected AbstractStringPredicate(String parameter, boolean caseSensitive) {
            this.parameter = parameter;
            this.caseSensitive = caseSensitive;
        }

        public boolean evaluate(Object o) {
            String value = o != null ? o.toString() : "";
            if (!caseSensitive) {
                return evaluate(parameter.toUpperCase(), value.toUpperCase());
            } else {
                return evaluate(parameter, value);
            }
        }

        public abstract boolean evaluate(String parameter, String value);
    }


    private static class ComparePredicate implements Predicate {
        private FilterCondition filterCondition;
        private Object parameter;
        private Comparator comparator;

        private ComparePredicate(Object parameter, FilterCondition filterCondition, Comparator comparator) {
            this.parameter = parameter;
            this.comparator = comparator;
            this.filterCondition = filterCondition;
        }

        protected boolean interpreteComparatorResult(int compareToResult) {
            switch (filterCondition) {
                case EQUALS:
                    return compareToResult == 0;
                case GREATER_OR_EQUAL:
                    return compareToResult >= 0;
                case GREATER:
                    return compareToResult > 0;
                case LESS_OR_EQUAL:
                    return compareToResult <= 0;
                case LESS:
                    return compareToResult < 0;
                default:
                    throw new UnsupportedOperationException("Unsupported operation type: " + filterCondition);
            }
        }

        @SuppressWarnings("unchecked")
        public boolean evaluate(Object o) {
            int compareToResult = comparator.compare(o, parameter);
            return interpreteComparatorResult(compareToResult);
        }
    }

    private static class TruePredicate implements Predicate {
        private static TruePredicate instance = new TruePredicate();

        public boolean evaluate(Object o) {
            return true;
        }

        public static TruePredicate getInstance() {
            return instance;
        }
    }
}
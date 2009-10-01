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

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.functors.AllPredicate;
import org.apache.commons.collections.functors.AnyPredicate;
import org.apache.commons.collections.functors.EqualPredicate;
import org.apache.commons.collections.functors.NotPredicate;
import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.FilterCriterionProcessor;
import org.openfaces.component.filter.OperationType;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class PredicateAdapter extends FilterCriterionProcessor {

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

    private static PredicateAdapter instance;

    public static PredicateAdapter getInstance() {
        if (instance == null)
            instance = new PredicateAdapter();
        return instance;
    }

    public static Predicate convertToPredicate(FilterCriterion criterion) {
        return (Predicate) criterion.process(getInstance());
    }

    public Object process(PropertyFilterCriterion criterion) {
        return convertToPredicate(criterion);
    }

    public Object process(AndFilterCriterion criterion) {
        List<FilterCriterion> criteria = criterion.getCriteria();
        Predicate[] predicates = new Predicate[criteria.size()];
        for (int i = 0; i < criteria.size(); i++) {
            FilterCriterion filterCriterion = criteria.get(i);
            predicates[i] = (Predicate) filterCriterion.process(this);
        }
        return new AllPredicate(predicates);
    }

    public Object process(OrFilterCriterion criterion) {
        List<FilterCriterion> criteria = criterion.getCriteria();
        Predicate[] predicates = new Predicate[criteria.size()];
        for (int i = 0; i < criteria.size(); i++) {
            FilterCriterion filterCriterion = criteria.get(i);
            predicates[i] = (Predicate) filterCriterion.process(this);
        }
        return new AnyPredicate(predicates);
    }

    private static Predicate convertToPredicate(PropertyFilterCriterion propertyFilterCriterion) {
        OperationType operation = propertyFilterCriterion.getOperation();
        final Predicate predicateFunctor;
        Object parameter = propertyFilterCriterion.getArg1();
        switch (operation) {


            case EQ: {
                Comparator comparator = getComparatorForParameter(parameter);
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    Date dayStart = ParametersInterpretator.dayStart((Date) parameter, timeZone);
                    Predicate preficateForBefore = new SimpleComparePredicate(dayStart, OperationType.GE, comparator);
                    Date dayEnd = ParametersInterpretator.dayEnd((Date) parameter, timeZone);
                    Predicate preficateForAfter = new SimpleComparePredicate(dayEnd, OperationType.LE, comparator);
                    predicateFunctor = new AllPredicate(new Predicate[]{preficateForBefore, preficateForAfter});
                    break;
                } else {
                    predicateFunctor = new SimpleComparePredicate(parameter, operation, comparator);
                    break;
                }
            }
            case GE: {
                Comparator comparator = getComparatorForParameter(parameter);
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    parameter = ParametersInterpretator.dayStart((Date) parameter, timeZone);
                }
                predicateFunctor = new SimpleComparePredicate(parameter, operation, comparator);
                break;
            }
            case GT: {
                Comparator comparator = getComparatorForParameter(parameter);
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    parameter = ParametersInterpretator.dayEnd((Date) parameter, timeZone);
                }
                predicateFunctor = new SimpleComparePredicate(parameter, operation, comparator);
                break;
            }
            case LE: {
                Comparator comparator = getComparatorForParameter(parameter);
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    parameter = ParametersInterpretator.dayEnd((Date) parameter, timeZone);
                }
                predicateFunctor = new SimpleComparePredicate(parameter, operation, comparator);
                break;
            }
            case LT: {
                Comparator comparator = getComparatorForParameter(parameter);
                if (parameter instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    parameter = ParametersInterpretator.dayStart((Date) parameter, timeZone);
                }
                predicateFunctor = new SimpleComparePredicate(parameter, operation, comparator);
                break;

            }
            case BETWEEN: {
                Comparator comparator = getComparatorForParameter(parameter);
                Object parameter2 = propertyFilterCriterion.getArg2();
                if (parameter instanceof Date && parameter2 instanceof Date) {
                    TimeZone timeZone = (TimeZone) propertyFilterCriterion.getParameters().get("timeZone");
                    parameter = ParametersInterpretator.dayStart((Date) parameter, timeZone);
                    parameter2 = ParametersInterpretator.dayEnd((Date) parameter, timeZone);
                }
                Predicate preficateForBefore = new SimpleComparePredicate(parameter, OperationType.GE, comparator);
                Predicate preficateForAfter = new SimpleComparePredicate(parameter2, OperationType.LE, comparator);
                predicateFunctor = new AllPredicate(new Predicate[]{preficateForBefore, preficateForAfter});
                break;
            }
            case EXACT:
                if (!(parameter instanceof String)) {
                    predicateFunctor = new EqualPredicate(parameter);
                    break;
                }
            case EQUALS: {
                boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                predicateFunctor = new AbstractStringPredicate(parameter.toString(), caseSensitive) {
                    public boolean evaluate(String parameter, String value) {
                        return value.equals(parameter);
                    }
                };
                break;
            }
            case BEGINS: {
                boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                predicateFunctor = new AbstractStringPredicate(parameter.toString(), caseSensitive) {
                    public boolean evaluate(String parameter, String value) {
                        return value.startsWith(parameter);
                    }
                };
                break;
            }
            case ENDS: {
                boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                predicateFunctor = new AbstractStringPredicate(parameter.toString(), caseSensitive) {
                    public boolean evaluate(String parameter, String value) {
                        return value.endsWith(parameter);
                    }
                };
                break;
            }
            case CONTAINS: {
                boolean caseSensitive = propertyFilterCriterion.isCaseSensitive();
                predicateFunctor = new AbstractStringPredicate(parameter.toString(), caseSensitive) {
                    public boolean evaluate(String parameter, String value) {
                        return value.contains(parameter);
                    }
                };
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported operation operation: " + propertyFilterCriterion.getOperation());
        }

        final PropertyLocator propertyLocator = propertyFilterCriterion.getPropertyLocator();
        Predicate predicate = new Predicate() {
            public boolean evaluate(Object o) {
                return predicateFunctor.evaluate(propertyLocator.getPropertyValue(o));
            }
        };

        return (propertyFilterCriterion.isInverse()) ? new NotPredicate(predicate) : predicate;
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
            String value = (String) o;
            if (!caseSensitive) {
                return evaluate(parameter.toUpperCase(), value.toUpperCase());
            } else {
                return evaluate(parameter, value);
            }
        }

        public abstract boolean evaluate(String parameter, String value);
    }


    private static class SimpleComparePredicate extends ComparePredicate {
        private OperationType operationType;

        private SimpleComparePredicate(Object parameter, OperationType operationType, Comparator comparator) {
            super(parameter, comparator);
            this.operationType = operationType;
        }

        protected boolean interpreteComparatorResult(int compareToResult) {
            switch (operationType) {
                case EQ:
                    return compareToResult == 0;
                case GE:
                    return compareToResult >= 0;
                case GT:
                    return compareToResult > 0;
                case LE:
                    return compareToResult <= 0;
                case LT:
                    return compareToResult < 0;
                default:
                    throw new UnsupportedOperationException("Unsupported operation type: " + operationType);
            }
        }
    }


    private abstract static class ComparePredicate implements Predicate {
        private Object parameter;
        private Comparator comparator;

        protected ComparePredicate(Object parameter, Comparator comparator) {
            this.parameter = parameter;
            this.comparator = comparator;
        }

        @SuppressWarnings("unchecked")
        public boolean evaluate(Object o) {
            int compareToResult = comparator.compare(o, parameter);
            return interpreteComparatorResult(compareToResult);
        }

        protected abstract boolean interpreteComparatorResult(int compareToResult);

    }

}
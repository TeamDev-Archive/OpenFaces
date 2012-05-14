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
package org.openfaces.validation.core;

import static org.openfaces.validation.core.EL.EL_CONTEXT;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * <p><strong>Expressions</strong></p> is a utility class that is used to creating and parsing
 * of value and method expressions.
 * 
 * @author Gavin King
 * @author Eugene Goncharov
 * 
 */
public class Expressions implements Serializable {
	private static final long serialVersionUID = -5127834621632138270L;
	private static Expressions ourInstance;


    protected Expressions() {

    }

    /**
     * Get the JBoss EL ExpressionFactory
     */
    public ExpressionFactory getExpressionFactory() {
        return FacesContext.getCurrentInstance().getApplication().getExpressionFactory();
    }

    /**
     * Get an appropriate ELContext. If there is an active JSF request,
     * use JSF's ELContext. Otherwise, use one that we created.
     */
    public ELContext getELContext() {
        return EL_CONTEXT;
    }

    /**
     * Create a value expression.
     *
     * @param expression a JBoss EL value expression
     */
    public ValueExpression<Object> createValueExpression(String expression) {
        return createValueExpression(expression, Object.class);
    }

    /**
     * Create a method expression.
     *
     * @param expression a JBoss EL method expression
     */
    public MethodExpression<Object> createMethodExpression(String expression) {
        return createMethodExpression(expression, Object.class);
    }

    /**
     * Create a value expression.
     *
     * @param expression a JBoss EL value expression
     * @param type       the type of the value
     */
	public <T> ValueExpression<T> createValueExpression(final String expression, final Class<T> type) {

        return new ValueExpression<T>() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -7691165008753700929L;
			private javax.el.ValueExpression facesValueExpression;
            private javax.el.ValueExpression seamValueExpression;

            public javax.el.ValueExpression toUnifiedValueExpression() {
                if (isFacesContextActive()) {
                    if (seamValueExpression == null) {
                        seamValueExpression = createExpression();
                    }
                    return seamValueExpression;
                } else {
                    if (facesValueExpression == null) {
                        facesValueExpression = createExpression();
                    }
                    return facesValueExpression;
                }
            }

            private javax.el.ValueExpression createExpression() {
                return getExpressionFactory().createValueExpression(getELContext(), expression, type);
            }

            
			public T getValue() {
                return (T) toUnifiedValueExpression().getValue(getELContext());
            }

            public void setValue(T value) {
                toUnifiedValueExpression().setValue(getELContext(), value);
            }

            public String getExpressionString() {
                return expression;
            }

			public Class<T> getType() {
                return (Class<T>) toUnifiedValueExpression().getType(getELContext());
            }

        };
    }

    /**
     * Create a method expression.
     *
     * @param expression a JBoss EL method expression
     * @param type       the method return type
     * @param argTypes   the method parameter types
     */
    
	public <T> MethodExpression<T> createMethodExpression(final String expression, final Class<T> type, final Class<T>... argTypes) {
        return new MethodExpression<T>() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -8104696883667153519L;
			private javax.el.MethodExpression facesMethodExpression;
            private javax.el.MethodExpression seamMethodExpression;

            public javax.el.MethodExpression toUnifiedMethodExpression() {
                if (isFacesContextActive()) {
                    if (seamMethodExpression == null) {
                        seamMethodExpression = createExpression();
                    }
                    return seamMethodExpression;
                } else {
                    if (facesMethodExpression == null) {
                        facesMethodExpression = createExpression();
                    }
                    return facesMethodExpression;
                }
            }

            private javax.el.MethodExpression createExpression() {
                return getExpressionFactory().createMethodExpression(getELContext(), expression, type, argTypes);
            }

			public T invoke(Object... args) {
                return (T) toUnifiedMethodExpression().invoke(getELContext(), args);
            }

            public String getExpressionString() {
                return expression;
            }

        };
    }

    /**
     * A value expression - an EL expression that evaluates to
     * an attribute getter or get/set pair. This interface
     * is just a genericized version of the Unified EL ValueExpression
     * interface.
     *
     * @author Gavin King
     * @param <T> the type of the value
     */
    public static interface ValueExpression<T> extends Serializable { // todo: see if this custom ValueExpression implementation is really necessary
        public T getValue();

        public void setValue(T value);

        public String getExpressionString();

        public Class<T> getType();

        /**
         * @return the underlying Unified EL ValueExpression
         */
        public javax.el.ValueExpression toUnifiedValueExpression();
    }

    /**
     * A method expression - an EL expression that evaluates to
     * a method. This interface is just a genericized version of
     * the Unified EL ValueExpression interface.
     *
     * @author Gavin King
     * @param <T> the method return type
     */
    public static interface MethodExpression<T> extends Serializable { // todo: see if this custom ValueExpression implementation is really necessary
        public T invoke(Object... args);

        public String getExpressionString();

        /**
         * @return the underlying Unified EL MethodExpression
         */
        public javax.el.MethodExpression toUnifiedMethodExpression();
    }

    protected boolean isFacesContextActive() {
        return false;
    }

    
    public static Expressions instance() {
        if (ourInstance == null) {
            ourInstance = new Expressions();
        }
        return ourInstance;
    }
}

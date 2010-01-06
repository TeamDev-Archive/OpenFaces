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
package org.openfaces.validation.core;

import java.util.Locale;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;
import javax.el.VariableMapper;

import org.jboss.el.ExpressionFactoryImpl;
import org.jboss.el.lang.FunctionMapperImpl;
import org.jboss.el.lang.VariableMapperImpl;

/**
 * An instance of JBoss EL.
 * 
 * @author Gavin King
 * @author Eugene Goncharov
 * 
 */
public class EL {
	public static final ELResolver EL_RESOLVER = createELResolver();
	public static final ELContext EL_CONTEXT = createELContext(EL_RESOLVER,
			new FunctionMapperImpl());

	public static final ExpressionFactory EXPRESSION_FACTORY = new ExpressionFactoryImpl();

	private static ELResolver createELResolver() {
		CompositeELResolver resolver = new CompositeELResolver();
		// resolver.add( new SeamELResolver() );
		resolver.add(new MapELResolver());
		resolver.add(new ListELResolver());
		resolver.add(new ArrayELResolver());
		resolver.add(new ResourceBundleELResolver());
		resolver.add(new BeanELResolver());
		return resolver;
	}

	public static ELContext createELContext(final ELResolver resolver,
			final FunctionMapper functionMapper) {
		return new ELContext() {
			final VariableMapperImpl variableMapper = new VariableMapperImpl();

			@Override
			public ELResolver getELResolver() {
				return resolver;
			}

			@Override
			public FunctionMapper getFunctionMapper() {
				return functionMapper;
			}

			@Override
			public VariableMapper getVariableMapper() {
				return variableMapper;
			}

		};
	}

	public static ELContext createELContext(final ELContext context,
			final ELResolver resolver) {
		return new ELContext() {

			@Override
			public Locale getLocale() {
				return context.getLocale();
			}

			@Override
			public void setPropertyResolved(boolean value) {
				super.setPropertyResolved(value);
				context.setPropertyResolved(value);
			}

			/*
			 * @Override public boolean isPropertyResolved() { return
			 * super.isPropertyResolved(); }
			 */

			@SuppressWarnings("unchecked")
			@Override
			//SuppressWarnings is used for meet requirements for overriding superclass method
			public void putContext(Class clazz, Object object) {
				super.putContext(clazz, object);
				context.putContext(clazz, object);
			}

			@SuppressWarnings("unchecked")
			@Override
			//SuppressWarnings is used for meet requirements for overriding superclass method
			public Object getContext(Class clazz) {
				return context.getContext(clazz);
			}

			@Override
			public void setLocale(Locale locale) {
				super.setLocale(locale);
				context.setLocale(locale);
			}

			@Override
			public ELResolver getELResolver() {
				return resolver;
			}

			@Override
			public FunctionMapper getFunctionMapper() {
				return context.getFunctionMapper();
			}

			@Override
			public VariableMapper getVariableMapper() {
				return context.getVariableMapper();
			}

		};
	}

}

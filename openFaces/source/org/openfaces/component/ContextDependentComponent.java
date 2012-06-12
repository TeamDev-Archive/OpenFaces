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

package org.openfaces.component;

/**
 * @author Dmitry Pikhulya
 */
public interface ContextDependentComponent {
    /**
     * Enters the context (for example declares the request scope variables) required by this component prior to working
     * with this component (e.g. rendering its children, etc.)
     *
     * @return an implementation of Runnable which should be used to exit the context which is entered by this method
     * after it is no longer needed, or null if the component is already in context
     */
    public Runnable enterComponentContext();

    /**
     * @return true if this component is currently in context, which means that its enterComponentContext has been
     * invoked but the context has not been exited from yet
     */
    boolean isComponentInContext();
}

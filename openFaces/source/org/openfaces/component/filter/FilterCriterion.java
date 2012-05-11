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

import java.io.Serializable;

/**
 * An abstract base class for all filter criterion classes. A filter criterion is an entity that identifies a rule for
 * passing or rejecting data objects during the filtering procedure. The actual rule is defined in the concrete
 * implementations of this class.
 *
 * @author Dmitry Pikhulya
 */
public abstract class FilterCriterion implements Serializable {
    public abstract boolean acceptsAll();

    public abstract Object process(FilterCriterionProcessor processor);
}

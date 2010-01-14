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

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract base class for a criterion that aggregates several filter criteria.
 *
 * @author Dmitry Pikhulya
 */
public abstract class CompositeFilterCriterion extends FilterCriterion {
    private List<FilterCriterion> criteria;

    protected CompositeFilterCriterion() {
        this.criteria = new ArrayList<FilterCriterion>();
    }

    protected CompositeFilterCriterion(List<FilterCriterion> criteria) {
        if (criteria == null)
            throw new IllegalArgumentException("criteria shouldn't be null");
        this.criteria = criteria;
    }

    public List<FilterCriterion> getCriteria() {
        return criteria;
    }
}

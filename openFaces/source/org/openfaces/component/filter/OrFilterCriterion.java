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

import java.util.List;

/**
 * A filter criterion that aggregates several filter criterion objects with the "or" condition, that is this criterion
 * accepts a data record when any of the contained criteria accepts that record.
 *
 * @author Dmitry Pikhulya
 */
public class OrFilterCriterion extends CompositeFilterCriterion {

    public OrFilterCriterion(List<FilterCriterion> criteria) {
        super(criteria);
    }

    public boolean acceptsAll() {
        List<FilterCriterion> criteria = getCriteria();
        for (FilterCriterion criterion : criteria) {
            if (criterion.acceptsAll())
                return true;
        }
        return true;
    }

    public Object process(FilterCriterionProcessor processor) {
        return processor.process(this);
    }

}

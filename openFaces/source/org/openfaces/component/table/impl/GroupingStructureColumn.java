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

package org.openfaces.component.table.impl;

import org.openfaces.component.ContextDependentComponent;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.SyntheticColumn;
import org.openfaces.component.table.TreeColumn;
import org.openfaces.renderkit.TableUtil;

import javax.faces.component.UIComponent;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class GroupingStructureColumn extends TreeColumn implements ContextDependentComponent, SyntheticColumn {

    // delegate is not stored in state and is used only temporarily for a functionality that
    // "converts" an ordinary column to a TreeColumn implicitly, during the rendering phase without affecting
    // components' state
    private BaseColumn delegate;

    public GroupingStructureColumn(BaseColumn originalColumn) {
        if (originalColumn == null)
            throw new IllegalArgumentException("originalColumn must be specified");
        TableUtil.copyColumnAttributes(originalColumn, this);
        delegate = originalColumn;
    }

    public BaseColumn getDelegate() {
        return delegate;
    }

    @Override
    public AbstractTable getTable() {
        return delegate.getTable();
    }

    public List<UIComponent> getChildrenForProcessing() {
        return delegate.getChildren();
    }

    @Override
    public UIComponent getFacet(String name) {
        UIComponent facet = super.getFacet(name);
        if (facet != null) return facet;
        return delegate.getFacet(name);
    }

    public Runnable enterComponentContext() {
        return delegate instanceof ContextDependentComponent
                ? ((ContextDependentComponent) delegate).enterComponentContext()
                : null;
    }

    public boolean isComponentInContext() {
        return !(delegate instanceof ContextDependentComponent) ||
                ((ContextDependentComponent) delegate).isComponentInContext();
    }
}

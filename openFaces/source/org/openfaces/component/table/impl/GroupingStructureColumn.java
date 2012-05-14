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

import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.TreeColumn;
import org.openfaces.renderkit.TableUtil;

import javax.faces.component.UIComponent;

/**
 * @author Dmitry Pikhulya
 */
public class GroupingStructureColumn extends TreeColumn {
    public GroupingStructureColumn(BaseColumn originalColumn) {
        TableUtil.copyColumnAttributes(originalColumn, this);
        setDelegate(originalColumn);

    }

    @Override
    public UIComponent getFacet(String name) {
        UIComponent facet = super.getFacet(name);
        if (facet != null) return facet;
        return getDelegate().getFacet(name);
    }


}

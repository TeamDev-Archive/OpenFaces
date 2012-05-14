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
package org.openfaces.taglib.jsp.table;

import org.openfaces.taglib.internal.table.TreeColumnTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class TreeColumnJspTag extends ColumnJspTag {

    public TreeColumnJspTag() {
        super(new TreeColumnTag());
    }

    public void setLevelIndent(ValueExpression levelIndent) {
        getDelegate().setPropertyValue("levelIndent", levelIndent);
    }

    public void setExpansionToggleCellStyle(ValueExpression expansionToggleCellStyle) {
        getDelegate().setPropertyValue("expansionToggleCellStyle", expansionToggleCellStyle);
    }

    public void setExpansionToggleCellClass(ValueExpression expansionToggleCellClass) {
        getDelegate().setPropertyValue("expansionToggleCellClass", expansionToggleCellClass);
    }

    public void setShowAsTree(ValueExpression showAsTree) {
        getDelegate().setPropertyValue("showAsTree", showAsTree);
    }

    public void setExpandedToggleImageUrl(ValueExpression expandedToggleImageUrl) {
        getDelegate().setPropertyValue("expandedToggleImageUrl", expandedToggleImageUrl);
    }

    public void setCollapsedToggleImageUrl(ValueExpression collapsedToggleImageUrl) {
        getDelegate().setPropertyValue("collapsedToggleImageUrl", collapsedToggleImageUrl);
    }
}

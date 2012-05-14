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
package org.openfaces.taglib.jsp.panel;

import org.openfaces.taglib.jsp.AbstractComponentJspTag;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.el.ValueExpression;
import javax.el.MethodExpression;

/**
 * @author Dmitry Pikhulya
 */
public class MultiPageContainerJspTag extends AbstractComponentJspTag {
    public MultiPageContainerJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setSelectedIndex(ValueExpression selectedIndex) {
        getDelegate().setPropertyValue("selectedIndex", selectedIndex);
    }

    public void setLoadingMode(ValueExpression loadingMode) {
        getDelegate().setPropertyValue("loadingMode", loadingMode);
    }

    public void setSelectionChangeListener(MethodExpression selectionChangeListener) {
        getDelegate().setPropertyValue("selectionChangeListener", selectionChangeListener);
    }

    public void setContainerStyle(ValueExpression containerStyle) {
        getDelegate().setPropertyValue("containerStyle", containerStyle);
    }

    public void setContainerClass(ValueExpression containerClass) {
        getDelegate().setPropertyValue("containerClass", containerClass);
    }

    public void setImmediate(ValueExpression immediate) {
        getDelegate().setPropertyValue("immediate", immediate);
    }
    
}

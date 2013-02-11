/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.renderkit.input;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import java.util.List;

/**
 * @author Sergey Pensov
 */
public class MockItem extends UISelectItem {
    List<UIComponent> childrenComponents;

    public void setChildren(List<UIComponent> childrenComponents) {
        this.childrenComponents = childrenComponents;
    }

    public MockItem() {
        super();
    }

    @Override
    public List<UIComponent> getChildren() {
        return childrenComponents;
    }

    @Override
    public int getChildCount() {
        return childrenComponents.size();
    }
}

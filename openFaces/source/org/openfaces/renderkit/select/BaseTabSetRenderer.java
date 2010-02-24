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
package org.openfaces.renderkit.select;

import org.openfaces.component.select.TabPlacement;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Andrew Palval
 */
public abstract class BaseTabSetRenderer extends RendererBase {
    protected static final String SELECTED_INDEX_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "selected";

    protected static final CircularList borderNames = new CircularList(new Object[]{
            TabPlacement.LEFT,
            TabPlacement.TOP,
            TabPlacement.RIGHT,
            TabPlacement.BOTTOM});

    @Override
    public void decode(FacesContext context, UIComponent component) {
        String indexKey = component.getClientId(context) + getSelectionHiddenFieldSuffix();
        String tabIndexStr = context.getExternalContext().getRequestParameterMap().get(indexKey);
        if (tabIndexStr == null)
            return;
        int selectedIndex = Integer.parseInt(tabIndexStr);
        changeTabIndex(component, selectedIndex);
    }

    protected abstract String getSelectionHiddenFieldSuffix();

    protected abstract void changeTabIndex(UIComponent component, int tabIndex);

    protected static class CircularList {
        private List<Object> items = new LinkedList<Object>();

        public CircularList(Collection<Object> items) {
            this.items = new LinkedList<Object>(items);
        }

        public CircularList(Object[] items) {
            this.items = new LinkedList<Object>(Arrays.asList(items));
        }

        public List<Object> getListWithFirst(Object firstItem, int offset) {
            if (firstItem == null) throw new IllegalArgumentException("Parameter 'firstItem' is null.");

            List<Object> list1 = new LinkedList<Object>();
            List<Object> list2 = new LinkedList<Object>();

            int itemIndex = items.indexOf(firstItem);
            if (itemIndex == -1) return null;

            // trim to list size
            boolean overflow = (itemIndex + offset) < 0 || (itemIndex + offset) >= items.size();
            if (overflow) {
                offset = offset % items.size();
            }

            // circle index
            int offsettedIndex = itemIndex + offset;
            if (offsettedIndex >= items.size()) {
                offsettedIndex = offsettedIndex - items.size();
            } else if (offsettedIndex < 0) {
                offsettedIndex = offsettedIndex + items.size();
            }

            for (int i = 0; i < items.size(); i++) {
                Object currentObject = items.get(i);
                if (i < offsettedIndex) {
                    list2.add(currentObject);
                } else {
                    list1.add(currentObject);
                }
            }

            list1.addAll(list2);
            return list1;
        }
    }
}

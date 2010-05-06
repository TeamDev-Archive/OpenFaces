/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.screenshot;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

public class ScreenshotBean {

    private List<SelectItem> items;
    private List selectedItem;

    public ScreenshotBean() {
        items = new ArrayList<SelectItem>();
        items.add(new SelectItem("#FF0000", "red", "description 1"));
        items.add(new SelectItem("#0000FF", "blue", "description 2"));
        items.add(new SelectItem("#FFFF00", "yellow", "description 3"));
        items.add(new SelectItem("#008000", "green", "description 4", true));
        items.add(new SelectItem("#FFA500", "orange", "description 5", true));
        items.add(new SelectItem("#00008B", "darkblue", "description 6"));
        items.add(new SelectItem("#800080", "purple", "description 7"));
        items.add(new SelectItem("#800080", "winee", "description 7"));
        items.add(new SelectItem("#800080", "white", "description 7"));
        items.add(new SelectItem("#800080", "black", "description 7"));
        items.add(new SelectItem("#800080", "silver", "description 7"));
        items.add(new SelectItem("#800080", "gray", "description 7"));
        items.add(new SelectItem("#800080", "pink", "description 7"));
        items.add(new SelectItem("#800080", "navy", "description 7"));


        selectedItem = new ArrayList();
    }

    public List<SelectItem> getItems() {
        return items;
    }

    public void setItems(List<SelectItem> items) {
        this.items = items;
    }

    public List getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(List selectedItem) {
        this.selectedItem = selectedItem;
    }
}

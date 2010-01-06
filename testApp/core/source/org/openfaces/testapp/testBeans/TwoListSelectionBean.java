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
package org.openfaces.testapp.testBeans;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kharchenko
 */
public class TwoListSelectionBean {

    private List<SelectItem> items;
    private List<Object> selectedItem;

    public TwoListSelectionBean() {
        items = new ArrayList<SelectItem>();
        int counter = 1;
        for (int i = 0; i < 100; i++) {
            items.add(new SelectItem("строка " + counter, "красный " + counter, "описание " + counter++));
            items.add(new SelectItem("строка " + counter, "жёлтый" + counter, "описание " + counter++));
            items.add(new SelectItem("строка " + counter, "зелёный" + counter, "описание " + counter++, true));
            items.add(new SelectItem("строка " + counter, "синий" + counter, "описание " + counter++, true));
            items.add(new SelectItem("строка " + counter, "оранжевый" + counter, "описание " + counter++));
            items.add(new SelectItem("строка " + counter, "фиолетовый" + counter, "описание " + counter++));
            items.add(new SelectItem("строка " + counter, "белый" + counter, "описание " + counter++));
            items.add(new SelectItem("строка " + counter, "сиреневый" + counter, "описание " + counter++));
            items.add(new SelectItem("строка " + counter, "чёрный" + counter, "описание " + counter++));
            items.add(new SelectItem("строка " + counter, "коричневый" + counter, "описание " + counter++));
        }

        selectedItem = new ArrayList<Object>();
        selectedItem.add((items.get(0)).getValue());
        selectedItem.add((items.get(2)).getValue());
        selectedItem.add((items.get(4)).getValue());
        selectedItem.add((items.get(5)).getValue());
        selectedItem.add((items.get(6)).getValue());
    }

    public int getItemCount() {
        return items.size();
    }

    public List<SelectItem> getItems() {
        return items;
    }

    public void setItems(List<SelectItem> items) {
        this.items = items;
    }

    public List<Object> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(List<Object> selectedItem) {
        this.selectedItem = selectedItem;
    }

    public boolean testTwoListSelectionValidator(FacesContext context, UIComponent component, Object object) {
        return false;
    }

}

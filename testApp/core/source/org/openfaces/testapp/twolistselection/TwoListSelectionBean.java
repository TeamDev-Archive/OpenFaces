/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.twolistselection;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Darya Shumilina
 */
public class TwoListSelectionBean {

    private List<SelectItem> tlsItems = new ArrayList<SelectItem>();

    public List<SelectItem> getTLSItems() {
        List<SelectItem> tempList = new ArrayList<SelectItem>();
        Random rand = new Random();
        for (int i = 0; i < 7; i++) {
            String currentItem = "TLS item #" + String.valueOf(rand.nextInt(37000));
            SelectItem item = new SelectItem(currentItem, currentItem);
            tempList.add(item);
        }
        tlsItems = tempList;
        return tlsItems;
    }

    public void setTLSItems(List<SelectItem> TLSItems) {
        tlsItems = TLSItems;
    }
}
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

package org.openfaces.testapp.support.QKS820;

import org.openfaces.component.input.DropDownItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class QKS820 {
    private List<DropDownItem> list = new ArrayList<DropDownItem>();
    private String label = "drop down value";


    public QKS820() {
        list.add(new DropDownItem("item1"));
        list.add(new DropDownItem("item2"));
        list.add(new DropDownItem("item3"));
        list.add(new DropDownItem("item4"));
    }


    public List<DropDownItem> getList() {
        return list;
    }

    public void setList(List<DropDownItem> list) {
        this.list = list;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
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

package org.openfaces.testapp.popupmenu;

import javax.faces.event.ActionEvent;

public class PopupMenuBean {
    private String value = "Empty value";

    public PopupMenuBean() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void menuItem2Action() {
        setValue("Menu item 2 pressed");
    }

    public void menuItem3Listener(ActionEvent event) {
        setValue("Menu item 3 pressed");
    }
}

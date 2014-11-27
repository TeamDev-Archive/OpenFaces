/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.test.componentInspector;

import org.openqa.selenium.By;

import java.util.AbstractList;
import java.util.LinkedList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

/**
 * Author: SergeyPensov@teamdev.com
 */
public class TabSetInspector extends Element {
    public TabSetInspector(String id) {
        super(id);
    }

    public List<Element> getTabs() {
        int i = 0;
        List<Element> tabs = new LinkedList<Element>();
        while (true) {
            Element tab = new Element(this.getId() + "::" + i);
            if ( !tab.isElementInPage())
                break;
            tabs.add(tab);

        }
        return tabs;

    }

}

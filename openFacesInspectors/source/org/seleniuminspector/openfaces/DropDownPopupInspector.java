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
package org.seleniuminspector.openfaces;

import org.openfaces.component.input.DropDownPopup;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.TableInspector;

import java.util.AbstractList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class DropDownPopupInspector extends ElementByReferenceInspector {
    public DropDownPopupInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public DropDownPopupInspector(ElementInspector element) {
        super(element);
    }

    public TableInspector itemsTable() {
        return new TableInspector(id() + DropDownPopup.INNER_TABLE_SUFFIX);
    }

    public List<ElementInspector> items() {
        final int itemCount = itemsTable().body().rowCount();
        return new AbstractList<ElementInspector>() {
            public int size() {
                return itemCount;
            }

            public ElementInspector get(int index) {
                return new ElementByLocatorInspector(id() + DropDownPopup.ITEM_PREFIX + index);
            }
        };
    }

    public void assertItemTexts(String[] expectedRowTexts) {
        itemsTable().assertBodyRowTexts(expectedRowTexts);
    }
}

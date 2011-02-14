/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.seleniuminspector.openfaces;

import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementByReferenceInspector;

/**
 * @author Andrii Gorbatov
 */
public class TwoListSelectionSideInspector extends ElementByReferenceInspector {

    public TwoListSelectionSideInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public TwoListSelectionSideInspector(ElementInspector element) {
        super(element);
    }

    public void selectedItem(int itemIndex) {
        evalExpression("childNodes[" + itemIndex + "].selected=true;");
        evalExpression(" onchange();");
    }

    public void unselectedItem(int itemIndex) {
        evalExpression("childNodes[" + itemIndex + "].selected=false;");
        evalExpression(" onchange();");
    }
}

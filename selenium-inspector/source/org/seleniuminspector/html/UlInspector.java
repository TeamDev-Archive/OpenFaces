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
package org.seleniuminspector.html;

import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ElementInspector;

import java.util.AbstractList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class UlInspector extends ElementByReferenceInspector {
    public UlInspector(ElementInspector ulElement) {
        super(ulElement);
        assertNodeName("ul");
    }

    public UlInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
        assertNodeName("ul");
    }

    public List<LiInspector> liElements() {
        final List<ElementInspector> lis = getElementsByTagName("li");
        return new AbstractList<LiInspector>() {
            public int size() {
                return lis.size();
            }

            public LiInspector get(int index) {
                return new LiInspector(lis.get(index));
            }
        };

    }


}

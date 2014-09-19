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

package org.seleniuminspector.openfaces;

import org.openfaces.util.Rendering;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.ElementByLocatorInspector;

import java.lang.reflect.Constructor;

/**
 * @author Andrii Gorbatov
 */
public class ForEachInspector extends ElementByReferenceInspector {
    public ForEachInspector(ElementInspector element) {
        super(element);
    }

    public ForEachInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public ElementInspector item(int index, String embeddedItemId) {
        return item(index, embeddedItemId, ElementByLocatorInspector.class);
    }

    public <T extends ElementInspector> T item(int index, String embeddedItemId, Class<T> itemClass) {
        T item;

        String id = id() + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + index + ":" + embeddedItemId;
        try {
            Constructor<T> elementConstructor = itemClass.getConstructor(String.class);
            item = elementConstructor.newInstance(id);
        } catch (Exception ex) {
            throw new RuntimeException("ForEach item inspection failure: id=" + id, ex);
        }

        return item;
    }
}

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

import org.openfaces.renderkit.select.TwoListSelectionRenderer;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.InputInspector;

/**
 * @author Dmitry Pikhulya
 */
public class TwoListSelectionInspector extends ElementByReferenceInspector {

    public TwoListSelectionInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public TwoListSelectionInspector(ElementInspector element) {
        super(element);
    }

    public TwoListSelectionSideInspector leftList() {
        return new TwoListSelectionSideInspector(id() + TwoListSelectionRenderer.LEFT_LIST_SUFFIX);
    }

    public TwoListSelectionSideInspector rightList() {
        return new TwoListSelectionSideInspector(id() + TwoListSelectionRenderer.RIGHT_LIST_SUFFIX);
    }

    public ElementInspector leftListHeader() {
        return new ElementByLocatorInspector(id() + TwoListSelectionRenderer.LEFT_LIST_HEADER_SUFFIX);
    }

    public ElementInspector rightListHeader() {
        return new ElementByLocatorInspector(id() + TwoListSelectionRenderer.RIGHT_LIST_HEADER_SUFFIX);
    }

    public InputInspector addButton() {
        return new InputInspector(id() + TwoListSelectionRenderer.ADD_BUTTON_SUFFIX);
    }

    public InputInspector addAllButton() {
        return new InputInspector(id() + TwoListSelectionRenderer.ADD_ALL_BUTTON_SUFFIX);
    }

    public InputInspector removeButton() {
        return new InputInspector(id() + TwoListSelectionRenderer.REMOVE_BUTTON_SUFFIX);
    }

    public InputInspector removeAllButton() {
        return new InputInspector(id() + TwoListSelectionRenderer.REMOVE_ALL_BUTTON_SUFFIX);
    }

    public InputInspector moveUpButton() {
        return new InputInspector(id() + TwoListSelectionRenderer.MOVE_UP_BUTTON_SUFFIX);
    }

    public InputInspector moveDownButton() {
        return new InputInspector(id() + TwoListSelectionRenderer.MOVE_DOWN_BUTTON_SUFFIX);
    }

}

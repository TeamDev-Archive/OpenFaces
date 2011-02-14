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

import org.openfaces.renderkit.ComponentWithCaptionRenderer;
import org.openfaces.renderkit.window.ConfirmationRenderer;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.InputInspector;

/**
 * @author Dmitry Pikhulya
 */
public class ConfirmationInspector extends PopupLayerInspector {
    public ConfirmationInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public ConfirmationInspector(ElementInspector element) {
        super(element);
    }

    public ElementInspector caption() {
        return new ElementByLocatorInspector(id() + ComponentWithCaptionRenderer.CAPTION_SUFFIX);
    }

    public ElementInspector message() {
        return new ElementByLocatorInspector(id() + ConfirmationRenderer.MESSAGE_SUFFIX);
    }

    public ElementInspector details() {
        return new ElementByLocatorInspector(id() + ConfirmationRenderer.DETAILS_SUFFIX);
    }

    public InputInspector okButton() {
        return new InputInspector(id() + ConfirmationRenderer.OK_BUTTON_SUFFIX);
    }

    public InputInspector cancelButton() {
        return new InputInspector(id() + ConfirmationRenderer.CANCEL_BUTTON_SUFFIX);
    }

    public ElementInspector buttonArea() {
        return new ElementByLocatorInspector(id() + ConfirmationRenderer.BUTTON_AREA_SUFFIX);
    }

    public ElementInspector iconArea() {
        return new ElementByLocatorInspector(id() + ConfirmationRenderer.ICON_AREA_SUFFIX);
    }

    public ElementInspector content() {
        return new ElementByLocatorInspector(id() + ConfirmationRenderer.MIDDLE_AREA_SUFFIX);
    }

}

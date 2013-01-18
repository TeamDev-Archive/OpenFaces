/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.seleniuminspector.openfaces;

import org.openfaces.renderkit.output.HintLabelRenderer;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.LoadingMode;

/**
 * @author Andrii Gorbatov
 */
public class HintLabelInspector extends ElementByReferenceInspector {

    public HintLabelInspector(ElementInspector element) {
        super(element);
    }

    public HintLabelInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public ElementInspector hint() {
        return new ElementByLocatorInspector(id() + HintLabelRenderer.HINT_SUFFIX);
    }

    public void checkVisibilityAndContent(String value, String hint) {
        ElementInspector hintElement = hint();

        hintElement.assertVisible(false);
        assertVisible(true);
        assertText(value);

        mouseOver();
        sleep(250);
        hintElement.assertVisible(true);
        hintElement.assertText(hint);
    }

    public void showHintLabel() {
        mouseOver();
        sleep(250);

        if (!hint().isVisible()) {
            sleep(100);
        }
    }

}

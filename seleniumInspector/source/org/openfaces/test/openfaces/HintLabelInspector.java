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

package org.openfaces.test.openfaces;

import org.openfaces.renderkit.output.HintLabelRenderer;
import org.openfaces.test.ElementByLocatorInspector;
import org.openfaces.test.ElementByReferenceInspector;
import org.openfaces.test.ElementInspector;
import org.openfaces.test.SeleniumTestCase;

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
        SeleniumTestCase.sleep(250);
        hintElement.assertVisible(true);
        hintElement.assertText(hint);
    }

    public void showHintLabel() {
        mouseOver();
        SeleniumTestCase.sleep(250);

        if (!hint().isVisible()) {
            SeleniumTestCase.sleep(100);
        }
    }

}

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
package org.openfaces.component.miscellaneous;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.ElementInspector;
import org.openfaces.test.OpenFacesTestCase;

/**
 * @author Darya Shumilina
 */
public class ScrollPositionTest extends OpenFacesTestCase {
    @Test
    public void testScrollPositionByPageLoading() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/scrollfocus/scrollPosition.jsf");
        assertEquals("600", selenium.getEval("window.pageYOffset"));
        assertEquals("700", selenium.getEval("window.pageXOffset"));
    }

    @Test
    public void testScrollPositionSaveAfterPageSubmission() {
        testAppFunctionalPage("/components/scrollfocus/scrollPosition.jsf");

        sleep(500);
        ElementInspector inputText = element("formID:inputTextId");
        inputText.focus();
        inputText.setCursorPosition(0);
        sleep(500);
        int ordinate = window().evalIntExpression("pageYOffset");
        int abscissa = window().evalIntExpression("pageXOffset");
        inputText.keyPress(13);
        waitForPageToLoad();
        window().assertExpressionEquals("pageYOffset", ordinate);
        window().assertExpressionEquals("pageXOffset", abscissa);
    }
}
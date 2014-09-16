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
package org.openfaces.component.miscellaneous;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.ElementInspector;

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
        ElementInspector button = element("formID:clickIt");
        sleep(500);
        int ordinate = window().evalIntExpression("pageYOffset");
        int abscissa = window().evalIntExpression("pageXOffset");
        button.evalExpression("click()");
        waitForPageToLoad();
        window().assertExpressionEquals("pageYOffset", ordinate, 22);
        window().assertExpressionEquals("pageXOffset", abscissa);
    }

}
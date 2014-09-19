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
package org.openfaces.component.dynamicimage;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.RichFacesAjaxLoadingMode;

/**
 * @author Darya Shumilina
 */
public class DynamicImageTest extends OpenFacesTestCase {
     @Test
    public void testReRenderThroughA4J() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/dynamicimage/dynamicImage_a4j.jsf");
        String oldValue = selenium.getHtmlSource();
        selenium.click("formID:refresher");
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        String newValue = selenium.getHtmlSource();
        assertFalse(newValue.equals(oldValue));
    }

     @Test
    public void testDefaultView() {
        testAppFunctionalPage("/components/dynamicimage/dynamicImage_defaultView.jsf");
        assertAppearanceNotChanged("DynamicImageDefaultView");
    }
}
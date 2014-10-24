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

package org.openfaces.component.sessionexpiration;

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.openqa.selenium.By;
import org.seleniuminspector.openfaces.ConfirmationInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;

/**
 * @author Eugene Goncharov
 */
public class SessionExpirationTest extends OpenFacesTestCase {
     @Test
    public void testSessionExpiration() {
        testAppFunctionalPage("/components/sessionexpiration/rawTesting.jsf");

        getDriver().findElements(By.tagName("img")).get(0).click();
        sleep(15000);

        ConfirmationInspector confirmation = confirmation("formID:openfaces_internal_sessionexpiration_confirmation");
        confirmation.assertElementExists();
        confirmation.okButton().assertElementExists();

        confirmation.okButton().clickAndWait();
        confirmation.assertElementExists(false);

//     Selenium selenium = getSelenium();
//    testAppFunctionalPage("/components/sessionexpiration/rawTesting.jsf");
//
//    selenium.click("document.getElementsByTagName('img').item(0);");
//    waitForLoadCompletion(OpenFacesAjaxLoadingMode.getInstance());
//
//    ElementInspector confirmation = element("formID:openfaces_internal_sessionexpiration_confirmation");
//    confirmation.assertElementExists();
//
//    ElementInspector yesButton = element("formID:openfaces_internal_sessionexpiration_confirmation::yes_button");
//    yesButton.assertElementExists();
//    yesButton.click();
//    waitForPageToLoad();
//
//    confirmation.assertElementExists(false);
    }
}

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

import com.thoughtworks.selenium.Selenium;
import org.openfaces.test.SeleniumHolder;

/**
 * @author Dmitry Pikhulya
 */
public enum LoadingMode {
    CLIENT,
    SERVER,
    AJAX;

    /**
     * This method should usually be invoked after various client-side actions that require data loading and it waits for
     * completion of a data loading process.
     * <ul>
     * <li> AJAX - waits for an action that triggers ajax data loading (works only with OpenFaces Ajax)
     * <li> SERVER - waits for an action that triggers form submission. Specifying this option will result in waitng for page load completion.
     * <li> CLIENT - the data is preloaded on the client, and no additional waiting is required.
     * </ul>
     */
    public void waitForLoadCompletion() {
        if (this == CLIENT)
            return;
        if (this == AJAX)
            waitForAjax();
        else if (this == SERVER)
            waitForPageToLoad();
        else
            throw new IllegalStateException("Unknown loading mode: " + this);
    }

    private void waitForPageToLoad() {
        Selenium selenium = SeleniumHolder.getInstance().getSelenium();
        selenium.waitForPageToLoad("30000");
        sleep(1000);
    }

    private void waitForAjax() {
        // wait a little while Ajax request starts asynchronously
        sleep(200);
        Selenium selenium = SeleniumHolder.getInstance().getSelenium();
        selenium.waitForCondition("var value = window.document._ajaxInProgressMessage ? window.document._ajaxInProgressMessage.style.display : 'none'; value == 'none';", "30000");
        sleep(1000);
    }


    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

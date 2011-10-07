/*
 * OpenFaces - JSF Component Library 2.0
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

import com.thoughtworks.selenium.Selenium;
import org.seleniuminspector.SeleniumHolder;
import org.seleniuminspector.LoadingMode;

/**
 * @author Eugene Goncharov
 */
public class OpenFacesAjaxLoadingMode extends org.seleniuminspector.LoadingMode {
    private static LoadingMode loadingMode = new OpenFacesAjaxLoadingMode();

    private OpenFacesAjaxLoadingMode(){}


    public static LoadingMode getInstance() {
        return loadingMode;
    }

    public void waitForLoad() {
        // todo: write a correct processing code for OpenFaces Ajax completion without relying on fixed time delays
        
        sleep(200); // wait a little while Ajax request starts asynchronously
        Selenium selenium = SeleniumHolder.getInstance().getSelenium();
        selenium.waitForCondition("var value = window.document._ajaxInProgressMessage ? window.document._ajaxInProgressMessage.style.display : 'none'; value == 'none';", "30000");
        sleep(2000);
    }
}

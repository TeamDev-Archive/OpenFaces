/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.inspector.components;

import org.inspector.api.AjaxSupport;
import org.openqa.selenium.WebDriver;

/**
 * @author Max Yurin
 */
public class AjaxCommandLink extends CommandLink {
    private AjaxSupport ajaxSupport;

    public AjaxCommandLink(WebDriver webDriver, String id) {
        super(webDriver, id);
        this.ajaxSupport = new AjaxSupport(webDriver);
    }

    @Override
    public void click() {
        super.click();
        ajaxSupport.waitAjaxProcess();
    }
}

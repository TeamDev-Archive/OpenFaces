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
package org.seleniuminspector.junit4ex;

import org.junit.runners.model.Statement;
import org.seleniuminspector.SeleniumHolder;
import com.thoughtworks.selenium.Selenium;

/**
 * @author Andrii Gorbatov
 */
public class StartBrowserRun extends Statement {
    private final Statement fNext;

    public StartBrowserRun(Statement next) {
        fNext = next;
    }

    public void evaluate() throws Throwable {
        Selenium selenium = SeleniumHolder.getInstance().getSelenium(false);
        selenium.start();
        fNext.evaluate();
    }

}

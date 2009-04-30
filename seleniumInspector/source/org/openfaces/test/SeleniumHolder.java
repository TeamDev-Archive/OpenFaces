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
package org.openfaces.test;

import com.thoughtworks.selenium.Selenium;

public class SeleniumHolder {

    private static SeleniumHolder seleniumHolder = new SeleniumHolder();

    private SeleniumFactory seleniumFactory;
    private Selenium selenium;

    private SeleniumHolder() {
    }

    public static SeleniumHolder getInstance(){
        return seleniumHolder;
    }

    public void setSeleniumFactory(SeleniumFactory seleniumFactory) {
        this.seleniumFactory = seleniumFactory;
    }

    public Selenium getSelenium() {
        if (selenium == null) {
            if (seleniumFactory == null) {
                throw new RuntimeException("Can't obtain selenium. SeleniumFactory isn't specified.");
            }
            selenium = seleniumFactory.getSelenium();
        }
        return selenium;
    }

}
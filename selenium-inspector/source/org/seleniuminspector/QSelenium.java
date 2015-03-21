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

package org.seleniuminspector;

import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.DefaultSelenium;

/**
 * @author Tatyana Matveyeva
 */
public class QSelenium extends DefaultSelenium {
    public QSelenium(String serverHost, int serverPort, String browserStartCommand, String browserURL, boolean addNamespacesToXpath) {
        super(serverHost, serverPort, browserStartCommand, browserURL);
        this.commandProcessor = new QCommandProcessor(serverHost, serverPort, browserStartCommand, browserURL, addNamespacesToXpath);
    }

    public QSelenium(CommandProcessor commandProcessor) {
        super(commandProcessor);
    }
}
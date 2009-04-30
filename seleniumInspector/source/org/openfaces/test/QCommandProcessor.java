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

import com.thoughtworks.selenium.HttpCommandProcessor;

/**
 * @author Tatyana Matveyeva
 */
public class QCommandProcessor extends HttpCommandProcessor {

    private boolean addNamespacesToXpath = true;

    public QCommandProcessor(String serverHost, int serverPort, String browserStartCommand, String browserURL, boolean addNamespacesToXpath) {
        super(serverHost, serverPort, browserStartCommand, browserURL);
        this.addNamespacesToXpath = addNamespacesToXpath;
    }

    public QCommandProcessor(String pathToServlet, String browserStartCommand, String browserURL) {
        super(pathToServlet, browserStartCommand, browserURL);
    }

    public String doCommand(String commandName, String[] args) {
        if (addNamespacesToXpath && args != null && args.length > 0) {
            String xpathexp = args[0];
            if (xpathexp.startsWith("//")) {
                xpathexp = xpathexp.replaceAll("/table", "/x:table");
                xpathexp = xpathexp.replaceAll("/tbody", "/x:tbody");
                xpathexp = xpathexp.replaceAll("/div", "/x:div");
                xpathexp = xpathexp.replaceAll("/input", "/x:input");
                xpathexp = xpathexp.replaceAll("/tr", "/x:tr");
                xpathexp = xpathexp.replaceAll("/td", "/x:td");
                xpathexp = xpathexp.replaceAll("/span", "/x:span");
                xpathexp = xpathexp.replaceAll("/img", "/x:img");
                xpathexp = xpathexp.replaceAll("/li", "/x:li");
                xpathexp = xpathexp.replaceAll("/a", "/x:a");
            }
            args[0] = xpathexp;
        }
        return super.doCommand(commandName, args);
    }
}
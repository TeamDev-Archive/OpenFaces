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

import java.awt.*;

/**
 * @author Dmitry Pikhulya
 */
public class WindowInspector extends ElementInspector {
    private DocumentInspector document;

    public WindowInspector() {
    }

    public String getElementReferenceExpression() {
        return "window";
    }

    public DocumentInspector document() {
        if (document == null)
            document = new DocumentInspector();
        return document;
    }

    public Dimension size() {
        String sizeStr = evalSeleniumInspectorExpression("getQ__getWindowSize()");
        String[] sizeArr = sizeStr.split(",");
        int width = Integer.parseInt(sizeArr[0]);
        int height = Integer.parseInt(sizeArr[1]);
        return new Dimension(width, height);
    }

    public String toString() {
        return "window";
    }
}
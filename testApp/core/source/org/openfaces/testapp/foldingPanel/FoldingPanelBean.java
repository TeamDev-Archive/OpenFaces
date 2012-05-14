/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.foldingPanel;

import java.util.Random;

/**
 * @author Darya Shumilina
 */
public class FoldingPanelBean {

    public FoldingPanelBean() {
    }

    public String getCurrentContent() {
        Random rand = new Random();
        return "Current content #" + String.valueOf(rand.nextInt(37000));
    }

    public void setCurrentContent(String currentContent) {
    }

    public String getCurrentHeader() {
        Random rand = new Random();
        return "Current header #" + String.valueOf(rand.nextInt(37000));
    }

    public void setCurrentHeader(String currentHeader) {
    }
}
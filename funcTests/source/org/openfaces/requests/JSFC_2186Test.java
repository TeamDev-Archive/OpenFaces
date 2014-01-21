/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.requests;

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.TabbedPaneInspector;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.LoadingMode;

/**
 * @author Pavel Kaplin
 *         <p/>
 *         JSFC-2186 Floating Icon bleeds through to other panes when using the Tabbed Pane
 */
public class JSFC_2186Test extends OpenFacesTestCase {
     @Test
    public void testFloatingIconMessageIsHidedWhenTargetElementIsInvisible() throws InterruptedException {
        testAppFunctionalPage("/requests/JSFC-2186.jsf");

        ElementInspector iconMessage = element("fm:fim");
        iconMessage.assertElementExists(false);
        element("fm:btn").click();
        iconMessage.assertVisible(true);

        TabbedPaneInspector tabbedPane = tabbedPane("fm:tp");
        tabbedPane.tabSet().tabs().get(1).click();
        waitForFloatingIconMessageRepaint();
        iconMessage.assertVisible(false);
        ElementInspector inputText = element("fm:it");
        inputText.assertVisible(false);
        tabbedPane.tabSet().tabs().get(0).click();
        waitForFloatingIconMessageRepaint();
        iconMessage.assertVisible(true);
        inputText.assertVisible(true);
    }

    private void waitForFloatingIconMessageRepaint() throws InterruptedException {
        sleep(500);
    }
}

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
package org.openfaces.testapp.tabbedpane;

import org.openfaces.event.SelectionChangeEvent;

/**
 * @author Darya Shumilina
 */
public class TabbedPaneBean {

    private boolean selectionChangeListenerAttributeWithoutSubmitFuncTest;
    public static boolean testTabbedPaneSelectionChangeListener;

    public TabbedPaneBean() {

    }

    public boolean isSelectionChangeListenerAttributeWithoutSubmitFuncTest() {
        return selectionChangeListenerAttributeWithoutSubmitFuncTest;
    }

    public void setSelectionChangeListenerAttributeWithoutSubmitFuncTest(boolean selectionChangeListenerAttributeWithoutSubmitFuncTest) {
        this.selectionChangeListenerAttributeWithoutSubmitFuncTest = selectionChangeListenerAttributeWithoutSubmitFuncTest;
    }

    public void selectionChangedAttributeWithoutSubmitFunctTest(SelectionChangeEvent event) {
        selectionChangeListenerAttributeWithoutSubmitFuncTest = !selectionChangeListenerAttributeWithoutSubmitFuncTest;
    }

    public boolean isTabbedPaneTestSelectionChangeListener() {
        return testTabbedPaneSelectionChangeListener;
    }

}
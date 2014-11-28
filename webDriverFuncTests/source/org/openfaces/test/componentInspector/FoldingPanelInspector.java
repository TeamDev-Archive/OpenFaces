/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.test.componentInspector;

/**
 * Author: SergeyPensov@teamdev.com
 */
public class FoldingPanelInspector extends Element {

    private static final String TOGGLE_SUF ="--_defaultButtonsArea--toggle";
    public FoldingPanelInspector(String id) {
        super(id);
    }
    public void toggle(){
        if(isElementInPage()){
            Element toggleButton = new Element(this.getId()+TOGGLE_SUF);
            toggleButton.click();
        }
    }

}

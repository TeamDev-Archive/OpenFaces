/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.testing;

import org.openfaces.component.LoadingMode;

/**
 * @author Darya Shumilina
 */
public class LoadingModeItem {

    private LoadingMode value;
    private String label;

    public LoadingModeItem(LoadingMode value, String label) {
        this.label = label;
        this.value = value;
    }

    public LoadingMode getValue() {
        return value;
    }

    public void setValue(LoadingMode value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
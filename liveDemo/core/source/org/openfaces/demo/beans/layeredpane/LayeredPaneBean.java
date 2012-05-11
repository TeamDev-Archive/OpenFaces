/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.demo.beans.layeredpane;

import org.openfaces.component.LoadingMode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class LayeredPaneBean implements Serializable {

    private int loadingModeIndex = 0;

    public LayeredPaneBean() {
    }

    public List<LoadingMode> getLoadingModes() {
        return Arrays.asList(
                LoadingMode.CLIENT,
                LoadingMode.AJAX_LAZY,
                LoadingMode.AJAX_ALWAYS,
                LoadingMode.SERVER
        );
    }

    public LoadingMode getLoadingMode() {
        return getLoadingModes().get(getLoadingModeIndex());
    }


    public int getLoadingModeIndex() {
        return loadingModeIndex;
    }

    public void setLoadingModeIndex(int loadingModeIndex) {
        this.loadingModeIndex = loadingModeIndex;
    }
}

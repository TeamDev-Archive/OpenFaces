/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.selectbooleancheckbox;

public class SelectedViewBean {
    private static final int ORIGINAL_VIEW_INDEX = 0;
    private static final int BEHIND_THE_SCENES_INDEX = 1;

    private int selectedViewIndex = 0;

    public int getSelectedViewIndex() {
        return selectedViewIndex;
    }

    public void setSelectedViewIndex(int selectedViewIndex) {
        this.selectedViewIndex = selectedViewIndex;
    }

    public boolean isOriginalView() {
        return selectedViewIndex == ORIGINAL_VIEW_INDEX;
    }

    public boolean isBehindSceneView() {
        return selectedViewIndex == BEHIND_THE_SCENES_INDEX;
    }
}

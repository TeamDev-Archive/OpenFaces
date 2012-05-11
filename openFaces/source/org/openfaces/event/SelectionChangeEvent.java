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
package org.openfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * @author Andrew Palval
 */
public class SelectionChangeEvent extends FacesEvent {
    private int oldIndex;
    private int newIndex;

    public SelectionChangeEvent(UIComponent uiComponent, int oldIndex, int newIndex) {
        super(uiComponent);
        if (uiComponent == null) throw new IllegalArgumentException("uiComponent");
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
    }

    public int getOldIndex() {
        return oldIndex;
    }

    public int getNewIndex() {
        return newIndex;
    }

    // METHODS
    public boolean isAppropriateListener(FacesListener faceslistener) {
        return faceslistener instanceof SelectionChangeListener;
    }

    public void processListener(FacesListener faceslistener) {
        ((SelectionChangeListener) faceslistener).processSelectionChange(this);
    }
}

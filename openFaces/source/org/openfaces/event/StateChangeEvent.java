/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
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
 * @author Kharchenko
 */
public class StateChangeEvent extends FacesEvent {

    public StateChangeEvent(UIComponent uiComponent) {
        super(uiComponent);
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return facesListener instanceof StateChangeListener;
    }

    public void processListener(FacesListener facesListener) {
        ((StateChangeListener) facesListener).processStateChange(this);
    }
}

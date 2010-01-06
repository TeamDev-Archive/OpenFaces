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
package org.openfaces.component;


import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * @author Dmitry Pikhulya
 */
public class EventWrapperForIterator extends FacesEvent {
    private FacesEvent originalEvent;
    private String objectId;

    public EventWrapperForIterator(FacesEvent facesEvent, String objectId,
                                   OUIObjectIterator redirectComponent) {
        super((UIComponent) redirectComponent);
        originalEvent = facesEvent;
        this.objectId = objectId;
    }

    @Override
    public PhaseId getPhaseId() {
        return originalEvent.getPhaseId();
    }

    @Override
    public void setPhaseId(PhaseId phaseId) {
        originalEvent.setPhaseId(phaseId);
    }

    @Override
    public void queue() {
        originalEvent.queue();
    }

    @Override
    public String toString() {
        return originalEvent.toString();
    }

    public boolean isAppropriateListener(FacesListener faceslistener) {
        return originalEvent.isAppropriateListener(faceslistener);
    }

    public void processListener(FacesListener faceslistener) {
        originalEvent.processListener(faceslistener);
    }

    public FacesEvent getWrappedFacesEvent() {
        return originalEvent;
    }

    public String getObjectId() {
        return objectId;
    }
}

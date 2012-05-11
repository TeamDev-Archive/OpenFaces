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
package org.openfaces.component;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class OUIObjectIteratorBase extends OUIComponentBase implements OUIObjectIterator {

    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (!isVisitable(context))
            return false;
        FacesContext facesContext = context.getFacesContext();
        pushComponentToEL(facesContext, null);
        try {
            VisitResult result = context.invokeVisitCallback(this, callback);

            if (result == VisitResult.COMPLETE)
                return true;

            if (result == VisitResult.ACCEPT) {
                Iterator<UIComponent> directChildren = getDirectChildren();

                if (directChildren != null)
                    while (directChildren.hasNext()) {
                        UIComponent c = directChildren.next();
                        if (c.visitTree(context, callback))
                            return true;
                    }

                Collection<UIComponent> iteratedChildren = getIteratedChildren();
                if (iteratedChildren != null && iteratedChildren.size() > 0) {
                    Iterable<String> objectIds = getObjectIds();
                    String originalObjectId = getObjectId();
                    try {
                        for (String objectId : objectIds) {
                            setObjectId(objectId);
                            for (UIComponent component : iteratedChildren) {
                                if (component.visitTree(context, callback))
                                    return true;
                            }
                        }
                    } finally {
                        setObjectId(originalObjectId);
                    }

                }
            }
            return false;
        } finally {
            popComponentFromEL(facesContext);
        }
    }

    protected Iterator<UIComponent> getDirectChildren() {
        return this.getFacetsAndChildren();
    }

    protected Collection<UIComponent> getIteratedChildren() {
        return Collections.emptyList();
    }

    private List<String> getObjectIds() {
        FacesContext context = getFacesContext();
        String objectIdsKey = "_renderedObjectIds";
        List<String> result = (List<String>) getAttributes().get(objectIdsKey);
        if (result == null) {
            result = new ArrayList<String>();
            getAttributes().put(objectIdsKey, result);
        }
        return result;
    }

    private boolean objectIterationAndRendering;

    public void startObjectIterationAndRendering() {
        if (objectIterationAndRendering)
            throw new IllegalStateException("startObjectIterationAndRendering has already been called on this component: " +
                    getClientId(getFacesContext()));
        getObjectIds().clear();
        objectIterationAndRendering = true;
    }

    public void stopObjectIterationAndRendering() {
        if (!objectIterationAndRendering)
            throw new IllegalStateException("startObjectIterationAndRendering hasn't been called on this component: " +
                    getClientId(getFacesContext()));
        objectIterationAndRendering = false;
    }

    public void setObjectId(String objectId) {
        if (objectIterationAndRendering)
            getObjectIds().add(objectId);
    }

    @Override
    public boolean invokeOnComponent(
            FacesContext context, String clientId, ContextCallback callback) throws FacesException {
        if (getClientId(context).equals(clientId))
            try {
                callback.invokeContextCallback(context, this);
                return true;
            } catch (Exception e) {
                throw new FacesException(e);
            }

        for (UIComponent facet : getFacets().values()) {
            if (facet.invokeOnComponent(context, clientId, callback))
                return true;
        }

        String idPrefix = super.getClientId(context) + OUIObjectIterator.OBJECT_ID_SEPARATOR;

        if (!clientId.startsWith(idPrefix))
            return false;
        String idSuffix = clientId.substring(idPrefix.length());
        int objectIdEndIdx = idSuffix.indexOf(':');
        if (objectIdEndIdx == -1)
            return false;
        String objectId = idSuffix.substring(0, objectIdEndIdx);

        String prevObjectId = getObjectId();
        setObjectId(objectId);
        try {
            for (UIComponent child : getChildren()) {
                if (child.invokeOnComponent(context, clientId, callback))
                    return true;
            }
        } finally {
            setObjectId(prevObjectId);
        }

        return false;
    }

    @Override
    public String getClientId(FacesContext context) {
        String clientId = super.getClientId(context);
        String objectId = getObjectId();
        if (objectId == null)
            return clientId;

        return clientId + OBJECT_ID_SEPARATOR + objectId;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i]);

    }

    @Override
    public void queueEvent(FacesEvent event) {
        super.queueEvent(new EventWrapperForIterator(event, getObjectId(), this));
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (!(event instanceof EventWrapperForIterator)) {
            super.broadcast(event);
            return;
        }
        EventWrapperForIterator eventWrapper = (EventWrapperForIterator) event;
        FacesEvent originalEvent = eventWrapper.getWrappedFacesEvent();
        String objectId = eventWrapper.getObjectId();
        String currentObjectId = getObjectId();
        setObjectId(objectId);
        originalEvent.getComponent().broadcast(originalEvent);
        setObjectId(currentObjectId);
    }
}

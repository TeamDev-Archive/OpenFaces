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

package org.openfaces.component.table;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Dmitry Pikhulya
 */
public class StampStates<S extends StampStates.State> implements StateHolder {
    private static final String DEFAULT_STATE_KEY = "< defaultState >";

    private UIComponent component;
    private Class<S> stampStateClass;

    private Map<String, S> stampStates = new HashMap<String, S>();

    public StampStates(UIComponent component, Class<S> stampStateClass) {
        this.component = component;
        this.stampStateClass = stampStateClass;
    }

    public S currentState(FacesContext context) {
        String contextId = getStampContextId(context);
        boolean defaultState = contextId == null || isDefaultStateId(contextId);
        String stateKey = defaultState ? DEFAULT_STATE_KEY : contextId;
        return stateForKey(stateKey);
    }

    /**
     * Returns the string which identifies the current stamp
     * @param context
     * @return
     */
    protected String getStampContextId(FacesContext context) {
        return component.getClientId(context);
    }

    private S stateForKey(String stateKey) {
        S s = stampStates.get(stateKey);
        if (s == null) {
            s = newStampState();
            // apply the default state to all non-default stamps by default
            if (!stateKey.equals(DEFAULT_STATE_KEY)) {
                S defaultStampState = getDefaultStampState();
                FacesContext context = FacesContext.getCurrentInstance();
                s.restoreState(context, defaultStampState.saveState(context));
            }
            stampStates.put(stateKey, s);
        }
        return s;
    }

    private S getDefaultStampState() {
        return stateForKey(DEFAULT_STATE_KEY);
    }

    /**
     * Identifies whether the current stamp context is the default one or not. The stampContextId variable is auxiliary.
     * It may or may not be used by this method, and value passed in this parameter always refers to the current stamp
     * context (as can be found using the getStampContextId method).
     */
    protected boolean isDefaultStateId(String stampContextId) {
        String[] parts = stampContextId.split(":");
        if (parts.length == 1) {
            // There are two possibilities of why there might be just one client id path component:
            // (1) The component hasn't been placed into the view yet.
            // (2) Another possibility is that this is component is on the top level, and thus its id legally consists
            // of just one part, which means that this component is not located inside of iterator so its safe to report
            // its state as "default" as well.
            return true;
        }
        for (String part : parts) {
            if (part.length() == 0) continue;
            char c = part.charAt(0);
            if (Character.isDigit(c)) {
                // path components starting with a number mean an artificially generated id, which is normally generated
                // by iterator components
                return false;
            }
        }
        return true;
    }

    private S newStampState() {
        S state;
        try {
            state = stampStateClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        state.setComponent(component);
        return state;
    }

    public Object saveState(FacesContext context) {
        Map<String, Object> savedState = new HashMap<String, Object>();
        Set<Map.Entry<String, S>> entries = stampStates.entrySet();
        for (Map.Entry<String, S> entry : entries) {
            Object entryState = entry.getValue().saveState(context);
            savedState.put(entry.getKey(), entryState);
        }
        return savedState;
    }

    public void restoreState(FacesContext context, Object state) {
        stampStates = new HashMap<String, S>();
        Map<String, Object> stateMap = (Map<String, Object>) state;
        Set<Map.Entry<String, Object>> entries = stateMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            S restoredStampState = newStampState();
            restoredStampState.restoreState(context, entry.getValue());
            stampStates.put(entry.getKey(), restoredStampState);
        }
    }

    public boolean isTransient() {
        return false;
    }

    public void setTransient(boolean newTransientValue) {
        throw new UnsupportedOperationException();
    }

    public static class State implements StateHolder {
        private UIComponent component;

        public State() {
        }

        public UIComponent getComponent() {
            return component;
        }

        public void setComponent(UIComponent component) {
            this.component = component;
        }

        public Object saveState(FacesContext context) {
            return null;
        }

        public void restoreState(FacesContext context, Object state) {
        }

        public boolean isTransient() {
            return false;
        }

        public void setTransient(boolean newTransientValue) {
            throw new UnsupportedOperationException();
        }
    }

}

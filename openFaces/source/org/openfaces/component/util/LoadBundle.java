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
package org.openfaces.component.util;

import org.openfaces.util.ValueBindings;

import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.Serializable;
import java.util.*;

/**
 * The LoadBundle component is used for loading a resource bundle localized for the
 * Locale of the current view, and expose it (as a Map) in the request attributes of
 * the current request. The OpenFaces LoadBundle component is similar to the LoadBundle
 * from the RI or MyFaces JSF implementation but can be used in the components with Ajax enabled.
 *
 * @author Vladimir Kurganov
 */
public class LoadBundle extends UIComponentBase implements Serializable, AjaxLoadBundleComponent {

    public static final String COMPONENT_TYPE = "org.openfaces.LoadBundle";
    public static final String COMPONENT_FAMILY = "org.openfaces.LoadBundle";

    private String var;
    private String basename;

    public LoadBundle() {
        FacesContext.getCurrentInstance().getApplication().subscribeToEvent(PreRenderViewEvent.class, new SystemEventListener() {
            public void processEvent(SystemEvent event) throws AbortProcessingException {
                loadBundle(getFacesContext());
            }

            public boolean isListenerForSource(Object source) {
                return true;
            }
        });
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void loadBundle(FacesContext context) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String initializedKey = LoadBundle.class + ".loadBundle._initialized";
        if (requestMap.containsKey(initializedKey)) return;
        requestMap.put(initializedKey, true);

        if (null == basename || null == var) {
            throw new FacesException("null basename or var");
        }
        Locale locale = context.getViewRoot().getLocale();
        if (locale == null) {
            locale = context.getApplication().getDefaultLocale();
        }

        final ResourceBundle bundle = ResourceBundle.getBundle(basename, locale, Thread.currentThread().getContextClassLoader());
        if (null == bundle) {
            throw new FacesException("null ResourceBundle for " + basename);
        }
        Map toStore = new Map() {
            // this is an immutable Map

            public String toString() {
                StringBuilder sb = new StringBuilder();
                Iterator entries = this.entrySet().iterator();
                Map.Entry cur;
                while (entries.hasNext()) {
                    cur = (Entry) entries.next();
                    sb.append(cur.getKey()).append(": ").append(cur.getValue()).append('\n');
                }

                return sb.toString();
            }

            public void clear() {
                throw new UnsupportedOperationException();
            }


            public boolean containsKey(Object key) {
                boolean result = false;
                if (null != key) {
                    result = (null != bundle.getObject(key.toString()));
                }
                return result;
            }


            public boolean containsValue(Object value) {
                Enumeration<String> keys = bundle.getKeys();
                boolean result = false;
                while (keys.hasMoreElements()) {
                    Object curObj = bundle.getObject(keys.nextElement());
                    if (curObj == value || (null != curObj && curObj.equals(value))) {
                        result = true;
                        break;
                    }
                }
                return result;
            }


            public Set entrySet() {
                HashMap<Object, Object> mappings = new HashMap<Object, Object>();
                Enumeration<String> keys = bundle.getKeys();
                while (keys.hasMoreElements()) {
                    Object key = keys.nextElement();
                    Object value = bundle.getObject((String) key);
                    mappings.put(key, value);
                }
                return mappings.entrySet();
            }


            public boolean equals(Object obj) {
                return !(obj == null || !(obj instanceof Map)) &&
                        entrySet().equals(((Map) obj).entrySet());
            }


            public Object get(Object key) {
                if (null == key) {
                    return null;
                }
                try {
                    return bundle.getObject(key.toString());
                } catch (MissingResourceException e) {
                    return "???" + key + "???";
                }
            }


            public int hashCode() {
                return bundle.hashCode();
            }


            public boolean isEmpty() {
                Enumeration<String> keys = bundle.getKeys();
                boolean result = !keys.hasMoreElements();
                return result;
            }


            public Set keySet() {
                Set<String> keySet = new HashSet<String>();
                Enumeration<String> keys = bundle.getKeys();
                while (keys.hasMoreElements()) {
                    keySet.add(keys.nextElement());
                }
                return keySet;
            }


            // Do not need to implement for immutable Map
            public Object put(Object k, Object v) {
                throw new UnsupportedOperationException();
            }


            // Do not need to implement for immutable Map
            public void putAll(Map t) {
                throw new UnsupportedOperationException();
            }


            // Do not need to implement for immutable Map
            public Object remove(Object k) {
                throw new UnsupportedOperationException();
            }


            public int size() {
                int result = 0;
                Enumeration<String> keys = bundle.getKeys();
                while (keys.hasMoreElements()) {
                    keys.nextElement();
                    result++;
                }
                return result;
            }


            public java.util.Collection values() {
                List<Object> result = new ArrayList<Object>();
                Enumeration<String> keys = bundle.getKeys();
                while (keys.hasMoreElements()) {
                    result.add(bundle.getObject(keys.nextElement()));
                }
                return result;
            }
        };

        requestMap.put(var, toStore);
    }

    public String getBasename() {
        return ValueBindings.get(this, "basename", basename);
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                var,
                basename
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        var = (String) values[i++];
        basename = (String) values[i++];
    }

}

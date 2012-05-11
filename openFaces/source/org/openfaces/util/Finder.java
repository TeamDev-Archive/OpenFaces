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
package org.openfaces.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public abstract class Finder {

    private HashMap<UIComponent, String> componentsChain = new HashMap<UIComponent, String>();

    public Set<UIComponent> getComponentsChain() {
        return componentsChain.keySet();
    }

    public HashMap<UIComponent, String> getComponentsChainWithIds() {
        return componentsChain;
    }

    public String getComponentId(UIComponent uiComponent) {
        return componentsChain.get(uiComponent);
    }

    public Collection<String> getRender() {
        return componentsChain.values();
    }

    public UIComponent getComponent() {
        if (componentsChain.size() == 0)
            return null;

        return componentsChain.keySet().iterator().next();

    }

    public Finder(UIComponent rootComponent) {
        FacesContext context = FacesContext.getCurrentInstance();
        findComponentChain(rootComponent, context);
    }

    public void findComponentChain(UIComponent component, FacesContext context) {
        if (test(component)) {
            if (context != null)
                componentsChain.put(component, component.getClientId(context));
            else
                componentsChain.put(component, null);
        }

        List<UIComponent> children = component.getChildren();
        for (UIComponent child : children) {
            findComponentChain(child, context);
        }

    }

    public abstract boolean test(UIComponent component);
}
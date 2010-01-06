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
package org.openfaces.taglib;

import org.openfaces.component.CompoundComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class TagUtil {

    private TagUtil() {
    }

    public static void initComponentChildren(FacesContext facesContext, UIComponent component) {
        if (!(component instanceof CompoundComponent))
            return;

        Map<String, Object> attributes = component.getAttributes();
        if (attributes.containsKey("_of_childrenCreated"))
            return; // avoid repetitive initializations under Facelets (Facelets tag handlers are processed on each rendering)
        attributes.put("_of_childrenCreated", Boolean.TRUE);

        CompoundComponent compoundComponent = ((CompoundComponent) component);
        compoundComponent.createSubComponents(facesContext);
    }
}

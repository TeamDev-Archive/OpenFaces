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
package org.openfaces.taglib.internal.panel;

import org.openfaces.component.LoadingMode;
import org.openfaces.event.SelectionChangeEvent;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public abstract class MultiPageContainerTag extends AbstractComponentTag {

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setIntProperty(component, "selectedIndex");
        setEnumerationProperty(component, "loadingMode", LoadingMode.class);
        setMethodExpressionProperty(facesContext, component, "selectionChangeListener",
                new Class[]{SelectionChangeEvent.class}, void.class);

        setStringProperty(component, "containerStyle");
        setStringProperty(component, "containerClass");

        setBooleanProperty(component, "immediate");
    }

}

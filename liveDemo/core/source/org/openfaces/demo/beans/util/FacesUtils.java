/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.List;

public class FacesUtils {
    private FacesUtils() {

    }

    @SuppressWarnings("unchecked")
    public static <E> E getEventParameter(AjaxBehaviorEvent event, String paramName) {
        List<UIComponent> children = event.getComponent().getChildren();
        for (UIComponent component : children) {
            if (component instanceof UIParameter) {
                UIParameter uiParameter = (UIParameter) component;
                if (paramName.equals(uiParameter.getName())) {
                    return (E) uiParameter.getValue();
                }
            }
        }
        return null;
    }
}
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
package org.openfaces.taglib.internal.command;

import org.openfaces.component.command.PopupMenu;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Vladimir Kurganov
 */
public class PopupMenuTag extends AbstractComponentTag {
    public String getComponentType() {
        return PopupMenu.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.PopupMenuRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent uiComponent) {
        super.setComponentProperties(facesContext, uiComponent);

        setStringProperty(uiComponent, "for");
        setStringProperty(uiComponent, "event");
        setBooleanProperty(uiComponent, "standalone");

        setIntProperty(uiComponent, "submenuShowDelay");
        setIntProperty(uiComponent, "submenuHideDelay");

        setBooleanProperty(uiComponent, "indentVisible");
        setBooleanProperty(uiComponent, "selectDisabledItems");

        setStringProperty(uiComponent, "itemIconUrl");
        setStringProperty(uiComponent, "disabledItemIconUrl");
        setStringProperty(uiComponent, "selectedItemIconUrl");
        setStringProperty(uiComponent, "selectedDisabledItemIconUrl");

        setStringProperty(uiComponent, "submenuImageUrl");
        setStringProperty(uiComponent, "disabledSubmenuImageUrl");
        setStringProperty(uiComponent, "selectedSubmenuImageUrl");
        setStringProperty(uiComponent, "selectedDisabledSubmenuImageUrl");

        setStringProperty(uiComponent, "itemStyle");
        setStringProperty(uiComponent, "itemClass");
        setStringProperty(uiComponent, "selectedItemStyle");
        setStringProperty(uiComponent, "selectedItemClass");
        setStringProperty(uiComponent, "disabledItemStyle");
        setStringProperty(uiComponent, "disabledItemClass");
        setStringProperty(uiComponent, "itemContentStyle");
        setStringProperty(uiComponent, "itemContentClass");
        setStringProperty(uiComponent, "indentStyle");
        setStringProperty(uiComponent, "indentClass");
        setStringProperty(uiComponent, "itemIndentStyle");
        setStringProperty(uiComponent, "itemIndentClass");
        setStringProperty(uiComponent, "itemSubmenuIconStyle");
        setStringProperty(uiComponent, "itemSubmenuIconClass");
        setStringProperty(uiComponent, "selectedDisabledItemStyle");
        setStringProperty(uiComponent, "selectedDisabledItemClass");

        setStringProperty(uiComponent, "onshow");
        setStringProperty(uiComponent, "onhide");

    }


}

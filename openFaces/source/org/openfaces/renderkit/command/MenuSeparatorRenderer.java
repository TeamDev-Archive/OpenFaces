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
package org.openfaces.renderkit.command;


import org.openfaces.component.command.MenuSeparator;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Vladimir Kurganov
 */
public class MenuSeparatorRenderer extends RendererBase {

    private static final String DEFAULT_LIST_ITEM_CLASS = "o_menu_list_item o_menu_list_item_separator";
    private static final String DEFAULT_MENU_SEPARATOR_CLASS = "o_menu_separator";
    private static final String DIV_PREFIX = "::separator";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        MenuSeparator menuSeparator = (MenuSeparator) component;
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("li", menuSeparator);
        writeAttribute(writer, "id", menuSeparator.getClientId(context));
        writeAttribute(writer, "class", DEFAULT_LIST_ITEM_CLASS);
        writer.startElement("span", menuSeparator);
        writeAttribute(writer, "id", menuSeparator.getClientId(context) + DIV_PREFIX);

        String styleClass = Styles.getCSSClass(context, menuSeparator, menuSeparator.getStyle(), StyleGroup.regularStyleGroup(),
                menuSeparator.getStyleClass(), DEFAULT_MENU_SEPARATOR_CLASS);
        writeAttribute(writer, "class", styleClass);

        Styles.renderStyleClasses(context, menuSeparator);

        writer.endElement("span");
        writer.endElement("li");
    }

}
 

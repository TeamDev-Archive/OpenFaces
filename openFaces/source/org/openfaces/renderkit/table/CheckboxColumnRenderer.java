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
package org.openfaces.renderkit.table;

import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class CheckboxColumnRenderer extends BaseColumnRenderer {
    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("input", component);
        writer.writeAttribute("type", "checkbox", null);
        Styles.renderStyleClasses(context, component);
        writer.endElement("input");
    }


}

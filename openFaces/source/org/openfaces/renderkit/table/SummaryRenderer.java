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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.Summary;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class SummaryRenderer extends RendererBase {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        Summary summary = (Summary) component;
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", summary);
        writeIdAttribute(context, summary);
        Rendering.writeStyleAndClassAttributes(writer, summary);
        Rendering.writeStandardEvents(writer, summary);

        // The actual content will be assigned on the client side since it might not be available at the moment of
        // rendering of this component yet. The reason is that the summary component can be placed in the column's
        // header which is rendered before the table's data is iterated and summary value(s) calculated.

        writer.endElement("span");
    }
}

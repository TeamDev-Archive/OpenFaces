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

package org.openfaces.renderkit.timetable;

import org.openfaces.component.timetable.TimetableView;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class TimetableRendererBase extends RendererBase {
    protected void renderHeader(FacesContext context, TimetableView timetableView) throws IOException {
        UIComponent header = timetableView.getHeader();
        UIComponent headerRight = timetableView.getHeaderRight();
        if (header == null && headerRight == null) return;

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tr", timetableView);
        writer.startElement("td", timetableView);
        writer.startElement("table", timetableView);
        writeHeaderTableAttributes(writer);
        String headerClass = Styles.getCSSClass(context,
                timetableView, timetableView.getHeaderStyle(), "o_timetableView_header", timetableView.getHeaderClass());
        writer.writeAttribute("class", headerClass, null);

        writer.startElement("tr", timetableView);
        writer.startElement("td", timetableView);
        if (header != null)
            header.encodeAll(context);
        writer.endElement("td");

        if (headerRight != null) {
            writer.startElement("td", timetableView);
            writer.writeAttribute("style", "width: 1px", null);
            writeHeaderRightAreaAttributes(writer, timetableView);
            headerRight.encodeAll(context);
            writer.endElement("td");
        }

        writer.endElement("tr");

        writer.endElement("table");
        writer.endElement("td");
        writer.endElement("tr");
    }

    protected void writeHeaderTableAttributes(ResponseWriter writer) throws IOException {

    }

    protected void writeHeaderRightAreaAttributes(ResponseWriter writer, TimetableView timetableView) throws IOException {

    }

    protected void renderFooter(FacesContext context, TimetableView timetableView) throws IOException {
        UIComponent footer = timetableView.getFooter();
        if (footer == null) return;

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tr", footer);
        writer.startElement("td", footer);
        writer.startElement("table", footer);
        String footerClass = Styles.getCSSClass(context,
                timetableView, timetableView.getFooterStyle(), "o_timetableView_footer", timetableView.getFooterClass());
        writer.writeAttribute("class", footerClass, null);

        writer.startElement("tr", footer);
        writer.startElement("td", footer);
        footer.encodeAll(context);
        writer.endElement("td");
        writer.endElement("tr");

        writer.endElement("table");
        writer.endElement("td");
        writer.endElement("tr");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }
}

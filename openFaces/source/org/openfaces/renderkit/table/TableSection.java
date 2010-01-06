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

import org.openfaces.component.TableStyles;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.TableUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public abstract class TableSection extends TableElement {
    public TableSection(TableElement parent) {
        super(parent);
    }

    public JSONObject getInitParam(TableStyles defaultStyles) {
        JSONObject result = new JSONObject();

        try {
            fillInitParam(result, defaultStyles);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    protected abstract void fillInitParam(JSONObject result, TableStyles defaultStyles) throws JSONException;

    public void render(FacesContext context,
                       HeaderCell.AdditionalContentWriter additionalContentWriter) throws IOException {
        TableStructure tableStructure = getParent(TableStructure.class);
        UIComponent component = tableStructure.getComponent();
        ResponseWriter writer = context.getResponseWriter();

        boolean scrollable = tableStructure.getScrolling() != null;

        if (scrollable) {
            writer.startElement("table", component);
            writer.writeAttribute("style", "width: 100%", null);
            writer.writeAttribute("border", 0, null);
            writer.writeAttribute("cellspacing", 0, null);
            writer.writeAttribute("cellpadding", 0, null);

            if (tableStructure.getLeftFixedCols() > 0)
                TableUtil.writeColTag(component, writer, null, null, null);
            TableUtil.writeColTag(component, writer, null, null, null);
            if (tableStructure.getRightFixedCols() > 0)
                TableUtil.writeColTag(component, writer, null, null, null);
        }

        String sectionTag = getSectionName();
        writer.startElement(sectionTag, component);

        renderRows(context, additionalContentWriter);

        writer.endElement(sectionTag);

        if (scrollable) {
            writer.endElement("table");
        }
    }

    protected abstract void renderRows(
            FacesContext facesContext,
            HeaderCell.AdditionalContentWriter additionalContentWriter
    ) throws IOException;

    public boolean isContentSpecified() {
        return true;
    }

    protected abstract String getSectionName();
}

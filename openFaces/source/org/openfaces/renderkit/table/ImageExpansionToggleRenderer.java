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

import org.openfaces.component.table.ImageExpansionToggle;
import org.openfaces.component.table.ColumnGroup;
import org.openfaces.component.table.TreeColumn;
import org.openfaces.component.table.TreeTable;
import org.openfaces.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class ImageExpansionToggleRenderer extends RendererBase {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);

        boolean nodeExpanded = getTreeTable(component).isNodeExpanded();
        encodeStaticImage(context, (ImageExpansionToggle) component, nodeExpanded, nodeExpanded ? "o_toggle_e" : "o_toggle_c");
    }

    private TreeTable getTreeTable(UIComponent expansionToggle) {
        UIComponent column = expansionToggle.getParent();
        if (!(column instanceof TreeColumn) || ((TreeColumn) column).getExpansionToggle() != expansionToggle)
            throw new IllegalStateException("ImageExpansionToggleRenderer can only be inserted as an 'expansionToggle' facet of TreeColumn");

        UIComponent columnParent = column.getParent();
        while (columnParent instanceof ColumnGroup)
            columnParent = columnParent.getParent();
        TreeTable treeTable = (TreeTable) columnParent;
        return treeTable;
    }


    private void encodeStaticImage(
            FacesContext context,
            ImageExpansionToggle toggle,
            boolean nodeExpanded,
            String className) throws IOException {
        String url = toggle.getToggleImageUrl(context, nodeExpanded);
        TreeColumnRenderer.renderImgTag(context, url, className);
    }

}

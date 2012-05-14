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
package org.openfaces.renderkit.input.fileattachments;

import org.openfaces.component.input.fileattachments.FileAttachments;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.Column;
import org.openfaces.renderkit.DefaultTableStyles;
import org.openfaces.renderkit.TableUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.List;


public class FileAttachmentsTableStyles extends DefaultTableStyles {
    private final FileAttachments fileAttachments;
    private final List<UIComponent> childComponents;

    public FileAttachmentsTableStyles(FileAttachments fileAttachments, List<UIComponent> childComponents) {
        this.fileAttachments = fileAttachments;
        this.childComponents = childComponents;
    }

    public List<BaseColumn> getRenderedColumns() {
        FacesContext context = FacesContext.getCurrentInstance();
        List<BaseColumn> columns = TableUtil.getColumnsFromList(context, fileAttachments.getChildren());
        for (Iterator iterator = columns.iterator(); iterator.hasNext(); ) {
            Column column = (Column) iterator.next();
            if (!column.isRendered())
                iterator.remove();
        }
        if (childComponents.size() > 0 || columns.size() == 0)
            columns.add(new Column());

        return columns;
    }

    public List<BaseColumn> getAllColumns() {
        FacesContext context = FacesContext.getCurrentInstance();
        List<BaseColumn> columns = TableUtil.getColumnsFromList(context, fileAttachments.getChildren());

        if (childComponents.size() > 0 || columns.size() == 0)
            columns.add(new Column());

        return columns;
    }

}

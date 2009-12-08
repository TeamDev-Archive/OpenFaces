/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.table;

import org.openfaces.component.table.AbstractTable;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class ColumnReorderingRenderer extends RendererBase {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        AbstractTable table = (AbstractTable) component.getParent();
        RenderingUtil.renderInitScript(context, new ScriptBuilder().initScript(context, table, 
                "O$.Table._initColumnReordering"), new String[] {
                AbstractTableRenderer.getTableJsURL(context)
        });
    }
}

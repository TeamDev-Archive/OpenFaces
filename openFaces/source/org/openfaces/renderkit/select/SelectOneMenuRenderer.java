/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.select;

import org.openfaces.renderkit.input.DropDownFieldRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectOneMenuRenderer extends DropDownFieldRenderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);

    }

    protected String getClientItemLabel(UISelectItem item, String convertedItemValue) {
        String itemLabel = item.getItemLabel();
        return itemLabel != null ? itemLabel : convertedItemValue;
    }

    protected String getDefaultDropDownClass() {
        return "o_dropdown o_selectonemenu";
    }

}

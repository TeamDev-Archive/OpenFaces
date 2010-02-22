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

package org.openfaces.component.select;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code. Use javax.faces.model.SelectItem class instead.
 *
 * @author Oleg Marshalenko
 */
public class _SelectItem extends UIComponentBase implements Serializable {
    private static final long serialVersionUID = 6188475522203035120L;

    public static final String COMPONENT_TYPE = "org.openfaces.SelectItem";
    public static final String COMPONENT_FAMILY = "org.openfaces.SelectItem";

    private String itemDescription;
    private boolean itemDisabled;
    private String itemLabel;
    private Object itemValue;
    private UIComponent value;

    public _SelectItem() {
    }


    public _SelectItem(Object itemValue) {
        this.itemValue = itemValue;
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object getItemValue() {
        return ValueBindings.get(this, "itemValue", itemValue, Object.class);
    }

    public void setItemValue(Object itemValue) {
        this.itemValue = itemValue;
    }

    public String getItemDescription() {
        return ValueBindings.get(this, "itemDescription", itemLabel);
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemLabel() {
        return ValueBindings.get(this, "itemLabel", itemLabel);
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public boolean isItemDisabled() {
        return ValueBindings.get(this, "itemDisabled", itemDisabled, false);
    }

    public void setItemDisabled(boolean itemDisabled) {
        this.itemDisabled = itemDisabled;
    }

    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                saveAttachedState(context, itemValue),
                itemLabel,
                itemDescription,
                itemDisabled
        };
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        itemValue = restoreAttachedState(context, values[i++]);
        itemLabel = (String) values[i++];
        itemDescription = (String) values[i++];
        itemDisabled = (Boolean) values[i++];
    }
}

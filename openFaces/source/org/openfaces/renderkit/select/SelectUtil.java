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

package org.openfaces.renderkit.select;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Natalia.Zolochevska@Teamdev.com
 */
public class SelectUtil {

     public static List<SelectItem> collectSelectItems(UIComponent selectComponent) {
        List<javax.faces.model.SelectItem> result = new ArrayList<SelectItem>();
        List<UIComponent> children = selectComponent.getChildren();
        if (children != null) {
            for (UIComponent uiComponent : children) {
                if (uiComponent instanceof UISelectItem) {
                    UISelectItem item = (UISelectItem) uiComponent;
                    Object itemValue = item.getValue();
                    javax.faces.model.SelectItem si;
                    if (itemValue != null) {
                        if (!(itemValue instanceof javax.faces.model.SelectItem)) {
                            String clientId = selectComponent.getClientId(FacesContext.getCurrentInstance());
                            throw new IllegalArgumentException(
                                    "The 'value' attribute <f:selectItem> tag should be null or an instance of SelectItem, " +
                                            "but the following type was encountered: " + itemValue.getClass().getName() +
                                            "; Select component client id: " + clientId);
                        }
                        si = (javax.faces.model.SelectItem) itemValue;
                    } else {
                        si = new javax.faces.model.SelectItem(item.getItemValue(), item.getItemLabel(), item.getItemDescription(),
                                item.isItemDisabled());
                    }
                    result.add(si);
                } else if (uiComponent instanceof UISelectItems) {
                    UISelectItems items = (UISelectItems) uiComponent;
                    Object value = items.getValue();
                    if (value != null) {
                        if (value instanceof Collection) {
                            Collection col = (Collection) value;
                            for (Object item : col) {
                                if (item instanceof javax.faces.model.SelectItem) {
                                    result.add((javax.faces.model.SelectItem) item);
                                } else {
                                    String clientId = selectComponent.getClientId(FacesContext.getCurrentInstance());
                                    throw new IllegalArgumentException(
                                            "The items specified inside the <f:selectItems> collection should be of type javax.faces.model.SelectItem, but the following type was encountered: " +
                                                    item.getClass().getName() + "; Select component client id: " + clientId);
                                }
                            }
                        } else if (value instanceof Object[]) {
                            Object[] arrayValue = (Object[]) value;
                            for (Object item : arrayValue) {
                                if (item instanceof javax.faces.model.SelectItem) {
                                    result.add((javax.faces.model.SelectItem) item);
                                } else {
                                    String clientId = selectComponent.getClientId(FacesContext.getCurrentInstance());
                                    throw new IllegalArgumentException(
                                            "The items specified inside the <f:selectItems> array should be of type javax.faces.model.SelectItem, but the following type was encountered: " +
                                                    item.getClass().getName() + "; Select component client id: " + clientId);
                                }
                            }
                        } else if (value instanceof Map) {
                            Map mapValue = (Map) value;
                            Set set = mapValue.entrySet();
                            for (Object aSet : set) {
                                Map.Entry entry = (Map.Entry) aSet;
                                result.add(new javax.faces.model.SelectItem(entry.getValue(), (String) entry.getKey()));
                            }
                        } else {
                            String clientId = selectComponent.getClientId(FacesContext.getCurrentInstance());
                            throw new IllegalArgumentException(
                                    "The 'value' attribute <f:selectItems> tag should be specified as a collection or an array " +
                                            "of SelectItem instances, or as a Map, but the following type was encountered: " +
                                            value.getClass().getName() + "; Select component client id: " + clientId);
                        }
                    }
                }
            }
        }
        return result.size() > 0 ? result : null;
    }


}

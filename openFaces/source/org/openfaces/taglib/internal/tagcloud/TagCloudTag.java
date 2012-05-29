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

package org.openfaces.taglib.internal.tagcloud;

import org.openfaces.component.tagcloud.Layout;
import org.openfaces.component.tagcloud.TagCloud;
import org.openfaces.component.tagcloud.TagsOrder;
import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.taglib.internal.OUICommandTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author : roman.nikolaienko
 */
public class TagCloudTag extends OUICommandTag {
    public String getComponentType() {
        return "org.openfaces.TagCloud";
    }

    public String getRendererType() {
        return "org.openfaces.TagCloudRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        TagCloud cloud = (TagCloud) component;
        setActionProperty(facesContext, cloud);
        setActionListener(facesContext, cloud);

        setStringProperty(component, "converter");

        setValueExpressionProperty(component, "items");

        setValueExpressionProperty(component, "itemKey");
        setValueExpressionProperty(component, "itemText");
        setValueExpressionProperty(component, "itemUrl");
        setValueExpressionProperty(component, "itemWeight");
        setValueExpressionProperty(component, "itemTitle");

        setBooleanProperty(component, "itemWeightVisible");
        
        setStringProperty(component, "itemWeightStyle");
        setStringProperty(component, "itemWeightClass");
        setStringProperty(component, "itemWeightFormat");
        
        setStringProperty(component, "itemStyle");
        setStringProperty(component, "itemClass");
        setStringProperty(component, "itemRolloverClass");
        setStringProperty(component, "itemRolloverStyle");

        setStringProperty(component, "itemTextClass");
        setStringProperty(component, "itemTextStyle");

        setEnumerationProperty(component, "order", TagsOrder.class);
        setEnumerationProperty(component, "layout", Layout.class);

        setStringProperty(component, "minItemStyle");
        setStringProperty(component, "maxItemStyle");
                
        setDoubleProperty(component,"rotationSpeed3D");
        setDoubleProperty(component,"shadowScale3D");
        setDoubleProperty(component,"stopRotationPeriod3D");

        setStringProperty(component, "var");
        setStringProperty(component, "onitemclick");
    }
}

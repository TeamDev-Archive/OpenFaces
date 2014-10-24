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

package org.openfaces.taglib.jsp.tagcloud;

import org.openfaces.taglib.internal.tagcloud.TagCloudTag;
import org.openfaces.taglib.jsp.OUICommandJspTag;

import javax.el.ValueExpression;

/**
 * @author : roman.nikolaienko
 */
public class TagCloudJspTag extends OUICommandJspTag {
    public TagCloudJspTag() {
        super(new TagCloudTag());
    }


    public void setConverter(ValueExpression converter) {
        getDelegate().setPropertyValue("converter", converter);
    }

    public void setItems(ValueExpression items) {
        getDelegate().setPropertyValue("items", items);
    }

    public void setItemKey(ValueExpression itemKey) {
        getDelegate().setPropertyValue("itemKey", itemKey);
    }

    public void setItemUrl(ValueExpression itemUrl) {
        getDelegate().setPropertyValue("itemUrl", itemUrl);
    }

    public void setItemText(ValueExpression itemText) {
        getDelegate().setPropertyValue("itemText", itemText);
    }

    public void setItemTitle(ValueExpression itemTitle) {
        getDelegate().setPropertyValue("itemTitle", itemTitle);
    }

    public void setItemWeight(ValueExpression itemWeight) {
        getDelegate().setPropertyValue("itemWeight", itemWeight);
    }

    public void setItemWeightVisible(ValueExpression itemWeightVisible) {
        getDelegate().setPropertyValue("itemWeightVisible", itemWeightVisible);
    }

    public void setItemWeightStyle(ValueExpression itemWeightStyle) {
        getDelegate().setPropertyValue("itemWeightStyle", itemWeightStyle);
    }

     public void setItemWeightClass(ValueExpression itemWeightClass) {
        getDelegate().setPropertyValue("itemWeightClass", itemWeightClass);
    }

    public void setItemWeightFormat(ValueExpression itemWeightFormat) {
        getDelegate().setPropertyValue("itemWeightFormat", itemWeightFormat);
    }

    public void setOrder(ValueExpression order) {
        getDelegate().setPropertyValue("order", order);
    }

    public void setMinItemStyle(ValueExpression minItemStyle) {
        getDelegate().setPropertyValue("minItemStyle", minItemStyle);
    }

    public void setMaxItemStyle(ValueExpression maxItemStyle) {
        getDelegate().setPropertyValue("maxItemStyle", maxItemStyle);
    }

    public void setLayout(ValueExpression layout) {
        getDelegate().setPropertyValue("layout", layout);
    }

    public void setItemClass(ValueExpression itemClass) {
        getDelegate().setPropertyValue("itemClass", itemClass);
    }

    public void setItemStyle(ValueExpression itemStyle) {
        getDelegate().setPropertyValue("itemStyle", itemStyle);
    }

    public void setItemRolloverClass(ValueExpression itemRolloverClass) {
        getDelegate().setPropertyValue("itemRolloverClass", itemRolloverClass);
    }

    public void setItemRolloverStyle(ValueExpression itemRolloverStyle) {
        getDelegate().setPropertyValue("itemRolloverStyle", itemRolloverStyle);
    }

    public void setItemTextClass(ValueExpression itemTextClass) {
        getDelegate().setPropertyValue("itemTextClass", itemTextClass);
    }

    public void setItemTextStyle(ValueExpression itemTextStyle) {
        getDelegate().setPropertyValue("itemTextStyle", itemTextStyle);
    }
       
    public void setShadowScale3D(ValueExpression shadowScale3D) {
        getDelegate().setPropertyValue("shadowScale3D", shadowScale3D);
    }

    public void setRotationSpeed3D(ValueExpression rotationSpeed3D) {
        getDelegate().setPropertyValue("rotationSpeed3D", rotationSpeed3D);
    }

    public void setStopRotationPeriod3D(ValueExpression stopRotationPeriod3D) {
        getDelegate().setPropertyValue("stopRotationPeriod3D", stopRotationPeriod3D);
    }

    public void setVar(ValueExpression var) {
        getDelegate().setPropertyValue("var", var);
    }

    public void setOnitemclick(ValueExpression onitemclick) {
        getDelegate().setPropertyValue("onitemclick", onitemclick);
    }

}

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

package org.openfaces.component.tagcloud;

import org.openfaces.component.OUICommand;
import org.openfaces.util.ValueBindings;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author : roman.nikolaienko
 */
public class TagCloud extends OUICommand {

    public static final String COMPONENT_TYPE = "org.openfaces.TagCloud";
    public static final String COMPONENT_FAMILY = "org.openfaces.TagCloud";

    public static final String DEFAULT_ITEM_ID_PREFIX = "_item_";

    private List<TagCloudItem> tagCloudItems;

    private Boolean itemWeightVisible;

    private String itemStyle;
    private String itemClass;
    private String itemRolloverClass;
    private String itemRolloverStyle;

    private String itemTextStyle;
    private String itemTextClass;
    
    private TagsOrder order;
    private Layout layout;

    private String minItemStyle;
    private String maxItemStyle;

    private String var;

    private Converter converter = null;

    private String itemWeightFormat;

    private String itemWeightStyle;
    private String itemWeightClass;

    private Double rotationSpeed3D;
    private Double shadowScale3D;
    private Double stopRotationPeriod3D;

    private Double minItemWeight;
    private Double maxItemWeight;
    private String onitemclick;

    public TagCloud() {
        setRendererType("org.openfaces.TagCloudRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),

                itemClass,
                itemStyle,
                itemRolloverClass,
                itemRolloverStyle,

                itemTextClass,
                itemTextStyle,

                itemWeightVisible,

                order,
                layout,

                minItemStyle,
                maxItemStyle,
                minItemWeight,
                maxItemWeight,
                var,

                itemWeightFormat,
                itemWeightStyle,
                itemWeightClass,

                rotationSpeed3D,
                shadowScale3D,
                stopRotationPeriod3D,
                saveAttachedState(context, converter),
                onitemclick
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);

        itemClass = (String) values[i++];
        itemStyle = (String) values[i++];
        itemRolloverClass = (String) values[i++];
        itemRolloverStyle = (String) values[i++];

        itemTextClass = (String) values[i++];
        itemTextStyle = (String) values[i++];

        itemWeightVisible = (Boolean) values[i++];

        order = (TagsOrder) values[i++];
        layout = (Layout) values[i++];

        minItemStyle = (String) values[i++];
        maxItemStyle = (String) values[i++];
        minItemWeight = (Double) values[i++];
        maxItemWeight = (Double) values[i++];

        var = (String) values[i++];

        itemWeightFormat = (String) values[i++];

        itemWeightStyle = (String) values[i++];
        itemWeightClass = (String) values[i++];

        rotationSpeed3D = (Double) values[i++];
        shadowScale3D = (Double) values[i++];
        stopRotationPeriod3D = (Double) values[i++];

        converter = (Converter) restoreAttachedState(context, values[i]);
        onitemclick = (String) values[i++];
    }

    public Converter getConverter() {
        if (this.converter != null) {
            return (this.converter);
        }
        ValueExpression ve = getValueExpression("converter");
        if (ve != null) {
            try {
                return ((Converter) ve.getValue(getFacesContext().getELContext()));
            }
            catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return (null);
        }
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public ValueExpression getItems() {
        return getValueExpression("items");
    }

    public void setItems(ValueExpression items) {
        setValueExpression("items", items);
    }

    public ValueExpression getItemKey() {
        return getValueExpression("itemKey");
    }

    public void setItemKey(ValueExpression itemKey) {
        setValueExpression("itemKey", itemKey);
    }

    public ValueExpression getItemText() {
        return getValueExpression("itemText");
    }

    public void setItemText(ValueExpression itemText) {
        setValueExpression("itemText", itemText);
    }

    public ValueExpression getItemTitle() {
        return getValueExpression("itemTitle");
    }

    public void setItemTitle(ValueExpression itemTitle) {
        setValueExpression("itemTitle", itemTitle);
    }

    public ValueExpression getItemUrl() {
        return getValueExpression("itemUrl");
    }

    public void setItemUrl(ValueExpression itemUrl) {
        setValueExpression("itemUrl", itemUrl);
    }

    public ValueExpression getItemWeight() {
        return getValueExpression("itemWeight");
    }

    public void setItemWeight(ValueExpression itemWeight) {
        setValueExpression("itemWeight", itemWeight);
    }

    public TagsOrder getOrder() {
        return ValueBindings.get(this, "order", order, TagsOrder.ALPHABETICALLY, TagsOrder.class);
    }

    public void setOrder(TagsOrder order) {
        this.order = order;
    }

    public Layout getLayout() {
        return ValueBindings.get(this, "layout", layout, Layout.RECTANGLE, Layout.class);
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public double getRotationSpeed3D() {
        return ValueBindings.get(this, "rotationSpeed3D", rotationSpeed3D, 80);
    }

    public void setRotationSpeed3D(double rotationSpeed3D) {
        this.rotationSpeed3D = rotationSpeed3D;
    }

    public double getShadowScale3D() {
        return ValueBindings.get(this, "shadowScale3D", shadowScale3D, 0.7);
    }

    public void setShadowScale3D(double shadowScale3D) {
        this.shadowScale3D = shadowScale3D;
    }

    public Double getStopRotationPeriod3D() {
        return ValueBindings.get(this, "stopRotationPeriod3D", stopRotationPeriod3D, 4);
    }

    public void setStopRotationPeriod3D(Double stopRotationPeriod3D) {
        this.stopRotationPeriod3D = stopRotationPeriod3D;
    }

    public boolean isItemWeightVisible() {
        return ValueBindings.get(this, "itemWeightVisible", itemWeightVisible, false);
    }

    public void setItemWeightVisible(boolean itemWeightVisible) {
        this.itemWeightVisible = itemWeightVisible;
    }

    public String getMinItemStyle() {
        return ValueBindings.get(this, "minItemStyle", minItemStyle);
    }

    public void setMinItemStyle(String minItemStyle) {
        this.minItemStyle = minItemStyle;
    }

    public String getMaxItemStyle() {
        return ValueBindings.get(this, "maxItemStyle", maxItemStyle);
    }

    public void setMaxItemStyle(String maxItemStyle) {
        this.maxItemStyle = maxItemStyle;
    }

    public String getItemStyle() {
        return ValueBindings.get(this, "itemStyle", itemStyle);
    }

    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    public String getItemClass() {
        return ValueBindings.get(this, "itemClass", itemClass);
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getItemRolloverClass() {
        return ValueBindings.get(this, "itemRolloverClass", itemRolloverClass);
    }

    public void setItemRolloverClass(String itemRolloverClass) {
        this.itemRolloverClass = itemRolloverClass;
    }

    public String getItemRolloverStyle() {
        return ValueBindings.get(this, "itemRolloverStyle", itemRolloverStyle);
    }

    public void setItemRolloverStyle(String itemRolloverStyle) {
        this.itemRolloverStyle = itemRolloverStyle;
    }

    public String getItemTextClass() {
        return ValueBindings.get(this, "itemTextClass", itemTextClass);
    }

    public void setItemTextClass(String itemTextClass) {
        this.itemTextClass = itemTextClass;
    }

    public String getItemTextStyle() {
        return ValueBindings.get(this, "itemTextStyle", itemTextStyle);
    }

    public void setItemTextStyle(String itemTextStyle) {
        this.itemTextStyle = itemTextStyle;
    }


    public String getVar() {
        return ValueBindings.get(this, "var", var, "item");
    }

    public void setVar(String var) {
        this.var = var;
    }

    public double getMinItemWeight() {
        return this.minItemWeight;
    }

    public void setMinItemWeight(double minItemWeight) {
        this.minItemWeight = minItemWeight;
    }

    public double getMaxItemWeight() {
        return this.maxItemWeight;
    }

    public void setMaxItemWeight(double maxItemWeight) {
        this.maxItemWeight = maxItemWeight;
    }

    public String getItemWeightFormat() {
        return ValueBindings.get(this, "itemWeightFormat", itemWeightFormat, "(#,##0.##)");
    }

    public void setItemWeightFormat(String itemWeightFormat) {
        this.itemWeightFormat = itemWeightFormat;
    }

    public String getItemWeightStyle() {
        return ValueBindings.get(this, "itemWeightStyle", itemWeightStyle);
    }

    public void setItemWeightStyle(String itemWeightStyle) {
        this.itemWeightStyle = itemWeightStyle;
    }
     public String getItemWeightClass() {
        return ValueBindings.get(this, "itemWeightClass", itemWeightClass);
    }

    public void setItemWeightClass(String itemWeightClass) {
        this.itemWeightClass = itemWeightClass;
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     *
     * Returns the current list of items that is converted to the list of TagCloud instances. These instances are
     * added as children of the TagCloud component and invokers of this method must ensure that these components are
     * removed from the list of child components after they are not needed.
     */
    public List<TagCloudItem> getTagCloudItems(FacesContext context) {
        if (tagCloudItems == null)
            tagCloudItems = createTagCloudItems(context);
        return tagCloudItems;
    }

    public void selectItemObject(FacesContext context,String hashCode) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Collection itemsData = getItemsCollection(context);
        String var = getVar();
        Object prevValue = requestMap.get(var);
        ValueExpression keyValueExpression = getItemKey();
        int itemHashCode;
        for (Object item : itemsData) {
            requestMap.put(var, item);
            itemHashCode = keyValueExpression != null ?
                    getVarParameter(keyValueExpression).hashCode() : item.hashCode();

            if (itemHashCode == Integer.parseInt(hashCode)) {
                return;
            }
            requestMap.put(getVar(), prevValue);
        }
    }

    private Object getVarParameter(ValueExpression parameterExpression) {
        return parameterExpression.getValue(getFacesContext().getELContext());
    }

    private void setVarParameter(ValueExpression parameterExpression, Object value) {
        if (parameterExpression == null)
            return;
        parameterExpression.setValue(getFacesContext().getELContext(), value);
    }

    private List<TagCloudItem> createTagCloudItems(FacesContext context) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Collection itemsData = getItemsCollection(context);
        List<TagCloudItem> items = new ArrayList<TagCloudItem>(itemsData.size());

        String var = getVar();
        Object prevVarValue = requestMap.get(var);
        ValueExpression keyValueExpression = getItemKey();
        ValueExpression textValueExpression = getItemText();
        ValueExpression titleExpression = getItemTitle();
        ValueExpression urlExpression = getItemUrl();
        ValueExpression weightExpression = getItemWeight();
        ValueExpression onItemClickExpression = getOnitemclick();

        double curWeight;
        String cloudId = getId();
        int itemId;
        setMaxItemWeight(-1);
        setMinItemWeight(-1);

        for (Object itemData : itemsData) {
            requestMap.put(var, itemData);
            TagCloudItem item = new TagCloudItem();
            itemId = keyValueExpression != null ?
                    getVarParameter(keyValueExpression).hashCode() : itemData.hashCode();

            item.setId(cloudId + DEFAULT_ITEM_ID_PREFIX + itemId);

            item.setTitle(titleExpression != null ?
                    (String) getVarParameter(titleExpression) : "");

            item.setValue(textValueExpression != null ?
                    getVarParameter(textValueExpression) :
                    String.valueOf(itemData));

            item.setUrl(urlExpression != null ?
                    (String) getVarParameter(urlExpression) : "#");

            item.setOnItemClick(onItemClickExpression != null ?
                    (String) getVarParameter(onItemClickExpression) : "");
            
            curWeight = weightExpression != null ?
                    (Double) getVarParameter(weightExpression) : 0;

            item.setRender(getRender());
            item.setExecute(getExecute());
            item.setWeight(curWeight);

            if (getMaxItemWeight() < 0 || curWeight > getMaxItemWeight())
                setMaxItemWeight(curWeight);

            if (getMinItemWeight() < 0 || curWeight < getMinItemWeight())
                setMinItemWeight(curWeight);

            item.setStyle(getItemStyle());
            item.setStyleClass(getItemClass());

            item.setRolloverClass(getItemRolloverClass());
            item.setRolloverStyle(getItemRolloverStyle());

            item.setTextClass(getItemTextClass());
            item.setTextStyle(getItemTextStyle());

            item.setConverter(getConverter());

            this.getChildren().add(item);
            items.add(item);

            requestMap.put(var, prevVarValue);
        }

        TagCloudGradientStyleGenerator gradientStyleGenerator =
                new TagCloudGradientStyleGenerator(getMaxItemStyle(), getMinItemStyle(),
                        getMaxItemWeight(), getMinItemWeight());

        for (TagCloudItem item : items)
            item.setGradientStyle(gradientStyleGenerator.generateStyle(item));

        return items;
    }

    private Collection getItemsCollection(FacesContext context) {
        ValueExpression itemsExpression = getItems();
        if (itemsExpression == null) {
            return Collections.EMPTY_LIST;
        }
        Object items = itemsExpression.getValue(context.getELContext());
        Collection data;
        if (items == null) {
            data = Collections.EMPTY_LIST;
        } else {
            if (items instanceof Collection) {
                data = (Collection) items;
            } else if (items.getClass().isArray()) {
                data = Arrays.asList(items);
            } else
                throw new IllegalArgumentException("Unsupported object type received from o:tagCloud items attribute: "
                        + items.getClass().getName() + "; should be either array or collection");
        }
        return data;
    }

    public ValueExpression getOnitemclick() {
        return getValueExpression("onitemclick");
    }

    public void setOnitemclick(ValueExpression onitemclick) {
        setValueExpression("onitemclick", onitemclick);
    }
}

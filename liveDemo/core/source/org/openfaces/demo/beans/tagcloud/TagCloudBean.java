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

package org.openfaces.demo.beans.tagcloud;


import org.openfaces.component.tagcloud.Layout;
import org.openfaces.component.tagcloud.TagCloud;
import org.openfaces.component.tagcloud.TagsOrder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : roman.nikolaienko
 */
public class TagCloudBean implements ActionListener, Serializable {

    private Item var = new Item();

    private List<Item> entitiesItemsList;

    private String currentEntities = "";
    private String currentCategory = "All";

    private TaggedEntityStore entityStore;

    private String skin = "default";
    private String itemWeightFormat = "(#0)";
    private String itemWeightStyle = "";
    private String maxItemStyle = "";
    private String minItemStyle = "";
    private boolean itemWeightVisible;
    private Layout layout = Layout.RECTANGLE;
    private TagsOrder order = TagsOrder.ALPHABETICALLY;

    private Converter layoutConverter = new Converter() {
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return Layout.valueOf(value.toUpperCase());
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            return value.toString();
        }
    };

    private List<SelectItem> layoutList;

    private Converter orderConverter = new Converter() {
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            if ("weightRevers".equals(value)) {
                return TagsOrder.WEIGHT_REVERS;
            }
            return TagsOrder.valueOf(value.toUpperCase());
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            return value.toString();
        }
    };
    private List<SelectItem> orderList;

    public TagCloudBean() {
        initEntities();
    }

    private void initEntities() {
        entityStore = new TaggedEntityStore();
        entitiesItemsList = new ArrayList<Item>();

        orderList = new ArrayList<SelectItem>();
        for (TagsOrder order : TagsOrder.values()) {
            orderList.add(new SelectItem(order));
        }
        layoutList = new ArrayList<SelectItem>();
        for (Layout layout : Layout.values()) {
            layoutList.add(new SelectItem(layout));
        }
        Set<String> tags = entityStore.getAllTags();
        int counter;
        this.entitiesItemsList.clear();
        for (String tag : tags) {
            counter = 0;
            for (TaggedEntity current : this.entityStore.getEntities()) {
                if (current.hasTag(tag)) {
                    counter++;
                }
            }
            if (counter != 0) {
                this.entitiesItemsList.add(new Item(tag, "", "#", counter));
            }
        }
    }

    public List<Item> getEntitiesItemsList() {
        Set<String> tags = entityStore.getAllTags();
        int counter;
        boolean flag = currentCategory.equals("All");
        this.entitiesItemsList.clear();

        for (String tag : tags) {
            counter = 0;
            for (TaggedEntity current : this.entityStore.getEntities()) {
                if ((flag ? true : current.getType().toString().equals(currentCategory)) &&
                        current.hasTag(tag)) {
                    counter++;
                }
            }
            if (counter != 0) {
                this.entitiesItemsList.add(new Item(tag, "", "#", counter));
            }
        }
        return entitiesItemsList;
    }

    public void setEntitiesItemsList(List<Item> entitiesItemsList) {
        this.entitiesItemsList = entitiesItemsList;
    }

    public String getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public boolean isItemWeightVisible() {
        return itemWeightVisible;
    }

    public void setItemWeightVisible(boolean itemWeightVisible) {
        this.itemWeightVisible = itemWeightVisible;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        orderList.clear();
        if (layout.equals(Layout.SPHERE)) {
            orderList.add(new SelectItem(TagsOrder.ORIGINAL));
            this.order = TagsOrder.ORIGINAL;
        } else {
            for (TagsOrder order : TagsOrder.values()) {
                orderList.add(new SelectItem(order));
            }
        }
        this.layout = layout;
    }

    public TagsOrder getOrder() {
        return order;
    }

    public void setOrder(TagsOrder order) {
        this.order = order;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
        if (skin.equals("default")) {
            this.itemWeightFormat = "(#0)";
            this.itemWeightStyle = "";
            this.maxItemStyle = "";
            this.minItemStyle = "";
        }
        if (skin.equals("theme1")) {
            this.itemWeightFormat = "<#0>";
            this.itemWeightStyle = "margin-left:3px; font-size: 0.8em; vertical-align:super; color: #1E9660";
            this.maxItemStyle = "color: #3097FF; font-size: 1.5em;";
            this.minItemStyle = "color: #91C8FF; font-size: 1em;";
        }
        if (skin.equals("theme2")) {
            this.itemWeightFormat = "->#0";
            this.itemWeightStyle = "margin-left:3px;font-size: 0.8em; vertical-align:baseline; color: #1E9660";
            this.maxItemStyle = "color: #681313; font-size: 15pt;";
            this.minItemStyle = "color: #D3670E; font-size: 10pt;";
        }
    }

    public String getItemWeightFormat() {
        return this.itemWeightFormat;
    }

    public void setItemWeightFormat(String itemWeightFormat) {
        this.itemWeightFormat = itemWeightFormat;
    }

    public String getItemWeightStyle() {
        return this.itemWeightStyle;
    }

    public void setItemWeightStyle(String itemWeightStyle) {
        this.itemWeightStyle = itemWeightStyle;
    }

    public String getMaxItemStyle() {
        return this.maxItemStyle;
    }

    public void setMaxItemStyle(String maxItemStyle) {
        this.maxItemStyle = maxItemStyle;
    }

    public String getMinItemStyle() {
        return this.minItemStyle;
    }

    public void setMinItemStyle(String minItemStyle) {
        this.minItemStyle = minItemStyle;
    }

    public void processAction(ActionEvent event) {

        TagCloud cloud = (TagCloud) event.getComponent();
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        var = (Item) requestMap.get(cloud.getVar());
    }

    public Item getVar() {
        return var;
    }

    public void setCurrentEntities(String currentEntities) {
        this.currentEntities = currentEntities;
    }

    public String getCurrentEntities() {
        int counter;
        boolean flag = currentCategory.equals("All");
        StringBuilder currentEntitiesBuilder = new StringBuilder("");
        counter = 1;
        for (TaggedEntity current : entityStore.getEntities()) {
            if ((flag ? true : current.getType().toString().equals(currentCategory))) {
                if ((var != null && current.hasTag(var.getText()))) {
                    //currentEntitiesBuilder.append("<b><i>").append(counter++).append(".</i></b> ");
                    currentEntitiesBuilder.append(current.getInfo());
                    currentEntitiesBuilder.append("<hr > ");
                }
            }
        }
        if (currentEntitiesBuilder.toString().equals("")) {
            currentEntitiesBuilder.append("There is no information for this category and tag. Make another selections.");
        }
        this.currentEntities = currentEntitiesBuilder.toString();

        return this.currentEntities;
    }

    public void setOrderList(List<SelectItem> orderList) {
        this.orderList = orderList;
    }

    public List<SelectItem> getOrderList() {
        return orderList;
    }

    public Converter getOrderConverter() {
        return orderConverter;
    }

    public void setLayoutList(List<SelectItem> layoutList) {
        this.layoutList = layoutList;
    }

    public List<SelectItem> getLayoutList() {
        return layoutList;
    }

    public Converter getLayoutConverter() {
        return layoutConverter;
    }
}

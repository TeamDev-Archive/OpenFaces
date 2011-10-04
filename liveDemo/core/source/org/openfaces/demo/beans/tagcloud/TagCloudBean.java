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

package org.openfaces.demo.beans.tagcloud;


import org.openfaces.component.tagcloud.Layout;
import org.openfaces.component.tagcloud.TagCloud;
import org.openfaces.component.tagcloud.TagsOrder;
import org.openfaces.demo.beans.dropdown.Color;
import org.openfaces.demo.beans.dropdown.Colors;

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
    private List<Item> coloredItemsList;

    private TaggedEntityStore entityStore;
    private Colors colors = new Colors();
    private String currentEntities = "";
    private String currentCategory = "All";

    private String skin = "rectangle";
    private String itemWeightFormat = "(#0)";
    private String itemWeightStyle = "";
    private Color minColor = colors.getColorByText("Gray");
    private Color maxColor = colors.getColorByText("Black");
    private int minFontSize = 15;
    private int maxFontSize = 30;
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
    private List<String> weightFormatList;

    public TagCloudBean() {
        initEntities();
        setMinFontSize(15);
        setMaxFontSize(50);
        setMaxColor(colors.getColorByText("CornflowerBlue"));
        setMinColor(colors.getColorByText("YellowGreen"));
    }

    private void initEntities() {
        entityStore = new TaggedEntityStore();
        entitiesItemsList = new ArrayList<Item>();
        coloredItemsList = new ArrayList<Item>();
        coloredItemsList.add(new Item("Java", "", "#", 152, colors.getColorByText("SteelBlue")));
        coloredItemsList.add(new Item("JSF", "", "#", 105, colors.getColorByText("SteelBlue")));
        coloredItemsList.add(new Item("AJAX", "", "#", 32, colors.getColorByText("SteelBlue")));
        coloredItemsList.add(new Item("HTML", "", "#", 104, colors.getColorByText("SteelBlue")));
        coloredItemsList.add(new Item("JavaScript", "", "#", 43, colors.getColorByText("SteelBlue")));
        coloredItemsList.add(new Item("J2EE", "", "#", 85, colors.getColorByText("SteelBlue")));

        coloredItemsList.add(new Item("FireFox", "", "#", 120, colors.getColorByText("Tomato")));
        coloredItemsList.add(new Item("IE", "", "#", 148, colors.getColorByText("Tomato")));
        coloredItemsList.add(new Item("Opera", "", "#", 50, colors.getColorByText("Tomato")));
        coloredItemsList.add(new Item("Chrome", "", "#", 47, colors.getColorByText("Tomato")));
        coloredItemsList.add(new Item("Safari", "", "#", 21, colors.getColorByText("Tomato")));

        coloredItemsList.add(new Item("DeskTop", "", "#", 154, colors.getColorByText("MediumSeaGreen")));
        coloredItemsList.add(new Item("LapTop", "", "#", 132, colors.getColorByText("MediumSeaGreen")));
        coloredItemsList.add(new Item("NetBook", "", "#", 83, colors.getColorByText("MediumSeaGreen")));
        coloredItemsList.add(new Item("iPod", "", "#", 54, colors.getColorByText("MediumSeaGreen")));
        coloredItemsList.add(new Item("Nexen", "", "#", 25, colors.getColorByText("MediumSeaGreen")));


        orderList = new ArrayList<SelectItem>();
        for (TagsOrder order : TagsOrder.values()) {
            orderList.add(new SelectItem(order));
        }
        weightFormatList = new ArrayList<String>();
        weightFormatList.add("(#0)");
        weightFormatList.add("[#0]");
        weightFormatList.add("#0 items");
        weightFormatList.add("-#0-");
        weightFormatList.add("#0");

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

    public List<Item> getColoredItemsList() {
        return coloredItemsList;
    }

    public void setColoredItemsList(List<Item> coloredItemsList) {
        this.coloredItemsList = coloredItemsList;
    }

    public List<Item> getEntitiesItemsList() {
        Set<String> tags = entityStore.getAllTags();
        int counter;
        boolean flag = currentCategory.equals("All");
        this.entitiesItemsList.clear();

        for (String tag : tags) {
            counter = 0;
            for (TaggedEntity current : this.entityStore.getEntities()) {
                if ((flag || current.getType().toString().equals(currentCategory)) &&
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
        }
        if (skin.equals("theme1")) {
            this.itemWeightFormat = "<#0>";
            this.itemWeightStyle = "margin-left:3px; font-size: 0.8em; vertical-align:top; color: #1E9660";
            setMinFontSize(20);
            setMaxFontSize(50);
        }
        if (skin.equals("theme2")) {
            this.itemWeightFormat = "[#0]";
            this.itemWeightStyle = "margin-left:3px;font-size: 0.6em; vertical-align:top; color: #1E6096";
            setMinFontSize(12);
            setMaxFontSize(24);
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
        StringBuilder rez = new StringBuilder("");
        if (maxColor != null) {
            rez.append("color:").append(maxColor.getHex()).append(";");
            rez.append("font-size:").append(getMaxFontSize()).append("px;");
        }
        return rez.toString();
    }
//
//    public void setMaxItemStyle(String maxItemStyle) {
//        this.maxItemStyle = maxItemStyle;
//    }

    public String getMinItemStyle() {
        StringBuilder rez = new StringBuilder("");
        if (minColor != null) {
            rez.append("color:").append(minColor.getHex()).append(";");
            rez.append("font-size:").append(getMinFontSize()).append("px;");
        }
        return rez.toString();
    }

    public void setMinItemStyle(String minItemStyle) {
        this.minItemStyle = minItemStyle;
    }

    public Color getMinColor() {
        return minColor;
    }

    public void setMinColor(Color minColor) {
        this.minColor = minColor;
    }

    public Color getMaxColor() {
        return maxColor;
    }

    public void setMaxColor(Color maxColor) {
        this.maxColor = maxColor;
    }

    public int getMinFontSize() {
        return minFontSize;
    }

    public void setMinFontSize(int minFontSize) {
        this.minFontSize = minFontSize;
    }

    public int getMaxFontSize() {
        return maxFontSize;
    }

    public void setMaxFontSize(int maxFontSize) {
        this.maxFontSize = maxFontSize;
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
        boolean flag = currentCategory.equals("All");
        StringBuilder currentEntitiesBuilder = new StringBuilder("");
        for (TaggedEntity current : entityStore.getEntities()) {
            if ((flag || current.getType().toString().equals(currentCategory))) {
                if ((var != null && current.hasTag(var.getText()))) {
                    currentEntitiesBuilder.append("<span>Category: ").append(current.getType()).append("</span>");
                    currentEntitiesBuilder.append(current.getInfo());
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

    public List<String> getWeightFormatList() {
        return weightFormatList;
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

    public void setRectangleTheme(ActionEvent event) {
        setSkin("rectangle");
        setLayout(Layout.RECTANGLE);
        setOrder(TagsOrder.ALPHABETICALLY);
        setVertical(false);
        setOval(false);
        setMinFontSize(15);
        setMaxFontSize(50);
        setItemWeightFormat("(#0)");
        setMaxColor(colors.getColorByText("SteelBlue"));
        setMinColor(colors.getColorByText("Tan"));
        setItemWeightVisible(false);
//        setMaxColor(colors.getColorByText("AntiqueWhite") );
//        setMinColor(colors.getColorByText("PaleTurquoise") );
    }

    public void setVerticalTheme(ActionEvent event) {
        setSkin("vertical");
        setLayout(Layout.VERTICAL);
        setOrder(TagsOrder.WEIGHT_REVERS);
        setVertical(true);
        setOval(false);
        setMinFontSize(12);
        setMaxFontSize(30);
        setItemWeightVisible(true);
        setMaxColor(colors.getColorByText("Peru"));
        setMinColor(colors.getColorByText("MidnightBlue"));
        setItemWeightFormat("-#0-");
    }

    public void setOvalTheme(ActionEvent event) {
        setSkin("oval");
        setLayout(Layout.OVAL);
        setOrder(TagsOrder.WEIGHT);
        setMinFontSize(15);
        setMaxFontSize(25);
        setMaxColor(colors.getColorByText("DarkTurquoise"));
        setMinColor(colors.getColorByText("Tomato"));
        setVertical(false);
        setOval(true);
        setItemWeightVisible(false);
        setItemWeightFormat("<#0>");
    }

    public void setSphereTheme(ActionEvent event) {
        setSkin("sphere");
        setLayout(Layout.SPHERE);
        setMinFontSize(15);
        setMaxFontSize(25);
        setMaxColor(colors.getColorByText("DodgerBlue"));
        setMinColor(colors.getColorByText("SteelBlue"));
        setVertical(false);
        setOval(false);
        setItemWeightVisible(false);
        setItemWeightFormat("[#0]");
    }

    private boolean vertical = false;
    private boolean oval = false;

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public boolean isOval() {
        return oval;
    }

    public void setOval(boolean oval) {
        this.oval = oval;
    }
}

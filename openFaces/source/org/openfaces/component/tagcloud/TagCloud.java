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

package org.openfaces.component.tagcloud;

import org.openfaces.component.OUICommand;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.util.Environment;
import org.openfaces.util.ValueBindings;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.awt.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author : roman.nikolaienko
 */
public class TagCloud extends OUICommand {

    public static final String COMPONENT_TYPE = "org.openfaces.TagCloud";
    public static final String COMPONENT_FAMILY = "org.openfaces.TagCloud";

    private static final double DEFAULT_MAX_FONT_SIZE = 25;
    private static final double DEFAULT_MIN_FONT_SIZE = 10;

    private static final double DEFAULT_MAX_OPACITY = 1;
    private static final double DEFAULT_MIN_OPACITY = 0.8;

    private static final int DEFAULT_MAX_RED = 0;
    private static final int DEFAULT_MAX_GREEN = 0;
    private static final int DEFAULT_MAX_BLUE = 0;

    private static final int DEFAULT_MIN_RED = 140;
    private static final int DEFAULT_MIN_GREEN = 140;
    private static final int DEFAULT_MIN_BLUE = 140;

    private static final String DEFAULT_ITEM_ID_PREFIX = "item";

    private Object prevVarValue = null;
    private List<TagCloudItem> itemsList;

    private boolean itemWeightVisible;

    private String itemStyle;
    private String itemClass;
    private String itemRolloverClass;
    private String itemRolloverStyle;

    private TagsOrder order;
    private Layout layout;

    private String minItemStyle;
    private String maxItemStyle;

    private String var;

    private Converter converter = null;

    private double minItemWeight;
    private double maxItemWeight;

    private String minFontSize;
    private String maxFontSize;

    private double minOpacity;
    private double maxOpacity;

    private Color minColor;
    private Color maxColor;

    private String itemWeightFormat;
    private String itemWeightStyle;

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
                
                prevVarValue,
                itemClass,
                itemStyle,
                itemRolloverClass,
                itemRolloverStyle,

                itemWeightVisible,

                order,
                layout,

                minItemStyle,
                maxItemStyle,

                var,
                minItemWeight,
                maxItemWeight,

                minFontSize,
                maxFontSize,

                minOpacity,
                maxOpacity,

                minColor,
                maxColor,
                itemWeightFormat,
                itemWeightStyle,
                saveAttachedState(context, converter)
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);

        prevVarValue = values[i++];
        itemClass = (String) values[i++];
        itemStyle = (String) values[i++];
        itemRolloverClass = (String) values[i++];
        itemRolloverStyle = (String) values[i++];

        itemWeightVisible = (Boolean) values[i++];

        order = (TagsOrder) values[i++];
        layout = (Layout) values[i++];

        minItemStyle = (String) values[i++];
        maxItemStyle = (String) values[i++];

        var = (String) values[i++];
        minItemWeight = (Double) values[i++];
        maxItemWeight = (Double) values[i++];

        minFontSize = (String) values[i++];
        maxFontSize = (String) values[i++];

        minOpacity = (Double) values[i++];
        maxOpacity = (Double) values[i++];

        minColor = (Color) values[i++];
        maxColor = (Color) values[i++];

        itemWeightFormat =  (String) values[i++];
        itemWeightStyle= (String) values[i++];

        converter = (Converter) restoreAttachedState(context, values[i]);

    }

    public ValueExpression getItems() {
        return getValueExpression("items");
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

    public void setItems(ValueExpression items) {
        setValueExpression("items", items);
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

    public String getVar() {
        return ValueBindings.get(this, "var", var, "item");
    }

    public void setVar(String var) {
        this.var = var;
    }

    public Object getPrevVarValue() {
        return prevVarValue;
    }

    public void setPrevVarValue(Object prevVarValue) {
        this.prevVarValue = prevVarValue;
    }

    public double getMinItemWeight() {
        return ValueBindings.get(this, "minItemWeight", minItemWeight, -1);
    }

    public void setMinItemWeight(double minItemWeight) {
        this.minItemWeight = minItemWeight;
    }

    public double getMaxItemWeight() {
        return ValueBindings.get(this, "maxItemWeight", maxItemWeight, -1);
    }

    public void setMaxItemWeight(double maxItemWeight) {
        this.maxItemWeight = maxItemWeight;
    }

    public String getMinFontSize() {
        return ValueBindings.get(this, "minFontSize", minFontSize, "");
    }

    public void setMinFontSize(String minFontSize) {
        this.minFontSize = minFontSize;
    }

    public String getMaxFontSize() {
        return ValueBindings.get(this, "maxFontSize", maxFontSize, "");
    }

    public void setMaxFontSize(String maxFontSize) {
        this.maxFontSize = maxFontSize;
    }

    public double getMinOpacity() {
        return ValueBindings.get(this, "minOpacity", minOpacity, 0);
    }

    public void setMinOpacity(double minOpacity) {
        this.minOpacity = minOpacity;
    }

    public double getMaxOpacity() {
        return ValueBindings.get(this, "maxOpacity", maxOpacity, 0);
    }

    public void setMaxOpacity(double maxOpacity) {
        this.maxOpacity = maxOpacity;
    }

    public Color getMinColor() {
        return ValueBindings.get(this, "minColor", minColor, null, Color.class);
    }

    public void setMinColor(Color minColor) {
        this.minColor = minColor;
    }

    public Color getMaxColor() {
        return ValueBindings.get(this, "maxColor", maxColor, null, Color.class);
    }

    public void setMaxColor(Color maxColor) {
        this.maxColor = maxColor;
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

    private Object getVarParameter(ValueExpression parameterExpression) {
        return parameterExpression.getValue(getFacesContext().getELContext());
    }

    private void setVarParameter(ValueExpression parameterExpression, Object value) {
        if (parameterExpression == null)
            return;
        parameterExpression.setValue(getFacesContext().getELContext(), value);
    }

    private String getGradientStyle(TagCloudItem item) {
        StringBuilder styleBuilder = new StringBuilder();
        Color color = getColor(item);
        double opacity = getOpacity(item);
        String opacityToRender = new java.text.DecimalFormat("0.00").format(opacity);
        int ieOpacity = (int) Math.round(opacity * 100);
        String fonSize = getFontSize(item);
        String colorToRender = CSSUtil.formatColor(color);

        styleBuilder.append("color: ").append(colorToRender).append("; ");

        styleBuilder.append("font-size: ").append(fonSize).append("; ");

        if (opacity < 1) {
            styleBuilder.append("opacity: ").append(opacityToRender).append("; ");
            if (Environment.isExplorer()) {
                styleBuilder.append("filter: alpha(opacity=");
                styleBuilder.append(ieOpacity).append("); ");
            }
        }

        return styleBuilder.toString();
    }

    public List<TagCloudItem> itemsToTheList(FacesContext context) {
        if (itemsList == null)
            itemsList = createItemsList(context);
        return itemsList;
    }

    private List<TagCloudItem> createItemsList(FacesContext context) {
        Collection itemsData = getItemsData(context);
        List<TagCloudItem> items = new ArrayList<TagCloudItem>(itemsData.size());

        String var = getVar();

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        prevVarValue = requestMap.get(var);

        if (!itemsData.isEmpty() && prevVarValue == null)
            try {
                prevVarValue = itemsData.iterator().next().getClass().newInstance();
            } catch (InstantiationException e) {
                throw new FacesException(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                throw new FacesException(e.getMessage(), e);
            }

        ValueExpression textValueExpression = getItemText();
        ValueExpression titleExpression = getItemTitle();
        ValueExpression urlExpression = getItemUrl();
        ValueExpression weightExpression = getItemWeight();

        setItemGradientMaxMinStyles(getMaxItemStyle(), getMinItemStyle());
        double curWeight = 0;
        String cloudId = getClientId(context);
        int itemId = 1;
        setMaxItemWeight(-1);
        setMinItemWeight(-1);

        for (Object itemData : itemsData) {
            requestMap.put(var, itemData);

            TagCloudItem item = new TagCloudItem();

            item.setId(DEFAULT_ITEM_ID_PREFIX + (itemId++));
            item.setCloudId(cloudId);

            item.setTitle(titleExpression != null ?
                    (String) getVarParameter(titleExpression) : "");

            item.setValue(textValueExpression != null ?
                    getVarParameter(textValueExpression) :
                    String.valueOf(itemData));

            item.setUrl(urlExpression != null ?
                    (String) getVarParameter(urlExpression) : "#");

            curWeight = weightExpression != null ?
                    (Double) getVarParameter(weightExpression) : 0;

            item.setWeight(curWeight);

            if (getMaxItemWeight() < 0 || curWeight > getMaxItemWeight())
                setMaxItemWeight(curWeight);

            if (getMinItemWeight() < 0 || curWeight < getMinItemWeight())
                setMinItemWeight(curWeight);

            item.setStyle(getItemStyle());
            item.setStyleClass(getItemClass());

            item.setConverter(getConverter());

            item.setRolloverClass(getItemRolloverClass());
            item.setRolloverStyle(getItemRolloverStyle());

            item.setWeightVisible(isItemWeightVisible());
            item.setParent(this);
            items.add(item);

            requestMap.put(var, prevVarValue);
        }

        for (TagCloudItem item : items)
            item.setGradientStyle(getGradientStyle(item));

        return items;
    }

    private Collection getItemsData(FacesContext context) {

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
                data = new ArrayList();
                for (int i = 0, count = Array.getLength(items); i < count; i++) {
                    Object element = Array.get(items, i);
                    data.add(element);
                }
            } else
                throw new IllegalArgumentException("Unsupported object type received from o:tagCloud items attribute: "
                        + items.getClass().getName() + "; should be either array or collection");
        }
        return data;
    }

    private void setItemGradientMaxMinStyles(String maxStyle, String minStyle) {
        setMaxMinItemStyle(maxStyle, true);
        setMaxMinItemStyle(minStyle, false);
    }

    private void setMaxMinItemStyle(String style, boolean isMax) {
        if (style == null)
            return;
        StringTokenizer worker = new StringTokenizer(style.replace(" ", "").trim(), ";");
        StringTokenizer workerSmall;
        String stylePie;
        String attribute = "";
        String value = "";
        while (worker.hasMoreElements()) {
            stylePie = worker.nextToken().trim();
            workerSmall = new StringTokenizer(stylePie, ":");
            if (workerSmall.hasMoreElements() && workerSmall.countTokens() == 2) {
                attribute = workerSmall.nextToken().trim();
                value = workerSmall.nextToken().trim();
                if (attribute.equalsIgnoreCase("color")) {
                    if (isMax)
                        setMaxColor(CSSUtil.parseColor(value));
                    else
                        setMinColor(CSSUtil.parseColor(value));
                }
                if (attribute.equalsIgnoreCase("font-size")) {
                    if (isMax)
                        setMaxFontSize(value);
                    else
                        setMinFontSize(value);
                }
                if (attribute.equalsIgnoreCase("opacity")) {                    
                    if (isMax)
                        setMaxOpacity(Double.parseDouble(value));
                    else
                        setMinOpacity(Double.parseDouble(value));
                }
            }

        }
    }

    private String getUnits(String cssFontString, ParsePosition pos) {
        if (pos.getIndex() == cssFontString.length())
            return "";
        String fontUnits = cssFontString.substring(pos.getIndex()).trim();
        String[] allUnits = new String[]{"em", "ex", "px", "pt", "pc", "in", "mm", "cm", "%"};
        for (String curUnit : allUnits) {
            if (fontUnits.equalsIgnoreCase(curUnit))
                return curUnit;
        }
        throw new FacesException("Units are wrong " + fontUnits);
    }

    private String getFontSize(TagCloudItem item) {
        DecimalFormat format = new DecimalFormat();
        Number maxFontSizeValue = 0;
        Number minFontSizeValue = 0;

        String maxFontSize = getMaxFontSize();
        String minFontSize = getMinFontSize();
        String maxFontUnits = "em";
        String minFontUnits = "em";

        ParsePosition pos = new ParsePosition(0);

        if (maxFontSize.length() != 0) {
            maxFontSizeValue = format.parse(maxFontSize, pos);
            maxFontUnits = getUnits(maxFontSize, pos);
        }

        pos.setIndex(0);

        if (minFontSize.length() != 0) {
            minFontSizeValue = format.parse(minFontSize, pos);
            minFontUnits = getUnits(minFontSize, pos);
        }

        double resultDoubleValue;

        StringBuilder toReturn = new StringBuilder();

        if (maxFontSize.length() == 0 && minFontSize.length() == 0) {
            resultDoubleValue = Math.round(getParameterGradient(item, DEFAULT_MIN_FONT_SIZE, DEFAULT_MAX_FONT_SIZE) * 100) / 100.;
            return toReturn.append(String.valueOf(resultDoubleValue)).append("px").toString();
        }

        if (maxFontSize.length() != 0 && minFontSize.length() == 0) {
            double max = maxFontSizeValue.doubleValue();
            resultDoubleValue = Math.round(getParameterGradient(item, max - 0.4 * max, max) * 100) / 100.;
            return toReturn.append(String.valueOf(resultDoubleValue)).append(maxFontUnits).toString();
        }

        if (maxFontSize.length() == 0 && minFontSize.length() != 0) {
            double min = minFontSizeValue.doubleValue();
            resultDoubleValue = Math.round(getParameterGradient(item, min, min + 0.4 * min) * 100) / 100.;
            return toReturn.append(String.valueOf(resultDoubleValue)).append(maxFontUnits).toString();
        }

        if (!maxFontUnits.equalsIgnoreCase(minFontUnits))
            throw new FacesException("Units of font-size attribute must be the same, but was: " +
                    minFontUnits + " from minItemStyle, and " + maxFontUnits + " from maxItemStyle.");

        resultDoubleValue = getParameterGradient(item, minFontSizeValue.doubleValue(), maxFontSizeValue.doubleValue());
        resultDoubleValue = Math.round(resultDoubleValue * 100) / 100.;

        return toReturn.append(String.valueOf(resultDoubleValue)).append(minFontUnits).toString();

    }

    private double getOpacity(TagCloudItem item) {
        double minOpacity = getMinOpacity();
        double maxOpacity = getMaxOpacity();
        if (minOpacity == 0 && maxOpacity == 0){
            return 1;
        }
        
        minOpacity = minOpacity == 0 ? DEFAULT_MIN_OPACITY : minOpacity;
        maxOpacity = maxOpacity == 0 ? DEFAULT_MAX_OPACITY : maxOpacity;
        return Math.round(getParameterGradient(item, minOpacity, maxOpacity) * 100) / 100.;
    }

    private Color getColor(TagCloudItem item) {
        Color maxColor = getMaxColor();
        Color minColor = getMinColor();

        int maxRed = maxColor != null ? maxColor.getRed() : DEFAULT_MAX_RED;
        int maxGreen = maxColor != null ? maxColor.getGreen() : DEFAULT_MAX_GREEN;
        int maxBlue = maxColor != null ? maxColor.getBlue() : DEFAULT_MAX_BLUE;

        int minRed = minColor != null ? minColor.getRed() : DEFAULT_MIN_RED;
        int minGreen = minColor != null ? minColor.getGreen() : DEFAULT_MIN_GREEN;
        int minBlue = minColor != null ? minColor.getBlue() : DEFAULT_MIN_BLUE;

        int red = (int) getParameterGradient(item, minRed, maxRed);
        int green = (int) getParameterGradient(item, minGreen, maxGreen);
        int blue = (int) getParameterGradient(item, minBlue, maxBlue);

        return new Color(red, green, blue);

    }

    private double getParameterGradient(TagCloudItem item, double minParameterValue, double maxParameterValue) {
        if (getMaxItemWeight() == getMinItemWeight())
            return Math.round((maxParameterValue + minParameterValue) / 2);
        double weight = item.getWeight();
        if (weight <= getMinItemWeight())
            return minParameterValue;
        return (maxParameterValue - minParameterValue) * (weight - getMinItemWeight()) / (getMaxItemWeight() - getMinItemWeight()) + minParameterValue;
    }

}

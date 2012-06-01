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

import org.openfaces.component.ComponentConfigurator;
import org.openfaces.component.OUICommand;
import org.openfaces.util.Rendering;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @author : roman.nikolaienko
 */
public class TagCloudItem extends OUICommand implements ValueHolder, ComponentConfigurator {

    public static final String COMPONENT_TYPE = "org.openfaces.TagCloudItem";
    public static final String COMPONENT_FAMILY = "org.openfaces.TagCloudItem";

    private static final String DEFAULT_TEXT_CONTAINER = "::item_textValue";
    private static final String DEFAULT_WEIGHT = "::item_weight";

    private static final String DEFAULT_CLASS = "o_tagCloud_item";
    private static final String DEFAULT_ROLLOVER_CLASS = "o_tagCloud_item_rollover";
        
    private static final String DEFAULT_CLASS_3D = "o_tagCloud_item_3d";
    private static final String DEFAULT_ROLLOVER_CLASS_3D = "o_tagCloud_item_3d_rollover";

    private static final String DEFAULT_INNER_WEIGHT_CLASS = "o_tagCloud_item_inner_weight";
    private static final String DEFAULT_INNER_TEXT_CLASS = "o_tagCloud_item_inner_text";

    private String title;
    private String url;
    private Double weight;

    private String textClass;
    private String textStyle;

    private String weightClass;
    private String weightStyle;

    private String gradientStyle;
    private String onItemClick;
    private Converter converter = null;
    private Object value = null;

    public TagCloudItem() {
        setRendererType("org.openfaces.TagCloudItem");
    }

    private String getDefaultStyleClass() {
        return ((TagCloud)getParent()).getLayout().equals(Layout.SPHERE) ?
                DEFAULT_CLASS_3D : DEFAULT_CLASS;
    }

    private String getDefaultRolloverClass() {
        return ((TagCloud)getParent()).getLayout().equals(Layout.SPHERE) ?
               DEFAULT_ROLLOVER_CLASS_3D : DEFAULT_ROLLOVER_CLASS;
    }

    private String getDefaultTextClass() {
        return getRolloverStyle() != null ? "" : DEFAULT_INNER_TEXT_CLASS;
    }

    private String getDefaultWeightClass() {
        return getRolloverStyle() != null ? "" : DEFAULT_INNER_WEIGHT_CLASS;
    }

    private String getStyles() {
        return Styles.mergeStyles(getGradientStyle(), getStyle());
    }

    private String getStyleClass(FacesContext context) {
        return Styles.getCSSClass(context, this, getStyles(),
                StyleGroup.regularStyleGroup(),
                this.getStyleClass(),
                getDefaultStyleClass());
    }

    private String getRolloverStyleClass(FacesContext context) {
        return Styles.getCSSClass(context, this, getRolloverStyle(),
                StyleGroup.rolloverStyleGroup(),
                getRolloverClass(),
                getDefaultRolloverClass());
    }

    private String getTextStyleClass(FacesContext context) {
        return Styles.mergeClassNames(getDefaultTextClass(),getTextClass());
    }

    private String getWeightStyleClass(FacesContext context) {
        return Styles.mergeClassNames(getDefaultWeightClass(),getWeightClass());
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                title,
                url,
                weight,

                textClass,
                textStyle,

                weightClass,
                weightStyle,

                gradientStyle,
                onItemClick
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        title = (String) values[i++];
        url = (String) values[i++];
        weight = (Double) values[i++];

        textClass = (String) values[i++];
        textStyle = (String) values[i++];

        weightClass = (String) values[i++];
        weightStyle = (String) values[i++];

        gradientStyle=(String) values[i++];
        onItemClick = (String) values[i++];
    }

    public String getTitle() {
        return title == null ? "null" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url == null ? "null" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getTextClass() {
        return textClass;
    }

    public void setTextClass(String textClass) {
        this.textClass = textClass;
    }

    public String getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
    }

    public String getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(String weightClass) {
        this.weightClass = weightClass;
    }

    public String getWeightStyle() {
        return weightStyle;
    }

    public void setWeightStyle(String weightStyle) {
        this.weightStyle = weightStyle;
    }

    public String getGradientStyle() {
        return gradientStyle;
    }

    public void setGradientStyle(String gradientStyle) {
        this.gradientStyle = gradientStyle;
    }

    public String getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(String onItemClick) {
        this.onItemClick = onItemClick;
    }

    public String getTextValue() {
        return Rendering.convertToString(getFacesContext(), this, getValue());
    }


    public void encodeBegin(FacesContext context) throws IOException {
        TagCloud tagCloud = (TagCloud) getParent();
        ResponseWriter writer = context.getResponseWriter();
        String id = getClientId(context);
        String styleClass = getStyleClass(context);
        String rolloverStyleClass = getRolloverStyleClass(context);
        String textStyleClass = getTextStyleClass(context);
        String weightStyleClass = getWeightStyleClass(context);

        Styles.renderStyleClasses(context, this, false, true);
        TagCloud cloud = (TagCloud) getParent();
        DecimalFormat weightFormat = new DecimalFormat(cloud.getItemWeightFormat());
        String weightStyle = getWeightStyle();

        writer.startElement("a", cloud);

        writer.writeAttribute("id", id, null);
        writer.writeAttribute("title", getTitle(), null);
        writer.writeAttribute("href", getUrl(), null);
        writer.writeAttribute("class", styleClass, null);
        writer.writeAttribute("style", getStyles(), null);

        setOnclick(getOnItemClick());
        boolean ajaxJsRequired = Rendering.writeEventsWithAjaxSupport(context, writer, this);
        if (ajaxJsRequired)
            this.getAttributes().put("_ajaxRequired", Boolean.TRUE);

        writer.startElement("span", this);
        writer.writeAttribute("id", id + DEFAULT_TEXT_CONTAINER, null);
        writer.writeAttribute("class", textStyleClass, null);
        writer.writeAttribute("style", getTextStyle(), null);
        writer.writeText(getTextValue(), null);
        writer.endElement("span");
        StringBuilder styleBuilder = new StringBuilder("");
        if (weightStyle != null)
            styleBuilder.append(weightStyle);
        if (!cloud.isItemWeightVisible()) {
            styleBuilder.append(";display:none;");
        } else{
            styleBuilder.append(";display:inline;");
        }

        writer.startElement("span", this);
        writer.writeAttribute("id", id + DEFAULT_WEIGHT, null);
        writer.writeAttribute("class", weightStyleClass, null);

        writer.writeAttribute("style", styleBuilder.toString(), null);
        writer.writeText(weightFormat.format(getWeight()), null);
        writer.endElement("span");

        Script initScript = new ScriptBuilder().initScript(context, this, "O$.TagCloudItem._init",
                styleClass,
                rolloverStyleClass,
                tagCloud.getClientId(context),
                tagCloud.getStyle(),
                tagCloud.getStyleClass()
        );
        Rendering.renderInitScript(context, initScript);
        super.encodeBegin(context);
    }

    public void encodeEnd(FacesContext context) throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        writer.endElement("a");

        writer.flush();
    }

    public void encodeChildren(FacesContext context) throws IOException {
        super.encodeChildren(context);
    }

    public Object getLocalValue() {
        return (this.value);
    }

    public Converter getConverter() {
        if (this.converter != null) {
            return (this.converter);
        }
        ValueExpression ve = getValueExpression("converter");
        if (ve != null) {
            try {
                return ((Converter) ve.getValue(getFacesContext().getELContext()));
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return (null);
        }
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public UIComponent getConfiguredComponent() {
        return getParent();
    }

}

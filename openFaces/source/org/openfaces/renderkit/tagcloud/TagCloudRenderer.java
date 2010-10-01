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

package org.openfaces.renderkit.tagcloud;

import org.openfaces.component.tagcloud.TagCloud;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Map;

/**
 * @author : roman.nikolaienko
 */
public class TagCloudRenderer extends RendererBase {

    private static final String DEFAULT_VAR_TITLE = "::var_title";
    private static final String DEFAULT_VAR_TEXT = "::var_textValue";
    private static final String DEFAULT_VAR_URL = "::var_url";
    private static final String DEFAULT_VAR_WEIGHT = "::var_weight";

    private static final String DEFAULT_CLASS_RECTANGLE_LAYOUT = "o_tagCloud_rectangle";
    private static final String DEFAULT_CLASS_VERTICAL_LAYOUT = "o_tagCloud_vertical";
    private static final String DEFAULT_CLASS_OVAL_LAYOUT = "o_tagCloud_oval";

    private static final String DEFAULT_ROLLOVER_CLASS = "o_tagCloud_rollover";

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map<String, String> requestParameters = context.getExternalContext().getRequestParameterMap();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String key = component.getClientId(context);

        if (requestParameters.containsKey(key + DEFAULT_VAR_TEXT)) {
            TagCloud cloud = (TagCloud) component;

            cloud.queueEvent(new ActionEvent(cloud));

            requestMap.put(cloud.getVar(), cloud.getPrevVarValue());

            ELContext elContext = context.getELContext();

            String varTitle = requestParameters.get(key + DEFAULT_VAR_TITLE);
            String varText = requestParameters.get(key + DEFAULT_VAR_TEXT);
            String varUrl = requestParameters.get(key + DEFAULT_VAR_URL);
            String varWeight = requestParameters.get(key + DEFAULT_VAR_WEIGHT);

            ValueExpression textExpression = cloud.getItemText();
            ValueExpression titleExpression = cloud.getItemTitle();
            ValueExpression urlExpression = cloud.getItemUrl();
            ValueExpression weightExpression = cloud.getItemWeight();

            if (titleExpression != null && varTitle != null) {
                titleExpression.setValue(elContext, varTitle);
            }
            if (textExpression != null && varText != null) {
                Converter converter = cloud.getConverter();
                textExpression.setValue(elContext, converter != null ?
                        converter.getAsObject(context, cloud, varText) : varText);
            }
            if (urlExpression != null && varUrl != null)
                urlExpression.setValue(elContext, varUrl);

            Number weightToSet = null;
            
            if (varWeight != null) {
                DecimalFormat weightFormat = new DecimalFormat(cloud.getItemWeightFormat());
                ParsePosition pos = new ParsePosition(0);
                weightToSet = weightFormat.parse(varWeight, pos);
            }

            if (weightExpression != null && weightToSet != null) {                
                weightExpression.setValue(elContext, weightToSet);
            }
        }
    }

    private String getDefaultStyleClass(TagCloud cloud) {
        switch (cloud.getLayout()) {
            case RECTANGLE: {
                return DEFAULT_CLASS_RECTANGLE_LAYOUT;
            }
            case VERTICAL: {
                return DEFAULT_CLASS_VERTICAL_LAYOUT;
            }
            case OVAL: {
                return DEFAULT_CLASS_OVAL_LAYOUT;
            }
        }
        throw new FacesException("No default style for this layout type: ");
    }

    private String getDefaultRolloverClass() {
        return DEFAULT_ROLLOVER_CLASS;
    }

    private String getTagCloudStyleClass(FacesContext context, TagCloud cloud) {
        String tagCloudStyleClass = Styles.mergeClassNames(DefaultStyles.CLASS_INITIALLY_INVISIBLE, cloud.getStyleClass());
        return Styles.getCSSClass(context, cloud, cloud.getStyle(),
                StyleGroup.regularStyleGroup(),
                tagCloudStyleClass,
                getDefaultStyleClass(cloud));
    }

    private String getRolloverStyleClass(FacesContext context, TagCloud cloud) {
        return Styles.getCSSClass(context, cloud, cloud.getRolloverStyle(),
                StyleGroup.rolloverStyleGroup(),
                cloud.getRolloverClass(),
                null);
        //getDefaultRolloverClass());
    }

    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        Components.generateIdIfNotSpecified(component);

        TagCloud cloud = (TagCloud) component;

        String clientId = component.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        Rendering.writeStandardEvents(writer, cloud);

        writer.startElement("div", cloud);
        writeAttribute(writer, "id", clientId);

        writeAttribute(writer, "class", getTagCloudStyleClass(context, cloud));
        writeAttribute(writer, "style", cloud.getStyle());
        encodeScripts(context, cloud);
        encodeVarFields(context, cloud);
        encodeLayout(context, cloud);

    }

    private void encodeVarFields(FacesContext context, TagCloud cloud) throws IOException {
        ELContext elContext = context.getELContext();
        String clientId = cloud.getClientId(context);

        ResponseWriter writer = context.getResponseWriter();

        ValueExpression textExpression = cloud.getItemText();
        ValueExpression titleExpression = cloud.getItemTitle();
        ValueExpression urlExpression = cloud.getItemUrl();
        ValueExpression weightExpression = cloud.getItemWeight();

        String title = titleExpression != null ? (String) titleExpression.getValue(elContext) : "";

        Object textValue = textExpression != null ? textExpression.getValue(elContext) : "";

        String url = urlExpression != null ? (String) urlExpression.getValue(elContext) : "#";

        double weight = weightExpression != null ? (Double) weightExpression.getValue(elContext) : 0;
        writer.startElement("div", cloud);

        writeAttribute(writer, "style", "visibility:hidden;");

        writer.startElement("input", cloud);
        writeAttribute(writer, "id", clientId + DEFAULT_VAR_TITLE);
        writeAttribute(writer, "name", clientId + DEFAULT_VAR_TITLE);
        writeAttribute(writer, "type", "hidden");
        writeAttribute(writer, "value", title == null ? "" : title);
        writer.endElement("input");

        writer.startElement("input", cloud);
        writeAttribute(writer, "id", clientId + DEFAULT_VAR_TEXT);
        writeAttribute(writer, "name", clientId + DEFAULT_VAR_TEXT);
        writeAttribute(writer, "type", "hidden");
        Converter converter = cloud.getConverter();
        String text = converter != null ?
                converter.getAsString(context, cloud, textValue) :
                (textValue != null ? textValue.toString() : "");
        writeAttribute(writer, "value", text);
        writer.endElement("input");

        writer.startElement("input", cloud);
        writeAttribute(writer, "id", clientId + DEFAULT_VAR_URL);
        writeAttribute(writer, "name", clientId + DEFAULT_VAR_URL);
        writeAttribute(writer, "type", "hidden");
        writeAttribute(writer, "value", url == null ? "" : url);
        writer.endElement("input");

        writer.startElement("input", cloud);
        writeAttribute(writer, "id", clientId + DEFAULT_VAR_WEIGHT);
        writeAttribute(writer, "name", clientId + DEFAULT_VAR_WEIGHT);
        writeAttribute(writer, "type", "hidden");
        writeAttribute(writer, "value", weight == 0 ? "0" : String.valueOf(weight));
        writer.endElement("input");

        writer.endElement("div");
    }

    private void encodeScripts(FacesContext context, TagCloud cloud) throws IOException {

        Script initScript = new ScriptBuilder().initScript(context, cloud, "O$.TagCloud._init",
                cloud.getLayout(),
                getRolloverStyleClass(context, cloud)
        );
        Rendering.renderInitScript(context, initScript,
                Resources.getUtilJsURL(context),
                Resources.getInternalURL(context, "util/jquery-1.4.2.min.js"),
                Resources.getInternalURL(context, "tagcloud/tagcloud.js"),
                Resources.getInternalURL(context, "tagcloud/tagclouditem.js")
        );

        Styles.renderStyleClasses(context, cloud);
    }


    public void encodeLayout(FacesContext context, TagCloud cloud)
            throws IOException {
        AbstractTagCloudLayoutRender.getInstance(cloud.getLayout()).renderLayout(context, cloud);
    }

    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
        writer.flush();
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        Rendering.encodeClientActions(context, component);
    }


}

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

package org.openfaces.renderkit.tagcloud;

import org.openfaces.component.tagcloud.Layout;
import org.openfaces.component.tagcloud.TagCloud;
import org.openfaces.component.tagcloud.TagCloudItem;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author : roman.nikolaienko
 */
public class TagCloudRenderer extends RendererBase {

    private static final String DEFAULT_CLASS_RECTANGLE_LAYOUT = "o_tagCloud_rectangle";
    private static final String DEFAULT_CLASS_VERTICAL_LAYOUT = "o_tagCloud_vertical";
    private static final String DEFAULT_CLASS_OVAL_LAYOUT = "o_tagCloud_oval";
    private static final String DEFAULT_CLASS_SPHERE_LAYOUT = "o_tagCloud_3d";
        
    private static final String DEFAULT_ITEM_ROLLOVER_CLASS = "o_tagCloud_item_rollover";
    private static final String DEFAULT_ITEM_ROLLOVER_CLASS_3D = "o_tagCloud_item_3d_rollover";
    private static final String DEFAULT_ID_FIELD = "::id";

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map<String, String> requestParameters = context.getExternalContext().getRequestParameterMap();
        TagCloud cloud = (TagCloud) component;
        String clientId = cloud.getClientId(context);

        if (requestParameters.containsKey(clientId+DEFAULT_ID_FIELD)) {
            String id = requestParameters.get(clientId+DEFAULT_ID_FIELD);
            String preId = cloud.getClientId(context) + TagCloud.DEFAULT_ITEM_ID_PREFIX;
            int index = preId.length();
            if (index < id.length() && preId.equals(id.substring(0, index))) {
                cloud.queueEvent(new ActionEvent(cloud));
                cloud.selectItemObject(context, id.substring(index));
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
            case SPHERE: {
                return DEFAULT_CLASS_SPHERE_LAYOUT;
            }
        }
        throw new FacesException("No default style for this layout type. ");
    }

    private String getDefaultItemRolloverClass(TagCloud cloud) {
        return cloud.getLayout().equals(Layout.SPHERE) ? DEFAULT_ITEM_ROLLOVER_CLASS_3D : DEFAULT_ITEM_ROLLOVER_CLASS;
    }

    private String getTagCloudStyleClass(FacesContext context, TagCloud cloud) {
        return Styles.getCSSClass(context, cloud, cloud.getStyle(),
                StyleGroup.regularStyleGroup(),
                cloud.getStyleClass(),
                getDefaultStyleClass(cloud));
    }

    private String getRolloverStyleClass(FacesContext context, TagCloud cloud) {
        return Styles.getCSSClass(context, cloud, cloud.getRolloverStyle(),
                StyleGroup.rolloverStyleGroup(),
                cloud.getRolloverClass(),
                null);        
    }

    private String getItemRolloverStyleClass(FacesContext context, TagCloud cloud) {
        return Styles.getCSSClass(context, cloud, cloud.getItemRolloverStyle(),
                StyleGroup.rolloverStyleGroup(),
                cloud.getItemRolloverClass(),
                getDefaultItemRolloverClass(cloud));
    }


    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        Components.generateIdIfNotSpecified(component);
        TagCloud cloud = (TagCloud) component;

        if ((cloud.getItemUrl() != null &&  cloud.getRender() != null))
            throw new FacesException("Both attributes 'itemUrl' and 'renderer' cannot be set in the <o:tagCloud> component at the same time");

        String clientId = cloud.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        Rendering.writeStandardEvents(writer, cloud);

        writer.startElement("div", cloud);
        writeAttribute(writer, "id", clientId);

        writeAttribute(writer, "class", getTagCloudStyleClass(context, cloud));
        writeAttribute(writer, "style", cloud.getStyle());
        encodeScripts(context, cloud);
        encodeIdField(context, cloud);
        encodeLayout(context, cloud);
    }

    private void encodeIdField(FacesContext context, TagCloud cloud) throws IOException {
        String clientId = cloud.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("input", cloud);
        writer.writeAttribute("id", clientId+DEFAULT_ID_FIELD, null);
        writer.writeAttribute("name", clientId+DEFAULT_ID_FIELD, null);
        writeAttribute(writer, "type", "hidden");
        writer.endElement("input");
    }

    private void encodeScripts(FacesContext context, TagCloud cloud) throws IOException {
        String  rolloverClass = getRolloverStyleClass(context, cloud);
        String  itemRolloverClass = getItemRolloverStyleClass(context, cloud);

        Script initScript = new ScriptBuilder().initScript(context, cloud, "O$.TagCloud._init",
                cloud.getLayout(),
                rolloverClass,
                itemRolloverClass,
                cloud.getShadowScale3D(),
                cloud.getRotationSpeed3D(),
                cloud.getStopRotationPeriod3D()
        );
        Styles.renderStyleClasses(context, cloud, false, true);
        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "util/jquery-1.4.2.min.js"),
                Resources.internalURL(context, "tagcloud/tagcloud.js"),
                Resources.internalURL(context, "tagcloud/tagclouditem.js")
        );

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
        TagCloud tagCloud = (TagCloud) component;
        List<UIComponent> children = tagCloud.getChildren();
        List<TagCloudItem> items = tagCloud.getTagCloudItems(context);
        for (TagCloudItem tagCloudItem : items) {
            children.remove(tagCloudItem);
        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        // Do nothing, custom children renderer is used
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}

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
package org.openfaces.renderkit.chart;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.imagemap.StandardToolTipTagFragmentGenerator;
import org.jfree.chart.imagemap.StandardURLTagFragmentGenerator;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.GridPointInfo;
import org.openfaces.component.chart.PieChartView;
import org.openfaces.component.chart.PieSectorInfo;
import org.openfaces.component.chart.impl.JfcRenderHints;
import org.openfaces.component.chart.impl.helpers.ChartInfoUtil;
import org.openfaces.component.chart.impl.helpers.MapRenderUtilities;
import org.openfaces.component.command.PopupMenu;
import org.openfaces.component.output.DynamicImage;
import org.openfaces.component.output.ImageType;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.renderkit.output.DynamicImageRenderer;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartRenderer extends RendererBase {

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
        // This is done to avoid rendering during encode begin phase
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        // This is done to avoid rendering of child tags during component's rendering
    }

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        Components.generateIdIfNotSpecified(component);
        ResponseWriter writer = facesContext.getResponseWriter();
        Chart chart = (Chart) component;

        ChartView view = chart.getChartView();
        if (!chart.isRendered() || view == null)
            return;

        writer.startElement("div", chart);
        writeIdAttribute(facesContext, chart);
        Rendering.writeComponentClassAttribute(writer, chart);
        writer.writeAttribute("style", "width: " + chart.getWidth() + "px;" + " height:" + chart.getHeight() + "px;", "style");

        String actionFiledId = chart.getClientId(facesContext) + MapRenderUtilities.ACTION_FIELD_SUFFIX;
        Rendering.writeNewLine(writer);
        Rendering.renderHiddenField(writer, actionFiledId, null);
        Rendering.writeNewLine(writer);

        final byte[] imageAsByteArray = view.renderAsImageFile();
        final JfcRenderHints renderHints = chart.getRenderHints();
        final ChartRenderingInfo renderingInfo = renderHints.getRenderingInfo();
        String mapId = renderHints.getMapId(chart);
        String map = MapRenderUtilities.getImageMapExt(chart, mapId, renderingInfo,
                new StandardToolTipTagFragmentGenerator(), new StandardURLTagFragmentGenerator());
        renderHints.setMap(map);
        if (view.getChartPopup() != null) {
            encodeChartPopup(facesContext, chart, view, renderingInfo);
        }

        chart.setImageBytes(imageAsByteArray);

        DynamicImage dynamicImage = new DynamicImage();
        ValueExpression ve = new ValueExpression() {
            public Object getValue(ELContext elContext) {
                return imageAsByteArray;
            }

            public void setValue(ELContext elContext, Object value) {
                throw new UnsupportedOperationException("Could not change 'data' property using ValueExpression");
            }

            public boolean isReadOnly(ELContext elContext) {
                return true;
            }

            public Class getType(ELContext elContext) {
                if (imageAsByteArray == null)
                    return Object.class;
                return imageAsByteArray.getClass();
            }

            public Class getExpectedType() {
                return Object.class;
            }

            public String getExpressionString() {
                return null;
            }

            public boolean equals(Object o) {
                return false;
            }

            public int hashCode() {
                return 0;
            }

            public boolean isLiteralText() {
                return false;
            }
        };
        dynamicImage.setValueExpression("data", ve);
        dynamicImage.setId("img");
        dynamicImage.setParent(chart);
        dynamicImage.setMapId(mapId);
        dynamicImage.setMap(map);
        dynamicImage.getAttributes().put(DynamicImageRenderer.DEFAULT_STYLE_ATTR, "o_chart");
        dynamicImage.setWidth(chart.getWidth());
        dynamicImage.setHeight(chart.getHeight());
        copyAttributes(dynamicImage, chart, "onclick", "ondblclick", "onmousedown", "onmouseup",
                "onmousemove", "onmouseover", "onmouseout");

        dynamicImage.setImageType(ImageType.PNG);
        dynamicImage.encodeAll(facesContext);
        if (map != null) {
            Resources.renderJSLinkIfNeeded(facesContext, Resources.getUtilJsURL(facesContext));
            Resources.renderJSLinkIfNeeded(facesContext, Resources.getInternalURL(facesContext, "chart/chartPopup.js"));
        }
        writer.endElement("div");
        encodeScripts(facesContext, chart, dynamicImage);
    }

    private void encodeChartPopup(FacesContext facesContext, Chart chart, ChartView view,
                                  ChartRenderingInfo renderingInfo) throws IOException {
        EntityCollection entities = renderingInfo.getEntityCollection();
        if (entities != null) {
            int count = entities.getEntityCount();

            for (int i = count - 1; i >= 0; i--) {
                ChartEntity entity = entities.getEntity(i);

                Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
                chart.setEntityIndex(i);

                if (view instanceof GridChartView) {
                    final GridPointInfo pointInfo = ChartInfoUtil.getGridPointInfo(entity, chart);
                    if (pointInfo != null) {
                        Object oldAttributeValue = requestMap.put("point", pointInfo);
                        renderChartPopup(facesContext, view);
                        requestMap.put("point", oldAttributeValue);
                    }
                } else if (view instanceof PieChartView) {
                    final PieSectorInfo pieSectorInfo = ChartInfoUtil.getPieSectorInfo(entity);
                    if (pieSectorInfo != null) {
                        Object oldAttributeValue = requestMap.put("sector", pieSectorInfo);
                        renderChartPopup(facesContext, view);
                        requestMap.put("sector", oldAttributeValue);
                    }
                }
            }
        }
    }

    private void renderChartPopup(FacesContext facesContext, ChartView view) throws IOException {
        view.getChartPopup().encodeAll(facesContext);
    }

    protected void encodeScripts(FacesContext context, Chart chart, DynamicImage dynamicImage) throws IOException {
        ScriptBuilder buf = new ScriptBuilder();
        final Integer oldEntityIndex = chart.getEntityIndex();
        chart.setEntityIndex(-1);
        buf.initScript(context, chart, "O$.Chart._init");

        encodeChartMenuSupport(context, chart, dynamicImage, buf);
        Rendering.renderInitScript(context, buf, Resources.getUtilJsURL(context),
                Resources.getInternalURL(context, "chart/chart.js"),
                Resources.getAjaxUtilJsURL(context),
                getChartMenuJsURL(context));
        chart.setEntityIndex(oldEntityIndex);
    }

    private void encodeChartMenuSupport(FacesContext context, Chart chart, DynamicImage dynamicImage,
                                        ScriptBuilder buf) throws IOException {
        UIComponent component = chart.getChartMenu();
        if (component == null) return;

        PopupMenu chartMenu = (PopupMenu) component;
        chartMenu.encodeAll(context);

        buf.initScript(context, chartMenu, "O$.ChartMenu._init", chart, dynamicImage.getClientId(context));
    }

    private String getChartMenuJsURL(FacesContext context) {
        return Resources.getInternalURL(context, "chart/chartMenu.js");
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();

        Chart chart = (Chart) component;
        String name = chart.getClientId(FacesContext.getCurrentInstance()) + MapRenderUtilities.ACTION_FIELD_SUFFIX;
        String actionValue = (String) requestParameterMap.get(name);

        if (actionValue != null && !actionValue.equals("")) {
            if (actionValue.equals("title")) {
                //   chart.setAction(chart.getTitle().getActionExpression());
                chart.getTitle().decodeAction(actionValue);
            } else {
                //   chart.setAction(chart.getChartView().getActionExpression());
                chart.getChartView().decodeAction(actionValue);
                // chart.getChartView().queueEvent(event);
            }
        }
        chart.setModel(null);
    }

    private void copyAttributes(UIComponent dest, UIComponent src, String... attributeNames) {
        for (String attributeName : attributeNames) {
            Object attributeValue = src.getAttributes().get(attributeName);
            dest.getAttributes().put(attributeName, attributeValue);
        }
    }

}

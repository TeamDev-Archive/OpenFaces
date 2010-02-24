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
package org.openfaces.component.chart.impl.helpers;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.imagemap.StandardToolTipTagFragmentGenerator;
import org.jfree.chart.imagemap.StandardURLTagFragmentGenerator;
import org.jfree.chart.imagemap.ToolTipTagFragmentGenerator;
import org.jfree.chart.imagemap.URLTagFragmentGenerator;
import org.jfree.util.StringUtils;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartTitle;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.ChartViewValueExpression;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.GridPointInfo;
import org.openfaces.component.chart.PieChartView;
import org.openfaces.component.chart.PieSectorInfo;
import org.openfaces.component.chart.impl.plots.DynamicPropertiesUtils;
import org.openfaces.util.Rendering;

import javax.faces.context.FacesContext;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class MapRenderUtilities {
    public static final String ACTION_FIELD_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "af";

    public static String getImageMapExt(
            Chart chart,
            String name,
            ChartRenderingInfo info,
            ToolTipTagFragmentGenerator toolTipTagFragmentGenerator,
            URLTagFragmentGenerator urlTagFragmentGenerator) {
        StringBuilder sb = new StringBuilder();
        sb.append("<map id=\"").append(name).append("\" name=\"").append(name).append("\">");
        sb.append(StringUtils.getLineSeparator());

        // the following is needed for displaying tooltips under IE (doesn't display tooltips if there's no area with href)
        String fakeArea = "<area shape=\"rect\" coords=\"0,0,0,0\" href=\"fake.jsp\" alt=\"\" title=\"\" />";
        sb.append(fakeArea);
        sb.append(StringUtils.getLineSeparator());

        EntityCollection entities = info.getEntityCollection();
        if (entities != null) {
            int count = entities.getEntityCount();
            for (int i = count - 1; i >= 0; i--) {
                ChartEntity entity = entities.getEntity(i);
                if (entity.getToolTipText() == null && entity.getURLText() == null && getOnClick(chart, chart.getChartView(), entity) == null)
                    continue;

                String area;
                if (i == 0) {
                    area = getTitleImageMapAreaTag(chart, new StandardToolTipTagFragmentGenerator(), new StandardURLTagFragmentGenerator(), entity);
                } else {
                    ChartView view = chart.getChartView();
                    if (entity instanceof PieSectionEntity) {
                        area = getImageMapAreaTag(chart, view, toolTipTagFragmentGenerator, urlTagFragmentGenerator, entity, i);
                    } else if (entity instanceof CategoryItemEntity) {
                        area = getImageMapAreaTag(chart, view, toolTipTagFragmentGenerator, urlTagFragmentGenerator, entity, i);
                    } else if (entity instanceof XYItemEntity) {
                        area = getImageMapAreaTag(chart, view, toolTipTagFragmentGenerator, urlTagFragmentGenerator, entity, i);
                    } else {
                        area = "";
                    }
                }

                if (area.length() > 0) {
                    sb.append(area);
                    sb.append(StringUtils.getLineSeparator());
                }
            }

        }

        sb.append("</map>");
        return sb.toString();
    }


    private static String getTitleImageMapAreaTag(
            Chart chart,
            ToolTipTagFragmentGenerator toolTipTagFragmentGenerator,
            URLTagFragmentGenerator urlTagFragmentGenerator,
            ChartEntity entity) {
        StringBuilder tag = new StringBuilder();
        ChartTitle title = chart.getTitle();
        boolean hasURL = entity.getURLText() != null && !entity.getURLText().equals("");
        boolean hasAction = title.getActionExpression() != null || title.getActionListeners().length > 0;
        boolean hasToolTip = title.getTooltip() != null && !title.getTooltip().equals("");

        if (hasURL || hasToolTip || hasAction) {
            String fieldId = chart.getClientId(FacesContext.getCurrentInstance()) + ACTION_FIELD_SUFFIX;

            tag.append("<area");

            if (hasAction)
                tag.append(" onclick=\"O$.setValue('").append(fieldId).append("','title'); O$.submitById('").append(fieldId).append("');\"");

            tag.append(" shape=\"").append(entity.getShapeType()).append("\"" + " coords=\"").append(entity.getShapeCoords()).append("\"");

            if (hasToolTip) {
                String toolTipText = entity.getToolTipText();
                if (toolTipText != null)
                    tag.append(toolTipTagFragmentGenerator.generateToolTipFragment(toolTipText));
            }

            if (hasURL && !hasAction)
                tag.append(urlTagFragmentGenerator.generateURLFragment(entity.getURLText()));


            if (!hasToolTip)
                tag.append(" alt=\"\"");

            tag.append("/>");
        }
        return tag.toString();
    }

    private static String getImageMapAreaTag(
            Chart chart, ChartView view,
            ToolTipTagFragmentGenerator toolTipTagFragmentGenerator,
            URLTagFragmentGenerator urlTagFragmentGenerator,
            ChartEntity entity,
            int entityIndex) {
        StringBuilder tag = new StringBuilder();
        boolean hasURL = entity.getURLText() != null && !entity.getURLText().equals("");
        boolean hasAction = view.getActionExpression() != null || view.getActionListeners().length > 0;
        boolean hasToolTip = entity.getToolTipText() != null && !entity.getToolTipText().equals("");

        String onClick = getOnClick(chart, view, entity);
        String onMouseOut = getOnMouseOut(chart, view, entity);
        String onMouseOver = getOnMouseOver(chart, view, entity);

        boolean hasCustomOnMouseOver = onMouseOver != null && !onMouseOver.equals("");
        boolean hasCustomOnMouseOut = onMouseOut != null && !onMouseOut.equals("");
        boolean hasCustomClick = onClick != null && !onClick.equals("");

        if (hasURL || hasToolTip || hasAction || hasCustomClick) {
            String fieldId = view.getParent().getClientId(FacesContext.getCurrentInstance()) + ACTION_FIELD_SUFFIX;

            tag.append("<area");

            if (hasAction && !hasCustomClick)
                tag.append(" onclick=\"O$.setValue('").append(fieldId).append("','").append(entityIndex).append("'); O$.submitById('").append(fieldId).append("');\"");

            if (hasAction && hasCustomClick)
                tag.append(" onclick=\"O$.setValue('").append(fieldId).append("','").append(entityIndex).append("'); ").append(onClick).append("O$.submitById('").append(fieldId).append("');\"");

            if (!hasAction && hasCustomClick)
                tag.append(" onclick=\"O$.setValue('").append(fieldId).append("','").append(entityIndex).append("'); ").append(onClick).append("\"");

            if (hasCustomOnMouseOver)
                tag.append(" onmouseover=\"").append(onMouseOver).append("\"");

            if (hasCustomOnMouseOut)
                tag.append(" onmouseout=\"").append(onMouseOut).append("\"");

            tag.append(" shape=\"").append(entity.getShapeType()).append("\"" + " coords=\"").append(entity.getShapeCoords()).append("\"");

            if (hasToolTip)
                tag.append(toolTipTagFragmentGenerator.generateToolTipFragment(entity.getToolTipText()));

            if (hasURL && !hasAction)
                tag.append(urlTagFragmentGenerator.generateURLFragment(entity.getURLText()));

            if (!hasToolTip)
                tag.append(" alt=\"\"");

            tag.append("/>");
        }
        return tag.toString();
    }

    private static String getOnClick(Chart chart, ChartView view, ChartEntity entity) {
        if (view.getOnclick() != null) {
            return view.getOnclick();
        } else if (view.getDynamicOnclick() != null) {
            return getDynamicProperties(chart, view.getDynamicOnclick(), entity);
        }
        return null;
    }

    private static String getOnMouseOver(Chart chart, ChartView view, ChartEntity entity) {
        if (view.getOnmouseover() != null) {
            return view.getOnmouseover();
        } else if (view.getDynamicOnMouseOver() != null) {
            return getDynamicProperties(chart, view.getDynamicOnMouseOver(), entity);
        }
        return null;
    }

    private static String getOnMouseOut(Chart chart, ChartView view, ChartEntity entity) {
        if (view.getOnmouseout() != null) {
            return view.getOnmouseout();
        } else if (view.getDynamicOnMouseOut() != null) {
            return getDynamicProperties(chart, view.getDynamicOnMouseOut(), entity);
        }
        return null;
    }

    private static String getDynamicProperties(Chart chart, ChartViewValueExpression view, ChartEntity entity) {
        GridPointInfo info = ChartInfoUtil.getGridPointInfo(entity, chart);
        if (info == null) {
            PieSectorInfo infoPie = ChartInfoUtil.getPieSectorInfo(entity);
            if (infoPie != null) {
                PieChartView pieView = (PieChartView) chart.getChartView();
                pieView.setSector(infoPie);
                String res = (String) DynamicPropertiesUtils.getDynamicValue("sector", infoPie, view);
                pieView.setSector(null);
                return res;
            }
        } else {
            GridChartView gridView = (GridChartView) chart.getChartView();
            gridView.setPoint(info);
            String res = (String) DynamicPropertiesUtils.getDynamicValue("point", info, view);
            gridView.setPoint(null);
            return res;
        }
        return null;
    }

}
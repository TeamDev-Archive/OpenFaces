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

package org.openfaces.component.chart.impl.renderers;

import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.XYSeriesLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.BooleanUtilities;
import org.jfree.util.ShapeUtilities;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.GradientLineAreaFill;
import org.openfaces.component.chart.LineAreaFill;
import org.openfaces.component.chart.SolidLineAreaFill;
import org.openfaces.component.chart.impl.configuration.ConfigurableRenderer;
import org.openfaces.component.chart.impl.configuration.RendererConfigurator;
import org.openfaces.component.chart.impl.renderers.states.XYLineFillItemRendererState;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class XYLineFillRenderer extends AbstractXYItemRenderer
        implements XYItemRenderer, XYRendererAdapter, AreaFillRenderer, CustomizedRenderer, ConfigurableRenderer {

    private ItemsRenderer delegate = new ItemsRenderer();
    private ConfigurableRendererBase configurationDelegate = new ConfigurableRendererBase();
    private Map<Integer, Boolean> seriesLinesVisible = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> seriesShapesVisible = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> seriesShapesFilled = new HashMap<Integer, Boolean>();
    private boolean baseShapesVisible = true;
    private boolean baseLinesVisible = true;
    private boolean baseShapesFilled = true;
    private boolean useFillPaint = false;
    private boolean useOutlinePaint = false;
    private boolean drawFilledArea = true;
    private boolean drawOutlines = false;
    private Paint backgroundPaint;
    private LineAreaFill lineAreaFill;

    public int getPassCount() {
        return 2;
    }

    public XYItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea,
                                          XYPlot plot, XYDataset data, PlotRenderingInfo info) {
        XYLineFillItemRendererState itemRendererState = new XYLineFillItemRendererState(info);
        itemRendererState.setProcessVisibleItemsOnly(false);

        return itemRendererState;
    }

    public LegendItem getLegendItem(int dataSetIndex, int series) {
        XYPlot plot = getPlot();
        if (plot == null) {
            return null;
        }

        LegendItem legendItem = null;
        XYDataset dataSet = plot.getDataset(dataSetIndex);
        if (dataSet != null) {
            if (getItemVisible(series, 0)) {
                legendItem = createLegendItem(dataSetIndex, series, dataSet);
            }
        }

        return legendItem;

    }

    public void drawItem(Graphics2D g2, XYItemRendererState state,
                         Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
                         ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataSet,
                         int series, int item, CrosshairState crosshairState, int pass) {
        if (!getItemVisible(series, item)) {
            return;
        }

        double itemXValue = dataSet.getXValue(series, item);
        double itemYValue = dataSet.getYValue(series, item);
        if (Double.isNaN(itemYValue) || Double.isNaN(itemXValue)) {
            return;
        }

        double currentItemX = calculateItemXPoint(series, item, dataArea, domainAxis, dataSet, plot);
        double currentItemY = calculateItemYPoint(series, item, dataArea, rangeAxis, dataSet, plot);
        int previousItemIndex = item > 0 ? item - 1 : 0;
        double previousItemX = calculateItemXPoint(series, previousItemIndex, dataArea, domainAxis, dataSet, plot);
        double previousItemY = calculateItemYPoint(series, previousItemIndex, dataArea, rangeAxis, dataSet, plot);
        final int lastItemIndex = dataSet.getItemCount(series) - 1;
        int nextItemIndex = item < lastItemIndex ? item + 1 : lastItemIndex;
        double nextItemX = calculateItemXPoint(series, nextItemIndex, dataArea, domainAxis, dataSet, plot);
        double nextItemY = calculateItemYPoint(series, nextItemIndex, dataArea, rangeAxis, dataSet, plot);
        double zeroRangePoint = rangeAxis.valueToJava2D(0.0, dataArea, plot.getRangeAxisEdge());

        if (isAreaAndLinePass(pass)) {
            XYLineFillItemRendererState rendererState = (XYLineFillItemRendererState) state;
            renderLineArea(g2, info, plot, series, item, rendererState, dataSet,
                    currentItemX, currentItemY, previousItemX, previousItemY, zeroRangePoint);
        } else if (isShapesAndLabelsPass(pass)) {
            Shape entityArea = renderShapeAndLabel(g2, dataArea, plot, dataSet, series, item, itemYValue,
                    currentItemX, currentItemY, previousItemX, previousItemY, nextItemX, nextItemY, zeroRangePoint);

            int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
            int rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
            updateCrosshairValues(crosshairState, itemXValue, itemYValue, domainAxisIndex,
                    rangeAxisIndex, currentItemX, currentItemY, plot.getOrientation());

            EntityCollection entityCollection = state.getEntityCollection();
            if (entityCollection != null) {
                addEntity(entityCollection, entityArea, dataSet, series, item, 0.0, 0.0);
            }
        } else {
            throw new IllegalStateException("Unknown pass: " + pass);
        }
    }


    private void renderLineArea(Graphics2D g2, PlotRenderingInfo info, XYPlot plot, int series, int item,
                                XYLineFillItemRendererState rendererState, XYDataset dataSet,
                                double currentItemX, double currentItemY,
                                double previousItemX, double previousItemY,
                                double zeroRangePoint) {
        boolean isFirstItem = item == 0;
        boolean isLastItem = item == (dataSet.getItemCount(series) - 1);
        boolean areaPolygonCanBeFilled = !isFirstItem && isLastItem;

        if (isFirstItem) {
            initializeRendererState(rendererState, plot, currentItemX, zeroRangePoint);
        }

        addPointToAreaPolygon(rendererState.getAreaPolygon(), plot, currentItemX, currentItemY);

        if (isDrawFilledArea() && areaPolygonCanBeFilled) {
            final Polygon polygon = rendererState.getAreaPolygon();
            addPointToAreaPolygon(polygon, plot, currentItemX, zeroRangePoint);

            final Paint itemPaint = getItemPaint(series, item);
            configureAreaFill(g2, plot, info, itemPaint);

            g2.fill(polygon);
        }

        if (getItemLineVisible(series, item) && !isFirstItem) {
            final Collection<Line2D> lines = rendererState.getLines();
            addItemLine(plot, lines, previousItemX, previousItemY, currentItemX, currentItemY);

            if (isLastItem) {
                drawSeriesLines(g2, lines, series, item);
            }
        }
    }

    private void configureAreaFill(Graphics2D g2, XYPlot plot, PlotRenderingInfo info, Paint itemPaint) {
        LineAreaFill areaFill = getLineAreaFill();

        if (areaFill instanceof SolidLineAreaFill) {
            configureSolidAreaFill(g2, itemPaint, (SolidLineAreaFill) areaFill);
        } else if (areaFill instanceof GradientLineAreaFill) {
            configureGradientAreaFill(g2, plot, info, itemPaint, (GradientLineAreaFill) areaFill);
        }
    }

    private void configureGradientAreaFill(Graphics2D g2, XYPlot plot, PlotRenderingInfo info, Paint itemPaint,
                                           GradientLineAreaFill gradientLineAreaFill) {
        final Rectangle2D plotArea = info.getPlotArea();
        double plotWidth = plotArea.getWidth();
        double plotHeight = plotArea.getHeight();
        Double mainColorTransparency = gradientLineAreaFill.getMaxValueTransparency();
        Double bgColorTransparency = gradientLineAreaFill.getMinValueTransparency();

        if (itemPaint instanceof Color) {
            Color itemColor = (Color) itemPaint;
            int red = itemColor.getRed();
            int green = itemColor.getGreen();
            int blue = itemColor.getBlue();
            int mainColorAlpha = (mainColorTransparency >= 0.0 && mainColorTransparency <= 1.0)
                    ? Math.round(255 * mainColorTransparency.floatValue())
                    : 150;
            int bgColorAlpha = (bgColorTransparency >= 0.0 && bgColorTransparency <= 1.0)
                    ? Math.round(255 * bgColorTransparency.floatValue())
                    : 128;

            Color mainColor = new Color(red, green, blue, mainColorAlpha);
            Paint bgColor = getBackgroundPaint();
            if (bgColor == null) {
                bgColor = plot.getBackgroundPaint();
            }
            Color secondaryColor = getSecondaryColor(bgColorAlpha, bgColor);

            Paint areaPaint = getAreaFillPaint(plot, plotWidth, plotHeight, mainColor, secondaryColor);
            g2.setPaint(areaPaint);
        } else {
            g2.setPaint(itemPaint);
        }
    }

    private Color getSecondaryColor(int bgColorAlpha, Paint bgColor) {
        return (bgColor != null && bgColor instanceof Color)
                ? new Color(((Color) bgColor).getRed(), ((Color) bgColor).getGreen(), ((Color) bgColor).getBlue(), bgColorAlpha)
                : new Color(0, 0, 0, bgColorAlpha);
    }

    private Paint getAreaFillPaint(XYPlot plot, Double plotWidth, Double plotHeight,
                                   Color mainColor, Color secondaryColor) {
        return (plot.getOrientation() == PlotOrientation.VERTICAL)
                ? new GradientPaint(0.0f, 0.0f, mainColor, 0.0f, plotHeight.floatValue(), secondaryColor, true)
                : new GradientPaint(plotWidth.floatValue(), 0.0f, mainColor, 0.0f, 0.0f, secondaryColor, true);
    }

    private void configureSolidAreaFill(Graphics2D g2, Paint itemPaint, SolidLineAreaFill solidLineAreaFill) {
        double transparency = solidLineAreaFill.getTransparency();

        if (itemPaint instanceof Color) {
            Color itemColor = (Color) itemPaint;
            int alpha = transparency >= 0.0 && transparency <= 1.0
                    ? Math.round(255 * (float) transparency)
                    : 255;
            g2.setPaint(new Color(itemColor.getRed(), itemColor.getGreen(), itemColor.getBlue(), alpha));
        } else {
            g2.setPaint(itemPaint);
        }
    }

    private void addPointToAreaPolygon(Polygon polygon,
                                       XYPlot plot,
                                       double currentItemX, double currentItemY) {
        if (plot.getOrientation() == PlotOrientation.VERTICAL) {
            polygon.addPoint((int) currentItemX, (int) currentItemY);
        } else {
            polygon.addPoint((int) currentItemY, (int) currentItemX);
        }
    }

    private void initializeRendererState(XYLineFillItemRendererState rendererState, XYPlot plot,
                                         double currentItemX,
                                         double zeroRangePoint) {
        rendererState.setLines(new ArrayList<Line2D>());
        final Polygon polygon = new Polygon();
        rendererState.setAreaPolygon(polygon);

        addPointToAreaPolygon(polygon, plot, currentItemX, zeroRangePoint);
    }

    public void completePass(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot,
                             CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataSet, int row, int pass) {
        // This renderer does not require any additional processing during complete pass phase
    }

    private void drawSeriesLines(Graphics2D g2, Collection<Line2D> lines, int series, int item) {
        g2.setPaint(getItemPaint(series, item));
        g2.setStroke(getItemStroke(series, item));

        for (Line2D line : lines) {
            g2.draw(line);
        }
    }

    private void addItemLine(XYPlot plot, Collection<Line2D> lines,
                             double previousItemX, double previousItemY,
                             double currentItemX, double currentItemY) {

        if (plot.getOrientation() == PlotOrientation.VERTICAL) {
            lines.add(new Line2D.Double(previousItemX, previousItemY, currentItemX, currentItemY));
        } else if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
            lines.add(new Line2D.Double(previousItemY, previousItemX, currentItemY, currentItemX));
        }
    }

    private Shape renderShapeAndLabel(Graphics2D g2, Rectangle2D dataArea, XYPlot plot, XYDataset dataSet,
                                      int series, int item, double yValue,
                                      double currentItemX, double currentItemY,
                                      double previousItemX, double previousItemY,
                                      double nextItemX, double nextItemY, double zeroRangePoint) {
        boolean isVerticallyOrientedPlot = plot.getOrientation() == PlotOrientation.VERTICAL;
        Shape entityShape;

        if (getItemShapeVisible(series, item)) {
            entityShape = getItemShape(series, item);
            if (isVerticallyOrientedPlot) {
                entityShape = ShapeUtilities.createTranslatedShape(entityShape, currentItemX, currentItemY);
            } else {
                entityShape = ShapeUtilities.createTranslatedShape(entityShape, currentItemY, currentItemX);
            }

            if (entityShape.intersects(dataArea)) {
                drawItemShape(g2, series, item, entityShape);
            }
        } else {
            entityShape = createEntityArea(plot, previousItemX, previousItemY, currentItemX, currentItemY,
                    nextItemX, nextItemY, zeroRangePoint);
        }

        if (isItemLabelVisible(series, item)) {
            drawLabel(g2, dataSet, series, item, currentItemX, currentItemY, yValue < 0, plot.getOrientation());
        }

        return entityShape;
    }

    private void drawItemShape(Graphics2D g2, int series, int item, Shape entityShape) {
        if (getItemShapeFilled(series, item)) {
            g2.setPaint(getUseFillPaint()
                    ? getItemFillPaint(series, item)
                    : getItemPaint(series, item));
            g2.fill(entityShape);
        }

        if (getDrawOutlines()) {
            g2.setPaint(getUseOutlinePaint()
                    ? getItemOutlinePaint(series, item)
                    : getItemPaint(series, item));
            g2.setStroke(getItemOutlineStroke(series, item));
        }

        g2.draw(entityShape);
    }

    private void drawLabel(Graphics2D g2, XYDataset dataSet, int series, int item,
                           double currentItemX, double currentItemY, boolean negative,
                           PlotOrientation orientation) {
        double labelX = (orientation == PlotOrientation.HORIZONTAL) ? currentItemY : currentItemX;
        double labelY = (orientation == PlotOrientation.HORIZONTAL) ? currentItemX : currentItemY;

        drawItemLabel(g2, orientation, dataSet, series, item, labelX, labelY, negative);
    }

    private Polygon createEntityArea(XYPlot plot,
                                     double previousItemX, double previousItemY,
                                     double currentItemX, double currentItemY,
                                     double nextItemX, double nextItemY,
                                     double zeroPoint) {
        Polygon entityAreaPolygon = new Polygon();
        final int halfwayPrevX = (int) ((previousItemX + currentItemX) / 2.0);
        final int halfwayPrevY = (int) ((previousItemY + currentItemY) / 2.0);
        final int halfwayNextY = (int) ((currentItemY + nextItemY) / 2.0);
        final int halfwayNextX = (int) ((currentItemX + nextItemX) / 2.0);
        if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
            entityAreaPolygon.addPoint((int) zeroPoint, halfwayPrevX);
            entityAreaPolygon.addPoint(halfwayPrevY, halfwayPrevX);
            entityAreaPolygon.addPoint((int) currentItemY, (int) currentItemX);
            entityAreaPolygon.addPoint(halfwayNextY, halfwayNextX);
            entityAreaPolygon.addPoint((int) zeroPoint, halfwayNextX);
        } else {
            entityAreaPolygon.addPoint(halfwayPrevX, (int) zeroPoint);
            entityAreaPolygon.addPoint(halfwayPrevX, halfwayPrevY);
            entityAreaPolygon.addPoint((int) currentItemX, (int) currentItemY);
            entityAreaPolygon.addPoint(halfwayNextX, halfwayNextY);
            entityAreaPolygon.addPoint(halfwayNextX, (int) zeroPoint);
        }

        return entityAreaPolygon;
    }

    private double calculateItemXPoint(int series, int item, Rectangle2D dataArea,
                                       ValueAxis domainAxis,
                                       XYDataset dataSet, XYPlot plot) {
        double xValue = dataSet.getXValue(series, item);
        return domainAxis.valueToJava2D(xValue, dataArea, plot.getDomainAxisEdge());
    }

    private double calculateItemYPoint(int series, int item, Rectangle2D dataArea,
                                       ValueAxis rangeAxis,
                                       XYDataset dataSet, XYPlot plot) {
        double yValue = dataSet.getYValue(series, item);
        return rangeAxis.valueToJava2D(yValue, dataArea, plot.getRangeAxisEdge());
    }

    private LegendItem createLegendItem(int dataSetIndex, int series, XYDataset dataSet) {

        Shape shape = lookupLegendShape(series);
        Paint fillPaint = (getUseFillPaint() ? lookupSeriesFillPaint(series) : lookupSeriesPaint(series));
        Paint outlinePaint = (getUseOutlinePaint() ? lookupSeriesOutlinePaint(series) : lookupSeriesPaint(series));
        Stroke outlineStroke = lookupSeriesOutlineStroke(series);
        Stroke lineStroke = lookupSeriesStroke(series);
        Paint linePaint = lookupSeriesPaint(series);
        Paint labelPaint = lookupLegendTextPaint(series);
        Font labelFont = lookupLegendTextFont(series);

        String itemLegendLabel = getLegendItemLabelGenerator().generateLabel(dataSet, series);
        final XYSeriesLabelGenerator toolTipGenerator = getLegendItemToolTipGenerator();
        String toolTipText = (toolTipGenerator != null) ? toolTipGenerator.generateLabel(dataSet, series) : null;
        final XYSeriesLabelGenerator urlGenerator = getLegendItemURLGenerator();
        String urlText = urlGenerator != null ? urlGenerator.generateLabel(dataSet, series) : null;

        boolean isItemShapeVisible = getItemShapeVisible(series, 0);
        boolean isItemShapeFilled = getItemShapeFilled(series, 0);
        boolean isItemShapeOutlineVisible = getDrawOutlines();
        boolean isItemLineVisible = getItemLineVisible(series, 0);

        LegendItem legendItem = new LegendItem(itemLegendLabel, itemLegendLabel, toolTipText,
                urlText, isItemShapeVisible, shape, isItemShapeFilled,
                fillPaint, isItemShapeOutlineVisible, outlinePaint,
                outlineStroke, isItemLineVisible, new Line2D.Double(-7.0, 0.0, 7.0, 0.0),
                lineStroke, linePaint);

        legendItem.setLabelFont(labelFont);
        legendItem.setLabelPaint(labelPaint);
        legendItem.setSeriesKey(dataSet.getSeriesKey(series));
        legendItem.setSeriesIndex(series);
        legendItem.setDataset(dataSet);
        legendItem.setDatasetIndex(dataSetIndex);

        return legendItem;
    }

    protected boolean isAreaAndLinePass(int pass) {
        return pass == 0;
    }

    protected boolean isShapesAndLabelsPass(int pass) {
        return pass == 1;
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public void setBackgroundPaint(Paint backgroundPaint) {
        this.backgroundPaint = backgroundPaint;
    }

    public LineAreaFill getLineAreaFill() {
        return lineAreaFill;
    }

    public void setLineAreaFill(LineAreaFill lineAreaFill) {
        this.lineAreaFill = lineAreaFill;
    }

    public boolean getUseFillPaint() {
        return useFillPaint;
    }

    public void setUseFillPaint(boolean flag) {
        this.useFillPaint = flag;
    }

    public boolean getDrawOutlines() {
        return drawOutlines;
    }

    public void setDrawOutlines(boolean show) {
        this.drawOutlines = show;
    }

    public boolean getUseOutlinePaint() {
        return useOutlinePaint;
    }

    public void setUseOutlinePaint(boolean flag) {
        this.useOutlinePaint = flag;
    }

    public boolean isDrawFilledArea() {
        return drawFilledArea;
    }

    public void setDrawFilledArea(boolean drawFilledArea) {
        this.drawFilledArea = drawFilledArea;
    }

    public Boolean getSeriesLinesVisible(int series) {
        return seriesLinesVisible.get(series);
    }

    public void setSeriesLinesVisible(int series, Boolean flag) {
        seriesLinesVisible.put(series, flag);
    }

    public void setSeriesLinesVisible(int series, boolean visible) {
        setSeriesLinesVisible(series, BooleanUtilities.valueOf(visible));
    }

    public Boolean getSeriesShapesVisible(int series) {
        return seriesShapesVisible.get(series);
    }

    public void setSeriesShapesVisible(int series, boolean visible) {
        setSeriesShapesVisible(series, BooleanUtilities.valueOf(visible));
    }

    public void setSeriesShapesVisible(int series, Boolean flag) {
        seriesShapesVisible.put(series, flag);
    }

    public Boolean getSeriesShapesFilled(int series) {
        return seriesShapesFilled.get(series);
    }

    public void setSeriesShapesFilled(int series, boolean filled) {
        seriesShapesFilled.put(series, filled);
    }

    public void setSeriesShapesFilled(int series, Boolean filled) {
        seriesShapesFilled.put(series, filled);
    }

    public boolean getBaseLinesVisible() {
        return baseLinesVisible;
    }

    public void setBaseLinesVisible(boolean flag) {
        this.baseLinesVisible = flag;
    }

    public boolean getBaseShapesVisible() {
        return baseShapesVisible;
    }

    public void setBaseShapesVisible(boolean flag) {
        this.baseShapesVisible = flag;
    }

    public boolean getBaseShapesFilled() {
        return baseShapesFilled;
    }

    public void setBaseShapesFilled(boolean flag) {
        this.baseShapesFilled = flag;
    }

    public boolean getItemShapeFilled(int series, int item) {
        final Boolean shapesFilled = getSeriesShapesFilled(series);
        return shapesFilled != null ? shapesFilled : getBaseShapesFilled();
    }

    public boolean getItemLineVisible(int series, int item) {
        final Boolean linesVisible = getSeriesLinesVisible(series);
        return linesVisible != null ? linesVisible : getBaseLinesVisible();
    }

    public boolean getItemShapeVisible(int series, int item) {
        final Boolean shapesVisible = getSeriesShapesVisible(series);
        return shapesVisible != null ? shapesVisible : getBaseShapesVisible();
    }

    @Override
    public Paint getItemOutlinePaint(int row, int column) {
        final Paint itemOutlinePaint = delegate.getItemOutlinePaint(row, column);
        if (itemOutlinePaint != null) {
            return itemOutlinePaint;
        }

        return super.getItemOutlinePaint(row, column);
    }

    @Override
    public Stroke getItemOutlineStroke(int row, int column) {
        final Stroke outlineStroke = delegate.getItemOutlineStroke(row, column);
        if (outlineStroke != null) {
            return outlineStroke;
        }

        return super.getItemOutlineStroke(row, column);
    }

    @Override
    public Paint getItemPaint(int row, int column) {
        final Paint itemPaint = delegate.getItemPaint(row, column);
        if (itemPaint != null) {
            return itemPaint;
        }

        return super.getItemPaint(row, column);
    }

    public void setItemOutlinePaint(int row, int column, Paint paint) {
        delegate.setItemOutlinePaint(row, column, paint);
    }

    public void setItemOutlineStroke(int row, int column, Stroke stroke) {
        delegate.setItemOutlineStroke(row, column, stroke);
    }

    public void setItemPaint(int row, int column, Paint paint) {
        delegate.setItemPaint(row, column, paint);
    }

    public void addConfigurator(RendererConfigurator configurator) {
        configurationDelegate.addConfigurator(configurator);
    }

    public Collection<RendererConfigurator> getConfigurators() {
        return configurationDelegate.getConfigurators();
    }

    public void configure(ChartView chartView) {
        configurationDelegate.configure(chartView, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        XYLineFillRenderer that = (XYLineFillRenderer) o;

        if (baseLinesVisible != that.baseLinesVisible) return false;
        if (baseShapesFilled != that.baseShapesFilled) return false;
        if (baseShapesVisible != that.baseShapesVisible) return false;
        if (drawFilledArea != that.drawFilledArea) return false;
        if (drawOutlines != that.drawOutlines) return false;
        if (useFillPaint != that.useFillPaint) return false;
        if (useOutlinePaint != that.useOutlinePaint) return false;
        if (backgroundPaint != null ? !backgroundPaint.equals(that.backgroundPaint) : that.backgroundPaint != null)
            return false;
        if (delegate != null ? !delegate.equals(that.delegate) : that.delegate != null) return false;
        if (lineAreaFill != null ? !lineAreaFill.equals(that.lineAreaFill) : that.lineAreaFill != null) return false;
        if (seriesLinesVisible != null ? !seriesLinesVisible.equals(that.seriesLinesVisible) : that.seriesLinesVisible != null)
            return false;
        if (seriesShapesFilled != null ? !seriesShapesFilled.equals(that.seriesShapesFilled) : that.seriesShapesFilled != null)
            return false;
        if (seriesShapesVisible != null ? !seriesShapesVisible.equals(that.seriesShapesVisible) : that.seriesShapesVisible != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (delegate != null ? delegate.hashCode() : 0);
        result = 31 * result + (seriesShapesVisible != null ? seriesShapesVisible.hashCode() : 0);
        result = 31 * result + (seriesLinesVisible != null ? seriesLinesVisible.hashCode() : 0);
        result = 31 * result + (seriesShapesFilled != null ? seriesShapesFilled.hashCode() : 0);
        result = 31 * result + (baseShapesVisible ? 1 : 0);
        result = 31 * result + (baseLinesVisible ? 1 : 0);
        result = 31 * result + (baseShapesFilled ? 1 : 0);
        result = 31 * result + (useFillPaint ? 1 : 0);
        result = 31 * result + (useOutlinePaint ? 1 : 0);
        result = 31 * result + (drawFilledArea ? 1 : 0);
        result = 31 * result + (drawOutlines ? 1 : 0);
        result = 31 * result + (backgroundPaint != null ? backgroundPaint.hashCode() : 0);
        result = 31 * result + (lineAreaFill != null ? lineAreaFill.hashCode() : 0);

        return result;
    }

}

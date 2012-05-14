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

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ShapeUtilities;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.GradientLineAreaFill;
import org.openfaces.component.chart.LineAreaFill;
import org.openfaces.component.chart.SolidLineAreaFill;
import org.openfaces.component.chart.impl.configuration.ConfigurableRenderer;
import org.openfaces.component.chart.impl.configuration.RendererConfigurator;
import org.openfaces.component.chart.impl.helpers.CategoryAxisAdapter;
import org.openfaces.component.chart.impl.renderers.states.LineFillItemRendererState;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

public class LineFillRenderer extends LineAndShapeRenderer implements AreaFillRenderer, CustomizedRenderer, ConfigurableRenderer {
    private ItemsRenderer delegate;
    private ConfigurableRendererBase configurationDelegate;

    private boolean drawFilledArea;
    private LineAreaFill lineAreaFill;
    private Paint backgroundPaint;

    public LineFillRenderer() {
        super(true, true);

        drawFilledArea = true;
        delegate = new ItemsRenderer();
        configurationDelegate = new ConfigurableRendererBase();
    }

    protected CategoryItemRendererState createState(PlotRenderingInfo info) {
        return new LineFillItemRendererState(info);
    }

    public int getPassCount() {
        return 2;
    }

    public void drawItem(Graphics2D g2, CategoryItemRendererState state,
                         Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis,
                         ValueAxis rangeAxis, CategoryDataset dataSet, int row, int column,
                         int pass) {
        if (!getItemVisible(row, column)) {
            return;
        }

        Number value = dataSet.getValue(row, column);
        int visibleRow = state.getVisibleSeriesIndex(row);
        if ((value == null || visibleRow < 0)) {
            return;
        }

        LineFillItemRendererState rendererState = (LineFillItemRendererState) state;
        double currentValue = value.doubleValue();
        double currentItemXPoint = calculateItemXPoint(plot, dataSet, domainAxis, dataArea, column, visibleRow,
                state.getVisibleSeriesCount());
        double currentItemYPoint = calculateItemYPoint(plot, rangeAxis, dataArea, currentValue);

        if (isAreaAndLinePass(pass)) {
            processAreaAndLine(dataArea, plot, domainAxis, rangeAxis, dataSet, rendererState, row, column,
                    currentValue, currentItemXPoint, currentItemYPoint, visibleRow);
        } else if (isShapesAndLabelsPass(pass)) {
            Shape entityArea = renderItemShapeAndLabel(g2, dataSet, row, column, plot.getOrientation(),
                    currentValue, currentItemXPoint, currentItemYPoint);

            int dataSetIndex = plot.indexOf(dataSet);
            updateCrosshairValues(state.getCrosshairState(),
                    dataSet.getRowKey(row), dataSet.getColumnKey(column),
                    currentValue, dataSetIndex, currentItemXPoint, currentItemYPoint, plot.getOrientation());

            EntityCollection entities = state.getEntityCollection();
            if (entities != null) {
                addItemEntity(entities, dataSet, row, column, entityArea);
            }
        }

    }

    public void completePass(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea,
                             CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis,
                             CategoryDataset dataSet, int row, int pass) {
        LineFillItemRendererState rendererState = (LineFillItemRendererState) state;
        if (isAreaAndLinePass(pass)) {
            if (isDrawFilledArea()) {
                drawAreaPolygonFill(g2, plot, row, rendererState);
            }

            drawPrimaryLine(g2, rendererState.getLines(), row, 0);
        }
    }

    private void processAreaAndLine(Rectangle2D dataArea, CategoryPlot plot,
                                    CategoryAxis domainAxis, ValueAxis rangeAxis,
                                    CategoryDataset dataSet,
                                    LineFillItemRendererState rendererState,
                                    int row, int column,
                                    double currentValue, double currentItemXPoint, double currentItemYPoint,
                                    int visibleRow) {
        int totalColumns = dataSet.getColumnCount();
        int lastColumnIndex = totalColumns - 1;
        boolean isFirstItem = column == 0;
        boolean isLastItem = (column == lastColumnIndex);

        if (isFirstItem) {
            initializeRendererState(rendererState);
        }

        int previousItemIndex = Math.max(column - 1, 0);
        Number previousItemValue = dataSet.getValue(row, previousItemIndex);
        int visibleSeriesCount = rendererState.getVisibleSeriesCount();
        double previousItemXPoint = (previousItemValue != null)
                ? calculateItemXPoint(plot, dataSet, domainAxis, dataArea, previousItemIndex, visibleRow, visibleSeriesCount)
                : currentItemXPoint;
        double previousItemYPoint = (previousItemValue != null)
                ? calculateItemYPoint(plot, rangeAxis, dataArea, previousItemValue.doubleValue())
                : currentItemYPoint;


        processCategoryArea(dataArea, plot, domainAxis, rangeAxis, dataSet, rendererState, row, column,
                currentValue, currentItemYPoint, totalColumns, isFirstItem, isLastItem);

        if (getItemLineVisible(row, column) && !isFirstItem) {
            addItemLine(plot, rendererState.getLines(), previousItemXPoint, previousItemYPoint, currentItemXPoint, currentItemYPoint);
        }
    }

    private void processCategoryArea(Rectangle2D dataArea, CategoryPlot plot,
                                     CategoryAxis domainAxis, ValueAxis rangeAxis,
                                     CategoryDataset dataSet, LineFillItemRendererState rendererState,
                                     int row, int column, double currentValue, double currentItemYPoint,
                                     int totalColumns, boolean isFirstItem, boolean isLastItem) {
        RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
        RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();
        double zeroRangePoint = calculateItemYPoint(plot, rangeAxis, dataArea, 0.0);
        double categoryStartXPoint = domainAxis.getCategoryStart(column, totalColumns, dataArea, domainAxisEdge);
        double categoryMiddleXPoint = domainAxis.getCategoryMiddle(column, totalColumns, dataArea, domainAxisEdge);
        double categoryEndXPoint = domainAxis.getCategoryEnd(column, totalColumns, dataArea, domainAxisEdge);

        if (isFirstItem) {
            categoryStartXPoint = categoryMiddleXPoint;
        } else if (isLastItem) {
            categoryEndXPoint = categoryMiddleXPoint;
        }

        double previousItemYValue = 0.0;
        if (!isFirstItem) {
            Number previousItemValue = dataSet.getValue(row, column - 1);

            if (previousItemValue != null) {
                previousItemYValue = (previousItemValue.doubleValue() + currentValue) / 2.0;

                if (domainAxis instanceof CategoryAxisAdapter) {
                    categoryStartXPoint -= ((CategoryAxisAdapter) domainAxis).
                            calculateCategoryGapSize(totalColumns, dataArea, domainAxisEdge) / 2.0;
                }
            } else {
                categoryStartXPoint = categoryMiddleXPoint;
            }
        }

        double nextItemYValue = 0.0;
        if (!isLastItem) {
            Number nextValue = dataSet.getValue(row, column + 1);
            if (nextValue != null) {
                nextItemYValue = (nextValue.doubleValue() + currentValue) / 2.0;

                if (domainAxis instanceof CategoryAxisAdapter) {
                    categoryEndXPoint += ((CategoryAxisAdapter) domainAxis).
                            calculateCategoryGapSize(totalColumns, dataArea, domainAxisEdge) / 2.0;
                }
            } else {
                categoryEndXPoint = categoryMiddleXPoint;
            }
        }

        double previousItemYPoint = rangeAxis.valueToJava2D(previousItemYValue, dataArea, rangeAxisEdge);
        double nextItemYPoint = rangeAxis.valueToJava2D(nextItemYValue, dataArea, rangeAxisEdge);

        Polygon polygon = rendererState.getAreaPolygon();
        if (plot.getOrientation() == PlotOrientation.VERTICAL) {
            polygon.addPoint((int) categoryStartXPoint, (int) zeroRangePoint);
            polygon.addPoint((int) categoryStartXPoint, (int) previousItemYPoint);
            polygon.addPoint((int) categoryMiddleXPoint, (int) currentItemYPoint);
            polygon.addPoint((int) categoryEndXPoint, (int) nextItemYPoint);
            polygon.addPoint((int) categoryEndXPoint, (int) zeroRangePoint);
        } else {
            polygon.addPoint((int) zeroRangePoint, (int) categoryStartXPoint);
            polygon.addPoint((int) previousItemYPoint, (int) categoryStartXPoint);
            polygon.addPoint((int) currentItemYPoint, (int) categoryMiddleXPoint);
            polygon.addPoint((int) nextItemYPoint, (int) categoryEndXPoint);
            polygon.addPoint((int) zeroRangePoint, (int) categoryEndXPoint);
        }
    }

    private void initializeRendererState(LineFillItemRendererState rendererState) {
        rendererState.setLines(new ArrayList<Line2D>());
        rendererState.setAreaPolygon(new Polygon());
    }

    private void drawAreaPolygonFill(Graphics2D g2, CategoryPlot plot, int row, LineFillItemRendererState rendererState) {


        final Paint itemPaint = getItemPaint(row, 0);
        final LineAreaFill areaFill = getLineAreaFill();

        if (areaFill instanceof SolidLineAreaFill) {
            configureSolidAreaFill(g2, itemPaint, (SolidLineAreaFill) areaFill);
        } else if (areaFill instanceof GradientLineAreaFill) {
            configureGradientAreaFill(g2, plot, itemPaint, rendererState.getInfo(), (GradientLineAreaFill) areaFill);
        }

        g2.fill(rendererState.getAreaPolygon());
    }

    private void configureGradientAreaFill(Graphics2D g2, CategoryPlot plot, Paint itemPaint,
                                           PlotRenderingInfo info, GradientLineAreaFill gradientLineAreaFill) {
        double plotWidth = info.getPlotArea().getWidth();
        double plotHeight = info.getPlotArea().getHeight();
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

    private Paint getAreaFillPaint(CategoryPlot plot, Double plotWidth, Double plotHeight,
                                   Color mainColor, Color secondaryColor) {
        return (plot.getOrientation() == PlotOrientation.VERTICAL)
                ? new GradientPaint(0.0f, 0.0f, mainColor, 0.0f, plotHeight.floatValue(), secondaryColor, true)
                : new GradientPaint(plotWidth.floatValue(), 0.0f, mainColor, 0.0f, 0.0f, secondaryColor, true);
    }

    private void configureSolidAreaFill(Graphics2D g2, Paint itemPaint, SolidLineAreaFill solidLineAreaFill) {
        final Double transparency = solidLineAreaFill.getTransparency();

        if (itemPaint instanceof Color && transparency >= 0.0 && transparency <= 1.0) {
            Color itemColor = (Color) itemPaint;
            int alpha = transparency >= 0.0 && transparency <= 1.0
                    ? Math.round(255 * transparency.floatValue())
                    : 255;
            g2.setPaint(new Color(itemColor.getRed(), itemColor.getGreen(), itemColor.getBlue(), alpha));
        } else {
            g2.setPaint(itemPaint);
        }
    }

    private void drawPrimaryLine(Graphics2D g2, Collection<Line2D> lines, int row, int item) {
        g2.setPaint(getItemPaint(row, item));
        g2.setStroke(getItemStroke(row, item));

        for (Line2D line : lines) {
            g2.draw(line);
        }
    }

    private void addItemLine(CategoryPlot plot, Collection<Line2D> lines,
                             double previousItemX, double previousItemY,
                             double currentItemX, double currentItemY) {

        if (plot.getOrientation() == PlotOrientation.VERTICAL) {
            lines.add(new Line2D.Double(previousItemX, previousItemY, currentItemX, currentItemY));
        } else if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
            lines.add(new Line2D.Double(previousItemY, previousItemX, currentItemY, currentItemX));
        }
    }

    private Shape renderItemShapeAndLabel(Graphics2D g2, CategoryDataset dataSet,
                                          int row, int column,
                                          PlotOrientation orientation,
                                          double currentValue,
                                          double currentItemX, double currentItemY) {
        Shape shape = getTranslatedItemShape(row, column, orientation, currentItemX, currentItemY);

        if (getItemShapeVisible(row, column)) {
            drawItemShape(g2, row, column, shape);
        }

        if (isItemLabelVisible(row, column)) {
            if (orientation == PlotOrientation.VERTICAL) {
                drawItemLabel(g2, orientation, dataSet, row, column, currentItemX, currentItemY, (currentValue < 0.0));
            } else {
                drawItemLabel(g2, orientation, dataSet, row, column, currentItemY, currentItemX, (currentValue < 0.0));
            }
        }

        return shape;
    }

    private void drawItemShape(Graphics2D g2, int row, int column, Shape shape) {
        if (getItemShapeFilled(row, column)) {
            g2.setPaint(getUseFillPaint()
                    ? getItemFillPaint(row, column)
                    : getItemPaint(row, column));

            g2.fill(shape);
        }

        if (getDrawOutlines()) {
            g2.setPaint(getUseOutlinePaint()
                    ? getItemOutlinePaint(row, column)
                    : getItemPaint(row, column));

            g2.setStroke(getItemOutlineStroke(row, column));

            g2.draw(shape);
        }
    }

    private Shape getTranslatedItemShape(int row, int column, PlotOrientation orientation,
                                         double currentItemX, double currentItemY) {
        Shape shape = getItemShape(row, column);
        if (orientation == PlotOrientation.HORIZONTAL) {
            shape = ShapeUtilities.createTranslatedShape(shape, currentItemY, currentItemX);
        } else {
            shape = ShapeUtilities.createTranslatedShape(shape, currentItemX, currentItemY);
        }
        return shape;
    }

    private double calculateItemXPoint(CategoryPlot plot, CategoryDataset dataSet, CategoryAxis domainAxis,
                                       Rectangle2D dataArea, int column, int visibleRow, int visibleRowCount) {
        double itemXPoint;
        if (getUseSeriesOffset()) {
            itemXPoint = domainAxis.getCategorySeriesMiddle(column,
                    dataSet.getColumnCount(), visibleRow, visibleRowCount,
                    getItemMargin(), dataArea, plot.getDomainAxisEdge());
        } else {
            itemXPoint = domainAxis.getCategoryMiddle(column, getColumnCount(),
                    dataArea, plot.getDomainAxisEdge());
        }

        return itemXPoint;
    }

    private double calculateItemYPoint(CategoryPlot plot, ValueAxis rangeAxis,
                                       Rectangle2D dataArea, double value) {
        return rangeAxis.valueToJava2D(value, dataArea, plot.getRangeAxisEdge());
    }

    protected boolean isAreaAndLinePass(int pass) {
        return pass == 0;
    }

    protected boolean isShapesAndLabelsPass(int pass) {
        return pass == 1;
    }

    public LineAreaFill getLineAreaFill() {
        return lineAreaFill;
    }

    public void setLineAreaFill(LineAreaFill lineAreaFill) {
        this.lineAreaFill = lineAreaFill;
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public void setBackgroundPaint(Paint backgroundPaint) {
        this.backgroundPaint = backgroundPaint;
    }

    public boolean isDrawFilledArea() {
        return drawFilledArea;
    }

    public void setDrawFilledArea(boolean drawFilledArea) {
        this.drawFilledArea = drawFilledArea;
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

        LineFillRenderer that = (LineFillRenderer) o;

        if (drawFilledArea != that.drawFilledArea) return false;
        if (backgroundPaint != null ? !backgroundPaint.equals(that.backgroundPaint) : that.backgroundPaint != null)
            return false;
        if (delegate != null ? !delegate.equals(that.delegate) : that.delegate != null) return false;
        if (lineAreaFill != null ? !lineAreaFill.equals(that.lineAreaFill) : that.lineAreaFill != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (delegate != null ? delegate.hashCode() : 0);
        result = 31 * result + (backgroundPaint != null ? backgroundPaint.hashCode() : 0);
        result = 31 * result + (lineAreaFill != null ? lineAreaFill.hashCode() : 0);
        result = 31 * result + (drawFilledArea ? 1 : 0);
        return result;
    }
}

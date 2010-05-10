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

package org.openfaces.component.chart.impl.renderers;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.AreaRendererEndType;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ShapeUtilities;
import org.openfaces.component.chart.GradientLineAreaFill;
import org.openfaces.component.chart.LineAreaFill;
import org.openfaces.component.chart.LineChartView;
import org.openfaces.component.chart.SolidLineAreaFill;
import org.openfaces.component.chart.impl.helpers.CategoryAxisAdapter;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Eugene Goncharov
 */
public class LineAreaFillRenderer extends LineAndShapeRenderer implements AreaFillRenderer {

    /**
     * A state object used by this renderer.
     */
    static class LineAreaFillRendererState extends CategoryItemRendererState {

        /**
         * Working storage for the area under one series.
         */
        public Polygon area;

        /**
         * Working line that can be recycled.
         */
        public Line2D line;

        public Collection<Line2D.Double> lines;

        /**
         * Creates a new state.
         *
         * @param info the plot rendering info.
         */
        public LineAreaFillRendererState(PlotRenderingInfo info) {
            super(info);
            this.area = new Polygon();
            this.line = new Line2D.Double();
            this.lines = new ArrayList<Line2D.Double>();
        }

    }

    private Paint backgroundPaint;

    private LineAreaFill lineAreaFill;

    /**
     * A flag indicating whether or not Area are drawn at each XY point.
     */
    private boolean drawFilledArea;

    /**
     * A flag that controls how the ends of the areas are drawn.
     */
    private AreaRendererEndType endType;

    public LineAreaFillRenderer(LineChartView chartView, CategoryDataset dataSet) {
        super(true, true);
        this.drawFilledArea = true;
        this.endType = AreaRendererEndType.TAPER;

        ChartRendering.setupSeriesColors(chartView, this);
        ChartRendering.processLineAndShapeRendererProperties(this, dataSet, chartView);
    }

    /**
     * Creates a new state instance.  This method is called from
     * {@link #initialise(Graphics2D, Rectangle2D, CategoryPlot, int,
     * PlotRenderingInfo)}, and we override it to ensure that the state
     * contains a working Line2D instance.
     *
     * @param info the plot rendering info (<code>null</code> is permitted).
     * @return A new state instance.
     */
    protected CategoryItemRendererState createState(PlotRenderingInfo info) {
        return new LineAreaFillRendererState(info);
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public void setBackgroundPaint(Paint paint) {
        this.backgroundPaint = paint;
    }

    public LineAreaFill getLineAreaFill() {
        return lineAreaFill;
    }

    public void setLineAreaFill(LineAreaFill lineAreaFill) {
        this.lineAreaFill = lineAreaFill;
    }

    /**
     * Returns true if Area is being plotted by the renderer.
     *
     * @return <code>true</code> if Area is being plotted by the renderer.
     */
    public boolean isDrawFilledArea() {
        return this.drawFilledArea;
    }

    public void setDrawFilledArea(boolean drawFilledArea) {
        this.drawFilledArea = drawFilledArea;
    }

    /**
     * Returns a token that controls how the renderer draws the end points.
     * The default value is {@link AreaRendererEndType#TAPER}.
     *
     * @return The end type (never <code>null</code>).
     * @see #setEndType
     */
    public AreaRendererEndType getEndType() {
        return this.endType;
    }

    /**
     * Sets a token that controls how the renderer draws the end points, and
     * sends a {@link org.jfree.chart.event.RendererChangeEvent} to all registered listeners.
     *
     * @param type the end type (<code>null</code> not permitted).
     * @see #getEndType()
     */
    public void setEndType(AreaRendererEndType type) {
        if (type == null) {
            throw new IllegalArgumentException("Null 'type' argument.");
        }
        this.endType = type;
        fireChangeEvent();
    }

    /**
     * Returns the number of passes through the data that the renderer requires
     * in order to draw the chart.  Most charts will require a single pass, but
     * some require two passes.
     *
     * @return The pass count.
     */
    public int getPassCount() {
        return 2;
    }

    /**
     * Draw a single data item.
     *
     * @param g2         the graphics device.
     * @param state      the renderer state.
     * @param dataArea   the area in which the data is drawn.
     * @param plot       the plot.
     * @param domainAxis the domain axis.
     * @param rangeAxis  the range axis.
     * @param dataSet    the dataSet.
     * @param row        the row index (zero-based).
     * @param column     the column index (zero-based).
     * @param pass       the pass index.
     */
    public void drawItem(Graphics2D g2, CategoryItemRendererState state,
                         Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis,
                         ValueAxis rangeAxis, CategoryDataset dataSet, int row, int column,
                         int pass) {
        // do nothing if item is not visible
        if (!getItemVisible(row, column)) {
            return;
        }

        // nothing is drawn for null...
        Number value = dataSet.getValue(row, column);
        int visibleRow = state.getVisibleSeriesIndex(row);
        if ((value == null || visibleRow < 0)) {
            // Current row is not visible if result index is -1
            return;
        }

        LineAreaFillRendererState rendererState = (LineAreaFillRendererState) state;
        final int columnCount = dataSet.getColumnCount();
        final int lastColumnIndex = columnCount - 1;
        final boolean isFirstItem = column == 0;
        final boolean isLastItem = (column == lastColumnIndex);

        final PlotOrientation orientation = plot.getOrientation();
        final boolean isVerticallyOrientedPlot = orientation == PlotOrientation.VERTICAL;
        final boolean isHorizontallyOrientedPlot = orientation == PlotOrientation.HORIZONTAL;
        final RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
        final RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();
        final int visibleRowCount = state.getVisibleSeriesCount();

        final double currentValue = value.doubleValue();
        final int previousItemIndex = Math.max(column - 1, 0);
        final Number previousItemValue = dataSet.getValue(row, previousItemIndex);

        // the first point is (x, 0)
        final double zeroRangePoint = calculateItemYPoint(plot, rangeAxis, dataArea, 0.0);
        double currentItemX = calculateItemXPoint(plot, dataSet, domainAxis, dataArea, column, visibleRow, visibleRowCount);
        double currentItemY = calculateItemYPoint(plot, rangeAxis, dataArea, currentValue);
        double previousItemX = 0;
        double previousItemY = 0;
        if (previousItemValue != null) {
            previousItemX = calculateItemXPoint(plot, dataSet, domainAxis, dataArea, previousItemIndex, visibleRow, visibleRowCount);
            previousItemY = calculateItemYPoint(plot, rangeAxis, dataArea, previousItemValue.doubleValue());
        } else {
            previousItemX = currentItemX;
            previousItemY = currentItemY;
        }

        if (isAreaAndLinePass(pass)) {
            // Start with area stuff
            if (isFirstItem) {
                rendererState.lines = new ArrayList<Line2D.Double>();
                // create a new area polygon for the series
                rendererState.area = new Polygon();
            }

            double categoryStartX = domainAxis.getCategoryStart(column, columnCount, dataArea, domainAxisEdge);
            double categoryMiddleX = domainAxis.getCategoryMiddle(column, columnCount, dataArea, domainAxisEdge);
            double categoryEndX = domainAxis.getCategoryEnd(column, columnCount, dataArea, domainAxisEdge);

            if (isFirstItem) {
                categoryStartX = categoryMiddleX;
            } else if (isLastItem) {
                categoryEndX = categoryMiddleX;
            }

            double yy0 = 0.0;
            if (column > 0) {
                Number prevValue = dataSet.getValue(row, column - 1);
                if (prevValue != null) {
                    yy0 = (prevValue.doubleValue() + currentValue) / 2.0;

                    if (domainAxis instanceof CategoryAxisAdapter) {
                        categoryStartX -= ((CategoryAxisAdapter) domainAxis).calculateCategoryGapSize(columnCount, dataArea, domainAxisEdge) / 2.0;
                    }
                } else {
                    categoryStartX = categoryMiddleX;
                }
            }

            double yy2 = 0.0;
            if (column < dataSet.getColumnCount() - 1) {
                Number nextValue = dataSet.getValue(row, column + 1);
                if (nextValue != null) {
                    yy2 = (nextValue.doubleValue() + currentValue) / 2.0;

                    if (domainAxis instanceof CategoryAxisAdapter) {
                        categoryEndX += ((CategoryAxisAdapter) domainAxis).calculateCategoryGapSize(columnCount, dataArea, domainAxisEdge) / 2.0;
                    }
                } else {
                    categoryEndX = categoryMiddleX;
                }
            }

            float prevY = (float) rangeAxis.valueToJava2D(yy0, dataArea, rangeAxisEdge);
            float nextY = (float) rangeAxis.valueToJava2D(yy2, dataArea, rangeAxisEdge);

            // Add each point to Area (x, y)
            if (isVerticallyOrientedPlot) {
                rendererState.area.addPoint((int) categoryStartX, (int) zeroRangePoint);
                rendererState.area.addPoint((int) categoryStartX, (int) prevY);
                rendererState.area.addPoint((int) categoryMiddleX, (int) currentItemY);
                rendererState.area.addPoint((int) categoryEndX, (int) nextY);
                rendererState.area.addPoint((int) categoryEndX, (int) zeroRangePoint);
            } else if (isHorizontallyOrientedPlot) {
                rendererState.area.addPoint((int) zeroRangePoint, (int) categoryStartX);
                rendererState.area.addPoint((int) prevY, (int) categoryStartX);
                rendererState.area.addPoint((int) currentItemY, (int) categoryMiddleX);
                rendererState.area.addPoint((int) nextY, (int) categoryEndX);
                rendererState.area.addPoint((int) zeroRangePoint, (int) categoryEndX);
            }
            // Area Drawing End

            // And then line stuff
            if (getItemLineVisible(row, column) && !isFirstItem) {
                addItemLine(plot, rendererState, previousItemX, previousItemY, currentItemX, currentItemY);
            }
        } else if (isShapesAndLabelsPass(pass)) {
            Shape shape = getItemShape(row, column);
            if (orientation == PlotOrientation.HORIZONTAL) {
                shape = ShapeUtilities.createTranslatedShape(shape, currentItemY, currentItemX);
            } else if (orientation == PlotOrientation.VERTICAL) {
                shape = ShapeUtilities.createTranslatedShape(shape, currentItemX, currentItemY);
            }

            if (getItemShapeVisible(row, column)) {
                if (getItemShapeFilled(row, column)) {
                    if (getUseFillPaint()) {
                        g2.setPaint(getItemFillPaint(row, column));
                    } else {
                        g2.setPaint(getItemPaint(row, column));
                    }
                    g2.fill(shape);
                }
                if (getDrawOutlines()) {
                    if (getUseOutlinePaint()) {
                        g2.setPaint(getItemOutlinePaint(row, column));
                    } else {
                        g2.setPaint(getItemPaint(row, column));
                    }
                    g2.setStroke(getItemOutlineStroke(row, column));
                    g2.draw(shape);
                }
            }

            // draw the item label if there is one...
            if (isItemLabelVisible(row, column)) {
                if (orientation == PlotOrientation.HORIZONTAL) {
                    drawItemLabel(g2, orientation, dataSet, row, column, currentItemY,
                            currentItemX, (currentValue < 0.0));
                } else if (orientation == PlotOrientation.VERTICAL) {
                    drawItemLabel(g2, orientation, dataSet, row, column, currentItemX,
                            currentItemY, (currentValue < 0.0));
                }
            }

            // submit the current data point as a crosshair candidate
            int dataSetIndex = plot.indexOf(dataSet);
            updateCrosshairValues(state.getCrosshairState(),
                    dataSet.getRowKey(row), dataSet.getColumnKey(column),
                    currentValue, dataSetIndex, currentItemX, currentItemY, orientation);

            // add an item entity, if this information is being collected
            EntityCollection entities = state.getEntityCollection();
            if (entities != null) {
                addItemEntity(entities, dataSet, row, column, shape);
            }
        }

    }

    public void completePass(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea,
                             CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis,
                             CategoryDataset dataSet, int row, int pass) {
        LineAreaFillRendererState rendererState = (LineAreaFillRendererState) state;
        if (isAreaAndLinePass(pass)) {
            if (isDrawFilledArea()) {
                final double plotWidth = rendererState.getInfo().getPlotArea().getWidth();
                final double plotHeight = rendererState.getInfo().getPlotArea().getHeight();

                final Paint itemPaint = getItemPaint(row, 0);
                final LineAreaFill areaFill = getLineAreaFill();

                if (areaFill instanceof SolidLineAreaFill) {
                    SolidLineAreaFill solidLineAreaFill = (SolidLineAreaFill) areaFill;
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
                } else if (areaFill instanceof GradientLineAreaFill) {
                    GradientLineAreaFill gradientLineAreaFill = (GradientLineAreaFill) areaFill;
                    final Double mainColorTransparency = gradientLineAreaFill.getMaxValueTransparency();
                    final Double bgColorTransparency = gradientLineAreaFill.getMinValueTransparency();

                    if (itemPaint instanceof Color) {
                        final Color itemColor = (Color) itemPaint;
                        final int red = itemColor.getRed();
                        final int green = itemColor.getGreen();
                        final int blue = itemColor.getBlue();
                        int mainColorAlpha = (mainColorTransparency >= 0.0 && mainColorTransparency <= 1.0)
                                ? Math.round(255 * mainColorTransparency.floatValue())
                                : 150;
                        int bgColorAlpha = (bgColorTransparency >= 0.0 && bgColorTransparency <= 1.0)
                                ? Math.round(255 * bgColorTransparency.floatValue())
                                : 128;

                        final Color mainColor = new Color(red, green, blue, mainColorAlpha);
                        final Paint bgColor = getBackgroundPaint();

                        final Color secondaryColor = (bgColor != null && bgColor instanceof Color)
                                ? new Color(((Color) bgColor).getRed(), ((Color) bgColor).getGreen(), ((Color) bgColor).getBlue(), bgColorAlpha)
                                : new Color(0, 0, 0, bgColorAlpha);

                        Paint areaPaint = (plot.getOrientation() == PlotOrientation.VERTICAL)
                                ? new GradientPaint(0.0f, 0.0f, mainColor, 0.0f, new Double(plotHeight).floatValue(), secondaryColor, true)
                                : new GradientPaint(new Double(plotWidth).floatValue(), 0.0f, mainColor, 0.0f, 0.0f, secondaryColor, true);

                        g2.setPaint(areaPaint);
                    } else {
                        g2.setPaint(itemPaint);
                    }
                }

                g2.fill(rendererState.area);
            }

            drawPrimaryLine(g2, rendererState, row, 0);
        }
    }

    private void drawPrimaryLine(Graphics2D g2, LineAreaFillRendererState rendererState, int row, int item) {
        g2.setPaint(getItemPaint(row, item));
        g2.setStroke(getItemStroke(row, item));

        for (Line2D line : rendererState.lines) {
            g2.draw(line);
        }
    }

    private void addItemLine(CategoryPlot plot, LineAreaFillRendererState rendererState,
                             double previousItemX, double previousItemY,
                             double currentItemX, double currentItemY) {

        if (plot.getOrientation() == PlotOrientation.VERTICAL) {
            rendererState.lines.add(new Line2D.Double(previousItemX, previousItemY, currentItemX, currentItemY));
        } else if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
            rendererState.lines.add(new Line2D.Double(previousItemY, previousItemX, currentItemY, currentItemX));
        }
    }


    private double calculateItemXPoint(CategoryPlot plot, CategoryDataset dataSet, CategoryAxis domainAxis,
                                       Rectangle2D dataArea, int column, int visibleRow, int visibleRowCount) {
        double xValue;
        if (getUseSeriesOffset()) {
            xValue = domainAxis.getCategorySeriesMiddle(column,
                    dataSet.getColumnCount(), visibleRow, visibleRowCount,
                    getItemMargin(), dataArea, plot.getDomainAxisEdge());
        } else {
            xValue = domainAxis.getCategoryMiddle(column, getColumnCount(),
                    dataArea, plot.getDomainAxisEdge());
        }

        return xValue;
    }


    private double calculateItemYPoint(CategoryPlot plot, ValueAxis rangeAxis,
                                       Rectangle2D dataArea, double value) {
        return rangeAxis.valueToJava2D(value, dataArea, plot.getRangeAxisEdge());
    }

    /**
     * Returns <code>true</code> if the specified pass is the one for drawing
     * lines.
     *
     * @param pass the pass.
     * @return A boolean.
     */
    protected boolean isAreaAndLinePass(int pass) {
        return pass == 0;
    }

    /**
     * Returns <code>true</code> if the specified pass is the one for drawing
     * items.
     *
     * @param pass the pass.
     * @return A boolean.
     */
    protected boolean isShapesAndLabelsPass(int pass) {
        return pass == 1;
    }

    /**
     * Tests this renderer for equality with an arbitrary object.
     *
     * @param obj the object (<code>null</code> permitted).
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LineAreaFillRenderer)) {
            return false;
        }

        LineAreaFillRenderer that = (LineAreaFillRenderer) obj;

        return super.equals(obj);
    }

    /**
     * Returns an independent copy of the renderer.
     *
     * @return A clone.
     * @throws CloneNotSupportedException should not happen.
     */
    public Object clone() throws CloneNotSupportedException {
        LineAreaFillRenderer clone = (LineAreaFillRenderer) super.clone();

        return clone;
    }

}

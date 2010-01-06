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
package org.openfaces.component.chart.impl.plots;

import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.TableOrder;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartLabels;
import org.openfaces.component.chart.ChartLegend;
import org.openfaces.component.chart.PieChartView;
import org.openfaces.component.chart.PieSectorProperties;
import org.openfaces.component.chart.impl.PropertiesConverter;
import org.openfaces.component.chart.impl.generators.DynamicPieGenerator;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleBorderModel;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

import java.awt.*;
import java.text.AttributedString;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class PiePlotAdapter extends PiePlot {
    private TableOrder order;

    public PiePlotAdapter(PieDataset pieDataset, Chart chart, PieChartView chartView) {
        setDataset(pieDataset);
        init(this, chart, chartView, pieDataset, null);
    }

    PiePlotAdapter(PiePlot piePlot, CategoryDataset categoryDataset, TableOrder order, PieChartView chartView, Chart chart) { // todo: consider refactoring -- view the usage
        this.order = order;
        PieDataset dataset = new CategoryToPieDataset(categoryDataset, order, 0);
        init(piePlot, chart, chartView, dataset, categoryDataset);
    }

    private void setupLegendLabels(PiePlot plot, Chart chart, PieChartView chartView) {
        if (!chart.isLegendVisible())
            return;

        ChartLegend legend = chart.getLegend();
        if (legend == null || legend.getLabels() == null)
            return;

        final ChartLabels labels = legend.getLabels();
        if (labels.getText() != null) {
            // simple text = the same string for every item
            plot.setLegendLabelGenerator(new PieSectionLabelGenerator() {
                public String generateSectionLabel(PieDataset pieDataset, Comparable comparable) {
                    return labels.getText();
                }

                public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
                    return null;
                }
            });
        } else if (labels.getDynamicText() != null) {
            plot.setLegendLabelGenerator(new DynamicPieGenerator(chartView, labels.getDynamicText()));
        } else {
            plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator());
        }

    }

    private void setupPieLabelGenerator(PiePlot plot, PieChartView chartView) {
        if (chartView.isLabelsVisible()) {
            ChartLabels labels = chartView.getLabels();
            if (labels != null && labels.getDynamicText() != null)
                plot.setLabelGenerator(new DynamicPieGenerator(chartView, labels.getDynamicText()));
            else
                plot.setLabelGenerator(new StandardPieSectionLabelGenerator());
            //TODO: move to style helper
            if (labels != null) {
                StyleObjectModel cssLabelsModel = labels.getStyleObjectModel();
                plot.setLabelFont(CSSUtil.getFont(cssLabelsModel));
                plot.setLabelPaint(cssLabelsModel.getColor());
                plot.setLabelBackgroundPaint(cssLabelsModel.getBackground());
            }
        } else {
            plot.setLabelGenerator(null);
        }
    }

    private void setupSectionPaints(PiePlot plot, PieChartView chartView) {
        if (chartView.getColors() != null) {
            Color[] colors = PropertiesConverter.getColors(chartView.getColors());
            if (colors != null) {
                for (int i = 0; i < colors.length; i++) {
                    Color color = colors[i];
                    plot.setSectionPaint(i, color);
                }
            }
        }
    }

    private void setupTooltipsAndUrls(PiePlot plot, final PieChartView chartView) {
        if (chartView.getTooltip() != null) {
            plot.setToolTipGenerator(new PieToolTipGenerator() {
                public String generateToolTip(PieDataset pieDataset, Comparable comparable) {
                    return chartView.getTooltip();
                }
            });
        } else if (chartView.getDynamicTooltip() != null) {
            plot.setToolTipGenerator(new DynamicPieGenerator(chartView, chartView.getDynamicTooltip()));
        }

        if (chartView.getUrl() != null) {
            plot.setURLGenerator(new PieURLGenerator() {
                public String generateURL(PieDataset pieDataset, Comparable comparable, int i) {
                    return chartView.getUrl();
                }
            });
        } else if (chartView.getDynamicUrl() != null) {
            plot.setURLGenerator(new DynamicPieGenerator(chartView, chartView.getDynamicUrl()));
        }

    }

    private void sectorProcessing(PiePlot plot, PieChartView chartView, PieDataset dataset, CategoryDataset categoryDataset) {
        List<PieSectorProperties> sectors = chartView.getSectors();
        if (sectors == null || sectors.size() == 0)
            return;

        for (PieSectorProperties sector : sectors) {
            DynamicPieGenerator generator = new DynamicPieGenerator(chartView, null);

            int index = -1;

            Float sectorPulled = sector.getPulled();
            if (dataset instanceof CategoryToPieDataset) {
                CategoryToPieDataset cds = (CategoryToPieDataset) dataset;
                int count = getIterationCount(cds);
                for (int q = 0; q < count; q++) {
                    CategoryToPieDataset currPieDataset = new CategoryToPieDataset(categoryDataset, order, q);
                    List keys = currPieDataset.getKeys();
                    index = -1;
                    for (int j = 0; j < keys.size(); j++) {
                        index++;

                        boolean conditionValue = generator.getConditionValue(sector, q, currPieDataset, currPieDataset.getKey(j));
                        if (!conditionValue)
                            continue;

                        if (sectorPulled != null && sectorPulled > 0) {
                            plot.setExplodePercent(index, (double) sectorPulled);
                        }

                        StyleObjectModel cssSectorModel = sector.getStyleObjectModel();
                        if (cssSectorModel != null && cssSectorModel.getBorder() != null) {
                            StyleBorderModel border = cssSectorModel.getBorder();
                            Color borderColor = border.getColor();
                            if (borderColor != null) {
                                plot.setSectionOutlinePaint(index, borderColor);
                                plot.setSectionOutlineStroke(index, PropertiesConverter.toStroke(border));
                            }

                            Color sectorModelColor = cssSectorModel.getColor();
                            if (sectorModelColor != null) {
                                plot.setSectionPaint(index, sectorModelColor);
                            }
                        }
                    }
                }
            } else {
                if (dataset == null || dataset.getKeys() == null)
                    continue;

                for (int j = 0; j < dataset.getKeys().size(); j++) {
                    index++;

                    boolean conditionValue = generator.getConditionValue(sector, 0, dataset, dataset.getKey(j));
                    if (!conditionValue)
                        continue;

                    if (sectorPulled != null && sectorPulled > 0) {
                       plot.setExplodePercent(index, (double) sectorPulled);
                    }

                    StyleObjectModel cssSectorModel = sector.getStyleObjectModel();
                    if (cssSectorModel != null && cssSectorModel.getBorder() != null) {
                        StyleBorderModel border = cssSectorModel.getBorder();
                        if (border.getColor() != null) {
                            plot.setSectionOutlinePaint(index, border.getColor());
                            plot.setSectionOutlineStroke(index, PropertiesConverter.toStroke(border));
                        }

                    }
                    if (cssSectorModel != null && cssSectorModel.getColor() != null) {
                        plot.setSectionPaint(index, cssSectorModel.getColor());
                    }
                }
            }

        }

    }

    private int getIterationCount(CategoryToPieDataset cds) {
        if (order == TableOrder.BY_ROW) {
            return cds.getUnderlyingDataset().getRowCount();
        }
        if (order == TableOrder.BY_COLUMN) {
            return cds.getUnderlyingDataset().getColumnCount();
        }

        return 0;
    }

    private void init(PiePlot plot, Chart chart, PieChartView chartView, PieDataset dataset, CategoryDataset categoryDataset) {
        PlotUtil.setupColorProperties(chart, plot);
        setupLegendLabels(plot, chart, chartView);
        setupSectionPaints(plot, chartView);
        setupPieLabelGenerator(plot, chartView);
        setupLegendLabels(plot, chart, chartView);
        setupTooltipsAndUrls(plot, chartView);
        sectorProcessing(plot, chartView, dataset, categoryDataset);
    }


}


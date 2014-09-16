/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.chart;

import org.openfaces.component.chart.ChartLabelPosition;
import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.ItemInfo;
import org.openfaces.component.chart.LineStyle;
import org.openfaces.component.chart.PieSectorEvent;
import org.openfaces.component.chart.PieSectorInfo;
import org.openfaces.component.chart.PlainSeries;
import org.openfaces.component.chart.Series;
import org.openfaces.component.chart.Sorter;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.util.Faces;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import java.awt.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ChartDemo implements Serializable {
    private CityPrecipitation cityPrecipitation; // todo: simplify backing beans structure for Chart demos
    private AverageTemp averageTemp;
    private MonthPrecipitation monthPrecipitation;
    private MonthPrecipitation monthPrecipitationDynamic;
    private CityTemperature cityTemperature;
    private PieSectorInfo selectedSector;
    private ItemInfo selectedItem;

    public DateFormat getMediumDateFormat() {
        return new SimpleDateFormat("MMM-yyyy", new Locale("en"));
    }

    public CityPrecipitation getCityPrecipitation() {
        return cityPrecipitation;
    }

    public void setCityPrecipitation(CityPrecipitation cityPrecipitation) {
        this.cityPrecipitation = cityPrecipitation;
    }

    public AverageTemp getAverageTemp() {
        return averageTemp;
    }

    public void setAverageTemp(AverageTemp averageTemp) {
        this.averageTemp = averageTemp;
    }

    public MonthPrecipitation getMonthPrecipitation() {
        return monthPrecipitation;
    }

    public MonthPrecipitation getMonthPrecipitationDynamic() {
        monthPrecipitationDynamic.makeData();
        return monthPrecipitationDynamic;
    }

    public void setMonthPrecipitation(MonthPrecipitation monthPrecipitation) {
        this.monthPrecipitation = monthPrecipitation;
    }

    public void setMonthPrecipitationDynamic(MonthPrecipitation monthPrecipitationDynamic) {
        this.monthPrecipitationDynamic = monthPrecipitationDynamic;
    }

    public CityTemperature getCityTemperature() {
        return cityTemperature;
    }

    public void setCityTemperature(CityTemperature cityTemperature) {
        this.cityTemperature = cityTemperature;
    }

    public void generateNewData(AjaxBehaviorEvent event) {
        cityPrecipitation.makeData();
        averageTemp.makeData();
        monthPrecipitation.makeData();
        cityTemperature.makeData();
    }

    public Collection<Paint> getPieChartColors() {
        String[] colorStrings = new String[]{"#00C8FF", "#FFB900", "#BEFF3F", "#FF591F"};
        ArrayList<Paint> gradients = new ArrayList<Paint>();

        for (String colorString : colorStrings) {
            try {
                Color color = CSSUtil.parseColor(colorString);
                Color secondaryColor = new Color(color.getRed() / 2, color.getGreen() / 2, color.getBlue() / 2, color.getAlpha());
                GradientPaint gradient = new GradientPaint(0, 0, color, 0, 250, secondaryColor);
                gradients.add(gradient);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Can't parse color string: " + colorString);
            }
        }

        return gradients;
    }

    public LineStyle getPieChartOutline() {
        return new LineStyle(Color.WHITE, new BasicStroke(2.25f));
    }

    public void quarterClickListener(ActionEvent event) {
        PieSectorEvent pEvent = (PieSectorEvent) event;
        selectedSector = pEvent.getSector();
    }

    public ItemInfo getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(ItemInfo selectedItem) {
        this.selectedItem = selectedItem;
    }

    private PieSectorInfo getSector() {
        return Faces.var("sector", PieSectorInfo.class);
    }

    public boolean isSelectedSector() {
        if (selectedSector == null)
            return false;

        return getSector().getKey().equals(selectedSector.getKey());
    }

    public boolean isDefaultSector() {
        if (selectedSector != null)
            return false;

        PieSectorInfo sector = getSector();
        if (sector.getKey().equals("ten")) {
            selectedSector = sector;
            return true;
        }
        return false;
    }


    public ChartLabelPosition getOutsideRightLabelsPosition() {
        return ChartLabelPosition.OUTSIDE_RIGHT;
    }

    public ChartLabelPosition getOutsideLeftLabelsPosition() {
        return ChartLabelPosition.OUTSIDE_LEFT;
    }

    public ChartLabelPosition getDefaultLabelsPosition() {
        return ChartLabelPosition.CENTER;
    }

    public ChartLabelPosition getPositiveLabelsPosition() {
        return ChartLabelPosition.OUTSIDE_TOP;
    }

    public ChartLabelPosition getNegativeLabelsPosition() {
        return ChartLabelPosition.OUTSIDE_BOTTOM;
    }

    public Paint getBackgroundGradientPaint() {
        return new GradientPaint(150.0F, 0.0F, new Color(255, 255, 0, 200), 350F, 0.0F, new Color(255, 200, 128, 255), false);
    }

    public Paint getTitleGradientPaint() {
        return new GradientPaint(20.0F, 0.0F, new Color(255, 0, 0, 255), 400F, 0.0F, new Color(80, 0, 0, 255), false);
    }

    public Paint getColorAsPaint() {
        return Color.GREEN;
    }

    public Color getGreenColor() {
        return Color.GREEN;
    }

    public Color getLightRedColor() {
        return new Color(255, 128, 128, 128);
    }

    public Color getBlackColor() {
        return Color.BLACK;
    }

    public LineStyle getDefaultStroke() {
        return new LineStyle(new BasicStroke(3.0F));
    }

    public LineStyle getDefaultSelectionStyle() {
        return new LineStyle(new Color(0, 0, 255), new BasicStroke(5.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    }

    public Collection<LineStyle> getLineStyles() {
        final ArrayList<LineStyle> strokes = new ArrayList<LineStyle>();

        strokes.add(new LineStyle(new BasicStroke(1.75F, BasicStroke.CAP_SQUARE, 1, 1.0F, new float[]{
                1F, 5.5F
        }, 5.0F)));
        strokes.add(new LineStyle(new BasicStroke(2.25F, BasicStroke.CAP_SQUARE, 1, 1.0F, new float[]{
                25F, 8F
        }, 1.0F)));
        strokes.add(new LineStyle(new BasicStroke(2F, BasicStroke.CAP_SQUARE, 1, 1.0F, new float[]{
                15F, 7.5F, 1.5F, 7.5F
        }, 1.0F)));

        return strokes;
    }

    public Collection<Paint> getFillPaints() {
        final ArrayList<Paint> fillPaints = new ArrayList<Paint>();
        fillPaints.add(Color.WHITE);
        fillPaints.add(Color.WHITE);
        fillPaints.add(Color.BLUE);
        return fillPaints;
    }

    public LineStyle getDefaultLineChartLineStyle() {
        return new LineStyle(Color.BLUE, new BasicStroke(1.25F, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0F, new float[]{
                6F, 12F
        }, 0.0F));
    }

    public LineStyle getDefaultLineStyle() {
        return new LineStyle(Color.BLUE, new BasicStroke(3.0F, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0F, new float[]{
                4F, 4F
        }, 0.0F));
    }

    public LineStyle getSolidLineStyle() {
        return new LineStyle(Color.BLUE, new BasicStroke(2.5F, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 7.0F));
    }


    public LineStyle getMarkerLineStyle() {
        return new LineStyle(Color.RED, new BasicStroke(5.0F, BasicStroke.CAP_SQUARE, 1, 1.0F, new float[]{
                4F, 4F
        }, 0.0F));
    }


    public Collection<LineStyle> getOutlines() {
        final ArrayList<LineStyle> outlines = new ArrayList<LineStyle>();

        outlines.add(new LineStyle(Color.RED, new BasicStroke(1.5F, BasicStroke.CAP_BUTT, 1)));
        outlines.add(new LineStyle(Color.BLUE, new BasicStroke(2.5F, BasicStroke.CAP_SQUARE, 1, 1.0F, new float[]{
                1F, 1F
        }, 0.0F)));
        outlines.add(new LineStyle(Color.GREEN, new BasicStroke(1.25F, BasicStroke.CAP_SQUARE, 1, 1.0F, new float[]{
                1F, 1F
        }, 0.0F)));

        return outlines;
    }

    public ChartModel getNumericChartModel() {
        return new NumbersDataModel();
    }

    private class NumbersDataModel implements ChartModel, Externalizable, Serializable {
        private Series[] series;

        private void initializeDataModel() {
            series = new Series[2];

            PlainSeries usdSeries = new PlainSeries("USD");
            PlainSeries euroSeries = new PlainSeries("Euro");

            Map<Date, Double> usdData = new HashMap<Date, Double>();
            Map<Date, Double> euroData = new HashMap<Date, Double>();

            Random random = new Random();

            final double usdMaximumDailyDiff = 0.55;
            final double usdMinimumExchangeRate = 1.25;
            final double usdMaximumExchangeRate = 10.0;
            double usdExchangeRateValue = usdMaximumExchangeRate * Math.random();

            final double euroMaximumDailyDiff = 0.25;
            final double euroMinimumExchangeRate = 9.5;
            final double euroMaximumExchangeRate = 12.25;
            double euroExchangeRateValue = euroMaximumExchangeRate * Math.random();

            for (int valuePoints = 1; valuePoints <= 200; valuePoints++) {
                Calendar calendar = Calendar.getInstance(new Locale("en"));
                Date key = null;

                while (key == null || usdData.containsKey(key)) {
                    final int month = 1;
                    final int date = valuePoints;
                    final int hourOfDay = 8 + random.nextInt(9);
                    final int minute = 30;
                    final int seconds = 0;
                    final int milliseconds = 0;

                    calendar.set(2008, month, date, hourOfDay, minute, seconds);
                    calendar.set(Calendar.MILLISECOND, milliseconds);
                    key = calendar.getTime();
                }

                usdData.put(key, usdExchangeRateValue);
                euroData.put(key, euroExchangeRateValue);

                usdExchangeRateValue += 2 * usdMaximumDailyDiff * random.nextDouble() - usdMaximumDailyDiff;
                euroExchangeRateValue += 2.25 * euroMaximumDailyDiff * random.nextDouble() - euroMaximumDailyDiff;

                if (usdExchangeRateValue < usdMinimumExchangeRate) {
                    usdExchangeRateValue = usdMinimumExchangeRate;
                } else if (usdExchangeRateValue > usdMaximumExchangeRate) {
                    usdExchangeRateValue = usdMaximumExchangeRate;
                }

                if (euroExchangeRateValue < euroMinimumExchangeRate) {
                    euroExchangeRateValue = euroMinimumExchangeRate;
                } else if (euroExchangeRateValue > euroMaximumExchangeRate) {
                    euroExchangeRateValue = euroMaximumExchangeRate;
                }
            }

            usdSeries.setComparator(Sorter.ASCENDING);
            usdSeries.setData(usdData);

            euroSeries.setComparator(Sorter.ASCENDING);
            euroSeries.setData(euroData);

            series[0] = usdSeries;
            series[1] = euroSeries;
        }

        public Series[] getSeries() {
            if (series == null) initializeDataModel();
            return series;
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(series);
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            series = (Series[]) in.readObject();
        }
    }
}
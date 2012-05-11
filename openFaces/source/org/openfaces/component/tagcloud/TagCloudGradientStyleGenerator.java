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

import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.util.Environment;

import javax.faces.FacesException;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.StringTokenizer;

/**
 * @author : roman.nikolaienko
 */
public class TagCloudGradientStyleGenerator {
    private static final double DEFAULT_MAX_FONT_SIZE = 25;
    private static final double DEFAULT_MIN_FONT_SIZE = 12;

    private static final double DEFAULT_MAX_OPACITY = 1;
    private static final double DEFAULT_MIN_OPACITY = 0.8;

    private static final int DEFAULT_MAX_RED = 0;
    private static final int DEFAULT_MAX_GREEN = 0;
    private static final int DEFAULT_MAX_BLUE = 0;

    private static final int DEFAULT_MIN_RED = 140;
    private static final int DEFAULT_MIN_GREEN = 140;
    private static final int DEFAULT_MIN_BLUE = 140;

    private double maxWeight;
    private double minWeight;

    private String minFontSize = "";
    private String maxFontSize = "";

    private double minOpacity;
    private double maxOpacity;

    private Color minColor;
    private Color maxColor;

    public TagCloudGradientStyleGenerator(String maxStyle, String minStyle,
                                       double maxWeight, double minWeight) {
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;

        setMaxMinStyleParameters(maxStyle, true);
        setMaxMinStyleParameters(minStyle, false);
    }

    public double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(double minItemWeight) {
        this.minWeight = minItemWeight;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxItemWeight) {
        this.maxWeight = maxItemWeight;
    }

    public String getMinFontSize() {
        return minFontSize;
    }

    public void setMinFontSize(String minFontSize) {
        this.minFontSize = minFontSize;
    }

    public String getMaxFontSize() {
        return maxFontSize;
    }

    public void setMaxFontSize(String maxFontSize) {
        this.maxFontSize = maxFontSize;
    }

    public double getMinOpacity() {
        return minOpacity;
    }

    public void setMinOpacity(double minOpacity) {
        this.minOpacity = minOpacity;
    }

    public double getMaxOpacity() {
        return maxOpacity;
    }

    public void setMaxOpacity(double maxOpacity) {
        this.maxOpacity = maxOpacity;
    }

    public Color getMinColor() {
        return minColor;
    }

    public void setMinColor(Color minColor) {
        this.minColor = minColor;
    }

    public Color getMaxColor() {
        return maxColor;
    }

    public void setMaxColor(Color maxColor) {
        this.maxColor = maxColor;
    }

    public String generateStyle(TagCloudItem item) {
        StringBuilder styleBuilder = new StringBuilder();
        Color color = getColor(item);
        double opacity = getOpacity(item);
        String opacityToRender = new java.text.DecimalFormat("0.00").format(opacity);
        int ieOpacity = (int) Math.round(opacity * 100);
        String fonSize = getFontSize(item);
        String colorToRender = CSSUtil.formatColor(color);

        styleBuilder.append("color: ").append(colorToRender).append("; ");

        styleBuilder.append("font-size: ").append(fonSize).append("; ");

        if (opacity < 1) {
            styleBuilder.append("opacity: ").append(opacityToRender).append("; ");
            if (Environment.isExplorer()) {
                styleBuilder.append("filter: alpha(opacity=");
                styleBuilder.append(ieOpacity).append("); ");
            }
        }
        return styleBuilder.toString();
    }

    private void setMaxMinStyleParameters(String style, boolean isMax) {
        if (style == null)
            return;
        StringTokenizer worker = new StringTokenizer(style.replace(" ", "").trim(), ";");
        StringTokenizer workerSmall;
        String stylePie;
        String attribute = "";
        String value = "";
        while (worker.hasMoreElements()) {
            stylePie = worker.nextToken().trim();
            workerSmall = new StringTokenizer(stylePie, ":");
            if (workerSmall.hasMoreElements() && workerSmall.countTokens() == 2) {
                attribute = workerSmall.nextToken().trim();
                value = workerSmall.nextToken().trim();
                if (attribute.equalsIgnoreCase("color")) {
                    if (isMax)
                        setMaxColor(CSSUtil.parseColor(value));
                    else
                        setMinColor(CSSUtil.parseColor(value));
                }
                if (attribute.equalsIgnoreCase("font-size")) {
                    if (isMax)
                        setMaxFontSize(value);
                    else
                        setMinFontSize(value);
                }
                if (attribute.equalsIgnoreCase("opacity")) {
                    if (isMax)
                        setMaxOpacity(Double.parseDouble(value));
                    else
                        setMinOpacity(Double.parseDouble(value));
                }
            }
        }
    }

    private String getUnits(String cssFontString, ParsePosition pos) {
        if (pos.getIndex() == cssFontString.length())
            return "";
        String fontUnits = cssFontString.substring(pos.getIndex()).trim();
        String[] allUnits = new String[]{"em", "ex", "px", "pt", "pc", "in", "mm", "cm", "%"};
        for (String curUnit : allUnits) {
            if (fontUnits.equalsIgnoreCase(curUnit))
                return curUnit;
        }
        throw new FacesException("Units are wrong " + fontUnits);
    }

    private String getFontSize(TagCloudItem item) {
        DecimalFormat format = new DecimalFormat();
        Number maxFontSizeValue = 0;
        Number minFontSizeValue = 0;

        String maxFontSize = getMaxFontSize();
        String minFontSize = getMinFontSize();
        String maxFontUnits = "em";
        String minFontUnits = "em";

        ParsePosition pos = new ParsePosition(0);

        if (maxFontSize.length() != 0) {
            maxFontSizeValue = format.parse(maxFontSize, pos);
            maxFontUnits = getUnits(maxFontSize, pos);
        }

        pos.setIndex(0);

        if (minFontSize.length() != 0) {
            minFontSizeValue = format.parse(minFontSize, pos);
            minFontUnits = getUnits(minFontSize, pos);
        }

        double resultDoubleValue;

        StringBuilder toReturn = new StringBuilder();
        double maxSize =  DEFAULT_MAX_FONT_SIZE;
        double minSize =  DEFAULT_MIN_FONT_SIZE;

        if (maxFontSize.length() == 0 && minFontSize.length() == 0) {
            resultDoubleValue = Math.round(getParameterGradient(item, minSize, maxSize) * 100) / 100.;
            return toReturn.append(String.valueOf(resultDoubleValue)).append("px").toString();
        }

        if (maxFontSize.length() != 0 && minFontSize.length() == 0) {
            double max = maxFontSizeValue.doubleValue();
            resultDoubleValue = Math.round(getParameterGradient(item, max - 0.4 * max, max) * 100) / 100.;
            return toReturn.append(String.valueOf(resultDoubleValue)).append(maxFontUnits).toString();
        }

        if (maxFontSize.length() == 0 && minFontSize.length() != 0) {
            double min = minFontSizeValue.doubleValue();
            resultDoubleValue = Math.round(getParameterGradient(item, min, min + 0.4 * min) * 100) / 100.;
            return toReturn.append(String.valueOf(resultDoubleValue)).append(maxFontUnits).toString();
        }

        if (!maxFontUnits.equalsIgnoreCase(minFontUnits))
            throw new FacesException("Units of font-size attribute must be the same, but was: " +
                    minFontUnits + " from minItemStyle, and " + maxFontUnits + " from maxItemStyle.");

        resultDoubleValue = getParameterGradient(item, minFontSizeValue.doubleValue(), maxFontSizeValue.doubleValue());
        resultDoubleValue = Math.round(resultDoubleValue * 100) / 100.;

        return toReturn.append(String.valueOf(resultDoubleValue)).append(minFontUnits).toString();

    }

    private double getOpacity(TagCloudItem item) {
        double minOpacity = getMinOpacity();
        double maxOpacity = getMaxOpacity();
        if (minOpacity == 0 && maxOpacity == 0) {
            return 1;
        }
        minOpacity = minOpacity == 0 ? DEFAULT_MIN_OPACITY : minOpacity;
        maxOpacity = maxOpacity == 0 ? DEFAULT_MAX_OPACITY : maxOpacity;
        return Math.round(getParameterGradient(item, minOpacity, maxOpacity) * 100) / 100.;
    }

    private Color getColor(TagCloudItem item) {
        Color maxColor = getMaxColor();
        Color minColor = getMinColor();

        int maxRed = maxColor != null ? maxColor.getRed() : DEFAULT_MAX_RED;
        int maxGreen = maxColor != null ? maxColor.getGreen() : DEFAULT_MAX_GREEN;
        int maxBlue = maxColor != null ? maxColor.getBlue() : DEFAULT_MAX_BLUE;

        int minRed = minColor != null ? minColor.getRed() : DEFAULT_MIN_RED;
        int minGreen = minColor != null ? minColor.getGreen() : DEFAULT_MIN_GREEN;
        int minBlue = minColor != null ? minColor.getBlue() : DEFAULT_MIN_BLUE;

        int red = (int) getParameterGradient(item, minRed, maxRed);
        int green = (int) getParameterGradient(item, minGreen, maxGreen);
        int blue = (int) getParameterGradient(item, minBlue, maxBlue);

        return new Color(red, green, blue);
    }

    private double getParameterGradient(TagCloudItem item, double minParameterValue, double maxParameterValue) {
        if (getMaxWeight() == getMinWeight())
//            return Math.round((maxParameterValue + minParameterValue) / 2);
        return minParameterValue;
        double weight = item.getWeight();
        if (weight <= getMinWeight())
            return minParameterValue;
        return (maxParameterValue - minParameterValue) * (weight - getMinWeight()) / (getMaxWeight() - getMinWeight()) + minParameterValue;
    }
}

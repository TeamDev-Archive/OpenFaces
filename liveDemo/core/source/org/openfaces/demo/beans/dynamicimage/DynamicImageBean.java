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

package org.openfaces.demo.beans.dynamicimage;

import org.openfaces.component.input.DropDownItem;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class DynamicImageBean implements Serializable {
    private static final List<DropDownItem> PREDEFINED_FONT_SIZES = Arrays.asList(
            new DropDownItem("12"),
            new DropDownItem("16"),
            new DropDownItem("20"),
            new DropDownItem("35"),
            new DropDownItem("50"),
            new DropDownItem("80"),
            new DropDownItem("150"),
            new DropDownItem("300"));
    private static final List<DropDownItem> PREDEFINED_GLOW_WIDTHS = Arrays.asList(
            new DropDownItem("3"),
            new DropDownItem("4"),
            new DropDownItem("5"),
            new DropDownItem("7"),
            new DropDownItem("10"),
            new DropDownItem("20"),
            new DropDownItem("30"));

    private static final List<SelectItem> COLOR_ITEMS = Arrays.asList(
            new SelectItem(Color.BLACK, "Black", null),
            new SelectItem(Color.WHITE, "White", null),
            new SelectItem(Color.LIGHT_GRAY, "Light-gray", null),
            new SelectItem(Color.GRAY, "Gray", null),
            new SelectItem(Color.DARK_GRAY, "Dark-gray", null),
            new SelectItem(Color.RED, "Red", null),
            new SelectItem(Color.GREEN, "Green", null),
            new SelectItem(Color.BLUE, "Blue", null),
            new SelectItem(Color.CYAN, "Cyan", null),
            new SelectItem(Color.MAGENTA, "Magenta", null),
            new SelectItem(Color.YELLOW, "Yellow", null),
            new SelectItem(Color.ORANGE, "Orange", null),
            new SelectItem(Color.PINK, "Pink", null),
            new SelectItem(new Color(245, 245, 220), "Beige", null),
            new SelectItem(new Color(240, 230, 140), "Khaki", null),
            new SelectItem(new Color(75, 0, 130), "Indigo", null),
            new SelectItem(new Color(0, 128, 128), "Teal", null));

    public static final Converter COLOR_CONVERTER = new ColorConverter();

    private String text = "DynamicImage";
    private int glowWidth = 5;
    private Color textColor = Color.DARK_GRAY;
    private Color glowColor = Color.ORANGE;
    private int fontSize = 50;
    private String fontName = "Tahoma";
    private int fontStyle = Font.PLAIN;
    private float paddingSize = 20;

    public List<SelectItem> getColorItems() {
        return COLOR_ITEMS;
    }

    public List<DropDownItem> getPredefinedFontSizes() {
        return PREDEFINED_FONT_SIZES;
    }

    public List<DropDownItem> getPredefinedGlowWidths() {
        return PREDEFINED_GLOW_WIDTHS;
    }

    public Converter getColorConverter() {
        return COLOR_CONVERTER;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text.length() > 50)
            text = text.substring(0, 50);
        this.text = text;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        if (fontSize < 5)
            fontSize = 5;
        if (fontSize > 300)
            fontSize = 300;
        this.fontSize = fontSize;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Color getGlowColor() {
        return glowColor;
    }

    public void setGlowColor(Color glowColor) {
        this.glowColor = glowColor;
    }

    public int getGlowWidth() {
        return glowWidth;
    }

    public void setGlowWidth(int glowWidth) {
        if (glowWidth < 0)
            glowWidth = 0;
        if (glowWidth > 75)
            glowWidth = 75;
        this.glowWidth = glowWidth;
    }

    public RenderedImage getTextAsImage() {
        Font font = new Font(fontName, fontStyle, fontSize);

        AffineTransform transform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(transform, true, true);
        GlyphVector glyphVector = font.createGlyphVector(frc, text);
        LineMetrics lineMetrics = font.getLineMetrics(text, frc);
        Rectangle2D logicalBounds = glyphVector.getLogicalBounds();

        int imageWidth = (int) Math.ceil(logicalBounds.getWidth() + glowWidth * 2 + paddingSize * 2);
        int imageHeight = (int) Math.ceil(logicalBounds.getHeight() + glowWidth * 2 + paddingSize * 2);
        BufferedImage image = new BufferedImage(
                imageWidth,
                imageHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setPaint(Color.WHITE);
        graphics.fill(new Rectangle2D.Float(0, 0, imageWidth, imageHeight));


        float x = glowWidth + paddingSize;
        float y = glowWidth + paddingSize + lineMetrics.getAscent();
        Shape textShape = glyphVector.getOutline(x, y);

        float glowR = 255 - glowColor.getRed();
        float glowG = 255 - glowColor.getGreen();
        float glowB = 255 - glowColor.getBlue();

        float maxStrokeWidth = glowWidth * 2;
        for (int gradationCount = (int) Math.ceil(glowWidth / 1.5), i = 1; i <= gradationCount; i++) {
            float saturation = ((float) i) / gradationCount;
            float r = 255 - saturation * glowR;
            float g = 255 - saturation * glowG;
            float b = 255 - saturation * glowB;
            Color currentGlowColor = new Color((int) r, (int) g, (int) b);
            float strokeWidth = maxStrokeWidth - maxStrokeWidth / gradationCount * (i - 1);
            Stroke stroke = new BasicStroke(strokeWidth, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_ROUND);
            Shape textOutlineShape = stroke.createStrokedShape(textShape);
            graphics.setStroke(stroke);
            graphics.setPaint(currentGlowColor);
            graphics.fill(textOutlineShape);
        }

        graphics.setPaint(textColor);
        graphics.fill(textShape);

        return image;
    }

    private static class ColorConverter implements Converter {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            StringTokenizer stringTokenizer = new StringTokenizer(value, ",", false);
            int r = Integer.parseInt(stringTokenizer.nextToken());
            int g = Integer.parseInt(stringTokenizer.nextToken());
            int b = Integer.parseInt(stringTokenizer.nextToken());
            return new Color(r, g, b);

        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            Color color = ((Color) value);
            return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
        }
    }
}

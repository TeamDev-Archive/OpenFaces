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
package org.openfaces.renderkit.output;

import org.openfaces.component.output.GraphicText;
import org.openfaces.renderkit.ImageDataModel;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;
import org.openfaces.renderkit.cssparser.StyleFontModel;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Darya Shumilina
 */
public class GraphicTextRenderer extends RendererBase {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        GraphicText graphicText = (GraphicText) component;

        // default style properties
        StyleObjectModel styleModel = graphicText.getStyleObjectModel();
        StyleFontModel font = styleModel.getFont();
        String fontName = font.getName();
        int fontSize = font.getSize();
        int fontStyle = font.getStyle();

        String text = Rendering.convertToString(context, graphicText, graphicText.getValue());

        int direction = graphicText.getDirection();

        AffineTransform transform = new AffineTransform();

        float translationIndex = (float) 1.2;
        float increaseIndex = (float) 2.4;

        if (text.length() <= 3) {
            translationIndex = 5;
            increaseIndex = 10;
        }

        transform.rotate(Math.toRadians(-direction));

        FontRenderContext frc = new FontRenderContext(transform, true, true);
        TextLayout textLayout = new TextLayout(text, new Font(fontName, fontStyle, fontSize), frc);

        Rectangle bounds = textLayout.getOutline(transform).getBounds();

        // calculate transformed text width and height
        double textHeight = bounds.getSize().getHeight();
        double textWidth = bounds.getSize().getWidth();

        BufferedImage image = new BufferedImage(
                (int) Math.round(textWidth * increaseIndex),
                (int) Math.round(textHeight * increaseIndex),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(styleModel.getColor());
        graphics.translate(translationIndex * textWidth, translationIndex * textHeight);
        graphics.transform(transform);

        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        textLayout.draw(graphics, 0, 0);

        int absciss = calculateAbsciss(image);
        int ordinate = calculateOrdinate(image);

        BufferedImage croppedImage = image.getSubimage(absciss - 1, ordinate - 1, (int) textWidth + 2, (int) textHeight + 2);
        graphics.dispose();

        ImageDataModel model = Rendering.getDataModel(croppedImage);

        ResponseWriter writer = context.getResponseWriter();

        int imageWidth = croppedImage.getWidth();
        int imageHeight = croppedImage.getHeight();

        Rendering.startWriteIMG(writer, context, graphicText, "png",
                model, new int[]{imageWidth, imageHeight});

        Rendering.writeComponentClassAttribute(writer, graphicText);
        Rendering.writeStandardEvents(writer, graphicText);
        writeAttribute(writer, "title ", graphicText.getTitle());
        writeAttribute(writer, "lang ", graphicText.getLang());
        writeAttribute(writer, "height", String.valueOf(imageHeight));
        writeAttribute(writer, "width", String.valueOf(imageWidth));

        Rendering.encodeInitComponentCall(context, graphicText, true);

        writer.endElement("img");
    }

    private int calculateAbsciss(BufferedImage image) {
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        boolean isAbsciss = false;
        int absciss = 0;
        Color background = g2.getBackground();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color currentColor = new Color(image.getRGB(i, j));
                if (!currentColor.equals(background)) {
                    absciss = i;
                    isAbsciss = true;
                    break;
                }
            }
            if (isAbsciss) break;
        }
        return absciss;
    }

    private int calculateOrdinate(BufferedImage image) { //todo: optimize this to avoid per-pixel image analysis
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        boolean isOrdinate = false;
        int ordinate = 0;
        Color background = g2.getBackground();

        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color currentColor = new Color(image.getRGB(x, y));
                if (!currentColor.equals(background)) {
                    ordinate = y;
                    isOrdinate = true;
                    break;
                }
            }
            if (isOrdinate) break;
        }
        return ordinate;
    }

}
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
package org.openfaces.renderkit.cssparser;

import com.steadystate.css.parser.CSSOMParser;
import org.openfaces.util.Log;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValueList;
import org.w3c.dom.css.RGBColor;

import java.awt.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class CSSUtil {
    private static final String DEFAULT_TITLE_FONT_NAME = "Verdana";
    private static final int DEFAULT_TITLE_FONT_SIZE = 12;
    private static final int DEFAULT_TITLE_FONT_STYLE = Font.PLAIN;

    private static final Map<String, Color> NAMED_COLORS = getNamedColors();

    public static Font getFont(StyleObjectModel som) {

        StyleFontModel somFont = som.getFont();
        if (somFont == null || somFont.getName() == null || somFont.getStyle() == null)
            return new Font(DEFAULT_TITLE_FONT_NAME, 0, DEFAULT_TITLE_FONT_SIZE);

        int size = DEFAULT_TITLE_FONT_SIZE;
        int style = DEFAULT_TITLE_FONT_STYLE;
        String name = DEFAULT_TITLE_FONT_NAME;

        if (somFont.getSize() != null) {
            size = somFont.getSize();
        }
        if (somFont.getName() != null) {
            name = somFont.getName();
        }
        if (somFont.getStyle() != null) {
            style = somFont.getStyle();
        }

        return new Font(name, style, size);
    }

    public static StyleObjectModel getStyleObjectModel(StyledComponent[] components) {
        if (components == null)
            return null;

        if (components.length == 0)
            return null;

        StyleObjectModel model = null;

        for (StyledComponent stComponent : components) {
            String style = stComponent.getTextStyle();
            if (style != null) {
                StyleObjectModel currentObjectModel = getStyleObjectModel(style, stComponent.getHint());
                if (currentObjectModel != null) {
                    //need casting
                    if (model == null) {
                        model = new StyleObjectModel();
                    }
                    castProperties(model, currentObjectModel);
                }
            }
        }

        return model;
    }

    public static StyleObjectModel getChartMarkerLabelOffsetModel(String cssString) {
        StringBuilder builder = new StringBuilder();
        if (!cssString.startsWith("margin:")) {
            builder.append("margin:");
        }
        builder.append(cssString.trim());
        return getStyleObjectModel(builder.toString(), null);
    }

    public static StyleObjectModel getLineStyleModel(String cssString) {
        StringBuilder builder = new StringBuilder();
        if (!cssString.startsWith("border:")) {
            builder.append("border:");
        }
        builder.append(cssString.trim());
        return getStyleObjectModel(builder.toString(), null);
    }

    public static StyleObjectModel getCustomAttributeStyleModel(String cssString) {
        return getStyleObjectModel(cssString, null);
    }

    private static StyleObjectModel getStyleObjectModel(String cssString, String hint) {
        if (cssString == null)
            return null;

        if (cssString.length() == 0)
            return null;

        if (cssString.startsWith("{"))
            cssString = cssString.substring(1);

        if (cssString.endsWith("}"))
            cssString = cssString.substring(0, cssString.length() - 1);

        if (hint != null && cssString.indexOf(hint) == -1) {
            String startBlock = cssString.substring(0, 1);
            String endBlock = cssString.substring(2);
            cssString = startBlock + hint + ": " + endBlock;
        }
        StyleObjectModel model = null;

        Reader reader = new StringReader(cssString);
        CSSOMParser parser2 = new CSSOMParser();
        CSSStyleDeclaration n;

        try {
            n = parser2.parseStyleDeclaration(new InputSource(reader));
            model = new StyleObjectModel();
            model.setBorder(new StyleBorderModel());

            boolean isItalic = false;
            boolean isBold = false;

            for (int i = 0; i < n.getLength(); i++) {
                String name = n.item(i);

                if (n.getPropertyCSSValue(name) instanceof CSSPrimitiveValue) {

                    CSSPrimitiveValue value = (CSSPrimitiveValue) n.getPropertyCSSValue(name);

                    if (name.equalsIgnoreCase("color")) {
                        model.setColor(getColor(value));
                    }

                    if (name.equalsIgnoreCase("border-color")) {
                        model.getBorder().setColor(getColor(value));
                    }

                    if (name.equalsIgnoreCase("border-width")) {
                        model.getBorder().setWidth(getSize(value));
                    }

                    if (name.equalsIgnoreCase("width")) {
                        model.setWidth(getSize(value));
                    }

                    if (name.equalsIgnoreCase("height")) {
                        model.setHeight(getSize(value));
                    }

                    if (name.equalsIgnoreCase("border")) {
                        CSSValueList list = (CSSValueList) n.getPropertyCSSValue(name);
                        model.setBorder(getBorder(list));
                    }

                    if (name.equalsIgnoreCase("margin")) {
                        CSSValueList list = (CSSValueList) n.getPropertyCSSValue(name);
                        model.setMargings(getMargins(list));
                    }

                    if (name.equalsIgnoreCase("border-style")) {
                        model.getBorder().setStyle(value.getStringValue());
                    }

                    if (name.equalsIgnoreCase("background")) {
                        model.setBackground(getColor(value));
                    }

                    if (name.equalsIgnoreCase("background-color")) {
                        model.setBackground(getColor(value));
                    }

                    if (name.equalsIgnoreCase("font")) {
                        if (model.getFont() == null) model.setFont(new StyleFontModel());
                        CSSValueList list = (CSSValueList) n.getPropertyCSSValue(name);
                        model.setFont(getFont(model, list));
                    }

                    if (name.equalsIgnoreCase("font-family")) {
                        if (model.getFont() == null) model.setFont(new StyleFontModel());
                        model.getFont().setName(value.getCssText());
                    }

                    if (name.equalsIgnoreCase("font-style")) {
                        if (model.getFont() == null) model.setFont(new StyleFontModel());
                        String stringFontStyleValue = value.getStringValue();
                        int fontStyleValue = 0;
                        if (stringFontStyleValue.equalsIgnoreCase("italic")) {
                            fontStyleValue = Font.ITALIC;
                            isItalic = true;
                        }
                        model.getFont().setStyle(fontStyleValue);
                    }

                    if (name.equalsIgnoreCase("font-weight")) {
                        if (model.getFont() == null) model.setFont(new StyleFontModel());
                        String stringFontWeightValue = value.getStringValue();
                        int fontStyleValue = 0;
                        if (stringFontWeightValue.equalsIgnoreCase("bold")) {
                            fontStyleValue = Font.BOLD;
                            isBold = true;
                        }
                        model.getFont().setStyle(fontStyleValue);
                    }

                    if (name.equalsIgnoreCase("font-size")) {
                        if (model.getFont() == null) model.setFont(new StyleFontModel());
                        model.getFont().setSize(getSize(value));
                    }
                }
            }
            if (isItalic && isBold) {
                model.getFont().setStyle(Font.BOLD | Font.ITALIC);
            }
        } catch (IOException e) {
            Log.log("Exception in CssUtils.getStyleObjectModel", e);
        }

        return model;
    }

    private static Integer[] getMargins(CSSValueList list) {
        Integer[] result = new Integer[4];

        if (list.getLength() == 0) {
            CSSPrimitiveValue itemValue = (CSSPrimitiveValue) list;
            result[0] = getSize(itemValue);
            result[1] = getSize(itemValue);
            result[2] = getSize(itemValue);
            result[3] = getSize(itemValue);
        } else {
            for (int i = 0; i < list.getLength(); i++) {
                CSSPrimitiveValue itemValue = (CSSPrimitiveValue) list.item(i);
                if (itemValue.getPrimitiveType() == CSSPrimitiveValue.CSS_PX) {
                    result[i] = getSize(itemValue);
                }
            }
        }

        return result;
    }

    private static void castProperties(StyleObjectModel oldModel, StyleObjectModel newModel) {
        if (oldModel == null || newModel == null)
            return;

        oldModel.setWidth(newModel.getWidth());
        oldModel.setHeight(newModel.getHeight());

        for (int i = 0; i < 4; i++) {
            if (!newModel.isNullMargin(i)) {
                oldModel.setMargin(i, newModel.getMargin(i));
            }
        }

        if (newModel.getColor() != null) {
            oldModel.setColor(newModel.getColor());
        }

        if (newModel.getBackground() != null) {
            oldModel.setBackground(newModel.getBackground());
        }

        StyleFontModel font = newModel.getFont();
        if (font != null) {
            if (oldModel.getFont() == null) {
                oldModel.setFont(new StyleFontModel());
            }

            if (font.getSize() != null) {
                oldModel.getFont().setSize(font.getSize());
            }

            if (font.getName() != null) {
                oldModel.getFont().setName(font.getName());
            }

            if (font.getStyle() != null) {
                oldModel.getFont().setStyle(font.getStyle());
            }
        }
        StyleBorderModel border = newModel.getBorder();
        if (border != null) {
            if (oldModel.getBorder() == null) {
                oldModel.setBorder(new StyleBorderModel());
            }

            if (border.getWidth() != null) {
                oldModel.getBorder().setWidth(border.getWidth());
            }

            if (border.getStyle() != null) {
                oldModel.getBorder().setStyle(border.getStyle());
            }
            if (border.getColor() != null) {
                oldModel.getBorder().setColor(border.getColor());
            }
        }

    }

    private static float getAlpha(float value) {
        return value / 255;
    }

    private static Color getColor(CSSPrimitiveValue value) {
        Color color = null;
        if (value.getPrimitiveType() == CSSPrimitiveValue.CSS_RGBCOLOR) {
            RGBColor rgb = value.getRGBColorValue();
            float r = getAlpha(rgb.getRed().getFloatValue(CSSPrimitiveValue.CSS_NUMBER));
            float g = getAlpha(rgb.getGreen().getFloatValue(CSSPrimitiveValue.CSS_NUMBER));
            float b = getAlpha(rgb.getBlue().getFloatValue(CSSPrimitiveValue.CSS_NUMBER));
            //todo: workaround for 'JSFC-3671'
            if (r == 0 && g == 0 && b == 0)
                color = new Color(1, 1, 1);
            else
                //------------------------------------
                color = new Color(r, g, b);
        } else if (value.getPrimitiveType() == CSSPrimitiveValue.CSS_IDENT) {
            String colorName = value.getStringValue();
            color = getColorByName(colorName);
        }
        return color;
    }

    private static Color getColorByName(String colorName) {
        return NAMED_COLORS.get(colorName.toLowerCase());
    }

    private static String formatColorComponent(int colorComponent) {
        String str = Integer.toString(colorComponent, 16);
        if (str.length() < 2)
            str = "0" + str;
        return str;
    }

    public static String formatColor(Color color) {
        if (color == null)
            return null;
        return "#" +
                formatColorComponent(color.getRed()) +
                formatColorComponent(color.getGreen()) +
                formatColorComponent(color.getBlue());
    }

    public static Color parseColor(String cssColorStr) {
        if (cssColorStr == null)
            return null;
        cssColorStr = cssColorStr.trim();
        if (cssColorStr.length() == 0)
            return null;
        String colorStr = normalizeCssColor(cssColorStr);
        if (!colorStr.startsWith("#"))
            throw new IllegalArgumentException("Improperly formatted color string (it should be of form #rrggbb, e.g. #2080ff): " + cssColorStr);
        int r = Integer.parseInt(colorStr.substring(1, 3), 16);
        int g = Integer.parseInt(colorStr.substring(3, 5), 16);
        int b = Integer.parseInt(colorStr.substring(5, 7), 16);
        return new Color(r, g, b);
    }

    public static String normalizeCssColor(String colorStr) {
        if (colorStr == null)
            return null;
        colorStr = colorStr.trim();
        if (colorStr.startsWith("#"))
            return colorStr;
        Color color = getColorByName(colorStr);
        if (color == null)
            throw new IllegalArgumentException("Unknown HTML color string: " + colorStr);
        String result = formatColor(color);
        return result;
    }

    private static StyleBorderModel getBorder(CSSValueList value) {
        StyleBorderModel border = new StyleBorderModel();

        if (value.getLength() > 0) {
            for (int i = 0; i < value.getLength(); i++) {
                CSSPrimitiveValue itemValue = (CSSPrimitiveValue) value.item(i);
                if (itemValue.getPrimitiveType() == CSSPrimitiveValue.CSS_RGBCOLOR) {
                    border.setColor(getColor(itemValue));
                } else if (itemValue.getPrimitiveType() == CSSPrimitiveValue.CSS_PX) {
                    border.setWidth(getSize(itemValue));
                } else if (itemValue.getPrimitiveType() == CSSPrimitiveValue.CSS_IDENT) {
                    Color tryColor = getColor(itemValue);
                    if (tryColor != null) {
                        border.setColor(tryColor);
                    } else {
                        border.setStyle(itemValue.getStringValue());
                    }
                }
            }
        } else {
            CSSPrimitiveValue itemValue = (CSSPrimitiveValue) value;
            if (itemValue.getPrimitiveType() == CSSPrimitiveValue.CSS_RGBCOLOR) {
                border.setColor(getColor(itemValue));
            } else if (itemValue.getPrimitiveType() == CSSPrimitiveValue.CSS_PX) {
                border.setWidth(getSize(itemValue));
            } else if (itemValue.getPrimitiveType() == CSSPrimitiveValue.CSS_IDENT) {
                Color tryColor = getColor(itemValue);
                if (tryColor != null) {
                    border.setColor(tryColor);
                } else {
                    border.setStyle(itemValue.getStringValue());
                }
            }
        }

        return border;
    }

    private static StyleFontModel getFont(StyleObjectModel model, CSSValueList value) {
        StyleFontModel font = model.getFont();

        StringBuffer family = new StringBuffer();
        if (value.getLength() > 0) {
            for (int i = 0; i < value.getLength(); i++) {
                CSSPrimitiveValue itemValue = (CSSPrimitiveValue) value.item(i);
                if (itemValue.getPrimitiveType() == CSSPrimitiveValue.CSS_PT) {
                    font.setSize(getSize(itemValue));
                } else if (itemValue.getPrimitiveType() == 19) {
                    family.append((family.length() > 1) ? (",") : ("")).append(itemValue.getStringValue());
                }
            }
            if (family.length() != 0) {
                font.setName(family.toString());
            }
        } else {
            CSSPrimitiveValue itemValue = (CSSPrimitiveValue) value;
            if (itemValue.getPrimitiveType() == CSSPrimitiveValue.CSS_PT) {
                font.setSize(getSize(itemValue));
            } else if (itemValue.getPrimitiveType() == 19) {
                family.append(itemValue.getStringValue()).append(",");
            }

            if (family.length() != 0) {
                font.setName(family.toString());
            }

        }

        return font;
    }

    private static Integer getSize(CSSPrimitiveValue value) {
        Integer size = null;
        if (value.getPrimitiveType() == CSSPrimitiveValue.CSS_PX) {
            Float f = value.getFloatValue(CSSPrimitiveValue.CSS_PX);
            size = f.intValue();
        } else if (value.getPrimitiveType() == CSSPrimitiveValue.CSS_PT) {
            //todo: it is temporary fix to apply 'font-size' in 'pt', will work correctly only for 96 dpi. To convert 'px' to 'pt' properly we should get client dpi and than perform conversion.
            Float f = value.getFloatValue(CSSPrimitiveValue.CSS_PX) * 4 / 3;
            size = f.intValue();
        }
        return size;
    }

    private static HashMap<String, Color> getNamedColors() {
        HashMap<String, Color> namedColors = new HashMap<String, Color>();
        namedColors.put("sienna", new Color(160, 82, 45));
        namedColors.put("dodgerblue", new Color(30, 144, 255));
        namedColors.put("powderblue", new Color(176, 224, 230));
        namedColors.put("pink", new Color(255, 192, 203));
        namedColors.put("mediumaquamarine", new Color(102, 205, 170));
        namedColors.put("royalblue", new Color(65, 105, 225));
        namedColors.put("coral", new Color(255, 127, 80));
        namedColors.put("yellowgreen", new Color(154, 205, 50));
        namedColors.put("skyblue", new Color(135, 206, 235));
        namedColors.put("wheat", new Color(245, 222, 179));
        namedColors.put("lightsteelblue", new Color(176, 196, 222));
        namedColors.put("olivedrab", new Color(107, 142, 35));
        namedColors.put("darkred", new Color(139, 0, 0));
        namedColors.put("lightgreen", new Color(144, 238, 144));
        namedColors.put("lavenderblush", new Color(255, 240, 245));
        namedColors.put("floralwhite", new Color(255, 250, 240));
        namedColors.put("dimgray", new Color(105, 105, 105));
        namedColors.put("blue", new Color(0, 0, 255));
        namedColors.put("lightslategray", new Color(119, 136, 153));
        namedColors.put("violet", new Color(238, 130, 238));
        namedColors.put("purple", new Color(128, 0, 128));
        namedColors.put("chocolate", new Color(210, 105, 30));
        namedColors.put("limegreen", new Color(50, 205, 50));
        namedColors.put("plum", new Color(221, 160, 221));
        namedColors.put("mediumseagreen", new Color(60, 179, 113));
        namedColors.put("darkcyan", new Color(0, 139, 139));
        namedColors.put("paleturquoise", new Color(175, 238, 238));
        namedColors.put("hotpink", new Color(255, 105, 180));
        namedColors.put("midnightblue", new Color(25, 25, 112));
        namedColors.put("mediumspringgreen", new Color(0, 250, 154));
        namedColors.put("navy", new Color(0, 0, 128));
        namedColors.put("linen", new Color(250, 240, 230));
        namedColors.put("papayawhip", new Color(255, 239, 213));
        namedColors.put("green", new Color(0, 128, 0));
        namedColors.put("orange", new Color(255, 165, 0));
        namedColors.put("mediumvioletred", new Color(199, 21, 133));
        namedColors.put("mediumpurple", new Color(147, 112, 219));
        namedColors.put("beige", new Color(245, 245, 220));
        namedColors.put("orangered", new Color(255, 69, 0));
        namedColors.put("salmon", new Color(250, 128, 114));
        namedColors.put("deeppink", new Color(255, 20, 147));
        namedColors.put("white", new Color(255, 255, 255));
        namedColors.put("darkorange", new Color(255, 140, 0));
        namedColors.put("darkblue", new Color(0, 0, 139));
        namedColors.put("ivory", new Color(255, 255, 240));
        namedColors.put("chartreuse", new Color(127, 255, 0));
        namedColors.put("antiquewhite", new Color(250, 235, 215));
        namedColors.put("mistyrose", new Color(255, 228, 225));
        namedColors.put("oldlace", new Color(253, 245, 230));
        namedColors.put("darkgoldenrod", new Color(184, 134, 11));
        namedColors.put("maroon", new Color(128, 0, 0));
        namedColors.put("mediumslateblue", new Color(123, 104, 238));
        namedColors.put("orchid", new Color(218, 112, 214));
        namedColors.put("azure", new Color(240, 255, 255));
        namedColors.put("darkviolet", new Color(148, 0, 211));
        namedColors.put("darkorchid", new Color(153, 50, 204));
        namedColors.put("honeydew", new Color(240, 255, 240));
        namedColors.put("red", new Color(255, 0, 0));
        namedColors.put("cyan", new Color(0, 255, 255));
        namedColors.put("olive", new Color(128, 128, 0));
        namedColors.put("tan", new Color(210, 180, 140));
        namedColors.put("seagreen", new Color(46, 139, 87));
        namedColors.put("navajowhite", new Color(255, 222, 173));
        namedColors.put("darkkhaki", new Color(189, 183, 107));
        namedColors.put("mintcream", new Color(245, 255, 250));
        namedColors.put("mediumturquoise", new Color(72, 209, 204));
        namedColors.put("slateblue", new Color(106, 90, 205));
        namedColors.put("brown", new Color(165, 42, 42));
        namedColors.put("thistle", new Color(216, 191, 216));
        namedColors.put("palegoldenrod", new Color(238, 232, 170));
        namedColors.put("gray", new Color(128, 128, 128));
        namedColors.put("gold", new Color(255, 215, 0));
        namedColors.put("cadetblue", new Color(95, 158, 160));
        namedColors.put("darkgray", new Color(169, 169, 169));
        namedColors.put("ghostwhite", new Color(248, 248, 255));
        namedColors.put("lightgoldenrodyellow", new Color(250, 250, 210));
        namedColors.put("fuchsia", new Color(255, 0, 255));
        namedColors.put("forestgreen", new Color(34, 139, 34));
        namedColors.put("palevioletred", new Color(219, 112, 147));
        namedColors.put("aliceblue", new Color(240, 248, 255));
        namedColors.put("lightcoral", new Color(240, 128, 128));
        namedColors.put("burlywood", new Color(222, 184, 135));
        namedColors.put("darkolivegreen", new Color(85, 107, 47));
        namedColors.put("rosybrown", new Color(188, 143, 143));
        namedColors.put("lightsalmon", new Color(255, 160, 122));
        //todo: workaround for 'JSFC-3671'
        namedColors.put("black", new Color(1, 1, 1));
        namedColors.put("teal", new Color(0, 128, 128));
        namedColors.put("silver", new Color(192, 192, 192));
        namedColors.put("gainsboro", new Color(220, 220, 220));
        namedColors.put("blanchedalmond", new Color(255, 235, 205));
        namedColors.put("darksalmon", new Color(233, 150, 122));
        namedColors.put("cornsilk", new Color(255, 248, 220));
        namedColors.put("greenyellow", new Color(173, 255, 47));
        namedColors.put("lavender", new Color(230, 230, 250));
        namedColors.put("aqua", new Color(0, 255, 255));
        namedColors.put("springgreen", new Color(0, 255, 127));
        namedColors.put("seashel", new Color(255, 245, 238));
        namedColors.put("darkgreen", new Color(0, 100, 0));
        namedColors.put("yellow", new Color(255, 255, 0));
        namedColors.put("goldenrod", new Color(218, 165, 32));
        namedColors.put("indigo", new Color(75, 0, 130));
        namedColors.put("mediumblue", new Color(0, 0, 205));
        namedColors.put("lightgrey", new Color(211, 211, 211));
        namedColors.put("palegreen", new Color(152, 251, 152));
        namedColors.put("lemonchiffon", new Color(255, 250, 205));
        namedColors.put("darkseagreen", new Color(143, 188, 143));
        namedColors.put("snow", new Color(255, 250, 250));
        namedColors.put("whitesmoke", new Color(245, 245, 245));
        namedColors.put("turquoise", new Color(64, 224, 208));
        namedColors.put("slategray", new Color(112, 128, 144));
        namedColors.put("sandybrown", new Color(244, 164, 96));
        namedColors.put("mediumorchid", new Color(186, 85, 211));
        namedColors.put("indianred", new Color(205, 92, 92));
        namedColors.put("lawngreen", new Color(124, 252, 0));
        namedColors.put("darkslategray", new Color(47, 79, 79));
        namedColors.put("darkmagenta", new Color(139, 0, 139));
        namedColors.put("crimson", new Color(220, 20, 60));
        namedColors.put("peru", new Color(205, 133, 63));
        namedColors.put("lightblue", new Color(173, 216, 230));
        namedColors.put("moccasin", new Color(255, 228, 181));
        namedColors.put("lightcyan", new Color(224, 255, 255));
        namedColors.put("blueviolet", new Color(138, 43, 226));
        namedColors.put("cornflowerblue", new Color(100, 149, 237));
        namedColors.put("khaki", new Color(240, 230, 140));
        namedColors.put("firebrick", new Color(178, 34, 34));
        namedColors.put("lightyellow", new Color(255, 255, 224));
        namedColors.put("peachpuff", new Color(255, 218, 185));
        namedColors.put("lightskyblue", new Color(135, 206, 250));
        namedColors.put("lightpink", new Color(255, 182, 193));
        namedColors.put("lime", new Color(0, 255, 0));
        namedColors.put("tomato", new Color(255, 99, 71));
        namedColors.put("darkturquoise", new Color(0, 206, 209));
        namedColors.put("darkslateblue", new Color(72, 61, 139));
        namedColors.put("magenta", new Color(255, 0, 255));
        namedColors.put("lightseagreen", new Color(32, 178, 170));
        namedColors.put("aquamarine", new Color(127, 255, 212));
        namedColors.put("saddlebrown", new Color(139, 69, 19));
        namedColors.put("bisque", new Color(255, 228, 196));
        namedColors.put("steelblue", new Color(70, 130, 180));
        namedColors.put("deepskyblue", new Color(0, 191, 255));

        return namedColors;
    }


}

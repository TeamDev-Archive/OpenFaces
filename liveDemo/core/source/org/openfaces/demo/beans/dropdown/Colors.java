/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.dropdown;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Colors implements Serializable {
    private List<Color> colors = new ArrayList<Color>();

    public Colors() {
        colors.add(new Color("AliceBlue", 240, 248, 255, "#F0F8FF"));
        colors.add(new Color("AntiqueWhite", 250, 235, 215, "#FAEBD7"));
        colors.add(new Color("Aqua", 0, 255, 255, "#00FFFF"));
        colors.add(new Color("Aquamarine", 127, 255, 212, "#7FFFD4"));
        colors.add(new Color("Azure", 240, 255, 255, "#F0FFFF"));
        colors.add(new Color("Beige", 245, 245, 220, "#F5F5DC"));
        colors.add(new Color("Bisque", 255, 228, 196, "#FFE4C4"));
        colors.add(new Color("Black", 0, 0, 0, "#000000"));
        colors.add(new Color("BlanchedAlmond", 255, 235, 205, "#FFEBCD"));
        colors.add(new Color("Blue", 0, 0, 255, "#0000FF"));
        colors.add(new Color("BlueViolet", 138, 43, 226, "#8A2BE2"));
        colors.add(new Color("Brown", 165, 42, 42, "#A52A2A"));
        colors.add(new Color("BurlyWood", 222, 184, 135, "#DEB887"));
        colors.add(new Color("CadetBlue", 95, 158, 160, "#5F9EA0"));
        colors.add(new Color("Chartreuse", 127, 255, 0, "#7FFF00"));
        colors.add(new Color("Chocolate", 210, 105, 30, "#D2691E"));
        colors.add(new Color("Coral", 255, 127, 80, "#FF7F50"));
        colors.add(new Color("CornflowerBlue", 100, 149, 237, "#6495ED"));
        colors.add(new Color("Cornsilk", 255, 248, 220, "#FFF8DC"));
        colors.add(new Color("Crimson", 220, 20, 60, "#DC143C"));
        colors.add(new Color("Cyan", 0, 255, 255, "#00FFFF"));
        colors.add(new Color("DarkBlue", 0, 0, 139, "#00008B"));
        colors.add(new Color("DarkCyan", 0, 139, 139, "#008B8B"));
        colors.add(new Color("DarkGoldenrod", 184, 134, 11, "#B8860B"));
        colors.add(new Color("DarkGray", 169, 169, 169, "#A9A9A9"));
        colors.add(new Color("DarkGreen", 0, 100, 0, "#006400"));
        colors.add(new Color("DarkKhaki", 189, 183, 107, "#BDB76B"));
        colors.add(new Color("DarkMagenta", 139, 0, 139, "#8B008B"));
        colors.add(new Color("DarkOliveGreen", 85, 107, 47, "#556B2F"));
        colors.add(new Color("DarkOrange", 255, 140, 0, "#FF8C00"));
        colors.add(new Color("DarkOrchid", 153, 50, 204, "#9932CC"));
        colors.add(new Color("DarkRed", 139, 0, 0, "#8B0000"));
        colors.add(new Color("DarkSalmon", 233, 150, 122, "#E9967A"));
        colors.add(new Color("DarkSeaGreen", 143, 188, 143, "#8FBC8F"));
        colors.add(new Color("DarkSlateBlue", 72, 61, 139, "#483D8B"));
        colors.add(new Color("DarkSlateGray", 47, 79, 79, "#2F4F4F"));
        colors.add(new Color("DarkTurquoise", 0, 206, 209, "#00CED1"));
        colors.add(new Color("DarkViolet", 148, 0, 211, "#9400D3"));
        colors.add(new Color("DeepPink", 255, 20, 147, "#FF1493"));
        colors.add(new Color("DeepSkyBlue", 0, 191, 255, "#00BFFF"));
        colors.add(new Color("DimGray", 105, 105, 105, "#696969"));
        colors.add(new Color("DodgerBlue", 30, 144, 255, "#1E90FF"));
        colors.add(new Color("FireBrick", 178, 34, 34, "#B22222"));
        colors.add(new Color("FloralWhite", 255, 250, 240, "#FFFAF0"));
        colors.add(new Color("ForestGreen", 34, 139, 34, "#228B22"));
        colors.add(new Color("Fuchsia", 255, 0, 255, "#FF00FF"));
        colors.add(new Color("Gainsboro", 220, 220, 220, "#DCDCDC"));
        colors.add(new Color("GhostWhite", 248, 248, 255, "#F8F8FF"));
        colors.add(new Color("Gold", 255, 215, 0, "#FFD700"));
        colors.add(new Color("Goldenrod", 218, 165, 32, "#DAA520"));
        colors.add(new Color("Gray", 128, 128, 128, "#808080"));
        colors.add(new Color("Green", 0, 128, 0, "#008000"));
        colors.add(new Color("GreenYellow", 173, 255, 47, "#ADFF2F"));
        colors.add(new Color("Honeydew", 240, 255, 240, "#F0FFF0"));
        colors.add(new Color("HotPink", 255, 105, 180, "#FF69B4"));
        colors.add(new Color("IndianRed", 205, 92, 92, "#CD5C5C"));
        colors.add(new Color("Indigo", 75, 0, 130, "#4B0082"));
        colors.add(new Color("Ivory", 255, 255, 240, "#FFFFF0"));
        colors.add(new Color("Khaki", 240, 230, 140, "#F0E68C"));
        colors.add(new Color("Lavender", 230, 230, 250, "#E6E6FA"));
        colors.add(new Color("LavenderBlush", 255, 240, 245, "#FFF0F5"));
        colors.add(new Color("LawnGreen", 124, 252, 0, "#7CFC00"));
        colors.add(new Color("LemonChiffon", 255, 250, 205, "#FFFACD"));
        colors.add(new Color("LightBlue", 173, 216, 230, "#ADD8E6"));
        colors.add(new Color("LightCoral", 240, 128, 128, "#F08080"));
        colors.add(new Color("LightCyan", 224, 255, 255, "#E0FFFF"));
        colors.add(new Color("LightGoldenrodYellow", 250, 250, 210, "#FAFAD2"));
        colors.add(new Color("LightGreen", 144, 238, 144, "#90EE90"));
        colors.add(new Color("LightGrey", 211, 211, 211, "#D3D3D3"));
        colors.add(new Color("LightPink", 255, 182, 193, "#FFB6C1"));
        colors.add(new Color("LightSalmon", 255, 160, 122, "#FFA07A"));
        colors.add(new Color("LightSeaGreen", 32, 178, 170, "#20B2AA"));
        colors.add(new Color("LightSkyBlue", 135, 206, 250, "#87CEFA"));
        colors.add(new Color("LightSlateGray", 119, 136, 153, "#778899"));
        colors.add(new Color("LightSteelBlue", 176, 196, 222, "#B0C4DE"));
        colors.add(new Color("LightYellow", 255, 255, 224, "#FFFFE0"));
        colors.add(new Color("Lime", 0, 255, 0, "#00FF00"));
        colors.add(new Color("LimeGreen", 50, 205, 50, "#32CD32"));
        colors.add(new Color("Linen", 250, 240, 230, "#FAF0E6"));
        colors.add(new Color("Magenta", 255, 0, 255, "#FF00FF"));
        colors.add(new Color("Maroon", 128, 0, 0, "#800000"));
        colors.add(new Color("MediumAquamarine", 102, 205, 170, "#66CDAA"));
        colors.add(new Color("MediumBlue", 0, 0, 205, "#0000CD"));
        colors.add(new Color("MediumOrchid", 186, 85, 211, "#BA55D3"));
        colors.add(new Color("MediumPurple", 147, 112, 219, "#9370DB"));
        colors.add(new Color("MediumSeaGreen", 60, 179, 113, "#3CB371"));
        colors.add(new Color("MediumSlateBlue", 123, 104, 238, "#7B68EE"));
        colors.add(new Color("MediumSpringGreen", 0, 250, 154, "#00FA9A"));
        colors.add(new Color("MediumTurquoise", 72, 209, 204, "#48D1CC"));
        colors.add(new Color("MediumVioletRed", 199, 21, 133, "#C71585"));
        colors.add(new Color("MidnightBlue", 25, 25, 112, "#191970"));
        colors.add(new Color("MintCream", 245, 255, 250, "#F5FFFA"));
        colors.add(new Color("MistyRose", 255, 228, 225, "#FFE4E1"));
        colors.add(new Color("Moccasin", 255, 228, 181, "#FFE4B5"));
        colors.add(new Color("NavajoWhite", 255, 222, 173, "#FFDEAD"));
        colors.add(new Color("Navy", 0, 0, 128, "#000080"));
        colors.add(new Color("OldLace", 253, 245, 230, "#FDF5E6"));
        colors.add(new Color("Olive", 128, 128, 0, "#808000"));
        colors.add(new Color("OliveDrab", 107, 142, 35, "#6B8E23"));
        colors.add(new Color("Orange", 255, 165, 0, "#FFA500"));
        colors.add(new Color("OrangeRed", 255, 69, 0, "#FF4500"));
        colors.add(new Color("Orchid", 218, 112, 214, "#DA70D6"));
        colors.add(new Color("PaleGoldenrod", 238, 232, 170, "#EEE8AA"));
        colors.add(new Color("PaleGreen", 152, 251, 152, "#98FB98"));
        colors.add(new Color("PaleTurquoise", 175, 238, 238, "#AFEEEE"));
        colors.add(new Color("PaleVioletRed", 219, 112, 147, "#DB7093"));
        colors.add(new Color("PapayaWhip", 255, 239, 213, "#FFEFD5"));
        colors.add(new Color("PeachPuff", 255, 218, 185, "#FFDAB9"));
        colors.add(new Color("Peru", 205, 133, 63, "#CD853F"));
        colors.add(new Color("Pink", 255, 192, 203, "#FFC0CB"));
        colors.add(new Color("Plum", 221, 160, 221, "#DDA0DD"));
        colors.add(new Color("PowderBlue", 176, 224, 230, "#B0E0E6"));
        colors.add(new Color("Purple", 128, 0, 128, "#800080"));
        colors.add(new Color("Red", 255, 0, 0, "#FF0000"));
        colors.add(new Color("RosyBrown", 188, 143, 143, "#BC8F8F"));
        colors.add(new Color("RoyalBlue", 65, 105, 225, "#4169E1"));
        colors.add(new Color("SaddleBrown", 139, 69, 19, "#8B4513"));
        colors.add(new Color("Salmon", 250, 128, 114, "#FA8072"));
        colors.add(new Color("SandyBrown", 244, 164, 96, "#F4A460"));
        colors.add(new Color("SeaGreen", 46, 139, 87, "#2E8B57"));
        colors.add(new Color("Seashel", 255, 245, 238, "#FFF5EE"));
        colors.add(new Color("Sienna", 160, 82, 45, "#A0522D"));
        colors.add(new Color("Silver", 192, 192, 192, "#C0C0C0"));
        colors.add(new Color("SkyBlue", 135, 206, 235, "#87CEEB"));
        colors.add(new Color("SlateBlue", 106, 90, 205, "#6A5ACD"));
        colors.add(new Color("SlateGray", 112, 128, 144, "#708090"));
        colors.add(new Color("Snow", 255, 250, 250, "#FFFAFA"));
        colors.add(new Color("SpringGreen", 0, 255, 127, "#00FF7F"));
        colors.add(new Color("SteelBlue", 70, 130, 180, "#4682B4"));
        colors.add(new Color("Tan", 210, 180, 140, "#D2B48C"));
        colors.add(new Color("Teal", 0, 128, 128, "#008080"));
        colors.add(new Color("Thistle", 216, 191, 216, "#D8BFD8"));
        colors.add(new Color("Tomato", 255, 99, 71, "#FF6347"));
        colors.add(new Color("Turquoise", 64, 224, 208, "#40E0D0"));
        colors.add(new Color("Violet", 238, 130, 238, "#EE82EE"));
        colors.add(new Color("Wheat", 245, 222, 179, "#F5DEB3"));
        colors.add(new Color("White", 255, 255, 255, "#FFFFFF"));
        colors.add(new Color("WhiteSmoke", 245, 245, 245, "#F5F5F5"));
        colors.add(new Color("Yellow", 255, 255, 0, "#FFFF00"));
        colors.add(new Color("YellowGreen", 154, 205, 50, "#9ACD32"));
    }

    public List<Color> getColors() {
        return colors;
    }

    public Color getColorByText(String text) {
        if (text == null)
            return null;
        text = text.trim();
        for (Color color : colors) {
            if (text.equalsIgnoreCase(color.getName()) ||
                    text.equalsIgnoreCase(color.getHex()))
                return color;
        }

        if (!text.startsWith("#"))
            return null;
        return new Color(text, 0, 0, 0, text);
    }
}

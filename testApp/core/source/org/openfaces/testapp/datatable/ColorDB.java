/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.datatable;

import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.CompositeFilterCriterion;
import org.openfaces.component.filter.ExpressionFilterCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ColorDB {

    private List<Color> colors = new ArrayList<Color>();
    private List<String> queries = new ArrayList<String>();

    public ColorDB() {
        colors.add(new Color(0, "AliceBlue", 240, 248, 255, "#F0F8FF"));
        colors.add(new Color(1, "AntiqueWhite", 250, 235, 215, "#FAEBD7"));
        colors.add(new Color(2, "Aqua", 0, 255, 255, "#00FFFF"));
        colors.add(new Color(3, "Aquamarine", 127, 255, 212, "#7FFFD4"));
        colors.add(new Color(4, "Azure", 240, 255, 255, "#F0FFFF"));
        colors.add(new Color(5, "Beige", 245, 245, 220, "#F5F5DC"));
        colors.add(new Color(6, "Bisque", 255, 228, 196, "#FFE4C4"));
        colors.add(new Color(7, "Black", 0, 0, 0, "#000000"));
        colors.add(new Color(8, "BlanchedAlmond", 255, 235, 205, "#FFEBCD"));
        colors.add(new Color(9, "Blue", 0, 0, 255, "#0000FF"));
        colors.add(new Color(10, "BlueViolet", 138, 43, 226, "#8A2BE2"));
        colors.add(new Color(11, "Brown", 165, 42, 42, "#A52A2A"));
        colors.add(new Color(12, "BurlyWood", 222, 184, 135, "#DEB887"));
        colors.add(new Color(13, "CadetBlue", 95, 158, 160, "#5F9EA0"));
        colors.add(new Color(14, "Chartreuse", 127, 255, 0, "#7FFF00"));
        colors.add(new Color(15, "Chocolate", 210, 105, 30, "#D2691E"));
        colors.add(new Color(16, "Coral", 255, 127, 80, "#FF7F50"));
        colors.add(new Color(17, "CornflowerBlue", 100, 149, 237, "#6495ED"));
        colors.add(new Color(18, "Cornsilk", 255, 248, 220, "#FFF8DC"));
        colors.add(new Color(19, "Crimson", 220, 20, 60, "#DC143C"));
        colors.add(new Color(20, "Cyan", 0, 255, 255, "#00FFFF"));
        colors.add(new Color(21, "DarkBlue", 0, 0, 139, "#00008B"));
        colors.add(new Color(22, "DarkCyan", 0, 139, 139, "#008B8B"));
        colors.add(new Color(23, "DarkGoldenrod", 184, 134, 11, "#B8860B"));
        colors.add(new Color(24, "DarkGray", 169, 169, 169, "#A9A9A9"));
        colors.add(new Color(25, "DarkGreen", 0, 100, 0, "#006400"));
        colors.add(new Color(26, "DarkKhaki", 189, 183, 107, "#BDB76B"));
        colors.add(new Color(27, "DarkMagenta", 139, 0, 139, "#8B008B"));
        colors.add(new Color(28, "DarkOliveGreen", 85, 107, 47, "#556B2F"));
        colors.add(new Color(29, "DarkOrange", 255, 140, 0, "#FF8C00"));
        colors.add(new Color(30, "DarkOrchid", 153, 50, 204, "#9932CC"));
        colors.add(new Color(31, "DarkRed", 139, 0, 0, "#8B0000"));
        colors.add(new Color(32, "DarkSalmon", 233, 150, 122, "#E9967A"));
        colors.add(new Color(33, "DarkSeaGreen", 143, 188, 143, "#8FBC8F"));
        colors.add(new Color(34, "DarkSlateBlue", 72, 61, 139, "#483D8B"));
        colors.add(new Color(35, "DarkSlateGray", 47, 79, 79, "#2F4F4F"));
        colors.add(new Color(36, "DarkTurquoise", 0, 206, 209, "#00CED1"));
        colors.add(new Color(37, "DarkViolet", 148, 0, 211, "#9400D3"));
        colors.add(new Color(38, "DeepPink", 255, 20, 147, "#FF1493"));
        colors.add(new Color(39, "DeepSkyBlue", 0, 191, 255, "#00BFFF"));
        colors.add(new Color(40, "DimGray", 105, 105, 105, "#696969"));
        colors.add(new Color(41, "DodgerBlue", 30, 144, 255, "#1E90FF"));
        colors.add(new Color(42, "FireBrick", 178, 34, 34, "#B22222"));
        colors.add(new Color(43, "FloralWhite", 255, 250, 240, "#FFFAF0"));
        colors.add(new Color(44, "ForestGreen", 34, 139, 34, "#228B22"));
        colors.add(new Color(45, "Fuchsia", 255, 0, 255, "#FF00FF"));
        colors.add(new Color(46, "Gainsboro", 220, 220, 220, "#DCDCDC"));
        colors.add(new Color(47, "GhostWhite", 248, 248, 255, "#F8F8FF"));
        colors.add(new Color(48, "Gold", 255, 215, 0, "#FFD700"));
        colors.add(new Color(49, "Goldenrod", 218, 165, 32, "#DAA520"));
        colors.add(new Color(50, "Gray", 128, 128, 128, "#808080"));
        colors.add(new Color(51, "Green", 0, 128, 0, "#008000"));
        colors.add(new Color(52, "GreenYellow", 173, 255, 47, "#ADFF2F"));
        colors.add(new Color(53, "Honeydew", 240, 255, 240, "#F0FFF0"));
        colors.add(new Color(54, "HotPink", 255, 105, 180, "#FF69B4"));
        colors.add(new Color(55, "IndianRed", 205, 92, 92, "#CD5C5C"));
        colors.add(new Color(56, "Indigo", 75, 0, 130, "#4B0082"));
        colors.add(new Color(57, "Ivory", 255, 255, 240, "#FFFFF0"));
        colors.add(new Color(58, "Khaki", 240, 230, 140, "#F0E68C"));
        colors.add(new Color(59, "Lavender", 230, 230, 250, "#E6E6FA"));
        colors.add(new Color(60, "LavenderBlush", 255, 240, 245, "#FFF0F5"));
        colors.add(new Color(61, "LawnGreen", 124, 252, 0, "#7CFC00"));
        colors.add(new Color(62, "LemonChiffon", 255, 250, 205, "#FFFACD"));
        colors.add(new Color(63, "LightBlue", 173, 216, 230, "#ADD8E6"));
        colors.add(new Color(64, "LightCoral", 240, 128, 128, "#F08080"));
        colors.add(new Color(65, "LightCyan", 224, 255, 255, "#E0FFFF"));
        colors.add(new Color(66, "LightGoldenrodYellow", 250, 250, 210, "#FAFAD2"));
        colors.add(new Color(67, "LightGreen", 144, 238, 144, "#90EE90"));
        colors.add(new Color(68, "LightGrey", 211, 211, 211, "#D3D3D3"));
        colors.add(new Color(69, "LightPink", 255, 182, 193, "#FFB6C1"));
        colors.add(new Color(70, "LightSalmon", 255, 160, 122, "#FFA07A"));
        colors.add(new Color(71, "LightSeaGreen", 32, 178, 170, "#20B2AA"));
        colors.add(new Color(72, "LightSkyBlue", 135, 206, 250, "#87CEFA"));
        colors.add(new Color(73, "LightSlateGray", 119, 136, 153, "#778899"));
        colors.add(new Color(74, "LightSteelBlue", 176, 196, 222, "#B0C4DE"));
        colors.add(new Color(75, "LightYellow", 255, 255, 224, "#FFFFE0"));
        colors.add(new Color(76, "Lime", 0, 255, 0, "#00FF00"));
        colors.add(new Color(77, "LimeGreen", 50, 205, 50, "#32CD32"));
        colors.add(new Color(78, "Linen", 250, 240, 230, "#FAF0E6"));
        colors.add(new Color(79, "Magenta", 255, 0, 255, "#FF00FF"));
        colors.add(new Color(80, "Maroon", 128, 0, 0, "#800000"));
        colors.add(new Color(81, "MediumAquamarine", 102, 205, 170, "#66CDAA"));
        colors.add(new Color(82, "MediumBlue", 0, 0, 205, "#0000CD"));
        colors.add(new Color(83, "MediumOrchid", 186, 85, 211, "#BA55D3"));
        colors.add(new Color(84, "MediumPurple", 147, 112, 219, "#9370DB"));
        colors.add(new Color(85, "MediumSeaGreen", 60, 179, 113, "#3CB371"));
        colors.add(new Color(86, "MediumSlateBlue", 123, 104, 238, "#7B68EE"));
        colors.add(new Color(87, "MediumSpringGreen", 0, 250, 154, "#00FA9A"));
        colors.add(new Color(88, "MediumTurquoise", 72, 209, 204, "#48D1CC"));
        colors.add(new Color(89, "MediumVioletRed", 199, 21, 133, "#C71585"));
        colors.add(new Color(90, "MidnightBlue", 25, 25, 112, "#191970"));
        colors.add(new Color(91, "MintCream", 245, 255, 250, "#F5FFFA"));
        colors.add(new Color(92, "MistyRose", 255, 228, 225, "#FFE4E1"));
        colors.add(new Color(93, "Moccasin", 255, 228, 181, "#FFE4B5"));
        colors.add(new Color(94, "NavajoWhite", 255, 222, 173, "#FFDEAD"));
        colors.add(new Color(95, "Navy", 0, 0, 128, "#000080"));
        colors.add(new Color(96, "OldLace", 253, 245, 230, "#FDF5E6"));
        colors.add(new Color(97, "Olive", 128, 128, 0, "#808000"));
        colors.add(new Color(98, "OliveDrab", 107, 142, 35, "#6B8E23"));
        colors.add(new Color(99, "Orange", 255, 165, 0, "#FFA500"));
        colors.add(new Color(100, "OrangeRed", 255, 69, 0, "#FF4500"));
        colors.add(new Color(101, "Orchid", 218, 112, 214, "#DA70D6"));
        colors.add(new Color(102, "PaleGoldenrod", 238, 232, 170, "#EEE8AA"));
        colors.add(new Color(103, "PaleGreen", 152, 251, 152, "#98FB98"));
        colors.add(new Color(104, "PaleTurquoise", 175, 238, 238, "#AFEEEE"));
        colors.add(new Color(105, "PaleVioletRed", 219, 112, 147, "#DB7093"));
        colors.add(new Color(106, "PapayaWhip", 255, 239, 213, "#FFEFD5"));
        colors.add(new Color(107, "PeachPuff", 255, 218, 185, "#FFDAB9"));
        colors.add(new Color(108, "Peru", 205, 133, 63, "#CD853F"));
        colors.add(new Color(109, "Pink", 255, 192, 203, "#FFC0CB"));
        colors.add(new Color(110, "Plum", 221, 160, 221, "#DDA0DD"));
        colors.add(new Color(111, "PowderBlue", 176, 224, 230, "#B0E0E6"));
        colors.add(new Color(112, "Purple", 128, 0, 128, "#800080"));
        colors.add(new Color(113, "Red", 255, 0, 0, "#FF0000"));
        colors.add(new Color(114, "RosyBrown", 188, 143, 143, "#BC8F8F"));
        colors.add(new Color(115, "RoyalBlue", 65, 105, 225, "#4169E1"));
        colors.add(new Color(116, "SaddleBrown", 139, 69, 19, "#8B4513"));
        colors.add(new Color(117, "Salmon", 250, 128, 114, "#FA8072"));
        colors.add(new Color(118, "SandyBrown", 244, 164, 96, "#F4A460"));
        colors.add(new Color(119, "SeaGreen", 46, 139, 87, "#2E8B57"));
        colors.add(new Color(120, "Seashel", 255, 245, 238, "#FFF5EE"));
        colors.add(new Color(121, "Sienna", 160, 82, 45, "#A0522D"));
        colors.add(new Color(122, "Silver", 192, 192, 192, "#C0C0C0"));
        colors.add(new Color(123, "SkyBlue", 135, 206, 235, "#87CEEB"));
        colors.add(new Color(124, "SlateBlue", 106, 90, 205, "#6A5ACD"));
        colors.add(new Color(125, "SlateGray", 112, 128, 144, "#708090"));
        colors.add(new Color(126, "Snow", 255, 250, 250, "#FFFAFA"));
        colors.add(new Color(127, "SpringGreen", 0, 255, 127, "#00FF7F"));
        colors.add(new Color(128, "SteelBlue", 70, 130, 180, "#4682B4"));
        colors.add(new Color(129, "Tan", 210, 180, 140, "#D2B48C"));
        colors.add(new Color(130, "Teal", 0, 128, 128, "#008080"));
        colors.add(new Color(131, "Thistle", 216, 191, 216, "#D8BFD8"));
        colors.add(new Color(132, "Tomato", 255, 99, 71, "#FF6347"));
        colors.add(new Color(133, "Turquoise", 64, 224, 208, "#40E0D0"));
        colors.add(new Color(134, "Violet", 238, 130, 238, "#EE82EE"));
        colors.add(new Color(135, "Wheat", 245, 222, 179, "#F5DEB3"));
        colors.add(new Color(136, "White", 255, 255, 255, "#FFFFFF"));
        colors.add(new Color(137, "WhiteSmoke", 245, 245, 245, "#F5F5F5"));
        colors.add(new Color(138, "Yellow", 255, 255, 0, "#FFFF00"));
        colors.add(new Color(139, "YellowGreen", 154, 205, 50, "#9ACD32"));
    }

    public int getFilteredColorCount(CompositeFilterCriterion filterCriteria) {
        List<Color> filteredCollection = filterRows(colors, filterCriteria);
        queries.add("Calculate filtered collection size: " + getFilterCriteriaQueryText(filterCriteria));
        return filteredCollection.size();
    }

    private String getFilterCriteriaQueryText(CompositeFilterCriterion filterCriteria) {
        StringBuffer buf = new StringBuffer();
        for (Iterator<FilterCriterion> it = filterCriteria.getCriteria().iterator(); it.hasNext();) {
            FilterCriterion c = it.next();
            ExpressionFilterCriterion criterion = (ExpressionFilterCriterion) c;
            String filterId = criterion.getExpressionStr();

            String text = criterion.getArg1().toString();
            String criterionText = filterId + " CONTAINS \"" + text + "\"";
            buf.append(criterionText);
            if (it.hasNext())
                buf.append(" AND ");
        }
        return buf.toString();
    }

    public Color getColorByKey(int key) {
        for (Color currentColor : colors) {
            if (currentColor.getId() == key)
                return currentColor;
        }
        queries.add("Find row data by key: key=" + key);
        return null;
    }

    public List findColorsForPage(CompositeFilterCriterion filterCriteria, final String sortColumnID, boolean sortAscending,
                                  int pageStart, int pageSize) {
        List filteredCollection = filterRows(colors, filterCriteria);
        if (sortColumnID != null)
            Collections.sort(filteredCollection, new Comparator<Color>() {
                public int compare(Color c1, Color c2) {
                    Comparable value1;
                    Comparable value2;
                    if ("name".equals(sortColumnID)) {
                        value1 = c1.getName();
                        value2 = c2.getName();
                    } else if ("red".equals(sortColumnID)) {
                        value1 = c1.getR();
                        value2 = c2.getR();
                    } else if ("green".equals(sortColumnID)) {
                        value1 = c1.getG();
                        value2 = c2.getG();
                    } else if ("blue".equals(sortColumnID)) {
                        value1 = c1.getB();
                        value2 = c2.getB();
                    } else if ("hex".equals(sortColumnID)) {
                        value1 = c1.getHex();
                        value2 = c2.getHex();
                    } else {
                        throw new IllegalStateException("Unknown columnID: " + sortColumnID);
                    }
                    return value1.compareTo(value2);
                }
            });
        if (sortAscending) {
            Collections.reverse(filteredCollection);
        }
        List resultingCollection = filteredCollection.subList(pageStart, pageStart + pageSize);
        String filterExpression = "";
        String filterText = getFilterCriteriaQueryText(filterCriteria);
        if (filterText.length() != 0) {
            filterExpression = filterText + " AND ";
        }
        queries.add("Load row datas for page: " + filterExpression + "sortColumnID=\"" + sortColumnID +
                "\" AND sortAscending=" + sortAscending + " AND pageStart=" + pageStart + " AND pageSize=" + pageSize);
        return resultingCollection;
    }

    private static List<Color> filterRows(List<Color> colorsToFilter, CompositeFilterCriterion criteria) {
        if (criteria == null) {
            return new ArrayList<Color>(colorsToFilter);
        }
        List<Color> filtered = new ArrayList<Color>();
        for (Color tempColor : colorsToFilter) {
            boolean colorAccepted = true;
            for (FilterCriterion c: criteria.getCriteria()) {
                ExpressionFilterCriterion criterion = (ExpressionFilterCriterion) c;
                String criterionUC = criterion.getArg1().toString().toUpperCase();
                String colorNameUC = tempColor.getName().toUpperCase();
                if (colorNameUC.indexOf(criterionUC) == -1) {
                    colorAccepted = false;
                }
            }
            if (colorAccepted) {
                filtered.add(tempColor);
            }
        }
        return filtered;
    }

    public List<String> getQueries() {
        return queries;
    }

    public Color getColorByText(String text) {
        if (text == null) {
            return null;
        }
        text = text.trim();
        for (Color color : colors) {
            if (text.equalsIgnoreCase(color.getName()) || text.equalsIgnoreCase(color.getHex())) {
                return color;
            }
        }

        if (!text.startsWith("#")) {
            return null;
        }
        return new Color(text, 0, 0, 0, text);
    }

    public List<Color> getColors() {
        return colors;
    }
}

/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.portlets;

import org.openfaces.util.FacesUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DropDownBean_Portlets {
  private List myColors = new ArrayList();
  private List myPlants = new ArrayList();

  public DropDownBean_Portlets() {
    try {
      InputStream resource = DropDownBean_Portlets.class.getResourceAsStream("houseplants.txt");
      BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
      String currentString;
      while (true) {
        currentString = reader.readLine();
        if (currentString == null) break;
        myPlants.add(new String(currentString.getBytes(), "UTF-8"));
      }
      reader.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


    myColors.add(new Color("AliceBlue", 240, 248, 255, "#F0F8FF"));
    myColors.add(new Color("AntiqueWhite", 250, 235, 215, "#FAEBD7"));
    myColors.add(new Color("Aqua", 0, 255, 255, "#00FFFF"));
    myColors.add(new Color("Aquamarine", 127, 255, 212, "#7FFFD4"));
    myColors.add(new Color("Azure", 240, 255, 255, "#F0FFFF"));
    myColors.add(new Color("Beige", 245, 245, 220, "#F5F5DC"));
    myColors.add(new Color("Bisque", 255, 228, 196, "#FFE4C4"));
    myColors.add(new Color("Black", 0, 0, 0, "#000000"));
    myColors.add(new Color("BlanchedAlmond", 255, 235, 205, "#FFEBCD"));
    myColors.add(new Color("Blue", 0, 0, 255, "#0000FF"));
    myColors.add(new Color("BlueViolet", 138, 43, 226, "#8A2BE2"));
    myColors.add(new Color("Brown", 165, 42, 42, "#A52A2A"));
    myColors.add(new Color("BurlyWood", 222, 184, 135, "#DEB887"));
    myColors.add(new Color("CadetBlue", 95, 158, 160, "#5F9EA0"));
    myColors.add(new Color("Chartreuse", 127, 255, 0, "#7FFF00"));
    myColors.add(new Color("Chocolate", 210, 105, 30, "#D2691E"));
    myColors.add(new Color("Coral", 255, 127, 80, "#FF7F50"));
    myColors.add(new Color("CornflowerBlue", 100, 149, 237, "#6495ED"));
    myColors.add(new Color("Cornsilk", 255, 248, 220, "#FFF8DC"));
    myColors.add(new Color("Crimson", 220, 20, 60, "#DC143C"));
    myColors.add(new Color("Cyan", 0, 255, 255, "#00FFFF"));
    myColors.add(new Color("DarkBlue", 0, 0, 139, "#00008B"));
    myColors.add(new Color("DarkCyan", 0, 139, 139, "#008B8B"));
    myColors.add(new Color("DarkGoldenrod", 184, 134, 11, "#B8860B"));
    myColors.add(new Color("DarkGray", 169, 169, 169, "#A9A9A9"));
    myColors.add(new Color("DarkGreen", 0, 100, 0, "#006400"));
    myColors.add(new Color("DarkKhaki", 189, 183, 107, "#BDB76B"));
    myColors.add(new Color("DarkMagenta", 139, 0, 139, "#8B008B"));
    myColors.add(new Color("DarkOliveGreen", 85, 107, 47, "#556B2F"));
    myColors.add(new Color("DarkOrange", 255, 140, 0, "#FF8C00"));
    myColors.add(new Color("DarkOrchid", 153, 50, 204, "#9932CC"));
    myColors.add(new Color("DarkRed", 139, 0, 0, "#8B0000"));
    myColors.add(new Color("DarkSalmon", 233, 150, 122, "#E9967A"));
    myColors.add(new Color("DarkSeaGreen", 143, 188, 143, "#8FBC8F"));
    myColors.add(new Color("DarkSlateBlue", 72, 61, 139, "#483D8B"));
    myColors.add(new Color("DarkSlateGray", 47, 79, 79, "#2F4F4F"));
    myColors.add(new Color("DarkTurquoise", 0, 206, 209, "#00CED1"));
    myColors.add(new Color("DarkViolet", 148, 0, 211, "#9400D3"));
    myColors.add(new Color("DeepPink", 255, 20, 147, "#FF1493"));
    myColors.add(new Color("DeepSkyBlue", 0, 191, 255, "#00BFFF"));
    myColors.add(new Color("DimGray", 105, 105, 105, "#696969"));
    myColors.add(new Color("DodgerBlue", 30, 144, 255, "#1E90FF"));
    myColors.add(new Color("FireBrick", 178, 34, 34, "#B22222"));
    myColors.add(new Color("FloralWhite", 255, 250, 240, "#FFFAF0"));
    myColors.add(new Color("ForestGreen", 34, 139, 34, "#228B22"));
    myColors.add(new Color("Fuchsia", 255, 0, 255, "#FF00FF"));
    myColors.add(new Color("Gainsboro", 220, 220, 220, "#DCDCDC"));
    myColors.add(new Color("GhostWhite", 248, 248, 255, "#F8F8FF"));
    myColors.add(new Color("Gold", 255, 215, 0, "#FFD700"));
    myColors.add(new Color("Goldenrod", 218, 165, 32, "#DAA520"));
    myColors.add(new Color("Gray", 128, 128, 128, "#808080"));
    myColors.add(new Color("Green", 0, 128, 0, "#008000"));
    myColors.add(new Color("GreenYellow", 173, 255, 47, "#ADFF2F"));
    myColors.add(new Color("Honeydew", 240, 255, 240, "#F0FFF0"));
    myColors.add(new Color("HotPink", 255, 105, 180, "#FF69B4"));
    myColors.add(new Color("IndianRed", 205, 92, 92, "#CD5C5C"));
    myColors.add(new Color("Indigo", 75, 0, 130, "#4B0082"));
    myColors.add(new Color("Ivory", 255, 255, 240, "#FFFFF0"));
    myColors.add(new Color("Khaki", 240, 230, 140, "#F0E68C"));
    myColors.add(new Color("Lavender", 230, 230, 250, "#E6E6FA"));
    myColors.add(new Color("LavenderBlush", 255, 240, 245, "#FFF0F5"));
    myColors.add(new Color("LawnGreen", 124, 252, 0, "#7CFC00"));
    myColors.add(new Color("LemonChiffon", 255, 250, 205, "#FFFACD"));
    myColors.add(new Color("LightBlue", 173, 216, 230, "#ADD8E6"));
    myColors.add(new Color("LightCoral", 240, 128, 128, "#F08080"));
    myColors.add(new Color("LightCyan", 224, 255, 255, "#E0FFFF"));
    myColors.add(new Color("LightGoldenrodYellow", 250, 250, 210, "#FAFAD2"));
    myColors.add(new Color("LightGreen", 144, 238, 144, "#90EE90"));
    myColors.add(new Color("LightGrey", 211, 211, 211, "#D3D3D3"));
    myColors.add(new Color("LightPink", 255, 182, 193, "#FFB6C1"));
    myColors.add(new Color("LightSalmon", 255, 160, 122, "#FFA07A"));
    myColors.add(new Color("LightSeaGreen", 32, 178, 170, "#20B2AA"));
    myColors.add(new Color("LightSkyBlue", 135, 206, 250, "#87CEFA"));
    myColors.add(new Color("LightSlateGray", 119, 136, 153, "#778899"));
    myColors.add(new Color("LightSteelBlue", 176, 196, 222, "#B0C4DE"));
    myColors.add(new Color("LightYellow", 255, 255, 224, "#FFFFE0"));
    myColors.add(new Color("Lime", 0, 255, 0, "#00FF00"));
    myColors.add(new Color("LimeGreen", 50, 205, 50, "#32CD32"));
    myColors.add(new Color("Linen", 250, 240, 230, "#FAF0E6"));
    myColors.add(new Color("Magenta", 255, 0, 255, "#FF00FF"));
    myColors.add(new Color("Maroon", 128, 0, 0, "#800000"));
    myColors.add(new Color("MediumAquamarine", 102, 205, 170, "#66CDAA"));
    myColors.add(new Color("MediumBlue", 0, 0, 205, "#0000CD"));
    myColors.add(new Color("MediumOrchid", 186, 85, 211, "#BA55D3"));
    myColors.add(new Color("MediumPurple", 147, 112, 219, "#9370DB"));
    myColors.add(new Color("MediumSeaGreen", 60, 179, 113, "#3CB371"));
    myColors.add(new Color("MediumSlateBlue", 123, 104, 238, "#7B68EE"));
    myColors.add(new Color("MediumSpringGreen", 0, 250, 154, "#00FA9A"));
    myColors.add(new Color("MediumTurquoise", 72, 209, 204, "#48D1CC"));
    myColors.add(new Color("MediumVioletRed", 199, 21, 133, "#C71585"));
    myColors.add(new Color("MidnightBlue", 25, 25, 112, "#191970"));
    myColors.add(new Color("MintCream", 245, 255, 250, "#F5FFFA"));
    myColors.add(new Color("MistyRose", 255, 228, 225, "#FFE4E1"));
    myColors.add(new Color("Moccasin", 255, 228, 181, "#FFE4B5"));
    myColors.add(new Color("NavajoWhite", 255, 222, 173, "#FFDEAD"));
    myColors.add(new Color("Navy", 0, 0, 128, "#000080"));
    myColors.add(new Color("OldLace", 253, 245, 230, "#FDF5E6"));
    myColors.add(new Color("Olive", 128, 128, 0, "#808000"));
    myColors.add(new Color("OliveDrab", 107, 142, 35, "#6B8E23"));
    myColors.add(new Color("Orange", 255, 165, 0, "#FFA500"));
    myColors.add(new Color("OrangeRed", 255, 69, 0, "#FF4500"));
    myColors.add(new Color("Orchid", 218, 112, 214, "#DA70D6"));
    myColors.add(new Color("PaleGoldenrod", 238, 232, 170, "#EEE8AA"));
    myColors.add(new Color("PaleGreen", 152, 251, 152, "#98FB98"));
    myColors.add(new Color("PaleTurquoise", 175, 238, 238, "#AFEEEE"));
    myColors.add(new Color("PaleVioletRed", 219, 112, 147, "#DB7093"));
    myColors.add(new Color("PapayaWhip", 255, 239, 213, "#FFEFD5"));
    myColors.add(new Color("PeachPuff", 255, 218, 185, "#FFDAB9"));
    myColors.add(new Color("Peru", 205, 133, 63, "#CD853F"));
    myColors.add(new Color("Pink", 255, 192, 203, "#FFC0CB"));
    myColors.add(new Color("Plum", 221, 160, 221, "#DDA0DD"));
    myColors.add(new Color("PowderBlue", 176, 224, 230, "#B0E0E6"));
    myColors.add(new Color("Purple", 128, 0, 128, "#800080"));
    myColors.add(new Color("Red", 255, 0, 0, "#FF0000"));
    myColors.add(new Color("RosyBrown", 188, 143, 143, "#BC8F8F"));
    myColors.add(new Color("RoyalBlue", 65, 105, 225, "#4169E1"));
    myColors.add(new Color("SaddleBrown", 139, 69, 19, "#8B4513"));
    myColors.add(new Color("Salmon", 250, 128, 114, "#FA8072"));
    myColors.add(new Color("SandyBrown", 244, 164, 96, "#F4A460"));
    myColors.add(new Color("SeaGreen", 46, 139, 87, "#2E8B57"));
    myColors.add(new Color("Seashel", 255, 245, 238, "#FFF5EE"));
    myColors.add(new Color("Sienna", 160, 82, 45, "#A0522D"));
    myColors.add(new Color("Silver", 192, 192, 192, "#C0C0C0"));
    myColors.add(new Color("SkyBlue", 135, 206, 235, "#87CEEB"));
    myColors.add(new Color("SlateBlue", 106, 90, 205, "#6A5ACD"));
    myColors.add(new Color("SlateGray", 112, 128, 144, "#708090"));
    myColors.add(new Color("Snow", 255, 250, 250, "#FFFAFA"));
    myColors.add(new Color("SpringGreen", 0, 255, 127, "#00FF7F"));
    myColors.add(new Color("SteelBlue", 70, 130, 180, "#4682B4"));
    myColors.add(new Color("Tan", 210, 180, 140, "#D2B48C"));
    myColors.add(new Color("Teal", 0, 128, 128, "#008080"));
    myColors.add(new Color("Thistle", 216, 191, 216, "#D8BFD8"));
    myColors.add(new Color("Tomato", 255, 99, 71, "#FF6347"));
    myColors.add(new Color("Turquoise", 64, 224, 208, "#40E0D0"));
    myColors.add(new Color("Violet", 238, 130, 238, "#EE82EE"));
    myColors.add(new Color("Wheat", 245, 222, 179, "#F5DEB3"));
    myColors.add(new Color("White", 255, 255, 255, "#FFFFFF"));
    myColors.add(new Color("WhiteSmoke", 245, 245, 245, "#F5F5F5"));
    myColors.add(new Color("Yellow", 255, 255, 0, "#FFFF00"));
    myColors.add(new Color("YellowGreen", 154, 205, 50, "#9ACD32"));
  }

  public List getAllColors() {
    return myColors;
  }

  public List getColors() {
    List allColors = getAllColors();
    String searchString = (String) FacesUtil.getRequestMapValue("searchString");
    if (searchString == null)
      return allColors;
    List result = new ArrayList();
    for (int i = 0; i < allColors.size(); i++) {
      Color color = (Color) allColors.get(i);
      String colorName = color.getName();
      if (colorName.toUpperCase().indexOf(searchString.toUpperCase()) != -1)
        result.add(color);
    }
    return result;
  }

  private Converter myColorConverter = new ColorConverter();

  public Converter getColorConverter() {
    return myColorConverter;
  }


  public Color getColorByText(String text) {
    if (text == null)
      return null;
    text = text.trim();
    for (int i = 0; i < myColors.size(); i++) {
      Color color = (Color) myColors.get(i);
      if (text.equalsIgnoreCase(color.getName()) ||
              text.equalsIgnoreCase(color.getHex()))
        return color;
    }

    if (!text.startsWith("#"))
      return null;
    return new Color(text, 0, 0, 0, text);
  }

  public static class Color implements Serializable {
    private String myName;
    private int myR;
    private int myG;
    private int myB;
    private String myHex;

    public Color(String name, int r, int g, int b, String hex) {
      myName = name;
      myR = r;
      myG = g;
      myB = b;
      myHex = hex;
    }

    public int getR() {
      return myR;
    }

    public int getG() {
      return myG;
    }

    public int getB() {
      return myB;
    }

    public String getHex() {
      return myHex;
    }

    public String getName() {
      return myName;
    }

  }

  private class ColorConverter implements Converter, Serializable {
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
      return getColorByText(value);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
      if (value == null)
        return "";
      return ((Color) value).getName();
    }
  }

  public List getSuggestedPlants() {
    List suggestedPlants = new ArrayList();
    String typedValue = (String) FacesUtil.getRequestMapValue("searchString");
    if (typedValue != null) {
      for (int i = 0; i < myPlants.size(); i++) {
        String plant = (String) myPlants.get(i);
        String plantForComparison = plant.toLowerCase();
        String typedValueForComparison = typedValue.toLowerCase();
        if (plantForComparison.startsWith(typedValueForComparison))
          suggestedPlants.add(plant);
      }

    } else {
      for (int i = 0; i < myPlants.size(); i++) {
        if (i % 20 == 0) {
          String plant = (String) myPlants.get(i);
          suggestedPlants.add(plant);
        }
      }
    }
    return suggestedPlants;
  }

  public List getPlants() {
    return myPlants;
  }

}

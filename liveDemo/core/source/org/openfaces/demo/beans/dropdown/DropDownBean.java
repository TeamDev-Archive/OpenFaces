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

package org.openfaces.demo.beans.dropdown;

import org.openfaces.util.Faces;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DropDownBean implements Serializable {
    private String selectedFont;
    private String selectedFontFamily;
    private Color selectedFontColor;
    private Color selectedBGColor;

    private String selectedTextAlign;
    private String countingRhyme;
    private Colors colors = new Colors();
    private Converter colorConverter = new ColorConverter();

    public DropDownBean() {
        selectedFont = "16pt";
        selectedFontFamily = "Tahoma";
        selectedFontColor = colors.getColorByText("Black");
        selectedBGColor = colors.getColorByText("White");

        selectedTextAlign = "Left";

        countingRhyme = "Humpty Dumpty sat on a wall." + "\n" + "Humpty Dumpty had a great fall." + "\n" +
                "All the king's horses and all the king's men" + "\n" + "Couldn't put Humpty together again.";
    }

    public String getSelectedFont() {
        return selectedFont;
    }

    public void setSelectedFont(String selectedFont) {
        this.selectedFont = selectedFont;
    }

    public String getSelectedFontFamily() {
        return selectedFontFamily;
    }

    public void setSelectedFontFamily(String selectedFontFamily) {
        this.selectedFontFamily = selectedFontFamily;
    }

    public Color getSelectedBGColor() {
        return selectedBGColor;
    }

    public void setSelectedBGColor(Color selectedBGColor) {
        this.selectedBGColor = selectedBGColor;
    }

    public String getSelectedTextAlign() {
        return selectedTextAlign;
    }

    public void setSelectedTextAlign(String selectedTextAlign) {
        this.selectedTextAlign = selectedTextAlign;
    }

    public String getCountingRhyme() {
        return countingRhyme;
    }

    public void setCountingRhyme(String countingRhyme) {
        this.countingRhyme = countingRhyme;
    }

    public List<Color> getColors() {
        List<Color> allColors = colors.getColors();
        String searchString = Faces.var("searchString", String.class);
        if (searchString == null)
            return allColors;
        List<Color> result = new ArrayList<Color>();
        for (Color color : allColors) {
            String colorName = color.getName();
            if (colorName.toUpperCase().indexOf(searchString.toUpperCase()) != -1)
                result.add(color);
        }
        return result;
    }

    public Converter getColorConverter() {
        return colorConverter;
    }

    public Color getSelectedFontColor() {
        return selectedFontColor;
    }

    public void setSelectedFontColor(Color selectedFontColor) {
        this.selectedFontColor = selectedFontColor;
    }

    private class ColorConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return colors.getColorByText(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            if (value == null)
                return "";
            return ((Color) value).getName();
        }
    }

}
/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.graphictext;

import org.openfaces.component.input.DropDownItem;
import org.openfaces.component.output.GraphicText;
import org.openfaces.demo.beans.dropdown.Color;
import org.openfaces.demo.beans.dropdown.Colors;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class GraphicTextBean {

    private List<Hotel> hotels = new ArrayList<Hotel>();
    private String text;
    private Integer direction = new Integer("315");
    private int fontSize = 30;
    private Color selectedFontColor;
    private String style;
    private Converter colorConverter = new ColorConverter();
    private Converter directionsConverter = new DirectionsConverter();
    private Colors colors = new Colors();

    private static final List<DropDownItem> PREDEFINED_FONT_SIZES = Arrays.asList(
            new DropDownItem("12"),
            new DropDownItem("20"),
            new DropDownItem("25"),
            new DropDownItem("30"),
            new DropDownItem("40"),
            new DropDownItem("50"));

    private static final List<DropDownItem> PREDEFINED_DIRECTIONS = Arrays.asList(
            new DropDownItem("leftToRight"),
            new DropDownItem("rightToLeft"),
            new DropDownItem("topToBottom"),
            new DropDownItem("bottomToTop"),
            new DropDownItem("170"),
            new DropDownItem("210"),
            new DropDownItem("315"));

    public GraphicTextBean() {

        selectedFontColor = colors.getColorByText("Orange");

        hotels.add(new Hotel("Desert Palms Hotel & Suites", true, false, false, false,
                true, true, false, false, false, false, true, false,
                true, false, false, false, false, false, false, true));
        hotels.add(new Hotel("Doubletree Guest Suites", true, false, false, false,
                false, true, false, false, false, false, false, false,
                true, false, false, false, true, false, true, false));
        hotels.add(new Hotel("Embassy Suites Hotel Anaheim North", false, false, false, false,
                true, true, false, false, false, false, false, false,
                false, false, true, false, false, false, true, true));
        hotels.add(new Hotel("Embassy Suites Hotel Anaheim South", false, false, false, false,
                true, false, false, false, false, false, false, false,
                false, true, true, false, false, false, false, true));
        hotels.add(new Hotel("Hilton Suites Anaheim-Orange", true, false, false, false,
                true, false, false, true, false, true, false, false,
                false, false, false, false, false, false, false, false));
        hotels.add(new Hotel("Holiday Inn Hotel & Suites", true, false, false, false,
                true, false, false, false, false, false, false, false,
                false, false, false, true, false, false, false, false));
        hotels.add(new Hotel("Homewood Suites by Hilton", true, true, true, true,
                true, true, false, false, false, true, false, true,
                false, false, false, false, false, false, false, false));
        hotels.add(new Hotel("La Quinta Inn & Suites", true, true, true, true,
                true, true, false, false, false, false, false, true,
                false, false, false, true, false, false, false, false));
        hotels.add(new Hotel("Marriott Anaheim Suites", true, false, false, false,
                true, false, false, false, false, false, true, true,
                true, false, false, false, true, true, false, true));
        hotels.add(new Hotel("Portofino Inn & Suites", true, true, true, true,
                true, true, false, false, true, false, false, true,
                false, false, false, false, false, true, false, false));
        hotels.add(new Hotel("Residence Inn Anaheim - Maingate", true, true, true, true,
                true, true, true, false, false, false, false, false,
                false, false, false, false, false, false, true, true));
        hotels.add(new Hotel("Residence Inn Anaheim Resort Area", true, true, true, true,
                true, true, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false));
        hotels.add(new Hotel("Staybridge Suites", true, false, false, false,
                true, false, false, false, false, false, false, false,
                false, true, false, false, false, false, false, true));

        text = "Graphic Text";
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text.equals(""))
            this.text = "Graphic Text";
        else
            this.text = text;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        if (fontSize < 5)
            fontSize = 5;
        if (fontSize > 50)
            fontSize = 50;
        this.fontSize = fontSize;
    }

    public String getStyle() {
        style = "font-size:" + fontSize + "pt; color: rgb(" + selectedFontColor.getR() + "," + selectedFontColor.getG() + "," + selectedFontColor.getB() + ");";
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Color getSelectedFontColor() {
        return selectedFontColor;
    }

    public void setSelectedFontColor(Color selectedFontColor) {
        this.selectedFontColor = selectedFontColor;
    }

    public List<DropDownItem> getPredefinedFontSizes() {
        return PREDEFINED_FONT_SIZES;
    }

    public List<DropDownItem> getDirections() {
        return PREDEFINED_DIRECTIONS;
    }

    public Converter getColorConverter() {
        return colorConverter;
    }

    public Converter getDirectionsConverter() {
        return directionsConverter;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public List<Color> getColors() {
        return colors.getColors();
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

    private class DirectionsConverter implements Converter, Serializable {

        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            if (value.equals("leftToRight")) {
                return GraphicText.LEFT_TO_RIGHT;
            } else if (value.equals("rightToLeft")) {
                return GraphicText.RIGHT_TO_LEFT;
            } else if (value.equals("topToBottom")) {
                return GraphicText.TOP_TO_BOTTOM;
            } else if (value.equals("bottomToTop")) {
                return GraphicText.BOTTOM_TO_TOP;
            } else {
                return new Integer(value);
            }
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            if (value.equals(GraphicText.LEFT_TO_RIGHT)) {
                return "leftToRight";
            } else if (value.equals(GraphicText.RIGHT_TO_LEFT)) {
                return "rightToLeft";
            } else if (value.equals(GraphicText.TOP_TO_BOTTOM)) {
                return "topToBottom";
            } else if (value.equals(GraphicText.BOTTOM_TO_TOP)) {
                return "bottomToTop";
            } else {
                return value.toString();
            }
        }
    }

}

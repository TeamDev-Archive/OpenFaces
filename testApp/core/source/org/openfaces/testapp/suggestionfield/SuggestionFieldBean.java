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
package org.openfaces.testapp.suggestionfield;

import org.openfaces.util.Faces;
import org.openfaces.testapp.datatable.Color;
import org.openfaces.testapp.datatable.ColorDB;
import org.openfaces.testapp.dropdown.DropDownBean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ValueChangeEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class SuggestionFieldBean {

    private String testSelectedValue;
    private String testSelectedValue2;
    private List<String> plants = new ArrayList<String>();
    private Color selectedBGColor = new Color("AliceBlue", 240, 248, 255, "#F0F8FF");

    private Converter colorConverter = new ColorConverter();
    private ColorDB colors = new ColorDB();
    private boolean disabled = false;
    private String selectedValue = "";

    private int valueChangeListenerCounter = 0;
    public static boolean testValueChangeListener;

    public SuggestionFieldBean() {
        try {
            InputStream resource = DropDownBean.class.getResourceAsStream("houseplants.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            String currentString;
            while (true) {
                currentString = reader.readLine();
                if (currentString == null) break;
                plants.add(new String(currentString.getBytes(), "UTF-8"));
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getTestSelectedValue() {
        return testSelectedValue;
    }

    public void setTestSelectedValue(String testSelectedValue) {
        this.testSelectedValue = testSelectedValue;
    }

    public List<String> getSimpleTestCollection() {
        return Arrays.asList("Red", "Yellow", "Blue");
    }

    public String getTestSelectedValue2() {
        return testSelectedValue2;
    }

    public void setTestSelectedValue2(String testSelectedValue2) {
        this.testSelectedValue2 = testSelectedValue2;
    }

    public List<String> getSuggestedPlants() {
        List<String> suggestedPlants = new ArrayList<String>();
        String typedValue = (String) Faces.var("searchString");
        if (typedValue != null) {
            for (Object myPlant : plants) {
                String plant = (String) myPlant;
                String plantForComparison = plant.toLowerCase();
                String typedValueForComparison = typedValue.toLowerCase();
                if (plantForComparison.startsWith(typedValueForComparison))
                    suggestedPlants.add(plant);
            }
        } else {
            for (int i = 0; i < plants.size(); i++) {
                if (i % 20 == 0) {
                    String plant = plants.get(i);
                    suggestedPlants.add(plant);
                }
            }
        }
        return suggestedPlants;
    }

    public List<String> getPlants() {
        return plants;
    }

    public Color getSelectedBGColor() {
        return selectedBGColor;
    }

    public void setSelectedBGColor(Color selectedBGColor) {
        this.selectedBGColor = selectedBGColor;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
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

    public List<Color> getColors() {
        List<Color> allColors = colors.getColors();
        String searchString = (String) Faces.var("searchString");
        if (searchString == null)
            return allColors;
        List<Color> result = new ArrayList<Color>();
        for (Object allColor : allColors) {
            Color color = (Color) allColor;
            String colorName = color.getName();
            if (colorName.toUpperCase().indexOf(searchString.toUpperCase()) != -1)
                result.add(color);
        }
        return result;
    }

    public Converter getColorConverter() {
        return colorConverter;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void makeDisabled() {
        setDisabled(true);
    }

    public void valueChangedAttribute(ValueChangeEvent event) {
        valueChangeListenerCounter++;
    }

    public int getValueChangeListenerCounter() {
        return valueChangeListenerCounter;
    }

    public void setValueChangeListenerCounter(int valueChangeListenerCounter) {
        this.valueChangeListenerCounter = valueChangeListenerCounter;
    }

    public String getValueChangeListenerFlag() {
        return String.valueOf(valueChangeListenerCounter);
    }

    public boolean isTestValueChangeListener() {
        return testValueChangeListener;
    }

}
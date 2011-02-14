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
package org.openfaces.testapp.dropdown;

import org.openfaces.util.Faces;
import org.openfaces.testapp.datatable.Color;
import org.openfaces.testapp.datatable.ColorDB;
import org.openfaces.testapp.datatable.User;
import org.openfaces.testapp.datatable.UserPermissionsTableDemoBean;

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
import java.util.logging.Logger;

/**
 * @author Andrew Palval
 */
public class DropDownBean {

    Logger logger = Logger.getLogger(DropDownBean.class.getName());

    private String value = "\u041c\u0430\u0442\u0440\u0438\u0446\u0430 ... \u0442\u0435\u0431\u044f";
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

    public DropDownBean() {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void valueChanged() {
        logger.info("ooo");
    }

    public void valueChanged(ValueChangeEvent e) {
        logger.info("hi there!");
    }

    private User selectedUser = getUserPermissionsTableDemoBean(FacesContext.getCurrentInstance()).getTestUser();

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public Converter getUserConverter() {
        return USER_CONVERTER;
    }

    private Converter USER_CONVERTER = new Converter() {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            UserPermissionsTableDemoBean usersBean = getUserPermissionsTableDemoBean(facesContext);
            return usersBean.findUserByName(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            User user = (User) value;
            return user.getUserName();
        }
    };

    private UserPermissionsTableDemoBean getUserPermissionsTableDemoBean(FacesContext facesContext) {
        return (UserPermissionsTableDemoBean) facesContext.getApplication().createValueBinding(
                "#{UserPermissionsTableDemoBean}").getValue(facesContext);
    }

    private String testValue1 = "unknown";

    public String getTestValue1() {
        return testValue1;
    }

    public void setTestValue1(String testValue1) {
        this.testValue1 = testValue1;
    }

    private String testValue2 = "unknown";

    public String getTestValue2() {
        return testValue2;
    }

    public void setTestValue2(String testValue2) {
        this.testValue2 = testValue2;
    }

    public List<String> getSimpleTestCollection() {
        return Arrays.asList("Red", "Yellow", "Blue");
    }

    public String getTestSelectedValue() {
        return testSelectedValue;
    }

    public void setTestSelectedValue(String testSelectedValue) {
        this.testSelectedValue = testSelectedValue;
    }

    public List<String> getSuggestedPlants() {
        List<String> suggestedPlants = new ArrayList<String>();
        String typedValue = Faces.var("searchString", String.class);
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

    public Converter getColorConverter() {
        return colorConverter;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
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

    public String getTestSelectedValue2() {
        return testSelectedValue2;
    }

    public void setTestSelectedValue2(String testSelectedValue2) {
        this.testSelectedValue2 = testSelectedValue2;
    }

    private class ColorConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return colors.getColorByText(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            if (value == null) {
                return "";
            }
            return ((Color) value).getName();
        }
    }

    public List<Color> getColors() {
        List<Color> allColors = colors.getColors();
        String searchString = Faces.var("searchString", String.class);
        if (searchString == null) {
            return allColors;
        }
        List<Color> result = new ArrayList<Color>();
        for (Object allColor : allColors) {
            Color color = (Color) allColor;
            String colorName = color.getName();
            if (colorName.toUpperCase().indexOf(searchString.toUpperCase()) != -1) {
                result.add(color);
            }
        }
        return result;
    }

    public void makeDisabled() {
        setDisabled(true);
    }

    public void valueChangedAttribute(ValueChangeEvent event) {
        valueChangeListenerCounter++;
    }

    public boolean isTestValueChangeListener() {
        return testValueChangeListener;
    }

}

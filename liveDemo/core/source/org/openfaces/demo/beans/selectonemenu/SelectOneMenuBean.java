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
package org.openfaces.demo.beans.selectonemenu;

import org.openfaces.demo.beans.datatable.CitiesDB;
import org.openfaces.demo.beans.util.City;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class SelectOneMenuBean implements Serializable {
    private CitiesDB citiesDB = new CitiesDB();

    private City selectedCity;

    private Converter cityConverter = new Converter() {
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            if ("".equals(value)) return null;
            int cityId = Integer.parseInt(value);
            return citiesDB.getCityById(cityId);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            if (value == null) return "";
            return String.valueOf(((City) value).getId());
        }
    };

    private List<SelectItem> cityItems;

    public City getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(City selectedCity) {
        this.selectedCity = selectedCity;
    }

    public List<SelectItem> getCityItems() {
        if (cityItems == null) {
            List<City> cities = citiesDB.getCitiesByParameters(null, "name", true, 0, -1);
            cityItems = new ArrayList<SelectItem>();
            for (City city : cities) {
                cityItems.add(new SelectItem(city, city.getName()));
            }
        }
        return cityItems;
    }

    public Converter getCityConverter() {
        return cityConverter;
    }
}

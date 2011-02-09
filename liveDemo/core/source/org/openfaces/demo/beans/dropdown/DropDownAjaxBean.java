/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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
import org.openfaces.demo.beans.datatable.CitiesDB;
import org.openfaces.demo.beans.util.City;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class DropDownAjaxBean {
    private List<City> cities = new ArrayList<City>();
    private List<String> animals = new ArrayList<String>();
    private List<String> plants = new ArrayList<String>();

    private Converter cityConverter = new CityConverter();
    private City selectedCity;

    private String selectedAnimal = "";
    private String selectedPlant = "";

    public DropDownAjaxBean() {
        CitiesDB cities = new CitiesDB();
        this.cities = cities.getCitiesList();

        try {
            InputStream resource = DropDownAjaxBean.class.getResourceAsStream("animals.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            String currentString;
            while (true) {
                currentString = reader.readLine();
                if (currentString == null) break;
                animals.add(new String(currentString.getBytes(), "UTF-8"));
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            InputStream resource = DropDownAjaxBean.class.getResourceAsStream("houseplants.txt");
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

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }


    public City getCity(String value) {
        for (City city : cities) {
            if (city.getName().equals(value))
                return city;
        }
        return null;
    }

    public City getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(City selectedCity) {
        this.selectedCity = selectedCity;
    }

    public List<String> getSuggestedAnimals() {
        String typedValue = Faces.var("searchString", String.class);
        int pageStart = Faces.var("pageStart", Integer.class);
        int pageSize = Faces.var("pageSize", Integer.class);

        List<String> suggestedAnimals = getSuggestedAnimals(typedValue);
        return pageSize == -1 ? suggestedAnimals : suggestedAnimals.subList(pageStart, pageStart + pageSize);
    }

    private List<String> getSuggestedAnimals(String typedValue) {
        List<String> suggestedAnimals = new ArrayList<String>();
        if (typedValue != null) {
            for (String animal : animals) {
                String animalForComparison = animal.toLowerCase();
                String typedValueForComparison = typedValue.toLowerCase();
                if (animalForComparison.startsWith(typedValueForComparison))
                    suggestedAnimals.add(animal);
            }
        } else {
            return animals;
        }
        return suggestedAnimals;
    }

    public int getTotalPlants() {
        return getSuggestedPlants(null).size();
    }

    public int getTotalAnimals() {
        return getSuggestedAnimals(null).size();
    }

    public int getTotalCities() {
        return getSuggestedCities(null).size();
    }


    public List<String> getSuggestedPlants() {
        String typedValue = Faces.var("searchString", String.class);
        int pageStart = Faces.var("pageStart", Integer.class);
        int pageSize = Faces.var("pageSize", Integer.class);
        List<String> suggestedPlants = getSuggestedPlants(typedValue);

        return pageSize == -1 ? suggestedPlants : suggestedPlants.subList(pageStart, pageStart + pageSize);
    }

    private List<String> getSuggestedPlants(String typedValue) {
        List<String> suggestedPlants = new ArrayList<String>();
        if (typedValue != null) {
            for (String plant : plants) {
                String plantForComparison = plant.toLowerCase();
                String typedValueForComparison = typedValue.toLowerCase();
                if (plantForComparison.startsWith(typedValueForComparison))
                    suggestedPlants.add(plant);
            }
        } else {
            return plants;
        }
        return suggestedPlants;
    }


    public List<City> getSuggestedCities() {
        String typedValue = Faces.var("searchString", String.class);
        int pageStart = Faces.var("pageStart", Integer.class);
        int pageSize = Faces.var("pageSize", Integer.class);

        List<City> suggestedCities = getSuggestedCities(typedValue);
        return pageSize == -1 ? suggestedCities : suggestedCities.subList(pageStart, pageStart + pageSize);
    }

    private List<City> getSuggestedCities(String typedValue) {
        List<City> suggestedCities = new ArrayList<City>();
        if (typedValue != null) {
            for (City city : cities) {
                String cityForComparison = city.getName().toLowerCase();
                String typedValueForComparison = typedValue.toLowerCase();
                if (cityForComparison.startsWith(typedValueForComparison))
                    suggestedCities.add(city);
            }
        } else {
            return cities;
        }
        return suggestedCities;
    }

    public Converter getCityConverter() {
        return cityConverter;
    }

    public List<String> getAnimals() {
        return animals;
    }

    public String getSelectedAnimal() {
        return selectedAnimal;
    }

    public void setSelectedAnimal(String selectedAnimal) {
        this.selectedAnimal = selectedAnimal;
    }

    public List<String> getPlants() {
        return plants;
    }

    public String getSelectedPlant() {
        return selectedPlant;
    }

    public void setSelectedPlant(String selectedPlant) {
        this.selectedPlant = selectedPlant;
    }

    public String getSelectedCityName() {
        if (selectedCity != null)
            return selectedCity.getName();
        else return "";
    }

    public String getSelectedCountry() {
        if (selectedCity != null)
            return selectedCity.getCountry();
        else return "";
    }

    public boolean isSegregatorVisible() {
        return selectedCity != null;
    }

    public void setSegregatorVsible(boolean segregatorVisible) {
    }

    public class SelectedValue implements Serializable {
        private String city;
        private String animal;
        private String plant;

        public SelectedValue(String city, String animal, String plant) {
            this.city = city;
            this.animal = animal;
            this.plant = plant;
        }


        public String getCity() {
            return city;
        }

        public String getAnimal() {
            return animal;
        }

        public String getPlant() {
            return plant;
        }
    }

    public class CityConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return getCity(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            if (value == null)
                return "";
            return ((City) value).getName();
        }
    }

}
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

package org.openfaces.testapp.filter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.openfaces.component.filter.AndFilterCriterion;
import org.openfaces.component.filter.CompositeFilterCriterion;
import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.FilterProperty;
import org.openfaces.component.filter.FilterPropertyBase;
import static org.openfaces.component.filter.FilterType.*;

import org.openfaces.component.filter.FilterType;
import org.openfaces.component.filter.JSONBuilder;
import org.openfaces.component.filter.PredicateBuilder;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompositeFilterBean {

    private List<User> users = new ArrayList<User>() {{
        add(new User("John", "Doe", date("17-06-1986 18"), "UK", "London"));
        add(new User("Snow", "White", date("25-05-1973 14"), "UK", "Oxford"));
        add(new User("David", "Black", date("10-12-1958 15"), "Ukraine", "Kyiv"));
        add(new User("Tom", "Sanders", date("12-01-1978 12"), "Ukraine", "Kharkiv"));
        add(new User("Petr", "Ivanov", date("01-05-1968 9"), "Russia", "Moscow"));
        add(new User("Alex", "Pupkin", date("05-02-1979 7"), "Russia", "St.Petersburg"));
        add(new User("Garry", "Potter", date("05-02-1979 17"), "Finland", "Jyvaskyla"));
    }};

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH");
    private static final Converter COUNTRY_CONVERTER = new CountryConverter();

    private static final String FIRST_NAME_LABEL = "First Name";

    private Collection<Country> countries = EnumSet.allOf(Country.class);
    private Collection<String> names = Arrays.asList("Tom", "David");
    private CompositeFilterCriterion criteria = new AndFilterCriterion();
    private CompositeFilterCriterion criteria2 = new AndFilterCriterion();

    public List<User> getFilteredUsers() {
        Predicate predicate = PredicateBuilder.build(criteria);
        ArrayList<User> result = new ArrayList<User>(users);
        CollectionUtils.filter(result, predicate);
        return result;
    }

    public List<User> getFilteredUsersForStoredCriterion() throws JSONException {
        String jsonCriteria = getJsonCriteria();
        FilterCriterion storedCriteria = JSONBuilder.getInstance().parse(new JSONObject(jsonCriteria));
        Predicate predicate = PredicateBuilder.build(storedCriteria);
        ArrayList<User> result = new ArrayList<User>(users);
        CollectionUtils.filter(result, predicate);
        return result;
    }

    public List<User> getUsers() {
        return users;
    }

    public Collection<Country> getCountries() {
        return countries;
    }

    public Converter getCountryConverter() {
        return COUNTRY_CONVERTER;
    }

    public Collection<String> getNames() {
        return names;
    }

    public CompositeFilterCriterion getCriteria() {
        return criteria;
    }

    public void setCriteria(CompositeFilterCriterion criteria) {
        this.criteria = criteria;
    }

    public String getJsonCriteria() {
        return JSONBuilder.build(criteria2).toString();
    }

    public CompositeFilterCriterion getCriteria2() {
        return criteria2;
    }

    public void setCriteria2(CompositeFilterCriterion criteria2) {
        this.criteria2 = criteria2;
    }

    public String getFirstNameLabel() {
        return FIRST_NAME_LABEL;
    }

    private static Date date(String dateString) {
        try {
            return DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static class PlaceOfBirth implements Serializable {
        private Country country;
        private String city;

        public PlaceOfBirth() {
        }

        public PlaceOfBirth(String country, String city) {
            this.country = Country.fromString(country);
            this.city = city;
        }

        public PlaceOfBirth(Country country, String city) {
            this.country = country;
            this.city = city;
        }

        public Country getCountry() {
            return country;
        }

        public void setCountry(Country country) {
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

    public static class User implements Serializable {
        private String firstName;
        private String lastName;
        private Date dateOfBirth;
        private int age = -1;
        private PlaceOfBirth placeOfBirth;

        public User() {
        }

        public User(String firstName, String lastName, Date dateOfBirth, String country, String city) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfBirth = dateOfBirth;
            this.placeOfBirth = new PlaceOfBirth(country, city);
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Date getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(Date dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public int getAge() {
            if (age == -1) {
                Calendar now = Calendar.getInstance();
                Calendar dateOfBirthCalendar = Calendar.getInstance();
                dateOfBirthCalendar.setTime(dateOfBirth);
                age = now.get(Calendar.YEAR) - dateOfBirthCalendar.get(Calendar.YEAR);
                if ((dateOfBirthCalendar.get(Calendar.MONTH) > now.get(Calendar.MONTH))
                        || (dateOfBirthCalendar.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                        && dateOfBirthCalendar.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
                    age--;
                }

            }
            return age;
        }

        public PlaceOfBirth getPlaceOfBirth() {
            return placeOfBirth;
        }

        public void setPlaceOfBirth(PlaceOfBirth placeOfBirth) {
            this.placeOfBirth = placeOfBirth;
        }

    }


    public static enum Country {
        UKRAINE("Ukraine"),
        UK("UK"),
        RUSSIA("Russia"),
        FINLAND("Finland");

        private final static Map<String, Country> stringToEnum
                = new HashMap<String, Country>();

        static {
            for (Country country : values())
                stringToEnum.put(country.toString(), country);
        }

        private String name;


        Country(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }

        public static Country fromString(String name) {
            return stringToEnum.get(name);
        }

    }

    public static class CountryConverter implements Converter {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return Country.fromString(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            return value.toString();
        }
    }

    public void clearFilter() {
        criteria = new AndFilterCriterion();
    }

    public boolean isAgeRendered() {
        return (criteria.getCriteria().size() % 2 == 0);
    }

    public List<FilterProperty> getFilterProperties() {
        return Arrays.asList(filterProperty("Name", "firstName", TEXT),
                filterProperty("Age", "age", NUMBER),
                filterProperty("Country", "placeOfBirth.country.name", TEXT));
    }


     public static FilterProperty filterProperty(String title,String name, FilterType type) {
            return new FilterPropertyImpl(title,name, type);
        }

    private static class FilterPropertyImpl extends FilterPropertyBase {

        private String title;
        private String name;
        private FilterType type;


        private FilterPropertyImpl(String title, String name, FilterType type) {
            this.title = title;
            this.name = name;
            this.type = type;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public FilterType getType() {
            return type;
        }

        public void setType(FilterType type) {
            this.type = type;
        }

    }

}
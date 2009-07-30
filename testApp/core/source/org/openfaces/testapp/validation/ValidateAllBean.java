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

package org.openfaces.testapp.validation;

import org.hibernate.validator.NotNull;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.Past;
import org.hibernate.validator.Email;

import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

/**
 * @author Natalia Zolochevska
 */
public class ValidateAllBean {

    private static final Converter COUNTRY_CONVERTER = new CountryConverter();
    private static final Collection<Country> COUNTRIES = EnumSet.allOf(Country.class);

    private Country country;
    private String name;
    private Date joinDate;
    private String email;


    @NotNull
    @NotEmpty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Past
    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<Country> getCountries() {
        return COUNTRIES;
    }

    public Converter getCountryConverter() {
        return COUNTRY_CONVERTER;
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

}

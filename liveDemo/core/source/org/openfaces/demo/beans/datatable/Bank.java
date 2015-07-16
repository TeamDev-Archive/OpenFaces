/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.datatable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class Bank {
    private static int idCounter = 0;
    
    private int id;
    private String institutionName;
    private List<String> institutions ;
    private List<String> countries ;
    private List<String> cities ;
    private List<String> states ;
    private int certificateNumber;
    private String city;
    private String state;
    private int zip;
    private String county;
    private int averageAssets;

    public Bank(
            String institutionName,
            int certificateNumber,
            String city,
            String state,
            int zip,
            String county,
            int averageAssets) {
        this.id = idCounter++;
        this.institutionName = institutionName;
        this.certificateNumber = certificateNumber;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.county = county;
        this.averageAssets = averageAssets;
    }

    public List<String> getInstitutions() {
        if(institutions == null){
            institutions = University.descriptions();
        }

        return institutions;
    }

    public List<String> getCountries() {
        if(countries == null){
            countries = Country.descriptions();
        }

        return countries;
    }

    public List<String> getCities() {
        if(cities == null) {
            cities = City.descriptions();
        }

        return cities;
    }

    public void setInstitutions(List<String> institutions) {
        //Jsf only needed
    }

    public List<String> getStates() {
        if(states == null){
            states = State.descriptions();
        }
        return states;
    }

    public int getId() {
        return id;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public int getCertificateNumber() {
        return certificateNumber;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getZip() {
        return zip;
    }

    public String getCounty() {
        return county;
    }

    public int getAverageAssets() {
        return averageAssets;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bank)) return false;

        Bank bank = (Bank) o;

        if (averageAssets != bank.averageAssets) return false;
        if (certificateNumber != bank.certificateNumber) return false;
        if (zip != bank.zip) return false;
        if (city != null ? !city.equals(bank.city) : bank.city != null) return false;
        if (county != null ? !county.equals(bank.county) : bank.county != null) return false;
        if (institutionName != null ? !institutionName.equals(bank.institutionName) : bank.institutionName != null)
            return false;
        if (state != null ? !state.equals(bank.state) : bank.state != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (institutionName != null ? institutionName.hashCode() : 0);
        result = 31 * result + certificateNumber;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + zip;
        result = 31 * result + (county != null ? county.hashCode() : 0);
        result = 31 * result + averageAssets;
        return result;
    }

    public enum University{
        Aberdeen("Aberdeen"),
        Abertay("Abertay"),
                Aberystwyth("Aberystwyth"),
        Anglia("Anglia"),
        Ruskin("Ruskin"),
        Arts_University_Bournemouth(" Arts University Bournemouth"),
                Aston("Aston"),
        Bangor("Bangor");

        private String description;

        private University(String desciption) {
            this.description = desciption;
        }

        public String getDescription() {
            return description;
        }

        public static List<String> descriptions() {
            List<String> values = new ArrayList<String>();

            for (University university : University.values()) {
                values.add(university.description);
            }

            return values;
        }
    }

    public enum City{
        KRAKOW("Krakow"),
        BERLIN("Berlin"),
        KIEV("Kiev");

        private String description;

        private City(String desciption) {
            this.description = desciption;
        }

        public String getDescription() {
            return description;
        }


        public static List<String> descriptions() {
            List<String> values = new ArrayList<String>();

            for (City item : City.values()) {
                values.add(item.description);
            }

            return values;
        }
    }

    public enum Country{
        UK("uk"),
        USA("USA"),
        GERMAN("German"),
        RUSSIA("Russia");

        private String description;

        private Country(String desciption) {
            this.description = desciption;
        }

        public String getDescription() {
            return description;
        }

        public static List<String> descriptions() {
            List<String> values = new ArrayList<String>();

            for (Country item : Country.values()) {
                values.add(item.description);
            }

            return values;
        }
    }

    public enum State {
        AK("Alaska"),
        AL("Alabama"),
        AR("Arkansas"),
        AZ("Arizona"),
        CA("California"),
        CO("Colorado"),
        CT("Connecticut"),
        DC("Dist Of Col"),
        DE("Delaware"),
        FL("Florida"),
        GA("Georgia"),
        HI("Hawaii"),
        IA("Iowa"),
        ID("Idaho"),
        IL("Illinois"),
        IN("Indiana"),
        KS("Kansas"),
        KY("Kentucky"),
        LA("Louisiana"),
        MA("Massachusetts"),
        MD("Maryland"),
        ME("Maine"),
        MI("Michigan"),
        MN("Minnesota"),
        MO("Missouri"),
        MS("Mississippi"),
        MT("Montana"),
        NC("North Carolina"),
        ND("North Dakota"),
        NE("Nebraska"),
        NH("New Hampshire"),
        NJ("New Jersey"),
        NM("New Mexico"),
        NV("Nevada"),
        NY("New York"),
        OH("Ohio"),
        OK("Oklahoma"),
        OR("Oregon"),
        PA("Pennsylvania"),
        RI("Rhode Island"),
        SC("South Carolina"),
        SD("South Dakota"),
        TN("Tennessee"),
        TX("Texas"),
        UT("Utah"),
        VA("Virginia"),
        VT("Vermont"),
        WA("Washington"),
        WI("Wisconsin"),
        WV("West Virginia"),
        WY("Wyoming");
        private String description;

        private State(String desciption) {
            this.description = desciption;
        }

        public String getDescription() {
            return description;
        }

        public static List<String> descriptions() {
            List<String> values = new ArrayList<String>();

            for (State item : State.values()) {
                values.add(item.description);
            }

            return values;
        }
    }
}
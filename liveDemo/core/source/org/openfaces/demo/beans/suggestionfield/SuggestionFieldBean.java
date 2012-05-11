/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.suggestionfield;

import org.openfaces.util.Faces;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SuggestionFieldBean implements Serializable {
    private List<String> countries = new ArrayList<String>();
    private List<String> industries = new ArrayList<String>();
    private List<Hobby> hobbies = new ArrayList<Hobby>();
    private List<String> lastNames = new ArrayList<String>();
    private List<String> firstNames = new ArrayList<String>();

    private String selectedCountry = "";
    private String selectedIndustry = "";
    private String selectedDegree = "";
    private Hobby selectedHobby;

    private List<Person> peopleByCriteria;
    private Map<PeopleSearchCriteria, List<Person>> cachedPeopleSearchResults = new HashMap<PeopleSearchCriteria, List<Person>>();

    private Converter hobbyConverter = new HobbyConverter();
    private List<String> degrees;

    public SuggestionFieldBean() {
        try {
            InputStream resource = SuggestionFieldBean.class.getResourceAsStream("countries.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            String currentString;
            while (true) {
                currentString = reader.readLine();
                if (currentString == null) break;
                countries.add(new String(currentString.getBytes(), "UTF-8"));
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        industries.addAll(Arrays.asList("Marketing", "Aerospace", "Military", "Agriculture", "Arts and Culture", "Automotive",
                "Banking", "Public Relations", "Construction", "Consulting", "Consumer Packaged Goods",
                "E-commerce", "Economic Development", "Education", "Energy", "Environment", "Food Services/Lodging",
                "Healthcare", "Information Technology", "Insurance", "International Aid and Development",
                "Journalism", "Manufacturing", "Entertainment", "Metals and mining", "Publishing", "Private Equity", "Venture Capital",
                "Pulp & Paper", "Real Estate", "Wholesale", "Social Services", "Software", "Telecommunications", "Logistics",
                "Tourism", "Financial services", "Government", "Energy", "Packaged goods", "Basic materials", "Consumer goods",
                "Travel and logistics", "High tech", "Pharmaceuticals", "Chemicals"));

        hobbies.add(new Hobby("drawing.jpg",
                "Drawing", "Drawing is the act of creation an image, form or shape."));
        hobbies.add(new Hobby("gardening.jpg",
                "Gardening", "Is the practice of the planning and cultivation of a garden."));
        hobbies.add(new Hobby("cooking.jpg",
                "Cooking", "Preparing food by the action of heat, as by boiling, baking, etc."));
        hobbies.add(new Hobby("chess.jpg",
                "Chess", " A game of skill for two players using a chessboard."));
        hobbies.add(new Hobby("googling.jpg",
                "Googling", "Is to search for (something on the internet) using a search engine."));

        lastNames.addAll(Arrays.asList("Askew", "Barton", "Conway", "Fry", "Schellden", "Dixie", "Fulke", "Lawley",
                "Ostheim", "Stapleton", "Manners", "Emerson", "Delven", "Vasser", "Jones", "Budd",
                "Osborn", "Handsel", "Stover", "Vibbard", "Acton", "Boyd", "Catherwood", "Driscol",
                "Falkland", "Hurst", "Logan", "McGowan", "Moriarty", "Stair", "Wickliff", "Agrippa",
                "Ayres", "Angus", "Binney", "Boynton", "Briare", "Buchanan", "Parshall", "Telford",
                "Tilton", "Van Slyck", "Wingfield", "Crayford", "Bromley ", "Bonar", "Allendorf", "Cormac"));

        firstNames.addAll(Arrays.asList("Aiden", "Frederic", "Vincent", "Ike", "Michael", "Larry", "Clayton",
                "Robert", "Adam", "Hannes", "James", "Andrew", "George", "Dean", "Walter", "Albert", "William", "Gary",
                "Chris", "Charles"));

        degrees = new ArrayList<String>(Arrays.asList("Associate Degree", "Bachelor-Arts", "Bachelor-Engineering",
                "Bachelor-Science", "Doctor of Medicine", "Doctoral", "Doctoral - All But Dissertation", "Juris Doctorate",
                "Masters-Arts", "Masters-Science", "Masters-Business Administration"));

        PeopleSearchCriteria criteria = new PeopleSearchCriteria(
                selectedCountry, selectedIndustry, selectedDegree, selectedHobby);

        cachedPeopleSearchResults.put(criteria, new ArrayList<Person>());
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getIndustries() {
        return industries;
    }

    public void setIndustries(List<String> industries) {
        this.industries = industries;
    }

    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Hobby> hobbies) {
        this.hobbies = hobbies;
    }

    public List<String> getLastNames() {
        return lastNames;
    }

    public void setLastNames(List<String> lastNames) {
        this.lastNames = lastNames;
    }

    public List<String> getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(List<String> firstNames) {
        this.firstNames = firstNames;
    }

    public List<Person> getPeopleByCriteria() {
        return peopleByCriteria;
    }

    public void findPeople(ActionEvent actionEvent) {
        PeopleSearchCriteria criteria = new PeopleSearchCriteria(
                selectedCountry, selectedIndustry, selectedDegree, selectedHobby);

        List<Person> people = cachedPeopleSearchResults.get(criteria);
        if (people == null) {
            people = findPeopleByCriteria(criteria);
            cachedPeopleSearchResults.put(criteria, people);
        }
        peopleByCriteria = people;
    }

    private List<Person> findPeopleByCriteria(PeopleSearchCriteria criteria) {
        Hobby hobbyParam = criteria.getHobby();
        String countryParam = criteria.getCountry();
        String industryParam = criteria.getIndustry();
        String degreeParam = criteria.getDegree();
        List<Person> people = new ArrayList<Person>();
        Random rand = new Random();

        List<String> filteredCountries = filterList(countries, countryParam);
        List<String> filteredIndustries = filterList(industries, industryParam);
        List<String> filteredDegrees = filterList(degrees, degreeParam);
        List<Hobby> filteredHobbies = filterList(hobbies, hobbyParam);
        if (!filteredCountries.isEmpty() && !filteredIndustries.isEmpty()
                && !filteredDegrees.isEmpty() && !filteredHobbies.isEmpty()) {
            int count = 1 + rand.nextInt(7);
            for (int i = 0; i < count; i++) {
                String firstLastName = firstNames.get(rand.nextInt(firstNames.size() - 1)) + ", " +
                        lastNames.get(rand.nextInt(lastNames.size() - 1));
                int countriesIndex = escapeRandomGeneration(rand, filteredCountries.size() - 1);
                String country = filteredCountries.get(countriesIndex);
                int industriesIndex = escapeRandomGeneration(rand, filteredIndustries.size() - 1);
                String industry = filteredIndustries.get(industriesIndex);
                int degreeIndex = escapeRandomGeneration(rand, filteredDegrees.size() - 1);
                String degree = filteredDegrees.get(degreeIndex);
                int hobbieIndex = escapeRandomGeneration(rand, filteredHobbies.size() - 1);
                Hobby hobby = filteredHobbies.get(hobbieIndex);

                Person person = new Person(firstLastName, country, industry, degree, hobby);
                people.add(person);
            }
        }
        return people;
    }

    private Integer escapeRandomGeneration(Random rand, int n) {
        return n == 0 ? 0 : rand.nextInt(n);
    }

    private List<String> filterList(List<String> list, String filter) {
        List<String> result = new ArrayList<String>();
        if (!"".equals(filter)) {
            for (String item : list) {
                if (item.toLowerCase().startsWith(filter.toLowerCase())) {
                    result.add(item);
                }
            }
        } else {
            result = list;
        }
        return result;
    }

    private List<Hobby> filterList(List<Hobby> list, Hobby filter) {
        List<Hobby> result = new ArrayList<Hobby>();
        if (filter != null) {
            for (Hobby item : list) {
                if (item.getHobbyTitle().toLowerCase().startsWith(filter.getHobbyTitle().toLowerCase())) {
                    result.add(item);
                }
            }
        } else {
            result = list;
        }
        return result;
    }

    public String getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public String getSelectedIndustry() {
        return selectedIndustry;
    }

    public void setSelectedIndustry(String selectedIndustry) {
        this.selectedIndustry = selectedIndustry;
    }

    public String getSelectedDegree() {
        return selectedDegree;
    }

    public void setSelectedDegree(String selectedDegree) {
        this.selectedDegree = selectedDegree;
    }

    public Hobby getSelectedHobby() {
        return selectedHobby;
    }

    public void setSelectedHobby(Hobby selectedHobby) {
        this.selectedHobby = selectedHobby;
    }

    public List<String> getSuggestedCountries() {
        List<String> suggestedCountries = new ArrayList<String>();
        String typedValue = Faces.var("searchString", String.class);
        if (typedValue != null) {
            for (String country : countries) {
                String countryForComparison = country.toLowerCase();
                String typedValueForComparison = typedValue.toLowerCase();
                if (countryForComparison.startsWith(typedValueForComparison))
                    suggestedCountries.add(country);
            }
        }
        return suggestedCountries;
    }

    public Converter getHobbyConverter() {
        return hobbyConverter;
    }

    private Hobby createHobby(String value) {
        return new Hobby(null, value, null);
    }

    public Hobby getHobbyByTitle(String value) {
        for (Hobby hobby : hobbies) {
            if (hobby.getHobbyTitle().equals(value))
                return hobby;
        }
        return null;
    }

    public class HobbyConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            if (value == null || value.equals(""))
                return null;
            Hobby hobby = getHobbyByTitle(value);
            if (hobby != null)
                return hobby;
            hobby = createHobby(value);
            return hobby;
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            if (value == null)
                return "";
            return ((Hobby) value).getHobbyTitle();
        }
    }

    private static class PeopleSearchCriteria implements Serializable {
        private String country;
        private String industry;
        private String degree;
        private Hobby hobby;

        public PeopleSearchCriteria(String country, String industry, String degree, Hobby hobby) {
            this.country = country;
            this.industry = industry;
            this.degree = degree;
            this.hobby = hobby;
        }


        public String getCountry() {
            return country;
        }

        public String getIndustry() {
            return industry;
        }

        public String getDegree() {
            return degree;
        }

        public Hobby getHobby() {
            return hobby;
        }


        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PeopleSearchCriteria that = (PeopleSearchCriteria) o;

            if (country != null ? !country.equals(that.country) : that.country != null) return false;
            if (degree != null ? !degree.equals(that.degree) : that.degree != null) return false;
            if (hobby != null ? !hobby.equals(that.hobby) : that.hobby != null) return false;
            if (industry != null ? !industry.equals(that.industry) : that.industry != null) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = (country != null ? country.hashCode() : 0);
            result = 31 * result + (industry != null ? industry.hashCode() : 0);
            result = 31 * result + (degree != null ? degree.hashCode() : 0);
            result = 31 * result + (hobby != null ? hobby.hashCode() : 0);
            return result;
        }
    }

}
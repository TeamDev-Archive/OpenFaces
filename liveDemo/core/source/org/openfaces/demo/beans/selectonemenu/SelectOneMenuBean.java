/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.demo.beans.selectonemenu;

import org.openfaces.demo.beans.datatable.Tour;
import org.openfaces.util.Faces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Dmitry Kashcheiev
 */
public class SelectOneMenuBean implements Serializable {

    private List<Region> availableRegions = new ArrayList<Region>();

    private List<String> availableRooms = new ArrayList<String>();

    private List<Food> availableFood = new ArrayList<Food>() {{
        add(new Food("RO", "Room only", false, false, false, false));
        add(new Food("BB", "Bed & breakfast", true, false, false, false));
        add(new Food("HB", "Half board", true, false, true, false));
        add(new Food("FB", "Full board", true, true, true, false));
        add(new Food("AI", "All inclusive", true, true, true, true));
    }};

    private static final List<String> availableHolidays = new ArrayList<String>() {{
        add("New Year's Day");
        add("Martin Luther King, Jr.");
        add("Inauguration Day");
        add("Presidents Day");
        add("Memorial Day");
        add("Independence Day");
        add("Labor Day");
        add("Columbus Day");
        add("Veterans Day");
        add("Thanksgiving Day");
        add("Christmas");
    }};

    private String selectedRegion = "";
    private String selectedFood = "";
    private String selectedHoliday = "";
    private String selectedRoom = "";
    private Integer selectedHotelStars = 0;

    private List<Tour> tours = new ArrayList<Tour>();
    private Map<TourSearchCriteria, List<Tour>> cachedToursSearchResults = new HashMap<TourSearchCriteria, List<Tour>>();
    private List<Tour> filteredTours = new ArrayList<Tour>();

    private static class TourSearchCriteria implements Serializable {
        private String region = "";
        private String food = "";
        private String holiday = "";
        private String room = "";
        private Integer hotelStars = 0;

        private TourSearchCriteria(String region, String food, String holiday, String room, Integer hotelStars) {
            this.region = region;
            this.food = food;
            this.holiday = holiday;
            this.room = room;
            this.hotelStars = hotelStars;
        }

        public String getRegion() {
            return region;
        }

        public String getFood() {
            return food;
        }

        public String getHoliday() {
            return holiday;
        }

        public String getRoom() {
            return room;
        }

        public Integer getHotelStars() {
            return hotelStars;
        }
    }

    public SelectOneMenuBean() {

        try {
            InputStream resource = Tour.class.getResourceAsStream("Tours.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            String currentString;
            Random random = new Random();

            while (true) {
                currentString = reader.readLine();
                if (currentString == null) break;
                String[] toursAttributes = currentString.split("\t");
                String country = new String(toursAttributes[0].getBytes(), "utf-8");
                String region = new String(toursAttributes[1].getBytes(), "utf-8");
                String nights = new String(toursAttributes[2].getBytes(), "utf-8");
                String hotel = new String(toursAttributes[3].getBytes(), "utf-8");
                String food = new String(toursAttributes[4].getBytes(), "utf-8");
                String room = new String(toursAttributes[5].getBytes(), "utf-8");
                String cost = new String(toursAttributes[6].getBytes(), "utf-8");
                tours.add(new Tour(availableHolidays.get(random.nextInt(availableHolidays.size())), new Region(country, region), Integer.parseInt(nights), hotel, food, room, Integer.parseInt(cost)));
                if (!availableRegions.contains(new Region(country, region))) {
                    availableRegions.add(new Region(country, region));
                }
                if (!availableRooms.contains(room)) {
                    availableRooms.add(room);
                }
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Region> getAvailableRegions() {
        String typedValue = Faces.var("searchString", String.class);
        if (typedValue == null || typedValue.isEmpty()) return availableRegions;

        List<Region> result = new ArrayList<Region>();
        for (Region region : availableRegions) {
            if (region.getCountry().toLowerCase().contains(typedValue.toLowerCase()) || region.getRegion().toLowerCase().contains(typedValue.toLowerCase())) {
                result.add(region);
            }
        }
        return result;
    }


    public void findTours() {
        TourSearchCriteria criteria = new TourSearchCriteria(selectedRegion, selectedFood, selectedHoliday, selectedRoom, selectedHotelStars);
        filteredTours = cachedToursSearchResults.get(criteria);
        if (filteredTours == null) {
            filteredTours = findToursByCriteria(criteria);
            cachedToursSearchResults.put(criteria, tours);
        }
    }

    public List<Tour> findToursByCriteria(TourSearchCriteria criteria) {
        List<Tour> resultTours = new ArrayList<Tour>();
        for (Tour tour : tours) {
            if (!criteria.getFood().equals("") && !criteria.getFood().equals(tour.getFood())) {
                continue;
            }
            if (!criteria.getHoliday().equals("") && !criteria.getHoliday().equals(tour.getHoliday())) {
                continue;
            }
            if (!criteria.getRegion().equals("") && !criteria.getRegion().equals(tour.getRegion().getRegion())) {
                continue;
            }
            if (!criteria.getRoom().equals("") && !criteria.getRoom().equals(tour.getRoom())) {
                continue;
            }
            if (criteria.getHotelStars() != 0 && criteria.getHotelStars() != tour.getHotelStars()) {
                continue;
            }
            resultTours.add(tour);
        }


        return resultTours;
    }

    public void setAvailableRegions(List<Region> availableRegions) {
        this.availableRegions = availableRegions;
    }

    public List<Food> getAvailableFood() {
        return availableFood;
    }

    public void setAvailableFood(List<Food> availableFood) {
        this.availableFood = availableFood;
    }

    public String getSelectedRegion() {
        return selectedRegion;
    }

    public void setSelectedRegion(String selectedRegion) {
        this.selectedRegion = selectedRegion;
    }

    public String getSelectedFood() {
        return selectedFood;
    }

    public void setSelectedFood(String selectedFood) {
        this.selectedFood = selectedFood;
    }

    public String getSelectedHoliday() {
        return selectedHoliday;
    }

    public void setSelectedHoliday(String selectedHoliday) {
        this.selectedHoliday = selectedHoliday;
    }

    public String getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(String selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    public Integer getSelectedHotelStars() {
        return selectedHotelStars;
    }

    public void setSelectedHotelStars(Integer selectedHotelStars) {
        this.selectedHotelStars = selectedHotelStars;
    }

    public List<String> getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(List<String> availableRooms) {
        this.availableRooms = availableRooms;
    }

    public List<Tour> getFilteredTours() {
        return filteredTours;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

    public Map<TourSearchCriteria, List<Tour>> getCachedToursSearchResults() {
        return cachedToursSearchResults;
    }

    public void setCachedToursSearchResults(Map<TourSearchCriteria, List<Tour>> cachedToursSearchResults) {
        this.cachedToursSearchResults = cachedToursSearchResults;
    }
}

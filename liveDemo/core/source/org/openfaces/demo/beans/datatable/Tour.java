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

package org.openfaces.demo.beans.datatable;

import org.openfaces.demo.beans.selectonemenu.Region;

/**
 * @author Dmitry Kashcheiev
 */
public class Tour {

    private static int idCounter = 0;

    private int id;

    private String holiday;
    private Region region;
    private Integer nights;
    private String hotel;
    private String food;
    private String room;
    private Integer cost;
    private int hotelStars;


    public Tour(String holiday, Region region, Integer nights, String hotel, String food, String room, Integer cost) {
        this.holiday = holiday;
        this.region = region;
        this.nights = nights;
        this.hotel = hotel;
        this.food = food;
        this.room = room;
        this.cost = cost;
        if (!hotel.replaceAll("[^0-9]","").equals("")){
            hotelStars = Integer.parseInt(hotel.replaceAll("[^0-9]",""));
        }else{
            hotelStars = 5;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Tour.idCounter = idCounter;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getHotelStars() {
        return hotelStars;
    }

    public void setHotelStars(int hotelStars) {
        this.hotelStars = hotelStars;
    }

    public Integer getNights() {
        return nights;
    }

    public void setNights(Integer nights) {
        this.nights = nights;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

}

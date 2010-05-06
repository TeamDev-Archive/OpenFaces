/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.graphictext;

import java.io.Serializable;

/**
 * @author Darya Shumilina
 */
public class Hotel implements Serializable {

    private String name;
    private boolean breakfastCookedToOrder;
    private boolean loungesBars;
    private boolean restaurants;
    private boolean roomService;
    private boolean parkingFee;
    private boolean concierge;
    private boolean gymHealthClub;
    private boolean laundromat;
    private boolean transportation;
    private boolean swimmingPools;
    private boolean whirlpoolsSauna;
    private boolean businessCenter;
    private boolean coffeeTeaMaker;
    private boolean crib;
    private boolean hairDryer;
    private boolean inRoomMoviesForAFee;
    private boolean internetAccess;
    private boolean microwave;
    private boolean refrigerator;
    private boolean rollaway;

    public Hotel(String name, boolean breakfastCookedToOrder, boolean loungesBars, boolean restaurants,
                 boolean roomService, boolean parkingFee, boolean concierge, boolean gymHealthClub,
                 boolean laundromat, boolean transportation, boolean swimmingPools, boolean whirlpoolsSauna,
                 boolean businessCenter, boolean coffeeTeaMaker, boolean crib, boolean hairDryer,
                 boolean inRoomMoviesForAFee, boolean internetAccess, boolean microwave,
                 boolean refrigerator, boolean rollaway) {
        this.name = name;
        this.breakfastCookedToOrder = breakfastCookedToOrder;
        this.loungesBars = loungesBars;
        this.restaurants = restaurants;
        this.roomService = roomService;
        this.parkingFee = parkingFee;
        this.concierge = concierge;
        this.gymHealthClub = gymHealthClub;
        this.laundromat = laundromat;
        this.transportation = transportation;
        this.swimmingPools = swimmingPools;
        this.whirlpoolsSauna = whirlpoolsSauna;
        this.businessCenter = businessCenter;
        this.coffeeTeaMaker = coffeeTeaMaker;
        this.crib = crib;
        this.hairDryer = hairDryer;
        this.inRoomMoviesForAFee = inRoomMoviesForAFee;
        this.internetAccess = internetAccess;
        this.microwave = microwave;
        this.refrigerator = refrigerator;
        this.rollaway = rollaway;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBreakfastCookedToOrder() {
        return breakfastCookedToOrder;
    }

    public void setBreakfastCookedToOrder(boolean breakfastCookedToOrder) {
        this.breakfastCookedToOrder = breakfastCookedToOrder;
    }

    public boolean isLoungesBars() {
        return loungesBars;
    }

    public void setLoungesBars(boolean loungesBars) {
        this.loungesBars = loungesBars;
    }

    public boolean isRestaurants() {
        return restaurants;
    }

    public void setRestaurants(boolean restaurants) {
        this.restaurants = restaurants;
    }

    public boolean isRoomService() {
        return roomService;
    }

    public void setRoomService(boolean roomService) {
        this.roomService = roomService;
    }

    public boolean isParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(boolean parkingFee) {
        this.parkingFee = parkingFee;
    }

    public boolean isConcierge() {
        return concierge;
    }

    public void setConcierge(boolean concierge) {
        this.concierge = concierge;
    }

    public boolean isGymHealthClub() {
        return gymHealthClub;
    }

    public void setGymHealthClub(boolean gymHealthClub) {
        this.gymHealthClub = gymHealthClub;
    }

    public boolean isLaundromat() {
        return laundromat;
    }

    public void setLaundromat(boolean laundromat) {
        this.laundromat = laundromat;
    }

    public boolean isTransportation() {
        return transportation;
    }

    public void setTransportation(boolean transportation) {
        this.transportation = transportation;
    }

    public boolean isSwimmingPools() {
        return swimmingPools;
    }

    public void setSwimmingPools(boolean swimmingPools) {
        this.swimmingPools = swimmingPools;
    }

    public boolean isWhirlpoolsSauna() {
        return whirlpoolsSauna;
    }

    public void setWhirlpoolsSauna(boolean whirlpoolsSauna) {
        this.whirlpoolsSauna = whirlpoolsSauna;
    }

    public boolean isBusinessCenter() {
        return businessCenter;
    }

    public void setBusinessCenter(boolean businessCenter) {
        this.businessCenter = businessCenter;
    }

    public boolean isCoffeeTeaMaker() {
        return coffeeTeaMaker;
    }

    public void setCoffeeTeaMaker(boolean coffeeTeaMaker) {
        this.coffeeTeaMaker = coffeeTeaMaker;
    }

    public boolean isCrib() {
        return crib;
    }

    public void setCrib(boolean crib) {
        this.crib = crib;
    }

    public boolean isHairDryer() {
        return hairDryer;
    }

    public void setHairDryer(boolean hairDryer) {
        this.hairDryer = hairDryer;
    }

    public boolean isInRoomMoviesForAFee() {
        return inRoomMoviesForAFee;
    }

    public void setInRoomMoviesForAFee(boolean inRoomMoviesForAFee) {
        this.inRoomMoviesForAFee = inRoomMoviesForAFee;
    }

    public boolean isInternetAccess() {
        return internetAccess;
    }

    public void setInternetAccess(boolean internetAccess) {
        this.internetAccess = internetAccess;
    }

    public boolean isMicrowave() {
        return microwave;
    }

    public void setMicrowave(boolean microwave) {
        this.microwave = microwave;
    }

    public boolean isRefrigerator() {
        return refrigerator;
    }

    public void setRefrigerator(boolean refrigerator) {
        this.refrigerator = refrigerator;
    }

    public boolean isRollaway() {
        return rollaway;
    }

    public void setRollaway(boolean rollaway) {
        this.rollaway = rollaway;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hotel)) return false;

        Hotel hotel = (Hotel) o;

        if (breakfastCookedToOrder != hotel.breakfastCookedToOrder) return false;
        if (businessCenter != hotel.businessCenter) return false;
        if (coffeeTeaMaker != hotel.coffeeTeaMaker) return false;
        if (concierge != hotel.concierge) return false;
        if (crib != hotel.crib) return false;
        if (gymHealthClub != hotel.gymHealthClub) return false;
        if (hairDryer != hotel.hairDryer) return false;
        if (inRoomMoviesForAFee != hotel.inRoomMoviesForAFee) return false;
        if (internetAccess != hotel.internetAccess) return false;
        if (laundromat != hotel.laundromat) return false;
        if (loungesBars != hotel.loungesBars) return false;
        if (microwave != hotel.microwave) return false;
        if (parkingFee != hotel.parkingFee) return false;
        if (refrigerator != hotel.refrigerator) return false;
        if (restaurants != hotel.restaurants) return false;
        if (rollaway != hotel.rollaway) return false;
        if (roomService != hotel.roomService) return false;
        if (swimmingPools != hotel.swimmingPools) return false;
        if (transportation != hotel.transportation) return false;
        if (whirlpoolsSauna != hotel.whirlpoolsSauna) return false;
        if (name != null ? !name.equals(hotel.name) : hotel.name != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 31 * result + (breakfastCookedToOrder ? 1 : 0);
        result = 31 * result + (loungesBars ? 1 : 0);
        result = 31 * result + (restaurants ? 1 : 0);
        result = 31 * result + (roomService ? 1 : 0);
        result = 31 * result + (parkingFee ? 1 : 0);
        result = 31 * result + (concierge ? 1 : 0);
        result = 31 * result + (gymHealthClub ? 1 : 0);
        result = 31 * result + (laundromat ? 1 : 0);
        result = 31 * result + (transportation ? 1 : 0);
        result = 31 * result + (swimmingPools ? 1 : 0);
        result = 31 * result + (whirlpoolsSauna ? 1 : 0);
        result = 31 * result + (businessCenter ? 1 : 0);
        result = 31 * result + (coffeeTeaMaker ? 1 : 0);
        result = 31 * result + (crib ? 1 : 0);
        result = 31 * result + (hairDryer ? 1 : 0);
        result = 31 * result + (inRoomMoviesForAFee ? 1 : 0);
        result = 31 * result + (internetAccess ? 1 : 0);
        result = 31 * result + (microwave ? 1 : 0);
        result = 31 * result + (refrigerator ? 1 : 0);
        result = 31 * result + (rollaway ? 1 : 0);
        return result;
    }
}
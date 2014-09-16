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

package org.openfaces.demo.beans.selectonemenu;

/**
 * @author Dmitry Kashcheiev
 */
public class Food {

    private String name;
    private String fullName;
    private boolean breakfast;
    private boolean lunch;
    private boolean dinner;
    private boolean drinks;

    public Food(String name, String fullName, boolean breakfast, boolean lunch, boolean dinner, boolean drinks) {
        this.name = name;
        this.fullName = fullName;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.drinks = drinks;
    }

    public String getName() {
        return name;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public boolean isDinner() {
        return dinner;
    }

    public boolean isLunch() {
        return lunch;
    }

    public boolean isDrinks() {
        return drinks;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return name;
    }
}

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

package org.openfaces.demo.beans.util;

import java.io.Serializable;

/**
 * @author Darya Shumilina
 */
public class City implements Serializable {
    private int id;
    private String name;
    private int population;
    private String country;

    public City(int id, String name, int population, String country) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.country = country;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public String getCountry() {
        return country;
    }
}
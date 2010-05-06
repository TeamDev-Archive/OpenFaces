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

package org.openfaces.demo.beans.suggestionfield;

import java.io.Serializable;

/**
 * @author Darya Shumilina
 */
public class Person implements Serializable {
    private String firstLastName;
    private String country;
    private String industry;
    private String degree;
    private Hobby hobby;

    public Person(String firstLastName, String country, String industry, String degree, Hobby hobby) {
        this.firstLastName = firstLastName;
        this.country = country;
        this.industry = industry;
        this.degree = degree;
        this.hobby = hobby;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Hobby getHobby() {
        return hobby;
    }

    public void setHobby(Hobby hobby) {
        this.hobby = hobby;
    }

}
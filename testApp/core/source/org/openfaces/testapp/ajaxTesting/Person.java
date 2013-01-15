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

package org.openfaces.testapp.ajaxTesting;

import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable {

    private String name;
    private String profession;
    private String hobby;
    private int age;
    private Date someDate;
    private String mail;

    public Person(String name, String profession, String hobby, int age, Date someDate, String mail) {
        this.name = name;
        this.profession = profession;
        this.hobby = hobby;
        this.age = age;
        this.someDate = someDate;
        this.mail = mail;
    }

    public String getProfession() {
        return profession;
    }

    public String getHobby() {
        return hobby;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Date getSomeDate() {
        return someDate;
    }

    public String getMail() {
        return mail;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSomeDate(Date someDate) {
        this.someDate = someDate;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}

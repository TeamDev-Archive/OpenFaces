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

package org.openfaces.testapp.screenshot;

import java.io.Serializable;

public class Person implements Serializable {

    private String name;
    private String profession;
    private String hobby;

    public Person(String name, String profession, String hobby) {
        this.name = name;
        this.profession = profession;
        this.hobby = hobby;
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

}

/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.portlets;

import java.io.Serializable;

public class Person implements Serializable {

  private String myName;
  private String myProfession;
  private String myHobby;

  public Person(String name, String profession, String hobby) {
    myName = name;
    myProfession = profession;
    myHobby = hobby;
  }

  public String getProfession() {
    return myProfession;
  }

  public String getHobby() {
    return myHobby;
  }

  public String getName() {
    return myName;
  }

}

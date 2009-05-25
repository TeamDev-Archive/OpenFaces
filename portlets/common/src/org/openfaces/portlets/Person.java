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

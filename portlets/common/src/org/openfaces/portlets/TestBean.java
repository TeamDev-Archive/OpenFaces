/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.portlets;

import java.util.HashMap;
import java.util.Map;

public class TestBean {
  static int myIdCounter = 0;
  private String myId;
  private String myField1;
  private int myField2;

  private static Map ourIdToBeanMap = new HashMap();

  public TestBean(String field1, int field2) {
    myField1 = field1;
    myField2 = field2;
    myId = myIdCounter++ + "";
    ourIdToBeanMap.put(myId, this);
  }

  public String getField1() {
    return myField1;
  }

  public void setField1(String field1) {
    myField1 = field1;
  }

  public int getField2() {
    return myField2;
  }

  public void setField2(int field2) {
    myField2 = field2;
  }

  public String getId() {
    return myId;
  }

  public static TestBean findById(String id) {
    return (TestBean) ourIdToBeanMap.get(id);
  }
}
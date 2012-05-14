/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.portlets;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

public class PortletsBean {
  private List myCollection1 = new ArrayList();

  public PortletsBean() {
    TestBean.myIdCounter = 0; 
    myCollection1.add(new TestBean("a_escapingTest:<test>&nbsp;&amp;'\n\"", 1));
    myCollection1.add(new TestBean("d", 11));
    myCollection1.add(new TestBean("b", 1));
    myCollection1.add(new TestBean("e", 4));
    myCollection1.add(new TestBean("c", 33));
    myCollection1.add(new TestBean("Four", 4));
    myCollection1.add(new TestBean("d", 3));
    myCollection1.add(new TestBean("e", 2));
    myCollection1.add(new TestBean("f", 2));
    myCollection1.add(new TestBean("d", 11));
    myCollection1.add(new TestBean("d", 33));
    myCollection1.add(new TestBean("f", 3));
    myCollection1.add(new TestBean("a", 4));
    myCollection1.add(new TestBean("Four", 4));
    myCollection1.add(new TestBean("Chetyre", 4));
    myCollection1.add(new TestBean(null, 0));
  }

  public void testAction() {
    System.out.println("testAction");
  }

  public List getCollection1() {
    return myCollection1;
  }

  public String getTopLevelComponents() {
    UIViewRoot uiViewRoot = FacesContext.getCurrentInstance().getViewRoot();
    List list = uiViewRoot.getChildren();
    String result = "";
    for (int i = 0; i < list.size(); i++) {
      UIComponent component = (UIComponent) list.get(i);
      if (i > 0)
        result += "<br>";
      result += "component family: " + component.getFamily() + "; component id: " + component.getId();
      if (component instanceof UIForm) {
        UIInput inputField = (UIInput) component.getChildren().get(0);
        result += "; [field value = '" + inputField.getValue() + "']";
      }
    }
    return result;
  }


}

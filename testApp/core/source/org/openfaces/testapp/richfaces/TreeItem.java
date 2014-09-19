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

package org.openfaces.testapp.richfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class TreeItem implements Serializable {
    private static int idCounter = 0;

    private String id = "item" + idCounter++;
    private String name;
    private String stringField1;
    private String stringField2;
    private int intField1;
    private int intField2;
    private List children;

    public TreeItem(String name, String stringField1, String stringField2, int intField1, int intField2, List children) {
        this.name = name;
        this.stringField1 = stringField1;
        this.stringField2 = stringField2;
        this.intField1 = intField1;
        this.intField2 = intField2;
        this.children = children != null ? new ArrayList(children) : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public boolean hasChildren() {
        return children != null && children.size() > 0;
    }

    public String getStringField1() {
        return stringField1;
    }

    public void setStringField1(String stringField1) {
        this.stringField1 = stringField1;
    }

    public String getStringField2() {
        return stringField2;
    }

    public void setStringField2(String stringField2) {
        this.stringField2 = stringField2;
    }

    public int getIntField1() {
        return intField1;
    }

    public void setIntField1(int intField1) {
        this.intField1 = intField1;
    }

    public int getIntField2() {
        return intField2;
    }

    public void setIntField2(int intField2) {
        this.intField2 = intField2;
    }


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TreeItem)) {
            return false;
        }
        TreeItem treeItem = (TreeItem) o;
        if (id != null ? !id.equals(treeItem.id) : treeItem.id != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }
}

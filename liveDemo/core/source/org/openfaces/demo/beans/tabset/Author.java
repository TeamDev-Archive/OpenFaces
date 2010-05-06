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

package org.openfaces.demo.beans.tabset;

import java.io.Serializable;

public class Author implements Serializable {
    public static final Author MONET = new Author("Claude Monet");
    public static final Author DALI = new Author("Salvador Dali");
    public static final Author KANDINSKY = new Author("Wassily Kandinsky");
    public static final Author REMBRANDT = new Author("Rembrandt van Rijn");

    private final String name;

    private Author(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}

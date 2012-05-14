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
package org.openfaces.testapp.tomahawk;

import java.io.Serializable;

/**
 * DOCUMENT ME!
 *
 * @author Thomas Spiegl (latest modification by $Author: werpu $)
 * @version $Revision: 371731 $ $Date: 2006-01-24 00:18:44 +0000 (Tue, 24 Jan 2006) $
 */
public class SimpleCar
        implements Serializable {
    /**
     * serial id for serialisation versioning
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String type;
    private String color;

    public SimpleCar(int id, String type, String color) {
        this.id = id;
        this.type = type;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

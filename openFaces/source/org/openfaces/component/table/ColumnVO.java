/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2016, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.table;

import java.io.Serializable;

/**
 * @author Max Yurin
 */
public class ColumnVO implements Serializable{
    private final String id;
    private final boolean visible;
    private final int position;

    public ColumnVO(String id, boolean visible, int position) {
        this.id = id;
        this.visible = visible;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getPosition() {
        return position;
    }
}

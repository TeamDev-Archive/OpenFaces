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

package org.openfaces.demo.beans.selectonemenu;

/**
 * @author: Roman Perin
 * Date: 12.09.14
 */
public class Holiday {
    private String image;
    private String holiday;

    public Holiday(String image, String holiday) {
        this.image = image;
        this.holiday = holiday;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }
}

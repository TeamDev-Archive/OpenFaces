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

package org.openfaces.demo.beans.suggestionfield;

import java.io.Serializable;

/**
 * @author Darya Shumilina
 */
public class Hobby implements Serializable {
    private String hobbyIcon;
    private String hobbyTitle;
    private String hobbyDescription;

    public Hobby(String hobbyIcon, String hobbyTitle, String hobbyDescription) {
        this.hobbyIcon = hobbyIcon;
        this.hobbyTitle = hobbyTitle;
        this.hobbyDescription = hobbyDescription;
    }

    public String getHobbyIcon() {
        return hobbyIcon;
    }

    public void setHobbyIcon(String hobbyIcon) {
        this.hobbyIcon = hobbyIcon;
    }

    public String getHobbyTitle() {
        return hobbyTitle;
    }

    public void setHobbyTitle(String hobbyTitle) {
        this.hobbyTitle = hobbyTitle;
    }

    public String getHobbyDescription() {
        return hobbyDescription;
    }

    public void setHobbyDescription(String hobbyDescription) {
        this.hobbyDescription = hobbyDescription;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hobby hobby = (Hobby) o;

        if (hobbyDescription != null ? !hobbyDescription.equals(hobby.hobbyDescription) : hobby.hobbyDescription != null)
            return false;
        if (hobbyIcon != null ? !hobbyIcon.equals(hobby.hobbyIcon) : hobby.hobbyIcon != null) return false;
        if (hobbyTitle != null ? !hobbyTitle.equals(hobby.hobbyTitle) : hobby.hobbyTitle != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (hobbyIcon != null ? hobbyIcon.hashCode() : 0);
        result = 31 * result + (hobbyTitle != null ? hobbyTitle.hashCode() : 0);
        result = 31 * result + (hobbyDescription != null ? hobbyDescription.hashCode() : 0);
        return result;
    }
}
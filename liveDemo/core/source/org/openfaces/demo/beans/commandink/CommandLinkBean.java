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
package org.openfaces.demo.beans.commandink;

import javax.faces.event.ActionEvent;
import java.io.Serializable;

/**
 * @author Dmitry Pikhulya
 */
public class CommandLinkBean implements Serializable {
    private String firstName = "";
    private String lastName = "";
    private String fullName = "";

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void composeFullName(ActionEvent event) {
        fullName = firstName + " " + lastName;
    }

    public String getFullName() {
        if (!isNameEntered())
            return "&#160;";
        return fullName;
    }

    public String getOutputClass() {
        return isNameEntered() ? "specifiedOutput" : "unspecifiedOutput";
    }

    private boolean isNameEntered() {
        return fullName.trim().length() > 0;
    }
}

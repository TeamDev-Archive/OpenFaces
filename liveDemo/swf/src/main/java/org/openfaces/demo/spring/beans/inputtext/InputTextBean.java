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

package org.openfaces.demo.spring.beans.inputText;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;

import javax.faces.bean.ManagedBean;
import java.io.Serializable;


/**
 * @author Alexei Tymchenko
 */

@ManagedBean
@Scope("session")
public class InputTextBean implements Serializable {
    private String clientFirstName;
    private String firstName;
    private String lastName;

    public String getClientFirstName() {
        return clientFirstName == null ? "" : clientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean fieldsAreFilled() {
        return !isEmpty(clientFirstName) && !isEmpty(firstName) && !isEmpty(lastName);
    }

    private boolean isEmpty(String expression){
        return expression != null && expression.isEmpty();
    }

    public String createOutputSuggestion() {
        return "<p>Dear &#160;" + StringEscapeUtils.escapeHtml(getClientFirstName()) +
                "!</p><p>Each year during the holiday season, we take great pleasure in setting aside our regular work and sending a heartfelt message to all our best friends and customers." +
                "</p><p>How joyful we are that this time has come again to extend to you our sincere gratitude because it is good friends and customers like you that make our business possible." +
                "</p><p>May your holiday be filled with joy and the coming year be overflowing with all the good things in life." +
                "</p><p>Sincerely Yours, &#160;" +
                StringEscapeUtils.escapeHtml(getFirstName()) + " &#160; " +
                StringEscapeUtils.escapeHtml(getLastName()) +
                ".</p>";
    }

    public String getText() {
        return "<p>Dear &#160;" + StringEscapeUtils.escapeHtml(getClientFirstName()) + "!</p><p>Each year during the holiday season, we take great pleasure in setting aside our regular work and sending a heartfelt message to all our best friends and customers." + "</p><p>How joyful we are that this time has come again to extend to you our sincere gratitude because it is good friends and customers like you that make our business possible." + "</p><p>May your holiday be filled with joy and the coming year be overflowing with all the good things in life." + "</p><p>Sincerely Yours, &#160;" + StringEscapeUtils.escapeHtml(getFirstName()) + " &#160; " + StringEscapeUtils.escapeHtml(getLastName()) + ".</p>";
    }
}

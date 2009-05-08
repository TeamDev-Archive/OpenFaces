package org.openfaces.demo.beans.inputtext;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author Alexei Tymchenko
 */
public class InputTextBean {
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

    public String getText() {
        StringBuilder contentBuilder = new StringBuilder();

        contentBuilder.append("<p>Dear &#160;").
                append(StringEscapeUtils.escapeHtml(getClientFirstName())).
                append("!</p><p>Each year during the holiday season, we take great pleasure in setting aside our regular work and sending a heartfelt message to all our best friends and customers.").
                append("</p><p>How joyful we are that this time has come again to extend to you our sincere gratitude because it is good friends and customers like you that make our business possible.").
                append("</p><p>May your holiday be filled with joy and the coming year be overflowing with all the good things in life.").append("</p><p>Sincerely Yours, &#160;").
                append(StringEscapeUtils.escapeHtml(getFirstName())).append(" &#160; ").
                append(StringEscapeUtils.escapeHtml(getLastName())).append(".</p>");
        return contentBuilder.toString();
    }
}

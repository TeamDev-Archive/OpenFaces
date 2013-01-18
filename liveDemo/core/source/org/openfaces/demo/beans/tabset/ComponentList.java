/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
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
import java.util.ArrayList;
import java.util.List;

public class ComponentList implements Serializable {

    private List<Component> componentsList = new ArrayList<Component>();
    private String currentComponentName;

    public ComponentList() {
        currentComponentName = "Calendar";
        componentsList.add(new Component("calendar_thumb.jpg", "Calendar", getCalendarString()));
        componentsList.add(new Component("confirm_th.jpg", "Confirmation", getConfirmationString()));
        componentsList.add(new Component("datach_th.jpg", "Date Chooser", getDateChooserString()));
        componentsList.add(new Component("dropdown_th.gif", "Drop Down", getDropDownString()));
        componentsList.add(new Component("tabpane_th.jpg", "Tabbed Pane", getTabbedPaneString()));
    }

    public List<Component> getComponentsList() {
        return componentsList;
    }

    public String getSelectedComponentDescription() {
        String currentComponentDescription = "";
        for (Component temp : componentsList) {
            if (temp.getComponentName().equals(currentComponentName))
                currentComponentDescription = temp.getComponentInfo();
        }
        return currentComponentDescription;
    }

    public String getSelectedComponentImageUrl() {
        String currentComponentImageUrl = "";
        for (Component temp : componentsList) {
            if (temp.getComponentName().equals(currentComponentName))
                currentComponentImageUrl = temp.getComponentImageUrl();
        }
        return currentComponentImageUrl;
    }

    public String getSelectedComponentName() {
        String currentComponentName = "";
        for (Component temp : componentsList) {
            if (temp.getComponentName().equals(this.currentComponentName))
                currentComponentName = temp.getComponentName();
        }
        return currentComponentName;
    }

    public String getCalendarString() {
        StringBuffer calendarSB = new StringBuffer();
        calendarSB.append("The Calendar component consists of three section: header, body and footer. There is table with calendar days in the body. ");
        calendarSB.append("Each column has a caption displaying weekday. And each row is one week. When user point mouse pointer over some date hint with following text appears: \"Day of year: NUMBER\", where NUMBER is number of day in the year. ");
        calendarSB.append("There are displayed month, year and week number in the header.");
        return calendarSB.toString();
    }

    public String getConfirmationString() {
        return "The Confirmation component is DHTML modal window with text and two buttons. Confirmation component is used to confirm of cancel some action.";
    }

    public String getDateChooserString() {
        StringBuffer dateChooserSB = new StringBuffer();
        dateChooserSB.append("The DateChooser is editable text field for input date. ");
        dateChooserSB.append("User can input date either by typing it right in the text field or by selecting it from the calendar, that appears in the popup if user clicks button next to text field.");
        return dateChooserSB.toString();
    }

    public String getDropDownString() {
        return "DropDownField is a component that provides a way to enter text either by typing it right in the text field or by selecting items from an associated drop-down list.";
    }

    public String getTabbedPaneString() {
        StringBuffer tabbedPaneSB = new StringBuffer();
        tabbedPaneSB.append("The TabbedPane component is a container for other components. ");
        tabbedPaneSB.append("The TabbedPane lets user switch between groups of components, that are shared the same place, with tabs.");
        return tabbedPaneSB.toString();
    }

    public String getCurrentComponentName() {
        return currentComponentName;
    }

    public void setCurrentComponentName(String currentComponentName) {
        this.currentComponentName = currentComponentName;
    }

    public String getBrTag() {
        return "<br/>";
    }
}
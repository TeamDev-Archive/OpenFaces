/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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

public class SelectorStyle implements Serializable {
    private String name;
    private String rolloverTabStyle;
    private String rolloverSelectedTabStyle;
    private String selectedTabStyle;

    private String tabStyle;
    private String frontBorderStyle;
    private String backBorderStyle;

    public SelectorStyle(String name,
                         String tabStyle,
                         String rolloverTabStyle,
                         String selectedTabStyle,
                         String rolloverSelectedTabStyle,
                         String frontBorderStyle,
                         String backBorderStyle) {
        this.name = name;
        this.rolloverTabStyle = rolloverTabStyle;
        this.rolloverSelectedTabStyle = rolloverSelectedTabStyle;
        this.selectedTabStyle = selectedTabStyle;
        this.tabStyle = tabStyle;
        this.frontBorderStyle = frontBorderStyle;
        this.backBorderStyle = backBorderStyle;
    }

    public String getRolloverTabStyle() {
        return rolloverTabStyle;
    }

    public String getRolloverSelectedTabStyle() {
        return rolloverSelectedTabStyle;
    }

    public String getSelectedTabStyle() {
        return selectedTabStyle;
    }

    public String getTabStyle() {
        return tabStyle;
    }

    public String getFrontBorderStyle() {
        return frontBorderStyle;
    }

    public String getName() {
        return name;
    }

    public String getBackBorderStyle() {
        return backBorderStyle;
    }
}

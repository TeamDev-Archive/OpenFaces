/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.support.QKS371;

public class QKS371 {
    public boolean loginSuccessful = false;
    public int selectedIndex = 0;

    private String selectedDropDownItem = "Tab 1";


    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public void setLoginSuccessful(boolean loginSuccessful) {
        this.loginSuccessful = loginSuccessful;
    }

    public String doLogin() {
        loginSuccessful = true;
        return null;
    }

    public String doLogout() {
        loginSuccessful = false;
        return null;
    }


    public String getSelectedDropDownItem() {
        return selectedDropDownItem;
    }

    public void setSelectedDropDownItem(String selectedDropDownItem) {
        this.selectedDropDownItem = selectedDropDownItem;
    }
}

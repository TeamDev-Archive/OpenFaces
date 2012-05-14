/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.datatable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class DataTableLoadingModeBean {

    private List<String> loadingModeList = new ArrayList<String>();
    private int loadingModeSelectedIndex;

    public DataTableLoadingModeBean() {
        loadingModeList.add("true");
        loadingModeList.add("false");
    }


    public List<String> getLoadingModeList() {
        return loadingModeList;
    }

    public void setLoadingModeList(List<String> loadingModeList) {
        this.loadingModeList = loadingModeList;
    }

    public int getLoadingModeSelectedIndex() {
        return loadingModeSelectedIndex;
    }

    public void setLoadingModeSelectedIndex(int loadingModeSelectedIndex) {
        this.loadingModeSelectedIndex = loadingModeSelectedIndex;
    }

    public Boolean getUseAjax() {
        String selectedValue = loadingModeList.get(loadingModeSelectedIndex);
        return Boolean.valueOf(selectedValue);
    }

}
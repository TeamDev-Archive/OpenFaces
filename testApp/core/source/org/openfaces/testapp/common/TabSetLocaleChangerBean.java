/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class TabSetLocaleChangerBean {

    private int selectedLocaleIndex;
    
    private List locales = new ArrayList();

    public TabSetLocaleChangerBean() {
        locales = Arrays.asList("ar", "es", "ja", "ru");
    }

    public int getSelectedLocaleIndex() {
        return selectedLocaleIndex;
    }

    public void setSelectedLocaleIndex(int selectedLocaleIndex) {
        this.selectedLocaleIndex = selectedLocaleIndex;
    }

    public String getSelectedLocale() {
        return (String) locales.get(selectedLocaleIndex);
    }

    public List getLocales() {
        return locales;
    }

}
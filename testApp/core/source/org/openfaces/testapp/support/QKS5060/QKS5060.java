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
package org.openfaces.testapp.support.QKS5060;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ilya Musihin
 */
public class QKS5060 {

    private String selectedUnidadNegocio;

    public List<String> getUnidadNegocio() {
        List<String> returnValue = new ArrayList<String>();
        returnValue.add("Day");
        returnValue.add("Night");
        return returnValue;
    }

    public List<String> getVendedorUnidadNegocio() {
        ArrayList<String> arrayList = new ArrayList<String>();
        if ("Day".equals(selectedUnidadNegocio)) {
            arrayList.add("08:00");
            arrayList.add("12:00");
            arrayList.add("16:00");
            arrayList.add("18:00");
        } else if ("Night".equals(selectedUnidadNegocio)) {
            arrayList.add("20:00");
            arrayList.add("22:00");
            arrayList.add("02:00");
            arrayList.add("06:00");
        } else {
            arrayList.add("Please, select first");
        }
        return arrayList;
    }

    public String getSelectedUnidadNegocio() {
        return selectedUnidadNegocio;
    }

    public void setSelectedUnidadNegocio(String selectedUnidadNegocio) {
        this.selectedUnidadNegocio = selectedUnidadNegocio;
    }
}

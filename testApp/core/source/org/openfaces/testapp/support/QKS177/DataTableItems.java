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

package org.openfaces.testapp.support.QKS177;


public class DataTableItems {
    private static int idCreated;
    private Integer id = idCreated++;
    private String nomMarche;


    public DataTableItems(String nomMarche) {
        this.nomMarche = nomMarche;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomMarche() {
        return nomMarche;
    }

    public void setNomMarche(String nomMarche) {
        this.nomMarche = nomMarche;
    }
}

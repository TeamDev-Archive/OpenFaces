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
package org.openfaces.testapp.datatable;

/**
 * @author Darya Shumilina
 */
public class SimpleDataTableItem {

    private String firstColumn;
    private String secondColumn;
    private String thirdColumn;
    private String fourthColumn;

    public SimpleDataTableItem(String firstColumn, String secondColumn, String thirdColumn, String fourthColumn) {
        this.firstColumn = firstColumn;
        this.secondColumn = secondColumn;
        this.thirdColumn = thirdColumn;
        this.fourthColumn = fourthColumn;
    }


    public String getFirstColumn() {
        return firstColumn;
    }

    public String getSecondColumn() {
        return secondColumn;
    }

    public String getThirdColumn() {
        return thirdColumn;
    }

    public String getFourthColumn() {
        return fourthColumn;
    }


}
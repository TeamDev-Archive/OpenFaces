/*
 * OpenFaces - JSF Component Library 3.0
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

import java.io.Serializable;

/**
 * @author Darya Shumilina
 */
public class TestTableItem implements Serializable {

    private String firstColumn;
    private String secondColumn;
    private String thirdColumn;
    private String fourthColumn;

    public TestTableItem(String firstColumn, String secondColumn, String thirdColumn, String fourthColumn) {
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

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestTableItem that = (TestTableItem) o;

        if (firstColumn != null ? !firstColumn.equals(that.firstColumn) : that.firstColumn != null) {
            return false;
        }
        if (fourthColumn != null ? !fourthColumn.equals(that.fourthColumn) : that.fourthColumn != null) {
            return false;
        }
        if (secondColumn != null ? !secondColumn.equals(that.secondColumn) : that.secondColumn != null) {
            return false;
        }
        if (thirdColumn != null ? !thirdColumn.equals(that.thirdColumn) : that.thirdColumn != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (firstColumn != null ? firstColumn.hashCode() : 0);
        result = 31 * result + (secondColumn != null ? secondColumn.hashCode() : 0);
        result = 31 * result + (thirdColumn != null ? thirdColumn.hashCode() : 0);
        result = 31 * result + (fourthColumn != null ? fourthColumn.hashCode() : 0);
        return result;
    }
}
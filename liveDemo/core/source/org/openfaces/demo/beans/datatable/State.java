/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.datatable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexei Tymchenko
 */
public class State implements Serializable {
    private String stateName;
    private List<Bank> banks = new ArrayList<Bank>();

    public State(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    public int getQuantity() {
        return banks == null ? 0 : banks.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State that = (State) o;

        if (banks != null ? !banks.equals(that.banks) : that.banks != null) return false;
        if (stateName != null ? !stateName.equals(that.stateName) : that.stateName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stateName != null ? stateName.hashCode() : 0;
        result = 31 * result + (banks != null ? banks.hashCode() : 0);
        return result;
    }
}

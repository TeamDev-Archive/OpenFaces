/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
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

/**
 * @author Darya Shumilina
 */
public class Bank implements Serializable {
    private String institutionName;
    private int certificateNumber;
    private String city;
    private String state;
    private int zip;
    private String county;
    private int averageAssets;

    public Bank(
            String institutionName,
            int certificateNumber,
            String city,
            String state,
            int zip,
            String county,
            int averageAssets) {
        this.institutionName = institutionName;
        this.certificateNumber = certificateNumber;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.county = county;
        this.averageAssets = averageAssets;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public int getCertificateNumber() {
        return certificateNumber;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getZip() {
        return zip;
    }

    public String getCounty() {
        return county;
    }

    public int getAverageAssets() {
        return averageAssets;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bank)) return false;

        Bank bank = (Bank) o;

        if (averageAssets != bank.averageAssets) return false;
        if (certificateNumber != bank.certificateNumber) return false;
        if (zip != bank.zip) return false;
        if (city != null ? !city.equals(bank.city) : bank.city != null) return false;
        if (county != null ? !county.equals(bank.county) : bank.county != null) return false;
        if (institutionName != null ? !institutionName.equals(bank.institutionName) : bank.institutionName != null)
            return false;
        if (state != null ? !state.equals(bank.state) : bank.state != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (institutionName != null ? institutionName.hashCode() : 0);
        result = 31 * result + certificateNumber;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + zip;
        result = 31 * result + (county != null ? county.hashCode() : 0);
        result = 31 * result + averageAssets;
        return result;
    }
}
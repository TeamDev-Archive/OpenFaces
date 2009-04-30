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

public class FeatureItem implements Serializable {
    private String featureName;
    private boolean pro;
    private boolean home;
    private boolean standard;
    private boolean lite;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeatureItem)) return false;

        FeatureItem that = (FeatureItem) o;

        if (home != that.home) return false;
        if (lite != that.lite) return false;
        if (pro != that.pro) return false;
        if (standard != that.standard) return false;
        if (!featureName.equals(that.featureName)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = featureName.hashCode();
        result = 31 * result + (pro ? 1 : 0);
        result = 31 * result + (home ? 1 : 0);
        result = 31 * result + (standard ? 1 : 0);
        result = 31 * result + (lite ? 1 : 0);
        return result;
    }

    public FeatureItem(String featureName, boolean pro, boolean home, boolean standard, boolean lite) {
        this.featureName = featureName;
        this.pro = pro;
        this.home = home;
        this.standard = standard;
        this.lite = lite;
    }

    public String getFeatureName() {
        return featureName;
    }

    public boolean isPro() {
        return pro;
    }

    public boolean isHome() {
        return home;
    }

    public boolean isStandard() {
        return standard;
    }

    public boolean isLite() {
        return lite;
    }
}

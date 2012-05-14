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

package org.openfaces.demo.beans.datatable;

import java.io.Serializable;

public class DeptItem implements Serializable {

    private String deptName;
    private String year2003;
    private String year2004;
    private String year2005;
    private String year2006;
    private String total;

    public DeptItem(String deptName, String year2003, String year2004, String year2005, String year2006, String total) {
        this.deptName = deptName;
        this.year2003 = year2003;
        this.year2004 = year2004;
        this.year2005 = year2005;
        this.year2006 = year2006;
        this.total = total;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getYear2003() {
        return year2003;
    }

    public String getYear2004() {
        return year2004;
    }

    public String getYear2005() {
        return year2005;
    }

    public String getYear2006() {
        return year2006;
    }

    public String getTotal() {
        return total;
    }
}
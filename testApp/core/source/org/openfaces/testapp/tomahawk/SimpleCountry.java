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
package org.openfaces.testapp.tomahawk;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author Thomas Spiegl (latest modification by $Author: werpu $)
 * @version $Revision: 371731 $ $Date: 2006-01-24 00:18:44 +0000 (Tue, 24 Jan 2006) $
 */
public class SimpleCountry implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String name;
    private String isoCode;
    private BigDecimal size;
    private boolean remove = false;
    private List cities;
    private String mSortCitiesColumn;
    private boolean mIsSortCitiesAscending;

    public SimpleCountry(long id, String name, String isoCode, BigDecimal size, SimpleCity[] cities) {
        this.id = id;
        this.name = name;
        this.isoCode = isoCode;
        this.size = size;

        if (cities != null)
            this.cities = new ArrayList(Arrays.asList(cities));
        else
            this.cities = new ArrayList();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public BigDecimal getSize() {
        return size;
    }

    public List getCities() {
        if (mSortCitiesColumn != null) {
            Collections.sort(cities, new Comparator() {
                public int compare(Object arg0, Object arg1) {
                    SimpleCity lhs;
                    SimpleCity rhs;
                    if (isSortCitiesAscending()) {
                        lhs = (SimpleCity) arg0;
                        rhs = (SimpleCity) arg1;
                    } else {
                        rhs = (SimpleCity) arg0;
                        lhs = (SimpleCity) arg1;
                    }
                    String lhsName = lhs.getName();
                    String rhsName = rhs.getName();
                    if (lhsName != null) {
                        if (rhsName != null) {
                            return lhsName.compareToIgnoreCase(rhsName);
                        }
                        return -1;
                    } else if (rhsName != null) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
        return cities;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public String addCity() {
        getCities().add(new SimpleCity());
        return null;
    }

    public void deleteCity(ActionEvent ev) {
        UIData datatable = findParentHtmlDataTable(ev.getComponent());
        getCities().remove(datatable.getRowIndex() + datatable.getFirst());
    }

    public void setSortCitiesColumn(String columnName) {
        mSortCitiesColumn = columnName;
    }

    /**
     * @return Returns the sortCitiesColumn.
     */
    public String getSortCitiesColumn() {
        return mSortCitiesColumn;
    }

    public boolean isSortCitiesAscending() {
        return mIsSortCitiesAscending;
    }

    /**
     * @param isSortCitiesAscending The isSortCitiesAscending to set.
     */
    public void setSortCitiesAscending(boolean isSortCitiesAscending) {
        mIsSortCitiesAscending = isSortCitiesAscending;
    }

    private HtmlDataTable findParentHtmlDataTable(UIComponent component) {
        while (true) {
            if (component == null) {
                return null;
            }
            if (component instanceof HtmlDataTable) {
                return (HtmlDataTable) component;
            }
            component = component.getParent();
        }
    }
}

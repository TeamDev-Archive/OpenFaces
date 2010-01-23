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
package org.openfaces.component;

import org.openfaces.component.filter.Filter;

import javax.faces.context.FacesContext;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public interface FilterableComponent {
    /**
     * @return a modifiable set of filters attached to this component.
     */
    List<Filter> getFilters();

    /**
     * @return name of a variable that refers to a current record in this component.
     */
    String getVar();

    /**
     * @return the default text for the "all records" filters in this component. This text
     * can also be customized on a per-filter basis for individual customization.
     */
    String getAllRecordsFilterText();
    void setAllRecordsFilterText(String allRecordsFilterText);

    /**
     * @return the default text for the "empty records" filters in this component. This text
     * can also be customized on a per-filter basis for individual customization.
     */
    String getEmptyRecordsFilterText();
    void setEmptyRecordsFilterText(String emptyRecordsFilterText);

    /**
     * @return the default text for the "non-empty records" filters in this component. This text
     * can also be customized on a per-filter basis for individual customization.
     */
    String getNonEmptyRecordsFilterText();
    void setNonEmptyRecordsFilterText(String nonEmptyRecordsFilterText);

    int getAutoFilterDelay();
    void setAutoFilterDelay(int autoFilterDelay);


    public List getRowListForFiltering(Filter filter);

    Object getFilteredValueByData(
            FacesContext facesContext,
            Object data,
            Object expression
    );

    void filterChanged(Filter filter);

}

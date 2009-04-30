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
package org.openfaces.component.table;

/**
 * @author Dmitry Pikhulya
 */
public class ComboBoxDataTableFilter extends DataTableFilter {
    public static final String COMPONENT_FAMILY = "org.openfaces.ComboBoxDataTableFilter";
    public static final String COMPONENT_TYPE = "org.openfaces.ComboBoxDataTableFilter";

    public ComboBoxDataTableFilter() {
        setRendererType("org.openfaces.ComboBoxDataTableFilterRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    protected boolean acceptsString(String filteredValueString, FilterCriterion filterCriterion) {
        if (!(filterCriterion instanceof ColumnFilterCriterion))
            throw new IllegalStateException("Wrong criterion type: " + filterCriterion);

        if (filterCriterion instanceof TextFilterCriterion) {
            TextFilterCriterion textFilterCriterion = (TextFilterCriterion) filterCriterion;
            String text = textFilterCriterion.getText();
            return text.equals(filteredValueString);
        }

        if (filterCriterion instanceof EmptyRecordsCriterion)
            return filteredValueString.length() == 0;

        if (filterCriterion instanceof NonEmptyRecordsCriterion) {
            return filteredValueString.length() > 0;
        }
        throw new IllegalStateException("Unknown filtering criterion: " + filterCriterion);
    }

    protected boolean isShowingPredefinedCriterionNames() {
        return true;
    }
}

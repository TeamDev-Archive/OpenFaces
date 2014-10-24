/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.table.impl;

import org.openfaces.component.table.AbstractTable;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.Comparator;
import java.util.Map;

/**
* @author Dmitry Pikhulya
*/
public class RowComparator implements Comparator<Object> {
    private final AbstractTable table;
    private final FacesContext facesContext;
    private final ValueExpression sortingExpressionBinding;
    private final Comparator<Object> valueComparator;
    private final Map<String, Object> requestMap;
    private final boolean sortAscending;
    private Object comparisonValue1;
    private Object comparisonValue2;

    protected final String var;

    public RowComparator(
            AbstractTable table,
            FacesContext facesContext,
            ValueExpression valueExpression,
            Comparator<Object> valueComparator,
            Map<String, Object> requestMap,
            boolean sortAscending) {
        this.table = table;
        this.facesContext = facesContext;
        sortingExpressionBinding = valueExpression;
        this.valueComparator = valueComparator;
        this.requestMap = requestMap;
        var = table.getVar();
        this.sortAscending = sortAscending;
    }

    public int compare(Object o1, Object o2) {
        Runnable restorePrevParams = table.populateSortingExpressionParams(var, requestMap, o1);
        ELContext elContext = facesContext.getELContext();
        comparisonValue1 = sortingExpressionBinding.getValue(elContext);
        restorePrevParams.run();
        restorePrevParams = table.populateSortingExpressionParams(var, requestMap, o2);
        comparisonValue2 = sortingExpressionBinding.getValue(elContext);
        restorePrevParams.run();
        int result;
        if (comparisonValue1 == null)
            result = (comparisonValue2 == null) ? 0 : -1;
        else if (comparisonValue2 == null)
            result = 1;
        else if (valueComparator != null) {
            result = valueComparator.compare(comparisonValue1, comparisonValue2);
        } else if (comparisonValue1 instanceof Comparable) {
            result = ((Comparable) comparisonValue1).compareTo(comparisonValue2);
        } else {
            throw new RuntimeException("The values to be sorted must implement the Comparable interface: " + comparisonValue1.getClass());
        }
        if (!sortAscending)
            result = -result;
        return result;
    }

    /**
     * @return the actual value which was participating in the last comparison session (first comparison argument)
     */
    public Object getComparisonValue1() {
        return comparisonValue1;
    }

    /**
     * @return the actual value which was participating in the last comparison session (second comparison argument)
     */
    public Object getComparisonValue2() {
        return comparisonValue2;
    }

}

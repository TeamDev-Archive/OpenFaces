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
package org.openfaces.component.util;

/**
 * IterationStatus is simple storage object class for OpenFaces's <code>ForEach</code> component class.
 * After it is been created all it's values may be read only. It's constructor called by <code>org.openfaces.component.foreach.ForEach</code> class into <code>setVarStatus()</code> method.
 *
 * @author Alexey Tarasyuk
 * @see ForEach
 */
public class IterationStatus {
    private final Object current;
    private final int index;
    private final int count;
    private final boolean first;
    private final boolean last;
    private final Integer begin;
    private final Integer end;
    private final Integer step;

    /**
     * see constructor call sequence into setVarStatus() method of org.openfaces.component.foreach.ForEach class.
     *
     * @param current is current iterated object in the value-bound collection of data. If no value-bound collection is exist <code>current</code> must be a <code>null</code>.
     * @param index   is index of current iteration. If value-bound data collection is specified it also the index of currend data object into collection.
     * @param count   is count of current iteration.
     * @param first   is equal <code>true</code> if current index is first into iteration sequence and it is equal <code>false</code> otherwise.
     * @param last    is equal <code>true</code> if current index is last into iteration sequence and it is equal <code>false</code> otherwise.
     * @param begin   is <code>begin</code> tag attribute's value of <code>&lt;o:forEach&gt;</code> tag.
     * @param end     is <code>end</code> tag attribute's value of <code>&lt;o:forEach&gt;</code> tag.
     * @param step    is <code>step</code> attribute's value of <code>&lt;o:forEach&gt;</code> tag.
     * @see ForEach#setIterationStatus()
     */
    public IterationStatus(Object current,
                           int index,
                           int count,
                           boolean first,
                           boolean last,
                           Integer begin,
                           Integer end,
                           Integer step) {
        this.current = current;
        this.index = index;
        this.count = count;
        this.first = first;
        this.last = last;
        this.begin = begin;
        this.end = end;
        this.step = step;
    }

    /**
     * Retrieves the current item in the value-bound collection of data.
     *
     * @return current item as an <code>java.lang.Object</code> or <code>null</code> if no value-bound data collection is specified.
     */
    public Object getCurrent() {
        return current;
    }

    /**
     * <p>Retrieves the index of the current round of the iteration. If <code>firstIndex</code> tag attribute is specified <code>index</code> start from it's value. If value-bound data collection is specified it also the index of currend data object into collection.</p>
     * <p/>
     * <p>As an example, an iteration with firstIndex = 10, lastIndex = 5, and step = -1 produces the indexes 10, 9, 8, 7, 6 and 5 in that order (of course if value-bound collection allow this indexes, or if it is not specified completely).</p>
     *
     * @return current index of the iteration
     */
    public int getIndex() {
        return index;
    }

    /**
     * <p>Retrieves the "count" of the current round of the iteration. The count is a relative, 1-based sequence number identifying the current "round" of iteration (in context with all rounds the current iteration will perform).</p>
     * <p/>
     * <p>As an example, an iteration with begin = 5, end = 15, and step = 5 produces the counts 1, 2, and 3 in that order.</p>
     *
     * @return the 1-based count of the current round of the iteration
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns information about whether the current round of the iteration is the first one. This current round may be the 'first' even when <code>getIndex() != 0</code>, for 'index' refers to the absolute index of the current 'item' in the context of its underlying collection. It is always that case that a <code>true</code> result from <code>isFirst()</code> implies <code>getCount() == 1</code>.
     *
     * @return <code>true</code> if the current round is the first in the iteration, <code>false</code> otherwise.
     */
    public boolean isFirst() {
        return first;
    }

    /**
     * Returns information about whether the current round of the iteration is the last one. As with <code>isFirst()</code>, subsetting is taken into account. <code>isLast()</code> is refer to whether or not the current round will be the final round of iteration.
     *
     * @return <code>true</code> if the current round is the last in the iteration, <code>false</code> otherwise.
     */
    public boolean isLast() {
        return last;
    }

    /**
     * Returns the value of the <code>'begin'</code> tag attribute's value, or null if no <code>'begin'</code> attribute was specified.
     *
     * @return the <code>'begin'</code> value for the associated <code>&lt;o:forEach&gt;</code> tag, or null if no <code>'begin'</code> attribute was specified.
     */
    public Integer getBegin() {
        return begin;
    }

    /**
     * Returns the value of the <code>'end'</code> tag attribute's value, or null if no <code>'end'</code> attribute was specified.
     *
     * @return the <code>'end'</code> value for the associated <code>&lt;o:forEach&gt;</code> tag, or null if no <code>'end'</code> attribute was specified.
     */
    public Integer getEnd() {
        return end;
    }

    /**
     * Returns the value of the <code>'step'</code> tag attribute's value, or null if no <code>'step'</code> attribute was specified.
     *
     * @return the <code>'step'</code> value for the associated <code>&lt;o:forEach&gt;</code> tag, or null if no <code>'step'</code> attribute was specified.
     */
    public Integer getStep() {
        return step;
    }
}

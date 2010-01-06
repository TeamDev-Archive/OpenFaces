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
package org.openfaces.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Andrew Palval
 */
public class StyleGroup implements Serializable, Comparable {
    private static final String REGULAR_GROUP_TYPE = "REGULAR";
    private static final String ROLLOVER_GROUP_TYPE = "ROLLOVER";
    private static final String SELECTED_GROUP_TYPE = "SELECTED";
    private static final String DISABLED_GROUP_TYPE = "DISABLED";

    private static final List<String> GROUP_TYPES_ORDER = Arrays.asList(
            REGULAR_GROUP_TYPE,
            ROLLOVER_GROUP_TYPE,
            SELECTED_GROUP_TYPE,
            DISABLED_GROUP_TYPE);

    private static SortedSet<StyleGroup> allGroups = new TreeSet<StyleGroup>();

    public static SortedSet<StyleGroup> getAllGroups() {
        return allGroups;
    }

    private static final StyleGroup REGULAR_STYLE_GROUP = regularStyleGroup(0);
    private static final StyleGroup ROLLOVER_STYLE_GROUP = rolloverStyleGroup(0);
    private static final StyleGroup SELECTED_STYLE_GROUP = selectedStyleGroup(0);
    private static final StyleGroup DISABLED_STYLE_GROUP = disabledStyleGroup(0);

    public static StyleGroup regularStyleGroup() {
        return REGULAR_STYLE_GROUP;
    }

    public static StyleGroup regularStyleGroup(int index) {
        return new StyleGroup(index, REGULAR_GROUP_TYPE);
    }

    public static StyleGroup rolloverStyleGroup() {
        return ROLLOVER_STYLE_GROUP;
    }

    public static StyleGroup rolloverStyleGroup(int index) {
        return new StyleGroup(index, ROLLOVER_GROUP_TYPE);
    }

    public static StyleGroup selectedStyleGroup() {
        return SELECTED_STYLE_GROUP;
    }

    public static StyleGroup selectedStyleGroup(int index) {
        return new StyleGroup(index, SELECTED_GROUP_TYPE);
    }

    public static StyleGroup disabledStyleGroup() {
        return DISABLED_STYLE_GROUP;
    }

    public static StyleGroup disabledStyleGroup(int index) {
        return new StyleGroup(index, DISABLED_GROUP_TYPE);
    }


    private String groupType;
    private int index = 0;

    private StyleGroup(int index, String type) {
        if (index < 0)
            throw new IllegalArgumentException("Index should be equal or greater than 0");
        groupType = type;
        this.index = index;
        allGroups.add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final StyleGroup that = (StyleGroup) o;

        if (index != that.index) return false;
        if (groupType != null ? !groupType.equals(that.groupType) : that.groupType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (groupType != null ? groupType.hashCode() : 0);
        result = 29 * result + index;
        return result;
    }

    public int compareTo(Object o) {
        if (o == null) return 1;
        StyleGroup that = (StyleGroup) o;
        int thisGroupTypeIndex = GROUP_TYPES_ORDER.indexOf(this.groupType);
        int thatGroupTypeIndex = GROUP_TYPES_ORDER.indexOf(that.groupType);
        if (thisGroupTypeIndex < thatGroupTypeIndex)
            return -1;
        if (thisGroupTypeIndex > thatGroupTypeIndex)
            return +1;
        return this.index - that.index;
    }

    @Override
    public String toString() {
        String result = groupType;
        if (index > 0)
            result += "_" + index;
        result += ":";
        return result;
    }

}

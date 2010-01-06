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
package org.openfaces.component.chart;

import java.util.Comparator;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public interface Sorter {
    public static final Comparator<Tuple> ASCENDING = new Comparator<Tuple>() {
        public int compare(Tuple t1, Tuple t2) {
            return t1.compareTo(t2);
        }
    };
    public static final Comparator<Tuple> DESCENDING = new Comparator<Tuple>() {
        public int compare(Tuple t1, Tuple t2) {
            return -t1.compareTo(t2);
        }
    };
    public static final Comparator<Tuple> NONE = new Comparator<Tuple>() {
        public int compare(Tuple t1, Tuple t2) {
            //invoke compareTo method just to be sure that keys ara comparable types
            t1.compareTo(t2);
            return 0;
        }
    };

}

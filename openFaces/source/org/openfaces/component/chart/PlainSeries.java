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
package org.openfaces.component.chart;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class PlainSeries implements Series, Externalizable {
    /**
     * For serialization.
     */
    private static final long serialVersionUID = 8100508075695195294L;

    private Comparable key;
    private Map data;
    private Comparator<Tuple> comparator;
    private Comparator<Tuple> checkComparator = Sorter.NONE;
    private Tuple[] tuples;

    public PlainSeries() {
        this(0);
    }

    public PlainSeries(Comparable key) {
        this.key = key;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public Comparator<Tuple> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<Tuple> comparator) { //todo(question): why do we need comparator?
        this.comparator = comparator;                       //todo: there are some problems: (1) exposing the Sorter interface to end users (2) usages of setComparator show that establishing the original (unsorted) tuple order should be done with custom comparator
    }

    public Comparable getKey() {
        return key;
    }

    public void setKey(Comparable seriesKey) {
        key = seriesKey;
    }

    public Tuple[] getTuples() {
        //if restored after serialization
        if (tuples != null)
            return tuples;

        Tuple[] resultTuples = null;
        List<Tuple> tuples = new ArrayList<Tuple>();
        if (data != null && data.keySet() != null) {
            for (Object key : data.keySet()) {
                Object value = data.get(key);
                if (key != null) {
                    Tuple tuple = new Tuple(key, value);
                    tuples.add(tuple);
                }
            }
            Comparator<Tuple> tupleComparator = getComparator();
            if (tupleComparator != null)
                Collections.sort(tuples, tupleComparator);
            else
                Collections.sort(tuples, checkComparator);

            resultTuples = tuples.toArray(new Tuple[tuples.size()]);
        }

        return resultTuples;
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Map t = (Map) in.readObject();
        key = (Comparable) t.get("key");
        tuples = (Tuple[]) t.get("tuples");
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", getKey());
        map.put("tuples", getTuples());
        out.writeObject(map);
    }
}

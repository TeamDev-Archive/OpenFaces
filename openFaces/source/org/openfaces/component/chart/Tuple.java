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

import java.io.Serializable;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class Tuple implements Comparable, Serializable {
    private Comparable<Comparable> key;
    private Object value;
    /**
     * For serialization.
     */
    private static final long serialVersionUID = 8199508075695195293L;

    public Tuple(Object value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public Tuple(Object key, Object value) {
        this(value);
        if (!(key instanceof Comparable)) //todo(question): why require the key to be comparable - what about the usecase where the original tuple order must be preserved?
            throw new IllegalArgumentException("Key must implement Comparable. key == " + key);
        this.key = (Comparable<Comparable>) key;
    }


    public Comparable getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public int compareTo(Object o) {
        if (o == null)
            throw new NullPointerException();
        if (!(o instanceof Tuple))
            throw new IllegalArgumentException("Attempt to compare Tuple and " + o.getClass());
        Tuple that = (Tuple) o;
        if (!(key.getClass().equals(that.getKey().getClass())))
            throw new IllegalArgumentException("Different key types: " + this.getKey().getClass() + " vs " + that.getKey().getClass());
        return key.compareTo(((Tuple) o).getKey());
    }
}

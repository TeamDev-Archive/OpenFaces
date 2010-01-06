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
package org.openfaces.renderkit;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class DefaultImageDataModel implements ImageDataModel, Serializable {

    private byte[] data = new byte[0];

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final DefaultImageDataModel that = (DefaultImageDataModel) o;

        return Arrays.equals(data, that.data);

    }

    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
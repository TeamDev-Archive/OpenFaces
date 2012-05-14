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
package org.openfaces.component.output;

import java.io.ObjectStreamException;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public enum ImageType {
    PNG("ImageType.PNG"),
    GIF("ImageType.GIF"),
    JPEG("ImageType.JPEG");

    private String name;

    ImageType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns a file extension representing current image type
     *
     * @return The string.
     */
    public String getExtension() {
        int i = name.indexOf(".");
        return name.substring(i + 1).toLowerCase();
    }

    /**
     * Ensures that serialization returns the unique instances.
     *
     * @return The object.
     * @throws java.io.ObjectStreamException if there is a problem.
     */
    private Object readResolve() throws ObjectStreamException {
        if (this.equals(ImageType.PNG)) {
            return ImageType.PNG;
        } else if (this.equals(ImageType.GIF)) {
            return ImageType.GIF;
        } else if (this.equals(ImageType.JPEG)) {
            return ImageType.JPEG;
        }
        return null;
    }

}


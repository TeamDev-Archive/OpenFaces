/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.input;

import java.util.Collection;

/**
 * @author Sergey Pensov
 */
public enum DefaultMasks {
    Date("########", "DD/MM/YYYY"),
    Time("####", "__:__"),
    NetMask("#########", "   .   .   .   "),
    CreditCard("################", "---- ---- ---- ----"),
    Percent("###", "___%");

    private String mask;
    private String blank;
    private Collection<MaskDynamicConstructor> dynamicConstructors;

    public String getMask() {
        return mask;
    }

    public String getBlank() {
        return blank;
    }

    DefaultMasks(String mask, String blank) {
        this.mask = mask;
        this.blank = blank;

    }


}
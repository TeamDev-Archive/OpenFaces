/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.input;

/**
 * @author Sergey Pensov
 */
public class MaskSymbolConstructor {
    private Character symbolFromBlank;
    private String symbols;

    public MaskSymbolConstructor(Character symbolFromBlank, String symbols) {
        this.symbolFromBlank = symbolFromBlank;
        this.symbols = symbols;
    }


    public void setSymbolFromBlank(Character symbolFromBlank) {
        this.symbolFromBlank = symbolFromBlank;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public Character getSymbolFromBlank() {

        return symbolFromBlank;
    }

    public String getSymbols() {
        return symbols;
    }

    @Override
    public String toString() {
        return "[" + symbolFromBlank + ","
                + symbols + "]";

    }
}

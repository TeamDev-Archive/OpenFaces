/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.table;

/**
 * @author Dmitry Pikhulya
 */
public class TextFilterCriterion extends ColumnFilterCriterion {
    private String text = "";

    public TextFilterCriterion() {
    }

    public TextFilterCriterion(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text must be not-null");
        }
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean equals(Object obj) {
        boolean superEquals = super.equals(obj);
        if (!superEquals)
            return false;
        if (!(obj instanceof TextFilterCriterion))
            return false;
        TextFilterCriterion that = (TextFilterCriterion) obj;
        if (this.text == null)
            return that.text == null;
        else
            return this.text.equals(that.text);
    }

    @Override
    public int hashCode() {
        if (text != null) {
            return text.hashCode();
        }
        return super.hashCode();
    }
}

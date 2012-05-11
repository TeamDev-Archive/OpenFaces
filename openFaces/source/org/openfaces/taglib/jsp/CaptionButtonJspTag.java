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
package org.openfaces.taglib.jsp;

import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.taglib.internal.CaptionButtonTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class CaptionButtonJspTag extends OUICommandJspTag {
    public CaptionButtonJspTag() {
        super(new CaptionButtonTag());
    }

    protected CaptionButtonJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setPressedStyle(ValueExpression pressedStyle) {
        getDelegate().setPropertyValue("pressedStyle", pressedStyle);
    }

    public void setPressedClass(ValueExpression pressedClass) {
        getDelegate().setPropertyValue("pressedClass", pressedClass);
    }

    public void setImageUrl(ValueExpression imageUrl) {
        getDelegate().setPropertyValue("imageUrl", imageUrl);
    }

    public void setRolloverImageUrl(ValueExpression rolloverImageUrl) {
        getDelegate().setPropertyValue("rolloverImageUrl", rolloverImageUrl);
    }

    public void setPressedImageUrl(ValueExpression pressedImageUrl) {
        getDelegate().setPropertyValue("pressedImageUrl", pressedImageUrl);
    }

    public void setHint(ValueExpression hint) {
        getDelegate().setPropertyValue("hint", hint);
    }

}

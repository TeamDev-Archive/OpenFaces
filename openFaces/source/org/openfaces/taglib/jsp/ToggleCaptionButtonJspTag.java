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

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class ToggleCaptionButtonJspTag extends CaptionButtonJspTag {
    protected ToggleCaptionButtonJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setToggledImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("toggledImageUrl", value);
    }

    public void setToggledRolloverImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("toggledRolloverImageUrl", value);
    }

    public void setToggledPressedImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("toggledPressedImageUrl", value);
    }

}

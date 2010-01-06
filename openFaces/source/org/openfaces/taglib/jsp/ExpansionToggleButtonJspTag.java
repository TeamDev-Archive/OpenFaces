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
package org.openfaces.taglib.jsp;

import org.openfaces.taglib.internal.ExpansionToggleButtonTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class ExpansionToggleButtonJspTag extends ToggleCaptionButtonJspTag {
    public ExpansionToggleButtonJspTag() {
        super(new ExpansionToggleButtonTag());
    }

    public void setExpandedImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("expandedImageUrl", value);
    }

    public void setExpandedRolloverImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("expandedRolloverImageUrl", value);
    }

    public void setExpandedPressedImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("expandedPressedImageUrl", value);
    }

    public void setCollapsedImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("collapsedImageUrl", value);
    }

    public void setCollapsedRolloverImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("collapsedRolloverImageUrl", value);
    }

    public void setCollapsedPressedImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("collapsedPressedImageUrl", value);
    }

}

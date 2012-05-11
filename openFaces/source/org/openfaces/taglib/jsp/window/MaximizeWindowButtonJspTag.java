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
package org.openfaces.taglib.jsp.window;

import org.openfaces.taglib.internal.window.MaximizeWindowButtonTag;
import org.openfaces.taglib.jsp.ToggleCaptionButtonJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class MaximizeWindowButtonJspTag extends ToggleCaptionButtonJspTag {
    public MaximizeWindowButtonJspTag() {
        super(new MaximizeWindowButtonTag());
    }

    public void setRestoreImageUrl(ValueExpression restoreImageUrl) {
        getDelegate().setPropertyValue("restoreImageUrl", restoreImageUrl);
    }

    public void setRestoreRolloverImageUrl(ValueExpression restoreRolloverImageUrl) {
        getDelegate().setPropertyValue("restoreRolloverImageUrl", restoreRolloverImageUrl);
    }

    public void setRestorePressedImageUrl(ValueExpression restorePressedImageUrl) {
        getDelegate().setPropertyValue("restorePressedImageUrl", restorePressedImageUrl);
    }

    public void setMaximizeImageUrl(ValueExpression maximizeImageUrl) {
        getDelegate().setPropertyValue("maximizeImageUrl", maximizeImageUrl);
    }

    public void setMaximizeRolloverImageUrl(ValueExpression maximizeRolloverImageUrl) {
        getDelegate().setPropertyValue("maximizeRolloverImageUrl", maximizeRolloverImageUrl);
    }

    public void setMaximizePressedImageUrl(ValueExpression maximizePressedImageUrl) {
        getDelegate().setPropertyValue("maximizePressedImageUrl", maximizePressedImageUrl);
    }
}

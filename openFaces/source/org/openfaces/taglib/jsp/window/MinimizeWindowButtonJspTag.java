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

import org.openfaces.taglib.internal.window.MinimizeWindowButtonTag;
import org.openfaces.taglib.jsp.ToggleCaptionButtonJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class MinimizeWindowButtonJspTag extends ToggleCaptionButtonJspTag {
    public MinimizeWindowButtonJspTag() {
        super(new MinimizeWindowButtonTag());
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

    public void setMinimizeImageUrl(ValueExpression minimizeImageUrl) {
        getDelegate().setPropertyValue("minimizeImageUrl", minimizeImageUrl);
    }

    public void setMinimizeRolloverImageUrl(ValueExpression minimizeRolloverImageUrl) {
        getDelegate().setPropertyValue("minimizeRolloverImageUrl", minimizeRolloverImageUrl);
    }

    public void setMinimizePressedImageUrl(ValueExpression minimizePressedImageUrl) {
        getDelegate().setPropertyValue("minimizePressedImageUrl", minimizePressedImageUrl);
    }
}

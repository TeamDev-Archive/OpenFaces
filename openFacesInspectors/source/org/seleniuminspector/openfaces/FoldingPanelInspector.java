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
package org.seleniuminspector.openfaces;

import junit.framework.Assert;
import org.openfaces.renderkit.ComponentWithCaptionRenderer;
import org.openfaces.renderkit.panel.FoldingPanelRenderer;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ClientLoadingMode;
import org.seleniuminspector.LoadingMode;

import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class FoldingPanelInspector extends ElementByReferenceInspector {
    public FoldingPanelInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public FoldingPanelInspector(ElementInspector element) {
        super(element);
    }

    public ElementInspector caption() {
        return new ElementByLocatorInspector(id() + ComponentWithCaptionRenderer.CAPTION_SUFFIX);
    }

    public ElementInspector content() {
        return new ElementByLocatorInspector(id() + FoldingPanelRenderer.CONTENT_SUFFIX);
    }

    public ElementInspector toggle() {
        List<ElementInspector> toggles = toggles();
        if (toggles.size() == 0)
            return null;
        return toggles.get(0);
    }

    public List<ElementInspector> toggles() {
        return getElementsByScript("_expansionToggleButtons");
    }

    public boolean isExpanded() {
        return evalBooleanExpression("isExpanded()");
    }

    public void setExpanded(boolean expanded) {
        setExpanded(expanded, ClientLoadingMode.getInstance());
    }

    public void setExpanded(boolean expanded, LoadingMode loadingMode) {
        evalExpression("setExpanded(" + expanded + ")");
        loadingMode.waitForLoad();
        Assert.assertEquals(expanded, isExpanded());
    }
}

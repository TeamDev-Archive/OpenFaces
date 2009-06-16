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
package org.seleniuminspector.openfaces;

import junit.framework.Assert;
import org.openfaces.renderkit.panel.TabbedPaneRenderer;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ClientLoadingMode;
import org.seleniuminspector.LoadingMode;

import java.util.AbstractList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TabbedPaneInspector extends ElementByReferenceInspector {
    public TabbedPaneInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public TabbedPaneInspector(ElementInspector element) {
        super(element);
    }

    public int pageCount() {
        return tabSet().tabs().size();
    }

    public int getPageIndex() {
        return evalIntExpression("getSelectedIndex()");
    }

    public void setPageIndex(int pageIndex) {
        setPageIndex(pageIndex, ClientLoadingMode.getInstance());
    }

    public void setPageIndex(int pageIndex, LoadingMode loadingMode) {
        evalExpression("setSelectedIndex(" + pageIndex + ")");
        loadingMode.waitForLoad();
        Assert.assertEquals(pageIndex, getPageIndex());
    }

    public TabSetInspector tabSet() {
        return new TabSetInspector(id() + "--tabSet");
    }

    /**
     * @return a list of elements corresponding to the content panes for each of the tab. Note that not all of the tab
     *         contents are preloaded at any given moment depending on the loading mode and tab loading history, so it is possible
     *         that some of the returned elements might actually be missing. Use ElementInspector.elementExists to check element
     *         existence.
     */
    public List<ElementInspector> contentPanes() {
        final int pageCount = pageCount();
        return new AbstractList<ElementInspector>() {
            public int size() {
                return pageCount;
            }

            public ElementInspector get(int index) {
                return new ElementByLocatorInspector(id() + TabbedPaneRenderer.PANE_SUFFIX + index);
            }
        };
    }

}

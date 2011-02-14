/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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
import org.openfaces.util.Rendering;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.LoadingMode;
import org.seleniuminspector.ClientLoadingMode;

import java.util.AbstractList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TabSetInspector extends ElementByReferenceInspector {
    public TabSetInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public TabSetInspector(ElementInspector element) {
        super(element);
    }

    public List<ElementInspector> tabs() {
        final int tabCount = evalIntExpression("getTabCount()");
        return new AbstractList<ElementInspector>() {
            public int size() {
                return tabCount;
            }

            public ElementInspector get(int index) {
                return new ElementByLocatorInspector(id() + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + index);
            }
        };
    }

    public int getTabIndex() {
        return evalIntExpression("getSelectedIndex()");
    }

    public void setTabIndex(int pageIndex) {
        setTabIndex(pageIndex, ClientLoadingMode.getInstance());
    }

    public void setTabIndex(int pageIndex, LoadingMode loadingMode) {
        evalExpression("setSelectedIndex(" + pageIndex + ")");
        loadingMode.waitForLoad();
        Assert.assertEquals(pageIndex, getTabIndex());
    }

}

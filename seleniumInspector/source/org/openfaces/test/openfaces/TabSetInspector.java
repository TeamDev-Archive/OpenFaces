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
package org.openfaces.test.openfaces;

import junit.framework.Assert;
import org.openfaces.util.RenderingUtil;
import org.openfaces.test.ElementByLocatorInspector;
import org.openfaces.test.ElementByReferenceInspector;
import org.openfaces.test.ElementInspector;

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
                return new ElementByLocatorInspector(id() + RenderingUtil.CLIENT_ID_SUFFIX_SEPARATOR + index);
            }
        };
    }

    public int getTabIndex() {
        return evalIntExpression("getSelectedIndex()");
    }

    public void setTabIndex(int pageIndex) {
        setTabIndex(pageIndex, LoadingMode.CLIENT);
    }

    public void setTabIndex(int pageIndex, LoadingMode loadingMode) {
        evalExpression("setSelectedIndex(" + pageIndex + ")");
        loadingMode.waitForLoadCompletion();
        Assert.assertEquals(pageIndex, getTabIndex());
    }

}

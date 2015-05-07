/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.tests.components;

import org.apache.commons.lang3.StringUtils;
import org.inspector.components.TabSet;
import org.inspector.components.table.TableCell;
import org.inspector.css.BaseActionLabels;
import org.inspector.navigator.FuncTestsPages;
import org.openfaces.tests.common.BaseSeleniumTest;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Max Yurin
 */
public class TabSetTestCase extends BaseSeleniumTest {
    private static final FuncTestsPages PAGE = FuncTestsPages.TAB_SET;
    private static final String REPORT_BLOCK_ID = "empty";
    String clickableTabSet = "test_form:onclickTabSetID";

    @Test(groups = {"component"})
    public void testCommon() {
        navigateTo(PAGE);
        final TabSet tabSet = getControlFactory().getTabSet(clickableTabSet);
        int counter = 0;

        final Collection<TableCell> tabs = tabSet.getTabs();
        for (TableCell tab : tabs) {
            tab.click();
            sleep(500);
            assertThat("Current tab changed, tab id should changed too",
                    tab.id(),
                    is(clickableTabSet + "::" + String.valueOf(counter++)));
            assertEventFired(BaseActionLabels.ON_CLICK);
        }
    }

    @Test(groups = {"component"}, enabled = false)
    public void testDefaultView() {

    }

    @Test(groups = {"component"}, enabled = false)
    public void testChangeTabUsingAjax() {

    }

    @Test(groups = {"component"}, enabled = false)
    public void testChangeTabWithoutAjax() {

    }

    @Test(groups = {"component"}, enabled = false)
    public void tesClientSideApi() {

    }

    @Test(groups = {"component"}, enabled = false)
    public void testReRenderUsingAjax() {

    }

    @Test(groups = {"component"}, enabled = false)
    public void testSelectionChangeListener() {

    }

    @Test(groups = {"component"}, enabled = false)
    public void testValueChangeListener() {

    }

    private void assertEventFired(BaseActionLabels event) {
        final WebElement reportBlock = findBy(REPORT_BLOCK_ID);
        final String text = reportBlock.getText();
        final String eventName = event.getValue();

        assertThat("Checking report block, should contains: "
                        + eventName + ". But it is: " + (StringUtils.isEmpty(text) ? "empty" : text),
                text.contains(eventName));
    }
}

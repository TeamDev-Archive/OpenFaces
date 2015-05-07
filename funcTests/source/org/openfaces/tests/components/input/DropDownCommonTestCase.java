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

package org.openfaces.tests.components.input;

import com.google.common.collect.Sets;
import org.inspector.api.DropDown;
import org.inspector.api.Popup;
import org.inspector.navigator.FuncTestsPages;
import org.openfaces.tests.common.BaseSeleniumTest;
import org.testng.annotations.Test;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Max Yurin
 */
public class DropDownCommonTestCase extends BaseSeleniumTest {
    private static final FuncTestsPages PAGE = FuncTestsPages.DROPDOWN_FIELD;
    private static final String DROP_DOWN_ID = "formID:selectValue";
    private static final String SELECTED_VALUE_OUTPUT_TEXT_ID = "formID:selectedValue";
    private static final String REPORT_BLOCK_ID = "empty";

    @Test(groups = {"component"})
    public void testListCorrectness() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);

        final Set<String> itemsList = newHashSet(dropDown.showPopup().popup().getItemsList());
        final Set<String> originalDropDownItems = newHashSet("Red", "Yellow", "Blue");

        assertThat("DropDown list: count item is not correct", itemsList.size(), is(originalDropDownItems.size()));
        assertThat("DropDown list: item value is not correct ",
                Sets.difference(itemsList, originalDropDownItems).size(), is(0));

        final String selectedValue = dropDown.showPopup().popup().select(1);

        assertThat("Selected value doesn`t match value is in dropDown field",
                findBy(SELECTED_VALUE_OUTPUT_TEXT_ID).getText(),
                is(selectedValue));
    }

    @Test(groups = {"component"})
    public void testToggleList() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);

        dropDown.showPopup();
        assertThat("Popup is not visible", dropDown.popup().isDisplayed(), is(true));
        sleep(2000);

        dropDown.hidePopup();
        assertThat("Popup is visible", dropDown.popup().isDisplayed(), is(false));
    }

    @Test(groups = {"component"})
    public void testAutoOpening() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        dropDown.showPopup().popup().getItemsList();

        final Popup popup = dropDown.popup();
        final boolean displayed = popup.isDisplayed();

        assertThat("Popup is not visible", displayed, is(true));
    }

    @Test(groups = {"component"})
    public void testValueSelectionFromList() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);

        String selectedValue = dropDown.showPopup().popup().select(1);
        assertThat("Selected value doesn`t match value is in dropDown field",
                dropDown.inputField().getValue(), is(selectedValue));

    }

    @Test(groups = {"component"}, enabled = false)
    public void testAjaxBasedSuggestion() {

    }

    @Test(groups = {"component"}, enabled = false)
    public void testClientBasedSuggestion() {

    }

    @Test(groups = {"component"})
    public void testAutoCompletion() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        dropDown.inputField().setValue("R");

        sleep(2000);

        final String value = dropDown.inputField().getValue();
        assertThat("DropDown value doesn`t match value is in dropDown field", value, is("Red"));
    }

    @Test(groups = {"component"}, enabled = false)
    public void testStyling() {

    }

    @Test(groups = {"component"}, enabled = false)
    public void testNoCachingAutoCompletion() {

    }

    @Test(groups = {"component"}, enabled = false)
    public void testFieldIsNotCleanAfterInput() {

    }

    @Test(groups = {"component"})
    public void testCleanField() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        final String selected = dropDown.showPopup().popup().select(1);
        sleep(2000);

        assertThat("Selected value doesn`t match value is in dropDown field",
                dropDown.inputField().getValue(),
                is(selected));

        dropDown.inputField().clear();
        assertThat("DropDown field value does not cleared", dropDown.inputField().getValue(), is(""));
    }

    @Test(groups = {"component"}, enabled = false)
    public void testClientSideApi() {

    }
}

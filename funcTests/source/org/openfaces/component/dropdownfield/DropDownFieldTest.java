/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.dropdownfield;

import org.junit.Test;

/**
 * @author Darya Shumilina
 */
public class DropDownFieldTest extends BaseDropDownTestCase {
    @Test
    public void testReRenderThroughA4J() {
        checkReRenderingThroughA4J("/components/dropdown/dropDown_a4j.jsf");
    }

    @Test
    public void testListCorrectness() {
        checkListCorrectness("/components/dropdown/dropDownBaseFunctionality.jsf", true);
    }

    @Test
    public void testManualOpeningAndClosing() {
        checkManualListOpeningAndClosing("/components/dropdown/dropDownBaseFunctionality.jsf", true);
    }

    @Test
    public void testValueSelectionFromList() {
        checkValueSelectionFromList("/components/dropdown/dropDownBaseFunctionality.jsf", true);
    }

    @Test
    public void testTypedValue() {
        checkTypedValue("/components/dropdown/dropDownBaseFunctionality.jsf");
    }

    @Test
    public void testAjaxBasedSuggestion() {
        checkAjaxBasedSuggestion("/components/dropdown/dropDownAjaxSuggestion.jsf");
    }

    @Test
    public void testClientBasedSuggestion() {
        checkClientBasedSuggestion("/components/dropdown/dropDownClientSuggestion.jsf");
    }

    @Test
    public void testAutoCompletionFeature() {
        checkAutoCompletionFeature("/components/dropdown/dropDownClientSuggestion.jsf");
    }

    @Test
    public void testStyling() {
        checkStyles(false, true, false, "/components/dropdown/dropDownStyling.jsf", true);
        checkStyles(true, false, false, "/components/dropdown/dropDownStyling.jsf", true);
        checkStyles(false, false, true, "/components/dropdown/dropDownStyling.jsf", true);
        checkStyles(true, true, true, "/components/dropdown/dropDownStyling.jsf", true);
    }

    @Test
    public void testClintSideAPI() {
        checkClintSideAPI("/components/dropdown/dropDownClientSideAPI.jsf");
    }

    @Test
    public void testValueChangeListener() {
        checkValueChangeListener("/components/dropdown/dropDownValueChangeListener.jsf");
    }

}

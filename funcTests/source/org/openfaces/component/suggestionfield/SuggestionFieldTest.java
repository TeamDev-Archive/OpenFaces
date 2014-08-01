/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.suggestionfield;

import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.component.dropdownfield.BaseDropDownTestCase;

/**
 * @author Darya Shumilina
 */
public class SuggestionFieldTest extends BaseDropDownTestCase {

    //todo: uncomment if 'JSFC-3319' is in 'Fixed' state
    @Ignore
    //@Test
    public void _testReRenderThroughA4J() {
        checkReRenderingThroughA4J("/components/suggestionfield/suggestionField_a4j.jsf");
    }

    //@Test
    public void testListCorrectness() {
        checkListCorrectness("/components/dropdown/dropDownBaseFunctionality.jsf", false);
    }

    //@Test
    public void testManualListOpeningAndClosing() {
        checkManualListOpeningAndClosing("/components/dropdown/dropDownBaseFunctionality.jsf", false);
    }

    //@Test
    public void testValueSelectionFromList() {
        checkValueSelectionFromList("/components/suggestionfield/suggestionFieldBaseFunctionality.jsf", false);
    }

    //@Test
    public void testTypedValue() {
        closeBrowser();
        checkTypedValue("/components/suggestionfield/suggestionFieldBaseFunctionality.jsf");
    }

    //@Test
    @Ignore // todo: temporarily ignored because of failures in 2.x branch only
    public void testAjaxBasedSuggestion() {
        checkAjaxBasedSuggestion("/components/suggestionfield/suggestionFieldAjaxSuggestion.jsf");
    }

    //@Test
    public void testClientBasedSuggestion() {
        checkClientBasedSuggestion("/components/suggestionfield/suggestionFieldClientSuggestion.jsf");
    }

    //@Test
    @Ignore // todo: bring this test back when it's clear how to disable skinning in RichFaces 4 (M2)
    public void testStyling() {
        checkStyles(false, true, false, "/components/suggestionfield/suggestionFieldStyling.jsf", false);
        checkStyles(true, false, false, "/components/suggestionfield/suggestionFieldStyling.jsf", false);
        checkStyles(false, false, true, "/components/suggestionfield/suggestionFieldStyling.jsf", false);
        checkStyles(true, true, true, "/components/suggestionfield/suggestionFieldStyling.jsf", false);
    }

    //@Test
    public void testClintSideAPI() {
        checkClintSideAPI("/components/suggestionfield/suggestionFieldClientSideAPI.jsf");
    }

    //@Test
    public void testValueChangeListener() {
        closeBrowser();
        checkValueChangeListener("/components/suggestionfield/suggestionFieldValueChangeListener.jsf");
    }

}

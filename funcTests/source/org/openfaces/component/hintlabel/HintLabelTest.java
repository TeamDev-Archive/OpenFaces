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
package org.openfaces.component.hintlabel;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.HintLabelInspector;
import org.openfaces.test.RichFacesAjaxLoadingMode;
import org.seleniuminspector.ElementInspector;

/**
 * @author Darya Shumilina
 */
public class HintLabelTest extends OpenFacesTestCase {

    @Test
    public void testReRenderThroughA4J() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/hintlabel/hintLabel_a4j.jsf");

        ElementInspector hintLabel = element("formID:hintLabelID");
        String oldValue = hintLabel.text();
        hintLabel.mouseOver();
        String oldHintBody = selenium.getBodyText();
        String[] oldHintValue = oldHintBody.split("<!--");
        String oldHint = oldHintValue[0];
        element("formID:refresher").click();
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        String newValue = hintLabel.text();
        hintLabel.mouseOver();
        String newHintBody = selenium.getBodyText();
        String[] newHintValue = newHintBody.split("<!--");
        String newHint = newHintValue[0];
        assertFalse(newHint.equals(oldHint));
        assertFalse(newValue.equals(oldValue));
    }

    @Test
    public void testDefaultView() {
        testAppFunctionalPage("/components/hintlabel/hintLabel_defaultView.jsf");
        assertAppearanceNotChanged("HintLabelDefaultView");
    }

    @Test
    public void testBasicFunctionality() {
        testAppFunctionalPage("/components/hintlabel/hintLabelBasicFunctionality.jsf");
        sufficientLengthWithoutHintSpecified();
//    insufficientLengthWithoutHintSpecified(selenium);
        sufficientLengthWithHintSpecified();
//    insufficientLengthWithHintSpecified(selenium);
    }

    private void sufficientLengthWithoutHintSpecified() {
        HintLabelInspector firstHintLabel = hintLabel("formID:first");
        firstHintLabel.assertVisible(true);
        firstHintLabel.assertText("Jupiter is more than twice as massive");

        firstHintLabel.hint().assertElementExists(false);
        firstHintLabel.showHintLabel();
        firstHintLabel.hint().assertVisible(false);
    }

//  private void insufficientLengthWithoutHintSpecified(Selenium selenium) {
//    HintLabelInspector secondHintLabel = hintLabel("formID:second");
//    secondHintLabel.assertVisible(true);
//    secondHintLabel.assertText("Jupiter is more than twice as massive as all the other planets combined (the mass of Jupiter is 318 times that of Earth)");
//
//    secondHintLabel.hint().assertElementExists(false);
//    secondHintLabel.showHintLabel();
//    secondHintLabel.hint().assertVisible(true);
//    secondHintLabel.hint().assertText("Jupiter is more than twice as massive as all the other planets combined (the mass of Jupiter is 318 times that of Earth)");
//  }

    private void sufficientLengthWithHintSpecified() {
        HintLabelInspector thirdHintLabel = hintLabel("formID:third");
        thirdHintLabel.assertVisible(true);
        thirdHintLabel.assertText("Jupiter is more than twice as massive");

        thirdHintLabel.hint().assertVisible(false);
        thirdHintLabel.showHintLabel();
        thirdHintLabel.hint().assertVisible(true);
        thirdHintLabel.hint().assertText("Jupiter is more than twice as massive as all the other planets combined (the mass of Jupiter is 318 times that of Earth)");
    }

//  private void insufficientLengthWithHintSpecified(Selenium selenium) {
//    HintLabelInspector forthHintLabel = hintLabel("formID:fourth");
//    forthHintLabel.assertVisible(true);
//    forthHintLabel.assertText("Jupiter is more than twice as massive as all the other planets combined (the mass of Jupiter is 318 times that of Earth)");
//
//    forthHintLabel.hint().assertElementExists(false);
//    forthHintLabel.showHintLabel();
//    forthHintLabel.hint().assertElementExists(true);
//    forthHintLabel.hint().assertText("Jupiter is more than twice as massive as all the other planets combined (the mass of Jupiter is 318 times that of Earth)");
//  }

    @Test
    public void testStyles() {
        testAppFunctionalPage("/components/hintlabel/hintLabelStyles.jsf");
        checkStyles(false);
        checkStyles(true);
    }

    private void checkStyles(boolean makeSubmit) {

        if (makeSubmit) {
            element("formID:submit").clickAndWait();
        }

        HintLabelInspector hintLabel = hintLabel("formID:hintLabelStyled");
        hintLabel.assertStyle("background-color: beige;" +
                " border-left-color: pink;" +
                " border-left-width: 3px;" +
                " border-left-style: solid;" +
                " padding-top: 10px;" +
                " padding-bottom: 10px;" +
                " width: 160px;" +
                " color: brown;" +
                " font-weight: bold;" +
                " overflow: hidden;");

        hintLabel.showHintLabel();

        hintLabel.hint().assertStyle("background-color: PaleGreen;" +
                " border-left-color: aqua;" +
                " border-left-width: 3px;" +
                " border-left-style: dotted;" +
                " padding-top: 20px;" +
                " padding-bottom: 20px;" +
                " color: blue;");
    }

}

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
package org.openfaces.component.ajax;

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.openfaces.InputTextInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;

/**
 * @author Ilya Musihin
 */
public class AjaxTest extends OpenFacesTestCase {
     //
     @Test
    public void testActionPhase() {
        testAppFunctionalPage("/components/ajax/ajax.jsf");
        element("form1:resetBtn").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        ElementInspector counter = element("form1:counter");
        counter.assertText("0");

        // check operation with <h:commandButton> -- a special implementation case
        element("form1:btn1").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        counter.assertText("1"); // check direct embedding
        element("form1:btn2").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        counter.assertText("2"); // check attaching with "for" attribute
        element("form1:btn3").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        counter.assertText("3"); // standalone invokation

        // check operation with <h:commandButton> -- a special implementation case
        element("form1:link1").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        counter.assertText("4"); // check direct embedding
        element("form1:link2").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        counter.assertText("5"); // check attaching with "for" attribute
        element("form1:link3").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        counter.assertText("6"); // standalone invokation

        // check operation with <h:inputText> -- a non-default event, delay

        InputTextInspector inputText = inputText("form1:input1");
        inputText.typeKeys("1");
        sleep(100);
        inputText.typeKeys("2");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        counter.assertText("7"); // check direct embedding
        inputText = inputText("form1:input2");
        inputText.typeKeys("1");
        sleep(100);
        inputText.typeKeys("2");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        counter.assertText("8"); // check attaching with "for" attribute
        inputText = inputText("form1:input3");
        inputText.typeKeys("1");
        sleep(100);
        inputText.typeKeys("2");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        counter.assertText("9"); // standalone invokation

        // check operation with <h:graphicImage> -- a case for a typical JSF component without special implementation cases
        element("form1:image1").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        counter.assertText("10"); // check direct embedding
        element("form1:image2").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        counter.assertText("11"); // check attaching with "for" attribute
        element("form1:image3").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        counter.assertText("12"); // standalone invokation

        // check operation with HTML tags
        element("div2").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        counter.assertText("13"); // check attaching with "for" attribute
        element("div3").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        counter.assertText("14"); // standalone invokation
    }
}

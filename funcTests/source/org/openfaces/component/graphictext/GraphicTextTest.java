/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.graphictext;

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;

/**
 * @author Darya Shumilina
 */
public class GraphicTextTest extends OpenFacesTestCase {

    //todo: problems with image comparison are possible if the test server dpi doesn't equals to 96 when 'font-size' defined via 'pt'
     @Test
    public void testStylesAndDirections() {

        testAppFunctionalPage("/components/graphictext/graphicTextCheckStyles.jsf");

        /*check defaults:
        font-family: Verdana; font-size:12px; color:black; font-style:normal; font-weight:normal;
        direction="0"*/
        assertAppearanceNotChanged("StylesAndDirections", "formID:test");
    }

     @Test
    public void testStylesAndDirectionRightToLeft() {
        testAppFunctionalPage("/components/graphictext/graphicTextCheckStyles.jsf");
        //font-size: 45px; color:#a10d07; font-style:italic; font-weight:bold; direction="rightToLeft"
        assertAppearanceNotChanged("StylesAndDirectionRightToLeft", "formID:test4");
    }

     @Test
    public void testStylesAndDirectionTopToBottom() {
        testAppFunctionalPage("/components/graphictext/graphicTextCheckStyles.jsf");
        //font-size: 15pt; color:red;}; direction="topToBottom"
        assertAppearanceNotChanged("StylesAndDirectionTopToBottom", "formID:test1");
    }

     @Test
    public void testStylesAndDirectionsBottomToTop() {
        testAppFunctionalPage("/components/graphictext/graphicTextCheckStyles.jsf");
        //font-size: 20px; color:green; font-style:italic; font-family:Arial; direction="115"
        assertAppearanceNotChanged("StylesAndDirectionsBottomToTop", "formID:test5");
    }

     @Test
    public void testStylesAndDirection50() {
        testAppFunctionalPage("/components/graphictext/graphicTextCheckStyles.jsf");
        //font-size: 45px; color:#FFA07A; font-style:italic; direction="50"
        assertAppearanceNotChanged("StylesAndDirection50", "formID:test2");
    }

     @Test
    public void testStylesAndDirection15() {
        testAppFunctionalPage("/components/graphictext/graphicTextCheckStyles.jsf");
        //font-size: 15px; color:blue; font-weight:bold; font-family:Courier; direction="15"
        assertAppearanceNotChanged("StylesAndDirection15", "formID:test6");
    }

     @Test
    public void testDateConverterDefinedByPattern() {
        testAppFunctionalPage("/components/graphictext/graphicTextCheckStyles.jsf");
        //<f:convertDateTime pattern="dd/MM/yyyy"/>
        assertAppearanceNotChanged("DateConverterDefinedByPattern", "formID:test7");
    }

     @Test
    public void testBindedCustomDateConverter() {
        testAppFunctionalPage("/components/graphictext/graphicTextCheckStyles.jsf");
        assertAppearanceNotChanged("BindedCustomDateConverter", "formID:test8");
    }

     @Test
    public void testBindingAttribute() {
        testAppFunctionalPage("/components/graphictext/graphicTextCheckStyles.jsf");
        assertAppearanceNotChanged("BindingAttribute", "formID:test9");
    }

}
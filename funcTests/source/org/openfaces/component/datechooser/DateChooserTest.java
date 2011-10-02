/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.datechooser;

import org.junit.Test;
import org.openfaces.component.calendar.BaseCalendarTestCase;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.InputInspector;
import org.seleniuminspector.openfaces.CalendarInspector;
import org.seleniuminspector.openfaces.DateChooserInspector;
import org.openfaces.test.RichFacesAjaxLoadingMode;
import org.seleniuminspector.openfaces.TabSetInspector;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Darya Shumilina
 */
public class DateChooserTest extends BaseCalendarTestCase {

    private Map<String, String> shortFieldValues = new HashMap<String, String>();
    private Map<String, String> mediumFieldValues = new HashMap<String, String>();
    private Map<String, String> longFieldValues = new HashMap<String, String>();
    private Map<String, String> fullFieldValues = new HashMap<String, String>();
    private Map<String, String> firstPatternFieldValues = new HashMap<String, String>();
    private Map<String, String> secondPatternFieldValues = new HashMap<String, String>();
    private Map<String, String> thirdPatternFieldValues = new HashMap<String, String>();
    private Map<String, String> fourthPatternFieldValues = new HashMap<String, String>();
    private Map<String, String> calendarValues = new HashMap<String, String>();
    private Map<String, String> firstDaysOfWeek = new HashMap<String, String>();

    @Test
    public void testReRenderThroughA4J() {
        testAppFunctionalPage("/components/datechooser/dateChooser_a4j.jsf");

        ElementInspector dataChooser = element("formID:dateChooserID");
        String oldValue = dataChooser.text();
        element("formID:refresher").click();

        RichFacesAjaxLoadingMode.getInstance().waitForLoad();

        String newValue = dataChooser.text();
        assertFalse(newValue.equals(oldValue));
    }

    @Test
    public void testChangingSelectedDate() {
        testAppFunctionalPage("/components/datechooser/dateChooserChangeDateMonthYear.jsf");

        final String initialValue = "03.12.2007";
        final String initialDate = "03 December, 2007";
        final String selectedValue = "05.12.2007";
        final String changedCalendarDate = "05 December, 2007";
        final String typedDate = "07.12.2007";
        final String expectedTypedDate = "07 December, 2007";

        DateChooserInspector dateChooser = dateChooser("formID:changeSelectedDate");
        ElementInspector selectedDateOutput = element("formID:selectedDate");
        ElementInspector selectedDateGetter = element("selectedDate");
        ElementInspector selectedDateInfo = element("selectedDateInfo");
        ElementInspector submitter = element("formID:submit");

        checkBeforeChangingSelectedDate(dateChooser, initialValue, selectedDateOutput, selectedDateGetter, initialDate,
                selectedDateInfo);

        checkChangingSelectedDate(dateChooser.calendar(), initialValue, selectedValue, new int[]{1, 2}, selectedDateOutput, submitter);

        checkAfterChangingSelectedDate(dateChooser, selectedValue, selectedDateOutput, selectedDateGetter,
                changedCalendarDate, selectedDateInfo, typedDate, submitter, expectedTypedDate);
    }

    private void checkBeforeChangingSelectedDate(DateChooserInspector dateChooser, String initialValue, ElementInspector selectedDateOutput,
                                                 ElementInspector selectedDateGetter, String initialDate, ElementInspector selectedDateInfo) {
        dateChooser.field().assertValue(initialValue);
        dateChooser.field().assertValue(selectedDateOutput.text());
        dateChooser.button().mouseDown();

        //check selected date in Calendar part
        selectedDateGetter.click();
        selectedDateInfo.assertText(initialDate);
    }


    private void checkAfterChangingSelectedDate(DateChooserInspector dateChooser, String selectedValue, ElementInspector selectedDateOutput,
                                                ElementInspector selectedDateGetter, String changedCalendarDate, ElementInspector selectedDateInfo,
                                                String typedDate, ElementInspector submitter, String expectedTypedDate) {
        dateChooser.field().assertValue(selectedValue);
        dateChooser.field().assertValue(selectedDateOutput.text());

        //check selected date in Calendar part
        selectedDateGetter.click();
        selectedDateInfo.assertText(changedCalendarDate);

        //input date value into the DateChooser field part
        dateChooser.field().type(typedDate);

        submitter.clickAndWait();
        dateChooser.field().assertValue(selectedDateOutput.text());

        //check selected date in Calendar part
        selectedDateGetter.click();
        selectedDateInfo.assertText(expectedTypedDate);
    }

    @Test
    public void testMonthChange() {
        testAppFunctionalPage("/components/datechooser/dateChooserChangeDateMonthYear.jsf");

        final String selectedDate = "06.03.2007";

        DateChooserInspector dateChooser = dateChooser("formID:changeYearMonth");
        ElementInspector submitter = element("formID:submit");
        ElementInspector selectedMonthOutput = element("formID:selectedMonth");

        dateChooser.button().mouseDown();

        prepareCheckingMonthChange(dateChooser.calendar(), "December", "January", "December", 2, new int[]{1, 2});
        dateChooser.field().assertValue(selectedDate);

        checkMonthChange(dateChooser.calendar(), "March", selectedMonthOutput, submitter, selectedDate);
        dateChooser.field().assertValue(selectedMonthOutput.text());
    }

    public void testYearChange() {
        testAppFunctionalPage("/components/datechooser/dateChooserChangeDateMonthYear.jsf");

        final String selectedDate = "06.12.2005";

        DateChooserInspector dateChooser = dateChooser("formID:changeYearMonth");
        ElementInspector submitter = element("formID:submit");
        ElementInspector selectedYearOutput = element("formID:selectedMonth");

        dateChooser.button().mouseDown();

        prepareCheckingYearChange(dateChooser.calendar(), "2007", "2008", "2007", 2, new int[]{1, 2});
        dateChooser.field().assertValue(selectedDate);

        checkYearChange(dateChooser.calendar(), "2005", selectedYearOutput, submitter, selectedDate);
        dateChooser.field().assertValue(selectedYearOutput.text());
    }

    @Test
    public void testTodayNoneButtons() {
        testAppFunctionalPage("/components/datechooser/dateChooserTodayNoneButton.jsf");

        final String dateFormat = "dd MMMM, yyyy";

        DateChooserInspector todayDateChooser = dateChooser("formID:today");
        DateChooserInspector noneDateChooser = dateChooser("formID:none");
        ElementInspector todayDateOutput = element("formID:todayDateString");
        ElementInspector selectedDateButton = element("selectedDate");
        ElementInspector selectedDateOutput = element("selectedDateInfo");
        ElementInspector todayStyleButton = element("todayStyleButton");
        ElementInspector noneStyleButton = element("noneStyleButton");
        ElementInspector todayInfoOutput = element("todayInfo");
        ElementInspector noneInfoOutput = element("noneInfo");
        ElementInspector submitter = element("formID:submit");
        ElementInspector noneSelectedDateButton = element("noneSelectedDate");
        ElementInspector noneSelectedDateOutput = element("noneSelectedDateInfo");
        ElementInspector noneDateOutput = element("formID:noneDateString");
        ElementInspector todayStyleButton1 = element("todayStyleButton1");
        ElementInspector noneStyleButton1 = element("noneStyleButton1");
        ElementInspector todayInfoOutput1 = element("todayInfo1");
        ElementInspector noneInfoOutput1 = element("noneInfo1");

        String todayDate = checkAndGetCurrentDateSelected(dateFormat, todayDateOutput, selectedDateButton, selectedDateOutput);

        todayDateChooser.field().assertValue(selectedDateOutput.text());
        todayDateChooser.field().assertValue(todayDateOutput.text());
        checkButtonPairState(todayStyleButton, todayInfoOutput, noneStyleButton, noneInfoOutput);

        todayDateChooser.button().mouseDown();
        //click 'None' button and check this button state
        checkButtonStateAndSelectedDateOnEmptiness(todayDateChooser.calendar(), todayDateOutput, selectedDateButton,
                selectedDateOutput, todayStyleButton, noneStyleButton, todayInfoOutput,
                noneInfoOutput, submitter, noneSelectedDateButton, noneSelectedDateOutput,
                noneDateOutput);

//    assertEquals("", selenium.getValue(noneFieldId));
        noneDateChooser.field().assertValue("");
        checkButtonPairState(noneStyleButton1, noneInfoOutput1, todayStyleButton1, todayInfoOutput1);

        noneDateChooser.button().mouseDown();
        checkTodayNoneButtons(noneDateChooser.calendar(), submitter, noneSelectedDateButton, noneSelectedDateOutput,
                noneDateOutput, todayDate);

        noneSelectedDateButton.click();
        noneDateChooser.field().assertValue(noneSelectedDateOutput.text());
        noneDateChooser.field().assertValue(noneDateOutput.text());

        checkButtonPairState(todayStyleButton1, todayInfoOutput1, noneStyleButton1, noneInfoOutput1);
    }

    @Test
    public void testLocalesPatternsFormats() {
        testAppFunctionalPage("/components/datechooser/dateChooserFormatLocalePattern.jsf");

        shortFieldValues.put(Locale.ENGLISH.toString(), "12/3/07");
        shortFieldValues.put(Locale.FRENCH.toString(), "03/12/07");
        shortFieldValues.put(Locale.GERMAN.toString(), "03.12.07");
        shortFieldValues.put(Locale.JAPANESE.toString(), "07/12/03");
        shortFieldValues.put(Locale.ITALIAN.toString(), "03/12/07");

        mediumFieldValues.put(Locale.ENGLISH.toString(), "Dec 3, 2007");
        mediumFieldValues.put(Locale.FRENCH.toString(), "3 d\u00E9c. 2007");
        mediumFieldValues.put(Locale.GERMAN.toString(), "03.12.2007");
        mediumFieldValues.put(Locale.JAPANESE.toString(), "2007/12/03");
        mediumFieldValues.put(Locale.ITALIAN.toString(), "3-dic-2007");

        longFieldValues.put(Locale.ENGLISH.toString(), "December 3, 2007");
        longFieldValues.put(Locale.FRENCH.toString(), "3 d\u00E9cembre 2007");
        longFieldValues.put(Locale.GERMAN.toString(), "3. Dezember 2007");
        longFieldValues.put(Locale.JAPANESE.toString(), "2007/12/03");
        longFieldValues.put(Locale.ITALIAN.toString(), "3 dicembre 2007");

        fullFieldValues.put(Locale.ENGLISH.toString(), "Monday, December 3, 2007");
        fullFieldValues.put(Locale.FRENCH.toString(), "lundi 3 d\u00E9cembre 2007");
        fullFieldValues.put(Locale.GERMAN.toString(), "Montag, 3. Dezember 2007");
        fullFieldValues.put(Locale.JAPANESE.toString(), "2007\u5E7412\u67083\u65E5");
        fullFieldValues.put(Locale.ITALIAN.toString(), "luned\u00EC 3 dicembre 2007");

        firstPatternFieldValues.put(Locale.ENGLISH.toString(), "03, 12, 07");
        firstPatternFieldValues.put(Locale.FRENCH.toString(), "03, 12, 07");
        firstPatternFieldValues.put(Locale.GERMAN.toString(), "03, 12, 07");
        firstPatternFieldValues.put(Locale.JAPANESE.toString(), "03, 12, 07");
        firstPatternFieldValues.put(Locale.ITALIAN.toString(), "03, 12, 07");

        secondPatternFieldValues.put(Locale.ENGLISH.toString(), "12/03/2007");
        secondPatternFieldValues.put(Locale.FRENCH.toString(), "12/03/2007");
        secondPatternFieldValues.put(Locale.GERMAN.toString(), "12/03/2007");
        secondPatternFieldValues.put(Locale.JAPANESE.toString(), "12/03/2007");
        secondPatternFieldValues.put(Locale.ITALIAN.toString(), "12/03/2007");

        thirdPatternFieldValues.put(Locale.ENGLISH.toString(), "03.December.07");
        thirdPatternFieldValues.put(Locale.FRENCH.toString(), "03.d\u00E9cembre.07");
        thirdPatternFieldValues.put(Locale.GERMAN.toString(), "03.Dezember.07");
        thirdPatternFieldValues.put(Locale.JAPANESE.toString(), "03.12\u6708.07");
        thirdPatternFieldValues.put(Locale.ITALIAN.toString(), "03.dicembre.07");

        fourthPatternFieldValues.put(Locale.ENGLISH.toString(), "3-12-2007");
        fourthPatternFieldValues.put(Locale.FRENCH.toString(), "3-12-2007");
        fourthPatternFieldValues.put(Locale.GERMAN.toString(), "3-12-2007");
        fourthPatternFieldValues.put(Locale.JAPANESE.toString(), "3-12-2007");
        fourthPatternFieldValues.put(Locale.ITALIAN.toString(), "3-12-2007");

        calendarValues.put(Locale.ENGLISH.toString(), "03 December, 2007");
        calendarValues.put(Locale.FRENCH.toString(), "03 d\u00E9cembre, 2007");
        calendarValues.put(Locale.GERMAN.toString(), "03 Dezember, 2007");
        calendarValues.put(Locale.JAPANESE.toString(), "03 12\u6708, 2007");
        calendarValues.put(Locale.ITALIAN.toString(), "03 dicembre, 2007");

        firstDaysOfWeek.put(Locale.ENGLISH.toString(), "Sun");
        firstDaysOfWeek.put(Locale.FRENCH.toString(), "lun.");
        firstDaysOfWeek.put(Locale.GERMAN.toString(), "Mo");
        firstDaysOfWeek.put(Locale.JAPANESE.toString(), "\u65E5");
        firstDaysOfWeek.put(Locale.ITALIAN.toString(), "lun");

        TabSetInspector localeChanger = tabSet("formID:localeChanger");
        checkValues(Locale.ENGLISH.toString());
        localeChanger.tabs().get(1).clickAndWait();
        checkValues(Locale.FRENCH.toString());
        localeChanger.tabs().get(2).clickAndWait();
        checkValues(Locale.GERMAN.toString());
        localeChanger.tabs().get(3).clickAndWait();
        checkValues(Locale.JAPANESE.toString());
        /* this used to work locally but failing on the server (command timed out + some italian locale issue) --
        uncomment and see what might be wrong
        localeChanger.tabs().get(4).clickAndWait();
        checkValues(Locale.ITALIAN.toString());
        */
    }

    private void checkValues(String locale) {
        dateChooser("formID:shortDF").field().assertValue(shortFieldValues.get(locale));
        element("getSelDate1").click();
        element("out1").assertText(calendarValues.get(locale));
        element("out11").assertText(firstDaysOfWeek.get(locale));

        dateChooser("formID:mediumDF").field().assertValue(mediumFieldValues.get(locale));
        element("getSelDate2").click();
        element("out2").assertText(calendarValues.get(locale));
        element("out21").assertText(firstDaysOfWeek.get(locale));

        dateChooser("formID:longDF").field().assertValue(longFieldValues.get(locale));
        element("getSelDate3").click();
        element("out3").assertText(calendarValues.get(locale));
        element("out31").assertText(firstDaysOfWeek.get(locale));

        dateChooser("formID:fullDF").field().assertValue(fullFieldValues.get(locale));
        element("getSelDate4").click();
        element("out4").assertText(calendarValues.get(locale));
        element("out41").assertText(firstDaysOfWeek.get(locale));

        dateChooser("formID:first").field().assertValue(firstPatternFieldValues.get(locale));
        element("getSelDate5").click();
        element("out5").assertText(calendarValues.get(locale));
        element("out51").assertText(firstDaysOfWeek.get(locale));

        dateChooser("formID:second").field().assertValue(secondPatternFieldValues.get(locale));
        element("getSelDate6").click();
        element("out6").assertText(calendarValues.get(locale));
        element("out61").assertText(firstDaysOfWeek.get(locale));

        dateChooser("formID:third").field().assertValue(thirdPatternFieldValues.get(locale));
        element("getSelDate7").click();
        element("out7").assertText(calendarValues.get(locale));
        element("out71").assertText(firstDaysOfWeek.get(locale));

        dateChooser("formID:fourth").field().assertValue(fourthPatternFieldValues.get(locale));
        element("getSelDate8").click();
        element("out8").assertText(calendarValues.get(locale));
        element("out81").assertText(firstDaysOfWeek.get(locale));
    }

    @Test
    public void testDefaultView() {
        testAppFunctionalPage("/components/datechooser/datachooser_defaultView.jsf");
        assertAppearanceNotChanged("DateChooserDefaultView");
    }

    @Test
    public void testStyling() {
        checkEnabledStyles(false);
        checkEnabledStyles(true);
        checkDisabledStyles(false);
        checkDisabledStyles(true);
    }

    @Test
    public void testValueChangeListener() {
        testAppFunctionalPage("/components/datechooser/dateChooserValueChangeListener.jsf");

        ElementInspector asAttributeOutput = element("formID:asAttributeOutput");
        asAttributeOutput.assertText("false");
        ElementInspector asTagOutput = element("formID:asTagOutput");
        asTagOutput.assertText("false");

        DateChooserInspector attributeDateChooser = dateChooser("formID:asAttributeDC");
        attributeDateChooser.button().mouseDown();
        attributeDateChooser.calendar().selectCalendarCell(3, 3);

        DateChooserInspector asTagDateChooser = dateChooser("formID:asTagDC");
        asTagDateChooser.button().mouseDown();
        asTagDateChooser.calendar().selectCalendarCell(3, 3);

        element("formID:submit").clickAndWait();
        asAttributeOutput.assertText("true");
        asTagOutput.assertText("true");
    }

    private void checkEnabledStyles(boolean makeSubmit) {
        testAppFunctionalPage("/components/datechooser/dateChooserStyling.jsf");

        if (makeSubmit) {
            element("formID:submit").clickAndWait();
        }
        DateChooserInspector dateChooser = dateChooser("formID:styled");

        ElementInspector button = dateChooser.button();
        CalendarInspector calendarInspector = dateChooser.calendar();
        ElementInspector calendarBody = calendarInspector.body();
        InputInspector field = dateChooser.field();

        checkEnabledStyles(dateChooser, button, calendarInspector, calendarBody, field);
    }

    private void checkDisabledStyles(boolean makeSubmit) {
        testAppFunctionalPage("/components/datechooser/dateChooserStyling.jsf");

        if (makeSubmit) {
            element("formID:submit").clickAndWait();
        }
        DateChooserInspector dateChooser = dateChooser("formID:styled");

        ElementInspector button = dateChooser.button();
//    CalendarInspector calendarInspector = dateChooser.calendar();
        InputInspector field = dateChooser.field();

        element("formID:makeDisabled").clickAndWait();

        // disabledButtonImageUrl="../dropdown/dropdown_arrow_disabled.gif"
        button.childNodes().get(0).assertAttributeStartsWith("src", "../dropdown/dropdown_arrow_disabled.gif");

        // disabledButtonStyle="background: pink;"
        button.assertStyle("background: pink");

        // disabledFieldStyle="background: yellow; border: 2px solid green;"
        field.assertStyle("border: 2px solid green; background: yellow");

        // disabledStyle="width: 400px;"
        dateChooser.assertWidth(400);
    }


    private void checkEnabledStyles(DateChooserInspector dateChooser, ElementInspector button, CalendarInspector calendarInspector, ElementInspector calendarBody, InputInspector field) {
        /*check field and button style*/

        // style="width: 230px;"
        dateChooser.assertWidth(230);

        // buttonStyle="border: 1px solid blue; background: PowderBlue;"
        button.assertStyle("background: PowderBlue; border: 1px solid blue");

        // fieldStyle="background: GreenYellow; font-weight: bold;"
        field.assertStyle("background: GreenYellow; font-weight: bold");

        // rolloverFieldStyle="background: red; font-weight: normal;"
        dateChooser.mouseMove();
        field.assertStyle("background-color: red; font-weight: normal");

        // buttonImageUrl="../dropdown/dropdown_arrow.gif"
        button.childNodes().get(0).assertAttributeStartsWith("src", "../dropdown/dropdown_arrow.gif");

        // rolloverButtonStyle="border: 2px solid darkgreen; background: green;"
        dateChooser.button().mouseOver();
        dateChooser.button().mouseMove();
        button.assertStyle("border: 2px solid darkgreen; background: green");

        /* check calendar styles */
        button.mouseDown();
        // pressedButtonStyle="border: 3px solid gray; background: blue;"
        dateChooser.mouseMove();
        button.assertStyle("border: 3px solid gray; background: blue");

        button.mouseUp();

        // daysHeaderStyle="background: red;"
        calendarBody.subElement("tr[0]").assertStyle("background-color: red");

        // dayStyle="color: red;"
        calendarBody.subElement(DAY_PATH).assertStyle("color: red");

        // footerStyle="background: greenyellow;"
        calendarInspector.subElement(FOOTER_PATH).assertStyle("background: greenyellow");

        // headerStyle="background: #FF36F8;"
        calendarInspector.subElement("tbody[0]/tr[0]/td[0]/table[0]").assertStyle("background: #FF36F8");

        // inactiveMonthDayStyle="color: yellow;"
        calendarBody.subElement(INACTIVE_DAY_PATH).assertStyle("color: yellow");

        // rolloverDayStyle="font-weight: bold;"
        calendarBody.subElement(DAY_PATH).evalExpression("onmouseover()");
        calendarBody.subElement(DAY_PATH).assertStyle("font-weight: bold");
        calendarBody.subElement(DAY_PATH).evalExpression("onmouseout()");

        // rolloverInactiveMonthDayStyle="color: aquamarine;"
        calendarBody.subElement(INACTIVE_DAY_PATH).evalExpression("onmouseover()");
        calendarBody.subElement(INACTIVE_DAY_PATH).assertStyle("color: aquamarine");
        calendarBody.subElement(INACTIVE_DAY_PATH).evalExpression("onmouseout()");

        // selectedDayStyle="border: 1px solid springgreen;"
        calendarBody.subElement(SELECTED_DAY_PATH).assertStyle("border: 1px solid springgreen");

        // rolloverSelectedDayStyle="background: orange; font-weight: bold;"
        calendarBody.subElement(SELECTED_DAY_PATH).evalExpression("onmouseover()");
        calendarBody.subElement(SELECTED_DAY_PATH).assertStyle("background: orange; font-weight: bold");
        calendarBody.subElement(SELECTED_DAY_PATH).evalExpression("onmouseout()");

        // rolloverTodayStyle="border: 1px solid black;"
        //todo: add code here!

        // rolloverWeekendDayStyle="background: red;"
        calendarBody.subElement(WEEKEND_DAY_PATH).evalExpression("onmouseover()");
        calendarBody.subElement(WEEKEND_DAY_PATH).assertStyle("background: red");
        calendarBody.subElement(WEEKEND_DAY_PATH).evalExpression("onmouseout()");

        // todayStyle="color: Teal;"
        //todo: add code here!

        // weekendDayStyle="border-style:dotted; border-color:RoyalBlue; border-width:1px;"
        calendarBody.subElement(WEEKEND_DAY_PATH).assertStyle("border: 1px dotted RoyalBlue");

        // calendarStyle="background: azure;"
        calendarInspector.assertStyle("background: azure");
    }

}

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
package org.openfaces.component.calendar;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.openfaces.CalendarInspector;
import org.openfaces.test.RichFacesAjaxLoadingMode;

/**
 * @author Darya Shumilina
 */
public class CalendarTest extends BaseCalendarTestCase {
    @Test
    @Ignore // revive this test when RichFaces 4 is fully functional
    public void testReRenderThroughA4J() {
        testAppFunctionalPage("/components/calendar/calendar_a4j.jsf");

        for (int i = 0; i < 3; i++) {
            element("formID:refresher").click();
            RichFacesAjaxLoadingMode.getInstance().waitForLoad();
            element("formID:onchangePopup").assertVisible(true);
            element("formID:closer").click();
        }
    }

    @Test
    public void testClientSideEvents() {
        testAppFunctionalPage("/components/calendar/calendar.jsf");
        Selenium selenium = getSelenium();
        assertTrue(selenium.isTextPresent("onperiodchange")); // JSFC-2053

        //check onchange event
        calendar("formID:onchangeCalendar").selectCalendarCell(1, 2);
        assertTrue(selenium.isTextPresent("onchange fired"));
        //todo: uncomment following verification after 'JSFC-1465' and 'JSFC-1466' fix
//    assertTrue(selenium.isTextPresent("Event name: change"));
    }

    @Test
    public void testChangingSelectedDate() {
        testAppFunctionalPage("/components/calendar/calendarChangeDateMonthYear.jsf");

        final String initialValue = "03.12.2007";
        final String selectedValue = "05.12.2007";

        CalendarInspector calendar = calendar("formID:changeSelectedDate");

        checkChangingSelectedDate(calendar, initialValue, selectedValue, new int[]{1, 2}, element("formID:selectedDate"),
                element("formID:submit"));
    }

    @Test
    public void testMonthChange() {
        testAppFunctionalPage("/components/calendar/calendarChangeDateMonthYear.jsf");

        CalendarInspector calendar = calendar("formID:changeYearMonth");
        ElementInspector selectedMonthOutput = element("formID:selectedMonth");
        ElementInspector submitter = element("formID:submit");

        prepareAndCheckMonthChange(
                calendar, "December", "January", "December", "March", 2, selectedMonthOutput,
                new int[]{1, 2}, submitter, "06.03.2007");
    }

    @Test
    public void testYearChange() {
        testAppFunctionalPage("/components/calendar/calendarChangeDateMonthYear.jsf");

        CalendarInspector calendar = calendar("formID:changeYearMonth");
        ElementInspector selectedYearOutput = element("formID:selectedMonth");
        ElementInspector submitter = element("formID:submit");

        prepareAndCheckYearChange(
                calendar, "2007", "2008", "2007", "2005", 2, selectedYearOutput, new int[]{1, 2},
                submitter, "06.12.2005");
    }

    @Test
    public void testTodayNoneButtons() {
        testAppFunctionalPage("/components/calendar/calendarTodayNoneButtons.jsf");

        final String dateFormat = "d.MM.yyyy";

        CalendarInspector todayCalendar = calendar("formID:today");
        CalendarInspector noneCalendar = calendar("formID:none");
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

        //check is current date selected (on server and client)
        String todayDate = checkAndGetCurrentDateSelected(dateFormat, todayDateOutput, selectedDateButton, selectedDateOutput);

        checkButtonPairState(todayStyleButton, todayInfoOutput, noneStyleButton, noneInfoOutput);

        //click 'None' button and check this button state
        checkButtonStateAndSelectedDateOnEmptiness(todayCalendar, todayDateOutput, selectedDateButton, selectedDateOutput, todayStyleButton,
                noneStyleButton, todayInfoOutput, noneInfoOutput, submitter, noneSelectedDateButton,
                noneSelectedDateOutput, noneDateOutput);

        checkButtonPairState(noneStyleButton1, noneInfoOutput1, todayStyleButton1, todayInfoOutput1);

        checkTodayNoneButtons(noneCalendar, submitter, noneSelectedDateButton, noneSelectedDateOutput, noneDateOutput, todayDate);

        //check 'Today' and 'None' button state if current date is selected
        checkButtonPairState(todayStyleButton1, todayInfoOutput1, noneStyleButton1, noneInfoOutput1);
    }

    @Test
    public void testDateRangeChanging() {
        testAppFunctionalPage("/components/calendar/calendarChangeDateRanges.jsf");

        //check is date range reflected correctly by page load
        ElementInspector dateRangesPrinter = element("dateRangesPrinter");
        dateRangesPrinter.click();
        ElementInspector emptyDiv = element("empty");
        emptyDiv.assertText("3 4 5 6 7 8 9 10 11 ");

        dateChooser("formID:fromDateChooser").field().type("Nov 6, 2007");
        dateChooser("formID:toDateChooser").field().type("Nov 20, 2007");

        element("formID:submit").clickAndWait();

        dateRangesPrinter.click();
        emptyDiv.assertText("6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 ");
    }

    @Test
    public void testStyles() {
        checkStyles(false);
        checkStyles(true);
    }

    @Test
    public void testClientSideAPI() {
        testAppFunctionalPage("/components/calendar/calendarClientSideAPI.jsf");

        //check getSelectedDate() function
        element("dateGetter").click();
        element("dateGetterOutput").assertText("12=12");

        //check setSelectedDate() function
        element("dateSetter").click();
        element("dateSetterOutput").assertText("works well :-)");
    }

    private void checkStyles(boolean makeSubmit) {
        testAppFunctionalPage("/components/calendar/calendarStyles.jsf");

        if (makeSubmit) {
            element("formID:submit").clickAndWait();
        }

        CalendarInspector calendar = calendar("formID:styled");
        ElementInspector calendarBody = calendar.body();

        // check bodyStyle attribute
        calendarBody.assertStyle("background-color: PowderBlue");

        // check dayStyle attribute
        calendarBody.subElement(DAY_PATH).assertStyle("color: red");

        // check daysHeaderStyle attribute
        calendarBody.subElement(DAY_HEADER_PATH).assertStyle("background: #A8FFFE");

        // check footerStyle attribute
        calendar.subElement(FOOTER_PATH).assertStyle("background: greenyellow");

        // check headerStyle attribute
        calendar.subElement(HEADER_PATH).assertStyle("background: #FF36F8");

        // check inactiveMonthDayStyle attribute
        calendarBody.subElement(INACTIVE_DAY_PATH).assertStyle("color: yellow");

        // check rolloverDayStyle attribute
        calendarBody.subElement(DAY_PATH).evalExpression("onmouseover()");
        calendarBody.subElement(DAY_PATH).assertStyle("font-weight: bold");
        calendarBody.subElement(DAY_PATH).evalExpression("onmouseout()");

        // check rolloverInactiveMonthDayStyle attribute
        calendarBody.subElement(INACTIVE_DAY_PATH).evalExpression("onmouseover()");
        calendarBody.subElement(INACTIVE_DAY_PATH).assertStyle("color: aquamarine");
        calendarBody.subElement(INACTIVE_DAY_PATH).evalExpression("onmouseout()");

        // check selectedDayStyle attribute
        calendarBody.subElement(SELECTED_DAY_PATH).assertStyle("border: 1px solid springgreen");

        // check rolloverSelectedDayStyle attribute
        calendarBody.subElement(SELECTED_DAY_PATH).evalExpression("onmouseover()");
        calendarBody.subElement(SELECTED_DAY_PATH).assertStyle("background: orange; font-weight: bold");
        calendarBody.subElement(SELECTED_DAY_PATH).evalExpression("onmouseout()");

        // check rolloverWeekendDayStyle attribute
        calendarBody.subElement(WEEKEND_DAY_PATH).evalExpression("onmouseover()");
        calendarBody.subElement(WEEKEND_DAY_PATH).assertStyle("background: red");
        calendarBody.subElement(WEEKEND_DAY_PATH).evalExpression("onmouseout()");

        // check weekendDayStyle attribute
        calendarBody.subElement(WEEKEND_DAY_PATH).assertStyle("border: 1px dotted RoyalBlue");

        //check rolloverTodayStyle
        //todo: add code here!

        //todayStyle="color: Teal;"
        //todo: add code here!
    }

    @Test
    public void testDefaultView() {
        testAppFunctionalPage("/components/calendar/calendar_defaultView.jsf");
        assertAppearanceNotChanged("CalendarDefaultView");
    }

    @Test
    public void testValueChangeListener() {
        testAppFunctionalPage("/components/calendar/calendarValueChangeListener.jsf");

        ElementInspector asAttributeOutput = element("formID:asAttributeOutput");
        asAttributeOutput.assertText("0");
        ElementInspector asTagOutput = element("formID:asTagOutput");
        asTagOutput.assertText("false");

        calendar("formID:asTagDD").selectCalendarCell(3, 3);
        calendar("formID:asAttributeDD").selectCalendarCell(3, 3);

        element("formID:submit").clickAndWait();

        asAttributeOutput.assertText("1");
        asTagOutput.assertText("true");
    }

    @Test
    public void testKeepTime() {
        testAppFunctionalPage("/components/calendar/calendarChangeDateMonthYear.jsf");

        final String initialValue = "12:34:56";
        final String selectedValue = "12:34:56";

        CalendarInspector keepTimeCalendar = calendar("formID:keepTimeCalendar");
        ElementInspector keepTimeOutput = element("formID:timeForKeepTimeCalendar");
        ElementInspector submitter = element("formID:submit");

        checkChangingSelectedDate(keepTimeCalendar, initialValue, selectedValue, new int[]{1, 2}, keepTimeOutput, submitter);

        testAppFunctionalPage("/components/calendar/calendarChangeDateMonthYear.jsf");

        final String zeroValue = "00:00:00";

        CalendarInspector noKeepTimeCalendar = calendar("formID:dontKeepTimeCalendar");
        ElementInspector noKeepTimeOutput = element("formID:timeForDontKeepTimeCalendar");

        checkChangingSelectedDate(noKeepTimeCalendar, initialValue, zeroValue, new int[]{1, 2},
                noKeepTimeOutput, submitter
        );

    }

}
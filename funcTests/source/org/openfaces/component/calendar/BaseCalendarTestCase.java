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
package org.openfaces.component.calendar;

import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.CalendarInspector;
import org.seleniuminspector.ElementInspector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Darya Shumilina
 */
public abstract class BaseCalendarTestCase extends OpenFacesTestCase {

    protected static final String TODAY_NONE_DISABLED = "color: rgb(128, 128, 128); cursor: default";
    protected static final String TODAY_NONE_ENABLED = "color: rgb(0, 0, 0); cursor: pointer";

    protected static final String TODAY_NONE_DISABLED_IE_OPERA = "color: #808080; cursor: default";
    protected static final String TODAY_NONE_ENABLED_IE_OPERA = "color: #000000; cursor: pointer";

    protected static final String DAY_HEADER_PATH = "tr[0]";
    protected static final String DAY_PATH = "tr[2]/td[2]/div[0]";

    protected static final String SELECTED_DAY_PATH = "tr[3]/td[0]/div[0]";
    protected static final String FOOTER_PATH = "tbody[2]/tr[0]/td[0]/table[0]";
    protected static final String HEADER_PATH = "tbody[0]/tr[0]/td[0]/table[0]";
    protected static final String INACTIVE_DAY_PATH = "tr[1]/td[0]/div[0]";

    protected static final String WEEKEND_DAY_PATH = "tr[2]/td[6]/div[0]";

    protected void checkChangingSelectedDate(CalendarInspector testableComponent, String initialValue, String selectedValue,
                                             int[] calendarCellCoordinates, ElementInspector selectedDateOutput, ElementInspector submitter) {
        selectedDateOutput.assertText(initialValue);
        testableComponent.selectCalendarCell(calendarCellCoordinates[0], calendarCellCoordinates[1]);

        submitter.clickAndWait();
        selectedDateOutput.assertText(selectedValue);
    }

    protected void prepareAndCheckMonthChange(CalendarInspector calendar,
                                              String initialMonthName, String nextMonthName,
                                              String previousMonthName, String chosenFromPopupMonthName, int selectedPopupMonthIndex,
                                              ElementInspector selectedMonthOutput, int[] calendarCellCoordinates, ElementInspector submitter,
                                              String selectedDate) {


        prepareCheckingMonthChange(calendar, initialMonthName, nextMonthName, previousMonthName, selectedPopupMonthIndex, calendarCellCoordinates);

        checkMonthChange(calendar, chosenFromPopupMonthName, selectedMonthOutput, submitter, selectedDate);
    }

    protected void checkMonthChange(CalendarInspector testableCalendar, String chosenFromPopupMonthName, ElementInspector selectedMonthOutput, ElementInspector submitter, String selectedDate) {
        submitter.clickAndWait();
        testableCalendar.month().assertText(chosenFromPopupMonthName);
        selectedMonthOutput.assertText(selectedDate);
    }

    protected void prepareCheckingMonthChange(CalendarInspector calendar, String initialMonthName, String nextMonthName, String previousMonthName, int selectedPopupMonthIndex, int[] calendarCellCoordinates) {
        calendar.month().assertText(initialMonthName);

        //next month
        calendar.monthIncrease().click();
        calendar.month().assertText(nextMonthName);

        //previous month
        calendar.monthDecrease().click();
        calendar.month().assertText(previousMonthName);

        //choose from month popup list
        calendar.selectMonthFromPopupList(selectedPopupMonthIndex);
        calendar.selectCalendarCell(calendarCellCoordinates[0], calendarCellCoordinates[1]);
    }

    protected void prepareAndCheckYearChange(CalendarInspector calendar,
                                             String initialYearName, String nextYearName,
                                             String previousYearName, String chosenFromPopupYearName, int selectInPopupYearIndex,
                                             ElementInspector selectedYearOutput, int[] calendarCellCoordinates, ElementInspector submitter,
                                             String selectedDate) {

        prepareCheckingYearChange(calendar, initialYearName, nextYearName, previousYearName, selectInPopupYearIndex, calendarCellCoordinates);

        checkYearChange(calendar, chosenFromPopupYearName, selectedYearOutput, submitter, selectedDate);
    }

    protected void checkYearChange(CalendarInspector calendar, String chosenFromPopupYearName, ElementInspector selectedYearOutput, ElementInspector submitter, String selectedDate) {
        submitter.clickAndWait();
        calendar.year().assertText(chosenFromPopupYearName);
        selectedYearOutput.assertText(selectedDate);
    }

    protected void prepareCheckingYearChange(CalendarInspector calendar, String initialYearName, String nextYearName, String previousYearName, int selectInPopupYearIndex, int[] calendarCellCoordinates) {
        calendar.year().assertText(initialYearName);

        //next year
        calendar.yearIncrease().click();
        calendar.year().assertText(nextYearName);

        //previous year
        calendar.yearDecrease().click();
        calendar.year().assertText(previousYearName);

        //choose from year popup list
        calendar.selectYearFromPopupList(selectInPopupYearIndex);
        calendar.selectCalendarCell(calendarCellCoordinates[0], calendarCellCoordinates[1]);
    }

    protected void checkTodayNoneButtons(String dateFormat, CalendarInspector todayCalendar, CalendarInspector noneCalendar,
                                         ElementInspector todayDateOutput, ElementInspector selectedDateButton,
                                         ElementInspector selectedDateOutput, ElementInspector todayStyleButton,
                                         ElementInspector noneStyleButton, ElementInspector todayInfoOutput,
                                         ElementInspector noneInfoOutput, ElementInspector submitter,
                                         ElementInspector noneSelectedDateButton, ElementInspector noneSelectedDateOutput,
                                         ElementInspector noneDateOutput, ElementInspector todayStyleButton1,
                                         ElementInspector noneStyleButton1, ElementInspector todayInfoOutput1,
                                         ElementInspector noneInfoOutput1) {

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

    protected void checkButtonStateAndSelectedDateOnEmptiness(CalendarInspector todayCalendar, ElementInspector todayDateOutput, ElementInspector selectedDateButton, ElementInspector selectedDateOutput, ElementInspector todayStyleButton, ElementInspector noneStyleButton, ElementInspector todayInfoOutput, ElementInspector noneInfoOutput, ElementInspector submitter, ElementInspector noneSelectedDateButton, ElementInspector noneSelectedDateOutput, ElementInspector noneDateOutput) {
        todayCalendar.none().mouseUp();
        checkButtonPairState(noneStyleButton, noneInfoOutput, todayStyleButton, todayInfoOutput);
        checkSelectedDateOnEmptiness(todayDateOutput, selectedDateButton, selectedDateOutput, submitter,
                noneSelectedDateButton, noneSelectedDateOutput, noneDateOutput);
    }

    protected void checkTodayNoneButtons(CalendarInspector noneCalendar, ElementInspector submitter,
                                         ElementInspector noneSelectedDateButton, ElementInspector noneSelectedDateOutput,
                                         ElementInspector noneDateOutput, String todayDate) {
        //choose current date using 'Today' button
        noneCalendar.today().mouseUp();

        //check is current date is selected
        noneSelectedDateButton.click();
        noneSelectedDateOutput.assertText(todayDate);

        submitter.clickAndWait();
        noneDateOutput.assertText(todayDate);
    }


    protected void checkSelectedDateOnEmptiness(ElementInspector todayDateOutput, ElementInspector selectedDateButton,
                                                ElementInspector selectedDateOutput, ElementInspector submitter,
                                                ElementInspector noneSelectedDateButton, ElementInspector noneSelectedDateOutput,
                                                ElementInspector noneDateOutput) {
        //check is selected date value is empty (on server and client)
        selectedDateButton.click();
        selectedDateOutput.assertText("null");

        submitter.clickAndWait();
        todayDateOutput.assertText("null");
        //------------------------------S-------------------------------------------------------
        //check is selected date value is empty
        noneSelectedDateButton.click();
        noneSelectedDateOutput.assertText("null");
        noneDateOutput.assertText("null");
    }

    protected void checkButtonStateAsEnabled(ElementInspector styleButton, ElementInspector infoOutput) {
        styleButton.click();
        assertTrue(infoOutput.text().equals(TODAY_NONE_ENABLED) || infoOutput.text().equals(TODAY_NONE_ENABLED_IE_OPERA));
    }

    protected void checkButtonStateAsDisabled(ElementInspector styleButton, ElementInspector infoOutput) {
        styleButton.click();
        assertTrue(infoOutput.text().equals(TODAY_NONE_DISABLED) || infoOutput.text().equals(TODAY_NONE_DISABLED_IE_OPERA));
    }

    protected void checkButtonPairState(ElementInspector styleButtonDisabled, ElementInspector infoOutputDisabled,
                                        ElementInspector styleButtonEnabled, ElementInspector infoOutputEnabled) {
        checkButtonStateAsDisabled(styleButtonDisabled, infoOutputDisabled);
        checkButtonStateAsEnabled(styleButtonEnabled, infoOutputEnabled);
    }

    protected String checkAndGetCurrentDateSelected(String dateFormat, ElementInspector todayDateOutput,
                                                    ElementInspector selectedDateButton, ElementInspector selectedDateOutput) {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat dayMonthYearFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        String todayDate = dayMonthYearFormat.format(today);
        todayDateOutput.assertText(todayDate);

        selectedDateButton.click();
        selectedDateOutput.assertText(todayDate);
        return todayDate;
    }
}
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

package org.openfaces.demo.beans.timetable;

import org.openfaces.component.timetable.AbstractTimetableEvent;
import org.openfaces.util.Faces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author: Roman Perin
 * Date: 16.09.14
 */
public class WeekTableBean extends TimeTableBean implements Serializable {

    public WeekTableBean(){
        try {
            InputStream resource = WeekTableBean.class.getResourceAsStream("Lessons.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource, "utf-8"));
            String currentString;
            int i = 0;
            while (true) {
                currentString = reader.readLine();
                if (currentString == null) break;
                String[] bankAttributes = currentString.split("\t");

                int lessonNumber = Integer.parseInt(bankAttributes[0]);
                String courseTitle = bankAttributes[1];
                int roomNumber = Integer.parseInt(bankAttributes[2]);
                String dayOfWeek = bankAttributes[3];
                String timeStart = bankAttributes[4];
                String timeEnd = bankAttributes[5];
                String instructor = bankAttributes[6];

                DayOfWeek day = DayOfWeek.getFromString(dayOfWeek);
                int startHours = getHours(timeStart);
                int startMinutes = getMinutes(timeStart);
                int endHours = getHours(timeEnd);
                int endMinutes = getMinutes(timeEnd);

                Lesson lesson = new Lesson(lessonNumber, courseTitle, roomNumber, dayOfWeek,
                                           getDateBy(day.getDayNumber(), startHours, startMinutes),
                                           getDateBy(day.getDayNumber(), endHours, endMinutes),
                                           instructor);
                addNewEvent(lesson.getStartTime(), lesson.getEndTime(), lesson.getFullName(),
                            lesson.getDescription());
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private int getHours(String timeBlank) {
        String[] time = timeBlank.split(":");
        return Integer.parseInt(time[0]);
    }

    private int getMinutes(String timeBlank) {
        String[] time = timeBlank.split(":");
        return Integer.parseInt(time[1]);
    }

    //This method returns date in the scope of current week.
    private static Date getDateBy(int dayOfWeek, int hour, int minute) {
        Calendar c1 = new GregorianCalendar();
        c1.set(Calendar.HOUR_OF_DAY, hour);
        c1.set(Calendar.MINUTE, minute);
        c1.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return c1.getTime();
    }

    public List<AbstractTimetableEvent> getEvents() {
        Date startTime = Faces.var("startTime", Date.class);
        Date endTime = Faces.var("endTime", Date.class);
        List<AbstractTimetableEvent> events = retrieveEventsForPeriod(startTime, endTime);
        return events;
    }

    private enum DayOfWeek{
        SUNDAY(1, "su"),
        MONDAY(2, "mo"),
        TUESDAY(3, "tu"),
        WEDNESDAY(4, "we"),
        THURSDAY(5, "th"),
        FRIDAY(6, "fr"),
        SATURDAY(7, "sa");

        private int dayNumber;
        private String abbreviation;

        DayOfWeek(int dayNumber, String abbreviation){
            this.dayNumber = dayNumber;
            this.abbreviation = abbreviation;
        }

        public int getDayNumber(){
            return dayNumber;
        }

        public static DayOfWeek getFromString(String dayName){
            if (dayName != null && !dayName.isEmpty()){
                for (DayOfWeek day: DayOfWeek.values()){
                    if (day.abbreviation.equalsIgnoreCase(dayName.substring(0, 2))){
                        return day;
                    }
                }
            }
            throw new IllegalArgumentException("Such day of week \'" + dayName + "\' does not exist.");
        }
    }
}

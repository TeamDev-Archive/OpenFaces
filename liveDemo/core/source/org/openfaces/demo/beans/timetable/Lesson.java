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

import java.util.Date;

/**
 * @author: Roman Perin
 * Date: 15.09.14
 */
public class Lesson {
    private int id;
    private Date startTime;
    private Date endTime;
    private int lessonNumber;
    private String courseTitle;
    private int roomNumber;
    private String dayOfWeek;
    private String instructor;
    private String fullName;
    private String description;

    public Lesson(int lessonNumber, String courseTitle, int roomNumber,
                  String dayOfWeek, Date timeStart, Date timeEnd, String instructor) {
        this.lessonNumber = lessonNumber;
        this.courseTitle = courseTitle;
        this.roomNumber = roomNumber;
        this.dayOfWeek = dayOfWeek;
        this.startTime = timeStart;
        this.endTime = timeEnd;
        this.instructor = instructor;
        this.fullName = lessonNumber + ": " + courseTitle;
        this.description = "Instructor: " + instructor + ";\n"
                           + "room: " + roomNumber + ".";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassNumber() {
        return lessonNumber;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Class{" +
                "lessonNumber=" + lessonNumber +
                ", courseTitle='" + courseTitle + '\'' +
                ", roomNumber=" + roomNumber +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", Instructor='" + instructor + '\'' +
                '}';
    }
}

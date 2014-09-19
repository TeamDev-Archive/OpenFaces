/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
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
import org.openfaces.component.timetable.ReservedTimeEvent;
import org.openfaces.component.timetable.TimetableEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Kashcheiev
 */
public class MonthTableBean  extends TimeTableBean {

    public MonthTableBean(){
        Color red1 = new Color(220, 0, 0);
        Color red2 = new Color(230, 100, 100);
        Color green = new Color(0, 180, 0);
        Color blue = new Color(51, 102, 255);
        Color orange = new Color(247, 103, 24);

//        today
        addNewEvent(thisMonth(2), thisMonth(14), "Vacation",
                "Instructor: Ivan Doe <br/>Fee: $40", red1);
        addNewEvent(thisMonth(14), thisMonth(15), "Ill",
                "Instructor: Gregory House <br/>Fee: $30", blue);
        addNewEvent(thisMonth(17), thisMonth(17), "Day Off",
                "Instructor: Melany Scott <br/>Fee: $25", red1);
        addNewEvent(thisMonth(17), thisMonth(17), "Day Off",
                "Instructor: Tony Bricks <br/>Fee: Free", orange);
        addNewEvent(thisMonth(17), thisMonth(17), "Day Off",
                "Instructor: Tony Bricks <br/>Fee: Free", blue);
        addNewEvent(thisMonth(28), thisMonth(28), "Payday",
                "Instructor: Alex West <br/>Fee: $30", blue);

    }

}

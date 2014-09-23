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

import org.openfaces.util.Faces;
import org.openfaces.component.timetable.AbstractTimetableEvent;
import org.openfaces.component.timetable.TimetableChangeEvent;
import org.openfaces.component.timetable.TimetableEvent;
import org.openfaces.component.timetable.TimetableResource;
import org.openfaces.component.timetable.ReservedTimeEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.Color;

/**
 * @author Dmitry Pikhulya
 */
public class DayTableBean2 extends TimeTableBean implements Serializable {

    private List<TimetableResource> resources = new ArrayList<TimetableResource>();
    private Color defaultColor = new Color(0, 0x6e, 0xbb);

    public DayTableBean2() {
        Color green = new Color(41, 142, 1);
        Color blue = new Color(2, 105, 220);
        Color orange = new Color(232, 65, 2);

        Resource andrew = new Resource("Andrew", "#0269dc");
        Resource lucie = new Resource("Lucie", "#df4c11");
        Resource alex = new Resource("Alex", "#298e01");

//        today
        addNewEvent(todayAt(9, 0), todayAt(19, 30), "WORK", "Usual day, nothing special", blue, andrew.getId());
        addNewEvent(todayAt(20, 0), todayAt(23, 0), "ALEX BIRTHDAY PARTY", "Dress-code: Harry Potter", blue, andrew.getId());

        addNewEvent(todayAt(7, 0), todayAt(9, 0), "SUNRISE AT THE ROOF", "Don't forget the camera", orange, lucie.getId());
        addNewEvent(todayAt(9, 30), todayAt(13, 0), "PAINTING", "...", orange, lucie.getId());
        addNewEvent(todayAt(14, 0), todayAt(19, 30), "LOOKING FOR A PRESENT", "Alex loves sweet honey", orange, lucie.getId());
        addNewEvent(todayAt(20, 0), todayAt(23, 0), "ALEX BIRTHDAY PARTY", "Dress-code: Harry Potter", orange, lucie.getId());

        addNewEvent(todayAt(6, 0), todayAt(20, 0), "PREPARING FOR BIRTHDAY", "Oh my God!", green, alex.getId());
        addNewEvent(todayAt(20, 0), todayAt(23, 0), "MY BIRTHDAY", "There will be a lot of magic wands", green, alex.getId());

//        yesterday
        addNewEvent(yesterdayAt(9, 0), yesterdayAt(15, 30), "WORK", "Short day, will visit a doctor", blue, andrew.getId());
        addNewEvent(yesterdayAt(16, 30), yesterdayAt(17, 30), "DOCTOR CONSULTATION", "Once-a-year visit", blue, andrew.getId());
        addEvent(new ReservedTimeEvent(generateEventId(), andrew.getId(), yesterdayAt(18, 30), yesterdayAt(19, 30)));

        addNewEvent(yesterdayAt(10, 0), yesterdayAt(12, 30), "IREN FOTOSET", "Cute Iren with mother.<br/>Iren's mother phone: +1 (555) 987 98 12", orange, lucie.getId());
        addNewEvent(yesterdayAt(16, 30), yesterdayAt(19, 0), "NATIONAL GEOGRAPHIC", "Show photos to NG and close the deal.", orange, lucie.getId());

        addNewEvent(yesterdayAt(9, 0), yesterdayAt(11, 30), "BUY FOOD", "Beer, bread and circuses", green, alex.getId());
        addNewEvent(yesterdayAt(13, 30), yesterdayAt(21, 0), "VISIT PARENTS", "Mom asked for my old cell phone.", green, alex.getId());

//        tomorrow
        addEvent(new ReservedTimeEvent(generateEventId(), andrew.getId(), tomorrowAt(6, 0), tomorrowAt(8, 30)));
        addNewEvent(tomorrowAt(9, 0), tomorrowAt(19, 30), "WORK", "As usual", blue, andrew.getId());
        addNewEvent(tomorrowAt(20, 30), tomorrowAt(23, 30), "WENDY", "Table is reserved at Potato House", blue, andrew.getId());

        addEvent(new ReservedTimeEvent(generateEventId(), lucie.getId(), tomorrowAt(6, 0), tomorrowAt(12, 30)));
        addNewEvent(tomorrowAt(14, 0), tomorrowAt(15, 0), "BRUCE ECKEL PRESS CONFERENCE", "Take 50D with EF 28-300", orange, lucie.getId());
        addNewEvent(tomorrowAt(17, 30), tomorrowAt(20, 0), "SIGN CONTEST", "", orange, lucie.getId());

        addNewEvent(tomorrowAt(8, 30), tomorrowAt(11, 30), "CLEAN HOUSE FROM GUESTS", "Andrew will go the first", green, alex.getId());
        addNewEvent(tomorrowAt(13, 0), tomorrowAt(16, 0), "CHECK NEW KITE VIDEO", "Lucie promised to give me the \"Lines\".", green, alex.getId());
        addNewEvent(tomorrowAt(19, 30), tomorrowAt(21, 0), "COUNTER-STRIKE", "The Spawn team wants to repeat.", green, alex.getId());

        resources.add(new TimetableResource(andrew, andrew.getId(), andrew.getName()));
        resources.add(new TimetableResource(lucie, lucie.getId(), lucie.getName()));
        resources.add(new TimetableResource(alex, alex.getId(), alex.getName()));
    }

    @Override
    public List<AbstractTimetableEvent> getEvents() {
        Date startTime = Faces.var("startTime", Date.class);
        Date endTime = Faces.var("endTime", Date.class);

        List<AbstractTimetableEvent> result = retrieveEventsForPeriod(startTime, endTime);
        return result;
    }


    public List<TimetableResource> getResources() {
        return resources;
    }

    @Override
    public void addEvent(AbstractTimetableEvent event) {
        if (event instanceof TimetableEvent){
            TimetableEvent timetableEvent = (TimetableEvent) event;
            if(timetableEvent.getColor() == null) {
                timetableEvent.setColor(defaultColor);
            }
        }
        super.addEvent(event);
    }

    private TimetableEvent getEvent() {
        return Faces.var("event", TimetableEvent.class);
    }

}

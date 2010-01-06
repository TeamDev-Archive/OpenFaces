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

package org.openfaces.testapp.support.QKS524;

import org.openfaces.testapp.screenshot.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class QKS524 {
    private List<Person> person = new ArrayList<Person>();
    private List<Person> checkedUsers = new ArrayList<Person>();
    Person person1 = new Person(
            "John Smith",
            "Programmer",
            "Bowling, cinema"
    );
    Person person2 = new Person(
            "James Erratic",
            "Technical writer",
            "Walking and thinking"
    );


    public QKS524() {
        person.add(person1);
        checkedUsers.add(new Person(
                "Christina Strange",
                "Programmer",
                "Sitting on armchair"
        ));

        person.add(person2);
        person.add(new Person(
                "Christina Strange",
                "Programmer",
                "Sitting on armchair"
        ));
        person.add(new Person(
                "William Green",
                "Manager",
                "Sky-jumping"
        ));
        person.add(new Person(
                "Albert Ordinary",
                "Technical writer",
                "Watching TV"
        ));
        person.add(new Person(
                "Chris Lee",
                "Architect",
                "Ancient history"
        ));
        person.add(new Person(
                "Jane White",
                "Programmer",
                "Spent time in clubs"
        ));
        person.add(new Person(
                "Jimmy Cheerful",
                "Technical writer",
                "Snowboard, sky-jumping"
        ));
        person.add(new Person(
                "Andrew Ambitious",
                "Sales manager",
                "Gardening"
        ));
        person.add(new Person(
                "Bob Forgetive",
                "Designer",
                "Playing the piano"
        ));
        person.add(new Person(
                "Michael Equable",
                "Programmer",
                "Bowling, cinema"
        ));
        person.add(new Person(
                "Ike Adolescent",
                "Programmer",
                "Playing the piano"
        ));
        person.add(new Person(
                "Mary Honey",
                "Designer",
                "Needlework"
        ));
        person.add(new Person(
                "Den Glamourous",
                "Designer",
                "Painting"
        ));
        person.add(new Person(
                "Larry Smart",
                "Programmer",
                "Mind games"
        ));
        person.add(new Person(
                "Gary Efficient",
                "Programmer",
                "Bowling, cinema"
        ));
        person.add(new Person(
                "George Mediocrity",
                "Designer",
                "Cooking"
        ));
        person.add(new Person(
                "Walter Charitable",
                "Programmer",
                "History, archaeology"
        ));
        person.add(new Person(
                "Clayton Major",
                "Manager",
                "Just sleeping"
        ));
        person.add(new Person(
                "Christian Smile",
                "Manager",
                "Just sleeping"
        ));
        person.add(new Person(
                "Diana Ironist",
                "Designer",
                "Music"
        ));
        person.add(new Person(
                "Joe Tricky",
                "Programmer",
                "Skating, snowboarding"
        ));
        person.add(new Person(
                "Dean Genius",
                "Manager",
                "Cooking"
        ));

    }


    public List<Person> getPerson() {
        return person;
    }

    public void setPerson(List<Person> person) {
        this.person = person;
    }

    public List<Person> getCheckedUsers() {
        checkedUsers.add(person2);
        checkedUsers.add(person1);
        return checkedUsers;
    }

    public void setCheckedUsers(List<Person> checkedUsers) {
        this.checkedUsers = checkedUsers;
    }

    public void addUsers() {
        // TODO
    }
}

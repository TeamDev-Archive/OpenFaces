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

package org.openfaces.testapp.ajaxTesting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PeopleListBean {

    private List<Person> person = new ArrayList<Person>();

    public PeopleListBean() {

        person.add(new Person(
                "John Smith",
                "Programmer",
                "Bowling, cinema",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "James Erratic",
                "Technical writer",
                "Walking and thinking",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Bushy Toy",
                "Sales manager",
                "Painting",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Christina Strange",
                "Programmer",
                "Sitting on armchair",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "William Green",
                "Manager",
                "Sky-jumping",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Albert Ordinary",
                "Technical writer",
                "Watching TV",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Chris Lee",
                "Architect",
                "Ancient history",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Jane White",
                "Programmer",
                "Spent time in clubs",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Jimmy Cheerful",
                "Technical writer",
                "Snowboard, sky-jumping",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Andrew Ambitious",
                "Sales manager",
                "Gardening",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Bob Forgetive",
                "Designer",
                "Playing the piano",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Michael Equable",
                "Programmer",
                "Bowling, cinema",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Ike Adolescent",
                "Programmer",
                "Playing the piano",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Mary Honey",
                "Designer",
                "Needlework",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Den Glamourous",
                "Designer",
                "Painting",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Larry Smart",
                "Programmer",
                "Mind games",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Gary Efficient",
                "Programmer",
                "Bowling, cinema",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "George Mediocrity",
                "Designer",
                "Cooking",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Walter Charitable",
                "Programmer",
                "History, archaeology",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Clayton Major",
                "Manager",
                "Just sleeping",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Christian Smile",
                "Manager",
                "Just sleeping",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Diana Ironist",
                "Designer",
                "Music",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Joe Tricky",
                "Programmer",
                "Skating, snowboarding",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
        person.add(new Person(
                "Dean Genius",
                "Manager",
                "Cooking",
                (int) (Math.random() * 100),
                new Date(),
                ""
        ));
    }

    public List getPerson() {
        return person;
    }

}

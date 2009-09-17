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
package org.openfaces.portlets;

import org.openfaces.component.filter.ContainsFilterCriterion;

import java.util.ArrayList;
import java.util.List;

public class PeopleList {

  private List myPerson = new ArrayList();
  private ContainsFilterCriterion myFilterValue = new ContainsFilterCriterion("a");
  private Person mySelectedRow1;
  private Person mySelectedRow2;
  private List mySelectedRows = new ArrayList();

  public PeopleList() {

    myPerson.add(new Person(
            "John Smith (Джон Смит)",
            "Programmer (Программист)",
            "Bowling, cinema (Боулинг, кино)"
    ));
    myPerson.add(new Person(
            "James Erratic (Джеймс Эрратик)",
            "Technical writer (Технический писатель)",
            "Walking and thinking"
    ));
    myPerson.add(new Person(
            "Bushy Toy (Буши Той)",
            "Sales manager (Менеджер по продажам)",
            "Painting"
    ));
    myPerson.add(new Person(
            "Christina Strange (Кристина Стрэинж)",
            "Programmer (Программист)",
            "Sitting on armchair"
    ));
    myPerson.add(new Person(
            "William Green (Уиллиам Грин)",
            "Manager (Менеджер)",
            "Sky-jumping"
    ));
    myPerson.add(new Person(
            "Albert Ordinary (Альберт Ординари)",
            "Technical writer (Технический писатель)",
            "Watching TV"
    ));
    myPerson.add(new Person(
            "Chris Lee (Крис Ли)",
            "Architect (Архитектор)",
            "Ancient history"
    ));
    myPerson.add(new Person(
            "Jane White (Джейн Уайт)",
            "Programmer (Программист)",
            "Spent time in clubs"
    ));
    myPerson.add(new Person(
            "Jimmy Cheerful (Джимми Чирфул)",
            "Technical writer (Технический писатель)",
            "Snowboard, sky-jumping"
    ));
    myPerson.add(new Person(
            "Andrew Ambitious (Эндрю Амбишос)",
            "Sales manager (Менеджер по продажам)",
            "Gardening"
    ));
    myPerson.add(new Person(
            "Bob Forgetive (Боб Фогэтив)",
            "Designer (Дизайнер)",
            "Playing the piano"
    ));
    myPerson.add(new Person(
            "Michael Equable (Майкл Иквабл)",
            "Programmer (Программист)",
            "Bowling, cinema"
    ));
    myPerson.add(new Person(
            "Ike Adolescent",
            "Programmer (Программист)",
            "Playing the piano"
    ));
    myPerson.add(new Person(
            "Mary Honey",
            "Designer (Дизайнер)",
            "Needlework"
    ));
    myPerson.add(new Person(
            "Den Glamourous",
            "Designer (Дизайнер)",
            "Painting"
    ));
    myPerson.add(new Person(
            "Larry Smart",
            "Programmer (Программист)",
            "Mind games"
    ));
    myPerson.add(new Person(
            "Gary Efficient",
            "Programmer (Программист)",
            "Bowling, cinema"
    ));
    myPerson.add(new Person(
            "George Mediocrity",
            "Designer (Дизайнер)",
            "Cooking"
    ));
    myPerson.add(new Person(
            "Walter Charitable",
            "Programmer (Программист)",
            "History, archaeology"
    ));
    myPerson.add(new Person(
            "Clayton Major",
            "Manager (Менеджер)",
            "Just sleeping"
    ));
    myPerson.add(new Person(
            "Christian Smile",
            "Manager (Менеджер)",
            "Just sleeping"
    ));
    myPerson.add(new Person(
            "Diana Ironist",
            "Designer (Дизайнер)",
            "Music"
    ));
    myPerson.add(new Person(
            "Joe Tricky",
            "Programmer (Программист)",
            "Skating, snowboarding"
    ));
    myPerson.add(new Person(
            "Dean Genius",
            "Manager (Менеджер)",
            "Cooking"
    ));
  }

  public List getPerson() {
    return myPerson;
  }


  public ContainsFilterCriterion getFilterValue() {
    return myFilterValue;
  }

  public void setFilterValue(ContainsFilterCriterion filterValue) {
    myFilterValue = filterValue;
  }


  public Person getSelectedRow1() {
    return mySelectedRow1;
  }

  public void setSelectedRow1(Person selectedRow1) {
    mySelectedRow1 = selectedRow1;
  }


  public List getSelectedRows() {
    return mySelectedRows;
  }

  public void setSelectedRows(List selectedRows) {
    mySelectedRows = selectedRows;
  }

  public Person getSelectedRow2() {
    return mySelectedRow2;
  }

  public void setSelectedRow2(Person selectedRow2) {
    mySelectedRow2 = selectedRow2;
  }
}

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
package org.openfaces.demo.beans.datatable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookList implements Serializable {
    private List<Book> books = new ArrayList<Book>();
    private Book selectedBook;
    private List list = new ArrayList();

    public BookList() {
        books.add(new Book(
                "JSF. Manual",
                5874698214l,
                2005,
                "Smith",
                BookCategory.WEB_TECHNOLOGY
        ));
        books.add(new Book(
                "JSP. Tag library",
                5897246723l,
                2004,
                "Smith",
                BookCategory.WEB_TECHNOLOGY));
        books.add(new Book(
                "SQL. Introduction",
                7896324812l,
                2003,
                "ABC",
                BookCategory.DATABASES));
        books.add(new Book(
                "Internet protocols.Reference",
                6482157934l,
                2000,
                "ABC",
                BookCategory.NETWORKING
        ));
        books.add(new Book(
                "Introduction in XML",
                3625842798l,
                2006,
                "Jayson publishing house",
                BookCategory.ML
        ));
        books.add(new Book(
                "HTML and XML",
                6852439875l,
                2003,
                "ABC",
                BookCategory.ML
        ));
        books.add(new Book(
                "JavaScript for professionals",
                5378952146l,
                2005,
                "Jayson publishing house",
                BookCategory.WEB_TECHNOLOGY
        ));
    }

    public List<Book> getBooks() {
        return books;
    }

    public Book getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}

/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.selectmanycheckbox;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author Natalia Zolochevska
 */
public class Book {
    
    private String isbn;
    private String title;
    private List<String> authors;

    private String publisher;
    private int year;
    private int pages;

    private Collection<String> labels;

    public Book(String isbn, String title, List<String> authors, String publisher, int year, int pages, Collection<String> labels) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.labels = labels;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getAuthorsDescription() {
        return StringUtils.join(authors.iterator(), ", ");
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Collection<String> getLabels() {
        return labels;
    }

    public void setLabels(Collection<String> labels) {
        this.labels = labels;
    }
}

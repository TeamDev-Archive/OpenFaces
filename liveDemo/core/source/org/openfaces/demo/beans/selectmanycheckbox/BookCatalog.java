/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.selectmanycheckbox;

import org.openfaces.demo.beans.util.FacesUtils;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Natalia Zolochevska
 */
public class BookCatalog implements Serializable {

    private List<Book> books;
    private Book selectedBook;
    private List<String> selectedBookLabels;
    private Collection<SelectItem> labelItems;

    public List<Book> getBooks() {
        if (books == null) {
            books = Arrays.asList(
                    new Book("0764548395", "Java Servlet Programming Bible",
                            Arrays.asList("Suresh Rajagopalan", "Ramesh Rajamani", "Ramesh Krishnaswamy", "Sridhar Vijendran"),
                            "Wiley", 2002, 720,
                            Arrays.<String>asList("J2EE", "JSP")),

                    new Book("0131422464", "Core J2EE Patterns: Best Practices and Design Strategies",
                            Arrays.asList("Deepak Alur", "Dan Malks", "John Crupi"),
                            "Prentice Hall", 2003, 528,
                            Arrays.<String>asList("J2EE", "Patterns")),

                    new Book("0596007124", "Head First Design Patterns",
                            Arrays.asList("Elisabeth Freeman", "Eric Freeman", "Kathy Sierra"),
                            "O'Reilly Media", 2004, 676,
                            Arrays.<String>asList("Patterns")),

                    new Book("0735711952", "Java for the Web with Servlets, JSP, and EJB",
                            Arrays.asList("Budi Kurniawan"),
                            "Sams", 2002, 992,
                            Arrays.<String>asList("JSP", "EJB")),

                    new Book("1590595807", "Pro JSF and Ajax: Building Rich Internet Components",
                            Arrays.asList("John R. Fallows", "Jonas Jacobi"),
                            "Apress", 2006, 464,
                            Arrays.<String>asList("JSF", "Ajax", "RIA")),

                    new Book("0596517327", "Learning Flex 3: Getting Up to Speed with RIA",
                            Arrays.asList("Alaric Cole"),
                            "O'Reilly Media", 2008, 304,
                            Arrays.<String>asList("Flex", "RIA")),

                    new Book("0131738860", "Core JavaServer(TM) Faces",
                            Arrays.asList("David Geary", "Cay S. Horstmann"),
                            "Prentice Hall", 2007, 752,
                            Arrays.<String>asList("JSF")),

                    new Book("0131587560", "Persistence in the Enterprise: A Guide to Persistence Technologies ",
                            Arrays.asList("David Geary", "Cay S. Horstmann"),
                            "IBM Press", 2008, 464,
                            Arrays.<String>asList("J2EE", "ORM", "JDBC"))

            );
        }
        return books;
    }

    public Collection<SelectItem> getLabelItems() {

        if (labelItems == null) {
            Set<String> labels = new TreeSet<String>();
            for (Book book : getBooks()) {
                Collection<String> bookLabels = book.getLabels();
                if (bookLabels != null) {
                    labels.addAll(bookLabels);
                }
            }
            labelItems = new ArrayList<SelectItem>(labels.size());
            for (String label : labels) {
                labelItems.add(new SelectItem(label));
            }
        }

        return labelItems;
    }

    private Book findBookByIsbn(String isbn) {
        for (Book book : getBooks()) {
            if (isbn.equals(book.getIsbn())) {
                return book;
            }
        }
        return null;
    }

    public void selectBook(AjaxBehaviorEvent event) {
        String isbn = FacesUtils.getEventParameter(event, "isbn");
        selectedBook = findBookByIsbn(isbn);
        Collection<String> bookLabels = selectedBook.getLabels();
        if (bookLabels != null) {
            selectedBookLabels = new ArrayList<String>(bookLabels);
        } else {
            selectedBookLabels = null;
        }
    }

    public Book getSelectedBook() {
        return selectedBook;
    }

    public List<String> getSelectedBookLabels() {
        return selectedBookLabels;
    }

    public void setSelectedBookLabels(List<String> selectedBookLabels) {
        this.selectedBookLabels = selectedBookLabels;
    }

    public void saveBookLabels(AjaxBehaviorEvent event) {
        if (selectedBook != null) {
            selectedBook.setLabels(selectedBookLabels);
            selectedBook = null;
            selectedBookLabels = null;
        }
    }


}
/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.datatable;

import org.openfaces.event.FileUploadItem;

import java.io.Serializable;

public class Book implements Serializable {
    private String bookTitle;
    private long isbn;
    private int publicationDate;
    private String publisher;
    private BookCategory bookCategory;
    private FileUploadItem uploadedCoverImage;

    public Book(String bookTitle, long isbn, int publicationDate, String publisher, BookCategory bookCategory) {
        this.bookTitle = bookTitle;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.bookCategory = bookCategory;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getPublicationDate() {
        return publicationDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public long getIsbn() {
        return isbn;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Book book = (Book) o;

        if (isbn != book.isbn) return false;
        if (publicationDate != book.publicationDate) return false;
        if (bookCategory != null ? !bookCategory.equals(book.bookCategory) : book.bookCategory != null)
            return false;
        if (bookTitle != null ? !bookTitle.equals(book.bookTitle) : book.bookTitle != null) return false;
        if (publisher != null ? !publisher.equals(book.publisher) : book.publisher != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (bookTitle != null ? bookTitle.hashCode() : 0);
        result = 29 * result + (int) (isbn ^ (isbn >>> 32));
        result = 29 * result + publicationDate;
        result = 29 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 29 * result + (bookCategory != null ? bookCategory.hashCode() : 0);
        return result;
    }

    public FileUploadItem getUploadedCoverImage() {
        return uploadedCoverImage;
    }

    public void setUploadedCoverImage(FileUploadItem uploadedCoverImage) {
        this.uploadedCoverImage = uploadedCoverImage;
    }
}

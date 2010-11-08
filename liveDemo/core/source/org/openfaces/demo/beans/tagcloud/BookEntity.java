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

package org.openfaces.demo.beans.tagcloud;

/**
 * @author : roman.nikolaienko
 */
public class BookEntity extends TaggedEntity {

    private String title;
    private String year;
    private int pages;
    private String publisherHouse;

    protected BookEntity(String title, String publisherHouse,
                         String year, int pages,
            String publisher, String date, String imageUrl, int views, String... tags) {
        super(publisher, date, imageUrl, views, tags);
        setTitle(title);
        setPublisherHouse(publisherHouse);
        setYear(year);
        setPages(pages);
        setType(EntityType.BOOKS);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getPublisherHouse() {
        return publisherHouse;
    }

    public void setPublisherHouse(String publisherHouse) {
        this.publisherHouse = publisherHouse;
    }

    @Override
    String getInfo() {
        StringBuilder rez = new StringBuilder();
        rez.append("<div class='book'>");
        rez.append("<div class='cover'> <img src='").append(getImageUrl()).append("'/></div>");
        rez.append("<div class='title'> ").append(getTitle()).append("</div>");
        rez.append("<div class='publisher'> ").append(getPublisher()).append("</div>");
        rez.append("<div class='other_info'> ").append(getPublisherHouse()).append(", ");
        rez.append(getYear()).append(", ");
        rez.append(getPages()).append("<i>pages</i></div>");
        
        rez.append("<div class='tags'><b>Related Tags:</b><i> ");
        for (String tag : getTags()) {
            rez.append("<a href='#'>").append(tag).append("</a></i> ");
        }  
         rez.append("</i></div><div class='date'><b>Date:</b> ").append(getDate()).append("</div>");
        rez.append("<div class='views'><b>Views: </b>").append(getViews()).append("</div></div>");
        return rez.toString();
    }
}

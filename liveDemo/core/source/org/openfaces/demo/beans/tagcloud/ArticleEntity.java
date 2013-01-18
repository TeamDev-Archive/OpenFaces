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

package org.openfaces.demo.beans.tagcloud;

/**
 * @author : roman.nikolaienko
 */
public class ArticleEntity extends TaggedEntity {

    private String title;    
    private String text;

    public ArticleEntity(String title, String text,
                            String publisher, String date, String imageUrl,
                            int views, String ... tags) {

        super(publisher, date, imageUrl, views, tags);
        setTitle(title);

        setText(text);
        setType(EntityType.ARTICLES);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getInfo() {
        StringBuilder rez = new StringBuilder();
        rez.append("<dl class='article'>");

        rez.append("<dt class='title'> ").append(getTitle()).append("</dt>");
        rez.append("<dd class='text'> ").append(getText()).append("</dd>");
        rez.append("<dd class='publisher'><b>Published by:</b> ").append(getPublisher()).append("</dd>");

        rez.append("<dd class='tags'><b>Related Tags:</b> ");
        for (String tag : getTags()) {
            rez.append("<a class='tag' href='#'>").append(tag).append("</a> ");
        }
        
        rez.append("</dd><dd class='date'><b>Date:</b> ").append(getDate()).append("</dd>");
        rez.append("<dd class='views'><b>Views:</b> ").append(getViews()).append("</dd></dl>");

        return rez.toString();
    }
}

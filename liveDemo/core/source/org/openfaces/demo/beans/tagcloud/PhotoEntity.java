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
public class PhotoEntity extends TaggedEntity{

    public PhotoEntity(String publisher, String date, String imageUrl, int views,  String ... tags) {
        super(publisher, date, imageUrl, views, tags);
        setType(EntityType.PHOTOS);
    }

    @Override
    String getInfo() {
        StringBuilder rez = new StringBuilder();
        rez.append("<div class='photo'>");

        rez.append("<img src='").append(getImageUrl()).append("'/>");
        
        rez.append("<div class='publisher'><b>Published by:</b> ").append(getPublisher()).append("</div>");

        rez.append("<div class='tags'><b>Related Tags:</b><i> ");
        for(String tag : getTags()){
            rez.append("<a href='#'>").append(tag).append("</a> ");
        }
        rez.append("</i></div><div class='date'><b>Date:</b> ");
        rez.append(getDate()).append("</div>");
        rez.append("<div class='views'><b>Views:</b>").append(getViews()).append("</div></div>");
        return rez.toString();
    }
}

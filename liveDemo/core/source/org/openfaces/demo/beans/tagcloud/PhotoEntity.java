/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
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
    public String getInfo() {
        StringBuilder rez = new StringBuilder();
        rez.append("<dl class='photo'>");

        rez.append("<dt><img src='").append(getImageUrl()).append("'/></dt>");
        
        rez.append("<dd class='publisher'><b>Published by:</b> ").append(getPublisher()).append("</dd>");

        rez.append("<dd class='tags'><b>Related Tags:</b> ");
        for(String tag : getTags()){
            rez.append("<a class='tag' href='#'>").append(tag).append("</a>");
        }
        rez.append("</dd><dd class='date'><b>Date: </b> ");
        rez.append(getDate()).append("</dd>");
        rez.append("<dd class='views'><b>Views:</b> ").append(getViews()).append("</dd></dl>");
        return rez.toString();
    }
}

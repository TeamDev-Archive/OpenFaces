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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author : roman.nikolaienko
 */
public abstract class TaggedEntity implements Serializable{

    private EntityType type;

    private String publisher;
    private String date;
    private String imageUrl;
    private int views;

    private Set<String> tags = new TreeSet<String>();

    protected TaggedEntity(String publisher, String date, String imageUrl, int views, String ... tags) {
        this.publisher = publisher;        
        this.date = date;
        this.imageUrl = imageUrl;
        this.views = views;
        this.tags.addAll(Arrays.asList(tags));
    }
    
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public abstract String getInfo();

    public void deleteTag(String tag){
        tags.remove(tag);
    }

    public void addTag(String tag){
        tags.add(tag);
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public boolean hasTag(String tag){
        return tags.contains(tag);
    }
}

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

package org.openfaces.testapp.tagcloud;

import java.util.Date;
import java.util.Random;

/**
 * @author : roman.nikolaienko
 */
public class Item {
    private static final Random random = new Random();
    private String text;
    private Date dateText;
    private Number numberText;

    private String title;
    private String url;
    private double weight;

    public Item() {
        this.text = "empty";
        this.dateText = new Date(System.nanoTime());
        this.numberText = 0;
        this.title = "title"+numberText;
        this.url = "#";
        this.weight = 0;
    }

    public Item(String text, Date dateText, Number numberText) {
        this.text = text;
        this.dateText = dateText;
        this.numberText = numberText;
        this.title = "title"+numberText;
        this.url = "#";
        this.weight = random.nextInt(500);
    }

    public Item(String text, String title, String url, int weight) {
        this.text = text;
        this.title = title;
        this.url = url;
        this.weight = weight;
    }

    public Item(Date dateText, String title, String url, int weight) {
        this.dateText = dateText;
        this.title = title;
        this.url = url;
        this.weight = weight;
    }

    public Item(Number numberText, String title, String url, int weight) {
        this.numberText = numberText;
        this.title = title;
        this.url = url;
        this.weight = weight;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Number getNumberText() {
        return numberText;
    }

    public void setNumberText(Number numberText) {
        this.numberText = numberText;
    }

    public Date getDateText() {
        return dateText;
    }

    public void setDateText(Date dateText) {
        this.dateText = dateText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String toString(){
        return text;
    }
}

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

import org.openfaces.demo.beans.dropdown.Color;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

/**
 * @author : roman.nikolaienko
 */
public class Item implements Serializable {
    private static final Random random = new Random();

    private String id;
    private String text;
    private Date dateText;
    private Number numberText;

    private String title;
    private String url;
    private double weight;

    private Color color;

     public Item() {
        this.text = "";
        this.dateText = new Date(System.nanoTime());
        this.numberText = 0;
        this.title = "title" + numberText;
        this.url = "#";
        this.weight = 0;      
    }

    public Item(String text, Date dateText, Number numberText) {
        this.text = text;
        this.dateText = dateText;
        this.numberText = numberText;
        this.title = "title" + numberText;
        this.url = "#";
        this.weight = random.nextInt(500);
    }


    public Item(String text, String title, String url, int weight) {
        this.text = text;
        this.title = title;
        this.url = url;
        this.weight = weight;
        this.dateText = new Date(System.nanoTime());
        this.numberText  = random.nextInt(500);
    }

    public Item(String text, String title, String url, int weight, Color color) {
        this.text = text;
        this.title = title;
        this.url = url;
        this.weight = weight;
        this.color = color;
        this.dateText = new Date(System.nanoTime());
        this.numberText  = random.nextInt(500);
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

    public String toString() {
//        StringBuilder rez = new StringBuilder();
//        rez.append("Item[text = ").append(text).append(", ");
//        rez.append("dateText = ").append(dateText).append(", ");
//        rez.append("numberText = ").append(numberText).append(", ");
//        rez.append("title = ").append(title).append(", ");
//        rez.append("url = ").append(url).append(", ");
//        rez.append("weight = ").append(weight).append("] ");
//
//        return rez.toString();
        return getText();
    }


}

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

package org.openfaces.demo.beans.selectoneradio;

/**
 * @author Natalia Zolochevska
 */
public class Track {

    private int number;
    //private String writer;
    private String artist;
    private String title;
    //private String length;
    private String albumName;
    private String coverImageFileName;


    public Track(int number, String artist, String title, String albumName, String coverImageFileName) {
        this.number = number;
        this.artist = artist;
        this.title = title;
        this.albumName = albumName;
        this.coverImageFileName = coverImageFileName;
    }

    /*public Track(int number, String writer, String title, String length) {
        this.number = number;
        this.writer = writer;
        this.title = title;
        this.length = length;
    }*/

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    /*public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }*/

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }*/

    public String getAlbumName() {
        return albumName;
    }

    public String getCoverImageFileName() {
        return coverImageFileName;
    }

    public void setCoverImageFileName(String coverImageFileName) {
        this.coverImageFileName = coverImageFileName;
    }    

    @Override
    public String toString() {
        return new StringBuilder().append(getTitle()).append(" -- ")
                .append(getArtist()).append(" (").append(getAlbumName()).append(")").toString();
    }
}

/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.selectoneradio;

import javax.faces.model.SelectItem;
import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrackList {
    private List<Track> trackList;
    private List<SelectItem> trackListItems;
    private Track playing;
    private TrackConverter trackConverter;

    private List<SelectItem> effectItems;

    private List<Track> getTrackList() {
        if (trackList == null){
             trackList = Arrays.asList(
                 new Track(1, "Lady Gaga", "Alejandro", "The Frame Monster", "gaga.png"),
                 new Track(2, "NOHA", "Bas From Above", "Dive In Your Life", "noha.png"),
                 new Track(3, "Madonna", "Celebration", "Celebration", "madonna.png"),
                 new Track(4, "Katy Perry", "Hot n Cold", "One of the Boys", "perry.png"),
                 new Track(5, "Elbow", "The Bones Of You", "The Seldom Seen Kid", "elbow.png"),
                 new Track(6, "AC/DC", "War Machine", "Black Ice", "acdc.png")
               /* new Track(1, "Vivaldi", "Four seasons - Summer", "3:14"),
                new Track(2, "Grieg", "The morning", "4:02"),
                new Track(3, "Bach", "Air", "4:18"),
                new Track(4, "Tchaikovsky", "Andante", "4:22"),
                new Track(5, "Schubert", "Ave Maria", "4:16"),
                new Track(6, "Vivaldi", "Addia mare", "4:12"),
                new Track(7, "Bizet", "Carmen 'Habanera'", "3:21"),
                new Track(8, "Beethoven", "Moonlight sonata", "4:18"),
                new Track(9, "Brahms", "Hungarian dance", "2:19"),
                new Track(10, "Grieg", "Anita's dance", "3:17"),
                new Track(11, "Monti", "Czardas", "6:35"),
                new Track(12, "Dvorjak", "Humoresque", "2:34"),
                new Track(13, "Prokofiev", "Romeo et Juliet", "3:38"),
                new Track(14, "Paganini", "Capriccio", "2:52"),
                new Track(15, "Rossini", "La caza landra", "4:29"),
                new Track(16, "Brahms", "Waltz As-dur", "3:11"),
                new Track(17, "Rimsky-Korsakov", "Fly of the bumble bee", "1:13"),
                new Track(18, "Albinoni", "Adagio", "4:40"),
                new Track(19, "Paganini", "Il carnivale di Venezia", "3:43"),
                new Track(20, "Beethoven", "Fur Elise", "2:48"),
                new Track(21, "Vivaldi", "La stravaganza", "3:17"),
                new Track(22, "Goya", "Elvira Mandigan", "3:37"),
                new Track(23, "Verdi", "La Violetta", "3:04"),
                new Track(24, "Bizet", "March", "3:05"),
                new Track(25, "Vivaldi", "Devil's trill", "3:37"),
                new Track(26, "Gershwin", "Rhapsody in blue", "5:27"),
                new Track(27, "Bizet", "Pearl fishers duet", "3:43"),
                new Track(28, "Bach", "A joke", "1:19"),
                new Track(29, "Mozart", "Menuetto", "2:20"),
                new Track(30, "Vivaldi", "Autumn 'La caccia'", "3:14"),
                new Track(31, "Rossini", "Estate veneziana", "3:27"),
                new Track(32, "Mendelsohn", "Herald angels sing", "2:30"),
                new Track(33, "Gershwin", "Romance", "3:13"),
                new Track(34, "Rossini", "Figaro", "3:21"),
                new Track(35, "Goya", "Fantasie", "4:19"),
                new Track(36, "Steiner", "Solle", "3:32"),
                new Track(37, "Bach", "Presto", "3:25"),
                new Track(38, "Strauss", "Valse", "3:32") */
        );
        }
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }

    public List<SelectItem> getTrackListItems() {
        if (trackListItems == null){
            List<Track> trackList = getTrackList();
            trackListItems = new ArrayList<SelectItem>(trackList.size());
            for (Track track : trackList){
                trackListItems.add(new SelectItem(track, track.toString()));
            }
        }
        return trackListItems;
    }

    public void setTrackListItems(List<SelectItem> trackListItems) {
        this.trackListItems = trackListItems;
    }

    public Track getPlaying() {
        return playing;
    }

    public void setPlaying(Track playing) {       
        this.playing = playing;
    }

    public TrackConverter getTrackConverter() {
        if (trackConverter == null){
            trackConverter = new TrackConverter();
        }
        return trackConverter;
    }

    public void setTrackConverter(TrackConverter trackConverter) {
        this.trackConverter = trackConverter;
    }

    private class TrackConverter implements Converter{

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            return String.valueOf(((Track)value).getNumber());
        }

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            Integer number = Integer.valueOf(value);
            List<Track> trackList = getTrackList();
            for (Track track : trackList){
                if (number.equals(track.getNumber())){
                    return track;
                }
            }
            return null;
        }

    }

    public List<SelectItem> getEffectItems() {
        if (effectItems == null) {
            effectItems = Arrays.asList(
                    new SelectItem("Compressor"), //Динамическое сжатие
                    new SelectItem("Crystality"),
                    new SelectItem("True Bass"), //Усиление низких частот
                    new SelectItem("Band Pass"), //Частотный фильтр
                    new SelectItem("Voice Removal"),
                    new SelectItem("Normalizer"), //Автоподбор громкости
                    new SelectItem("Freeverb"), //Объемность
                    new SelectItem("Treble Enhancer"), //Усиление высоких частот
                    new SelectItem("Downmix to mono"), //Преобразование в моно
                    new SelectItem("Voice Emphasis"), //Усиление голоса
                    new SelectItem("Noise Reduction"),
                    new SelectItem("3D Effect"));
        }
        return effectItems;
    }

    public void setEffectItems(List<SelectItem> effectItems) {
        this.effectItems = effectItems;
    }
}

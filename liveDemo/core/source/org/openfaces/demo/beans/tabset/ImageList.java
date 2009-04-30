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
package org.openfaces.demo.beans.tabset;

import org.openfaces.event.SelectionChangeEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageList implements Serializable {
    private static final String STYLE_CUSTOM = "Custom Style";
    private static final String STYLE_DEFAULT = "Default Style";

    private List<String> styleNames = Arrays.asList(STYLE_CUSTOM, STYLE_DEFAULT);
    private int styleIndex = 0;
    private List<SelectorStyle> authorStyles = new ArrayList<SelectorStyle>();
    private List<SelectorStyle> imageStyles = new ArrayList<SelectorStyle>();

    private List<Author> authors = Arrays.asList(Author.MONET, Author.DALI, Author.KANDINSKY, Author.REMBRANDT);
    private List<Image> imageList = new ArrayList<Image>();

    private int authorIndex;
    private int imageIndex;

    public ImageList() {
        authorStyles.add(new SelectorStyle(STYLE_CUSTOM,
                "background-color: #fbfaf1; font-family: 'Times New Roman', Times, serif; font-size: 14px; color: #5f4414; white-space: nowrap; height: 25px; text-align: center; vertical-align: middle;",
                "background-color: #f3f1e6;",
                "color: #ffffff; font-weight: bold; background-color: #715a2e;",
                "background-color: #715a2e;",
                "1px solid #5f4414", "1px solid #5f4414"));
        authorStyles.add(new SelectorStyle(STYLE_DEFAULT, "padding: 2px 4px 2px 4px;", null, null, null, null, null));

        imageStyles.add(new SelectorStyle(STYLE_CUSTOM,
                "background-color: #fbfaf1; font-family: 'Times New Roman', Times, serif; font-size: 14px; color: #5f4414; white-space: nowrap; height: 25px; padding-left: 7px;",
                "background-color: #f3f1e6;",
                "border-right: 3px solid #5f4414; font-weight: bold;",
                "",
                "1px solid #5f4414", "1px solid #5f4414"));
        imageStyles.add(new SelectorStyle(STYLE_DEFAULT, "padding: 2px 4px 2px 4px;", null, null, null, null, null));

        populateImageList();
    }

    private void populateImageList() {
        imageList.add(new Image(Author.MONET,
                "../images/tabset/monet/HousesOfParliamentAtSunset.gif", "Houses of Parliament at Sunset"));
        imageList.add(new Image(Author.MONET,
                "../images/tabset/monet/JapaneseFootbribge.gif", "Japanese Footbribge"));
        imageList.add(new Image(Author.MONET,
                "../images/tabset/monet/MadameMonetAndHerSon.gif", "Madame Monet and Her Son"));
        imageList.add(new Image(Author.MONET,
                "../images/tabset/monet/WaterLilies.gif", "Water Lilies"));

        imageList.add(new Image(Author.DALI,
                "../images/tabset/dali/WomanAtTheWindow.jpg", "Woman at the Window"));
        imageList.add(new Image(Author.DALI,
                "../images/tabset/dali/ThePersistenceOfMemory.jpg", "The Persistance of Memory"));
        imageList.add(new Image(Author.DALI,
                "../images/tabset/dali/SoftSelfPortraitWithGrilledBacon.jpg", "Soft Self-Portrait with Grilled Bacon"));
        imageList.add(new Image(Author.DALI,
                "../images/tabset/dali/Melancholy.jpg", "Melancholy"));
        imageList.add(new Image(Author.DALI,
                "../images/tabset/dali/ImpressionsOfAfrica.jpg", "Impressions of Africa"));
        imageList.add(new Image(Author.DALI,
                "../images/tabset/dali/EnigmaticElementsInTheLandscape.jpg", "Enigmatic Elements in the Landscape"));

        imageList.add(new Image(Author.KANDINSKY,
                "../images/tabset/kandinsky/YellowRedBlue.jpg", "Yellow, Red, Blue"));
        imageList.add(new Image(Author.KANDINSKY,
                "../images/tabset/kandinsky/RavineImprovisation.jpg", "Ravine Improvisation"));
        imageList.add(new Image(Author.KANDINSKY,
                "../images/tabset/kandinsky/ContrastingSounds.jpg", "Contrasting Sounds"));
        imageList.add(new Image(Author.KANDINSKY,
                "../images/tabset/kandinsky/CompositionVIII.jpg", "Composition VIII"));
        imageList.add(new Image(Author.KANDINSKY,
                "../images/tabset/kandinsky/BlackAndViolet.jpg", "Black and Violet"));
        imageList.add(new Image(Author.KANDINSKY,
                "../images/tabset/kandinsky/AutumnInBavaria.jpg", "Autumn in Bavaria"));

        imageList.add(new Image(Author.REMBRANDT,
                "../images/tabset/rembrandt/WinterLandscape.jpg", "Winter Landscape"));
        imageList.add(new Image(Author.REMBRANDT,
                "../images/tabset/rembrandt/TheReturnOfTheProdigalSon.jpg", "The Return of the Prodigal Son"));
        imageList.add(new Image(Author.REMBRANDT,
                "../images/tabset/rembrandt/TheMusicParty.jpg", "The Music Party"));
        imageList.add(new Image(Author.REMBRANDT,
                "../images/tabset/rembrandt/TheMill.jpg", "The Mill"));
    }

    public List<String> getStyleNames() {
        return styleNames;
    }

    public int getStyleIndex() {
        return styleIndex;
    }

    public void setStyleIndex(int styleIndex) {
        this.styleIndex = styleIndex;
    }

    public SelectorStyle getAuthorSelectorStyle() {
        return authorStyles.get(styleIndex);
    }

    public SelectorStyle getImageSelectorStyle() {
        return imageStyles.get(styleIndex);
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public int getAuthorIndex() {
        return authorIndex;
    }

    public void setAuthorIndex(int authorIndex) {
        this.authorIndex = authorIndex;
    }

    public void selectedAuthorChanged(SelectionChangeEvent e) {
        imageIndex = 0;
    }

    public List<Image> getImageList() {
        return imageList;
    }


    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public List<String> getSelectedImages() {
        List<String> currentImageList = new ArrayList<String>();
        String currentAuthorName = authors.get(authorIndex).toString();
        for (Image image : imageList) {
            String authorName = image.getAuthor().toString();
            if (authorName.equals(currentAuthorName)) {
                String currentImageName = image.getName();
                currentImageList.add(currentImageName);
            }
        }
        return currentImageList;
    }

    public String getCurrentImageUrl() {
        String currentImageFileName = "monet/HousesOfParliamentAtSunset.gif";
        String currentImageName = getSelectedImages().get(imageIndex);
        for (Image image : imageList) {
            String imageName = image.getName();
            if (imageName.equals(currentImageName)) {
                currentImageFileName = image.getFileName();
            }
        }
        return currentImageFileName;
    }

}
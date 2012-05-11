/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author : roman.nikolaienko
 */
public class TaggedEntityStore implements Serializable {

    private Random random = new Random();
    private List<TaggedEntity> entities;
    private List<ArticleEntity> articles;
    private List<BookEntity> books;
    private List<PhotoEntity> photos;

    private Set<String> tags;

    public List<TaggedEntity> getEntities() {
        if (entities == null) {
            entities = new ArrayList<TaggedEntity>();
            entities.addAll(getArticles());
            entities.addAll(getBooks());
            entities.addAll(getPhotos());
            Collections.shuffle(entities);
        }
        return entities;
//        TaggedEntity currEntity;
//        for (int i = 0; i < length; i++) {
//            currEntity = new ArticleEntity("", getRandomTextWithWhiteSpaces(20, 50), getRandomText(5, 8) + " " + getRandomText(8, 10),
//                    getRandomDate(), "",
//                    getRandomLength(1000, 4500));
//            for (int j = 0; j < getRandomLength(1, 4); j++) {
//                currEntity.addTag(tags.get(random.nextInt(tags.size())));
//            }
//            entities.add(currEntity);
//        }
//
//        for (int i = 1; i < 21; i++) {
//            currEntity = new PhotoEntity(getRandomText(5, 8) + " " + getRandomText(8, 10),
//                    getRandomDate(),
//                    "./images/photo_" + i + ".jpg",
//                    getRandomLength(1000, 4500)
//            );
//            for (int j = 0; j < getRandomLength(1, 4); j++) {
//                currEntity.addTag(tags.get(random.nextInt(tags.size())));
//            }
//            entities.add(currEntity);
//        }             
    }

    private int getRandomLength(int minTextLength, int maxTextLength) {
        return random.nextInt(maxTextLength - minTextLength) + minTextLength;
    }

    public List<ArticleEntity> getArticles() {
        if (articles == null) {
            articles = Arrays.asList(
                    new ArticleEntity(
                            "Introduction to Comet and Reverse AJAX",
                            "Comet is capable of solving this issue by enabling the web servers...",
                            "Sureesh Joseph",
                            "Mon Sep 27th, 2010", "",
                            getRandomLength(1000, 4500),
                            "ajax", "comet"),
                    new ArticleEntity(
                            "Introduction to Facelets",
                            "This article will provide an introduction to the Facelets framewor...",
                            "Christy",
                            "Sun Sep 26th, 2010", "",
                            getRandomLength(1000, 4500),
                            "facelets", "jsf", "jsp"),
                    new ArticleEntity(
                            "Introduction to Spring Python",
                            "This article focuses on providing an introduction to the Spring Py...",
                            "Christy",
                            "Sun Sep 26th, 2010", "",
                            getRandomLength(1000, 4500),
                            "spring", "python", "ajax"),
                    new ArticleEntity(
                            "Introduction to JSFUnit",
                            "Testing has become an important aspect for every application and a...",
                            "Christy",
                            "Sun Sep 19th, 2010", "",
                            getRandomLength(1000, 4500),
                            "facelets", "jsf", "jsp"),
                    new ArticleEntity(
                            "Window System in NetBeans Platform 6.9",
                            "In short, the requirements for window management have become quite...",
                            "PacktPub",
                            "Fri Sep 17th, 2010", "",
                            getRandomLength(1000, 4500),
                            "java"),
                    new ArticleEntity(
                            "Introduction to Spring Batch",
                            "In this article, we will have an overview of Spring Batch which pr...",
                            "Raja",
                            "Thu Sep 16th, 2010", "",
                            getRandomLength(1000, 4500),
                            "java", "spring"),
                    new ArticleEntity(
                            "Sending and Receiving messages using Spring's AMQP",
                            "The messaging standards that an application uses for communication...",
                            "Raja",
                            "Thu Sep 16th, 2010", "",
                            getRandomLength(1000, 4500),
                            "java", "spring"),
                    new ArticleEntity(
                            "How to Configure JMS in GlassFish 3 Application Server?",
                            "The Java Messaging API (JMS) provides a mechanism for Java EE appl...",
                            "PacktPub",
                            "Fri Sep 10th, 2010", "",
                            getRandomLength(1000, 4500),
                            "java","j2ee"),
                    new ArticleEntity(
                            "JavaFX Media",
                            "One of the most celebrated features of JavaFX is its inherent supp...",
                            "PacktPub",
                            "Mon Sep 6th, 2010", "",
                            getRandomLength(1000, 4500),
                            "java")
            );
        }
        return articles;
    }

    public List<BookEntity> getBooks() {
        if (books == null) {
            books = Arrays.asList(
//                    new BookEntity("Freedom: A Novel",
//                            "Farrar, Straus and Giroux","2010", 576,
//                            "Jonathan Franzen", "23.03.2010", "./images/book_cover_1.jpg",
//                            getRandomLength(1000, 4500), "freedom","nature"),
//                    new BookEntity("The Stokes Field Guide to the Birds of North America", "Little, Brown and Company", "2010", 816 , "Donald Stokes", "31.11.2010", "./images/book_cover_2.jpg", getRandomLength(1000, 4500), "birds"),
//                    new BookEntity("The Best American Science and Nature Writing 2010", "Mariner Books", "2010", 416 , "Donald Stokes", "31.09.2010", "./images/book_cover_3.jpg", getRandomLength(1000, 4500), "nature","environment","care"),
//                    new BookEntity("Naturally Curious: A Photographic Field Guide and Month-by-Month Journey Through the Fields, Woods, and Marshes of New England",
//                            "Trafalgar Square Books", "2010", 496 , "Mary Holland", "20.10.2010", "./images/book_cover_4.jpg", getRandomLength(1000, 4500), "nature","tree","animals"),

                    new BookEntity("Java Servlet Programming Bible",
                            "Wiley", "2002", 720,
                            "Suresh Rajagopalan, Ramesh Rajamani, Ramesh Krishnaswamy, Sridhar Vijendran",
                            "21.09.2004","./images/0764548395.jpg",
                            getRandomLength(1000, 4500),"java","j2ee", "jsp"),

                    new BookEntity("Core J2EE Patterns: Best Practices and Design Strategies",
                            "Prentice Hall", "2003", 528,
                            "Deepak Alur, Dan Malks, John Crupi",
                            "3.01.2004","./images/0131422464.jpg",
                            getRandomLength(1000, 4500),  "j2ee", "patterns"),

                    new BookEntity("Head First Design Patterns",
                            "O'Reilly Media", "2004", 676,
                            "Elisabeth Freeman, Eric Freeman, Kathy Sierra",
                            "11.02.2006","./images/0596007124.jpg",
                            getRandomLength(1000, 4500), "java","patterns"),

                    new BookEntity("Java for the Web with Servlets, JSP, and EJB",
                            "Sams", "2002", 992,
                            "Budi Kurniawan",
                            "11.02.2006","./images/0735711952.jpg",
                            getRandomLength(1000, 4500), "java","jsp", "ejb"),

                    new BookEntity("Pro JSF and Ajax: Building Rich Internet Components",
                            "Apress", "2006", 464,
                            "John R. Fallows, Jonas Jacobi",
                            "11.01.2007","./images/1590595807.jpg",
                            getRandomLength(1000, 4500),"jsf", "ajax", "ria"),

                    new BookEntity("Learning Flex 3: Getting Up to Speed with RIA",
                            "O'Reilly Media", "2008", 304,
                            "Alaric Cole",
                            "11.01.2007","./images/0596517327.jpg",
                            getRandomLength(1000, 4500),"flex", "ria"),

                    new BookEntity("Core JavaServer(TM) Faces",
                            "Prentice Hall", "2007", 752,
                            "David Geary, Cay S. Horstmann",
                            "11.01.2007","./images/0131738860.jpg",
                            getRandomLength(1000, 4500),"jsf"),

                    new BookEntity("Persistence in the Enterprise: A Guide to Persistence Technologies ",
                            "IBM Press", "2008", 464,
                            "David Geary, Cay S. Horstmann",
                            "11.01.2007","./images/0131587560.jpg",
                            getRandomLength(1000, 4500),"j2ee", "orm", "jdbc")

            );

        }
        return books;
    }

    public List<PhotoEntity> getPhotos() {
        if (photos == null) {
            photos = Arrays.asList(
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_1.jpg", getRandomLength(1000, 4500), "birds", "nature"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_2.jpg", getRandomLength(1000, 4500), "beautiful", "expression"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_3.jpg", getRandomLength(1000, 4500), "nature", "animals", "sun"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_4.jpg", getRandomLength(1000, 4500), "freedom", "sun","environment"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_5.jpg", getRandomLength(1000, 4500), "nature", "freedom"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_6.jpg", getRandomLength(1000, 4500), "water", "tree"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_7.jpg", getRandomLength(1000, 4500), "tree", "beautiful"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_8.jpg", getRandomLength(1000, 4500), "expression", "care"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_9.jpg", getRandomLength(1000, 4500), "sky", "beautiful"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_10.jpg", getRandomLength(1000, 4500), "sky", "water", "freedom"),

                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_11.jpg", getRandomLength(1000, 4500), "sun", "sky"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_12.jpg", getRandomLength(1000, 4500), "water", "fish"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_13.jpg", getRandomLength(1000, 4500), "sky", "nature", "beautiful"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_14.jpg", getRandomLength(1000, 4500), "sky", "sun"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_15.jpg", getRandomLength(1000, 4500), "water", "freedom"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_16.jpg", getRandomLength(1000, 4500), "water", "care"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_17.jpg", getRandomLength(1000, 4500), "care","environment"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_18.jpg", getRandomLength(1000, 4500), "nature", "beautiful"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_19.jpg", getRandomLength(1000, 4500), "water", "sun"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_20.jpg", getRandomLength(1000, 4500), "nature", "sun")
            );
        }
        return photos;
    }

    public Set<String> getAllTags() {
        if (tags == null) {
            tags = new HashSet<String>();
            for (TaggedEntity current : getEntities()) {
                tags.addAll(current.getTags());
            }
        }
        return tags;
    }

}

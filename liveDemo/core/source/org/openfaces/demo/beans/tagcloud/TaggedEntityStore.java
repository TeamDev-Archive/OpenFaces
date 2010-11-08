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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
                            "Comet is capable of solving this issue by enabling the web servers to asynchronously push the data to clients through a single, previously opened connection. This approach was unheard previously with web applications. The Comet style applications can send the latest data from the server to client with negligible latency.",
                            "Sureesh Joseph",
                            "Mon Sep 27th, 2010", "",
                            getRandomLength(1000, 4500),
                            "ajax", "comet"),
                    new ArticleEntity(
                            "Introduction to Facelets",
                            "This article will provide an introduction to the Facelets framework with the assumption that the readers have a basic understanding on Java Server Pages. With the Introduction of JSF, the idea is to make JSP as the view technology for JSF. However the architecture of JSF and JSP are completely different and there were integration issues with the combination of JSF and JSP. With this in mind, Facelets was introduced which is another view definition framework similar to JSP. However, the architecture of the Facelets was designed with the complex JSF architecture and life-cycle in mind so that the component trees construction of Facelets can nicely mingle with JSF. Also in comparision with JSF, facelets can provide extending re-use of content code through templates.",
                            "Christy",
                            "Sun Sep 26th, 2010", "",
                            getRandomLength(1000, 4500),
                            "facelets", "jsf", "jsp"),
                    new ArticleEntity(
                            "Introduction to Spring Python",
                            "This article focuses on providing an introduction to the Spring Python which is a framework that simplifies writing Python applications. The first section of the article provides introductory details on setting up the Python environment and also introduces simple constructs on Python language which will be useful for novice Python programmers. The later section of the article focuses on setting up the Spring Python framework for writing Python applications using the Spring framework. This article provides various sample code snippets on various topics relevant to each section as and when required. This article assumes that the reader is comfortable with the core aspects of Spring framework like Spring Container, Dependency Injection etc.",
                            "Christy",
                            "Sun Sep 26th, 2010", "",
                            getRandomLength(1000, 4500),
                            "spring", "python", "ajax"),
                    new ArticleEntity(
                            "Introduction to JSFUnit",
                            "Testing has become an important aspect for every application and an application cannot be released unless it is not thoroughly tested. JSFUnit provides an attempt to bring in testing capabilities for JSF applications. Not many frameworks exists in the market for testing JSF applications and this framework which originated from JBoss community provides wider coverage for testing JSF applications with respect to the managed beans state, navigation flows, application configuration etc. This article is an attempt to provide an introduction to the framework JSFUnit.",
                            "Christy",
                            "Sun Sep 19th, 2010", "",
                            getRandomLength(1000, 4500),
                            "facelets", "jsf", "jsp"),
                    new ArticleEntity(
                            "Window System in NetBeans Platform 6.9",
                            "In short, the requirements for window management have become quite complex and can only be met by means of an external docking framework, otherwise all these various concerns would need to be coded (and debugged, tested, and maintained) by hand. The NetBeans Platform 6.9 provides all of these features via its docking framework, known as the NetBeans Window System. It also provides an APito let you programmatically access the Window System. Together, the Window System and its APifulfill all the requirements described above, letting you concentrate on your domain knowledge and business logic rather than on the work of creating a custom window management facility for each of your applications.",
                            "PacktPub",
                            "Fri Sep 17th, 2010", "",
                            getRandomLength(1000, 4500),
                            "java"),
                    new ArticleEntity(
                            "Introduction to Spring Batch",
                            "In this article, we will have an overview of Spring Batch which provides batch and bulk processing capabilities. The architecture is extremely robust and it provides parallel as well as scheduled batch processing. The API provides template and helper classes for repeatable and retryable operations which will be discussed in this article with suitable examples. The classes/interfaces in Spring Batch are not tied to a specified domain and thus it is possible to integrate an application in any business domain seamlessly. This article tries to explain the various concepts in a step-by-step fashion and examples are provided when necessary. This article assumes that the readers have a fair bit of understanding on Core Spring framework.",
                            "Raja",
                            "Thu Sep 16th, 2010", "",
                            getRandomLength(1000, 4500),
                            "java", "spring"),
                    new ArticleEntity(
                            "Sending and Receiving messages using Spring's AMQP",
                            "The messaging standards that an application uses for communication varies and Advanced Message Queuing Protocol (ADQP) aims in providing standards with respect to messaging communication like the format of the message, various contracts to be implied, etc. Note that ADQP is just a protocol and it is not tied with any specific technology or language (unlike JMS which is tightly coupled with Java). Some of the popular implementations available are Qpid (from Apache), Rabbit MQ (from Vmware) etc. Spring AMQP framework provides a complete end-to-end solution for integrating messaging solution providers into the application. Note that this framework itself doesn't provide the messaging solutions such as sending and receiving messages, but instead it simplifies the job of integrating applications with existing messaging solution providers.",
                            "Raja",
                            "Thu Sep 16th, 2010", "",
                            getRandomLength(1000, 4500),
                            "java", "spring"),
                    new ArticleEntity(
                            "Sending and Receiving messages using Spring's AMQP",
                            "The Java Messaging API (JMS) provides a mechanism for Java EE applications to send messages to each other. JMS applications do not communicate directly, instead message producers send messages to a destination and message consumers receive the message from the destination. The easiest way to set up a JMS connection factory is via GlashFish's web console. Recall from Chapter 1 that the web console can be accessed by starting our domain, by entering the following command in the command line:",
                            "PacktPub",
                            "Fri Sep 10th, 2010", "",
                            getRandomLength(1000, 4500),
                            "java"),
                    new ArticleEntity(
                            "JavaFX Media",
                            "One of the most celebrated features of JavaFX is its inherent support for media playback. As of version 1.2, JavaFX has the ability to seamlessly load images in different formats, play audio, and play video in several formats using its built-in components. To achieve platform independence and performance, the support for media playback in JavaFX is implemented as a two-tiered strategy:",
                            "PacktPub",
                            "Mon Sep 6th, 2010", "",
                            getRandomLength(1000, 4500),
                            "java"),
                    new ArticleEntity(
                            "JavaFX Media",
                            "One of the most celebrated features of JavaFX is its inherent support for media playback. As of version 1.2, JavaFX has the ability to seamlessly load images in different formats, play audio, and play video in several formats using its built-in components. To achieve platform independence and performance, the support for media playback in JavaFX is implemented as a two-tiered strategy:",
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
                    new BookEntity("Freedom: A Novel", "Farrar, Straus and Giroux", "2010", 576, "Jonathan Franzen", "23.03.2010", "./images/book_cover_1.jpg", getRandomLength(1000, 4500), "freedom","nature"),
                    new BookEntity("The Stokes Field Guide to the Birds of North America", "Little, Brown and Company", "2010", 816 , "Donald Stokes", "31.11.2010", "./images/book_cover_2.jpg", getRandomLength(1000, 4500), "birds"),
                    new BookEntity("The Best American Science and Nature Writing 2010", "Mariner Books", "2010", 416 , "Donald Stokes", "31.09.2010", "./images/book_cover_3.jpg", getRandomLength(1000, 4500), "nature","environment","care"),
                    new BookEntity("Naturally Curious: A Photographic Field Guide and Month-by-Month Journey Through the Fields, Woods, and Marshes of New England",
                            "Trafalgar Square Books", "2010", 496 , "Mary Holland", "20.10.2010", "./images/book_cover_4.jpg", getRandomLength(1000, 4500), "nature","tree","animals")


            );

        }
        return books;
    }

    public List<PhotoEntity> getPhotos() {
        if (photos == null) {
            photos = Arrays.asList(
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_1.jpg", getRandomLength(1000, 4500), "birds", "nature"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_2.jpg", getRandomLength(1000, 4500), "beautiful", "flower"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_3.jpg", getRandomLength(1000, 4500), "nature", "animals", "sun"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_4.jpg", getRandomLength(1000, 4500), "road", "sun","environment"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_5.jpg", getRandomLength(1000, 4500), "nature", "freedom"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_6.jpg", getRandomLength(1000, 4500), "water", "tree"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_7.jpg", getRandomLength(1000, 4500), "tree", "beautiful"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_8.jpg", getRandomLength(1000, 4500), "expression", "care"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_9.jpg", getRandomLength(1000, 4500), "sky", "beautiful"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_10.jpg", getRandomLength(1000, 4500), "sky", "water", "freedom"),

                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_11.jpg", getRandomLength(1000, 4500), "beach", "sun", "sky"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_12.jpg", getRandomLength(1000, 4500), "water", "fish"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_13.jpg", getRandomLength(1000, 4500), "sky", "nature", "beautiful"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_14.jpg", getRandomLength(1000, 4500), "sky", "sun"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_15.jpg", getRandomLength(1000, 4500), "water", "freedom"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_16.jpg", getRandomLength(1000, 4500), "water", "house"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_17.jpg", getRandomLength(1000, 4500), "care","environment"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_18.jpg", getRandomLength(1000, 4500), "butterfly"),
                    new PhotoEntity("Preeveer", "Mon Sep 6th, 2010", "./images/photo_19.jpg", getRandomLength(1000, 4500), "water", "waterfall"),
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

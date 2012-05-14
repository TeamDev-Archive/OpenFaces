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

package org.openfaces.demo.beans.datatable;

import java.io.Serializable;

/**
 * @author Darya Shumilina
 */
public class PaginatorStyleItem implements Serializable {

    private String firstDisabledImageUrl;
    private String firstImageUrl;
    private String nextDisabledImageUrl;
    private String nextImageUrl;

    private String previousDisabledImageUrl;
    private String previousImageUrl;
    private String lastDisabledImageUrl;
    private String lastImageUrl;

    private String pageNumberFieldStyle;

    public PaginatorStyleItem(String firstDisabledImageUrl, String firstImageUrl, String nextDisabledImageUrl,
                              String nextImageUrl, String previousDisabledImageUrl, String previousImageUrl,
                              String lastDisabledImageUrl, String lastImageUrl, String pageNumberFieldStyle) {
        this.firstDisabledImageUrl = firstDisabledImageUrl;
        this.firstImageUrl = firstImageUrl;
        this.nextDisabledImageUrl = nextDisabledImageUrl;
        this.nextImageUrl = nextImageUrl;

        this.previousDisabledImageUrl = previousDisabledImageUrl;
        this.previousImageUrl = previousImageUrl;
        this.lastDisabledImageUrl = lastDisabledImageUrl;
        this.lastImageUrl = lastImageUrl;

        this.pageNumberFieldStyle = pageNumberFieldStyle;
    }

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public String getNextDisabledImageUrl() {
        return nextDisabledImageUrl;
    }

    public String getNextImageUrl() {
        return nextImageUrl;
    }

    public String getPreviousDisabledImageUrl() {
        return previousDisabledImageUrl;
    }

    public String getPreviousImageUrl() {
        return previousImageUrl;
    }

    public String getLastDisabledImageUrl() {
        return lastDisabledImageUrl;
    }

    public String getLastImageUrl() {
        return lastImageUrl;
    }

    public String getPageNumberFieldStyle() {
        return pageNumberFieldStyle;
    }

    public String getFirstDisabledImageUrl() {
        return firstDisabledImageUrl;
    }
}
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

package org.openfaces.testapp.support.QKS1153;

/**
 * @author Tatyana Matveyeva
 */
public class CandidateTO {
    private String title;
    private String comment;
    private String commentInternal;


    public CandidateTO(String title, String comment, String commentInternal) {
        this.title = title;
        this.comment = comment;
        this.commentInternal = commentInternal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentInternal() {
        return commentInternal;
    }

    public void setCommentInternal(String commentInternal) {
        this.commentInternal = commentInternal;
    }
}
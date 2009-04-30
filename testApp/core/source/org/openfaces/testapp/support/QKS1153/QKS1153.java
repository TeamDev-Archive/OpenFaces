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

package org.openfaces.testapp.support.QKS1153;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class QKS1153 {

    private List<CandidateTO> candidateList;
    CandidateTO selectedCandidate;
    CandidateTO editedCandidate;

    String comment1 = "";
    String comment2 = "";

    public QKS1153() {
        candidateList = new ArrayList<CandidateTO>();
        candidateList.add(new CandidateTO("Title1", "Comment 1", "Comment Internal 1"));
        candidateList.add(new CandidateTO("Title2", "Comment 2", "Comment Internal 2"));
        candidateList.add(new CandidateTO("Title3", "Comment 3", "Comment Internal 3"));
    }


    public void editCandidate() {
        editedCandidate = selectedCandidate;
    }

    public CandidateTO getEditedCandidate() {
        return editedCandidate;
    }

    public void setEditedCandidate(CandidateTO editedCandidate) {
        this.editedCandidate = editedCandidate;
    }

    public CandidateTO getSelectedCandidate() {
        return selectedCandidate;
    }

    public void setSelectedCandidate(CandidateTO selectedCandidate) {
        this.selectedCandidate = selectedCandidate;
    }

    public String getComment() {
        if (editedCandidate != null)
            return editedCandidate.getComment();
        return "";
    }

    public void setComment(String comment) {
        if (editedCandidate != null)
            editedCandidate.setComment(comment);
    }

    public String getCommentInternal() {
        if (editedCandidate != null)
            return editedCandidate.getCommentInternal();
        return "";
    }

    public void setCommentInternal(String commentInternal) {
        if (editedCandidate != null)
            editedCandidate.setCommentInternal(commentInternal);
    }


    public List<CandidateTO> getCandidateList() {
        return candidateList;
    }

    public void setCandidateList(List<CandidateTO> candidateList) {
        this.candidateList = candidateList;
    }
}

/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.hintlabel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HintLabelDemo implements Serializable {
    private List<RequestIssue> requestList;
    private List<RequestIssue> truncatedRequestList;

    public HintLabelDemo() {
        requestList = createRequestList();
        truncatedRequestList = new ArrayList<RequestIssue>(requestList.subList(0, 5));
    }

    public List<RequestIssue> getTruncatedRequestList() {
        return truncatedRequestList;
    }

    public void setTruncatedRequestList(List<RequestIssue> truncatedRequestList) {
        this.truncatedRequestList = truncatedRequestList;
    }

    public List<RequestIssue> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<RequestIssue> requestList) {
        this.requestList = requestList;
    }

    private List<RequestIssue> createRequestList() {
        ArrayList<RequestIssue> requests = new ArrayList<RequestIssue>();
        LineNumberReader lineReader = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("iteration.txt")));

        try {
            for (String line = lineReader.readLine(); line != null; line = lineReader.readLine()) {
                RequestIssue issue = new RequestIssue();
                StringTokenizer tokens = new StringTokenizer(line, ";");
                issue.setIssueType(tokens.nextToken());
                issue.setKey(tokens.nextToken());
                issue.setSummary(tokens.nextToken());
                issue.setDescription(tokens.nextToken());
                issue.setAssignTo(tokens.nextToken());
                issue.setReporter(tokens.nextToken());
                issue.setPriority(tokens.nextToken());
                issue.setStatus(tokens.nextToken());
                issue.setResolution(tokens.nextToken());
                issue.setEstimate(tokens.nextToken());
                issue.setFixVersion(tokens.nextToken());
                issue.setUpdated(tokens.nextToken());
                requests.add(issue);
            }
            lineReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return requests;
    }
}
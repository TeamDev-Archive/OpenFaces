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

import java.io.Serializable;

public class RequestIssue implements Serializable {
    private String issueType;
    private String key;
    private String summary;
    private String description;
    private String assignTo;
    private String reporter;
    private String priority;
    private String status;
    private String resolution;
    private String estimate;
    private String fixVersion;
    private String updated;

    public String getSummaryTitle() {
        return "<div style='background-color: #DDDDDD; padding: 3px;'>" + summary + "</div>" +
                "<ul><li><b style='color: blue;'> resolution : </b>" + resolution + "</li>" +
                "<li><b style='color: blue;'> fix for&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : </b>" + fixVersion + "</li></ul><br/>";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public String getFixVersion() {
        return fixVersion;
    }

    public void setFixVersion(String fixVersion) {
        this.fixVersion = fixVersion;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final RequestIssue that = (RequestIssue) o;

        if (assignTo != null ? !assignTo.equals(that.assignTo) : that.assignTo != null) return false;
        if (estimate != null ? !estimate.equals(that.estimate) : that.estimate != null) return false;
        if (fixVersion != null ? !fixVersion.equals(that.fixVersion) : that.fixVersion != null) return false;
        if (issueType != null ? !issueType.equals(that.issueType) : that.issueType != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (priority != null ? !priority.equals(that.priority) : that.priority != null) return false;
        if (reporter != null ? !reporter.equals(that.reporter) : that.reporter != null) return false;
        if (resolution != null ? !resolution.equals(that.resolution) : that.resolution != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (summary != null ? !summary.equals(that.summary) : that.summary != null) return false;
        if (updated != null ? !updated.equals(that.updated) : that.updated != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (issueType != null ? issueType.hashCode() : 0);
        result = 29 * result + (key != null ? key.hashCode() : 0);
        result = 29 * result + (summary != null ? summary.hashCode() : 0);
        result = 29 * result + (assignTo != null ? assignTo.hashCode() : 0);
        result = 29 * result + (reporter != null ? reporter.hashCode() : 0);
        result = 29 * result + (priority != null ? priority.hashCode() : 0);
        result = 29 * result + (status != null ? status.hashCode() : 0);
        result = 29 * result + (resolution != null ? resolution.hashCode() : 0);
        result = 29 * result + (estimate != null ? estimate.hashCode() : 0);
        result = 29 * result + (fixVersion != null ? fixVersion.hashCode() : 0);
        result = 29 * result + (updated != null ? updated.hashCode() : 0);
        return result;
    }
}

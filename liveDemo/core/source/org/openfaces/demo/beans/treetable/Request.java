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
package org.openfaces.demo.beans.treetable;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    private String requestId;
    private List<Request> subrequests;
    private String description;
    private RequestPriority priority;
    private double estimatedWorkDays;
    private RequestStatus status;
    private String reporter;
    private String assignee;
    private RequestType type;
    private Request parentRequest;

    public Request(
            String id,
            List<Request> subrequests,
            String description,
            RequestPriority priority,
            double estimatedDurationDays,
            RequestStatus status,
            String reporter,
            String assignee,
            RequestType type) {
        requestId = id;
        setSubrequests(subrequests);
        this.description = description;
        this.priority = priority;
        estimatedWorkDays = estimatedDurationDays;
        this.status = status;
        this.reporter = reporter;
        this.assignee = assignee;
        this.type = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<Request> getSubrequests() {
        return subrequests;
    }

    public Request getParentRequest() {
        return parentRequest;
    }

    public void setParentRequest(Request parentRequest) {
        this.parentRequest = parentRequest;
    }

    public void setSubrequests(List<Request> subrequests) {
        if (this.subrequests != null)
            for (int i = 0; i < this.subrequests.size(); i++) {
                Request request = subrequests.get(i);
                request.setParentRequest(null);
            }
        this.subrequests = subrequests;
        if (this.subrequests != null)
            for (int i = 0; i < this.subrequests.size(); i++) {
                Request request = subrequests.get(i);
                request.setParentRequest(this);
            }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestPriority getPriority() {
        return priority;
    }

    public void setPriority(RequestPriority priority) {
        this.priority = priority;
    }

    public double getEstimatedWorkDays() {
        if (areThereSubrequests())
            return estimatedWorkDays;
        double totalWork = 0.0;
        for (Request request : subrequests) {
            totalWork += request.getEstimatedWorkDays();
        }
        return totalWork;
    }

    private boolean areThereSubrequests() {
        return subrequests == null || subrequests.size() == 0;
    }

    public void setEstimatedWorkDays(double estimatedWorkDays) {
        this.estimatedWorkDays = estimatedWorkDays;
    }

    public RequestStatus getStatus() {
        if (areThereSubrequests())
            return status;
        boolean allPending = true;
        boolean allCompleted = true;
        for (Request request : subrequests) {
            RequestStatus status = request.getStatus();
            if (status != RequestStatus.PENDING)
                allPending = false;
            if (status != RequestStatus.COMPLETED)
                allCompleted = false;
        }
        if (allPending)
            return RequestStatus.PENDING;
        if (allCompleted)
            return RequestStatus.COMPLETED;
        else
            return RequestStatus.IN_PROGRESS;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Request request = (Request) o;

        if (requestId != null ? !requestId.equals(request.requestId) : request.requestId != null) return false;

        return true;
    }

    public int hashCode() {
        return (requestId != null ? requestId.hashCode() : 0);
    }
}

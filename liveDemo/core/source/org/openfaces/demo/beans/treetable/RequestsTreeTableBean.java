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

import org.openfaces.component.table.AllNodesExpanded;
import org.openfaces.component.table.ExpansionState;
import org.openfaces.util.FacesUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RequestsTreeTableBean implements Serializable {

    private static final String ID_PREFIX = "NO-";
    private ExpansionState requestsTreeTableExpansionState = new AllNodesExpanded();
    private int requestsCreated;
    private List<Request> rootRequests = new ArrayList<Request>();

    private List<Request> selectedRequests = new ArrayList<Request>();
    private List<String> requestsColumnsOrder = new ArrayList<String>();
    private List<SelectItem> columnItems = new ArrayList<SelectItem>();
    private String[] selectedColumns;

    private List<SelectItem> requestTypeItems = new ArrayList<SelectItem>();
    private List<SelectItem> requestPriorityItems = new ArrayList<SelectItem>();
    private List<SelectItem> requestStatusItems = new ArrayList<SelectItem>();
    private Comparator idComparator = new IdComparator();

    private Converter requestTypeConverter = new RequestTypeConverter();
    private Converter requestStatusConverter = new RequestStatusConverter();
    private Converter requestPriorityConverter = new RequestPriorityConverter();
    private Request currentlyEditedRequest;

    public RequestsTreeTableBean() {
        requestTypeItems = new ArrayList<SelectItem>();
        requestTypeItems.add(new SelectItem(RequestType.TASK, RequestType.TASK.toString(), null));
        requestTypeItems.add(new SelectItem(RequestType.FEATURE, RequestType.FEATURE.toString(), null));
        requestTypeItems.add(new SelectItem(RequestType.BUG, RequestType.BUG.toString(), null));

        requestPriorityItems = new ArrayList<SelectItem>();
        requestPriorityItems.add(new SelectItem(RequestPriority.MAJOR, RequestPriority.MAJOR.toString(), null));
        requestPriorityItems.add(new SelectItem(RequestPriority.NORMAL, RequestPriority.NORMAL.toString(), null));
        requestPriorityItems.add(new SelectItem(RequestPriority.MINOR, RequestPriority.MINOR.toString(), null));

        requestStatusItems = new ArrayList<SelectItem>();
        requestStatusItems.add(new SelectItem(RequestStatus.PENDING, RequestStatus.PENDING.toString(), null));
        requestStatusItems.add(new SelectItem(RequestStatus.IN_PROGRESS, RequestStatus.IN_PROGRESS.toString(), null));
        requestStatusItems.add(new SelectItem(RequestStatus.COMPLETED, RequestStatus.COMPLETED.toString(), null));

        columnItems.add(new SelectItem("id", "Request ID", null));
        columnItems.add(new SelectItem("description", "Description", null, true));
        columnItems.add(new SelectItem("type", "Request Type", null));
        columnItems.add(new SelectItem("work", "Estimated Work (days)", null));
        columnItems.add(new SelectItem("priority", "Priority", null));
        columnItems.add(new SelectItem("status", "Status", null));
        columnItems.add(new SelectItem("reporter", "Reporter", null));
        columnItems.add(new SelectItem("assignee", "Assignee", null));

        selectedColumns = new String[]{"id", "description", "work", "priority", "status", "assignee"};
        selectColumns(null);

        rootRequests.add(createRequest(null, "Some task 1", RequestPriority.NORMAL, 2, RequestStatus.COMPLETED, "User 1", "Worker 1", RequestType.TASK));
        rootRequests.add(createRequest(null, "Some task 2", RequestPriority.NORMAL, 3, RequestStatus.COMPLETED, "User 2", "Worker 1", RequestType.TASK));
        rootRequests.add(createRequest(null, "Some task 3", RequestPriority.MAJOR, 2.5, RequestStatus.COMPLETED, "User 3", "Worker 1", RequestType.TASK));
        List<Request> bigTask = new ArrayList<Request>();
        bigTask.add(createRequest(null, "Feature 1", RequestPriority.NORMAL, 1, RequestStatus.COMPLETED, "User 1", "Worker 1", RequestType.FEATURE));
        bigTask.add(createRequest(null, "Feature 2", RequestPriority.NORMAL, 1, RequestStatus.PENDING, "User ", "Worker 1", RequestType.FEATURE));
        List<Request> complexTaskRequests = new ArrayList<Request>();
        complexTaskRequests.add(createRequest(null, "Sub-task 1", RequestPriority.NORMAL, 1, RequestStatus.IN_PROGRESS, "User 1", "Worker 2", RequestType.TASK));
        complexTaskRequests.add(createRequest(null, "Sub-task 2", RequestPriority.NORMAL, 1.5, RequestStatus.PENDING, "User 1", "Worker 2", RequestType.TASK));
        bigTask.add(createRequest(complexTaskRequests, "Complex task", null, 0, RequestStatus.COMPLETED, "User 1", null, null));
        rootRequests.add(createRequest(bigTask, "A serious undertaking", null, 0, RequestStatus.COMPLETED, "User 1", null, null));

        List<Request> subproblems = new ArrayList<Request>();
        subproblems.add(createRequest(null, "Sub-problem 1", RequestPriority.NORMAL, 0.5, RequestStatus.PENDING, "User 3", "Worker 1", RequestType.BUG));
        subproblems.add(createRequest(null, "Sub-problem 2", RequestPriority.MAJOR, 0.5, RequestStatus.COMPLETED, "User 3", "Worker 2", RequestType.BUG));
        rootRequests.add(createRequest(subproblems, "Solve problem 1", null, 0, null, "User 3", null, null));
        rootRequests.add(createRequest(null, "Solve problem 2", RequestPriority.MINOR, 1, RequestStatus.PENDING, "User 2", "Worker 3", RequestType.BUG));

    }

    private Request createRequest(
            List<Request> subrequests,
            String description,
            RequestPriority priority,
            double estimatedDurationDays,
            RequestStatus status,
            String reporter,
            String assignee,
            RequestType type) {
        return new Request(ID_PREFIX + ++requestsCreated,
                subrequests,
                description,
                priority,
                estimatedDurationDays,
                status,
                reporter,
                assignee,
                type);
    }


    public ExpansionState getRequestsTreeTableExpansionState() {
        return requestsTreeTableExpansionState;
    }

    public void setRequestsTreeTableExpansionState(ExpansionState requestsTreeTableExpansionState) {
        this.requestsTreeTableExpansionState = requestsTreeTableExpansionState;
    }

    public List<Request> getRequestsNodeChildren() {
        Request request = fetchRequestVariable();
        return request != null ? request.getSubrequests() : rootRequests;
    }

    public List<Request> getSelectedRequests() {
        return selectedRequests;
    }

    public void setSelectedRequests(List<Request> selectedRequests) {
        this.selectedRequests = selectedRequests;
    }

    public List<String> getRequestsColumnsOrder() {
        return requestsColumnsOrder;
    }

    public String[] getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(String[] selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public void selectColumns(ActionEvent event) {
        requestsColumnsOrder = Arrays.asList(selectedColumns);
    }

    public List<SelectItem> getColumnItems() {
        return columnItems;
    }

    public boolean isEditingThisRow() {
        Request request = fetchRequestVariable();
        return request.equals(currentlyEditedRequest);
    }

    public boolean isEditingLeafRequest() {
        if (!isEditingThisRow())
            return false;
        Request request = fetchRequestVariable();
        List<Request> subrequests = request.getSubrequests();
        boolean thereAreSubrequests = subrequests != null && subrequests.size() > 0;
        return !thereAreSubrequests;
    }

    public void editRequest(ActionEvent event) {
        currentlyEditedRequest = selectedRequests.get(0);
    }

    public void addSubrequest(ActionEvent event) {
        List<Request> requestList = getFirstSelectedRequest().getSubrequests();
        if (requestList == null) {
            requestList = new ArrayList<Request>();
            getFirstSelectedRequest().setSubrequests(requestList);
        }
        addNewRequest(requestList);
    }

    public void addRequest(ActionEvent event) {
        List<Request> requestList = selectedRequests != null && selectedRequests.size() > 0 && getFirstSelectedRequest().getParentRequest() != null
                ? getFirstSelectedRequest().getParentRequest().getSubrequests()
                : rootRequests;
        addNewRequest(requestList);
    }

    private Request getFirstSelectedRequest() {
        return selectedRequests.get(0);
    }

    private void addNewRequest(List<Request> requestList) {
        Request newRequest = createRequest(null, "", RequestPriority.NORMAL, 0, RequestStatus.PENDING, "", "", RequestType.TASK);
        requestList.add(newRequest);
        if (selectedRequests.size() > 0)
            selectedRequests.remove(selectedRequests.size() - 1);
        selectedRequests.add(newRequest);
        currentlyEditedRequest = newRequest;
    }

    public void deleteRequest(ActionEvent event) {
        Request siblingRequest = null;
        for (int i = 0, count = selectedRequests.size(); i < count; i++) {
            Request selectedRequest = selectedRequests.get(i);
            siblingRequest = removeRequest(rootRequests, selectedRequest);
        }

        if (siblingRequest != null) {
            selectedRequests = new ArrayList<Request>();
            selectedRequests.add(siblingRequest);
        } else {
            selectedRequests = null;
        }
    }

    private Request removeRequest(List<Request> requests, Request request) {
        int requestIndex = requests.indexOf(request);
        if (requestIndex != -1) {
            requests.remove(request);
            Request siblingRequest;
            int requestsRemaining = requests.size();
            if (requestsRemaining > 0) {
                if (requestIndex >= requestsRemaining)
                    requestIndex = requestsRemaining - 1;
                siblingRequest = requests.get(requestIndex);
            } else {
                siblingRequest = request.getParentRequest();
            }
            request.setParentRequest(null);
            return siblingRequest;
        }
        for (Request currRequest : requests) {
            List<Request> subrequests = currRequest.getSubrequests();
            if (subrequests == null)
                continue;
            Request siblingOfARemovedRequest = removeRequest(subrequests, request);
            if (siblingOfARemovedRequest != null)
                return siblingOfARemovedRequest;
        }
        return null;
    }

    public List<SelectItem> getRequestTypeItems() {
        return requestTypeItems;
    }

    public List<SelectItem> getRequestPriorityItems() {
        return requestPriorityItems;
    }

    public void setRequestPriorityItems(List<SelectItem> requestPriorityItems) {
        this.requestPriorityItems = requestPriorityItems;
    }

    public List<SelectItem> getRequestStatusItems() {
        return requestStatusItems;
    }

    public void setRequestStatusItems(List<SelectItem> requestStatusItems) {
        this.requestStatusItems = requestStatusItems;
    }


    public Converter getRequestTypeConverter() {
        return requestTypeConverter;
    }

    public Converter getRequestStatusConverter() {
        return requestStatusConverter;
    }

    public Converter getRequestPriorityConverter() {
        return requestPriorityConverter;
    }

    public Comparator getIdComparator() {
        return idComparator;
    }

    public boolean isEditingRequest() {
        return currentlyEditedRequest != null;
    }

    public void saveChanges(ActionEvent event) {
        currentlyEditedRequest = null;
    }

    public String getRequestTypeImageUrl() {
        Request request = fetchRequestVariable();
        RequestType type = request.getType();
        if (RequestType.TASK.equals(type))
            return "../images/treetable/type_task.gif";
        if (RequestType.FEATURE.equals(type))
            return "../images/treetable/type_feature.gif";
        if (RequestType.BUG.equals(type))
            return "../images/treetable/type_bug.gif";
        return "../images/treetable/empty_request_icon.gif";
    }

    public String getRequestStatusImageUrl() {
        Request request = fetchRequestVariable();
        RequestStatus status = request.getStatus();
        if (RequestStatus.PENDING.equals(status))
            return "../images/treetable/status_pending.gif";
        if (RequestStatus.IN_PROGRESS.equals(status))
            return "../images/treetable/status_in_progress.gif";
        if (RequestStatus.COMPLETED.equals(status))
            return "../images/treetable/status_complete.gif";
        return "../images/treetable/empty_request_icon.gif";
    }

    public String getRequestPriorityImageUrl() {
        Request request = fetchRequestVariable();
        RequestPriority priority = request.getPriority();
        if (RequestPriority.MAJOR.equals(priority))
            return "../images/treetable/priority_major.gif";
        if (RequestPriority.NORMAL.equals(priority))
            return "../images/treetable/priority_normal.gif";
        if (RequestPriority.MINOR.equals(priority))
            return "../images/treetable/priority_minor.gif";
        return "../images/treetable/empty_request_icon.gif";
    }

    private Request fetchRequestVariable() {
        return (Request) FacesUtil.getRequestMapValue("rq");
    }

    public boolean isRequestEditingButtonsEnabled() {
        return selectedRequests != null && !(selectedRequests.size() > 0);
    }

    public void setRequestEditingButtonsEnabled(boolean requestEditingButtonsEnabled) {
    }

    private static class RequestPriorityConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return RequestPriority.fromString(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            return value.toString();
        }
    }

    private static class RequestStatusConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return RequestStatus.fromString(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            return value.toString();
        }
    }

    private static class RequestTypeConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return RequestType.fromString(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            return value.toString();
        }
    }

    private static class IdComparator implements Comparator, Serializable {
        public int compare(Object o1, Object o2) {
            String s1 = (String) o1;
            String s2 = (String) o2;
            s1 = s1.substring(ID_PREFIX.length());
            s2 = s2.substring(ID_PREFIX.length());
            Integer no1 = new Integer(s1);
            Integer no2 = new Integer(s2);
            return no1.compareTo(no2);
        }
    }
}

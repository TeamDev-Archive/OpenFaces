package org.openfaces.richfaces.demo.beans;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {

    private String name;
    private Date startDate;
    private Date endDate;
    private String description;

    public Task(String taskName, Date startDate, Date endDate, String description) {
        this.name = taskName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!description.equals(task.description)) return false;
        if (!endDate.equals(task.endDate)) return false;
        if (!name.equals(task.name)) return false;
        if (!startDate.equals(task.startDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}

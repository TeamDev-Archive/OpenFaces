/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.datatable;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class EmailTableDemoBean {
    private List<MailMessage> messages = new ArrayList<MailMessage>();

    private MailMessage selectedMessage;
    private List<String> columnsOrder = new ArrayList<String>();
    private List<SelectItem> columnItems = new ArrayList<SelectItem>();
    private Object[] selectedColumnItems = new Object[]{};
    private String sortColumnId = "date";
    private boolean sortAscending = true;

    public EmailTableDemoBean() {
        columnItems.add(new SelectItem("importance", "importance", "Importance column"));
        columnItems.add(new SelectItem("address", "address", "Address column"));
        columnItems.add(new SelectItem("subject", "subject", "Subject column"));
        columnItems.add(new SelectItem("date", "date", "Date column"));
        selectedColumnItems = new String[]{"importance", "address", "subject", "date"};
        selectColumns();

        messages.add(new MailMessage("1", true, "Workstations: the build or buy ritual", "Javalobby News", "Semen Semenych", createDate(2005, 10, 2, 14, 2), false));
        messages.add(new MailMessage("2", false, "IBM Touts Expanded SOA Strategy", "XML Industry Newsletter", "XML Journal", createDate(2005, 8, 14, 8, 0), false));
        messages.add(new MailMessage("3", true, "JSFC build.69 Build Successful", "JSFC-Builder@teamdev.com", "Components Project", createDate(2005, 10, 8, 15, 49), true));
        messages.add(new MailMessage("4", false, "Where are the skilled employees? Free report", "brainbench@consumer.brainbench.com", "me", createDate(2005, 6, 16, 9, 15), true));
        messages.add(new MailMessage("5", false, "FW: Make big money from nothing", "spam12345@yahoo.com", "EveryoneOnTheWeb", createDate(2005, 10, 1, 2, 3), true));
    }

    public static Date createDate(int year, int month, int day, int hour, int minute) {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, year);
        instance.set(Calendar.MONTH, month);
        instance.set(Calendar.DAY_OF_MONTH, day);
        instance.set(Calendar.HOUR_OF_DAY, hour);
        instance.set(Calendar.MINUTE, minute);
        return instance.getTime();
    }

    public List<MailMessage> getMessages() {
        return messages;
    }

    public MailMessage getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(MailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public String markAsRead() {
        if (selectedMessage != null)
            selectedMessage.setRead(true);
        return null;
    }

    public String markAsUnread() {
        if (selectedMessage != null)
            selectedMessage.setRead(false);
        return null;
    }

    public List<String> getColumnsOrder() {
        return columnsOrder;
    }

    public void setColumnsOrder(List<String> columnsOrder) {
        this.columnsOrder = columnsOrder;
    }

    public List<SelectItem> getColumnItems() {
        return columnItems;
    }

    public Object[] getSelectedColumnItems() {
        int columnCount = columnsOrder.size();
        selectedColumnItems = new String[columnCount];

        for (int i = 0; i < columnCount; i++) {
            String columnId = columnsOrder.get(i);
            selectedColumnItems[i] = getColumnItem(columnId).getValue();

        }
        return selectedColumnItems;
    }

    private SelectItem getColumnItem(String columnId) {
        for (Object myColumnItem : columnItems) {
            SelectItem selectItem = (SelectItem) myColumnItem;
            if (selectItem.getValue().equals(columnId))
                return selectItem;
        }
        throw new IllegalArgumentException("Couldn't find SelectItem for column: " + columnId);
    }

    public void setSelectedColumnItems(Object[] selectedColumns) {
        selectedColumnItems = selectedColumns;
    }

    public void selectColumns() {
        columnsOrder = new ArrayList<String>();
        for (Object mySelectedColumnItem : selectedColumnItems) {
            String item = (String) mySelectedColumnItem;
            columnsOrder.add(item);
        }
    }

    public String getSortColumnId() {
        return sortColumnId;
    }

    public void setSortColumnId(String sortColumnId) {
        this.sortColumnId = sortColumnId;
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }
}

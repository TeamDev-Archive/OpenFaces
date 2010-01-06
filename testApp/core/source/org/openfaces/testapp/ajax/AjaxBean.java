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
package org.openfaces.testapp.ajax;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author Ilya Musihin
 */
public class AjaxBean {

    private String outputText;

    private String outputText2;

    private String inputText;

    private Date calendar;

    private List<Comment> comments;

    private List<SelectItem> tlsItems = new ArrayList<SelectItem>();

    public List<SelectItem> getItems() {
        List<SelectItem> tempList = new ArrayList<SelectItem>();
        Random rand = new Random();
        for (int i = 0; i < 7; i++) {
            String currentItem = "TLS item #" + String.valueOf(rand.nextInt(37000));
            SelectItem item = new SelectItem(currentItem, currentItem);
            tempList.add(item);
        }
        tlsItems = tempList;
        return tlsItems;
    }

    public void setItems(List<SelectItem> TLSItems) {
        tlsItems = TLSItems;
    }

    public AjaxBean() {
        outputText = "hello world!";
        outputText2 = "hello world!";
        calendar = new Date();
    }

    public String getOutputText() {
        return outputText + new Random().nextInt(100);
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public String getOutputText2() {
        return outputText2 + new Random().nextInt(100);
    }

    public void setOutputText2(String outputText2) {
        this.outputText2 = outputText2;
    }

    public String someAction() {
        outputText2 = "call action";
        return null;
    }

    public void processAction(ActionEvent ae) {
        outputText = ae.getPhaseId().toString();
    }

    public String getDate() {
        return DateFormat.getInstance().format(calendar);
    }

    public Date getCalendar() {
        return calendar;
    }

    public void setCalendar(Date calendar) {
        this.calendar = calendar;
    }

    public Date getFromDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 0);
        return c.getTime();
    }

    public Date getToDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 5);
        return c.getTime();
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public void removeItemFromTicket(ActionEvent event) {
        String ticketItemIdParamName = "ticketItemId";
        String ticketItemId = (String) getEventParameter(event,
                ticketItemIdParamName);
        if (ticketItemId == null) {
            throw new IllegalStateException("Parameter not found: "
                    + ticketItemIdParamName);
        }
        int savedI = 0;
        for (int i = 0; i < comments.size(); i++) {
            if ((comments.get(i)).getContent().equals(ticketItemId)) {
                savedI = i;
                break;
            }
        }
        getComments().remove(savedI);
    }

    protected static Object getEventParameter(ActionEvent event,
                                              String paramName) {

        Iterator<UIComponent> iterator = event.getComponent().getParent()
                .getFacetsAndChildren();

        while (iterator.hasNext()) {
            UIComponent component = iterator.next();
            if (component instanceof UIParameter) {
                UIParameter uiParameter = (UIParameter) component;
                if (paramName.equals(uiParameter.getName())) {
                    return uiParameter.getValue();
                }
            }
        }
        return null;
    }

    public List<Comment> getComments() {
        if (comments == null) {
            comments = new ArrayList<Comment>();
            comments.add(new Comment("aaa"));
            comments.add(new Comment("bbb"));
            comments.add(new Comment("ccc"));
            comments.add(new Comment("ddd"));
        }
        return comments;
    }

    public static final class Comment {

        private String content;

        public Comment(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCaption() {
            return content.toUpperCase();
        }

        public String toString() {
            return content;
        }
    }
}

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
package org.openfaces.richfaces.demo.beans;

 import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * @author Darya Shumilina
 */
public class EMailsTreeTableBean {

    private static final String TODAY = "Today";
    private static final String YESTERDAY = "Yesterday";
    private static final String MONDAY = "Monday";
    private static final String TUESDAY = "Tuesday";
    private static final String WEDNESDAY = "Wednesday";
    private static final String THURSDAY = "Thursday";
    private static final String FRIDAY = "Friday";
    private static final String LAST_WEEK = "Last Week";
    private static final String OLDER = "Older";
    private static final String FROM = "From: ";
    private static final String WITH_ATTACHMENTS = "Attachments: With Attachments";
    private static final String WITHOUT_ATTACHMENTS = "Attachments: Without Attachments";

    private static final String[] SUBJECTS = new String[] {
            "Proper Java XML parser",
            "In-place rows editing implementation",
            "'unexpected Syntax error'",
            "Problem in IE for div tag",
            "Hide menu by double-clicking",
            "Is it necessarily to implement DOCTYPE?",
            "Easiest way to get actual element size",
            "Branching the code",
            "UI improvement propositions",
            "Build artifacts placement",
            "Login to the issue tracker failed",
            "Designing architecture for this module",
            "Build successful (tests passed: 235)",
            "Birthday party invitation",
            "Web 2.0 notices",
            "Your account details",
            "Environment for the new project",
            "My travelling photos",
            "Join my network on LinkedIn",
            "Look at the NPE",
            "Next iteration plan",
            "Some development agreements",
            "Let me know about your vacation planning date",
            "Revert the last changes please",
            "Approximate estimate",
            "Code review remarks",
            "Upload speed optimization",
            "Scripts performance measurement results",
            "Translation request: some strings aren't localized",
            "Generated *.exe file size decreased",
            "FW: me and John Smith correspondence",
            "Recently implemented functionality is not working",
            "Specific characters are broken after Ajax re-rendering",
            "Customer registration procedure",
            "Specification supplement",
            "Thanks for the help"
    };
    private static final String[] PATHS = new String[] {
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Entertainment",
        "Dev",
        "Inbox",
        "Dev",
        "Entertainment",
        "Inbox",
        "Dev",
        "Dev",
        "Dev",
        "Inbox",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Dev",
        "Inbox"
    };
    private static final String[] SENDER_NAMES = new String[]{
            "Aiden Bonar",
            "Frederic Moriarty",
            "Vincent Askew",
            "Ike Wickliff",
            "Michael Allendorf",
            "Larry McGowan",
            "Clayton Vasser",
            "Robert Binney",
            "Adam Buchanan",
            "Hannes Telford",
            "James Cormac",
            "Andrew Bromley",
            "George Manners",
            "Dean Boynton",
            "Walter Boyd",
            "Albert Parshall",
            "William Budd",
            "Gary Agrippa",
            "Chris Dixie",
            "Charles Driscol",
            "Margaret Lawley",
            "Melissa Stover",
            "Clare Jones",
            "Chloe Angus",
            "Jane Crayford",
            "Joann Logan",
            "Jill Catherwood",
            "Jocelyn Conway",
            "Linus Stapleton",
            "Laurence Barton",
            "George Van Slyck",
            "Alex Smith",
            "Luis Vibbard",
            "Gabriel Zane",
            "Christine Cesio"};

    private static final Map<String, EMailImportance> IMPORTANCES_BY_NAMES = new HashMap<String, EMailImportance>();
    
    private static final String DEFAULT_MESSAGE_TEXT = "Message body content.";

    static {
        IMPORTANCES_BY_NAMES.put(EMailImportance.LOW.toString(), EMailImportance.LOW);
        IMPORTANCES_BY_NAMES.put(EMailImportance.NORMAL.toString(), EMailImportance.NORMAL);
        IMPORTANCES_BY_NAMES.put(EMailImportance.HIGH.toString(), EMailImportance.HIGH);
    }

    private List<EMail> eMails = new ArrayList<EMail>();
    private List<DateRange> dateRanges = new ArrayList<DateRange>();
    private String sortedColumnId = "date";
    private boolean sortAscending = false;
    private Map<String, List<EMail>> dataModel;
    private ReceivedDateConverter receivedDateConverter = new ReceivedDateConverter();
    private EMail selectedEMail;

    public EMailsTreeTableBean(String path) {
        int currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        dateRanges.add(new DateRange(TODAY, 0));
        if (currentDayOfWeek != Calendar.MONDAY)
            dateRanges.add(new DateRange(YESTERDAY, 1));

        if (Calendar.WEDNESDAY == currentDayOfWeek) {
            dateRanges.add(new DateRange(MONDAY, 2));
        } else if (Calendar.THURSDAY == currentDayOfWeek) {
            dateRanges.add(new DateRange(TUESDAY, 2));
            dateRanges.add(new DateRange(MONDAY, 3));
        } else if (Calendar.FRIDAY == currentDayOfWeek) {
            dateRanges.add(new DateRange(WEDNESDAY, 2));
            dateRanges.add(new DateRange(TUESDAY, 3));
            dateRanges.add(new DateRange(MONDAY, 4));
        } else if (Calendar.SATURDAY == currentDayOfWeek) {
            dateRanges.add(new DateRange(THURSDAY, 2));
            dateRanges.add(new DateRange(WEDNESDAY, 3));
            dateRanges.add(new DateRange(TUESDAY, 4));
            dateRanges.add(new DateRange(MONDAY, 5));
        } else if (Calendar.SUNDAY == currentDayOfWeek) {
            dateRanges.add(new DateRange(FRIDAY, 2));
            dateRanges.add(new DateRange(THURSDAY, 3));
            dateRanges.add(new DateRange(WEDNESDAY, 4));
            dateRanges.add(new DateRange(TUESDAY, 5));
            dateRanges.add(new DateRange(MONDAY, 6));
        }
        dateRanges.addAll(generateLastWeekOlderDateRanges(currentDayOfWeek));
        
        eMails = generateEmails(path);
        initDataModel();
    }
    
    private void initDataModel()
    {
        if (this.sortedColumnId.equals("date")) {
            dataModel = groupByDate(eMails);
        } else if (this.sortedColumnId.equals("subject")) {
            dataModel = groupBySubject(eMails);
        } else if (this.sortedColumnId.equals("sender")) {
            dataModel = groupBySender(eMails);
        } else if (this.sortedColumnId.equals("importance")) {
            dataModel = groupByImportance(eMails);
        } else if (this.sortedColumnId.equals("attachment")) {
            dataModel = groupByAttachment(eMails);
        }
    }

    public List getEMailsTreeChildren() {
        Object email = getEmailVar();
        if (email == null) {
        	List<String> list = new ArrayList<String>();
        	list.addAll(dataModel.keySet());
            return list;
        } else {
            return dataModel.get(email);
        }
    }

    private List<EMail> generateEmails(String path) {
        Random rand = new Random();
        List<EMail> result = new ArrayList<EMail>();

        //generate initial messages
        for (int i = 0, count = Math.max(SUBJECTS.length, SENDER_NAMES.length); i < count; i++) {
            DateRange dateRange = dateRanges.get(rand.nextInt(dateRanges.size()));

            Date generatedDate = generateDate(dateRange.getCategory(), dateRange.getStartDate(), dateRange.getEndDate(), false, new Date(0), rand);
            String sender = SENDER_NAMES[i % SENDER_NAMES.length];
            String subject = SUBJECTS[i % SUBJECTS.length];
            if (PATHS[i % SENDER_NAMES.length].equals(path)) {
            	EMail email = new EMail(sender, subject, generatedDate, EMailImportance.NORMAL, false, path, DEFAULT_MESSAGE_TEXT);
            	result.add(email);
            }
        }
        
        if (result.isEmpty())
        	return result;

        int replyMessagesQuantity = (rand.nextInt(20) + 40) * result.size() / Math.max(SUBJECTS.length, SENDER_NAMES.length);

        //generate 'Re: ' messages
        for (int i = 0; i < replyMessagesQuantity; i++) {
            EMail email = result.get(rand.nextInt(result.size()));
            EMail newEMail;

            Date receivedDate = email.getReceivedDate();
            String sender = SENDER_NAMES[rand.nextInt(SENDER_NAMES.length - 1)];
            while (sender.equals(email.getSender())) {
                int senderIndex = sender.indexOf(sender);
                if (senderIndex == 0) {
                    sender = SENDER_NAMES[rand.nextInt(SENDER_NAMES.length - 1) + 1];
                } else if (senderIndex == SENDER_NAMES.length - 1) {
                    sender = SENDER_NAMES[SENDER_NAMES.length - rand.nextInt(20)];
                } else {
                    sender = SENDER_NAMES[rand.nextInt(SENDER_NAMES.length - 1)];
                }
            }
            String subject = email.getSubject();
            if (!subject.startsWith("Re: ")) {
                subject = "Re: " + email.getSubject();
            }

            String dateRangeName = getDateRangeName(receivedDate);
            DateRange originalEmailDateRange = null;
            for (DateRange dateRange : dateRanges) {
                if (dateRange.getCategory().equals(dateRangeName)) {
                    originalEmailDateRange = dateRange;
                    break;
                }
            }
            Date reDate = generateDate(dateRangeName, originalEmailDateRange.getStartDate(), originalEmailDateRange.getEndDate(), true, receivedDate, rand);

            newEMail = new EMail(sender, subject, reDate, EMailImportance.NORMAL, false, email.getPath(), DEFAULT_MESSAGE_TEXT);
            result.add(newEMail);
        }

        List<Integer> highImportance = new ArrayList<Integer>();
        List<Integer> normalImportance = new ArrayList<Integer>();
        List<Integer> lowImportance = new ArrayList<Integer>();
        List<Integer> allImportances = new ArrayList<Integer>();
        Map<EMailImportance, List<Integer>> allImportancesMap = new HashMap<EMailImportance, List<Integer>>();

        int highImportanceQuantity = (int) (result.size() * 0.23);
        int normalImportanceQuantity = (int) (result.size() * 0.62);
        int lowImportanceQuantity = (int) (result.size() * 0.15);

        //generate importance
        for (int i = 0; i < result.size(); i++) {
            allImportances.add(i);
        }

        for (int i = 0; i < highImportanceQuantity; i++) {
            int allIndex = rand.nextInt(allImportances.size() - 1);
            Integer index = allImportances.get(allIndex);
            allImportances.remove(allIndex);
            highImportance.add(index);
        }
        for (int i = 0; i < normalImportanceQuantity; i++) {
            int allIndex = rand.nextInt(allImportances.size() - 1);
            Integer index = allImportances.get(allIndex);
            allImportances.remove(allIndex);
            normalImportance.add(index);
        }
        for (int i = 0; i < lowImportanceQuantity; i++) {
            int allIndex = rand.nextInt(allImportances.size() - 1);
            Integer index = allImportances.get(allIndex);
            allImportances.remove(allIndex);
            lowImportance.add(index);
        }

        allImportances.addAll(highImportance);
        allImportances.addAll(normalImportance);
        allImportances.addAll(lowImportance);

        allImportancesMap.put(EMailImportance.HIGH, highImportance);
        allImportancesMap.put(EMailImportance.NORMAL, normalImportance);
        allImportancesMap.put(EMailImportance.LOW, lowImportance);

        List<EMail> withImportancies = new ArrayList<EMail>();
        for (int i = 0; i < result.size(); i++) {
            EMail email = result.get(i);
            EMailImportance importance = getImportance(allImportancesMap, i);
            withImportancies.add(new EMail(email.getSender(), email.getSubject(), email.getReceivedDate(), importance, false, email.getPath(), DEFAULT_MESSAGE_TEXT));
        }
        result.clear();
        result.addAll(withImportancies);

        //generate attachments
        List<Integer> withAttachmentsIndexes = new ArrayList<Integer>();
        List<Integer> withoutAttachmentsIndexes = new ArrayList<Integer>();

        int withAttachmentsQuantity = (int) (result.size() * 0.2);
        int withoutAttachmentsQuantity = (int) (result.size() * 0.8);

        for (int i = 0; i < result.size(); i++) {
            allImportances.add(i);
        }

        for (int i = 0; i < withAttachmentsQuantity; i++) {
            int allIndex = rand.nextInt(allImportances.size() - 1);
            Integer index = allImportances.get(allIndex);
            allImportances.remove(allIndex);
            withAttachmentsIndexes.add(index);
        }
        for (int i = 0; i < withoutAttachmentsQuantity; i++) {
            int allIndex = rand.nextInt(allImportances.size() - 1);
            Integer index = allImportances.get(allIndex);
            allImportances.remove(allIndex);
            withoutAttachmentsIndexes.add(index);
        }
        Map<String, List<Integer>> allAttachmentsMap = new HashMap<String, List<Integer>>();
        allAttachmentsMap.put("with", withAttachmentsIndexes);
        allAttachmentsMap.put("without", withoutAttachmentsIndexes);

        List<EMail> withAttachments = new ArrayList<EMail>();
        for (int i = 0; i < result.size(); i++) {
            EMail email = result.get(i);
            boolean attachment = getAttachment(allAttachmentsMap, i);
            withAttachments.add(new EMail(email.getSender(), email.getSubject(), email.getReceivedDate(), email.getImportance(), attachment, email.getPath(), DEFAULT_MESSAGE_TEXT));
        }
        result.clear();
        result.addAll(withAttachments);

        return result;
    }

    private boolean getAttachment(Map<String, List<Integer>> allAttachmentsMap, Integer integer) {
        List<Integer> withAttachment = allAttachmentsMap.get("with");
        return withAttachment.contains(integer);
    }

    private EMailImportance getImportance(Map<EMailImportance, List<Integer>> allImportanciesMap, Integer index) {
        List<Integer> high = allImportanciesMap.get(EMailImportance.HIGH);
        List<Integer> low = allImportanciesMap.get(EMailImportance.LOW);
        if (high.contains(index))
            return EMailImportance.HIGH;
        else if (low.contains(index))
            return EMailImportance.LOW;
        else
            return EMailImportance.NORMAL;
    }


    private String getDateRangeName(Date date) {
        String result = null;
        int i = 0;
        while (i < dateRanges.size()) {
            DateRange dateRange = dateRanges.get(i);
            if ((date.getTime() >= dateRange.getEndDate().getTime()) && (date.getTime() <= dateRange.getStartDate().getTime())) {
                result = dateRange.getCategory();
                break;
            }
            i++;
        }
        return result;
    }

    private static Date generateDate(String dateRangeName,
                                     Date startDate, Date endDate, boolean isReply,
                                     Date receivedDate, Random rand) {
        Date result = null;
        long startDateTime = startDate.getTime();
        int forGenerating;
        if (!dateRangeName.equals(OLDER)) {
            if (!isReply) {
                forGenerating = (int) (startDateTime - endDate.getTime());
                result = new Date(startDateTime - rand.nextInt(Math.abs(forGenerating)));
            } else if (isReply) {
                forGenerating = (int) (startDateTime - receivedDate.getTime());
                result = new Date(receivedDate.getTime() + rand.nextInt(Math.abs(forGenerating)));
            }
        } else {
            if (!isReply) {
                BigInteger bigInteger = BigInteger.probablePrime(31, rand);
                result = new Date(startDateTime - bigInteger.longValue());
            } else if (isReply) {
                forGenerating = (int) (startDateTime - receivedDate.getTime());
                result = new Date(receivedDate.getTime() + rand.nextInt(Math.abs(forGenerating)));
            }
        }
        return result;
    }

    private Map<String, List<EMail>> groupByDate(List<EMail> emails) {
        Map<String, List<EMail>> grouped = new HashMap<String, List<EMail>>();

        for (DateRange range : dateRanges) {
            List<EMail> rangeArray = new ArrayList<EMail>();
            for (EMail email : emails) {
                Date receivedDate = email.getReceivedDate();
                if (receivedDate.getTime() >= range.getEndDate().getTime() && receivedDate.getTime() <= range.getStartDate().getTime()) {
                    rangeArray.add(email);
                }
            }
            grouped.put(range.getCategory(), rangeArray);
        }
        return grouped;
    }

    private Map<String, List<EMail>> groupBySubject(List<EMail> emails) {
        Map<String, List<EMail>> grouped = new HashMap<String, List<EMail>>();
        List<String> subjects = new ArrayList<String>();
        for (EMail email : emails) {
            String subject = email.getSubject();
            if (!subjects.contains(subject))
                subjects.add(subject);
        }

        for (String keySubject : subjects) {
            List<EMail> groupedSubjects = new ArrayList<EMail>();
            for (EMail email : emails) {
                String subject = email.getSubject();
                if (subject.equals(keySubject))
                    groupedSubjects.add(email);
            }
            grouped.put("Subject: " + keySubject, groupedSubjects);
        }
        return grouped;
    }

    private Map<String, List<EMail>> groupBySender(List<EMail> emails) {
        Map<String, List<EMail>> grouped = new HashMap<String, List<EMail>>();

        for (EMail email : emails) {
            String sender = email.getSender();
            List<EMail> groupedValues;
            if (!grouped.containsKey(FROM + sender)) {
                groupedValues = new ArrayList<EMail>();
                groupedValues.add(email);
                grouped.put(FROM + sender, groupedValues);
            } else {
                groupedValues = grouped.get(FROM + sender);
                groupedValues.add(email);
                grouped.put(FROM + sender, groupedValues);
            }
        }
        return grouped;
    }

    private Map<String, List<EMail>> groupByImportance(List<EMail> emails) {
        Map<EMailImportance, List<EMail>> groupedByImportance = new HashMap<EMailImportance, List<EMail>>();

        for (EMail email : emails) {
            EMailImportance importance = email.getImportance();
            if (groupedByImportance.containsKey(importance)) {
                List<EMail> value = groupedByImportance.get(importance);
                value.add(email);
            } else {
                List<EMail> value = new ArrayList<EMail>();
                value.add(email);
                groupedByImportance.put(importance, value);
            }
        }

        Map<String, List<EMail>> groupedByString = new HashMap<String, List<EMail>>();
        for (EMailImportance key : groupedByImportance.keySet()) {
            List<EMail> value = groupedByImportance.get(key);
            groupedByString.put("Importance: " + key + " (" + value.size() + " items)", value);
        }

        return groupedByString;
    }

    private Map<String, List<EMail>> groupByAttachment(List<EMail> emails) {
        Map<String, List<EMail>> grouped = new HashMap<String, List<EMail>>();

        List<EMail> withAttachmentsArray = new ArrayList<EMail>();
        List<EMail> withoutAttachmentsArray = new ArrayList<EMail>();

        for (EMail email : emails) {
            boolean attachment = email.getHasAttachment();
            if (!attachment)
                withoutAttachmentsArray.add(email);
            else
                withAttachmentsArray.add(email);
        }

        grouped.put(WITH_ATTACHMENTS + " (" + withAttachmentsArray.size() + " items)", withAttachmentsArray);
        grouped.put(WITHOUT_ATTACHMENTS + " (" + withoutAttachmentsArray.size() + " items)", withoutAttachmentsArray);

        return grouped;
    }

    private static List<DateRange> generateLastWeekOlderDateRanges(int currentDayOfWeek) {
        List<DateRange> dateRanges = new ArrayList<DateRange>();

        int startDateDifference = currentDayOfWeek - 1;
        int endDateDifference = currentDayOfWeek + 5;
        int olderStartDateDifference = currentDayOfWeek + 6;

        if (currentDayOfWeek == Calendar.SUNDAY) {
            startDateDifference = 7;
            endDateDifference = 13;
            olderStartDateDifference = 14;
        }

        dateRanges.add(new DateRange(LAST_WEEK, startDateDifference, endDateDifference));

        Date olderStartDate = DateRange.createStartDate(olderStartDateDifference);
        dateRanges.add(new DateRange(OLDER, olderStartDate, new Date(0)));

        return dateRanges;
    }

    public Converter getReceivedDateConverter() {
        return receivedDateConverter;
    }

    private Object getEmailVar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Map<String, Object> requestMap = externalContext.getRequestMap();
        return requestMap.get("email");
    }

    public Date getSortByDateExpression() {
        Object node = getEmailVar();
        if (node instanceof EMail) {
            EMail email = (EMail) node;
            return email.getReceivedDate();
        } else {
            for (DateRange dateRange : dateRanges) {
                String category = dateRange.getCategory();
                if (category.equals(node))
                    return dateRange.getStartDate();
            }
        }
        return null;
    }

    public String getSortBySubjectExpression() {
        Object node = getEmailVar();
        String subject;
        if (node instanceof EMail) {
            EMail email = (EMail) node;
            subject = email.getSubject();
        } else {
            String fullSubject = (String) node;
            subject = fullSubject.split("Subject: ")[1];
        }
        if (subject.startsWith("Re: "))
            return subject.split("Re: ")[1];
        else
            return subject;
    }

    public String getSortBySenderExpression() {
        Object node = getEmailVar();
        if (node instanceof EMail) {
            EMail email = (EMail) node;
            return email.getSender();
        } else {
            return (String) node;
        }
    }

    public EMailImportance getSortByImportance() {
        Object node = getEmailVar();
        if (node instanceof EMail) {
            EMail email = (EMail) node;
            return email.getImportance();
        }
        String category = (String) node;
        String importanceName = category.split(" ")[1];

        return IMPORTANCES_BY_NAMES.get(importanceName);
    }

    public boolean getSortByAttachmentExpression() {
        Object node = getEmailVar();
        if (node instanceof EMail) {
            EMail email = (EMail) node;
            return email.getHasAttachment();
        } else {
            String attachmentGroup = (String) node;
            String[] attachmentGroupArray = attachmentGroup.split(" ");
            String attachmentGroupName = attachmentGroupArray[0] + " " + attachmentGroupArray[1] + " " + attachmentGroupArray[2];
            return attachmentGroupName.equals(WITH_ATTACHMENTS);
        }
    }

    public String getImportanceIcon() {
        Object node = getEmailVar();
        if (!(node instanceof EMail))
            return null;

        EMail email = (EMail) node;
        EMailImportance importance = email.getImportance();
        if (importance.equals(EMailImportance.HIGH)) {
            return "/images/treetable/high_priority.gif";
        } else if (importance.equals(EMailImportance.NORMAL)) {
            return "/images/treetable/transparent.gif";
        } else {
            return "/images/treetable/low_priority.gif";
        }
    }

    public String getSortedColumnId() {
        return sortedColumnId;
    }

    public void setSortedColumnId(String sortedColumnId) {
        if (this.sortedColumnId.equals(sortedColumnId))
            return;
        this.sortedColumnId = sortedColumnId;
        initDataModel();
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    public EMail getSelectedEMail() {
        return selectedEMail;
    }
    
    public void setSelectedEMail(Object selectedEMail) {
        this.selectedEMail = selectedEMail instanceof EMail ? (EMail) selectedEMail : null;
    }

    private class ReceivedDateConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return null;
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
        	if (value == null)
        		return "None";

            Date receivedDate = (Date) value;
            String emailDateRange = getDateRangeName(receivedDate);
            Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

            if (emailDateRange.equals(TODAY))
                return DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(receivedDate);
            if (emailDateRange.equals(LAST_WEEK))
                return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale).format(receivedDate);
            if (emailDateRange.equals(OLDER))
                return DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(receivedDate);
            return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale).format(receivedDate);
        }
    }

    void addEMail(EMail eMail) {
    	eMails.add(eMail);
    	initDataModel();
    }
}
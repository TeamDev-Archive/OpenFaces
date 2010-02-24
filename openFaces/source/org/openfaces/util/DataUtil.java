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
package org.openfaces.util;

import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;
import javax.servlet.jsp.jstl.sql.Result;
import java.awt.*;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Dmitry Pikhulya
 */
public class DataUtil {
    private static final SimpleDateFormat JS_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat JS_DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final SimpleDateFormat JS_TIME_FORMAT = new SimpleDateFormat("HH:mm");

    private static final int[] DATE_CALENDAR_FIELDS = new int[]{java.util.Calendar.DAY_OF_MONTH, java.util.Calendar.MONTH, java.util.Calendar.YEAR};

    private DataUtil() {
    }

    public static DataModel objectAsDataModel(Object object) {
        DataModel dataModel = null;
        if (object == null)
            dataModel = new ArrayDataModel(new Object[0]);
        else if (object instanceof DataModel)
            dataModel = (DataModel) object;
        else if (object instanceof List)
            dataModel = new ListDataModel((List) object);
        else if (object instanceof Collection)
            dataModel = new ListDataModel(new ArrayList((Collection) object));
        else if (object.getClass().isArray())
            dataModel = new ArrayDataModel(Components.anyArrayToObjectArray(object));
        else if (object instanceof ResultSet)
            dataModel = new ResultSetDataModel((ResultSet) object);
        else {
            try {
                if (object instanceof Result) {
                    dataModel = new ResultDataModel((Result) object);
                }
            } catch (NoClassDefFoundError e) {
                // absence of javax.servlet.jsp.jstl.sql.Result class is an allowed configuration --
                // we treat wrappedData as not an instance of Result class in this case
            }

            if (dataModel == null)
                dataModel = new ScalarDataModel(object);
        }
        return dataModel;
    }

    public static List dataModelAsList(DataModel model) {
        int rowCount = model.getRowCount();
        List result = new ArrayList(rowCount > 0 ? rowCount : 10);
        int i = 0;
        while (true) {
            model.setRowIndex(i++);
            if (!model.isRowAvailable())
                break;
            result.add(model.getRowData());
        }
        return result;
    }

    public static String colorAsHtmlColor(Color color) {
        if (color == null)
            return null;
        return "#" +
                byteToHexString(color.getRed()) +
                byteToHexString(color.getGreen()) +
                byteToHexString(color.getBlue());
    }

    public static Color htmlColorAsColor(String htmlColor) {
        if (!htmlColor.startsWith("#"))
            throw new IllegalArgumentException("htmlColor should start with '#' character");
        if (htmlColor.length() != 7)
            throw new IllegalArgumentException("htmlColor should have 7 characters: '#' character followed by six hexadecimal digits");
        String redStr = htmlColor.substring(1, 3);
        String greenStr = htmlColor.substring(3, 5);
        String blueStr = htmlColor.substring(5, 7);
        Color result = new Color(
                Integer.parseInt(redStr, 16),
                Integer.parseInt(greenStr, 16),
                Integer.parseInt(blueStr, 16));
        return result;
    }

    public static String byteToHexString(int byteValue) {
        String str = Integer.toHexString(byteValue);
        if (str.length() == 1)
            str = "0" + str;
        return str;
    }

    public static synchronized String formatDateForJs(Date date, TimeZone timeZone) {
        if (date == null)
            return null;
        JS_DATE_FORMAT.setTimeZone(timeZone);
        return JS_DATE_FORMAT.format(date);
    }

    public static synchronized String formatTimeForJs(Date date, TimeZone timeZone) {
        if (date == null)
            return null;
        JS_TIME_FORMAT.setTimeZone(timeZone);
        return JS_TIME_FORMAT.format(date);
    }

    public static synchronized Date parseDateFromJs(String dateStr, TimeZone timeZone) {
        try {
            JS_DATE_FORMAT.setTimeZone(timeZone);
            return JS_DATE_FORMAT.parse(dateStr);
        }
        catch (ParseException e) {
            throw new ConverterException(e);
        }
    }

    public static synchronized String formatDateTimeForJs(Date date, TimeZone timeZone) {
        if (date == null)
            return null;
        JS_DATE_TIME_FORMAT.setTimeZone(timeZone);
        return JS_DATE_TIME_FORMAT.format(date);
    }

    public static synchronized Date parseDateTimeFromJs(String dateStr, TimeZone timeZone) {
        try {
            JS_DATE_TIME_FORMAT.setTimeZone(timeZone);
            return JS_DATE_TIME_FORMAT.parse(dateStr);
        }
        catch (ParseException e) {
            throw new ConverterException(e);
        }
    }

    public static void copyDateKeepingTime(Date fromDate, Date toDate,
                                           TimeZone timeZone) {
        Calendar toCalendar = java.util.Calendar.getInstance(timeZone);
        toCalendar.setTime(toDate);

        Calendar fromCalendar = Calendar.getInstance(timeZone);
        fromCalendar.setTime(fromDate);
        for (int fieldToCopy : DATE_CALENDAR_FIELDS) {
            toCalendar.set(fieldToCopy, fromCalendar.get(fieldToCopy));
        }
        toDate.setTime(toCalendar.getTime().getTime());
    }

    public static List readDataModelExpressionAsList(FacesContext context, ValueExpression dataExpression) {
        Object value = dataExpression.getValue(context.getELContext());
        DataModel dataModel = DataUtil.objectAsDataModel(value);
        return DataUtil.dataModelAsList(dataModel);
    }

    public static JSONArray arrayToJSONArray(ConvertibleToJSON[] array, Map<String, TimeZone> params) {
        return listToJSONArray(Arrays.asList(array), params);
    }

    public static JSONArray listToJSONArray(List<? extends ConvertibleToJSON> entries, Map<String, TimeZone> params) {
        JSONArray eventsJsArray = new JSONArray();
        for (int i = 0; i < entries.size(); i++) {
            ConvertibleToJSON obj = entries.get(i);
            try {
                eventsJsArray.put(i, obj.toJSONObject(params));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return eventsJsArray;
    }

    public static boolean isValidObjectId(String str) {
        int length = str.length();
        if (length == 0)
            return false;
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (!Character.isJavaIdentifierPart(c))
                return false;
        }
        return true;
    }
}
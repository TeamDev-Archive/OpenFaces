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

package org.openfaces.testapp.testBeans;


import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.PlainSeries;
import org.openfaces.component.chart.Series;

import javax.faces.model.SelectItem;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * This class is required for visual appearance of default components view testing.
 * Is used on pages <component_name>_defaultView.jsp
 */
public class DefaultViewTestBean {

    /**
     * is used in calendar_defaultView.jsp
     *
     * @return Date instance of 31-12-2007 12:00:00
     */
    public Date getDefaultDate() {
        //TODO: if date depends on TimeZone?
        TimeZone timeZone = new SimpleTimeZone(2, "uk");
        Calendar calendar = Calendar.getInstance(timeZone, new Locale("uk"));
        calendar.set(2007, 12, 31, 12, 0, 0);
        return calendar.getTime();
    }

    /**
     * is used in chart_defaultView.jsp
     *
     * @return ChartModel instance with one plain series that will form
     *         chart with 10%,20%,30% and 40% segments
     */
    public ChartModel getDefaultChartModel() {
        return new InnerChartModel();
    }

    /**
     * is used in dynamicImage_defaultView.jsp
     *
     * @return BufferedImage instance with two centered circles, caption "dynamic image"
     *         and two horizontal lines
     */
    public BufferedImage getDefaultDynamicImage() {
        BufferedImage image = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.CYAN);
        g.fillOval(0, 0, 150, 150);
        g.setColor(Color.YELLOW);
        g.fillOval(20, 20, 110, 110);
        g.setColor(Color.BLACK);
        g.drawString("dynamic image", 35, 70);
        image.getGraphics().drawLine(0, 70, 150, 70);
        image.getGraphics().drawLine(0, 100, 150, 100);
        return image;
    }

    public List<SelectItem> getTwoListDefaultItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        int counter = 0;
        items.add(new SelectItem("строка " + counter, "красный " + counter, "описание " + counter++));
        items.add(new SelectItem("строка " + counter, "жёлтый" + counter, "описание " + counter++));
        items.add(new SelectItem("строка " + counter, "зелёный" + counter, "описание " + counter++, true));
        items.add(new SelectItem("строка " + counter, "синий" + counter, "описание " + counter++, true));
        items.add(new SelectItem("строка " + counter, "оранжевый" + counter, "описание " + counter++));
        items.add(new SelectItem("строка " + counter, "фиолетовый" + counter, "описание " + counter++));
        items.add(new SelectItem("строка " + counter, "белый" + counter, "описание " + counter++));
        items.add(new SelectItem("строка " + counter, "сиреневый" + counter, "описание " + counter++));
        items.add(new SelectItem("строка " + counter, "чёрный" + counter, "описание " + counter++));
        items.add(new SelectItem("строка " + counter, "коричневый" + counter, "описание " + counter++));
        return items;
    }

    private static class InnerChartModel implements ChartModel {
        public Series[] getSeries() {
            Series[] series = new Series[1];
            PlainSeries series0 = new PlainSeries();
            series[0] = series0;

            Map<String, Integer> data = new HashMap<String, Integer>();

            data.put("ten", 10);
            data.put("twenty", 20);
            data.put("thirty", 30);
            data.put("fourty", 40);

            series0.setData(data);

            return series;
        }
    }
}

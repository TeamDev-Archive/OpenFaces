/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.portlets;

import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.PlainModel;
import org.openfaces.component.chart.PlainSeries;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Util {
  private Locale myLocale = new Locale("en");
  private Date myDate = new Date();
  private List<SelectItem> myAvailableItems = new ArrayList<SelectItem>();
  private List<String> mySelectedItems = new ArrayList<String>();


  public Util() {
    myAvailableItems.add(new SelectItem("value1", "label1", "description", false));
    myAvailableItems.add(new SelectItem("value2", "label2", "description", false));
    myAvailableItems.add(new SelectItem("value3", "label3", "description", true));
    myAvailableItems.add(new SelectItem("value4", "label4", "description", false));
    myAvailableItems.add(new SelectItem("value5", "label5", "description", false));
    myAvailableItems.add(new SelectItem("value6", "label6", "description", false));
    myAvailableItems.add(new SelectItem("value7", "label7", "description", true));
    myAvailableItems.add(new SelectItem("value8", "label8", "description", false));
    myAvailableItems.add(new SelectItem("value9", "label9", "description", false));
    myAvailableItems.add(new SelectItem("value10", "label10", "description", true));
    mySelectedItems.add("value5");
    mySelectedItems.add("value7");
    mySelectedItems.add("value10");
  }


  public List<String> getSelectedItems() {
    return mySelectedItems;
  }

  public void setSelectedItems(List<String> selectedItems) {
    mySelectedItems = selectedItems;
  }

  public List<SelectItem> getAvailableItems() {
    return myAvailableItems;
  }

  public void setAvailableItems(List<SelectItem> availableItems) {
    myAvailableItems = availableItems;
  }

  public Date getDate() {
    return myDate;
  }

  public void setDate(Date date) {
    myDate = date;
  }

  public Locale getLocale() {
    return myLocale;
  }

  public ChartModel getChart() {

    HashMap<String, Integer> data = new HashMap<String, Integer>();
    data.put("London, United Kingdom", 7615000);
    data.put("Berlin, Germany", 3396990);
    data.put("Madrid, Spain", 3155359);
    data.put("Rome, Italy", 2542003);
    data.put("Paris, France", 2142800);

    PlainSeries series = new PlainSeries("Largest cities of the European Union");
    series.setData(data);

    PlainModel model = new PlainModel();
    model.addSeries(series);
    return model;
  }
}

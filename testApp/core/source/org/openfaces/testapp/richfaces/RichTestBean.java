/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.richfaces;

import org.openfaces.util.Faces;
import org.openfaces.component.chart.BarChartView;
import org.openfaces.component.chart.LineChartView;
import org.openfaces.component.chart.PieChartView;

import javax.faces.model.SelectItem;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class RichTestBean {

    private List<TreeItem> rootMessages = new ArrayList<TreeItem>();
    private List<TableItem> tableValue = new ArrayList<TableItem>();
    private Date calendarValue;
    private Date dateChooserValue;
    private String dropDownFieldValue1;
    private String dropDownFieldValue2;
    private String suggestionFieldValue1;
    private String suggestionFieldValue2;
    private List twoListSelectionValue;
    private List<SelectItem> items = new ArrayList<SelectItem>();
    private transient PieChartView pieView;
    private transient BarChartView barView;
    private transient LineChartView lineView;
    private List<String> suggestedValues = new ArrayList<String>();

    public RichTestBean() {
        for (int r = 0; r < 5; r++) {
            List<TreeItem> child = new ArrayList<TreeItem>();
            for (int j = 0; j < 5; j++) {
                List<TreeItem> childChild = new ArrayList<TreeItem>();
                for (int i = 0; i < 5; i++) {
                    childChild.add(new TreeItem("Name" + r + j + i, "Field" + r + j + i, "Field" + r + j + i, i * j * r, i * j, null));
                }
                child.add(new TreeItem("Name" + r + j, "Field" + r + j, "Field" + r + j, r * j, j, childChild));
            }
            rootMessages.add(new TreeItem("Name" + r, "Firld" + r, "Field" + r, r * r, r, child));
        }
        for (int i = 0; i < 25; i++) {
            tableValue.add(new TableItem("Field 1." + i, "Field 2." + i, i, i * i));
        }
        for (int i = 0; i < 10; i++) {
            items.add(new SelectItem("value" + i, "Label " + i));
        }
        try {
            InputStream resource = RichTestBean.class.getResourceAsStream("../dropdown/houseplants.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            String currentString;
            while (true) {
                currentString = reader.readLine();
                if (currentString == null) break;
                suggestedValues.add(new String(currentString.getBytes(), "UTF-8"));
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List autocomplete(Object event) {
        List<String> suggestedValues = new ArrayList<String>();
        String pref = event.toString();
        if (pref != null) {
            for (Object myPlant : this.suggestedValues) {
                String plant = (String) myPlant;
                String plantForComparison = plant.toLowerCase();
                String typedValueForComparison = pref.toLowerCase();
                if (plantForComparison.startsWith(typedValueForComparison)) {
                    suggestedValues.add(plant);
                }
            }
        } else {
            for (int i = 0; i < this.suggestedValues.size(); i++) {
                if (i % 20 == 0) {
                    String plant = this.suggestedValues.get(i);
                    suggestedValues.add(plant);
                }
            }
        }
        return suggestedValues;
    }


    public List getNodeChildren() {
        TreeItem item = getNode();
        return item != null ? item.getChildren() : rootMessages;
    }

    public boolean getNodeHasChildren() {
        TreeItem item = getNode();
        return item.hasChildren();
    }

    private TreeItem getNode() {
        return Faces.var("node", TreeItem.class);
    }

    public RenderedImage getTestImage() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawLine(20, 20, 30, 30);
        image.getGraphics().drawLine(30, 20, 20, 30);
        return image;
    }

    public List<TreeItem> getRootMessages() {
        return rootMessages;
    }

    public void setRootMessages(List<TreeItem> rootMessages) {
        this.rootMessages = rootMessages;
    }

    public List<TableItem> getTableValue() {
        return tableValue;
    }

    public void setTableValue(List<TableItem> tableValue) {
        this.tableValue = tableValue;
    }


    public Date getCalendarValue() {
        return calendarValue;
    }

    public void setCalendarValue(Date calendarValue) {
        this.calendarValue = calendarValue;
    }

    public Date getDateChooserValue() {
        return dateChooserValue;
    }

    public void setDateChooserValue(Date dateChooserValue) {
        this.dateChooserValue = dateChooserValue;
    }

    public String getDropDownFieldValue1() {
        return dropDownFieldValue1;
    }

    public void setDropDownFieldValue1(String dropDownFieldValue1) {
        this.dropDownFieldValue1 = dropDownFieldValue1;
    }

    public String getDropDownFieldValue2() {
        return dropDownFieldValue2;
    }

    public void setDropDownFieldValue2(String dropDownFieldValue2) {
        this.dropDownFieldValue2 = dropDownFieldValue2;
    }


    public List getTwoListSelectionValue() {
        return twoListSelectionValue;
    }

    public void setTwoListSelectionValue(List twoListSelectionValue) {
        this.twoListSelectionValue = twoListSelectionValue;
    }

    public List<SelectItem> getItems() {
        return items;
    }

    public void setItems(List<SelectItem> items) {
        this.items = items;
    }

    public PieChartView getPieView() {
        return pieView;
    }

    public void setPieView(PieChartView pieView) {
        this.pieView = pieView;
    }

    public BarChartView getBarView() {
        return barView;
    }

    public void setBarView(BarChartView barView) {
        this.barView = barView;
    }

    public LineChartView getLineView() {
        return lineView;
    }

    public void setLineView(LineChartView lineView) {
        this.lineView = lineView;
    }

    public String getSuggestionFieldValue1() {
        return suggestionFieldValue1;
    }

    public void setSuggestionFieldValue1(String suggestionFieldValue1) {
        this.suggestionFieldValue1 = suggestionFieldValue1;
    }

    public String getSuggestionFieldValue2() {
        return suggestionFieldValue2;
    }

    public void setSuggestionFieldValue2(String suggestionFieldValue2) {
        this.suggestionFieldValue2 = suggestionFieldValue2;
    }
}

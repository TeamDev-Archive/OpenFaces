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

import org.openfaces.component.output.GraphicText;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class WithGraphicTextDataBean {

    private List data = new ArrayList();
    private Object text;
    private Integer direction = 90;
    private String fontName = "";

    private String fontSize = "";
    private String fontColorR = "";
    private String fontColorG = "";
    private String fontColorB = "";

    private String style;
    private Date dateValue;
    private CustomConverter dateConverter = new CustomConverter();
    private GraphicText graphicText = new GraphicText();

    public WithGraphicTextDataBean() {

        graphicText.setValue("Test Value");
        graphicText.setDirection(70);
        graphicText.setTextStyle("color: pink; font-size: 20px;");

        Calendar c2 = Calendar.getInstance();
        c2.set(2008, 5, 9);
        dateValue = c2.getTime();

        text = "Test";
        fontSize = "20pt";
        fontColorG = "0";
        fontColorB = "0";
        fontColorR = "255";
        fontName = "";

        Random rand = new Random();
        for (int i = 0; i < 15; i++) {
            data.add(new TestItem(String.valueOf(rand.nextInt(1000)),
                    String.valueOf(rand.nextInt(1000)),
                    String.valueOf(rand.nextInt(1000)),
                    String.valueOf(rand.nextInt(1000)),
                    String.valueOf(rand.nextInt(1000)),
                    String.valueOf(rand.nextInt(1000)),
                    String.valueOf(rand.nextInt(1000)),
                    String.valueOf(rand.nextInt(1000)),
                    String.valueOf(rand.nextInt(1000)),
                    String.valueOf(rand.nextInt(1000)),
                    String.valueOf(rand.nextInt(1000)),
                    String.valueOf(rand.nextInt(1000)),
                    String.valueOf(rand.nextInt(1000))));
        }

    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getStyle() {
        if (!fontName.equals("")) {
            style += "font-family:" + fontName + ";";
        }
        if (!fontSize.equals("")) {
            style += "font-size:" + fontSize + ";";
        }
        if (!fontColorB.equals("") && !fontColorR.equals("") && !fontColorG.equals("")) {
            style += "color:rgb(" + fontColorR + ", " + fontColorG + ", " + fontColorB + ");";
        }
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getFontColorR() {
        return fontColorR;
    }

    public void setFontColorR(String fontColorR) {
        this.fontColorR = fontColorR;
    }

    public String getFontColorG() {
        return fontColorG;
    }

    public void setFontColorG(String fontColorG) {
        this.fontColorG = fontColorG;
    }

    public String getFontColorB() {
        return fontColorB;
    }

    public void setFontColorB(String fontColorB) {
        this.fontColorB = fontColorB;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public GraphicText getGraphicText() {
        return graphicText;
    }

    public void setGraphicText(GraphicText graphicText) {
        this.graphicText = graphicText;
    }

    public class TestItem {

        private String value1;
        private String value2;
        private String value3;
        private String value4;
        private String value5;
        private String value6;
        private String value7;
        private String value8;
        private String value9;
        private String value10;
        private String value11;
        private String value12;
        private String value13;

        public TestItem(String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, String value9, String value10, String value11, String value12, String value13) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.value4 = value4;
            this.value5 = value5;
            this.value6 = value6;
            this.value7 = value7;
            this.value8 = value8;
            this.value9 = value9;
            this.value10 = value10;
            this.value11 = value11;
            this.value12 = value12;
            this.value13 = value13;
        }

        public String getValue1() {
            return value1;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public String getValue3() {
            return value3;
        }

        public void setValue3(String value3) {
            this.value3 = value3;
        }

        public String getValue4() {
            return value4;
        }

        public void setValue4(String value4) {
            this.value4 = value4;
        }

        public String getValue5() {
            return value5;
        }

        public void setValue5(String value5) {
            this.value5 = value5;
        }

        public String getValue6() {
            return value6;
        }

        public void setValue6(String value6) {
            this.value6 = value6;
        }

        public String getValue7() {
            return value7;
        }

        public void setValue7(String value7) {
            this.value7 = value7;
        }

        public String getValue8() {
            return value8;
        }

        public void setValue8(String value8) {
            this.value8 = value8;
        }

        public String getValue9() {
            return value9;
        }

        public void setValue9(String value9) {
            this.value9 = value9;
        }

        public String getValue10() {
            return value10;
        }

        public void setValue10(String value10) {
            this.value10 = value10;
        }

        public String getValue11() {
            return value11;
        }

        public void setValue11(String value11) {
            this.value11 = value11;
        }

        public String getValue12() {
            return value12;
        }

        public void setValue12(String value12) {
            this.value12 = value12;
        }

        public String getValue13() {
            return value13;
        }

        public void setValue13(String value13) {
            this.value13 = value13;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TestItem testItem = (TestItem) o;

            if (value1 != null ? !value1.equals(testItem.value1) : testItem.value1 != null) return false;
            if (value10 != null ? !value10.equals(testItem.value10) : testItem.value10 != null) return false;
            if (value11 != null ? !value11.equals(testItem.value11) : testItem.value11 != null) return false;
            if (value12 != null ? !value12.equals(testItem.value12) : testItem.value12 != null) return false;
            if (value13 != null ? !value13.equals(testItem.value13) : testItem.value13 != null) return false;
            if (value2 != null ? !value2.equals(testItem.value2) : testItem.value2 != null) return false;
            if (value3 != null ? !value3.equals(testItem.value3) : testItem.value3 != null) return false;
            if (value4 != null ? !value4.equals(testItem.value4) : testItem.value4 != null) return false;
            if (value5 != null ? !value5.equals(testItem.value5) : testItem.value5 != null) return false;
            if (value6 != null ? !value6.equals(testItem.value6) : testItem.value6 != null) return false;
            if (value7 != null ? !value7.equals(testItem.value7) : testItem.value7 != null) return false;
            if (value8 != null ? !value8.equals(testItem.value8) : testItem.value8 != null) return false;
            if (value9 != null ? !value9.equals(testItem.value9) : testItem.value9 != null) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = (value1 != null ? value1.hashCode() : 0);
            result = 31 * result + (value2 != null ? value2.hashCode() : 0);
            result = 31 * result + (value3 != null ? value3.hashCode() : 0);
            result = 31 * result + (value4 != null ? value4.hashCode() : 0);
            result = 31 * result + (value5 != null ? value5.hashCode() : 0);
            result = 31 * result + (value6 != null ? value6.hashCode() : 0);
            result = 31 * result + (value7 != null ? value7.hashCode() : 0);
            result = 31 * result + (value8 != null ? value8.hashCode() : 0);
            result = 31 * result + (value9 != null ? value9.hashCode() : 0);
            result = 31 * result + (value10 != null ? value10.hashCode() : 0);
            result = 31 * result + (value11 != null ? value11.hashCode() : 0);
            result = 31 * result + (value12 != null ? value12.hashCode() : 0);
            result = 31 * result + (value13 != null ? value13.hashCode() : 0);
            return result;
        }
    }

    private class CustomConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return null;
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            return DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH).format((Date) value);
        }
    }

    public Converter getDateConverter() {
        return dateConverter;
    }


}

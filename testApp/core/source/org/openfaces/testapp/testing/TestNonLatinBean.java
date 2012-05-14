/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.testing;

import org.openfaces.util.Faces;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class TestNonLatinBean {

    public class TestData {
        private int id;
        private String text;
        private double number;

        public int getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public double getNumber() {
            return number;
        }

        public TestData(int id, String text, double number) {
            this.id = id;
            this.text = text;
            this.number = number;
        }
    }


    private String dropDownValue;
    private List<String> dropDownValues = new ArrayList<String>();
    private List<TestData> data = new ArrayList<TestData>();

    public TestNonLatinBean() {
        dropDownValues.add("abc\u00E3");
        dropDownValues.add("adr\u00E3\u00FA");
        dropDownValues.add("abd\u011A");
        dropDownValues.add("bsre\u00F3\u011A");
        data.add(new TestData(1, "\u00E3", 12324.23));
        data.add(new TestData(2, "\u00E3\u00FA", 104232.42));
        data.add(new TestData(3, "\u011A", 0));
        data.add(new TestData(4, "\u00F3\u011A", 23.22));
        data.add(new TestData(298, "Русский текст", 23907.234098));
        data.add(new TestData(57, "Ещё русский текст", Math.PI));
        data.add(new TestData(87, "Текст українською мовою", Math.PI));
        data.add(new TestData(29, "Português", Math.E));
        data.add(new TestData(20, "Español", Math.E));
        data.add(new TestData(5690, "Slovenščina", Math.sqrt(Math.PI)));
        data.add(new TestData(39, "Arabic - العربية", Math.sqrt(Math.PI)));
        data.add(new TestData(48, "Korean - 한국어", 0));
        data.add(new TestData(-29, "Chinese - 中文", 1));
        data.add(new TestData(39, "Thai - ไทย", 2));
        data.add(new TestData(39, "Japanese - 日本語", -3));
        data.add(new TestData(39, "Czech - Čeština", 4));
        data.add(new TestData(39, "Norwegian - Norsk (bokmål)", 5.5));
        data.add(new TestData(39, "Hindi - हिन्दी", 324));
    }

    public String getDropDownValue() {
        return dropDownValue;
    }

    public void setDropDownValue(String dropDownValue) {
        this.dropDownValue = dropDownValue;
    }


    public List<String> getDropDownValues() {
        return dropDownValues;
    }

    public void setDropDownValues(List<String> dropDownValues) {
        this.dropDownValues = dropDownValues;
    }


    public List<TestData> getData() {
        return data;
    }

    public void setData(List<TestData> data) {
        this.data = data;
    }


    public List getSuggestedValues() {
        List<String> suggestedValues = new ArrayList<String>();
        String typedValue = (String) Faces.var("searchString");
        if (typedValue != null) {
            for (String value : dropDownValues) {
                String valueForComparison = value.toLowerCase();
                String typedValueForComparison = typedValue.toLowerCase();
                if (valueForComparison.contains(typedValueForComparison)) {
                    suggestedValues.add(value);
                }
            }
        }

        return suggestedValues;
    }
}
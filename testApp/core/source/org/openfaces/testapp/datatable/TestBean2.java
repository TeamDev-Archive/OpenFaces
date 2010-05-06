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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Dmitry Pikhulya
 */
public class TestBean2 {
    private static int idCounter = 0;
    private String id;
    private String stringField1;
    private String stringField2;
    private String stringField3;
    private int intField1;
    private int intField2;
    private int intField3;
    private boolean booleanField;
    private Map customFields = new HashMap();

    private static Map<String, TestBean2> idToBeanMap = new HashMap<String, TestBean2>();

    public TestBean2(String stringField1, String stringField2, String stringField3, int intField1, int intField2, int intField3, boolean booleanField) {
        this.stringField1 = stringField1;
        this.stringField2 = stringField2;
        this.stringField3 = stringField3;
        this.intField1 = intField1;
        this.intField2 = intField2;
        this.intField3 = intField3;
        this.booleanField = booleanField;
        id = String.valueOf(idCounter++);
        idToBeanMap.put(id, this);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            String customFieldName = String.valueOf(i);
            String customFieldValue = String.valueOf(Math.abs(random.nextInt()) % 1000);
            customFields.put(customFieldName, customFieldValue);
        }
    }

    public Map getCustomFields() {
        return customFields;
    }

    public String getStringField1() {
        return stringField1;
    }

    public void setStringField1(String stringField1) {
        this.stringField1 = stringField1;
    }

    public String getStringField2() {
        return stringField2;
    }

    public void setStringField2(String stringField2) {
        this.stringField2 = stringField2;
    }

    public String getStringField3() {
        return stringField3;
    }

    public void setStringField3(String stringField3) {
        this.stringField3 = stringField3;
    }

    public int getIntField1() {
        return intField1;
    }

    public void setIntField1(int intField1) {
        this.intField1 = intField1;
    }

    public int getIntField2() {
        return intField2;
    }

    public void setIntField2(int intField2) {
        this.intField2 = intField2;
    }

    public int getIntField3() {
        return intField3;
    }

    public void setIntField3(int intField3) {
        this.intField3 = intField3;
    }

    public boolean isBooleanField() {
        return booleanField;
    }

    public void setBooleanField(boolean booleanField) {
        this.booleanField = booleanField;
    }

    public String getId() {
        return id;
    }

    public static TestBean2 findById(String id) {
        return idToBeanMap.get(id);
    }
}

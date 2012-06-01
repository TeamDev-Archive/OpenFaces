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

package org.openfaces.testapp.datatable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Dmitry Pikhulya
 */
public class MultitypeBean {
    private static int idCounter = 0;

    private int id = idCounter++;

    private boolean booleanField;
    private byte byteField;
    private short shortField;
    private int intField;
    private long longField;
    private float floatField;
    private double doubleField;

    private Map<String, Object> customFields = new HashMap<String, Object>();

    private String stringField;

    private EnumType1 enumField;
    private Date dateField;

    public MultitypeBean() {
        Random r = new Random();
        booleanField = r.nextBoolean();
        byteField = (byte) r.nextInt();
        shortField = (short) r.nextInt(1000);
        intField = r.nextInt(10000);
        longField = r.nextInt(100000);
        floatField = r.nextFloat() * 10;
        doubleField = r.nextDouble() * 100;

        stringField = nextString(r);
        enumField = nextEnum(r, EnumType1.class);
        dateField = nextDate(r);

        for (int i = 0; i < 3; i++) {
            customFields.put("int" + String.valueOf(i), r.nextInt(10000));
            customFields.put("double" + String.valueOf(i), r.nextDouble() * 100.0);
            customFields.put("string" + String.valueOf(i), nextString(r));
            customFields.put("date" + String.valueOf(i), nextDate(r));
        }
    }

    public int getId() {
        return id;
    }

    public Map<String, Object> getCustomFields() {
        return customFields;
    }

    private <E extends Enum> E nextEnum(Random r, Class<E> enumClass) {
        E[] enumConstants = enumClass.getEnumConstants();
        int randomIndex = r.nextInt(enumConstants.length);
        return enumConstants[randomIndex];
    }

    private Date nextDate(Random r) {
        return new Date(System.currentTimeMillis() + r.nextLong() % (1000l*60*60*24 * 100));
    }

    private String nextString(Random r) {
        return Long.toString(Math.abs(r.nextLong()), 36);
    }

    public boolean isBooleanField() {
        return booleanField;
    }

    public void setBooleanField(boolean booleanField) {
        this.booleanField = booleanField;
    }

    public byte getByteField() {
        return byteField;
    }

    public void setByteField(byte byteField) {
        this.byteField = byteField;
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    public short getShortField() {
        return shortField;
    }

    public void setShortField(short shortField) {
        this.shortField = shortField;
    }

    public long getLongField() {
        return longField;
    }

    public void setLongField(long longField) {
        this.longField = longField;
    }

    public float getFloatField() {
        return floatField;
    }

    public void setFloatField(float floatField) {
        this.floatField = floatField;
    }

    public double getDoubleField() {
        return doubleField;
    }

    public void setDoubleField(double doubleField) {
        this.doubleField = doubleField;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public EnumType1 getEnumField() {
        return enumField;
    }

    public void setEnumField(EnumType1 enumField) {
        this.enumField = enumField;
    }

    public Date getDateField() {
        return dateField;
    }

    public void setDateField(Date dateField) {
        this.dateField = dateField;
    }
}

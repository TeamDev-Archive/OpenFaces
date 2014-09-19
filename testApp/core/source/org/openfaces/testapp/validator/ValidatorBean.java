/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.validator;

import org.openfaces.validator.EqualValidator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Vladimir Korenev
 */
public class ValidatorBean {
    private byte bytePrim;
    private short shortPrim;
    private int intPrim;
    private long longPrim;
    private float floatPrim;
    private double doublePrim;
    private Byte byteObj;
    private Short shortObj;
    private Integer integerObj;
    private Long longObj;
    private Float floatObj;
    private Double doubleObj;
    private String requiredStr;
    private String lengthStr;
    private String longRangeStr;
    private String doubleRangeStr;
    private Date date = new GregorianCalendar(12, 12, 2005).getTime();
    private String customProperty;
    private String customField;
    private Integer customFieldInt;
    private BigInteger bigIntegerObj;
    private BigDecimal bigDecimalObj;
    private boolean booleanTest;
    private Boolean booleanObj;
    private Character characterObj;
    private String email;
    private String url;
    private Number numberConverterTest = 442314.898989898;
    private String demo1;
    private Double demo2;
    private String demo3;
    private Double item1 = 1.11;
    private Double item2 = 2.22;
    private Double item3 = 3.33;

    public Double getItem1() {
        return item1;
    }

    public void setItem1(Double item1) {
        this.item1 = item1;
    }

    public Double getItem2() {
        return item2;
    }

    public void setItem2(Double item2) {
        this.item2 = item2;
    }

    public Double getItem3() {
        return item3;
    }

    public void setItem3(Double item3) {
        this.item3 = item3;
    }

    public String getDemo3() {
        return demo3;
    }

    public void setDemo3(String demo3) {
        this.demo3 = demo3;
    }

    public Double getDemo2() {
        return demo2;
    }

    public void setDemo2(Double demo2) {
        this.demo2 = demo2;
    }

    public String getDemo1() {
        return demo1;
    }

    public void setDemo1(String demo1) {
        this.demo1 = demo1;
    }

    public Number getNumberConverterTest() {
        return numberConverterTest;
    }

    public void setNumberConverterTest(Number numberConverterTest) {
        this.numberConverterTest = numberConverterTest;
    }

    public Integer[] getTableData() {
        return tableData;
    }

    public void setTableData(Integer[] tableData) {
        this.tableData = tableData;
    }

    private Integer[] tableData = new Integer[]{2, 9, 12};

    public String getURL() {
        return url;
    }

    public void setURL(String URL) {
        url = URL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Character getCharacterObj() {
        return characterObj;
    }

    public void setCharacterObj(Character characterObj) {
        this.characterObj = characterObj;
    }

    public Integer getCustomFieldInt() {
        return customFieldInt;
    }

    public void setCustomFieldInt(Integer customFieldInt) {
        this.customFieldInt = customFieldInt;
    }

    public Boolean getBooleanObj() {
        return booleanObj;
    }

    public void setBooleanObj(Boolean booleanObj) {
        this.booleanObj = booleanObj;
    }

    public boolean isBooleanTest() {
        return booleanTest;
    }

    public void setBooleanTest(boolean booleanTest) {
        this.booleanTest = booleanTest;
    }


    public BigDecimal getBigDecimalObj() {
        return bigDecimalObj;
    }

    public void setBigDecimalObj(BigDecimal bigDecimalObj) {
        this.bigDecimalObj = bigDecimalObj;
    }

    public BigInteger getBigIntegerObj() {
        return bigIntegerObj;
    }

    public void setBigIntegerObj(BigInteger bigIntegerObj) {
        this.bigIntegerObj = bigIntegerObj;
    }

    public String getCustomField() {
        return customField;
    }

    public void setCustomField(String customField) {
        this.customField = customField;
    }


    public Locale getFrLocale() {
        return Locale.FRANCE;
    }

    public String getCustomProperty() {
        return customProperty;
    }

    public void setCustomProperty(String customProperty) {
        this.customProperty = customProperty;
    }

    public byte getBytePrim() {
        return bytePrim;
    }

    public void setBytePrim(byte bytePrim) {
        this.bytePrim = bytePrim;
    }

    public short getShortPrim() {
        return shortPrim;
    }

    public void setShortPrim(short shortPrim) {
        this.shortPrim = shortPrim;
    }

    public int getIntPrim() {
        return intPrim;
    }

    public void setIntPrim(int intPrim) {
        this.intPrim = intPrim;
    }

    public long getLongPrim() {
        return longPrim;
    }

    public void setLongPrim(long longPrim) {
        this.longPrim = longPrim;
    }

    public float getFloatPrim() {
        return floatPrim;
    }

    public void setFloatPrim(float floatPrim) {
        this.floatPrim = floatPrim;
    }

    public double getDoublePrim() {
        return doublePrim;
    }

    public void setDoublePrim(double doublePrim) {
        this.doublePrim = doublePrim;
    }

    public Byte getByteObj() {
        return byteObj;
    }

    public void setByteObj(Byte byteObj) {
        this.byteObj = byteObj;
    }

    public Short getShortObj() {
        return shortObj;
    }

    public void setShortObj(Short shortObj) {
        this.shortObj = shortObj;
    }

    public Integer getIntegerObj() {
        return integerObj;
    }

    public void setIntegerObj(Integer integerObj) {
        this.integerObj = integerObj;
    }

    public Long getLongObj() {
        return longObj;
    }

    public void setLongObj(Long longObj) {
        this.longObj = longObj;
    }

    public Float getFloatObj() {
        return floatObj;
    }

    public void setFloatObj(Float floatObj) {
        this.floatObj = floatObj;
    }

    public Double getDoubleObj() {
        return doubleObj;
    }

    public void setDoubleObj(Double doubleObj) {
        this.doubleObj = doubleObj;
    }

    public String getRequiredStr() {
        return requiredStr;
    }

    public void setRequiredStr(String requiredStr) {
        this.requiredStr = requiredStr;
    }

    public String getLengthStr() {
        return lengthStr;
    }

    public void setLengthStr(String lengthStr) {
        this.lengthStr = lengthStr;
    }

    public String getLongRangeStr() {
        return longRangeStr;
    }

    public void setLongRangeStr(String longRangeStr) {
        this.longRangeStr = longRangeStr;
    }

    public String getDoubleRangeStr() {
        return doubleRangeStr;
    }

    public void setDoubleRangeStr(String doubleRangeStr) {
        this.doubleRangeStr = doubleRangeStr;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void customValidator(FacesContext context, UIComponent component, Object object) {
        context.addMessage(component.getClientId(context), new FacesMessage("server side validator error (summary)", "server side validator error (detail)"));
    }

    public boolean customValidatorTest(FacesContext context, UIComponent component, Object value) {
        return false;
    }

    public String register() {
        return null;
    }

    public String login() {
        return null;
    }


    public Validator validator(FacesContext context, UIComponent component, Object value) {
        EqualValidator e = new EqualValidator();
        e.setFor("first");
        e.validate(context, component, value);
        return e;
    }

    public Validator getValidator() {
        EqualValidator e = new EqualValidator();
        e.setFor("first");
        return e;
    }


}

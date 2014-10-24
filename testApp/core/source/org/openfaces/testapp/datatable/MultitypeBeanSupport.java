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

package org.openfaces.testapp.datatable;

import org.openfaces.component.table.CountFunction;
import org.openfaces.component.table.MaxFunction;
import org.openfaces.component.table.MinFunction;
import org.openfaces.component.table.SumFunction;
import org.openfaces.component.table.SummaryFunction;
import org.openfaces.util.Faces;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class MultitypeBeanSupport {
    private static final String[] CUSTOM_FIELD_PREFIXES = new String[]{"int", "double", "date", "string", "boolean", "temperature"};
    private static final String[] CUSTOM_FIELD_SUFFIXES = new String[]{"0", "1", "2"};

    private static final NumberConverter DOUBLE_COL_CONVERTER = new NumberConverter();
    static {
        DOUBLE_COL_CONVERTER.setMinFractionDigits(2);
        DOUBLE_COL_CONVERTER.setMaxFractionDigits(2);
    }

    private List<MultitypeBean> list1;

    public MultitypeBeanSupport() {
        list1 = createList1();
    }

    private List<MultitypeBean> createList1() {
        List<MultitypeBean> list = new ArrayList<MultitypeBean>();
        for (int i = 0; i < 30; i++) {
            list.add(new MultitypeBean());
        }
        return list;
    }

    public List<MultitypeBean> getList1() {
        return list1;
    }

    public List<String> getCustomFieldNames() {
        List<String> result = new ArrayList<String>();
        for (String prefix : CUSTOM_FIELD_PREFIXES) {
            for (String suffix : CUSTOM_FIELD_SUFFIXES) {
                result.add(prefix + suffix);
            }
        }
        return result;
    }

    private Map<String, SummaryFunction> customFieldsToDefaultFunctions;

    public Map<String, SummaryFunction> getCustomFieldsToDefaultFunctions() {
        if (customFieldsToDefaultFunctions == null) {
            customFieldsToDefaultFunctions = new HashMap<String, SummaryFunction>();
            for (String prefix : CUSTOM_FIELD_PREFIXES) {
                for (String suffix : CUSTOM_FIELD_SUFFIXES) {
                    SummaryFunction function = !prefix.equals("string") && !prefix.equals("boolean")
                            ? (
                            suffix.equals("0") ? new SumFunction() :
                                    suffix.equals("1") ? new MinFunction() :
                                            suffix.equals("2") ? new MaxFunction() : null
                    )
                            : new CountFunction();
                    customFieldsToDefaultFunctions.put(prefix + suffix, function);
                }
            }

        }
        return customFieldsToDefaultFunctions;
    }

    public Converter getColumnConverter() {
        String col = Faces.var("col", String.class);
        if (col != null && col.startsWith("double")) {
            return DOUBLE_COL_CONVERTER;
        } else {
            return null;
        }
    }

    private boolean inGroupFootersCollapsible = true;
    private boolean groupFootersCollapsible = true;
    private boolean functionEditable = true;
    private String patternText = "#{function}: #{valueString}";
    private ValueExpression patternExpression;
    private String style = "font-style: normal";

    public boolean isInGroupFootersCollapsible() {
        return inGroupFootersCollapsible;
    }

    public void setInGroupFootersCollapsible(boolean inGroupFootersCollapsible) {
        this.inGroupFootersCollapsible = inGroupFootersCollapsible;
    }

    public boolean isGroupFootersCollapsible() {
        return groupFootersCollapsible;
    }

    public void setGroupFootersCollapsible(boolean groupFootersCollapsible) {
        this.groupFootersCollapsible = groupFootersCollapsible;
    }

    public boolean isFunctionEditable() {
        return functionEditable;
    }

    public void setFunctionEditable(boolean functionEditable) {
        this.functionEditable = functionEditable;
    }

    public String getPattern() {
        if (patternExpression == null) {
            applyPattern();
        }
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        return patternExpression.getValue(elContext).toString();
    }

    public void applySettings() {
        applyPattern();
    }

    private void applyPattern() {
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        try {
            patternExpression = expressionFactory.createValueExpression(elContext, patternText, String.class);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        } catch (ELException e) {
            context.addMessage("form:patternInputText", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid pattern", e.getMessage()));
        }
    }

    public String getPatternText() {
        return patternText;
    }

    public void setPatternText(String patternText) {
        this.patternText = patternText;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}

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
package org.openfaces.renderkit.filter;

import org.openfaces.component.filter.ComboBoxFilter;
import org.openfaces.component.filter.ExpressionFilter;
import org.openfaces.component.filter.ExpressionFilterCriterion;
import org.openfaces.component.filter.FilterCondition;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.table.AbstractTableRenderer;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class ComboBoxFilterRenderer extends ExpressionFilterRenderer {
    private static final String USER_CRITERION_PREFIX = "u-";
    private static final String PREDEFINED_CRITERION_PREFIX = "p-";
    private static final String ALL = "ALL";
    private static final String EMPTY = "EMPTY";
    private static final String NON_EMPTY = "NON_EMPTY";

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ComboBoxFilter filter = ((ComboBoxFilter) component);

        ExpressionFilterCriterion currentCriterion = (ExpressionFilterCriterion) filter.getValue();

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("select", component);
        String clientId = writeIdAttribute(context, component);
        writeNameAttribute(context, component);
        writer.writeAttribute("size", "1", null);
        Rendering.writeStyleAndClassAttributes(writer, filter.getStyle(), filter.getStyleClass(), "o_fullWidth");
        writer.writeAttribute("onchange", getFilterSubmissionScript(filter), null);
        writer.writeAttribute("onclick", "event.cancelBubble = true;", null);
        writer.writeAttribute("onkeydown", "O$.stopEvent(event);", null);
        writer.writeAttribute("dir", filter.getDir(), "dir");
        writer.writeAttribute("lang", filter.getLang(), "lang");

        boolean thereAreEmptyItems = false;
        Collection<Object> criterionNamesCollection = filter.calculateAllCriterionNames(context);
        for (Iterator<Object> criterionIterator = criterionNamesCollection.iterator(); criterionIterator.hasNext();) {
            Object criterionObj = criterionIterator.next();
            if (isEmptyItem(criterionObj)) {
                thereAreEmptyItems = true;
                criterionIterator.remove();
            }
        }
        List<Object> criterionNames = new ArrayList<Object>(criterionNamesCollection);

        String allRecordsCriterionName = filter.getAllRecordsText();

        String predefinedCriterionsClass = getPredefinedCriterionClass(context, filter);
        writeOption(writer, component, PREDEFINED_CRITERION_PREFIX + ALL, allRecordsCriterionName,
                currentCriterion == null,
                predefinedCriterionsClass);
        FilterCondition condition = currentCriterion != null ? currentCriterion.getCondition() : null;
        if (thereAreEmptyItems) {
            String emptyRecordsCriterionName = filter.getEmptyRecordsText();
            writeOption(writer, component, PREDEFINED_CRITERION_PREFIX + EMPTY, emptyRecordsCriterionName,
                    condition != null && condition.equals(FilterCondition.EMPTY) && !currentCriterion.isInverse(),
                    predefinedCriterionsClass);

            String nonEmptyRecordsCriterionName = filter.getNonEmptyRecordsText();
            writeOption(writer, component, PREDEFINED_CRITERION_PREFIX + NON_EMPTY,
                    nonEmptyRecordsCriterionName,
                    condition != null && condition.equals(FilterCondition.EMPTY) && currentCriterion.isInverse(),
                    predefinedCriterionsClass);
        }

        Converter converter = getConverter(filter);
        Object currentCriterionArg = currentCriterion != null ? currentCriterion.getArg1() : null;
        String currentCriterionStr = converter.getAsString(context, filter, currentCriterionArg);
        boolean textCriterionSelected = false;
        for (Object criterionObj : criterionNames) {
            String criterionName = converter.getAsString(context, filter, criterionObj);
            boolean selected = currentCriterion instanceof ExpressionFilterCriterion &&
                    currentCriterionStr.equals(criterionName);
            writeOption(writer, component,
                    USER_CRITERION_PREFIX + criterionName,
                    criterionName,
                    selected, null);
            if (selected)
                textCriterionSelected = true;
        }
        boolean noRecordsWithSelectedCriterion = currentCriterion != null && condition != FilterCondition.EMPTY && !textCriterionSelected;
        if (noRecordsWithSelectedCriterion) {
            writeOption(writer, component,
                    USER_CRITERION_PREFIX + currentCriterionStr,
                    currentCriterionStr,
                    true, null);
        }

        UIComponent filteredComponent = (UIComponent) filter.getFilteredComponent();

        Rendering.renderInitScript(context,
                new ScriptBuilder().functionCall("O$.Filters._showFilter",
                        filteredComponent, clientId).semicolon(),
                Resources.utilJsURL(context),
                Resources.filtersJsURL(context),
                TableUtil.getTableUtilJsURL(context),
                AbstractTableRenderer.getTableJsURL(context));

        Styles.renderStyleClasses(context, component);
        writer.endElement("select");
    }


    private void writeOption(
            ResponseWriter writer,
            UIComponent component,
            String value,
            String text,
            boolean selected,
            String styleClass) throws IOException {
        writer.startElement("option", component);
        if (styleClass != null)
            writer.writeAttribute("class", styleClass, null);
        writer.writeAttribute("value", value, null);
        if (selected)
            writer.writeAttribute("selected", "true", null);
        writer.writeText(text, null);
        writer.endElement("option");
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String selectedCriterion = requestParameterMap.get(component.getClientId(context));
        if (selectedCriterion == null)
            return;
        ComboBoxFilter filter = ((ComboBoxFilter) component);
        if (selectedCriterion.startsWith(USER_CRITERION_PREFIX)) {
            String searchString = selectedCriterion.substring(USER_CRITERION_PREFIX.length());
            setDecodedString(filter, searchString);
        } else if (selectedCriterion.startsWith(PREDEFINED_CRITERION_PREFIX)) {
            String criterion = selectedCriterion.substring(PREDEFINED_CRITERION_PREFIX.length());
            ExpressionFilterCriterion newCriterion;
            if (criterion.equals(ALL))
                newCriterion = null;
            else if (criterion.equals(EMPTY))
                newCriterion = new ExpressionFilterCriterion(FilterCondition.EMPTY, false);
            else if (criterion.equals(NON_EMPTY))
                newCriterion = new ExpressionFilterCriterion(FilterCondition.EMPTY, true);
            else
                throw new IllegalStateException("Unknown predefined criterion came from client: " + criterion);
            setDecodedCriterion(filter, newCriterion);
        } else
            throw new IllegalStateException("Improperly formatted criterion came from client: " + selectedCriterion);
    }

    @Override
    protected FilterCondition getForceDefaultCondition(ExpressionFilter filter) {
        return FilterCondition.EQUALS;
    }
}

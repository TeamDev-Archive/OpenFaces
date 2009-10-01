/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
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
import org.openfaces.component.filter.EmptyRecordsCriterion;
import org.openfaces.component.filter.EqualsFilterCriterion;
import org.openfaces.component.filter.Filter;
import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.NonEmptyRecordsCriterion;
import org.openfaces.component.filter.OneParameterCriterion;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.table.AbstractTableRenderer;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class ComboBoxFilterRenderer extends FilterRenderer {
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

        FilterCriterion currentCriterion = filter.getValue();

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("select", component);
        String clientId = writeIdAttribute(context, component);
        writeNameAttribute(context, component);
        writer.writeAttribute("size", "1", null);
        RenderingUtil.writeStyleAndClassAttributes(writer, filter.getStyle(), filter.getStyleClass(), "o_fullWidth");
        String submitScript = getFilterSubmissionScript(filter);
        writer.writeAttribute("onchange", submitScript, null);
        writer.writeAttribute("onclick", "event.cancelBubble = true;", null);
        writer.writeAttribute("onkeydown", "O$.cancelBubble(event);", null);
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
        if (thereAreEmptyItems) {
            String emptyRecordsCriterionName = filter.getEmptyRecordsText();
            writeOption(writer, component, PREDEFINED_CRITERION_PREFIX + EMPTY, emptyRecordsCriterionName,
                    currentCriterion instanceof EmptyRecordsCriterion,
                    predefinedCriterionsClass);

            String nonEmptyRecordsCriterionName = filter.getNonEmptyRecordsText();
            writeOption(writer, component, PREDEFINED_CRITERION_PREFIX + NON_EMPTY,
                    nonEmptyRecordsCriterionName,
                    currentCriterion instanceof NonEmptyRecordsCriterion,
                    predefinedCriterionsClass);
        }

        boolean textCriterionSelected = false;
        for (Object criterionObj : criterionNames) {
            String criterionName = criterionObj != null ? criterionObj.toString() : "";
            boolean selected = currentCriterion instanceof OneParameterCriterion &&
                    ((OneParameterCriterion) currentCriterion).getValue().equals(criterionName);
            writeOption(writer, component,
                    USER_CRITERION_PREFIX + criterionName,
                    criterionName,
                    selected, null);
            if (selected)
                textCriterionSelected = true;
        }
        boolean noRecordsWithSelectedCriterion = currentCriterion instanceof OneParameterCriterion && !textCriterionSelected;
        if (noRecordsWithSelectedCriterion) {
            String criterionName = ((OneParameterCriterion) currentCriterion).getValue().toString();
            writeOption(writer, component,
                    USER_CRITERION_PREFIX + criterionName,
                    criterionName,
                    true, null);
        }

        UIComponent filteredComponent = (UIComponent) filter.getFilteredComponent();

        RenderingUtil.renderInitScript(context, new ScriptBuilder().functionCall("O$.Filters._showFilter",
                filteredComponent, clientId).semicolon(),
                new String[]{
                        ResourceUtil.getUtilJsURL(context),
                        ResourceUtil.getFiltersJsURL(context),
                        TableUtil.getTableUtilJsURL(context),
                        AbstractTableRenderer.getTableJsURL(context)}
        );

        StyleUtil.renderStyleClasses(context, component);
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
            FilterCriterion newCriterion;
            if (criterion.equals(ALL))
                newCriterion = null;
            else if (criterion.equals(EMPTY))
                newCriterion = new EmptyRecordsCriterion();
            else if (criterion.equals(NON_EMPTY))
                newCriterion = new NonEmptyRecordsCriterion();
            else
                throw new IllegalStateException("Unknown predefined criterion came from client: " + criterion);
            setDecodedCriterion(filter, newCriterion);
        } else
            throw new IllegalStateException("Improperly formatted criterion came from client: " + selectedCriterion);
    }

    protected FilterCriterion createDefaultCriterion(Filter filter, String searchString) {
        return new EqualsFilterCriterion(searchString, filter.isCaseSensitive());
    }

}

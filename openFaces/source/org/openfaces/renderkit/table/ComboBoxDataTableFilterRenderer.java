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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.ComboBoxDataTableFilter;
import org.openfaces.component.table.EmptyRecordsCriterion;
import org.openfaces.component.table.FilterCriterion;
import org.openfaces.component.table.NonEmptyRecordsCriterion;
import org.openfaces.component.table.TextFilterCriterion;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.renderkit.TableUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class ComboBoxDataTableFilterRenderer extends AbstractDataTableFilterRenderer {
    private static final String USER_CRITERION_PREFIX = "u-";
    private static final String PREDEFINED_CRITERION_PREFIX = "p-";
    private static final String ALL = "ALL";
    private static final String EMPTY = "EMPTY";
    private static final String NON_EMPTY = "NON_EMPTY";

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ComboBoxDataTableFilter filter = ((ComboBoxDataTableFilter) component);

        FilterCriterion currentCriterionName = filter.getSearchString();

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("select", component);
        String clientId = writeIdAttribute(context, component);
        writeNameAttribute(context, component);
        writer.writeAttribute("size", "1", null);
        RenderingUtil.writeStyleAndClassAttributes(writer, filter.getStyle(), filter.getStyleClass());
        String submitScript = getFilterSubmissionScript(filter, context);
        writer.writeAttribute("onchange", submitScript, null);
        writer.writeAttribute("onclick", "event.cancelBubble = true;", null);
        writer.writeAttribute("onkeydown", "O$.cancelBubble(event);", null);

        Collection<String> criterionNamesCollection = filter.calculateAllCriterionNames(context);
        boolean thereAreEmptyItems = criterionNamesCollection.contains("");
        if (thereAreEmptyItems)
            criterionNamesCollection.remove("");
        List<String> criterionNames = new ArrayList<String>(criterionNamesCollection);

        String allRecordsCriterionName = filter.getAllRecordsCriterionName();

        String predefinedCriterionsClass = getPredefinedCriterionClass(context, filter);
        writeOption(writer, component, PREDEFINED_CRITERION_PREFIX + ALL, allRecordsCriterionName,
                currentCriterionName == null,
                predefinedCriterionsClass);
        if (thereAreEmptyItems) {
            String emptyRecordsCriterionName = filter.getEmptyRecordsCriterionName();
            writeOption(writer, component, PREDEFINED_CRITERION_PREFIX + EMPTY, emptyRecordsCriterionName,
                    currentCriterionName instanceof EmptyRecordsCriterion,
                    predefinedCriterionsClass);

            String nonEmptyRecordsCriterionName = filter.getNonEmptyRecordsCriterionName();
            writeOption(writer, component, PREDEFINED_CRITERION_PREFIX + NON_EMPTY,
                    nonEmptyRecordsCriterionName,
                    currentCriterionName instanceof NonEmptyRecordsCriterion,
                    predefinedCriterionsClass);
        }

        boolean textCriterionSelected = false;
        for (String criterionName : criterionNames) {
            if (criterionName == null)
                criterionName = "";
            boolean selected = currentCriterionName instanceof TextFilterCriterion &&
                    ((TextFilterCriterion) currentCriterionName).getText().equals(criterionName);
            writeOption(writer, component,
                    USER_CRITERION_PREFIX + criterionName,
                    criterionName,
                    selected, null);
            if (selected)
                textCriterionSelected = true;
        }
        boolean noRecordsWithSelectedCriterion = currentCriterionName instanceof TextFilterCriterion && !textCriterionSelected;
        if (noRecordsWithSelectedCriterion) {
            String criterionName = ((TextFilterCriterion) currentCriterionName).getText();
            writeOption(writer, component,
                    USER_CRITERION_PREFIX + criterionName,
                    criterionName,
                    true, null);
        }

        AbstractTable table = getTable(component);

        RenderingUtil.renderInitScript(context, new ScriptBuilder().functionCall("O$._showTableFilter",
                table, clientId).semicolon(),
                new String[]{
                        ResourceUtil.getUtilJsURL(context),
                        TableUtil.getTableUtilJsURL(context),
                        AbstractTableRenderer.getTableJsURL(context)}
        );

        StyleUtil.renderStyleClasses(context, component);
        writer.endElement("select");
    }

    private AbstractTable getTable(UIComponent c) {
        for (UIComponent component = c.getParent(); component != null; component = component.getParent())
            if (component instanceof AbstractTable)
                return (AbstractTable) component;
        throw new RuntimeException("Couldn't find parent table");
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

    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String selectedCriterion = requestParameterMap.get(component.getClientId(context));
        if (selectedCriterion == null)
            return;
        ComboBoxDataTableFilter comboBoxDataTableFilter = ((ComboBoxDataTableFilter) component);
        if (selectedCriterion.startsWith(USER_CRITERION_PREFIX)) {
            String searchString = selectedCriterion.substring(USER_CRITERION_PREFIX.length());
            setDecodedSearchString(comboBoxDataTableFilter, new TextFilterCriterion(searchString));
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
            setDecodedSearchString(comboBoxDataTableFilter, newCriterion);
        } else
            throw new IllegalStateException("Improperly formatted criterion came from client: " + selectedCriterion);
    }
}

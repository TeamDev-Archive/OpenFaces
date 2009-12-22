package org.openfaces.renderkit.filter;

import org.openfaces.component.filter.CompositeFilter;
import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.CompositeFilterCriterion;
import org.openfaces.component.filter.OrFilterCriterion;
import org.openfaces.component.filter.ExpressionFilterCriterion;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.renderkit.input.DateChooserRenderer;
import org.openfaces.renderkit.input.DropDownComponentRenderer;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.RenderingUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Natalia Zolochevska
 */
public class CompositeFilterRenderer extends RendererBase implements AjaxPortionRenderer {

    private static final String NO_FILTER_ROW_SUFFIX = "no_filter";    
    private static final String NO_FILTER_TEXT_SUFFIX = "text";
    private static final String DEFAULT_NO_FILTER_TEXT_CLASS = "o_filter_row_item o_filter_no_text";


    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;

        CompositeFilter compositeFilter = (CompositeFilter) component;

        AjaxUtil.prepareComponentForAjax(context, component);

        synchronizeFilterRowsWithCriteria(compositeFilter);
        ResponseWriter writer = context.getResponseWriter();
        String clientId = compositeFilter.getClientId(context);
        writer.startElement("div", compositeFilter);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", "clearfix", null);
        writer.flush();
    }

    private void synchronizeFilterRowsWithCriteria(CompositeFilter filter) {
        CompositeFilterCriterion criteria = (CompositeFilterCriterion) filter.getValue();
        Iterator<FilterRow> rowIterator = filter.getFilterRows().iterator();
        if (criteria != null) {
            for (FilterCriterion criterion : criteria.getCriteria()) {
                if (criterion instanceof OrFilterCriterion) {
                    List<FilterCriterion> subCriteria = ((OrFilterCriterion) criterion).getCriteria();
                    for (FilterCriterion subCriterion : subCriteria)
                        synchronizeRowWithCriterion(filter, rowIterator, (ExpressionFilterCriterion) subCriterion);
                } else {
                    synchronizeRowWithCriterion(filter, rowIterator, (ExpressionFilterCriterion) criterion);
                }
            }
        }
        List<Integer> toRemove = new ArrayList<Integer>();
        while (rowIterator.hasNext()) {
            toRemove.add(rowIterator.next().getIndex());
        }
        for (Integer rowIndex : toRemove) {
            filter.removeFilterRow(rowIndex);
        }


    }

    private void synchronizeRowWithCriterion(CompositeFilter filter, Iterator<FilterRow> rowIterator, ExpressionFilterCriterion criterion) {
        FilterRow filterRow = null;
        if (rowIterator.hasNext()) {
            filterRow = rowIterator.next();
        }
        if (filterRow == null) {
            filterRow = filter.addFilterRow();
        }
        filterRow.updateRowModelFromCriterion(criterion, filter);
    }


    private void encodeNoFilterRow(FacesContext context, CompositeFilter compositeFilter) throws IOException {
        HtmlPanelGroup rowContainer = (HtmlPanelGroup) ComponentUtil.getChildBySuffix(compositeFilter, NO_FILTER_ROW_SUFFIX);
        if (rowContainer == null) {
            rowContainer = (HtmlPanelGroup) ComponentUtil.createChildComponent(context, compositeFilter, HtmlPanelGroup.COMPONENT_TYPE, NO_FILTER_ROW_SUFFIX);
            rowContainer.setLayout("block");
            rowContainer.setStyleClass(FilterRow.DEFAULT_ROW_CLASS);
            //rowContainer.setValueExpression("rendered", compositeFilter.getNoFilterRowRendererExpression());

            HtmlOutputText noFilterText = (HtmlOutputText) ComponentUtil.createChildComponent(context, rowContainer, HtmlOutputText.COMPONENT_TYPE, NO_FILTER_TEXT_SUFFIX);
            noFilterText.setStyleClass(DEFAULT_NO_FILTER_TEXT_CLASS);
            noFilterText.setValue(compositeFilter.getNoFilterMessage());

            HtmlPanelGroup addButtonContainer = (HtmlPanelGroup) ComponentUtil.createChildComponent(context, rowContainer, HtmlPanelGroup.COMPONENT_TYPE, FilterRow.ADD_BUTTON_CONTAINER_SUFFIX);
            addButtonContainer.setStyleClass(FilterRow.DEFAULT_ROW_ITEM_CLASS);
            HtmlCommandButton addButton = (HtmlCommandButton) ComponentUtil.createChildComponent(context, addButtonContainer, HtmlCommandButton.COMPONENT_TYPE, FilterRow.BUTTON_SUFFIX);
            addButton.setValue("+");            
            addButton.setOnclick("O$('"+compositeFilter.getClientId(context)+"').add(); return false;");
            addButton.setStyleClass(FilterRow.DEFAULT_ADD_BUTTON_CLASS);
        }
        rowContainer.encodeAll(context);
    }

    private void renderInitScript( FacesContext context, CompositeFilter compositeFilter) throws IOException {
        ScriptBuilder sb = new ScriptBuilder().initScript(context, compositeFilter, "O$.Filter._init");

        String[] libs = getNecessaryJsLibs(context);
        RenderingUtil.renderInitScript(context, sb, libs);
    }

    public static String getFilterJsURL(FacesContext facesContext) {
        return ResourceUtil.getInternalResourceURL(facesContext, CompositeFilterRenderer.class, "filter.js");
    }

    private String[] getNecessaryJsLibs(FacesContext context) {
        return new String[]{
                ResourceUtil.getUtilJsURL(context),
                ResourceUtil.getJsonJsURL(context),
                ResourceUtil.getInternalResourceURL(context, DropDownComponentRenderer.class, "dropdown.js"),
                ResourceUtil.getInternalResourceURL(context, DateChooserRenderer.class, "dateChooser.js"),
                getFilterJsURL(context)};
    }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        super.encodeEnd(context, component);
        CompositeFilter compositeFilter = (CompositeFilter) component;
        ResponseWriter writer = context.getResponseWriter();
        
      /*  writer.startElement("div", component);
        writer.writeAttribute("style", "clear:both;", null);
        writer.endElement("div");*/
        
        writer.endElement("div");
        renderInitScript(context, compositeFilter);

        writer.flush();
    }


    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        CompositeFilter compositeFilter = (CompositeFilter) component;        
        if (compositeFilter.isEmpty()) {
            encodeNoFilterRow(context, compositeFilter);
        } else {
            for (FilterRow filterRow : compositeFilter.getFilterRows()) {
                filterRow.encodeRow(context, compositeFilter);
            }
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public JSONObject encodeAjaxPortion(FacesContext context, UIComponent component, String portionName, JSONObject jsonParam) throws IOException, JSONException {
        CompositeFilter compositeFilter = (CompositeFilter) component;
        String operation = (String) jsonParam.get("operation");
        if (operation == null) {
        } else if (operation.equals("add")) {
            FilterRow filterRow = compositeFilter.addFilterRow();
            filterRow.encodeRow(context, compositeFilter);
        } else if (operation.equals("clear")) {
            compositeFilter.clear();
            encodeNoFilterRow(context, compositeFilter);
        } else if (operation.equals("remove")) {
            int index = (Integer) jsonParam.get("index");
            compositeFilter.removeFilterRow(index);
            if (compositeFilter.isEmpty()) {
                encodeNoFilterRow(context, compositeFilter);
            }
        } else if (operation.equals("propertyChange")) {
            int index = (Integer) jsonParam.get("index");
            FilterRow filterRow = compositeFilter.getFilterRow(index);
            filterRow.encodeOperationSelector(context, compositeFilter);
        } else if (operation.equals("operationChange")) {
            int index = (Integer) jsonParam.get("index");
            FilterRow filterRow = compositeFilter.getFilterRow(index);
            filterRow.encodeParametersEditor(context, compositeFilter);
        }
        return null;
    }
}

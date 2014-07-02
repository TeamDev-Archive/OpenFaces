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
package org.openfaces.component;

// todo: resolve datatable package dependencies

import org.openfaces.component.table.Cell;
import org.openfaces.component.table.Columns;
import org.openfaces.component.table.Row;
import org.openfaces.component.table.impl.DynamicCol;
import org.openfaces.renderkit.table.CustomCellRenderingInfo;
import org.openfaces.renderkit.table.CustomContentCellRenderingInfo;
import org.openfaces.renderkit.table.CustomRowRenderingInfo;
import org.openfaces.renderkit.table.TableStructure;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.ContextCallback;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UniqueIdVendor;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;
import javax.servlet.jsp.jstl.sql.Result;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is a modified version of the UIData class copied from MyFaces 2.0.2.
 * Here's a list of modifications from the original version:
 * <ul>
 * <li>MOD-1: Added the notion of unavailableRowIndexes, for which the processDecodes, processValidations, and processUpdates phases are not performed</li>
 * <li>MOD-2: Renamed the "value" property to "uiDataValue" in order make another "value" property in an ancestor</li>
 * <li>MOD-3: Changed mechanism for rendered column iteration to account for AbstractTable.columnsOrder attribute:
 * The list of column is now retrieved using the new getColumnsForProcessing method instead of using the getChildren</li>
 * <li>MOD-4: Include custom cell contents into phase processing (JSFC-3087)</li>
 * <li>MOD-5: Execute all pre-rendering phases for dynamic columns created with <o:columns> so that each
 * dynamic column was decoded with correct column-specific parameters.</li>
 * <li>MOD-6: Extended OUIData from UIData to overcome JBossSeam compatibility problem of s:link not
 * having access to the current row data variable. See JSFC-2585.</li>
 * <li>MOD-7: Extended OUIData from OUIComponent and supported all appropriate properties.</li>
 * <li>MOD-8: Fix for "this method is here only to maintain binary compatibility w/ the RI" error when running under JSF RI
 * <li>MOD-9: Removed the getClientId method to use the ones inherited from the parent UIData.
 * The reason is that the one copied from MyFaces invoke super.getClientId() method and expect that a method from the
 * UIComponentBase class is invoked, which is not the case, so we just use the appropriate method inherited from the parent UIData class.
 * <li>MOD-10: Replaced super.getClientId() invokation in invokeOnComponent onto its replacement super_getClientId()</li>
 * <li>MOD-11: Extended visitTree method to account for extensions that are made through component's child tags, in
 * particular for state saving for such tags as <o:singleRowSelection> to work properly.</li>
 * <li>MOD-12: Adjusted row state saving for dynamic component creation in filter components</li>
 * <li>MOD-13: Made PropertyKeys enumeration protected to allow state manipulation in AbstractTable.java</li>
 * <li>MOD-14: Adjust state saving  for situations when child components add sub-components dynamically</li>
 * </ul>
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * Represents an abstraction of a component which has multiple "rows" of data.
 * <p>
 * The children of this component are expected to be UIColumn components.
 * <p>
 * Note that the same set of child components are reused to implement each row of the table in turn during such phases
 * as apply-request-values and render-response. Altering any of the members of these components therefore affects the
 * attribute for every row, except for the following members:
 * <ul>
 * <li>submittedValue
 * <li>value (where no EL binding is used)
 * <li>valid
 * </ul>
 * <p>
 * This reuse of the child components also means that it is not possible to save a reference to a component during table
 * processing, then access it later and expect it to still represent the same row of the table.
 * <h1>
 * Implementation Notes</h1>
 * <p>
 * Each of the UIColumn children of this component has a few component children of its own to render the contents of the
 * table cell. However there can be a very large number of rows in a table, so it isn't efficient for the UIColumn and
 * all its child objects to be duplicated for each row in the table. Instead the "flyweight" pattern is used where a
 * serialized state is held for each row. When setRowIndex is invoked, the UIColumn objects and their children serialize
 * their current state then reinitialise themselves from the appropriate saved state. This allows a single set of real
 * objects to represent multiple objects which have the same types but potentially different internal state. When a row
 * is selected for the first time, its state is set to a clean "initial" state. Transient components (including any
 * read-only component) do not save their state; they are just reinitialised as required. The state saved/restored when
 * changing rows is not the complete component state, just the fields that are expected to vary between rows:
 * "submittedValue", "value", "isValid".
 * </p>
 * <p>
 * Note that a table is a "naming container", so that components within the table have their ids prefixed with the id of
 * the table. Actually, when setRowIndex has been called on a table with id of "zzz" the table pretends to its children
 * that its ID is "zzz_n" where n is the row index. This means that renderers for child components which call
 * component.getClientId automatically get ids of form "zzz_n:childId" thus ensuring that components in different rows
 * of the table get different ids.
 * </p>
 * <p>
 * When decoding a submitted page, this class iterates over all its possible rowIndex values, restoring the appropriate
 * serialized row state then calling processDecodes on the child components. Because the child components (or their
 * renderers) use getClientId to get the request key to look for parameter data, and because this object pretends to
 * have a different id per row ("zzz_n") a single child component can decode data from each table row in turn without
 * being aware that it is within a table. The table's data model is updated before each call to child.processDecodes, so
 * the child decode method can assume that the data model's rowData points to the model object associated with the row
 * currently being decoded. Exactly the same process applies for the later validation and updateModel phases.
 * </p>
 * <p>
 * When the data model for the table is bound to a backing bean property, and no validation errors have occured during
 * processing of a postback, the data model is refetched at the start of the rendering phase (ie after the update model
 * phase) so that the contents of the data model can be changed as a result of the latest form submission. Because the
 * saved row state must correspond to the elements within the data model, the row state must be discarded whenever a new
 * data model is fetched; not doing this would cause all sorts of inconsistency issues. This does imply that changing
 * the state of any of the members "submittedValue", "value" or "valid" of a component within the table during the
 * invokeApplication phase has no effect on the rendering of the table. When a validation error has occurred, a new
 * DataModel is <i>not</i> fetched, and the saved state of the child components is <i>not</i> discarded.
 * </p>
 * see Javadoc of the <a href="http://java.sun.com/j2ee/javaserverfaces/1.2/docs/api/index.html">JSF Specification</a>
 * for more information.
 *
 * @author Manfred Geiler (latest modification by $Author: jakobk $)
 * @version $Revision: 933753 $ $Date: 2010-04-13 14:19:14 -0500 (Tue, 13 Apr 2010) $
 */
public class OUIData extends UIData implements NamingContainer, UniqueIdVendor, OUIComponent // <MOD-6/> Extended UIData instead of UIComponentBase, <MOD-7/>
{
    public static final String COMPONENT_FAMILY = "javax.faces.Data";
    public static final String COMPONENT_TYPE = "javax.faces.Data"; // for unit tests

    private static final String FOOTER_FACET_NAME = "footer";
    private static final String HEADER_FACET_NAME = "header";
    private static final Class<Object[]> OBJECT_ARRAY_CLASS = Object[].class;
    private static final int PROCESS_DECODES = 1;
    private static final int PROCESS_VALIDATORS = 2;
    private static final int PROCESS_UPDATES = 3;

    private int _rowIndex = -1;

    // Holds for each row the states of the child components of this UIData.
    // Note that only "partial" component state is saved: the component fields
    // that are expected to vary between rows.
    private Map<String, Collection<Object[]>> _rowStates = new HashMap<String, Collection<Object[]>>();
    private Map<String, Object> dynamicColumnStates = new HashMap<String, Object>(); // <MOD-5/> added

    /**
     * Handle case where this table is nested inside another table. See method getDataModel for more details.
     * <p/>
     * Key: parentClientId (aka rowId when nested within a parent table) Value: DataModel
     */
    private Map<String, DataModel> _dataModelMap = new HashMap<String, DataModel>();

    // will be set to false if the data should not be refreshed at the beginning of the encode phase
    private boolean _isValidChilds = true;

    private Object _initialDescendantComponentState = null;
    private List<Object> initialDynamicColumnsState; // <MOD-5/> added

    //private int _first;
    //private boolean _firstSet;
    //private int _rows;
    //private boolean _rowsSet;
    //private Object _value;

    private static class FacesEventWrapper extends FacesEvent {
        private static final long serialVersionUID = 6648047974065628773L;
        private FacesEvent _wrappedFacesEvent;
        private int _rowIndex;

        public FacesEventWrapper(FacesEvent facesEvent, int rowIndex, UIData redirectComponent) {
            super(redirectComponent);
            _wrappedFacesEvent = facesEvent;
            _rowIndex = rowIndex;
        }

        @Override
        public PhaseId getPhaseId() {
            return _wrappedFacesEvent.getPhaseId();
        }

        @Override
        public void setPhaseId(PhaseId phaseId) {
            _wrappedFacesEvent.setPhaseId(phaseId);
        }

        @Override
        public void queue() {
            _wrappedFacesEvent.queue();
        }

        @Override
        public String toString() {
            return _wrappedFacesEvent.toString();
        }

        @Override
        public boolean isAppropriateListener(FacesListener faceslistener) {
            return _wrappedFacesEvent.isAppropriateListener(faceslistener);
        }

        @Override
        public void processListener(FacesListener faceslistener) {
            _wrappedFacesEvent.processListener(faceslistener);
        }

        public FacesEvent getWrappedFacesEvent() {
            return _wrappedFacesEvent;
        }

        public int getRowIndex() {
            return _rowIndex;
        }
    }

    private static final DataModel EMPTY_DATA_MODEL = new DataModel() {
        @Override
        public boolean isRowAvailable() {
            return false;
        }

        @Override
        public int getRowCount() {
            return 0;
        }

        @Override
        public Object getRowData() {
            throw new IllegalArgumentException();
        }

        @Override
        public int getRowIndex() {
            return -1;
        }

        @Override
        public void setRowIndex(int i) {
            if (i < -1)
                throw new IllegalArgumentException();
        }

        @Override
        public Object getWrappedData() {
            return null;
        }

        @Override
        public void setWrappedData(Object obj) {
            if (obj == null) {
                return; // Clearing is allowed
            }
            throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
        }
    };

    private static class EditableValueHolderState { // <MOD-5/> was a non-static class
        private final Object _value;
        private final boolean _localValueSet;
        private final boolean _valid;
        private final Object _submittedValue;

        public EditableValueHolderState(EditableValueHolder evh) {
            _value = evh.getLocalValue();
            _localValueSet = evh.isLocalValueSet();
            _valid = evh.isValid();
            _submittedValue = evh.getSubmittedValue();
        }

        public void restoreState(EditableValueHolder evh) {
            evh.setValue(_value);
            evh.setLocalValueSet(_localValueSet);
            evh.setValid(_valid);
            evh.setSubmittedValue(_submittedValue);
        }
    }

    /**
     * Construct an instance of the UIData.
     */
    public OUIData() {
        setRendererType("javax.faces.Table");
    }


    @Override
    public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback)
        throws FacesException
    {
        if (context == null || clientId == null || callback == null)
        {
            throw new NullPointerException();
        }

        // searching for this component?
        boolean returnValue = this.getClientId(context).equals(clientId);

        if (returnValue)
        {
            try
            {
                callback.invokeContextCallback(context, this);
            }
            catch (Exception e)
            {
                throw new FacesException(e);
            }
            return returnValue;
        }

        // Now Look throught facets on this UIComponent
        for (Iterator<UIComponent> it = this.getFacets().values().iterator(); !returnValue && it.hasNext();)
        {
            returnValue = it.next().invokeOnComponent(context, clientId, callback);
        }

        if (returnValue == true)
        {
            return returnValue;
        }

        // Now we have to check if it is searching an inner component
        String baseClientId = super_getClientId(context); // <MOD-10/> was: super.getClientId(context)

        // is the component an inner component?
        if (clientId.startsWith(baseClientId))
        {
            // Check if the clientId for the component, which we
            // are looking for, has a rowIndex attached
            if (clientId.matches(baseClientId + ":[0-9]+:.*"))
            {
                String subId = clientId.substring(baseClientId.length() + 1);
                String clientRow = subId.substring(0, subId.indexOf(':'));

                //Now we save the current position
                int oldRow = this.getRowIndex();

                // try-finally --> make sure, that the old row index is restored
                try
                {
                    //The conversion is safe, because its already checked on the
                    //regular expresion
                    this.setRowIndex(Integer.parseInt(clientRow));

                    // check, if the row is available
                    if (!isRowAvailable())
                    {
                        return false;
                    }

                    for (Iterator<UIComponent> it1 = getChildren().iterator();
                            !returnValue && it1.hasNext();)
                    {
                        //recursive call to find the component
                        returnValue = it1.next().invokeOnComponent(context, clientId, callback);
                    }
                }
                finally
                {
                    //Restore the old position. Doing this prevent
                    //side effects.
                    this.setRowIndex(oldRow);
                }
            }
            else
            {
                // MYFACES-2370: search the component in the childrens' facets too.
                // We have to check the childrens' facets here, because in MyFaces
                // the rowIndex is not attached to the clientId for the children of
                // facets of the UIColumns. However, in RI the rowIndex is
                // attached to the clientId of UIColumns' Facets' children.
                for (Iterator<UIComponent> itChildren = this.getChildren().iterator();
                        !returnValue && itChildren.hasNext();)
                {
                    // process the child's facets
                    for (Iterator<UIComponent> itChildFacets = itChildren.next().getFacets().values().iterator();
                            !returnValue && itChildFacets.hasNext();)
                    {
                        //recursive call to find the component
                        returnValue = itChildFacets.next().invokeOnComponent(context, clientId, callback);
                    }
                }
            }
        }

        return returnValue;
    }

    // <MOD-10> (added)
    private String super_getClientId(FacesContext context) {
        String clientId = getClientId(context);
        int rowIndex = getRowIndex();
        if (rowIndex == -1) return clientId;
        String suffix = "" + UINamingContainer.getSeparatorChar(context) + rowIndex;
        if (!clientId.endsWith(suffix)) throw new IllegalStateException(
                "clientId \"" + clientId + "\" is expected to end with \"" +suffix + "\"");
        return clientId.substring(0, clientId.length() - suffix.length());
    }
    // </MOD-10>

    public void setFooter(UIComponent footer) {
        getFacets().put(FOOTER_FACET_NAME, footer);
    }

    public UIComponent getFooter() {
        return getFacets().get(FOOTER_FACET_NAME);
    }

    public void setHeader(UIComponent header) {
        getFacets().put(HEADER_FACET_NAME, header);
    }

    public UIComponent getHeader() {
        return getFacets().get(HEADER_FACET_NAME);
    }

    public boolean isRowAvailable() {
        return getDataModel().isRowAvailable();
    }

    public int getRowCount() {
        return getDataModel().getRowCount();
    }

    public Object getRowData() {
        return getDataModel().getRowData();
    }

    public int getRowIndex() {
        return _rowIndex;
    }

    /**
     * Set the current row index that methods like getRowData use.
     * <p/>
     * Param rowIndex can be -1, meaning "no row".
     * <p/>
     *
     * @param rowIndex
     */
    public void setRowIndex(int rowIndex) {
        if (rowIndex < -1) {
            throw new IllegalArgumentException("rowIndex is less than -1");
        }

        if (_rowIndex == rowIndex) {
            return;
        }

        FacesContext facesContext = getFacesContext();

        if (_rowIndex == -1) {
            if (_initialDescendantComponentState == null) {
                // Create a template that can be used to initialise any row
                // that we haven't visited before, ie a "saved state" that can
                // be pushed to the "restoreState" method of all the child
                // components to set them up to represent a clean row.
                _initialDescendantComponentState = saveDescendantComponentStates(getChildren().iterator(), false);
                initialDynamicColumnsState = saveDynamicColumnsState(); // <MOD-5/> (added)
            }
        } else {
            // We are currently positioned on some row, and are about to
            // move off it, so save the (partial) state of the components
            // representing the current row. Later if this row is revisited
            // then we can restore this state.
            _rowStates.put(getClientId(facesContext), saveDescendantComponentStates(getChildren().iterator(), false));
            dynamicColumnStates.put(getClientId(facesContext), saveDynamicColumnsState()); // <MOD-5/> (added)
        }

        _rowIndex = rowIndex;

        DataModel dataModel = getDataModel();
        dataModel.setRowIndex(rowIndex);

        String var = (String) getStateHelper().get(PropertyKeys.var);
        if (rowIndex == -1) {
            if (var != null) {
                facesContext.getExternalContext().getRequestMap().remove(var);
            }
        } else {
            if (var != null) {
                if (isRowAvailable()) {
                    Object rowData = dataModel.getRowData();
                    facesContext.getExternalContext().getRequestMap().put(var, rowData);
                } else {
                    facesContext.getExternalContext().getRequestMap().remove(var);
                }
            }
        }

        if (_rowIndex == -1) {
            // reset components to initial state
            restoreDescendantComponentStates(getChildren().iterator(), _initialDescendantComponentState, false);
            restoreDynamicColumnsState(initialDynamicColumnsState); // <MOD-5/> (added)
        } else {
            Object rowState = _rowStates.get(getClientId(facesContext));
            List columnStates = (List) dynamicColumnStates.get(getClientId(facesContext)); // <MOD-5/> (added)
            if (rowState == null) {
                // We haven't been positioned on this row before, so just
                // configure the child components of this component with
                // the standard "initial" state
                restoreDescendantComponentStates(getChildren().iterator(), _initialDescendantComponentState, false);
                restoreDynamicColumnsState(initialDynamicColumnsState); // <MOD-5/> (added)
            } else {
                // We have been positioned on this row before, so configure
                // the child components of this component with the (partial)
                // state that was previously saved. Fields not in the
                // partial saved state are left with their original values.
                restoreDescendantComponentStates(getChildren().iterator(), rowState, false);
                restoreDynamicColumnsState(columnStates); // <MOD-5/> (added)
            }
        }
    }

    /**
     * Overwrite the state of the child components of this component with data previously saved by method
     * saveDescendantComponentStates.
     * <p/>
     * The saved state info only covers those fields that are expected to vary between rows of a table. Other fields are
     * not modified.
     */
    @SuppressWarnings("unchecked")
    public static void restoreDescendantComponentStates(Iterator<UIComponent> childIterator, Object state, // <MOD-5/> was just a non-static private declaration
                                                        boolean restoreChildFacets) {
        Iterator<? extends Object[]> descendantStateIterator = null;
        while (childIterator.hasNext()) {
            if (descendantStateIterator == null && state != null) {
                descendantStateIterator = ((Collection<? extends Object[]>) state).iterator();
            }
            UIComponent component = childIterator.next();

            // reset the client id (see spec 3.1.6)
            component.setId(component.getId());
            if (!component.isTransient()) {
                Object childState = null;
                Object descendantState = null;
                if (descendantStateIterator != null && descendantStateIterator.hasNext()) {
                    Object[] object = descendantStateIterator.next();
                    childState = object[0];
                    descendantState = object[1];
                }
                if (childState != null && component instanceof EditableValueHolder) { // <MOD-14/> was just component instanceof EditableValueHolder
                    ((EditableValueHolderState) childState).restoreState((EditableValueHolder) component);
                }
                Iterator<UIComponent> childsIterator;
                if (restoreChildFacets) {
                    childsIterator = component.getFacetsAndChildren();
                } else {
                    childsIterator = component.getChildren().iterator();
                }
                restoreDescendantComponentStates(childsIterator, descendantState, true);
            }
        }
    }

    /**
     * Walk the tree of child components of this UIData, saving the parts of their state that can vary between rows.
     * <p/>
     * This is very similar to the process that occurs for normal components when the view is serialized. Transient
     * components are skipped (no state is saved for them).
     * <p/>
     * If there are no children then null is returned. If there are one or more children, and all children are transient
     * then an empty collection is returned; this will happen whenever a table contains only read-only components.
     * <p/>
     * Otherwise a collection is returned which contains an object for every non-transient child component; that object
     * may itself contain a collection of the state of that child's child components.
     */
    public static Collection<Object[]> saveDescendantComponentStates(Iterator<UIComponent> childIterator, // <MOD-5/> was just a non-static private declaration
                                                                     boolean saveChildFacets) {
        Collection<Object[]> childStates = null;
        while (childIterator.hasNext()) {
            if (childStates == null) {
                childStates = new ArrayList<Object[]>();
            }

            UIComponent child = childIterator.next();
            if (!child.isTransient()) {
                // <MOD-12> added
                if (child instanceof CompoundComponent) {
                    ((CompoundComponent) child).createSubComponents(FacesContext.getCurrentInstance());
                }
                // </MOD-12>
                // Add an entry to the collection, being an array of two
                // elements. The first element is the state of the children
                // of this component; the second is the state of the current
                // child itself.

                Iterator<UIComponent> childsIterator;
                if (saveChildFacets) {
                    childsIterator = child.getFacetsAndChildren();
                } else {
                    childsIterator = child.getChildren().iterator();
                }
                Object descendantState = saveDescendantComponentStates(childsIterator, true);
                Object state = null;
                if (child instanceof EditableValueHolder) {
                    state = new EditableValueHolderState((EditableValueHolder) child);
                }
                childStates.add(new Object[]{state, descendantState});
            }
        }
        return childStates;
    }

    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if (name == null) {
            throw new NullPointerException("name");
        } else if (name.equals("uiDataValue")) // <MOD-2/> "value" was here
        {
            _dataModelMap.clear();
        } else if (name.equals("rowIndex")) {
            throw new IllegalArgumentException("name " + name);
        }
        super.setValueExpression(name, binding);
    }

    // <MOD-9/> Removed the getClientId method (using the inherited one)

    /**
     * Modify events queued for any child components so that the UIData state will be correctly configured before the
     * event's listeners are executed.
     * <p/>
     * Child components or their renderers may register events against those child components. When the listener for
     * that event is eventually invoked, it may expect the uidata's rowData and rowIndex to be referring to the same
     * object that caused the event to fire.
     * <p/>
     * The original queueEvent call against the child component has been forwarded up the chain of ancestors in the
     * standard way, making it possible here to wrap the event in a new event whose source is <i>this</i> component, not
     * the original one. When the event finally is executed, this component's broadcast method is invoked, which ensures
     * that the UIData is set to be at the correct row before executing the original event.
     */
    @Override
    public void queueEvent(FacesEvent event) {
        super.queueEvent(new FacesEventWrapper(event, getRowIndex(), this));
    }

    /**
     * Ensure that before the event's listeners are invoked this UIData component's "current row" is set to the row
     * associated with the event.
     * <p/>
     * See queueEvent for more details.
     */
    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (event instanceof FacesEventWrapper) {
            FacesEvent originalEvent = ((FacesEventWrapper) event).getWrappedFacesEvent();
            int eventRowIndex = ((FacesEventWrapper) event).getRowIndex();
            int currentRowIndex = getRowIndex();
            setRowIndex(eventRowIndex);
            try {
                originalEvent.getComponent().broadcast(originalEvent);
            }
            finally {
                setRowIndex(currentRowIndex);
            }
        } else {
            super.broadcast(event);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.0
     */
    public String createUniqueId(FacesContext context, String seed) {
        ExternalContext extCtx = context.getExternalContext();
        StringBuilder bld = new StringBuilder();

        Long uniqueIdCounter = (Long) getStateHelper().get(PropertyKeys.uniqueIdCounter);
        uniqueIdCounter = (uniqueIdCounter == null) ? 0 : uniqueIdCounter;
        getStateHelper().put(PropertyKeys.uniqueIdCounter, (uniqueIdCounter + 1L));
        // Generate an identifier for a component. The identifier will be prefixed with UNIQUE_ID_PREFIX, and will be unique within this UIViewRoot.
        if (seed == null) {
            return extCtx.encodeNamespace(bld.append(UIViewRoot.UNIQUE_ID_PREFIX).append(uniqueIdCounter).toString());
        }
        // Optionally, a unique seed value can be supplied by component creators which should be included in the generated unique id.
        else {
            return extCtx.encodeNamespace(bld.append(UIViewRoot.UNIQUE_ID_PREFIX).append(seed).toString());
        }
    }

    /**
     * Perform necessary actions when rendering of this component starts, before delegating to the inherited
     * implementation which calls the associated renderer's encodeBegin method.
     */
    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        _initialDescendantComponentState = null;
        initialDynamicColumnsState = null; // <MOD-5/> (added)
        if (_isValidChilds && !hasErrorMessages(context)) {
            // Clear the data model so that when rendering code calls
            // getDataModel a fresh model is fetched from the backing
            // bean via the value-binding.
            _dataModelMap.clear();

            // When the data model is cleared it is also necessary to
            // clear the saved row state, as there is an implicit 1:1
            // relation between objects in the _rowStates and the
            // corresponding DataModel element.
            _rowStates.clear();
            dynamicColumnStates.clear(); // <MOD-5/> (added)
        }
        resetDynamicColumnsStates(_isValidChilds && !hasErrorMessages(context)); // <MOD-5/> (added)
        super.encodeBegin(context);
    }

    private boolean hasErrorMessages(FacesContext context) {
        for (Iterator<FacesMessage> iter = context.getMessages(); iter.hasNext();) {
            FacesMessage message = iter.next();
            if (FacesMessage.SEVERITY_ERROR.compareTo(message.getSeverity()) <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see javax.faces.component.UIComponentBase#encodeEnd(javax.faces.context.FacesContext)
     */
    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        setRowIndex(-1);
        super.encodeEnd(context);
    }

    @Override
    public void processDecodes(FacesContext context) {
        if (context == null) {
            throw new NullPointerException("context");
        }
        if (!isRendered()) {
            return;
        }
        setRowIndex(-1);
        processFacets(context, PROCESS_DECODES);
        processColumnFacets(context, PROCESS_DECODES);
        processColumnChildren(context, PROCESS_DECODES);
        setRowIndex(-1);
        try {
            decode(context);
        }
        catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }
    }

    @Override
    public void processValidators(FacesContext context) {
        if (context == null) {
            throw new NullPointerException("context");
        }

        if (!isRendered()) {
            return;
        }

        setRowIndex(-1);
        processFacets(context, PROCESS_VALIDATORS);
        processColumnFacets(context, PROCESS_VALIDATORS);
        processColumnChildren(context, PROCESS_VALIDATORS);
        setRowIndex(-1);

        // check if an validation error forces the render response for our data
        if (context.getRenderResponse()) {
            _isValidChilds = false;
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        if (context == null) {
            throw new NullPointerException("context");
        }
        if (!isRendered()) {
            return;
        }
        setRowIndex(-1);
        processFacets(context, PROCESS_UPDATES);
        processColumnFacets(context, PROCESS_UPDATES);
        processColumnChildren(context, PROCESS_UPDATES);
        setRowIndex(-1);

        if (context.getRenderResponse()) {
            _isValidChilds = false;
        }
    }

    private void processFacets(FacesContext context, int processAction) {
        for (UIComponent facet : getFacets().values()) {
            process(context, facet, processAction);
        }
    }

    /**
     * Invoke the specified phase on all facets of all UIColumn children of this component. Note that no methods are
     * called on the UIColumn child objects themselves.
     *
     * @param context       is the current faces context.
     * @param processAction specifies a JSF phase: decode, validate or update.
     */
    private void processColumnFacets(FacesContext context, int processAction) {
        for (UIComponent child : (Iterable<? extends UIComponent>) getColumnsForProcessing())// <MOD-3/> getChildren().iterator() was here
        {
            if (child instanceof UIColumn) {
                if (!child.isRendered()) {
                    // Column is not visible
                    continue;
                }

                // <MOD-5>
                Map<String, UIComponent> colFacets;
                Runnable restoreVariables = null;
                if (child instanceof DynamicCol) {
                    DynamicCol dynamicCol = (DynamicCol) child;
                    restoreVariables = dynamicCol.enterComponentContext();
                    colFacets = dynamicCol.getFacetsForProcessing();
                } else
                    colFacets = child.getFacets();
                // </MOD-5>

                for (UIComponent facet : colFacets.values()) {// <MOD-5/> changed "child.getFacets().values()" to "colFacets.values()"
                    process(context, facet, processAction);
                }

                // <MOD-5>
                if (restoreVariables != null) restoreVariables.run();
                // </MOD-5>
            }
        }
    }

    /**
     * Invoke the specified phase on all non-facet children of all UIColumn children of this component. Note that no
     * methods are called on the UIColumn child objects themselves.
     *
     * @param context       is the current faces context.
     * @param processAction specifies a JSF phase: decode, validate or update.
     */
    private void processColumnChildren(FacesContext context, int processAction) {
        int first = getFirst();
        int rows = getRows();
        int last;
        if (rows == 0) {
            last = getRowCount();
        } else {
            last = first + rows;
        }
        // <MOD-4>
        List customRows = getCustomRows();
        Map<Integer, CustomRowRenderingInfo> customRowRenderingInfos = (Map) getAttributes().get(TableStructure.CUSTOM_ROW_RENDERING_INFOS_KEY);
        // </MOD-4>
        for (int rowIndex = first; last == -1 || rowIndex < last; rowIndex++) {
            // <MOD-1> code added
            if (!isRowAvailableAfterRestoring(rowIndex))
                continue;
            // </MOD-1>
            setRowIndex(rowIndex);

            // scrolled past the last row
            if (!isRowAvailable()) {
                break;
            }

            // <MOD-4>
            CustomRowRenderingInfo customRowRenderingInfo = customRowRenderingInfos.get(rowIndex);
            // </MOD-4>

            int colIndex = 0;
            List<UIComponent> columnsForProcessing = getColumnsForProcessing(); // <MOD-3/> line added
            for (UIComponent child : columnsForProcessing) // <MOD-3/> getChildren() was here
            {
                if (child instanceof UIColumn) {
                    if (!child.isRendered()) {
                        // Column is not visible
                        continue;
                    }

                    // <MOD-4>
                    if (customRowRenderingInfo != null && customRowRenderingInfo.getCustomCellRenderingInfo(colIndex) != null) {
                        continue;
                    }
                    // </MOD-4>

                    // <MOD-5>
                    List<UIComponent> colChildren;
                    Runnable restoreVariables = null;
                    if (child instanceof DynamicCol) {
                        DynamicCol dynamicCol = (DynamicCol) child;
                        restoreVariables = dynamicCol.enterComponentContext();
                        colChildren = dynamicCol.getChildrenForProcessing();
                    } else
                        colChildren = child.getChildren();
                    // </MOD-5>

                    for (UIComponent columnChild : colChildren)// <MOD-5/> changed "child.getChildren()" -> "colChildren"
                    {
                        process(context, columnChild, processAction);
                    }

                    // <MOD-5>
                    if (child instanceof DynamicCol)
                        if (restoreVariables != null) restoreVariables.run();
                    // </MOD-5>
                }
            }
            // <MOD-4>
            if (customRowRenderingInfo != null) {
                List<ClientBehavior> a4jSupportBehaviors = customRowRenderingInfo.getA4jAjaxBehaviorsForThisRow(customRows);
                for (ClientBehavior a4jAjax : a4jSupportBehaviors) {
//                    process(context, a4jAjax, processAction); todo: resolve what should be done with a4jAjax behavior here
                }
                for (int i = 0, count = columnsForProcessing.size(); i < count; i++) {
                    CustomCellRenderingInfo customCellRenderingInfo = customRowRenderingInfo.getCustomCellRenderingInfo(i);
                    if (customCellRenderingInfo == null || (!(customCellRenderingInfo instanceof CustomContentCellRenderingInfo)))
                        continue;
                    Cell customCell = ((CustomContentCellRenderingInfo) customCellRenderingInfo).findTableCell(customRows);
                    if (customCell != null)
                        process(context, customCell, processAction);
                }
            }
            // </MOD-4>

        }
    }

    private void process(FacesContext context, UIComponent component, int processAction) {
        switch (processAction) {
            case PROCESS_DECODES:
                component.processDecodes(context);
                break;
            case PROCESS_VALIDATORS:
                component.processValidators(context);
                break;
            case PROCESS_UPDATES:
                component.processUpdates(context);
                break;
        }
    }

    /**
     * Return the datamodel for this table, potentially fetching the data from a backing bean via a value-binding if
     * this is the first time this method has been called.
     * <p/>
     * This is complicated by the fact that this table may be nested within another table. In this case a different
     * datamodel should be fetched for each row. When nested within a parent table, the parent reference won't change
     * but parent.getClientId() will, as the suffix changes depending upon the current row index. A map object on this
     * component is therefore used to cache the datamodel for each row of the table. In the normal case where this table
     * is not nested inside a component that changes its id (like a table does) then this map only ever has one entry.
     */
    protected DataModel getDataModel() {
        DataModel dataModel;
        String clientID = "";

        UIComponent parent = getParent();
        if (parent != null) {
            clientID = parent.getClientId(getFacesContext());
        }
        dataModel = _dataModelMap.get(clientID);
        if (dataModel == null) {
            dataModel = createDataModel();
            _dataModelMap.put(clientID, dataModel);
        }
        return dataModel;
    }

    protected void setDataModel(DataModel dataModel) {
        // <MOD-8> UnsupportedOperationException was thrown here
        String clientID = "";
        UIComponent parent = getParent();
        if (parent != null) {
            clientID = parent.getClientId(getFacesContext());
        }
        _dataModelMap.put(clientID, dataModel);
        // </MOD-8>
    }

    /**
     * Evaluate this object's value property and convert the result into a DataModel. Normally this object's value
     * property will be a value-binding which will cause the value to be fetched from some backing bean.
     * <p/>
     * The result of fetching the value may be a DataModel object, in which case that object is returned directly. If
     * the value is of type List, Array, ResultSet, Result, other object or null then an appropriate wrapper is created
     * and returned.
     * <p/>
     * Null is never returned by this method.
     */
    private DataModel createDataModel() {
        Object value = getUiDataValue(); // <MOD-2/> "getValue()" was here

        if (value == null) {
            return EMPTY_DATA_MODEL;
        } else if (value instanceof DataModel) {
            return (DataModel) value;
        } else if (value instanceof List) {
            return new ListDataModel((List<?>) value);
        } else if (OBJECT_ARRAY_CLASS.isAssignableFrom(value.getClass())) {
            return new ArrayDataModel((Object[]) value);
        } else if (value instanceof ResultSet) {
            return new ResultSetDataModel((ResultSet) value);
        } else if (value instanceof Result) {
            return new ResultDataModel((Result) value);
        } else {
            return new ScalarDataModel(value);
        }
    }

    /**
     * An EL expression that specifies the data model that backs this table.
     * <p>
     * The value referenced by the EL expression can be of any type.
     * </p>
     * <ul>
     * <li>A value of type DataModel is used directly.</li>
     * <li>Array-like parameters of type array-of-Object, java.util.List, java.sql.ResultSet or
     * javax.servlet.jsp.jstl.sql.Result are wrapped in a corresponding DataModel that knows how to iterate over the
     * elements.</li>
     * <li>Other values are wrapped in a DataModel as a single row.</li>
     * </ul>
     * <p>
     * Note in particular that unordered collections, eg Set are not supported. Therefore if the value expression
     * references such an object then the table will be considered to contain just one element - the collection itself.
     * </p>
     */
    public Object getUiDataValue() // <MOD-2/> method name was "getValue()"
    {
        return getStateHelper().eval(PropertyKeys.uiDataValue);
    }

    public void setUiDataValue(Object value) // <MOD-2/> method name was "setValue"
    {
        getStateHelper().put(PropertyKeys.uiDataValue, value);
        _dataModelMap.clear();
        _rowStates.clear();
        dynamicColumnStates.clear(); // <MOD-5/> (added)
        _isValidChilds = true;
    }

    /**
     * Defines the index of the first row to be displayed, starting from 0.
     */
    public int getFirst() {
        return (Integer) getStateHelper().eval(PropertyKeys.first, 0);
    }

    public void setFirst(int first) {
        if (first < 0) {
            throw new IllegalArgumentException("Illegal value for first row: " + first);
        }
        getStateHelper().put(PropertyKeys.first, first);
    }

    /**
     * Defines the maximum number of rows of data to be displayed.
     * <p>
     * Specify zero to display all rows from the "first" row to the end of available data.
     * </p>
     */
    public int getRows() {
        return (Integer) getStateHelper().eval(PropertyKeys.rows, 0);
    }

    /**
     * Set the maximum number of rows displayed in the table.
     */
    public void setRows(int rows) {
        if (rows < 0) {
            throw new IllegalArgumentException("rows: " + rows);
        }
        getStateHelper().put(PropertyKeys.rows, rows);
    }

    /**
     * Defines the name of the request-scope variable that will hold the current row during iteration.
     * <p>
     * During rendering of child components of this UIData, the variable with this name can be read to learn what the
     * "rowData" object for the row currently being rendered is.
     * </p>
     * <p>
     * This value must be a static value, ie an EL expression is not permitted.
     * </p>
     */
    public String getVar() {
        return (String) getStateHelper().get(PropertyKeys.var);
    }

    /**
     * Overrides the behavior in
     * UIComponent.visitTree(javax.faces.component.visit.VisitContext, javax.faces.component.visit.VisitCallback)
     * to handle iteration correctly.
     *
     * @param context  the visit context which handles the processing details
     * @param callback the callback to be performed
     * @return false if the processing is not done true if we can shortcut
     *         the visiting because we are done with everything
     * @since 2.0
     */
    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (!isVisitable(context)) {
            return false;
        }

        // save the current row index
        int oldRowIndex = getRowIndex();
        // set row index to -1 to process the facets and to get the rowless clientId
        setRowIndex(-1);
        // push the Component to EL
        pushComponentToEL(context.getFacesContext(), this);
        try {
            VisitResult visitResult = context.invokeVisitCallback(this,
                    callback);
            switch (visitResult) {
                //we are done nothing has to be processed anymore
                case COMPLETE:
                    return true;

                case REJECT:
                    return false;

                //accept
                default:
                    // determine if we need to visit our children
                    Collection<String> subtreeIdsToVisit = context
                            .getSubtreeIdsToVisit(this);
                    boolean doVisitChildren = subtreeIdsToVisit != null
                            && !subtreeIdsToVisit.isEmpty();
                    if (doVisitChildren) {
                        // visit the facets of the component
                        for (UIComponent facet : getFacets().values()) {
                            if (facet.visitTree(context, callback)) {
                                return true;
                            }
                        }
                        // visit every column directly without visiting its children
                        // (the children of every UIColumn will be visited later for
                        // every row) and also visit the column's facets
                        for (UIComponent child : getChildren()) {
                            if (child instanceof UIColumn) {
                                VisitResult columnResult = context.invokeVisitCallback(child, callback);
                                if (columnResult == VisitResult.COMPLETE) {
                                    return true;
                                }
                                for (UIComponent facet : child.getFacets().values()) {
                                    if (facet.visitTree(context, callback)) {
                                        return true;
                                    }
                                }
                            }
                        }
                        // <MOD-11>
                        for (UIComponent child : getExtensionComponents()) {
                            if (child == null) continue;
                            if (child.visitTree(context, callback)) {
                                return true;
                            }
                        }
                        // </MOD-11>
                        // iterate over the rows
                        boolean visitRows = getVisitRows(context);
                        int rowsToProcess = visitRows ? getRows() : 1;
                        // if getRows() returns 0, all rows have to be processed
                        if (rowsToProcess == 0) {
                            rowsToProcess = getRowCount();
                        }
                        int rowIndex = getFirst();
                        for (int rowsProcessed = 0; rowsProcessed < rowsToProcess; rowsProcessed++, rowIndex++) {
                            if (visitRows) {
                                setRowIndex(rowIndex);
                                if (!isRowAvailable()) {
                                    return false;
                                }
                            }
                            // visit the children of every child of the UIData that is an instance of UIColumn
                            for (UIComponent child : getChildren()) {
                                if (child instanceof UIColumn) {
                                    for (UIComponent grandchild : child
                                            .getChildren()) {
                                        if (grandchild.visitTree(context, callback)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }
        finally {
            // pop the component from EL and restore the old row index
            popComponentFromEL(context.getFacesContext());
            setRowIndex(oldRowIndex);
        }

        // Return false to allow the visiting to continue
        return false;
    }

    private boolean getVisitRows(VisitContext visitContext) {
        try {
            VisitHint hint = VisitHint.valueOf("SKIP_ITERATION");
            return !visitContext.getHints().contains(hint);
        } catch (IllegalArgumentException e) {
            // Mojarra 2.x versions earlier than 2.1.0 versions don't have such a hint,
            // so simulate this by watching the current phase ID
            PhaseId currentPhaseId = visitContext.getFacesContext().getCurrentPhaseId();
            return !PhaseId.RESTORE_VIEW.equals(currentPhaseId);
        }
    }

    public void setVar(String var) {
        getStateHelper().put(PropertyKeys.var, var);
    }

    protected enum PropertyKeys { // <MOD-13/> was without "protected" keyword
        uiDataValue, first, rows, var, uniqueIdCounter
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    // <MOD-1> code added
    private Set unavailableRowIndexes = null;

    protected void setUnavailableRowIndexes(Set unavailableRowIndexes) {
        this.unavailableRowIndexes = unavailableRowIndexes;
    }

    public boolean isRowAvailableAfterRestoring(int rowIndex) {
        if (unavailableRowIndexes == null)
            return true;
        boolean rowAvailable = !unavailableRowIndexes.contains(Integer.valueOf(rowIndex));
        return rowAvailable;
    }
    // </MOD-1>

    // <MOD-3> This is the new method which is invoked instead of getChildren in processColumnChildren/processColumnFacets methods
    //         The AbstractTable class overrides this method to exclude the non-rendered columns according to the columnsOrder attribute

    protected List getColumnsForProcessing() {
        return getChildren();
    }
    // </MOD-3> "value" was here

    // <MOD-4>

    protected List<Row> getCustomRows() {
        List<Row> customRows = new ArrayList<Row>();
        List<UIComponent> children = getChildren();
        for (UIComponent child : children) {
            if (child instanceof Row)
                customRows.add((Row) child);
        }
        return customRows;
    }
    // </MOD-4>

    // <MOD-5> (added dynamic column state management methods)

    private List<Object> saveDynamicColumnsState() {
        List<Columns> dynamicColumnComponents = findDynamicColumnComponents();
        List<Object> allStates = new ArrayList<Object>();
        for (Columns columns : dynamicColumnComponents) {
            Object theseColumnStates = columns.getColumnStates();
            allStates.add(theseColumnStates);
        }
        return allStates;
    }

    private void restoreDynamicColumnsState(List<Object> state) {
        List<Columns> children = findDynamicColumnComponents();
        for (int i = 0, count = children.size(); i < count; i++) {
            Columns columns = children.get(i);
            Object theseColumnStates = state.get(i);
            columns.setColumnStates(theseColumnStates);
        }
    }

    private void resetDynamicColumnsStates(boolean noValidationErrors) {
        List<Columns> children = findDynamicColumnComponents();
        for (Columns columns : children) {
            columns.resetColumnStates(noValidationErrors);
        }

    }

    private List<Columns> findDynamicColumnComponents() {
        List<Columns> dynamicColumnComponents = new ArrayList<Columns>();
        List<UIComponent> children = getChildren();
        for (UIComponent child : children) {
            if (child instanceof Columns)
                dynamicColumnComponents.add((Columns) child);
        }
        return dynamicColumnComponents;
    }
    // </MOD-5>

    // <MOD-7> code added

    enum Mod7PropertyKeys {
        style,
        styleClass,
        rolloverStyle,
        rolloverClass,

        onclick,
        ondblclick,
        onmousedown,
        onmouseover,
        onmousemove,
        onmouseout,
        onmouseup,
        onfocus,
        onblur,
        onkeydown,
        onkeyup,
        onkeypress,
        oncontextmenu
    }


    public String getStyle() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.style);
    }

    public void setStyle(String style) {
        getStateHelper().put(Mod7PropertyKeys.style, style);
    }

    public String getRolloverStyle() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.rolloverStyle);
    }

    public void setRolloverStyle(String rolloverStyle) {
        getStateHelper().put(Mod7PropertyKeys.rolloverStyle, rolloverStyle);
    }

    public String getRolloverClass() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.rolloverClass);
    }

    public void setRolloverClass(String rolloverClass) {
        getStateHelper().put(Mod7PropertyKeys.rolloverClass, rolloverClass);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.styleClass);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(Mod7PropertyKeys.styleClass, styleClass);
    }

    public String getOnkeypress() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.onkeypress);
    }

    public void setOnkeypress(String onkeypress) {
        getStateHelper().put(Mod7PropertyKeys.onkeypress, onkeypress);
    }

    public String getOncontextmenu() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.oncontextmenu);
    }

    public void setOncontextmenu(String oncontextmenu) {
        getStateHelper().put(Mod7PropertyKeys.oncontextmenu, oncontextmenu);
    }

    public String getOnclick() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.onclick);
    }

    public void setOnclick(String onclick) {
        getStateHelper().put(Mod7PropertyKeys.onclick, onclick);
    }

    public String getOndblclick() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.ondblclick);
    }

    public void setOndblclick(String ondblclick) {
        getStateHelper().put(Mod7PropertyKeys.ondblclick, ondblclick);
    }

    public String getOnmousedown() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.onmousedown);
    }

    public void setOnmousedown(String onmousedown) {
        getStateHelper().put(Mod7PropertyKeys.onmousedown, onmousedown);
    }

    public String getOnmouseover() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.onmouseover);
    }

    public void setOnmouseover(String onmouseover) {
        getStateHelper().put(Mod7PropertyKeys.onmouseover, onmouseover);
    }

    public String getOnmousemove() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.onmousemove);
    }

    public void setOnmousemove(String onmousemove) {
        getStateHelper().put(Mod7PropertyKeys.onmousemove, onmousemove);
    }

    public String getOnmouseout() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.onmouseout);
    }

    public void setOnmouseout(String onmouseout) {
        getStateHelper().put(Mod7PropertyKeys.onmouseout, onmouseout);
    }

    public String getOnmouseup() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.onmouseup);
    }

    public void setOnmouseup(String onmouseup) {
        getStateHelper().put(Mod7PropertyKeys.onmouseup, onmouseup);
    }

    public String getOnfocus() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.onfocus);
    }

    public void setOnfocus(String onfocus) {
        getStateHelper().put(Mod7PropertyKeys.onfocus, onfocus);
    }

    public String getOnblur() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.onblur);
    }

    public void setOnblur(String onblur) {
        getStateHelper().put(Mod7PropertyKeys.onblur, onblur);
    }

    public String getOnkeydown() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.onkeydown);
    }

    public void setOnkeydown(String onkeydown) {
        getStateHelper().put(Mod7PropertyKeys.onkeydown, onkeydown);
    }

    public String getOnkeyup() {
        return (String) getStateHelper().eval(Mod7PropertyKeys.onkeyup);
    }

    public void setOnkeyup(String onkeyup) {
        getStateHelper().put(Mod7PropertyKeys.onkeyup, onkeyup);
    }

    // </MOD-7>

    //<MOD-11>
    protected List<UIComponent> getExtensionComponents() {
        return Collections.emptyList();
    }
    //

}

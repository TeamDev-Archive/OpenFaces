/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.util;

import org.openfaces.component.OUIData;
import org.openfaces.component.OUIObjectIteratorBase;
import org.openfaces.util.ValueBindings;
import org.openfaces.util.DataUtil;
import org.openfaces.util.Components;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.model.DataModel;
import java.util.HashMap;
import java.util.Map;

/**
 * The ForEach component is an iterator component that renders the specified set of components multiple times based on
 * its parameters.
 *
 * @author Alexey Tarasyuk
 */

public class ForEach extends OUIObjectIteratorBase {
    public static final String COMPONENT_TYPE = "org.openfaces.ForEach";
    public static final String COMPONENT_FAMILY = "org.openfaces.ForEach";

    private static final String DEFAULT_WRAPPER_TAG_NAME = "span";

    // todo: add support for rolloverStyle attribute and event attributes inherited from OUIObjectIteratorBase

    private Object items;
    private DataModel data;
    private Integer index;
    private Integer beginIndex;
    private Integer endIndex;
    private Integer step;
    private String var;
    private String varStatus;
    private String wrapperTagName;

    private IterationStatus status;
    private Map<Integer, Object> childrenStates;
    private Object childrenOriginalState;

    public ForEach() {
        setRendererType("org.openfaces.ForEachRenderer");
        childrenStates = new HashMap<Integer, Object>();
    }

    /**
     * @return <code>"org.openfaces.ForEach"</code>
     * @see javax.faces.component.UIComponent
     */
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * Indicate is it possible to continue the iteration or not.
     *
     * @return <code>true</code> if is it possible to continue the iteration or <code>false</code> otherwise.
     */
    public boolean hasNext() {
        int nextIndex = getNextIndex();
        DataModel data = getData();
        Integer endAttr = getEnd();
        if (endAttr != null) {
            int hasNextIndicator = (endAttr - nextIndex) * getIndexStep();
            if (hasNextIndicator < 0)
                return false;
        } else if (data == null) {
            return false;
        }
        if (data != null) {
            data.setRowIndex(nextIndex);
            if (!data.isRowAvailable())
                return false;
        }
        return true;
    }

    /**
     * Perform one step of iteration.
     *
     * @return current item of value-bound collection as an <code>java.lang.Object</code> or <code>null</code> if no value-bound data collection is specified.
     */
    public Object next() {
        if (!hasNext())
            throw new FacesException("Further iteration not allowed");
        setIndex(getNextIndex());
        return status.getCurrent();
    }

    /**
     * @return actual 'begin' value
     */
    private int getFirstIndex() {
        Integer beginAttr = getBegin();
        if (beginAttr == null)
            return 0;
        else
            return beginAttr;
    }

    /**
     * @return actual 'step' value
     */
    private int getIndexStep() {
        Integer indexStep = getStep();
        if (indexStep == null)
            return 1;
        return indexStep;
    }

    /**
     * @return index that will be next index of iteration, of course if it is allowed in current context
     */
    private int getNextIndex() {
        Integer curIndex = getIndex();
        if (curIndex == null)
            return getFirstIndex();
        else
            return curIndex + getIndexStep();
    }

    private Integer getIndex() {
        return index;
    }

    /**
     * The base method to perform iteration. Also reset all inner variables when 'index' is null.
     *
     * @param index index of row or
     */
    private void setIndex(Integer index) {
        if (index == null) {
            reset();
        } else {
            saveChildrenState();
            this.index = index;
            data = getData();
            if (data != null)
                data.setRowIndex(this.index);
            setIterationStatus();
            putVariables();
            restoreChildrenState();
        }
    }

    /**
     * reset inner variables and remove 'var' and 'varStatus' from requestMap. This method must been called at the start of iteration and at the end.
     */
    private void reset() {
        saveChildrenState();
        index = null;
        data = null;
        status = null;
        if (childrenOriginalState != null)
            restoreChildrenState();
        childrenOriginalState = null;

        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String var = getVar();
        String varStatus = getVarStatus();
        if (var != null)
            requestMap.remove(var);
        if (varStatus != null)
            requestMap.remove(varStatus);
    }

    /**
     * Wrap data collection that specified into 'items' attribute into DataModel class.
     *
     * @return wrapped data collection as DataModel.
     */
    private DataModel getData() {
        Object items = getItems();
        if (items == null)
            return null;
        return DataUtil.objectAsDataModel(items);
    }

    /**
     * cache current data of current iteration into IterationStatus object
     */
    private void setIterationStatus() {
        if (getIndex() == null)
            status = null;
        else {
            Object currentItem = (data != null && data.isRowAvailable()) ? data.getRowData() : null;
            int index = getIndex();
            int first = getFirstIndex();
            int step = getIndexStep();
            int count = 1 + (index - first) / step;
            status = new IterationStatus(currentItem, index, count, index == first, !hasNext(), getBegin(), getEnd(), getStep());
        }
    }

    public void setObjectId(String objectId) {
        if (objectId != null)
            setIndex(Integer.valueOf(objectId));
        else
            setIndex(null);
    }

    public String getObjectId() {
        if (getIndex() != null)
            return getIndex().toString();
        else
            return null;
    }

    /**
     * Save state of children into local variable. Both with <code>restoreChildrenState()</code> need to perform correct children JSF life-cycle processing.
     */
    private void saveChildrenState() {
        if (this.getChildCount() > 0) {
            Object childrenState = OUIData.saveDescendantComponentStates(this.getChildren().iterator(), true);
            if (getIndex() == null)
                childrenOriginalState = childrenState;
            else
                childrenStates.put(getIndex(), childrenState);
        }
    }

    /**
     * Restore state of children from local variable. Both with <code>saveChildrenState()</code> need to perform correct children JSF life-cycle processing.
     */
    private void restoreChildrenState() {
        if (this.getChildCount() > 0) {
            Object childrenState = childrenStates.get(getIndex());
            if (childrenState == null)
                childrenState = childrenOriginalState;
            OUIData.restoreDescendantComponentStates(this.getChildren().iterator(), childrenState, true);
        }
    }

    /**
     * Put 'var' and 'varStatus' variables into request context.
     */
    private void putVariables() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String var = getVar();
        String varStatus = getVarStatus();
        if (var != null) {
            Object varObj = status.getCurrent();
            requestMap.put(var, varObj);
        }
        if (varStatus != null) {
            requestMap.put(varStatus, status);
        }
    }

    /**
     * Process specified JSF life-cycle phase.
     *
     * @param context faces context.
     * @param phase   current JSF life-cycle phase identifier.
     */
    private void process(FacesContext context, PhaseId phase) {
        if (!this.isRendered())
            return;
        this.setIndex(null);

        if (this.getChildCount() > 0) {
            while (this.hasNext()) {
                this.next();
                if (PhaseId.APPLY_REQUEST_VALUES.equals(phase)) {
                    super.processDecodes(context);
                } else if (PhaseId.PROCESS_VALIDATIONS.equals(phase)) {
                    super.processValidators(context);
                } else if (PhaseId.UPDATE_MODEL_VALUES.equals(phase)) {
                    super.processUpdates(context);
                }
            }
        }
        this.setIndex(null);
    }

    @Override
    public void processDecodes(FacesContext faces) {
        this.process(faces, PhaseId.APPLY_REQUEST_VALUES);
    }

    @Override
    public void processUpdates(FacesContext faces) {
        this.process(faces, PhaseId.UPDATE_MODEL_VALUES);
    }

    @Override
    public void processValidators(FacesContext faces) {
        this.process(faces, PhaseId.PROCESS_VALIDATIONS);
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                items,
                beginIndex,
                endIndex,
                step,
                var,
                varStatus,
                wrapperTagName
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        items = values[i++];
        beginIndex = (Integer) values[i++];
        endIndex = (Integer) values[i++];
        step = (Integer) values[i++];
        var = (String) values[i++];
        varStatus = (String) values[i++];
        wrapperTagName = (String) values[i];
    }


    private static final class IndexedEvent extends FacesEvent {

        private final FacesEvent originalEvent;
        private final Integer index;

        public IndexedEvent(ForEach owner, FacesEvent originalEvent, Integer index) {
            super(owner);
            this.originalEvent = originalEvent;
            this.index = index;
        }

        public PhaseId getPhaseId() {
            return originalEvent.getPhaseId();
        }

        public void setPhaseId(PhaseId phaseId) {
            originalEvent.setPhaseId(phaseId);
        }

        public boolean isAppropriateListener(FacesListener listener) {
            return originalEvent.isAppropriateListener(listener);
        }

        public void processListener(FacesListener listener) {
            ForEach owner = (ForEach) this.getComponent();
            Integer currentIndex = owner.getIndex();
            owner.setIndex(index);
            originalEvent.processListener(listener);
            owner.setIndex(currentIndex);
        }

        public Integer getIndex() {
            return index;
        }

        public void queue() {
            originalEvent.queue();
        }

        public String toString() {
            return originalEvent.toString();
        }

        public FacesEvent getOriginalEvent() {
            return originalEvent;
        }
    }

    @Override
    public void queueEvent(FacesEvent event) {
        super.queueEvent(new IndexedEvent(this, event, getIndex()));
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (event instanceof IndexedEvent) {
            IndexedEvent indexedEvent = (IndexedEvent) event;
            Integer eventIndex = indexedEvent.getIndex();
            Integer currentIndex = getIndex();
            try {
                setIndex(eventIndex);
                FacesEvent originalEvent = indexedEvent.getOriginalEvent();
                originalEvent.getComponent().broadcast(originalEvent);
            } finally {
                this.setIndex(currentIndex);
            }
        } else {
            super.broadcast(event);
        }
    }

    public Object getItems() {
        return ValueBindings.get(this, "items", items, Object.class);
    }

    public void setItems(Object items) {
        this.items = items;
    }

    public Integer getBegin() {
        return ValueBindings.get(this, "begin", beginIndex, Integer.class);
    }

    public void setBegin(Integer beginIndex) {
        this.beginIndex = beginIndex;
    }

    public Integer getEnd() {
        return ValueBindings.get(this, "end", endIndex, Integer.class);
    }

    public void setEnd(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public Integer getStep() {
        return ValueBindings.get(this, "step", step, Integer.class);
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getVar() {
        return ValueBindings.get(this, "var", var);
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVarStatus() {
        return ValueBindings.get(this, "varStatus", varStatus);
    }

    public void setVarStatus(String varStatus) {
        this.varStatus = varStatus;
    }

    public String getWrapperTagName() {
        String defaultWrapperTagName = null;
        boolean idSpecified = Components.isComponentIdSpecified(this);
        if (idSpecified || (getStyle() != null) || (getStyleClass() != null))
            defaultWrapperTagName = DEFAULT_WRAPPER_TAG_NAME;
        return ValueBindings.get(this, "wrapperTagName", wrapperTagName, defaultWrapperTagName);
    }

    public void setWrapperTagName(String wrapperTagName) {
        this.wrapperTagName = wrapperTagName;
    }
}

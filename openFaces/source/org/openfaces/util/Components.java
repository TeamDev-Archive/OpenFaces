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
package org.openfaces.util;

import org.openfaces.ajax.PartialViewContext;
import org.openfaces.component.OUIObjectIterator;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIForm;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UniqueIdVendor;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @author Dmitry Pikhulya
 */
public class Components {
    private static final String POSTPONED_ACTIONS_ATTR = "org.openfaces.postponedActions";

    private Components() {
    }


    public static Object[] anyArrayToObjectArray(Object array) {
        Class componentType = array.getClass().getComponentType();
        if (!componentType.isPrimitive())
            return (Object[]) array;
        Object[] dstArray;
        int arrayLength = Array.getLength(array);
        dstArray = new Object[arrayLength];
        for (int i = 0; i < arrayLength; i++)
            dstArray[i] = Array.get(array, i);
        return dstArray;
    }

    public static String referenceIdToClientId(FacesContext context, UIComponent base, String refId) {
        if (refId == null)
            return refId;

        UIComponent component = referenceIdToComponent(base, refId);

        if (component != null) {
            refId = component.getClientId(context);
        } else {
            if (refId.startsWith(":"))
                refId = refId.substring(1);
        }
        return refId;
    }

    public static UIComponent referenceIdToComponent(UIComponent component, String refId) {
        if (refId == null)
            return null;
        UIComponent focusedComponent;
        try {
            focusedComponent = component.findComponent(refId);
        } catch (IllegalArgumentException iae) {
            // refId can refer to non-JSF components with "::" symbols as part of Id, which results in an exception
            // from findComponent (see QKS-6242 - interoperation of DropDownField with Focus component).
            focusedComponent = null;
        }
        return focusedComponent;
    }


    public static <T extends UIComponent> T getChildWithClass(UIComponent parent, Class<T> childClass, String defaultIdSuffix) {
        T component = findChildWithClass(parent, childClass);
        if (component != null)
            return component;
        try {
            component = childClass.newInstance();
            if (defaultIdSuffix.startsWith(Rendering.SERVER_ID_SUFFIX_SEPARATOR))
                defaultIdSuffix = defaultIdSuffix.substring(Rendering.SERVER_ID_SUFFIX_SEPARATOR.length());
            component.setId(parent.getId() + Rendering.SERVER_ID_SUFFIX_SEPARATOR + defaultIdSuffix);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        addChild(parent, component);

        return component;
    }

    /**
     * This method is required to overcome the Mojarra 2.1.9+ state saving problem, where an attempt to add child
     * component for a component that is rendered inside of an iterator component (UIData and others, which change
     * clientIds of their children during iteration), causes an exception
     * javax.faces.FacesException: Cannot add the same component twice: form:dataTable:0:dateChooser--popup
     *    at com.sun.faces.context.StateContext$AddRemoveListener.handleAddRemoveWithAutoPrune(StateContext.java:476)
     *    at com.sun.faces.context.StateContext$AddRemoveListener.handleAdd(StateContext.java:422)
     *    ...
     *
     *  Resetting the iterator position to the "initial" one for a moment of adding child component fixes the problem
     *  (Mojarra seems to save component's state under wrong client-ids if iteration index is in component's clientId).
     *
     *  This workaround should be in place until this bug is fixed in Mojarra
     *  (and older versions don't have to be supported).
     */
    public static Runnable resetParentIterators(UIComponent component) {
        if (!ApplicationParams.getIterationIndexWorkaround()) return null;

        final UIData uiData = getParentWithClass(component, UIData.class);
        final int rowIndex = uiData != null ? uiData.getRowIndex() : -1;
        if (rowIndex != -1)
            uiData.setRowIndex(-1);

        final OUIObjectIterator ouiObjectIterator = getParentWithClass(component, OUIObjectIterator.class);
        final String objectId = ouiObjectIterator != null ? ouiObjectIterator.getObjectId() : null;
        if (objectId != null)
            ouiObjectIterator.setObjectId(null);

        return (rowIndex == -1 && objectId == null)
                ? null
                : new Runnable() {
            public void run() {
                if (objectId != null)
                    ouiObjectIterator.setObjectId(objectId);
                if (rowIndex != -1)
                    uiData.setRowIndex(rowIndex);
            }
        };
    }

    public static <T extends UIComponent> T findChildWithClass(UIComponent parent, Class<T> childClass) {
        List<T> childrenWithClass = findChildrenWithClass(parent, childClass, false, false);
        if (childrenWithClass.size() == 0)
            return null;
        return childrenWithClass.get(0);
    }

    public static <T extends UIComponent> T findChildWithClass(
            UIComponent parent,
            Class<T> childClass,
            String childTagName) {
        List<T> childrenWithClass = findChildrenWithClass(parent, childClass, false, false);
        int size = childrenWithClass.size();
        if (size == 0)
            return null;
        if (size > 1)
            throw new RuntimeException("There should be only one " + childTagName + " child under this component: " +
                    parent.getId());
        return childrenWithClass.get(0);
    }

    public static <T extends UIComponent> List<T> findChildrenWithClass(UIComponent parent, Class<T> childClass) {
        return findChildrenWithClass(parent, childClass, false, false);
    }

    public static <T extends UIComponent> List<T> findChildrenWithClass(UIComponent parent, Class<? extends T> childClass,
                                                    boolean onlyRendered, boolean recursive) {
        List<T> result = new ArrayList<T>();
        List<UIComponent> children = parent.getChildren();
        for (UIComponent child : children) {
            if (childClass.isAssignableFrom(child.getClass()) && (!onlyRendered || child.isRendered())) {
                result.add((T) child);

                if (recursive) {
                    result.addAll(findChildrenWithClass(child, childClass, onlyRendered, recursive));
                }
            }
        }
        return result;
    }

    public static <T extends UIComponent> List<T> findFacetsWithClass(UIComponent parent, Class<? extends T> facetClass) {
        List<T> result = new ArrayList<T>();
        Collection<UIComponent> facets = parent.getFacets().values();
        for (UIComponent child : facets) {
            if (facetClass.isAssignableFrom(child.getClass())) {
                result.add((T) child);
            }
            result.addAll(findChildrenWithClass(child, facetClass, false, true));
        }
        return result;
    }


    public static boolean isComponentIdSpecified(UIComponent component) {
        String id = component.getId();
        if (id == null)
            return false;
        if (!id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX) || id.length() <= UIViewRoot.UNIQUE_ID_PREFIX.length())
            return true;

        String suffix = id.substring(UIViewRoot.UNIQUE_ID_PREFIX.length());
        try {
            Integer.parseInt(suffix);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /**
     * This method creates a new output text component and adds it as a facet to the specified parent component
     *
     * @param context  {@link javax.faces.context.FacesContext} for the current request
     * @param parent   Method will search fo facet in this component or create it, if needed
     * @param idSuffix The suffix identifying the {@link javax.faces.component.html.HtmlOutputText} to be returned
     * @param text     The text in output text field
     * @return created or existed output text component
     */
    public static HtmlOutputText composeHtmlOutputText(FacesContext context, UIComponent parent, String idSuffix, String text) {
        HtmlOutputText outputText = getOrCreateFacet(context, parent,
                HtmlOutputText.COMPONENT_TYPE, idSuffix, HtmlOutputText.class);
        outputText.setValue(text);
        return outputText;
    }

    /**
     * This method add child to parent component
     *
     * @param context       {@link javax.faces.context.FacesContext} for the current request
     * @param parent        Method will create child for this component
     * @param componentType The class for child creation
     * @param idSuffix      The suffix identifying the child {@link javax.faces.component.UIComponent} to be returned
     * @return created child
     */
    public static UIComponent createChildComponent(
            FacesContext context, UIComponent parent, String componentType, String idSuffix) {
        String childId = generateIdWithSuffix(parent, idSuffix);
        UIComponent component = createComponent(context, componentType, childId);
        addChild(parent, component);
        return component;
    }

    public static void addChild(UIComponent parent, UIComponent child) {
        Runnable restoreIterators = resetParentIterators(parent);
        parent.getChildren().add(child);
        if (restoreIterators != null) restoreIterators.run();
    }

    public static void removeChild(UIComponent parent, UIComponent child) {
        Runnable restoreIterators = resetParentIterators(parent);
        parent.getChildren().remove(child);
        if (restoreIterators != null) restoreIterators.run();
    }

    /**
     * This method add child to parent component at the specified position
     *
     * @param context       {@link javax.faces.context.FacesContext} for the current request
     * @param parent        Method will create child for this component
     * @param componentType The class for child creation
     * @param idSuffix      The suffix identifying the child {@link javax.faces.component.UIComponent} to be returned
     * @param i             index at which the specified child is to be inserted to parent's child list
     * @return created child
     */
    public static UIComponent createChildComponent(
            FacesContext context, UIComponent parent, String componentType, String idSuffix, int i) {
        String childId = generateIdWithSuffix(parent, idSuffix);
        UIComponent component = createComponent(context, componentType, childId);
        Runnable restoreIterators = resetParentIterators(parent);
        parent.getChildren().add(i, component);
        if (restoreIterators != null) restoreIterators.run();
        return component;
    }


    private static UIComponent createComponent(FacesContext context, String componentType, String id) {
        return createComponent(context, componentType, UIComponent.class, id);
    }

    public static <C extends UIComponent> C createComponent(FacesContext context, String componentType,
                                                            Class<C> componentClass,
                                                            UIComponent idGenerationBase, String idSuffix) {
        String componentId = generateIdWithSuffix(idGenerationBase, idSuffix);
        return createComponent(context, componentType, componentClass, componentId);
    }

    /**
     * This method creates components with by component-type string, and creates if sub-components if needed
     *
     * @param context       {@link javax.faces.context.FacesContext} for the current request
     * @param componentType The component type for which to create and return a new {@link javax.faces.component.UIComponent} instance
     * @param id            The id identifying the {@link javax.faces.component.UIComponent} to be returned
     */
    public static <C extends UIComponent> C createComponent(FacesContext context,
                                                            String componentType, Class<C> componentClass,
                                                            String id) {
        Application application = context.getApplication();
        UIComponent component = application.createComponent(componentType);
        Class<? extends UIComponent> actualClass = component.getClass();
        if (!componentClass.isAssignableFrom(actualClass))
            throw new IllegalArgumentException("Unexpected component class. " +
                    "Expected: " + componentClass.getName() +
                    "; actual: " + actualClass.getName());
        component.setId(id);
        return (C) component;
    }

    /**
     * This method creates a new command button component and adds it as a facet to the specified parent component
     *
     * @param context  {@link javax.faces.context.FacesContext} for the current request
     * @param parent   Method will search fo facet in this component or create it, if needed
     * @param idSuffix The suffix identifying the {@link javax.faces.component.html.HtmlCommandButton} to be returned
     * @param text     The text on command button
     * @return created or existed command button component
     */
    public static HtmlCommandButton createButtonFacet(FacesContext context, UIComponent parent, String idSuffix, String text) {
        HtmlCommandButton prevBtn = getOrCreateFacet(
                context, parent, HtmlCommandButton.COMPONENT_TYPE, idSuffix, HtmlCommandButton.class);
        prevBtn.setValue(text);
        return prevBtn;
    }

    /**
     * Generate id on base of component id.
     *
     * @param baseComponent The component, which id will be used for generating
     * @param idSuffix      The suffix, which will be added to component id
     * @return generated id
     */
    public static String generateIdWithSuffix(UIComponent baseComponent, String idSuffix) {
        generateIdIfNotSpecified(baseComponent); // null id may have place when creating a component programmatically (especially in JSF RI 1.2, where id is not assigned in getClientId automatically)
        String result = baseComponent.getId() + Rendering.SERVER_ID_SUFFIX_SEPARATOR + idSuffix;
        return result;
    }

    /**
     * Create {@link javax.faces.component.html.HtmlOutputText} component with given text
     *
     * @param context {@link javax.faces.context.FacesContext} for the current request
     * @param text    The text to be set in output text
     * @param escape  Flag indicating that characters that are sensitive in HTML and XML markup must be escaped.
     * @return the created {@link javax.faces.component.html.HtmlOutputText}
     */
    public static HtmlOutputText createOutputText(FacesContext context, String text, boolean escape) {
        HtmlOutputText outputText = (HtmlOutputText) context.getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
        outputText.setValue(text);
        outputText.setEscape(escape);
        return outputText;
    }

    /**
     * Create {@link javax.faces.component.html.HtmlOutputText} component with given text
     *
     * @param context {@link javax.faces.context.FacesContext} for the current request
     * @param text    The text to be set in output text
     * @return the created {@link javax.faces.component.html.HtmlOutputText}
     */
    public static HtmlOutputText createOutputText(FacesContext context, String text) {
        return createOutputText(context, text, true);
    }

    /**
     * Return the closest form for component
     *
     * @param component The component, which form we obtain
     * @return the nearest enclosing form for component
     */
    public static UIForm getEnclosingForm(UIComponent component) {
        UIComponent result = component;
        while (result != null) {
            if (result instanceof UIForm) {
                return (UIForm) result;
            }
            result = result.getParent();
        }
        return null;
    }

    public static UIForm findForm(UIComponent component) {
        while (component != null) {
            if (component instanceof UIForm)
                return (UIForm) component;
            component = component.getParent();
        }
        return null;
    }

    /**
     * Check component id and generate it, if necessary
     *
     * @param component The component for id generation
     */
    public static void generateIdIfNotSpecified(UIComponent component) {
        if (component.getId() != null)
            return;
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = context.getViewRoot();
        component.setId(viewRoot.createUniqueId());
    }

    /**
     * Find child component by its suffix
     *
     * @param component The parent component to search in
     * @param idSuffix  The suffix identifying the {@link javax.faces.component.UIComponent} to be returned
     * @return the found {@link javax.faces.component.UIComponent}, or <code>null</code> if the component was not found.
     * @see #generateIdWithSuffix
     */
    public static UIComponent getChildBySuffix(UIComponent component, String idSuffix) {
        String childId = generateIdWithSuffix(component, idSuffix);
        return component.findComponent(childId);
    }

    public static void runWhenReady(SelfScheduledAction action) {
        if (action.executeIfReady())
            return;
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        List<SelfScheduledAction> postponedActions = (List) requestMap.get(POSTPONED_ACTIONS_ATTR);
        if (postponedActions == null) {
            postponedActions = new ArrayList<SelfScheduledAction>();
            requestMap.put(POSTPONED_ACTIONS_ATTR, postponedActions);
        }
        postponedActions.add(action);
    }

    public static void runScheduledActions() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        List<SelfScheduledAction> postponedActions = (List) requestMap.get(POSTPONED_ACTIONS_ATTR);
        if (postponedActions == null)
            return;
        for (Iterator<SelfScheduledAction> actionIterator = postponedActions.iterator(); actionIterator.hasNext(); ) {
            SelfScheduledAction action = actionIterator.next();
            if (action.executeIfReady())
                actionIterator.remove();
        }
    }

    public static boolean isChildComponent(UIComponent child, UIComponent parent) {
        for (UIComponent c = child.getParent(); c != null; c = c.getParent())
            if (c == parent)
                return true;
        return false;
    }


    /**
     * This method checks and create, if needed new facet of parent component.
     *
     * @param context               {@link javax.faces.context.FacesContext} for the current request
     * @param parent                Method will search fo facet in this component or create it, if needed
     * @param componentType         The component type for which to create and return a new {@link javax.faces.component.UIComponent} instance
     * @param identifier            The id identifying the {@link javax.faces.component.UIComponent} to be returned
     * @param enforceComponentClass If facet with given identifier exist, but it's class doesn't
     *                              seem to be equal to enforceComponentClass, facet will be recreated
     * @return facet of parent component
     */
    public static <C extends UIComponent> C getOrCreateFacet(
            FacesContext context, UIComponent parent, String componentType, String identifier, Class<C> enforceComponentClass) {
        String id = generateIdWithSuffix(parent, identifier);
        return getOrCreateFacet(context, parent, componentType, identifier, id, enforceComponentClass);
    }

    public static <C extends UIComponent> C getOrCreateFacet(
            FacesContext context, UIComponent parent, String componentType, String facetName, String id, Class<C> enforceComponentClass) {

        UIComponent component = parent.getFacet(facetName);
        if (component != null) {
            if (enforceComponentClass == null || enforceComponentClass.isAssignableFrom(component.getClass())) {
                if (!id.equals(component.getId()))
                    component.setId(id);
                return (C) component;
            }
        }

        component = createComponent(context, componentType, id);
        List<Object> originalParentObjectIds = new ArrayList<Object>();
        for (UIComponent p = parent; p != null; p = p.getParent()) {
            // Reset rowIndex/objectId for all parent iterator components for proper functionality fo state saving
            // mechanism, which listens for component insertions and saves new components by their ids
            // (com.sun.faces.context.SateContext.AddRemoveListener.handleAddEvent which constructs a list of
            // "dynamicAdds" at least in Mojarra 2.0.3)
            // ...and it's important to save component ids without any suffixes that might have place if parent iterator
            // components are in the middle of iteration currently.
            if (p instanceof UIData) {
                UIData uiData = (UIData) p;
                originalParentObjectIds.add(uiData.getRowIndex());
                uiData.setRowIndex(-1);
            } else if (p instanceof OUIObjectIterator) {
                OUIObjectIterator objectIterator = (OUIObjectIterator) p;
                originalParentObjectIds.add(objectIterator.getObjectId());
                objectIterator.setObjectId(null);
            }
        }
        parent.getFacets().put(facetName, component);
        for (UIComponent p = parent; p != null; p = p.getParent()) {
            // restore the original iterator positions
            if (p instanceof UIData) {
                UIData uiData = (UIData) p;
                uiData.setRowIndex((Integer) originalParentObjectIds.remove(0));
            } else if (p instanceof OUIObjectIterator) {
                OUIObjectIterator objectIterator = (OUIObjectIterator) p;
                objectIterator.setObjectId((String) originalParentObjectIds.remove(0));
            }
        }
        return (C) component;
    }

    /**
     * This method searches in parent component for facet with given name and throw exception, if not found.
     *
     * @param parent                The component, in which facet will be searched
     * @param identifier            The id identifying the {@link javax.faces.component.UIComponent} to be returned
     * @param enforceComponentClass If facet with given identifier exist, but it's class doesn't
     *                              seem to be equal to enforceComponentClass, exception will be thrown
     * @return facet with given name
     */
    public static UIComponent getFacet(UIComponent parent, String identifier, Class enforceComponentClass) {
        UIComponent component = parent.getFacet(identifier);
        if (component != null) {
            if (enforceComponentClass == null || component.getClass().equals(enforceComponentClass))
                return component;
        } else {
            throw new IllegalStateException("There is no facet with id - " + identifier + " in component - " + parent);
        }

        return component;
    }

    /**
     * Searches the component's parent chain until it finds the nearest parent with this class. Super classes and
     * interfaces are also supported by this method.
     */
    public static <C> C getParentWithClass(UIComponent component, Class<C> parentClass) {
        for (UIComponent parent = component.getParent(); parent != null; parent = parent.getParent())
            if (parentClass.isAssignableFrom(parent.getClass()))
                return (C) parent;
        return null;
    }

    public static UIComponent getFacet(UIComponent component, String facetName) {
        UIComponent facet = component.getFacet(facetName);
        if (facet == null) return null;
        if (isImplicitPanel(facet)) {
            // deal with an implicit panel creation made by RI
            facet = facet.getChildren().get(0);
        }
        return facet;
    }

    /**
     * @param component
     */
    public static void fixImplicitPanelIdsForMojarra_2_0_3(UIComponent component, boolean fixDeeply) {
        UniqueIdVendor uniqueIdVendor = null;
        for (UIComponent c = component; c != null; c = c.getParent()) {
            if (c instanceof NamingContainer && c instanceof UniqueIdVendor) {
                uniqueIdVendor = (UniqueIdVendor) c;
                break;
            }
        }
        if (uniqueIdVendor == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            uniqueIdVendor = context.getViewRoot();
        }
        fixImplicitPanelIdsForMojarra_2_0_3(component, uniqueIdVendor, fixDeeply);
    }

    private static void fixImplicitPanelIdsForMojarra_2_0_3(UIComponent component, UniqueIdVendor uniqueIdVendor, boolean fixDeeply) {
        Map<String, UIComponent> facets = component.getFacets();
        Collection<UIComponent> facetComponents = facets.values();
        for (UIComponent facetComponent : facetComponents) {
            if (isImplicitPanel(facetComponent) && !isComponentIdSpecified(facetComponent)) {
                FacesContext context = FacesContext.getCurrentInstance();
                facetComponent.setId("_of_fix_implicitPanel_" + uniqueIdVendor.createUniqueId(context, null));
            }
            if (fixDeeply && !(facetComponent instanceof NamingContainer))
                fixImplicitPanelIdsForMojarra_2_0_3(facetComponent, uniqueIdVendor, true);
        }

        if (fixDeeply) {
            List<UIComponent> children = component.getChildren();
            for (UIComponent child : children) {
                if (!(child instanceof NamingContainer))
                    fixImplicitPanelIdsForMojarra_2_0_3(child, uniqueIdVendor, true);
            }
        }
    }

    /**
     * This method addresses the Mojarra 2.0.3 issue (already fixed in 2.1.3) where a component placed into a facet
     * was implicitly wrapped into an intermediate UIPanel component, which was registered as an immediate facet
     * content instead of the component placed into the facet via xhtml, so component.getParent() didn't return the
     * facet owner component, and returned UIPanel instead.
     * @param facet a facet content component
     * @return true if it is an excessive implicitly added UIPanel, which should be ignored
     */
    public static boolean isImplicitPanel(UIComponent facet) {
        return facet instanceof UIPanel && facet.getAttributes().containsKey("com.sun.faces.facelets.IMPLICIT_PANEL");
    }

    public static UIComponent getFacetOwner(UIComponent facetComponent) {
        UIComponent parent = facetComponent.getParent();
        if (isImplicitPanel(parent)) parent = parent.getParent();
        return parent;
    }

    public static String tagNameByClass(Class<? extends UIComponent> componentClass) {
        String fqClassName = componentClass.getName();
        String shortClassName = fqClassName.substring(fqClassName.lastIndexOf(".") + 1);
        String tagName = shortClassName.substring(0, 1).toLowerCase() + shortClassName.substring(1);
        return "<o:" + tagName + ">";
    }

    public static UIComponent checkParentTag(
            UIComponent component,
            Class... expectedParentClasses) {
        UIComponent parent = component.getParent();
        for (Class expectedParentClass : expectedParentClasses) {
            if (expectedParentClass.isAssignableFrom(parent.getClass()))
                return parent;
        }
        Class<? extends UIComponent> componentClass = component.getClass();
        String tagName = Components.tagNameByClass(componentClass);
        StringBuilder allowedParentTagNames = new StringBuilder();
        for (Class expectedParentClass : expectedParentClasses) {
            if (allowedParentTagNames.length() > 0) allowedParentTagNames.append(" or ");
            String parentComponentTagName = Components.tagNameByClass(expectedParentClass);
            allowedParentTagNames.append(parentComponentTagName);
        }

        throw new FacesException(tagName + " should be placed as a child tag for " + allowedParentTagNames +
                " tag, \b but it was placed into component with a class of " + parent.getClass().getName());
    }

    private static Map<String, Object> getRequestMap() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) return null;
        ExternalContext externalContext = facesContext.getExternalContext();
        return externalContext.getRequestMap();
    }

    public static void setRequestVariable(String varName, Object varValue) {
        Map<String, Object> requestMap = getRequestMap();
        Object prevVarValue = requestMap.put(varName, varValue);
        String backupVarName = "of:prev_" + varName;
        Stack<Object> backupValues = (Stack<Object>) requestMap.get(backupVarName);
        if (backupValues == null) {
            backupValues = new Stack<Object>();
            requestMap.put(backupVarName, backupValues);
        }
        backupValues.push(prevVarValue);
    }

    public static void restoreRequestVariable(String varName) {
        Map<String, Object> requestMap = getRequestMap();
        if (requestMap == null || varName == null) {
            return;
        }
        String backupVarName = "of:prev_" + varName;
        Stack backupValues = (Stack) requestMap.get(backupVarName);
        if (backupValues == null || backupValues.isEmpty())
            return;
        Object oldValue = backupValues.pop();
        requestMap.put(varName, oldValue);
    }

    /**
     * @deprecated this method family uses a practice of having to have a knowledge about the specific iterator
     * components and the way that their custom client id generation logic works. This should be reimplemented to usa a
     * generic mechanism (see the to-do below) <br/>
     * //todo: Try to rework with viewRoot.invokeOnComponent (and/or viewRoot.visitTree if required)
     */
    public static UIComponent findComponent(UIComponent baseComponent, String idPath) {
        return PartialViewContext.findComponentById(baseComponent, idPath, false, false, false);
    }


    public static List<UIComponent> getFacets(UIComponent component, String... facetNames) {
        List<UIComponent> facets = new ArrayList<UIComponent>();
        for (String facetName : facetNames) {
            UIComponent facet = getFacet(component, facetName);
            if (facet != null)
                facets.add(facet);
        }
        return facets;
    }

    /**
     * Finds the owner of this component that includes it as one of its facet (either directly or indirectly through
     * other container components)
     */
    public static FacetReference getParentFacetReference(UIComponent component) {
        UIComponent parent = component.getParent();
        if (parent == null) return null;
        Set<Map.Entry<String,UIComponent>> facetEntries = parent.getFacets().entrySet();
        for (Map.Entry<String, UIComponent> facetEntry : facetEntries) {
            UIComponent value = facetEntry.getValue();
            if (value == component)
                return new FacetReference(parent, facetEntry.getKey());
        }
        return getParentFacetReference(parent);
    }

    public static void clearCachedClientIds(UIComponent component) {
        // setId forces client id recalculation
        component.setId(component.getId());
        Iterator<UIComponent> kids = component.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            clearCachedClientIds(kid);
        }

    }

    public static class FacetReference {
        private UIComponent facetOwner;
        private String facetName;

        public FacetReference(UIComponent facetOwner, String facetName) {
            this.facetOwner = facetOwner;
            this.facetName = facetName;
        }

        public UIComponent getFacetOwner() {
            return facetOwner;
        }

        public String getFacetName() {
            return facetName;
        }
    }

    private static void resetCachedClientId(UIComponent component) {
        for (UIComponent c = component; c != null; c = c.getParent())
            c.setId(c.getId());
    }

    public static String getFreshClientId(UIComponent component, FacesContext context) {
        resetCachedClientId(component);
        return component.getClientId(context);
    }

}
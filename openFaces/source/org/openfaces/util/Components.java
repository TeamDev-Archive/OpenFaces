/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.util;

import org.openfaces.component.OUIObjectIterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIForm;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class Components {
    private static final String AUTO_ID_PREFIX = "j_id";
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


    public static Object setRequestMapValue(String requestMapKey, Object value) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Map<String, Object> requestMap = externalContext.getRequestMap();
        return requestMap.put(requestMapKey, value);
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
        parent.getChildren().add(component);

        return component;
    }

    public static <T extends UIComponent> T findChildWithClass(UIComponent parent, Class<T> childClass) {
        List<T> childrenWithClass = findChildrenWithClass(parent, childClass, false, false);
        if (childrenWithClass.size() == 0)
            return null;
        return childrenWithClass.get(0);
    }

    public static <T extends UIComponent> T findChildWithClass(UIComponent parent, Class<T> childClass, String childTagName) {
        List<T> childrenWithClass = findChildrenWithClass(parent, childClass, false, false);
        int size = childrenWithClass.size();
        if (size == 0)
            return null;
        if (size > 1)
            throw new RuntimeException("There should be only one " + childTagName + " child under this component: " + parent.getId());
        return childrenWithClass.get(0);
    }

    public static <T extends UIComponent> List<T> findChildrenWithClass(UIComponent parent, Class<T> childClass) {
        return findChildrenWithClass(parent, childClass, false, false);
    }

    public static <T> List<T> findChildrenWithClass(UIComponent parent, Class<? extends T> childClass,
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

    public static boolean isComponentIdSpecified(UIComponent component) {
        String id = component.getId();
        if (id == null)
            return false;
        if (!id.startsWith(AUTO_ID_PREFIX) || id.length() <= AUTO_ID_PREFIX.length())
            return true;

        String suffix = id.substring(AUTO_ID_PREFIX.length());
        try {
            Integer.parseInt(suffix);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /**
     * This method create new output text component and add it as facet to parent component
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
        UIComponent component = createComponent(context, childId, componentType);
        parent.getChildren().add(component);
        return component;
    }

    /**
     * This method add child to parent component at the specified position
     *
     * @param context       {@link javax.faces.context.FacesContext} for the current request
     * @param parent        Method will create child for this component
     * @param componentType The class for child creation
     * @param idSuffix      The suffix identifying the child {@link javax.faces.component.UIComponent} to be returned
     * @param i             index at which the specified child is to be inserted to paranet's child list
     * @return created child
     */
    public static UIComponent createChildComponent(
            FacesContext context, UIComponent parent, String componentType, String idSuffix, int i) {
        String childId = generateIdWithSuffix(parent, idSuffix);
        UIComponent component = createComponent(context, childId, componentType);
        parent.getChildren().add(i, component);
        return component;
    }

    /**
     * This method create components with given name and class and create, if needed, its subcomponents
     *
     * @param context       {@link javax.faces.context.FacesContext} for the current request
     * @param id            The id identifying the {@link javax.faces.component.UIComponent} to be returned
     * @param componentType The component type for which to create and return a new {@link javax.faces.component.UIComponent} instance
     * @return
     */
    public static UIComponent createComponent(FacesContext context, String id, String componentType) {
        Application application = context.getApplication();
        UIComponent component = application.createComponent(componentType);
        component.setId(id);
        return component;
    }

    /**
     * This method create new command button component and add it as facet to parent component
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
        for (Iterator<SelfScheduledAction> actionIterator = postponedActions.iterator(); actionIterator.hasNext();) {
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
    public static <E extends UIComponent> E getOrCreateFacet(
            FacesContext context, UIComponent parent, String componentType, String identifier, Class<E> enforceComponentClass) {
        String id = generateIdWithSuffix(parent, identifier);
        return getOrCreateFacet(context, parent, componentType, identifier, id, enforceComponentClass);
    }

    public static <E extends UIComponent> E getOrCreateFacet(
            FacesContext context, UIComponent parent, String componentType, String facetName, String id, Class<E> enforceComponentClass) {

        UIComponent component = parent.getFacet(facetName);
        if (component != null) {
            if (enforceComponentClass == null || enforceComponentClass.isAssignableFrom(component.getClass())) {
                if (!id.equals(component.getId()))
                    component.setId(id);
                return (E) component;
            }
        }

        component = createComponent(context, id, componentType);
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
        return (E) component;
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

    public static <E extends UIComponent> E getParentWithClass(UIComponent component, Class<E> parentClass) {
        for (UIComponent parent = component.getParent(); parent != null; parent = parent.getParent())
            if (parentClass.isAssignableFrom(parent.getClass()))
                return (E) parent;
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

    public static boolean isImplicitPanel(UIComponent facet) {
        return facet instanceof UIPanel && facet.getAttributes().containsKey("com.sun.faces.facelets.IMPLICIT_PANEL");
    }

    public static UIComponent getFacetOwner(UIComponent facetComponent) {
        UIComponent parent = facetComponent.getParent();
        if (isImplicitPanel(parent)) parent = parent.getParent();
        return parent;
    }

}
package org.openfaces.taglib.internal.filter;

import org.openfaces.component.filter.CompositeFilter;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Natalia Zolochevska
 */
public class CompositeFilterTag extends AbstractComponentTag {

    public String getComponentType() {
        return CompositeFilter.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return CompositeFilter.RENDERER_TYPE;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setPropertyBinding(component, "value");
        setPropertyBinding(component, "noFilterMessage");
        setPropertyBinding(component, "labels");
    }
}
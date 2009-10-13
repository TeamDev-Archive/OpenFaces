package org.openfaces.taglib.internal.input;

import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * Author: Oleg Marshalenko
 * Date: Sep 18, 2009
 * Time: 3:34:47 PM
 */
public class SelectOneRadioItemsTag extends AbstractComponentTag {
    private static final String COMPONENT_TYPE = "org.openfaces.SelectOneRadioItems";

    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setPropertyBinding(component, "value");
    }
}
